package com.example.caleb.beecontrol

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class TripActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val tripbookRef = db.collection("tripbook")

    private var adapter: TripAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val query = tripbookRef.orderBy("priority", Query.Direction.DESCENDING)

        val options = FirestoreRecyclerOptions.Builder<Trip>()
                .setQuery(query, Trip::class.java)
                .build()

        adapter = TripAdapter(options)

        val recyclerview = findViewById<RecyclerView>(R.id.recycleViewTrip)
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
}
