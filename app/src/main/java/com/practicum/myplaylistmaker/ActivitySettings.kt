package com.practicum.myplaylistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.switchmaterial.SwitchMaterial

const val PRACTICUM_EXAMPLE_PREFERENCES = "practicum_example_preferences"
const val THEME_KEY = "theme_key"
class ActivitySettings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)
        val sharedPrefs = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES,MODE_PRIVATE)
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

        val switchButton = findViewById<SwitchMaterial>(R.id.switch_button)
        switchButton.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPrefs.edit().putBoolean(THEME_KEY,checked).apply()
        }

    }
}