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
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.android.gms.tasks.OnCompleteListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener



class NewAssistanceActivity : AppCompatActivity() {

    lateinit var spinEmployeeName: Spinner
    lateinit var txtEmployeeEmail: EditText
    lateinit var txtAssistanceDate: TextView
    lateinit var spinStatus: Spinner
    lateinit var btnSaveAssistance: Button
    private val db = FirebaseFirestore.getInstance()
    private val userRef = db.collection("user")
    private val assistanceRef = db.collection("Assistance")

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newassistance)

        spinEmployeeName = findViewById(R.id.spinEmployeeName)
        txtEmployeeEmail = findViewById(R.id.txtEmployeeEmail)
        txtAssistanceDate = findViewById(R.id.txtAssistDate)
        spinStatus = findViewById(R.id.AssistStatus)
        btnSaveAssistance = findViewById(R.id.btnSaveAssistance)


        val subjects: ArrayList<String> = ArrayList()
        val adapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_spinner_item, subjects)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinEmployeeName.adapter = adapter
        userRef.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val subject = document["name"].toString() + " " + document["lastName"].toString()
                    subjects.add(subject)
                }
                adapter.notifyDataSetChanged()
            }
        })

        txtAssistanceDate.setOnClickListener {
            datePicker()
        }

        btnSaveAssistance.setOnClickListener {
            saveAssistance()
        }

        spinEmployeeName.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                userRef
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                var name = document["name"].toString() + " " + document["lastName"].toString()
                                if(spinEmployeeName.selectedItem.toString() == name){

                                    txtEmployeeEmail.setText(document["email"].toString())
                                }
                            }
                        }
                        .addOnFailureListener { exception ->

                        }
            }
        }
    }

    fun back(view: View){
        onBackPressed()
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
    fun saveAssistance() {
        val employeeEmail = txtEmployeeEmail.text.toString()
        val employeeName = spinEmployeeName.selectedItem.toString()
        val assistDate = txtAssistanceDate.text.toString()
        val status = spinStatus.selectedItem.toString()

        if (employeeName.trim().isEmpty() || assistDate.trim().isEmpty() || employeeEmail.trim().isEmpty()) {
            toast("Llene los campos restantes", Toast.LENGTH_SHORT)
            return
        }

        assistanceRef.get()
                .addOnSuccessListener { result ->
                    if (result != null) {
                        var assisted = false

                        for (document in result) {
                            document.toObject(Assistance::class.java)
                            if (document["employeeName"] == employeeName && document["assistDate"] == assistDate) {
                                toast("Ya estás asistido!", Toast.LENGTH_LONG)
                                assisted = true
                            }
                        }

                        if (!assisted) {


                            assistanceRef.add(Assistance(employeeName, employeeEmail, status, assistDate))
                            toast("Empleado agregado a la lista!", Toast.LENGTH_LONG)

                            val intent = Intent(this, AssistanceActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        toast("No hay asistencias!", Toast.LENGTH_LONG)
                    }
                }
                .addOnFailureListener { exception ->

                }
    }

    fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        val toast = Toast.makeText(this, message, duration)
        toast.setGravity(Gravity.TOP, 0, 200)
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