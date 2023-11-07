package com.practicum.myplaylistmaker.App

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.myplaylistmaker.ui.search.HISTORY_KEY
import com.practicum.myplaylistmaker.util.Creator

class App: Application() {
    var currentTheme: Boolean = false

    companion object {
        lateinit var instance: App
            private set

        lateinit var savedHistory: SharedPreferences
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        Creator.init(this)
        savedHistory = applicationContext.getSharedPreferences(HISTORY_KEY, Context.MODE_PRIVATE)

        val settingInteractor = Creator.provideSettingInteractor()
        currentTheme = settingInteractor.themeSwitch()
        switchTheme(currentTheme)

    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        currentTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}