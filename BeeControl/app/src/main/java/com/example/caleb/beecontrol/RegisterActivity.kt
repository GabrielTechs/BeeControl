package com.example.caleb.beecontrol

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun register(view: View){
        /* var nombre = txtName.text.toString()
             var apellido = txtApellido.text.toString()*/
        var email = txtEmail.text.toString()
        var password = txtPassword.text.toString()

        CreateAccount(email, password)
    }

    fun CreateAccount(email: String, password: String) {

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
            if(task.isSuccessful){
                Toast.makeText(applicationContext, "Usuario registrado corractamente!", Toast.LENGTH_LONG).show()
                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

    }
}