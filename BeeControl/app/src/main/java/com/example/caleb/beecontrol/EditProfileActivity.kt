package com.example.caleb.beecontrol

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_editprofile.*
import java.util.*
import android.media.ExifInterface




class EditProfileActivity : AppCompatActivity()
{
    lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)
        image = findViewById(R.id.imgProfilePic)

        btnChangePic.setOnClickListener {
            cargarImagen()
        }

        btnSave.setOnClickListener{
                subirFoto()
        }
    }

    var path: Uri? = null

    fun back(view: View)
    {
        startActivity(Intent(this, MenuActivity::class.java))
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

            val filename = UUID.randomUUID().toString()




            val path = data.data

            val bitMap = MediaStore.Images.Media.getBitmap(contentResolver, path)

            val bitMapDrawable = BitmapDrawable(bitMap)

            image.setImageDrawable(bitMapDrawable)



            val ref = FirebaseStorage.getInstance().getReference("/imagenes/$filename")

            ref.putFile(path!!)
            //image.setImageURI(path)
        }
    }

    fun subirFoto (){


    }
}


