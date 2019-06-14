package com.example.caleb.beecontrol

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.Gravity
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_newassistance.*

class NewAssistanceActivity : AppCompatActivity() {

    lateinit var txtEmployeeName: EditText
    lateinit var txtAssistanceDate: TextView
    lateinit var spinStatus: Spinner
    lateinit var btnSaveAssistance: Button
    private val db = FirebaseFirestore.getInstance()
    private val assistanceCollectionRef = db.collection("Assistance")

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newassistance)

        txtEmployeeName = findViewById(R.id.txtEmployeeName)
        txtAssistanceDate = findViewById(R.id.txtAssistDate)
        spinStatus = findViewById(R.id.AssistStatus)
        btnSaveAssistance = findViewById(R.id.btnSaveAssistance)

        txtAssistanceDate.setOnClickListener {
            datePicker()
        }

        btnSaveAssistance.setOnClickListener {
            saveAssistance()
        }
    }

    fun back(view: View){
        startActivity(Intent(this, AssistanceActivity::class.java))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun datePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
            txtAssistanceDate.text = "$mDay/$mMonth/$mYear"
        }, year, month + 1, day)
        dpd.show()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun saveAssistance(){
        val employeeName: String = txtEmployeeName.text.toString()
        val assistDate: String = txtAssistanceDate.text.toString()
        val status: String = spinStatus.selectedItem.toString()

        if(employeeName.trim().isEmpty() || assistDate.trim().isEmpty()){
            toast("Llene los campos restantes", Toast.LENGTH_SHORT)
            return
        }

        assistanceCollectionRef.add(Assistance(employeeName, status, assistDate))

        toast("Assistencia guardada!", Toast.LENGTH_SHORT)
        val intent = Intent(this, AssistanceActivity::class.java)
        startActivity(intent)
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