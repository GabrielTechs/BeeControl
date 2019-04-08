package com.example.caleb.beecontrol

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore

class ControlActivity : AppCompatActivity() {

    lateinit var txtTripDriverName: EditText
    lateinit var txtTripTitle: EditText
    lateinit var txtTripDescription: EditText
    lateinit var txtTripDate: TextView
    lateinit var txtTripPartingHour: EditText
    lateinit var btnCreateTrip: Button
    private val db = FirebaseFirestore.getInstance()
    private val tripCollectionRef = db.collection("Trips")

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)

        txtTripDriverName = findViewById(R.id.txtTripDriverName)
        txtTripTitle = findViewById(R.id.txtTripTitle)
        txtTripDescription = findViewById(R.id.txtTripDescription)
        txtTripDate = findViewById(R.id.txtTripDate)
        txtTripPartingHour = findViewById(R.id.txtTripPartingHour)
        btnCreateTrip = findViewById(R.id.btnCreateTrip)

        txtTripDate.setOnClickListener{
            datePicker()
        }

        btnCreateTrip.setOnClickListener {
            createTrip()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun datePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
            txtTripDate.text = "$mDay/$mMonth/$mYear"
        }, year, month, day)
        dpd.show()
    }

    fun createTrip(){
        var tripTitle: String = txtTripTitle.text.toString()
        var tripDriverName: String = txtTripDriverName.text.toString()
        var tripDate: String = txtTripDate.text.toString()

    }
}
