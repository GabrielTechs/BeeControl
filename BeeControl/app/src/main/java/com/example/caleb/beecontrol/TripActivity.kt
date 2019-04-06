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

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerViewTrip)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
    }

    fun addtrip(view: View){
        var intent = Intent(this, ControlActivity::class.java)
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
