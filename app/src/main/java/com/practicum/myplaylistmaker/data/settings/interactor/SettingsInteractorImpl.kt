package com.practicum.myplaylistmaker.data.settings.interactor

import com.practicum.myplaylistmaker.domain.settings.SettingsInteractor
import com.practicum.myplaylistmaker.domain.settings.model.ThemeSettings
import com.practicum.myplaylistmaker.util.Creator

class SettingsInteractorImpl(private var themeSettings: ThemeSettings) : SettingsInteractor {
    init {
        themeSettings = Creator.provideThemeSettings()
    }

    var isDarkTheme = true

    override fun themeSwitch(): Boolean {
        isDarkTheme = themeSettings.lookAtTheme()
        return isDarkTheme
    }

    //функция смены темы:светлая/темная
    override fun changeTheme(): Boolean {
        isDarkTheme = themeSettings.appThemeSwitch()
        return isDarkTheme
    }
}