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
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_assistance.*
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS


class AssistanceActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val AssistancebookRef = db.collection("Assistancebook")

    private var adapter: AssistanceAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assistance)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val query =AssistancebookRef.orderBy("priority", Query.Direction.DESCENDING)

        val options = FirestoreRecyclerOptions.Builder<Assitance>()
                .setQuery(query, Assitance::class.java)
                .build()

        adapter = AssistanceAdapter(options)

        val recyclerview = findViewById<RecyclerView>(R.id.recycleViewAssist)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
    }

    fun login(view:View){
        startActivity(Intent(this, MenuActivity::class.java))
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun clickDataPicker(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in Toast
            Toast.makeText(this, """$dayOfMonth - ${monthOfYear + 1} - $year""", Toast.LENGTH_LONG).show()
        }, year, month, day)
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