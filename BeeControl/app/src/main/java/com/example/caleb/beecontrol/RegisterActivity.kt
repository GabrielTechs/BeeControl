package com.example.caleb.beecontrol

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import android.util.Log
import android.view.Gravity
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.FieldValue


class RegisterActivity : AppCompatActivity() {

    lateinit var txtName: EditText
    lateinit var txtLastName: EditText
    lateinit var txtEmail: EditText
    lateinit var txtPassword: EditText
    lateinit var txtConfPassword: EditText
    lateinit var btnRegister: Button
    lateinit var progressBar: ProgressBar

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        txtName = findViewById(R.id.txtName)
        txtLastName = findViewById(R.id.txtLastName)
        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        txtConfPassword = findViewById(R.id.txtConfPasword)
        txtEmail = findViewById(R.id.txtEmail)
        btnRegister = findViewById(R.id.btnRegister)
        progressBar = findViewById(R.id.progressBar)

        firebaseAuth =  FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        /*val settings = FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build()
        db.firestoreSettings = settings*/

        btnRegister.setOnClickListener{
            createAccount()
        }
    }

    fun createAccount() {

        var name: String = txtName.text.toString()
        var lastName: String = txtLastName.text.toString()
        var email: String = txtEmail.text.toString()
        var password: String = txtPassword.text.toString()
        var confPass: String = txtConfPassword.text.toString()


        if(TextUtils.isEmpty(name)){
            toast("Introdusca su primer nombre", Toast.LENGTH_LONG)
            return
        }
        if(TextUtils.isEmpty(lastName)){
            toast("Introduzca su apellido", Toast.LENGTH_LONG)
            return
        }
        if(TextUtils.isEmpty(email)){
            toast("Introduzca su correo electrónico", Toast.LENGTH_LONG)
            return
        }
        if(TextUtils.isEmpty(password)){
            toast("Introduzca su contraseña", Toast.LENGTH_LONG)
            return
        }
        if(TextUtils.isEmpty(confPass)){
            toast("Introduzca la confimación de contraseña", Toast.LENGTH_LONG)
            return
        }
        if(password != confPass){
            toast("La contraseña no coincide", Toast.LENGTH_LONG)
            return
        }

        if(password == confPass){
            progressBar.visibility = View.VISIBLE
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> {
                    task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        progressBar.visibility = View.INVISIBLE
                        val user = HashMap<String, Any>()
                        user["name"] = name
                        user["lastName"] = lastName
                        user["email"] = email
                        user["role"] = "Empleado"

                        db.collection("user").document(email)
                                .set(user)
                                .addOnSuccessListener { Log.d("RegisterActivity", "DocumentSnapshot successfully written!") }
                                .addOnFailureListener { e -> Log.w("RegisterActivity", "Error writing document", e) }
                        Log.d("RegisterSuccess", "createUserWithEmail:success")
                        toast("Usuario registrado corractamente!", Toast.LENGTH_SHORT)
                        var intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        progressBar.visibility = View.INVISIBLE
                        Log.w("RegisterFailed", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(applicationContext, "Revise los datos introducidos",
                                Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    fun incrementId(view: View) {
        val db = FirebaseFirestore.getInstance()
        val path = db.collection("EmployeeID").document("Counter")

        //path.update("Id", FieldValue.increment(50))
    }

    fun back(view: View){
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        val toast = Toast.makeText(this, message, duration)
        toast.setGravity(Gravity.TOP,0,200)
        val view = toast.view
        val text = view.findViewById(android.R.id.message) as TextView
        view.setBackgroundResource(R.drawable.login_toast)
        text.gravity = Gravity.CENTER
        text.setBackgroundColor(Color.WHITE)
        text.setTextColor(Color.BLUE)
        text.textSize = 20F
        toast.show()
    }
}