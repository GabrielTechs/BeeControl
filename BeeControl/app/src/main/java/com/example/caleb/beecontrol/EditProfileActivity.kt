package com.example.caleb.beecontrol

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
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
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_editprofile.*
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*
import android.media.ExifInterface
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.graphics.Bitmap
import android.graphics.Matrix
import com.google.firebase.firestore.CollectionReference


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class EditProfileActivity : AppCompatActivity()
{

    lateinit var db: FirebaseFirestore
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var userRef: CollectionReference

    lateinit var image: ImageView
    lateinit var txtEditName: EditText
    lateinit var txtEditLastName: EditText
    lateinit var txtEditRole: TextView
    lateinit var txtEditEmail: TextView
    lateinit var user: DocumentReference
    var path: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        userRef = db.collection("user")

        val email = firebaseAuth.currentUser?.email.toString()
        user = userRef.document(email)

        val employee = intent.extras

        txtEditName = findViewById(R.id.txtEditName)
        txtEditLastName = findViewById(R.id.txtEditLastName)
        txtEditRole = findViewById(R.id.txtEditRole)
        txtEditEmail = findViewById(R.id.txtEditEmail)

        txtEditEmail.text = employee.getString("email")
        txtEditRole.text = employee.getString("role")

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

            if (!txtEditName.text.isEmpty() && !txtEditLastName.text.isEmpty()){
                user.update("name", txtEditName.text.toString())
                user.update("lastName", txtEditLastName.text.toString())
            }
            else if (txtEditName.text.isEmpty() && !txtEditLastName.text.isEmpty()){
                user.update("lastName", txtEditLastName.text.toString())
            }
            else if (!txtEditName.text.isEmpty() && txtEditLastName.text.isEmpty()){
                user.update("name", txtEditName.text.toString())
            }
        }

        subirFoto()

        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
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
            val matrix = Matrix()
            matrix.postRotate(270f)

            path = data.data

            val bitMap = MediaStore.Images.Media.getBitmap(contentResolver, path)
            val rotatedBitmap = Bitmap.createBitmap(bitMap, 0, 0, bitMap.getWidth(), bitMap.getHeight(), matrix, true)
            val bitMapDrawable = BitmapDrawable(rotatedBitmap)

            image.setImageDrawable(bitMapDrawable)

        }
    }

    fun subirFoto (){

        user.get().addOnSuccessListener { document ->
            if (path != null) {
                val filename = UUID.randomUUID().toString()
                val ref = FirebaseStorage.getInstance().getReference("/imagenes/$filename")
                ref.putFile(path!!).addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        user.update("ImagenPerfilUrl", it.toString())
                    }
                }
            }
        }
    }
}


