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
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.model.value.FieldValue


class RegisterActivity : AppCompatActivity() {

    lateinit var txtName: EditText
    lateinit var txtLastName: EditText
    lateinit var txtEmail: EditText
    lateinit var txtPassword: EditText
    lateinit var txtConfPassword: EditText
    lateinit var btnRegister: Button
    lateinit var progressBar: ProgressBar
    lateinit var path: DocumentReference
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

        path = db.collection("EmployeeID").document("Counter")

        btnRegister.setOnClickListener{
            createAccount()
        }
    }

    fun createAccount() {

        val name: String = txtName.text.toString()
        val lastName: String = txtLastName.text.toString()
        val email: String = txtEmail.text.toString()
        val password: String = txtPassword.text.toString()
        val confPass: String = txtConfPassword.text.toString()


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
                        path.get().addOnSuccessListener { document ->
                            val id = document["Id"].toString().toInt()
                            val user = HashMap<String, Any>()
                            val current = firebaseAuth.currentUser
                            user["name"] = name
                            user["lastName"] = lastName
                            user["email"] = email
                            user["isAdmin"] = false
                            user["onTrip"] = false
                            user["Id"] = id
                            user["ImagenPerfilUrl"] = ""
                            db.collection("user").document(email)
                                    .set(user)
                                    .addOnSuccessListener { Log.d("RegisterActivity", "DocumentSnapshot successfully written!") }
                                    .addOnFailureListener { e -> Log.w("RegisterActivity", "Error writing document", e) }
                            Log.d("RegisterSuccess", "createUserWithEmail:success")
                            Toast.makeText(applicationContext, "Usuario registrado corractamente!", Toast.LENGTH_SHORT).show()
                            incrementUserId()
                            current?.sendEmailVerification()
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Log.d("RegisterActivity", "Email sent.")
                                        }
                                    }
                            val intent = Intent(this, MenuActivity::class.java)
                            startActivity(intent)
                        }
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

    fun incrementUserId() {
        path.get().addOnSuccessListener { document ->
            var count = document["Id"]
            count = count.toString().toInt() + 1
            path.update("Id", count)
        }
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
        text.setBackgroundColor(Color.TRANSPARENT)
        text.setTextColor(Color.BLUE)
        text.textSize = 16F
        toast.show()
    }
}