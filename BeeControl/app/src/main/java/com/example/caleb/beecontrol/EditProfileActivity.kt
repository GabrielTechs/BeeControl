package com.example.caleb.beecontrol

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_assistance.*
import kotlinx.android.synthetic.main.activity_editprofile.*
import kotlinx.android.synthetic.main.activity_profile.*


class EditProfileActivity : AppCompatActivity()
{

    lateinit var db: FirebaseFirestore
    lateinit var firebaseAuth: FirebaseAuth
    var userRef = db.collection("user")

    lateinit var image: ImageView
    lateinit var txtEditName: EditText
    lateinit var txtEditLastName: EditText
    lateinit var txtEditRole: TextView
    lateinit var txtEditEmail: TextView
    lateinit var user: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        val email = firebaseAuth.currentUser?.email.toString()
        user = userRef.document(email)

        txtEditName = findViewById(R.id.txtEditName)
        txtEditLastName = findViewById(R.id.txtEditLastName)
        txtEditRole = findViewById(R.id.txtEditRole)
        txtEditEmail = findViewById(R.id.txtEditEmail)

        image = findViewById(R.id.imgProfilePic)

        btnChangePic.setOnClickListener {
            cargarImagen()
        }

        btnSave.setOnClickListener {
            saveData()
        }
    }

    fun back(view: View)
    {
        startActivity(Intent(this, MenuActivity::class.java))
    }

    fun saveData(){

        user.get().addOnSuccessListener { document ->
            user.update("name", txtEditName.text.toString())
            user.update("lastName", txtEditLastName.text.toString())
        }

    }

    fun cargarImagen() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null)
        {
            val path = data.data

            val bitMap = MediaStore.Images.Media.getBitmap(contentResolver, path)

            val bitMapDrawable = BitmapDrawable(bitMap)

            image.setImageDrawable(bitMapDrawable)

            //image.setImageURI(path)
        }
    }
}


