package com.example.caleb.beecontrol

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var firebaseAuth = FirebaseAuth.getInstance()
    var userRef = db.collection("user")
    val email = firebaseAuth.currentUser?.email.toString()
    val user = userRef.document(email)

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
        user.get().addOnSuccessListener { document ->
            if(document["isAdmin"] == true){
                txtEmployeeRole.text = "Adminitrador"
            }
            else {
                txtEmployeeRole.text = "Empleado"
            }
        }
        txtEmployeeEmail.text = employee.getString("email")
        }
        else{

            user.get().addOnSuccessListener { document ->
                txtEmployeeName.text = document["name"].toString()
                txtEmployeeLastName.text = document["lastName"].toString()
                txtEmployeeEmail.text = document["email"].toString()
                if(document["isAdmin"] == true){
                    txtEmployeeRole.text = "Adminitrador"
                }
                else {
                    txtEmployeeRole.text = "Empleado"
                }
            }
        }
        
    }
    fun editProf(view: View){

        val docRef = userRef.document(email)
        docRef.get().addOnSuccessListener { document ->
            var admin = document.toObject(Employee::class.java)?.isAdmin
            if(admin!!){
                val intent = Intent(this, EditProfileActivity::class.java)
                startActivity(intent)
            }
            else{
                toast("No eres admin!", Toast.LENGTH_LONG)
            }
        }
    }
    fun back(view:View){
        //startActivity(Intent(this, MenuActivity::class.java))
        onBackPressed()
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
        text.textSize = 16F
        toast.show()
    }
}
