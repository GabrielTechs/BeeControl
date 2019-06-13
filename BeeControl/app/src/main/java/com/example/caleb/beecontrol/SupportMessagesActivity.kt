package com.example.caleb.beecontrol

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SupportMessagesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support_messages)
    }
    fun back(view: View){
        //startActivity(Intent(this, EmployeesActivity::class.java))
        onBackPressed()
    }
}
