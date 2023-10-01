package com.practicum.myplaylistmaker.ui.settings

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practicum.myplaylistmaker.App.App
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.databinding.ActivitySettingsBinding

const val PRACTICUM_EXAMPLE_PREFERENCES = "practicum_example_preferences"
const val THEME_KEY = "theme_key"
class ActivitySettings : AppCompatActivity() {
    private lateinit var bindingSettings: ActivitySettingsBinding
    private lateinit var sharedPrefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSettings = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(bindingSettings.root)
        sharedPrefs = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES,MODE_PRIVATE)
        bindingSettings.returnButton.setOnClickListener{
           finish()
        }

        bindingSettings.shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.UrlPracticum))
            startActivity(shareIntent)

        }

        bindingSettings.supportView.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
               type = "text/plain"
               putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.defaultEmail)))
               putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subjectEmail))
               putExtra(Intent.EXTRA_TEXT, getString(R.string.DefaultTextEmail))
               putExtra(Intent.EXTRA_STREAM, Uri.parse(getString(R.string.emailURI)))
               startActivity(this)
            }
        }

        bindingSettings.TermsOfUse.setOnClickListener {
            val termsIntent = Intent(Intent.ACTION_VIEW)
            termsIntent.setData(Uri.parse(getString(R.string.AgreementUrl)))
            startActivity(termsIntent)

        }

        bindingSettings.switchButton.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPrefs.edit().putBoolean(THEME_KEY,checked).apply()
        }

    }
}