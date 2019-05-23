package com.example.caleb.beecontrol

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class EmployeesActivity : AppCompatActivity() {

    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var userRef: CollectionReference = db.collection("user")

    lateinit var adapter: EmployeeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employees)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView(){
        val query = userRef.orderBy("name", Query.Direction.ASCENDING)

        val options = FirestoreRecyclerOptions.Builder<Employee>()
                .setQuery(query, Employee::class.java)
                .build()

        adapter = EmployeeAdapter(options)

        val employeeRecyclerView = findViewById<RecyclerView>(R.id.recyclerViewEmployees)
        employeeRecyclerView.setHasFixedSize(true)
        employeeRecyclerView.layoutManager = LinearLayoutManager(this)
        employeeRecyclerView.adapter = adapter

        adapter.setOnItemClickListener { documentSnapshot, position ->
            val employee = documentSnapshot.toObject(Employee::class.java)

            val intent = Intent(this,ProfileActivity::class.java)

            intent.putExtra("name", employee!!.getName())
            intent.putExtra("lastName", employee.getLastName())
            intent.putExtra("role", employee.getRole())
            intent.putExtra("email", employee.getEmail())

            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
    fun seemessages(view: View){
        var intent = Intent(this, SupportMessagesActivity::class.java)
        startActivity(intent)
    }
}