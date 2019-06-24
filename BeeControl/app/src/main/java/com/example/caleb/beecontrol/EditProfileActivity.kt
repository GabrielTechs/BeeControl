package com.example.caleb.beecontrol

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.list_employees.*


class EditProfileActivity : AppCompatActivity()
{
    var imagen = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)

    }
    fun back(view: View)
    {
        startActivity(Intent(this, MenuActivity::class.java))
        imagen = findViewById(R.id.txtFperfil)
    }

    fun onClick(view: View): Unit
    {
        cargarImagen();
    }

    /*Para importar imagene de cualquier app*/
    fun cargarImagen() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/"
        startActivityForResult(intent.createChooser(intent, "Seleccione la aplicacion"), 10)

    }
    /*Esta parte es para seleccionar las imagenes desde la galería,*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK)
        {

            Uri path = data.getData();
            imagen.setImageURI(path);
        }
    }
}

private fun Intent.createChooser(intent: Intent, s: String): Intent? {

}
