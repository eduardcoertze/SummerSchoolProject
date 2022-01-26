package com.example.summerschool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn_APOD.setOnClickListener {
            val intent = Intent(this,Apod::class.java)
            startActivity(intent)
        }

        btn_APOD_select.setOnClickListener {
            val selectIntent = Intent(this,ApodDateSelection::class.java)
            startActivity(selectIntent)
        }

        btn_gallery.setOnClickListener {
            val selectIntent = Intent(this,ApodGallery::class.java)
            startActivity(selectIntent)
        }

    }



}