package com.example.caleb.beecontrol

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity()
{

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?)
    {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
       // FirebaseApp.initializeApp(this);


        txtRegister.setOnClickListener(){
            var intent = Intent(this,RegistroActivity::class.java)
            startActivity(intent)
        }


    }



        fun login(view: View){
                var email = txtEmailR.text.toString()
                var password = txtPasswordR.toString()

                signIn(email, password)
        }





    private fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {task->
            if(task.isSuccessful){

                var intent = Intent(this, PProfileActivity::class.java)
                startActivity(intent)
            }

        }

    }



}
