package com.example.caleb.beecontrol

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }
    fun editProf(view: View){
        startActivity(Intent(this, EditprofileActivity::class.java))
    }
    fun back(view:View){
        startActivity(Intent(this, Control::class.java))
    }
}
