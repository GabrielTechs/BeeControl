package com.example.caleb.beecontrol

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials
import com.estimote.proximity_sdk.api.ProximityObserverBuilder
import com.estimote.proximity_sdk.api.ProximityZoneBuilder

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

class TripActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val tripbookRef = db.collection("Trips")

    private var adapter: TripAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)
        setUpRecyclerView()

        val cloudCredentials = EstimoteCloudCredentials("beecontrol-afk", "0e4a52ed6b84786e84c489e8019a9a56")
        val proximityObserver = ProximityObserverBuilder(applicationContext, cloudCredentials)
                .withBalancedPowerMode()
                .onError { /* handle errors here */ }
                .build()

        val zone = ProximityZoneBuilder()
                .forTag("esdras-mateo-s-proximity-f-b7w")
                .inFarRange()
                .onEnter { Toast.makeText(applicationContext, "Entraste a la zona", Toast.LENGTH_LONG).show()}
                .onExit { Toast.makeText(applicationContext, "Saliste a la zona", Toast.LENGTH_LONG).show()}
                .onContextChange {/* do something here */}
                .build()

        RequirementsWizardFactory
                .createEstimoteRequirementsWizard()
                .fulfillRequirements(this,
                        // onRequirementsFulfilled
                        {
                            Log.d("app", "requirements fulfilled")
                            proximityObserver.startObserving(zone)
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

    private fun setUpRecyclerView() {
        val query = tripbookRef.whereGreaterThan("tripId", 0)

        val options = FirestoreRecyclerOptions.Builder<Trip>()
                .setQuery(query, Trip::class.java)
                .build()

        adapter = TripAdapter(options)

        val tripRecylerView = findViewById<RecyclerView>(R.id.recyclerViewTrip)
        tripRecylerView.setHasFixedSize(true)
        tripRecylerView.layoutManager = LinearLayoutManager(this)
        tripRecylerView.adapter = adapter

        adapter!!.setOnItemClickListener { documentSnapshot, position ->
            val trip = documentSnapshot.toObject(Trip::class.java)

            val intent = Intent(this, TripspecActivity::class.java)

            intent.putExtra("tripDriverName", trip!!.getTripDriverName())
            intent.putExtra("tripTitle", trip.getTripTitle())
            intent.putExtra("tripDate", trip.getTripDate())
            intent.putExtra("tripPartingHour", trip.getTripPartingHour())
            intent.putExtra("tripEntryHour", trip.getTripEntryHour())
            intent.putExtra("tripDescription", trip.getTripDescription())

            startActivity(intent)
        }
    }
    fun back(view:View){
        startActivity(Intent(this, MenuActivity::class.java))
    }
    fun addtrip(view: View){
        val intent = Intent(this, NewtripActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }
}
