package com.example.caleb.beecontrol

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.content.res.AppCompatResources
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import android.widget.TextView
import android.widget.Toast
import android.view.LayoutInflater





class LoginActivity : AppCompatActivity()
{

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var txtEmail: EditText
    lateinit var txtPassword: EditText
    lateinit var btnLogin: Button
    //lateinit var txtRegister: TextView
    lateinit var progressBar: ProgressBar
    //private var proximityObserver: ProximityObserver? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        //txtRegister = findViewById(R.id.txtRegister)
        progressBar = findViewById(R.id.progressBar)
        firebaseAuth =  FirebaseAuth.getInstance()

        btnLogin.setOnClickListener{
            findViewById<TextView>(R.id.txtEmailEmpty).visibility = View.GONE
            val editTextEmail = findViewById<EditText>(R.id.txtEmail)
            editTextEmail.background.mutate().setColorFilter(resources.getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP)
            findViewById<TextView>(R.id.txtPasswordEmpty).visibility = View.GONE
            val editTextPassw = findViewById<EditText>(R.id.txtPassword)
            editTextPassw.background.mutate().setColorFilter(resources.getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP)
            signIn()
        }
/*
        txtRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }*/
    }

    @SuppressLint("ResourceAsColor")
    fun signIn() {
        val email = txtEmail.text.toString()
        val password = txtPassword.text.toString()

        if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
            findViewById<TextView>(R.id.txtEmailEmpty).visibility = View.VISIBLE
            val editTextEmail = findViewById<EditText>(R.id.txtEmail)
            editTextEmail.background.mutate().setColorFilter(resources.getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP)
            findViewById<TextView>(R.id.txtPasswordEmpty).visibility = View.VISIBLE
            val editTextPassw = findViewById<EditText>(R.id.txtPassword)
            editTextPassw.background.mutate().setColorFilter(resources.getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP)
            return
        }

            if(TextUtils.isEmpty(email)){
            //toast("Introduzca su correo electrónico", Toast.LENGTH_LONG)
            findViewById<TextView>(R.id.txtEmailEmpty).visibility = View.VISIBLE
            val editText = findViewById<EditText>(R.id.txtEmail)
            editText.background.mutate().setColorFilter(resources.getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP)
            return
        }
        if(TextUtils.isEmpty(password)){
            //toast("Introduzca su contraseña", Toast.LENGTH_LONG)
            findViewById<TextView>(R.id.txtPasswordEmpty).visibility = View.VISIBLE
            val editText = findViewById<EditText>(R.id.txtPassword)
            editText.background.mutate().setColorFilter(resources.getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP)
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
                        //toast("Error al iniciar sesión", Toast.LENGTH_LONG)
                        findViewById<TextView>(R.id.txtEmailError).visibility = View.VISIBLE
                        val editEmailText = findViewById<EditText>(R.id.txtEmail)
                        editEmailText.background.mutate().setColorFilter(resources.getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP)
                        findViewById<TextView>(R.id.txtPasswordError).visibility = View.VISIBLE
                        val editPasswordText = findViewById<EditText>(R.id.txtPassword)
                        editPasswordText.background.mutate().setColorFilter(resources.getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_ATOP)
                    }
                })
        }
    }

    fun forgotPass(view:View)
    {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }
    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_LONG) {
        val toast = Toast.makeText(this, message, duration)
        toast.setGravity(Gravity.TOP,0,250)
        val view = toast.view
        val text = view.findViewById(android.R.id.message) as TextView
        //view.setBackgroundResource(R.drawable.login_toast)
        view.setBackgroundColor(Color.TRANSPARENT)
        text.gravity = CENTER
        text.setBackgroundColor(Color.TRANSPARENT)
        text.setTextColor(Color.RED)
        text.textSize = 20F
        toast.show()
    }
}
