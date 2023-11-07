package com.practicum.myplaylistmaker.data.settings.impl

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Configuration
import com.practicum.myplaylistmaker.App.App
import com.practicum.myplaylistmaker.domain.settings.model.ThemeSettings
import com.practicum.myplaylistmaker.ui.settings.activity.THEME_KEY

class ThemeSettingsImpl(private val application: App) : ThemeSettings {
    private var appTheme: Boolean = false
    private lateinit var themeSharedPrefs: SharedPreferences

    override fun lookAtTheme(): Boolean {
        themeSharedPrefs = application.getSharedPreferences(THEME_KEY, MODE_PRIVATE)
        appTheme = themeSharedPrefs.getBoolean(THEME_KEY, !isDarkThemeEnabled())
        return appTheme
    }


    private fun isDarkThemeEnabled(): Boolean {
        val applicationContext = App.instance
        val currentMode =
            applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentMode == Configuration.UI_MODE_NIGHT_YES
    }

     override fun appThemeSwitch(): Boolean {
        appTheme = !appTheme
        val editor = themeSharedPrefs.edit()
        editor.putBoolean(THEME_KEY, appTheme)
        editor.apply()
        return appTheme
    }
}