package com.practicum.myplaylistmaker.domain.settings.impl

import com.practicum.myplaylistmaker.domain.settings.SettingsInteractor
import com.practicum.myplaylistmaker.domain.settings.model.ThemeSettings
import com.practicum.myplaylistmaker.util.Creator

class SettingInteractorImpl(
    private var themeSettings: ThemeSettings
) : SettingsInteractor {
    init {
        themeSettings = Creator.provideThemeSettings()
    }
    var isDarkTheme = false


    override fun themeSwitch(): Boolean {
        isDarkTheme=themeSettings.lookAtTheme ()
        return isDarkTheme
    }

    override fun changeTheme(): Boolean {
        isDarkTheme=themeSettings.appThemeSwitch ()
        return isDarkTheme
    }


}