package com.example.caleb.beecontrol

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
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
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.RemoteViews
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query


class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    protected val TAG = "MenuActivity"
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var firebaseAuth: FirebaseAuth
    var userRef = db.collection("user")

    private val assistanceRef = db.collection("Assistance")
    private var proximityObserverHandler: ProximityObserver.Handler? = null
    lateinit var email: String
    lateinit var accountRef: DocumentReference
    var tripRef = db.collection("Trips")
    private lateinit var exitHandler: Handler
    private var adapter: AssistanceAdapter? = null

    var query = assistanceRef.orderBy("assistDate", Query.Direction.DESCENDING)

    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelId = "com.example.caleb.beecontrol"
    private val description = "Notification"
    var notification = ""

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbar)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        firebaseAuth = FirebaseAuth.getInstance()

        email = firebaseAuth.currentUser?.email.toString()
        accountRef = userRef.document(email)

        accountRef.get().addOnSuccessListener { document ->
            val employee = document.toObject(Employee::class.java)
            val admin = employee?.isAdmin

            if (admin!!) {
                showAdmingrp()
            }
        }

        query = query.whereEqualTo("employeeEmail", email)
        setUpRecyclerView(query)
        adapter?.startListening()

        exitHandler = Handler()

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

            val workZone = ProximityZoneBuilder()
                    .forTag("coconut")
                    .inCustomRange(2.0)
                    .onEnter { context ->
                        accountRef.update("exitChecker", false)
                        //val truckBeacon = context.attachments["zone"]
                        //toast("Bienvenido a la $truckBeacon de Supliyeso!", Toast.LENGTH_LONG)
                        onentrychecker(email)
                    }
                    .onExit {
                        accountRef.update("exitChecker", true)
                        notifications("Si no esta en un viaje se le colocará una ausencia en 10 segundos")
                        //toast("Si no esta en un viaje se le colocará una ausencia en 10 segundos")
                        onexitchecker(email)
                    }
                    .build()

            RequirementsWizardFactory
                    .createEstimoteRequirementsWizard()
                    .fulfillRequirements(this,
                            // onRequirementsFulfilled
                            {
                                Log.d("app", "requirements fulfilled")
                                proximityObserverHandler = proximityObserver.startObserving(workZone)
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

        val pullToRefresh = findViewById<SwipeRefreshLayout>(R.id.pullToRefresh)
        pullToRefresh.setOnRefreshListener {
            recreate()
            pullToRefresh.isRefreshing = false
        }
    }


    private fun setUpRecyclerView(query: Query) {
        val options = FirestoreRecyclerOptions.Builder<Assistance>()
                .setQuery(query, Assistance::class.java)
                .build()

        adapter = AssistanceAdapter(options)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerViewMenu)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }

    fun onentrychecker(email: String) {
        val docRef = userRef.document(email)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val employeeName = document.toObject(Employee::class.java)?.name + " " + document.toObject(Employee::class.java)?.lastName
                        var status = "Presente"
                        docRef.update("status", status)
                        val c = Calendar.getInstance().time
                        val df = SimpleDateFormat("dd-MM-yyyy")
                        val tf = SimpleDateFormat("HH:mm")
                        val assistTime = tf.format(c).toString()
                        val assistDate = df.format(c).toString()

                        if (document["onTrip"] == true) {
                            docRef.update("onTrip", false)
                            tripRef.get()
                                    .addOnSuccessListener { resulttrip ->
                                        if (resulttrip != null) {
                                            for (document in resulttrip) {
                                                if (document["tripDriverEmail"] == email && document["tripEntryHour"] == null && document["tripDate"] == assistDate) {
                                                    tripRef.document(document.id).update("tripEntryHour", assistTime)
                                                }
                                            }
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.d(TAG, "Error getting trip documents.", exception)
                                    }
                        } else {
                            assistanceRef.get()
                                    .addOnSuccessListener { result ->
                                        if (result != null) {
                                            var assisted = false

                                            for (document in result) {
                                                document.toObject(Assistance::class.java)
                                                if (document["employeeName"] == employeeName && document["assistDate"] == assistDate) {
                                                    notifications("Debe dirigirse a recursos humanos para una nueva asistencia.")
                                                    //toast("Debe dirigirse a recursos humanos para una nueva asistencia.", Toast.LENGTH_LONG)
                                                    assisted = true
                                                }
                                            }

                                            if (!assisted) {

                                                if (assistTime >= "08:00") {
                                                    status = "Tarde"
                                                }
                                                assistanceRef.add(Assistance(employeeName, email, status, assistDate, assistTime))
                                                notifications("$employeeName agregado a la lista!")
                                                //toast("$employeeName agregado a la lista!", Toast.LENGTH_LONG)
                                            }
                                        } else {
                                            notifications("No documents!")
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.d(TAG, "Error getting documents: ", exception)
                                    }
                        }
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
    }

    fun onexitchecker(email: String) {
        val accountRef = userRef.document(email)
        accountRef.get()
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
                                                    accountRef.update("status", "En viaje")
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
                                            exitHandler.postDelayed({
                                                accountRef.get()
                                                        .addOnSuccessListener { resultuser ->

                                                            if (resultuser["exitChecker"] == true) {
                                                                var exitchecker = false

                                                                for (document in resultassis) {
                                                                    if (document["employeeEmail"] == email && document["assistDate"] == assistDate && document["status"] == status) {
                                                                        notifications("Debe dirigirse a recursos humanos para una nueva ausencia.")
                                                                        //toast("Debe dirigirse a recursos humanos para una nueva ausencia.")
                                                                        exitchecker = true
                                                                    }
                                                                }
                                                                if (!exitchecker && exitTime <= "17:00") {
                                                                    assistanceRef.add(Assistance(employeeName, email, status, assistDate, exitTime))
                                                                    accountRef.update("status", status)
                                                                }
                                                                if (!exitchecker && exitTime > "17:00") {
                                                                    //Se pueden poner aqui condiciones de horas extra.
                                                                    assistanceRef.add(Assistance(employeeName, email, status, assistDate, exitTime))
                                                                    accountRef.update("status", status)
                                                                    notifications("Tenga buen resto del dia")
                                                                    // toast("Tenga buen resto del dia")
                                                                }
                                                            } else {
                                                                notifications("Gracia por regresar")
                                                                //toast("Gracia por regresar")
                                                            }
                                                        }.addOnFailureListener { exception ->
                                                            Log.d(TAG, "Error getting account documents.", exception)
                                                        }
                                            }, 10000)
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
            R.id.txtPersonalTrip -> {
                startActivity(Intent(this, EmployeesTripActivity::class.java))
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
            R.id.txtAsistencia -> {
                startActivity(Intent(this, AssistanceActivity::class.java))
            }
            R.id.txtAddAssistance -> {
                startActivity(Intent(this, NewAssistanceActivity::class.java))
            }
            R.id.txtTrips -> {
                // Handle the camera action
                startActivity(Intent(this, TripActivity::class.java))
            }
            R.id.txtAddTrip -> {
                startActivity(Intent(this, NewTripActivity::class.java))
            }
            R.id.txtRecursosH -> {
                startActivity(Intent(this, EmployeesActivity::class.java))
            }
            R.id.txtSupport -> {
                startActivity(Intent(this, SupportMessagesActivity::class.java))
            }
            R.id.txtRegister -> {
                startActivity(Intent(this, RegisterActivity::class.java))
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

    fun notifications(notification: String){
        val intent = Intent(this,LauncherActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val contentView = RemoteViews(packageName,R.layout.notifications)
        contentView.setTextViewText(R.id.tv_title,"BeeControl")
        contentView.setTextViewText(R.id.tv_content,notification)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId,description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this,channelId)
                    .setContent(contentView)
                    .setSmallIcon(R.drawable.icon)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.icon))
                    .setContentIntent(pendingIntent)
        }else{

            builder = Notification.Builder(this)
                    .setContent(contentView)
                    .setSmallIcon(R.drawable.icon)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.icon))
                    .setContentIntent(pendingIntent)
        }
        notificationManager.notify(1234,builder.build())

    }

    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_LONG) {
        val toast = Toast.makeText(this, message, duration)
        toast.setGravity(Gravity.TOP, 0, 250)
        val view = toast.view
        val text = view.findViewById(android.R.id.message) as TextView
        view.setBackgroundResource(R.drawable.login_toast)
        text.gravity = Gravity.CENTER
        text.setBackgroundColor(Color.TRANSPARENT)
        text.setTextColor(Color.BLACK)
        text.textSize = 16F
        toast.show()
    }
}
