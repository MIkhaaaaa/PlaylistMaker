package com.practicum.myplaylistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


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

        val switchButton = findViewById<Switch>(R.id.switch_button)
        switchButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }
}