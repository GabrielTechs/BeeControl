package com.example.caleb.beecontrol

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
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
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_assistance.*


class AssistanceActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val AssistancebookRef = db.collection("Assistance")
    var userRef = db.collection("user")
    lateinit var txtAssistanceDate: TextView
    lateinit var firebaseAuth: FirebaseAuth
    private var adapter: AssistanceAdapter? = null


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assistance)
        setUpRecyclerView()

        firebaseAuth = FirebaseAuth.getInstance()

        val email = firebaseAuth.currentUser?.email.toString()

        txtAssistanceDate = findViewById(R.id.txtAssistanceDate)

        btnAddAssistance.setOnClickListener {
            val docRef = userRef.document(email)
            docRef.get()
                    .addOnSuccessListener { document ->
                        var admin = document.toObject(Employee::class.java)?.isAdmin
                        if(admin!!){
                            val intent = Intent(this, NewAssistanceActivity::class.java)
                            startActivity(intent)
                        }
                        else{
                            toast("No eres admin!", Toast.LENGTH_LONG)
                        }
                    }
        }

        txtAssistanceDate.setOnClickListener{
            datePicker()
        }

    }

    private fun setUpRecyclerView() {
        val query = AssistancebookRef.orderBy("status", Query.Direction.ASCENDING)

        val options = FirestoreRecyclerOptions.Builder<Assistance>()
                .setQuery(query, Assistance::class.java)
                .build()

        adapter = AssistanceAdapter(options)

        val recyclerview = findViewById<RecyclerView>(R.id.recycleViewAssist)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
    }

    fun back(view:View){
        startActivity(Intent(this, MenuActivity::class.java))
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun datePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
            txtAssistanceDate.text = "$mDay/$mMonth/$mYear"
        }, year, month + 1, day)
        dpd.show()
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