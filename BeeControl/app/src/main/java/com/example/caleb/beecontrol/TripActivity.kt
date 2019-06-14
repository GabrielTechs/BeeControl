package com.example.caleb.beecontrol

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TripActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    lateinit var firebaseAuth: FirebaseAuth
    var userRef = db.collection("user")
    private val tripbookRef = db.collection("Trips")
    private var adapter: TripAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)
        setUpRecyclerView()

        firebaseAuth = FirebaseAuth.getInstance()
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

            val intent = Intent(this, TripDetailActivity::class.java)

            intent.putExtra("tripDriverName", trip!!.getTripDriverName())
            intent.putExtra("tripTitle", trip.getTripTitle())
            intent.putExtra("tripDate", trip.getTripDate())
            intent.putExtra("tripPartingHour", trip.getTripPartingHour())
            intent.putExtra("tripEntryHour", trip.getTripEntryHour())
            intent.putExtra("tripDescription", trip.getTripDescription())

            startActivity(intent)
        }
    }

    fun back(view: View) {
        startActivity(Intent(this, MenuActivity::class.java))
    }

    fun addtrip(view: View) {

        val email = firebaseAuth.currentUser?.email.toString()
        val docRef = userRef.document(email)
        docRef.get().addOnSuccessListener { document ->
            var admin = document.toObject(Employee::class.java)?.isAdmin
            if (admin!!) {
                val intent = Intent(this, NewTripActivity::class.java)
                startActivity(intent)
            } else {
                toast("No eres admin!", Toast.LENGTH_LONG)
            }
        }
        //val intent = Intent(this, NewTripActivity::class.java)
        //startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }

    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        val toast = Toast.makeText(this, message, duration)
        toast.setGravity(Gravity.TOP,0,200)
        val view = toast.view
        val text = view.findViewById(android.R.id.message) as TextView
        view.setBackgroundResource(R.drawable.login_toast)
        text.gravity = Gravity.CENTER
        text.setBackgroundColor(Color.WHITE)
        text.setTextColor(Color.BLUE)
        text.textSize = 20F
        toast.show()
    }
}
