package com.example.caleb.beecontrol

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class EmployeesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employees)
    }
    fun back(view: View){
        startActivity(Intent(this, Control::class.java))
    }
    fun seemessages(view: View){
        var intent = Intent(this, SupportMessagesActivity::class.java)
        startActivity(intent)
    }
}