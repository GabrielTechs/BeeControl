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
import com.google.firebase.firestore.SetOptions
import java.text.SimpleDateFormat

class NewTripActivity : AppCompatActivity() {

    lateinit var txtTripDriverName: EditText
    lateinit var txtTripTitle: EditText
    lateinit var txtTripDescription: EditText
    lateinit var btnCreateTrip: Button
    private val db = FirebaseFirestore.getInstance()
    private val path = db.collection("TripID").document("Counter")
    private val tripCollectionRef = db.collection("Trips")

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newtrip)

        txtTripDriverName = findViewById(R.id.txtTripDriverName)
        txtTripTitle = findViewById(R.id.txtTripTitle)
        txtTripDescription = findViewById(R.id.txtTripDescription)
        btnCreateTrip = findViewById(R.id.btnCreateTrip)


        btnCreateTrip.setOnClickListener {
            createTrip()
        }
    }

    fun createTrip(){
        val tripTitle: String = txtTripTitle.text.toString()
        val tripDriverName: String = txtTripDriverName.text.toString()
        val tripDescription: String = txtTripDescription.text.toString()
        val c = java.util.Calendar.getInstance().time
        val df = SimpleDateFormat("dd-MM-yyyy")
        val tf = SimpleDateFormat("HH:mm")
        val tripDate =  df.format(c).toString()
        val tripCreatedHour = tf.format(c).toString()

        if (tripTitle.trim().isEmpty() || tripDescription.trim().isEmpty() || tripDriverName.isEmpty()){
            toast("Llene los campos restantes", Toast.LENGTH_SHORT)
            return
        }

        path.get().addOnSuccessListener { document ->
            val id = document["Id"].toString().toInt()
            tripCollectionRef.add(Trip(tripDate, tripTitle, tripDriverName, tripCreatedHour, tripDescription, id))
            tripIdIncrement()
        }

        toast("Viaje agregado!", Toast.LENGTH_SHORT)
        val intent = Intent(this, TripActivity::class.java)
        startActivity(intent)
    }

    fun tripIdIncrement() {
        path.get().addOnSuccessListener { document ->
            var count = document["Id"]
            count = count.toString().toInt() + 1
            path.update("Id", count)
        }
    }

    fun back(view: View){
        startActivity(Intent(this, TripActivity::class.java))
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
