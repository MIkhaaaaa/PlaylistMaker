package com.practicum.myplaylistmaker.domain.settings.impl

import com.practicum.myplaylistmaker.domain.settings.SettingsInteractor
import com.practicum.myplaylistmaker.domain.settings.model.ThemeSettings
import com.practicum.myplaylistmaker.util.Creator

class SettingInteractorImpl(
    private var themeSettings: ThemeSettings
) : SettingsInteractor {
    var isDarkTheme = true


    override fun isDayOrNight(): Boolean {
        isDarkTheme = themeSettings.isDayOrNight()
        return isDarkTheme
    }

    override fun appThemeSwitch(): Boolean {
        isDarkTheme = themeSettings.appThemeSwitch()
        return isDarkTheme
    }


}