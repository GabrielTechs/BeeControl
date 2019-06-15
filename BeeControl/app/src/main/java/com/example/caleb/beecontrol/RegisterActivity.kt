package com.example.caleb.beecontrol

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import android.util.Log
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
    private val path = db.collection("EmployeeID").document("Counter")
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
            Toast.makeText(applicationContext, "Introdusca su primer nombre", Toast.LENGTH_LONG).show()
            return
        }
        if(TextUtils.isEmpty(lastName)){
            Toast.makeText(applicationContext, "Introdusca su apellido", Toast.LENGTH_LONG).show()
            return
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(applicationContext, "Introdusca su email", Toast.LENGTH_LONG).show()
            return
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(applicationContext, "Introdusca su contraseña", Toast.LENGTH_LONG).show()
            return
        }
        if(TextUtils.isEmpty(confPass)){
            Toast.makeText(applicationContext, "Introdusca la confimacion de contraseña", Toast.LENGTH_LONG).show()
            return
        }
        if(password != confPass){
            Toast.makeText(applicationContext, "La contraseña no coincide", Toast.LENGTH_LONG).show()
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
                            user["name"] = name
                            user["lastName"] = lastName
                            user["email"] = email
                            user["isAdmin"] = false
                            user["Id"] = id
                            db.collection("user").document(email)
                                    .set(user)
                                    .addOnSuccessListener { Log.d("RegisterActivity", "DocumentSnapshot successfully written!") }
                                    .addOnFailureListener { e -> Log.w("RegisterActivity", "Error writing document", e) }
                            Log.d("RegisterSuccess", "createUserWithEmail:success")
                            Toast.makeText(applicationContext, "Usuario registrado corractamente!", Toast.LENGTH_SHORT).show()
                            incrementUserId()
                            var intent = Intent(this, LoginActivity::class.java)
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
}