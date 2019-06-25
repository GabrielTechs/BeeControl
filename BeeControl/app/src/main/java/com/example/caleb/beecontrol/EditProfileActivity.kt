package com.example.caleb.beecontrol

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_editprofile.*


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
    }

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
            val path = data.data

            val bitMap = MediaStore.Images.Media.getBitmap(contentResolver, path)

            val bitMapDrawable = BitmapDrawable(bitMap)

            image.setImageDrawable(bitMapDrawable)

            //image.setImageURI(path)
        }
    }
}


