package com.example.caleb.beecontrol

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import android.widget.*
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials
import com.estimote.proximity_sdk.api.ProximityObserverBuilder
import com.estimote.proximity_sdk.api.ProximityObserver
import com.estimote.proximity_sdk.api.ProximityZoneContext
import com.estimote.proximity_sdk.api.ProximityZoneBuilder
import com.estimote.proximity_sdk.api.ProximityZone
import kotlin.Unit
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement
import java.time.temporal.TemporalQueries.zone
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory

class LoginActivity : AppCompatActivity()
{

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var txtEmail: EditText
    lateinit var txtPassword: EditText
    lateinit var btnLogin: Button
    lateinit var txtRegister: TextView
    lateinit var progressBar: ProgressBar
    private var proximityObserver: ProximityObserver? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val cloudCredentials = EstimoteCloudCredentials("beecontrol-afk", "0e4a52ed6b84786e84c489e8019a9a56")

        this.proximityObserver = ProximityObserverBuilder(applicationContext, cloudCredentials)
                .onError { throwable ->
                    Log.e("app", "proximity observer error: $throwable")
                    null
                }
                .withBalancedPowerMode()
                .build()

        val zone = ProximityZoneBuilder()
                .forTag("beecontrol-afk")
                .inNearRange()
                .onEnter { context ->

                    var beacon = context.attachments["beecontrol-afk"]
                    Toast.makeText(applicationContext, "Welcome to $beacon's zone", Toast.LENGTH_LONG).show()
                    Log.d("app", "Welcome to $beacon's zone")
                    null
                }
                .onExit {
                    Toast.makeText(applicationContext, "Bye bye, come again!", Toast.LENGTH_LONG).show()
                    Log.d("app", "Bye bye, come again!")
                    null
                }
                .build()


        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtRegister = findViewById(R.id.txtRegister)
        progressBar = findViewById(R.id.progressBar)
        firebaseAuth =  FirebaseAuth.getInstance()

        btnLogin.setOnClickListener{
            signIn()
        }

        txtRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        RequirementsWizardFactory
                .createEstimoteRequirementsWizard()
                .fulfillRequirements(this,
                        // onRequirementsFulfilled
                        {
                            Log.d("app", "requirements fulfilled")
                            proximityObserver!!.startObserving(zone)
                            null
                        },
                        // onRequirementsMissing
                        { requirements ->
                            Log.e("app", "requirements missing: $requirements")
                            null
                        }
                        // onError
                ) { throwable ->
                    Log.e("app", "requirements error: $throwable")
                    null
                }
    }

    fun signIn() {
        val email = txtEmail.text.toString()
        val password = txtPassword.text.toString()

        if(TextUtils.isEmpty(email)){
            Toast.makeText(applicationContext, "Introdusca su email", Toast.LENGTH_LONG).show()
            return
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(applicationContext, "Introdusca su contraseña", Toast.LENGTH_LONG).show()
            return
        }

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            progressBar.visibility = View.VISIBLE

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> {
                    task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        progressBar.visibility = View.INVISIBLE
                        val intent = Intent(this, MenuActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        progressBar.visibility = View.INVISIBLE
                        Log.w("SignInFailed", "signInWithEmail:failure", task.exception)
                        Toast.makeText(applicationContext, "Error al iniciar sesion.",
                                Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    fun forgotPass(view:View)
    {
        startActivity(Intent(this, ForgotmypasswordActivity::class.java))
    }

}
