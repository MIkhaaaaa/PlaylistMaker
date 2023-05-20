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
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.UrlPracticum))
            startActivity(shareIntent)

        }

        val supportView = findViewById<ImageView>(R.id.support_view)
        supportView.setOnClickListener {
           val mailIntent =  Intent(Intent.ACTION_SEND).apply {
               type = "text/plain"
               putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.defaultEmail)))
               putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subjectEmail))
               putExtra(Intent.EXTRA_TEXT, getString(R.string.DefaultTextEmail))
               putExtra(Intent.EXTRA_STREAM, Uri.parse(getString(R.string.emailURI)))
               startActivity(this)
            }
        }

        val terms = findViewById<ImageView>(R.id.Terms_of_use)
        terms.setOnClickListener {
            val termsIntent = Intent(Intent.ACTION_VIEW)
            termsIntent.setData(Uri.parse(getString(R.string.AgreementUrl)))
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