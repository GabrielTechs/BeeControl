package com.example.caleb.beecontrol

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView


class EditProfileActivity : AppCompatActivity()
{
    lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)

    }
    fun back(view: View)
    {
        startActivity(Intent(this, MenuActivity::class.java))
        image = findViewById(R.id.imgProfilePic)
    }

    fun changePicture(view: View)
    {
        cargarImagen()
    }

    fun cargarImagen() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/"
        startActivityForResult(intent, 10)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK)
        {
            val path: Uri = data?.data!!
            image.setImageURI(path)
        }
    }
}


