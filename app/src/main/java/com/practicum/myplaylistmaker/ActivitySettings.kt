package com.practicum.myplaylistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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

        val shareView = findViewById<ImageView>(R.id.share_button)
        shareView.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_VIEW)
            shareIntent.setData(Uri.parse("https://practicum.yandex.ru/android-developer/"))
            startActivity(shareIntent)
        }


    }
}