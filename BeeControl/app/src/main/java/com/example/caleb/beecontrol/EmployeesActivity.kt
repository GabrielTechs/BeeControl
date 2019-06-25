package com.example.caleb.beecontrol

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class EmployeesActivity : AppCompatActivity() {

    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var userRef: CollectionReference = db.collection("user")
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

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

    fun back(view: View){
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }

    fun seemessages(view: View){

        val email = firebaseAuth.currentUser?.email.toString()
        val docRef = userRef.document(email)
        docRef.get().addOnSuccessListener { document ->
            var admin = document.toObject(Employee::class.java)?.isAdmin
            if(admin!!){
                val intent = Intent(this, SupportMessagesActivity::class.java)
                startActivity(intent)
            }
            else{
                toast("No eres admin!", Toast.LENGTH_LONG)
            }
        }
    }
    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        val toast = Toast.makeText(this, message, duration)
        toast.setGravity(Gravity.TOP,0,200)
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