package com.example.caleb.beecontrol

import android.content.Intent
import android.os.Bundle
import android.os.RemoteException
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.app_bar_menu.*
import org.altbeacon.beacon.*
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconManager

class MenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, BeaconConsumer,
        MonitorNotifier {

    protected val TAG = "MenuActivity"
    private val PERMISSION_REQUEST_COARSE_LOCATION = 1
    private val REQUEST_ENABLE_BLUETOOTH = 1
    private val DEFAULT_SCAN_PERIOD_MS = 6000L
    private val ALL_BEACONS_REGION = "AllBeaconsRegion"
    lateinit private var mBeaconManager: BeaconManager
    lateinit private var mRegion: Region

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onResume(){
        super.onResume()
        mBeaconManager = BeaconManager.getInstanceForApplication(this)
        // Detect the main Eddystone-UID frame:
        mBeaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT))
        mBeaconManager.bind(this)
    }

    override fun onBeaconServiceConnect() {
        val myBeaconNamespaceId = Identifier.parse("1ef930cc1f0e886be663")
        val myBeaconInstanceId = Identifier.parse("123456780099")
        val region = Region("my-beacon-region", myBeaconNamespaceId, myBeaconInstanceId, null)
        mBeaconManager.addMonitorNotifier(this)
        try {
            mBeaconManager.startMonitoringBeaconsInRegion(region)
        }
        catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    override fun didDetermineStateForRegion(p0: Int, p1: Region?) {

    }

    override fun didEnterRegion(p0: Region?) {
        Toast.makeText(this, "I detected a beacon in the region with namespace id " + p0!!.id1 +
                " and instance id: " + p0.id2, Toast.LENGTH_SHORT).show()
    }

    override fun didExitRegion(p0: Region?) {

    }

    public override fun onPause() {
        super.onPause()
        mBeaconManager.unbind(this)
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
            R.id.txtSalir ->
            {
                startActivity(Intent(this, LoginActivity::class.java))

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
