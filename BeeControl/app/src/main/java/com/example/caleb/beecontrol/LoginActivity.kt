package com.example.caleb.beecontrol

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult

class LoginActivity : AppCompatActivity()
{

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var txtEmail: EditText
    lateinit var txtPassword: EditText
    lateinit var btnLogin: Button
    lateinit var txtRegister: TextView
    lateinit var progressBar: ProgressBar
    //private var proximityObserver: ProximityObserver? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtRegister = findViewById(R.id.txtRegister)
        progressBar = findViewById(R.id.progressBar)
        firebaseAuth =  FirebaseAuth.getInstance()

        btnLogin.setOnClickListener{
            signIn()
        }

        txtRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun signIn() {
        val email = txtEmail.text.toString()
        val password = txtPassword.text.toString()

        if(TextUtils.isEmpty(email)){
            Toast.makeText(applicationContext, "Introdusca su email", Toast.LENGTH_LONG).show()
            return
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(applicationContext, "Introdusca su contraseña", Toast.LENGTH_LONG).show()
            return
        }

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            progressBar.visibility = View.VISIBLE

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> {
                    task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        progressBar.visibility = View.INVISIBLE
                        val intent = Intent(this, MenuActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        progressBar.visibility = View.INVISIBLE
                        Log.w("SignInFailed", "signInWithEmail:failure", task.exception)
                        Toast.makeText(applicationContext, "Error al iniciar sesion.",
                                Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    fun forgotPass(view:View)
    {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }

}
