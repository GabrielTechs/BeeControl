package com.example.caleb.beecontrol

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class ForgotmypasswordActivity : AppCompatActivity()
{

    lateinit var btnSendEmail: Button
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotmypassword)

        btnSendEmail = findViewById(R.id.btnSendEmail)

        btnSendEmail.setOnClickListener {
            var intent = Intent(this, NewpasswordActivity::class.java)
            startActivity(intent)
        }
    }
    fun back(view: View){
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
