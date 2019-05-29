package com.example.caleb.beecontrol

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

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
            val path = documentSnapshot.reference.path

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
        startActivity(Intent(this, Control::class.java))
    }
    fun addtrip(view: View){
        var intent = Intent(this, NewtripActivity::class.java)
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
