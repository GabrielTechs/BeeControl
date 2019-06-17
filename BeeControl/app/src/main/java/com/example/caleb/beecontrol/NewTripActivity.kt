package com.example.caleb.beecontrol

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class NewTripActivity : AppCompatActivity() {

    lateinit var txtTripDriverName: EditText
    lateinit var txtTripTitle: EditText
    lateinit var txtTripDescription: EditText
    lateinit var txtTripDate: TextView
    lateinit var btnCreateTrip: Button
    private val db = FirebaseFirestore.getInstance()
    private val tripCollectionRef = db.collection("Trips")

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newtrip)

        txtTripDriverName = findViewById(R.id.txtTripDriverName)
        txtTripTitle = findViewById(R.id.txtTripTitle)
        txtTripDescription = findViewById(R.id.txtTripDescription)
        txtTripDate = findViewById(R.id.txtTripDate)
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
        }, year, month + 1, day)
        dpd.show()
    }

    fun createTrip(){
        var tripTitle: String = txtTripTitle.text.toString()
        var tripDriverName: String = txtTripDriverName.text.toString()
        var tripDate: String = txtTripDate.text.toString()
        var tripDescription: String = txtTripDescription.text.toString()

        if (tripTitle.trim().isEmpty() || tripDescription.trim().isEmpty() || tripDate.isEmpty() ||
                tripDriverName.isEmpty()){
            toast("Llene los campos restantes", Toast.LENGTH_SHORT)
            return
        }

        tripCollectionRef.add(Trip(tripDate, tripTitle, tripDriverName, "", tripDescription, 10))

        toast("Viaje agregado!", Toast.LENGTH_SHORT)
        var intent = Intent(this, TripActivity::class.java)
        startActivity(intent)
    }

    fun back(view: View){
        //startActivity(Intent(this, TripActivity::class.java))
        onBackPressed()
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
