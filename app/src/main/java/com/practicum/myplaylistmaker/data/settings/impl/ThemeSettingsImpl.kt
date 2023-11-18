package com.practicum.myplaylistmaker.data.settings.impl

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.res.Configuration
import android.util.Log
import com.practicum.myplaylistmaker.domain.settings.model.ThemeSettings

const val THEME_KEY = "theme_key"

class ThemeSettingsImpl(private val application: Context
) : ThemeSettings {
    private var appTheme: Boolean = false
    private val themeSharedPrefs by lazy {
        application.getSharedPreferences(
            THEME_KEY,
            MODE_PRIVATE
        )
    }

    override fun isDayOrNight(): Boolean {
        appTheme = themeSharedPrefs.getBoolean(THEME_KEY, !isDarkThemeEnabled())
        Log.d("appTheme1", appTheme.toString())
        return appTheme
    }


    private fun isDarkThemeEnabled(): Boolean {
        val currentMode =
            application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        Log.d("currentMode", currentMode.toString())
        return currentMode == Configuration.UI_MODE_NIGHT_YES
    }

    override fun appThemeSwitch(): Boolean {
        appTheme = !appTheme
        val editor = themeSharedPrefs.edit()
        editor.putBoolean(THEME_KEY, appTheme)
        editor.apply()
        Log.d("appTheme2", appTheme.toString())
        return appTheme

    }
}