package com.practicum.myplaylistmaker.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.myplaylistmaker.databinding.ActivityMainBinding
import com.practicum.myplaylistmaker.ui.mediaLibrary.ActivityMedia
import com.practicum.myplaylistmaker.ui.search.SearchActivity
import com.practicum.myplaylistmaker.ui.settings.activity.ActivitySettings

class MainActivity : AppCompatActivity() {
    private lateinit var bindingMain: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)

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