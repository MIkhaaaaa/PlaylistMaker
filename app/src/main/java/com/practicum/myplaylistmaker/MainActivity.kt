package com.practicum.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.practicum.myplaylistmaker.ActivityMedia
import com.practicum.myplaylistmaker.ActivitySettings
import com.practicum.myplaylistmaker.R
import com.practicum.myplaylistmaker.SearchActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search)
        val mediaButton = findViewById<Button>(R.id.media)
        val settingsButton = findViewById<Button>(R.id.settings)

        searchButton.setOnClickListener {
            val intentSearch = Intent(this, SearchActivity::class.java)
            startActivity(intentSearch)
        }

        mediaButton.setOnClickListener {
            val intentMedia = Intent(this, ActivityMedia::class.java)
            startActivity(intentMedia)
        }

        settingsButton.setOnClickListener {
            val intentSettings = Intent(this, ActivitySettings::class.java)
            startActivity(intentSettings)
        }
    }
}