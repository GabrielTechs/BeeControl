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
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_assistance.*
import java.text.SimpleDateFormat

class NewTripActivity : AppCompatActivity() {

    lateinit var spinTripEmployeeName: Spinner
    lateinit var txtTripTitle: EditText
    lateinit var txtTripEmployeeEmail: TextView
    lateinit var txtTripDescription: EditText
    lateinit var btnCreateTrip: Button
    private val db = FirebaseFirestore.getInstance()
    private val path = db.collection("TripID").document("Counter")
    private val tripCollectionRef = db.collection("Trips")
    private val userRef = db.collection("user")

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newtrip)

        spinTripEmployeeName = findViewById(R.id.spinTripEmployeeName)
        txtTripEmployeeEmail = findViewById(R.id.txtTripEmployeeEmail)
        txtTripTitle = findViewById(R.id.txtTripTitle)
        txtTripDescription = findViewById(R.id.txtTripDescription)
        btnCreateTrip = findViewById(R.id.btnCreateTrip)

        val subjects: ArrayList<String> = ArrayList()
        val adapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_spinner_item, subjects)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinTripEmployeeName.adapter = adapter
        userRef.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val subject = document["name"].toString() + " " + document["lastName"].toString()
                    subjects.add(subject)
                }
                adapter.notifyDataSetChanged()
            }
        })

        btnCreateTrip.setOnClickListener {
            createTrip()
        }

        spinTripEmployeeName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                userRef
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                var name = document["name"].toString() + " " + document["lastName"].toString()
                                if (spinTripEmployeeName.selectedItem.toString() == name) {

                                    txtTripEmployeeEmail.text = document["email"].toString()
                                }
                            }
                        }
                        .addOnFailureListener { exception ->

                        }
            }
        }
    }

    fun createTrip() {
        val tripTitle: String = txtTripTitle.text.toString()
        val tripEmployeeName: String = spinTripEmployeeName.selectedItem.toString()
        val tripEmployeeEmail = txtTripEmployeeEmail.text.toString()
        val tripDescription: String = txtTripDescription.text.toString()
        val c = java.util.Calendar.getInstance().time
        val df = SimpleDateFormat("dd-MM-yyyy")
        val tf = SimpleDateFormat("HH:mm")
        val tripDate = df.format(c).toString()
        val tripCreatedHour = tf.format(c).toString()

        if (tripTitle.trim().isEmpty() || tripDescription.trim().isEmpty() || tripEmployeeName.isEmpty()) {
            toast("Llene los campos restantes", Toast.LENGTH_SHORT)
            return
        }

        path.get().addOnSuccessListener { document ->
            val id = document["Id"].toString().toInt()
            tripCollectionRef.add(Trip(tripDate, tripTitle, tripEmployeeName, tripEmployeeEmail, tripCreatedHour, tripDescription, id))
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

    fun back(view: View) {
        //startActivity(Intent(this, TripActivity::class.java))
        onBackPressed()
    }

    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        val toast = Toast.makeText(this, message, duration)
        toast.setGravity(Gravity.TOP, 0, 200)
        val view = toast.view
        val text = view.findViewById(android.R.id.message) as TextView
        view.setBackgroundResource(R.drawable.login_toast)
        text.gravity = Gravity.CENTER
        text.setBackgroundColor(Color.TRANSPARENT)
        text.setTextColor(Color.BLUE)
        text.textSize = 20F
        toast.show()
    }
}
