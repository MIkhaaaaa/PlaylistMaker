package com.practicum.myplaylistmaker.App

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.myplaylistmaker.HISTORY_KEY
import com.practicum.myplaylistmaker.domain.models.Track

class App: Application() {
   private var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        savedHistory = applicationContext.getSharedPreferences(HISTORY_KEY, Context.MODE_PRIVATE)
    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    companion object {
        lateinit var savedHistory: SharedPreferences
        fun getSharedPreferences():SharedPreferences { return savedHistory
        }
        var trackHistoryList = ArrayList<Track>()
    }
}