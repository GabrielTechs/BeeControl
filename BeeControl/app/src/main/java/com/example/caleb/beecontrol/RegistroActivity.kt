package com.example.caleb.beecontrol

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registro.*

class RegistroActivity : AppCompatActivity() {

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        btnRegister.setOnClickListener(){

           /* var nombre = txtName.text.toString()
            var apellido = txtApellido.text.toString()*/
            var email = txtEmailR.text.toString()
            var password = txtPasswordR.text.toString()

            CreateAccount(email, password)
        }
    }

    private fun CreateAccount(email: String, password: String) {

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
            if(task.isSuccessful){

            }
        }


    }


}
