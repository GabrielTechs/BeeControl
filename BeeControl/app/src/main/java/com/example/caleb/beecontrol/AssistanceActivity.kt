package com.example.caleb.beecontrol

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_assistance.*


class AssistanceActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val AssistancebookRef = db.collection("Assistance")

    lateinit var txtAssistanceDate: TextView

    private var adapter: AssistanceAdapter? = null


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assistance)
        setUpRecyclerView()

        txtAssistanceDate = findViewById(R.id.txtAssistanceDate)

        btnAddAssistance.setOnClickListener {
            val intent = Intent(this, NewAssistanceActivity::class.java)
            startActivity(intent)
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
}