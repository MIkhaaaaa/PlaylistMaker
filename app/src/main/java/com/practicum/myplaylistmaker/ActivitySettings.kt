package com.practicum.myplaylistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
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

        val supportView = findViewById<ImageView>(R.id.support_view)
        supportView.setOnClickListener {
           val mailIntent =  Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("jan@example.com")) // recipients
                putExtra(Intent.EXTRA_SUBJECT, "Email subject")
                putExtra(Intent.EXTRA_TEXT, "Email message text")
                putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"))
            }
            startActivity(mailIntent)
        }



        val terms = findViewById<ImageView>(R.id.Terms_of_use)
        terms.setOnClickListener {
            val termsIntent = Intent(Intent.ACTION_VIEW)
            termsIntent.setData(Uri.parse("https://yandex.ru/legal/practicum_offer/"))
            startActivity(termsIntent)

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