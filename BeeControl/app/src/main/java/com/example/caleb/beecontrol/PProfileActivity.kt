package com.example.caleb.beecontrol

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.pactivity_profile.*

class PProfileActivity: AppCompatActivity () {

   var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pactivity_profile)

        btnLogout.setOnClickListener(){
          if (firebaseAuth !=null){

                firebaseAuth.signOut()

                firebaseAuth.addAuthStateListener {

                    if(firebaseAuth.currentUser == null)(
                        this.finish()
                        )
                }
            }
        }

    }

}