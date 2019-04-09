package com.example.caleb.beecontrol

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView


class TripspecActivity : AppCompatActivity() {

    lateinit var txtTripDriverName: TextView
    lateinit var txtTripTitle: TextView
    lateinit var txtTripDate: TextView
    lateinit var txtTripPartingHour: TextView
    lateinit var txtTripEntryHour: TextView
    lateinit var txtTripDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tripspec)

        txtTripDriverName = findViewById(R.id.txtTripDriverName)
        txtTripTitle = findViewById(R.id.txtTripTitle)
        txtTripDate = findViewById(R.id.txtTripDate)
        txtTripPartingHour = findViewById(R.id.txtTripPartingHour)
        txtTripEntryHour = findViewById(R.id.txtTripEntryHour)
        txtTripDescription = findViewById(R.id.txtTripDescription)

        val trip = intent.extras!!

        txtTripDriverName.text = trip.getString("tripDriverName")
        txtTripTitle.text = trip.getString("tripTitle")
        txtTripDate.text = trip.getString("tripDate")
        txtTripPartingHour.text = trip.getString("tripPartingHour")
        txtTripEntryHour.text = trip.getString("tripEntryHour")
        txtTripDescription.text = trip.getString("tripDescription")

    }
}