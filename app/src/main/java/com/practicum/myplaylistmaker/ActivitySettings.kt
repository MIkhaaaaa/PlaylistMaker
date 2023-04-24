package com.practicum.myplaylistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.practicum.myapplication.MainActivity

class ActivitySettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val returnButton = findViewById<ImageView>(R.id.return_button)
        returnButton.setOnClickListener{
           finish()
        }

    }
}