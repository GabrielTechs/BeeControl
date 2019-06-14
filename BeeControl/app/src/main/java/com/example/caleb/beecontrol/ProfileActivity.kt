package com.example.caleb.beecontrol

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    lateinit var txtEmployeeName: TextView
    lateinit var txtEmployeeLastName: TextView
    lateinit var txtEmployeeRole: TextView
    lateinit var txtEmployeeEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        txtEmployeeName = findViewById(R.id.txtEmployeeName)
        txtEmployeeLastName = findViewById(R.id.txtEmployeeLastName)
        txtEmployeeRole = findViewById(R.id.txtEmployeeRole)
        txtEmployeeEmail = findViewById(R.id.txtEmployeeEmail)

        val employee = intent.extras

        if(employee != null){
        txtEmployeeName.text = employee.getString("name")
        txtEmployeeLastName.text = employee.getString("lastName")
        txtEmployeeRole.text = employee.getString("role")
        txtEmployeeEmail.text = employee.getString("email")
        }
        
    }
    fun editProf(view: View){

        var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        var userRef = db.collection("user")

        val email = firebaseAuth.currentUser?.email.toString()
        val docRef = userRef.document(email)
        docRef.get().addOnSuccessListener { document ->
            var admin = document.toObject(Employee::class.java)?.isAdmin
            if(admin!!){
                val intent = Intent(this, EditProfileActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(applicationContext, "No eres admin!", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun back(view:View){
        //startActivity(Intent(this, MenuActivity::class.java))
        onBackPressed()
    }
}
