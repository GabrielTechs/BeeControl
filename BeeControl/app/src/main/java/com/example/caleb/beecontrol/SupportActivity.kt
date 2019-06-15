package com.example.caleb.beecontrol

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat

class SupportActivity : AppCompatActivity() {

    lateinit var txtSupportName: EditText
    lateinit var txtSupportEmail: TextView
    lateinit var txtSupportDescription: EditText
    lateinit var btnCreateSupportMessage: Button
    private val db = FirebaseFirestore.getInstance()
    private val supportMRef = db.collection("SupportM")
    private val path = db.collection("SupportID").document("Counter")
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)
        firebaseAuth = FirebaseAuth.getInstance()

        txtSupportName = findViewById(R.id.txtSupportName)
        txtSupportEmail = findViewById(R.id.txtSupportEmail)
        txtSupportEmail.text = firebaseAuth.currentUser?.email.toString()
        txtSupportDescription = findViewById(R.id.txtSupportDescription)
        btnCreateSupportMessage = findViewById(R.id.btnCreateSupportMessage)

        btnCreateSupportMessage.setOnClickListener {
            createSupportMessage()
        }
    }

    fun createSupportMessage(){
        val supportName = txtSupportName.text.toString()
        val supportEmail = txtSupportEmail.text.toString()
        val supportDescription = txtSupportDescription.text.toString()
        val c = java.util.Calendar.getInstance().time
        val df = SimpleDateFormat("dd-MM-yyyy")
        val supportDate =  df.format(c).toString()

        if(supportName.trim().isEmpty() || supportDescription.trim().isEmpty()){
            toast("Llene los campos restantes", Toast.LENGTH_SHORT)
        }

        path.get().addOnSuccessListener { document ->
            val id = document["Id"].toString().toInt()

            supportMRef.add(Support(supportName, supportEmail, id, supportDate, supportDescription))
            supportIdIncrement()
        }

        toast("Ticket de soporte enviado!", Toast.LENGTH_SHORT)
        onBackPressed()
    }

    fun supportIdIncrement() {
        path.get().addOnSuccessListener { document ->
            var count = document["Id"]
            count = count.toString().toInt() + 1
            path.update("Id", count)
        }
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

    fun back(view: View){
        startActivity(Intent(this, MenuActivity::class.java))
    }
}
