package com.example.caleb.beecontrol

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials
import com.estimote.proximity_sdk.api.ProximityObserver
import com.estimote.proximity_sdk.api.ProximityObserverBuilder
import com.estimote.proximity_sdk.api.ProximityZoneBuilder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.app_bar_menu.*
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_assistance.*
import kotlinx.android.synthetic.main.list_trip.*
import java.text.SimpleDateFormat
import java.util.*


class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    protected val TAG = "MenuActivity"
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var firebaseAuth: FirebaseAuth
    var userRef = db.collection("user")
    private val assistanceRef = db.collection("Assistance")
    private var proximityObserverHandler: ProximityObserver.Handler? = null
    //val email = firebaseAuth.currentUser?.email.toString()
    var tripRef = db.collection("Trips")


    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbar)

        firebaseAuth = FirebaseAuth.getInstance()

        val email = firebaseAuth.currentUser?.email.toString()

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        val headerLayout = nav_view.getHeaderView(0)

        val account: TextView = headerLayout.findViewById(R.id.userEmail)

        account.text = email

        nav_view.setNavigationItemSelectedListener(this)

        if (proximityObserverHandler == null) {

            val cloudCredentials = EstimoteCloudCredentials("beecontrol-afk", "0e4a52ed6b84786e84c489e8019a9a56")

            val proximityObserver = ProximityObserverBuilder(applicationContext, cloudCredentials)
                    .onError { throwable ->
                        Log.e("app", "proximity observer error: $throwable")
                        null
                    }
                    .withBalancedPowerMode()
                    .build()

            val entryZone = ProximityZoneBuilder()
                    .forTag("mint")
                    .inCustomRange(1.0)
                    .onEnter { context ->
                        assistance(email)
                        null
                    }
                    .onExit {
                        toast("Vuelva pronto!", Toast.LENGTH_LONG)
                        onTripExit(email)
                        null
                    }
                    .build()

            val truckZone = ProximityZoneBuilder()
                    .forTag("coconut")
                    .inNearRange()
                    .onEnter { context ->
                        val truckBeacon = context.attachments["zone"]
                        toast("Bienvenido a la $truckBeacon de Supliyeso!", Toast.LENGTH_LONG)
                    }
                    .onExit {
                        onTripExit(email)
                        //toast("Vuelva pronto!", Toast.LENGTH_LONG)
                    }
                    .build()

            val officeZone = ProximityZoneBuilder()
                    .forTag("ice")
                    .inNearRange()
                    .onEnter { context ->
                        val officeBeacon = context.attachments["zone"]
                        toast("Bienvenido a la $officeBeacon de Supliyeso!", Toast.LENGTH_LONG)
                    }
                    .onExit {
                        toast("Vuelva pronto!", Toast.LENGTH_LONG)
                    }
                    .build()

            val hrZone = ProximityZoneBuilder()
                    .forTag("blueberry")
                    .inNearRange()
                    .onEnter { context ->
                        val hrBeacon = context.attachments["zone"]
                        toast("Bienvenido a la $hrBeacon de Supliyeso!", Toast.LENGTH_LONG)
                    }
                    .onExit {
                        toast("Vuelva pronto!", Toast.LENGTH_LONG)
                    }
                    .build()

            RequirementsWizardFactory
                    .createEstimoteRequirementsWizard()
                    .fulfillRequirements(this,
                            // onRequirementsFulfilled
                            {
                                Log.d("app", "requirements fulfilled")
                                proximityObserverHandler = proximityObserver.startObserving(entryZone, truckZone, officeZone, hrZone)
                            },
                            // onRequirementsMissing
                            { requirements ->
                                Log.e("app", "requirements missing: $requirements")
                            }
                            // onError
                    ) { throwable ->
                        Log.e("app", "requirements error: $throwable")
                    }
        }

        val docRef = userRef.document(email)
        docRef.get().addOnSuccessListener { document ->
            var admin = document.toObject(Employee::class.java)?.isAdmin
            if (admin!!) {
                showAdmingrp()
            }
        }
    }

    fun assistance(email: String) {
        val docRef = userRef.document(email)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val employeeName = document.toObject(Employee::class.java)?.name + " " + document.toObject(Employee::class.java)?.lastName
                        var status = "Presente"
                        val c = Calendar.getInstance().time
                        val df = SimpleDateFormat("dd-MM-yyyy")
                        val tf = SimpleDateFormat("HH:mm")
                        val assistTime = tf.format(c).toString()
                        val assistDate = df.format(c).toString()


                        assistanceRef.get()
                                .addOnSuccessListener { result ->
                                    if (result != null) {
                                        var assisted = false

                                        for (document in result) {
                                            document.toObject(Assistance::class.java)
                                            if (document["employeeName"] == employeeName && document["assistDate"] == assistDate) {
                                                toast("Ya estás asistido!", Toast.LENGTH_LONG)
                                                assisted = true
                                            }
                                        }

                                        if (!assisted) {

                                            if (assistTime > "08:00") {
                                                status = "Tarde"
                                            }

                                            assistanceRef.add(Assistance(employeeName, email, status, assistDate, assistTime))
                                            toast("$employeeName agregado a la lista!", Toast.LENGTH_LONG)
                                        }
                                    } else {
                                        toast("No documents!", Toast.LENGTH_LONG)
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.d(TAG, "Error getting documents: ", exception)
                                }
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
    }

    fun onTripExit(email: String) {
        val account = userRef.document(email)
        account.get()
                .addOnSuccessListener { resultuser ->
                    if (resultuser != null) {

                        val employeeName = resultuser.toObject(Employee::class.java)?.name + " " + resultuser.toObject(Employee::class.java)?.lastName
                        val c = Calendar.getInstance().time
                        val df = SimpleDateFormat("dd-MM-yyyy")
                        val assistDate = df.format(c).toString()
                        val tf = SimpleDateFormat("HH:mm")
                        val exitTime = tf.format(c).toString()
                        var status = "Ausente"

                        if (resultuser["onTrip"] == true) {

                            tripRef.get()
                                    .addOnSuccessListener { resulttrip ->
                                        if (resulttrip != null) {

                                            for (document in resulttrip) {
                                                if (document["tripDriverEmail"] == email && document["tripPartingHour"] == null && document["tripDate"] == assistDate) {
                                                    tripRef.document(document.id).update("tripPartingHour", exitTime)
                                                }
                                            }

                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.d(TAG, "Error getting trip documents.", exception)
                                    }
                        } else {
                            assistanceRef.get()
                                    .addOnSuccessListener { resultassis ->
                                        if (resultassis != null) {
                                            var exitchecker = false

                                            for (document in resultassis) {
                                                if (document["employeeEmail"] == email && document["assistDate"] == assistDate && document["status"] == status) {
                                                    toast("Aqui cambiar la hora del ausente anterior.")
                                                    exitchecker = true
                                                }
                                            }
                                            if(!exitchecker){
                                                assistanceRef.add(Assistance(employeeName, email, status, assistDate, exitTime))
                                            }
                                            if(exitTime > "17:00"){
                                                //Se pueden poner aqui condiciones de horas extra.
                                                toast("Tenga buen resto del dia")
                                            }
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.d(TAG, "Error getting assistance documents.", exception)
                                    }
                        }
                    }
                }.addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting account documents.", exception)
                }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.txtControla -> {
                // Handle the camera action
                startActivity(Intent(this, TripActivity::class.java))
            }
            R.id.txtAsistencia -> {
                startActivity(Intent(this, AssistanceActivity::class.java))
            }
            R.id.txtConfi -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            R.id.txtAyuda -> {
                startActivity(Intent(this, SupportActivity::class.java))
            }
            R.id.txtSalir -> {
                proximityObserverHandler?.stop()
                firebaseAuth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
            }
            //Grupo de Administrador.
            R.id.txtRecursosH -> {
                startActivity(Intent(this, EmployeesActivity::class.java))
            }
            R.id.txtAddAssistance -> {
                startActivity(Intent(this, NewAssistanceActivity::class.java))
            }
            R.id.txtAddTrip -> {
                startActivity(Intent(this, NewTripActivity::class.java))
            }
            R.id.txtSupport -> {
                startActivity(Intent(this, SupportMessagesActivity::class.java))
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showAdmingrp() {
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val navgroup = navigationView.menu
        navgroup.setGroupVisible(R.id.admingrp, true)
    }

    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        val toast = Toast.makeText(this, message, duration)
        toast.setGravity(Gravity.TOP, 0, 250)
        val view = toast.view
        val text = view.findViewById(android.R.id.message) as TextView
        view.setBackgroundResource(R.drawable.login_toast)
        text.gravity = Gravity.CENTER
        text.setBackgroundColor(Color.TRANSPARENT)
        text.setTextColor(Color.BLACK)
        text.textSize = 20F
        toast.show()
    }
}
