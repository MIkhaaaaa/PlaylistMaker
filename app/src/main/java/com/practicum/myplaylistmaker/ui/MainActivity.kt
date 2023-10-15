package com.practicum.myplaylistmaker.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practicum.myplaylistmaker.App.App
import com.practicum.myplaylistmaker.databinding.ActivityMainBinding
import com.practicum.myplaylistmaker.ui.settings.ActivitySettings
import com.practicum.myplaylistmaker.ui.settings.PRACTICUM_EXAMPLE_PREFERENCES
import com.practicum.myplaylistmaker.ui.settings.THEME_KEY

class MainActivity : AppCompatActivity() {
    private lateinit var bindingMain: ActivityMainBinding
    private var sharedPrefs: SharedPreferences ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)

        sharedPrefs = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES,MODE_PRIVATE)
        App().switchTheme(sharedPrefs?.getBoolean(THEME_KEY,false)!!)
        bindingMain.search.setOnClickListener {
            val intentSearch = Intent(this, SearchActivity::class.java)
            startActivity(intentSearch)
        }

        bindingMain.media.setOnClickListener {
            val intentMedia = Intent(this, ActivityMedia::class.java)
            startActivity(intentMedia)
        }

        bindingMain.settings.setOnClickListener {
            val intentSettings = Intent(this, ActivitySettings::class.java)
            startActivity(intentSettings)
        }
    }
}