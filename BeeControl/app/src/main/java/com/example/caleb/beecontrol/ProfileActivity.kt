package com.example.caleb.beecontrol

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class ProfileActivity : AppCompatActivity() {

    lateinit var txtEmployeeName: TextView
    lateinit var txtEmployeeLastName: TextView
    lateinit var txtEmployeeRole: TextView
    lateinit var txtEmployeeEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        txtEmployeeName = findViewById(R.id.txtEmployeeName)
        txtEmployeeLastName = findViewById(R.id.txtEmployeeLastName)
        txtEmployeeRole = findViewById(R.id.txtEmployeeRole)
        txtEmployeeEmail = findViewById(R.id.txtEmployeeEmail)

        val employee = intent.extras

        if(employee != null){
        txtEmployeeName.text = employee.getString("name")
        txtEmployeeLastName.text = employee.getString("lastName")
        txtEmployeeRole.text = employee.getString("role")
        txtEmployeeEmail.text = employee.getString("email")
        }
        
    }
    fun editProf(view: View){
        startActivity(Intent(this, EditprofileActivity::class.java))
    }
    fun back(view:View){
        startActivity(Intent(this, MenuActivity::class.java))
    }
}
