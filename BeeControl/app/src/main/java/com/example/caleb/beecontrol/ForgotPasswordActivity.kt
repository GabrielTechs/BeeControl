package com.example.caleb.beecontrol

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class ForgotPasswordActivity : AppCompatActivity()
{

    lateinit var btnSendEmail: Button
    lateinit var txtMail: EditText
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var emailAddress: String
    lateinit var userRef: CollectionReference


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotmypassword)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        userRef = db.collection("user")

        btnSendEmail = findViewById(R.id.btnSendEmail)
        txtMail = findViewById(R.id.txtMail)

        btnSendEmail.setOnClickListener {
            sendPasswordReset()
        }
    }

    fun sendPasswordReset(){

        emailAddress = txtMail.text.toString()

        userRef.get().addOnSuccessListener { documents ->
            var exists = false

            for (d in documents){
                if(d["email"].toString() == emailAddress){
                    exists = true
                }
            }

            if(exists){
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("ForgotPassword", "Email sent.")
                                val intent = Intent(this, NewPasswordActivity::class.java)
                                startActivity(intent)
                            }
                        }
            }
            else{
                toast("Debe dirigirse a recursos humanos para una nueva asistencia.", Toast.LENGTH_LONG)
            }
        }
    }

    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        val toast = Toast.makeText(this, message, duration)
        toast.setGravity(Gravity.TOP, 0, 250)
        val view = toast.view
        val text = view.findViewById(android.R.id.message) as TextView
        view.setBackgroundResource(R.drawable.login_toast)
        text.gravity = Gravity.CENTER
        text.setBackgroundColor(Color.TRANSPARENT)
        text.setTextColor(Color.BLACK)
        text.textSize = 16F
        toast.show()
    }

    fun back(view: View){
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
