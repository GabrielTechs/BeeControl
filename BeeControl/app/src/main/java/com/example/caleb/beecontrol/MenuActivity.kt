package com.example.caleb.beecontrol

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



class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    protected val TAG = "MenuActivity"
    lateinit var firebaseAuth: FirebaseAuth
    private var proximityObserver: ProximityObserver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbar)

        firebaseAuth = FirebaseAuth.getInstance()

        val cloudCredentials = EstimoteCloudCredentials("beecontrol-afk", "0e4a52ed6b84786e84c489e8019a9a56")

        this.proximityObserver = ProximityObserverBuilder(applicationContext, cloudCredentials)
                .onError { throwable ->
                    Log.e("app", "proximity observer error: $throwable")
                    null
                }
                .withBalancedPowerMode()
                .build()

        val entryZone = ProximityZoneBuilder()
                .forTag("mint")
                .inNearRange()
                .onEnter { context ->
                    var entryBeacon = context.attachments["zone"]
                    Toast.makeText(applicationContext, "Bienvenido a la $entryBeacon de Supliyeso!", Toast.LENGTH_LONG).show()
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
                    var truckBeacon = context.attachments["zone"]
                    Toast.makeText(applicationContext, "Bienvenido a la $truckBeacon de Supliyeso!", Toast.LENGTH_LONG).show()
                    null
                }
                .onExit {
                    Toast.makeText(applicationContext, "Vuelva pronto!", Toast.LENGTH_LONG).show()
                    null
                }
                .build()

        val officeZone = ProximityZoneBuilder()
                .forTag("ice")
                .inNearRange()
                .onEnter { context ->
                    var officeBeacon = context.attachments["zone"]
                    Toast.makeText(applicationContext, "Bienvenido a la $officeBeacon de Supliyeso!", Toast.LENGTH_LONG).show()
                    null
                }
                .onExit {
                    Toast.makeText(applicationContext, "Vuelva pronto!", Toast.LENGTH_LONG).show()
                    null
                }
                .build()

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        val headerLayout = nav_view.getHeaderView(0)

        val account: TextView = headerLayout.findViewById(R.id.userEmail)

        account.text = firebaseAuth.currentUser?.email.toString()

        nav_view.setNavigationItemSelectedListener(this)

        RequirementsWizardFactory
                .createEstimoteRequirementsWizard()
                .fulfillRequirements(this,
                        // onRequirementsFulfilled
                        {
                            Log.d("app", "requirements fulfilled")
                            proximityObserver!!.startObserving(entryZone, truckZone, officeZone)
                            null
                        },
                        // onRequirementsMissing
                        { requirements ->
                            Log.e("app", "requirements missing: $requirements")
                            null
                        }
                        // onError
                ) { throwable ->
                    Log.e("app", "requirements error: $throwable")
                    null
                }

    }

    override fun onStart() {
        super.onStart()
        val user = firebaseAuth.currentUser

        if(user != null){
            Toast.makeText(applicationContext, user.email, Toast.LENGTH_SHORT).show()
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
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
