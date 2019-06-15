package com.example.caleb.beecontrol

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import java.text.SimpleDateFormat
import java.util.*


class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    protected val TAG = "MenuActivity"
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var firebaseAuth: FirebaseAuth
    var userRef = db.collection("user")
    private val assistanceRef = db.collection("Assistance")
    private var proximityObserverHandler: ProximityObserver.Handler? = null

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

        if(proximityObserverHandler == null){

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
                        Toast.makeText(applicationContext, "Vuelva pronto!", Toast.LENGTH_LONG).show()
                        null
                    }
                    .build()

            val truckZone = ProximityZoneBuilder()
                    .forTag("coconut")
                    .inNearRange()
                    .onEnter { context ->
                        val truckBeacon = context.attachments["zone"]
                        Toast.makeText(applicationContext, "Bienvenido a la $truckBeacon de Supliyeso!", Toast.LENGTH_LONG).show()
                    }
                    .onExit {
                        Toast.makeText(applicationContext, "Vuelva pronto!", Toast.LENGTH_LONG).show()
                    }
                    .build()

            val officeZone = ProximityZoneBuilder()
                    .forTag("ice")
                    .inNearRange()
                    .onEnter { context ->
                        val officeBeacon = context.attachments["zone"]
                        Toast.makeText(applicationContext, "Bienvenido a la $officeBeacon de Supliyeso!", Toast.LENGTH_LONG).show()
                    }
                    .onExit {
                        Toast.makeText(applicationContext, "Vuelva pronto!", Toast.LENGTH_LONG).show()
                    }
                    .build()

            val hrZone = ProximityZoneBuilder()
                    .forTag("blueberry")
                    .inNearRange()
                    .onEnter { context ->
                        val hrBeacon = context.attachments["zone"]
                        Toast.makeText(applicationContext, "Bienvenido a la $hrBeacon de Supliyeso!", Toast.LENGTH_LONG).show()
                    }
                    .onExit {
                        Toast.makeText(applicationContext, "Vuelva pronto!", Toast.LENGTH_LONG).show()
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
    }

    fun assistance(email: String){
        val docRef = userRef.document(email)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val employeeName = document.toObject(Employee::class.java)?.name + " " +  document.toObject(Employee::class.java)?.lastName
                        var status = "Presente"
                        val c = Calendar.getInstance().time
                        val df = SimpleDateFormat("dd-MM-yyyy")
                        val tf = SimpleDateFormat("HH:mm")
                        val assistTime = tf.format(c).toString()
                        val assistDate = df.format(c).toString()

                        assistanceRef.get()
                                .addOnSuccessListener { result ->
                                    if(result != null){
                                        var assisted = false

                                        for (document in result) {
                                            document.toObject(Assistance::class.java)
                                            if(document["employeeName"] == employeeName && document["assistDate"] == assistDate){
                                                Toast.makeText(applicationContext, "Ya estas asistido!", Toast.LENGTH_LONG).show()
                                                assisted = true
                                            }
                                        }

                                        if(!assisted){

                                            if(assistTime > "08:00"){
                                                status = "Tarde"
                                            }

                                            assistanceRef.add(Assistance(employeeName, status, assistDate))
                                            Toast.makeText(applicationContext, "$employeeName agregado a la lista!", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                    else{
                                        Toast.makeText(applicationContext, "No documents!", Toast.LENGTH_LONG).show()
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.d(TAG, "Error getting documents: ", exception)
                                }
                    }
                    else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
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
        menuInflater.inflate(R.menu.menu, menu)
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
            R.id.txtControla-> {
                // Handle the camera action
                startActivity(Intent(this, TripActivity::class.java))
            }
            R.id.txtAsistencia -> {
                startActivity(Intent(this, AssistanceActivity::class.java))
            }
            R.id.txtRecursosH -> {
                startActivity(Intent(this, EmployeesActivity::class.java))
            }
            R.id.txtConfi-> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            R.id.txtAyuda ->
            {
                startActivity(Intent(this, SupportActivity::class.java))
            }
            R.id.txtSalir ->
            {
                proximityObserverHandler?.stop()
                firebaseAuth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
