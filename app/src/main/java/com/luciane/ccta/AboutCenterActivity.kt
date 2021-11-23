package com.luciane.ccta

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class AboutCenterActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_center)

        val mapImage = findViewById<ImageView>(R.id.ccta_map_image)
        mapImage.setOnClickListener { view ->
            startCctaMap()
        }
    }

    fun startCctaMap(){
        val labelCcta = Uri.encode("Centro De Comunicação, Turismo e Artes - CCTA/UFPB - Jardim Cidade Universitária, João Pessoa - PB")
        val mapIntentUri = Uri.parse("geo:7.1374775705513205,-34.849560968837274?q=$labelCcta&z=21")

        val mapIntent = Intent(Intent.ACTION_VIEW, mapIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        startActivity(mapIntent)
    }
}