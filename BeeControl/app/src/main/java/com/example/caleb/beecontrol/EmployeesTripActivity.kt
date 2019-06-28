package com.example.caleb.beecontrol

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.TextView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class EmployeesTripActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    lateinit var firebaseAuth: FirebaseAuth
    var userRef = db.collection("user")
    private val tripbookRef = db.collection("Trips")
    private var adapter: TripAdapter? = null
    lateinit var txtTripDate: TextView
    lateinit var tripsearch: SearchView
    lateinit var email: String


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employees_trip)

        email = firebaseAuth.currentUser?.email.toString()
        val query = tripbookRef.whereGreaterThan("tripId", 0)
                .whereEqualTo("tripDriverEmail", email)
        setUpRecyclerView(query)

        firebaseAuth = FirebaseAuth.getInstance()

        txtTripDate = findViewById(R.id.txtTripDate)

        txtTripDate.setOnClickListener{
            datePicker()
        }

        val pullToRefresh = findViewById<SwipeRefreshLayout>(R.id.pullToRefresh)
        pullToRefresh.setOnRefreshListener {
            recreate()
            pullToRefresh.isRefreshing = false
        }
    }

    private fun setUpRecyclerView(query: Query) {
        //val query = tripbookRef.whereGreaterThan("tripId", 0)

        val options = FirestoreRecyclerOptions.Builder<Trip>()
                .setQuery(query, Trip::class.java)
                .build()

        adapter = TripAdapter(options)

        val tripRecylerView = findViewById<RecyclerView>(R.id.recyclerViewEmployeeTrip)
        tripRecylerView.setHasFixedSize(true)
        tripRecylerView.layoutManager = LinearLayoutManager(this)
        tripRecylerView.adapter = adapter

        adapter!!.setOnItemClickListener { documentSnapshot, position ->
            val trip = documentSnapshot.toObject(Trip::class.java)

            val intent = Intent(this, TripDetailActivity::class.java)

            intent.putExtra("tripDriverName", trip!!.tripDriverName)
            intent.putExtra("tripTitle", trip.tripTitle)
            intent.putExtra("tripDate", trip.tripDate)
            intent.putExtra("tripCreatedHour", trip.tripCreatedHour)
            intent.putExtra("tripPartingHour", trip.tripPartingHour)
            intent.putExtra("tripEntryHour", trip.tripEntryHour)
            intent.putExtra("tripDescription", trip.tripDescription)

            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun datePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
            var realmonth = mMonth+1
            if (realmonth > 9){
                txtTripDate.text = "$mDay-$realmonth-$mYear"
            }else{
                txtTripDate.text = "$mDay-0$realmonth-$mYear"
            }
            val qdate = txtTripDate.text.toString()
            val querydate = tripbookRef.whereGreaterThan("tripId", 0)
                    .whereEqualTo("tripDate", qdate)
                    .whereEqualTo("tripDriverEmail", email)
            setUpRecyclerView(querydate)
            adapter?.startListening()
        }, year, month + 1, day)
        dpd.show()
    }

    fun back(view: View) {
        startActivity(Intent(this, MenuActivity::class.java))
    }

}
