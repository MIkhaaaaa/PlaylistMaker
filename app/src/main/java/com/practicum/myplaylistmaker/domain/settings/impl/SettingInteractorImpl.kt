package com.practicum.myplaylistmaker.domain.settings.impl

import com.practicum.myplaylistmaker.domain.settings.SettingsInteractor
import com.practicum.myplaylistmaker.domain.settings.model.ThemeSettings

class SettingInteractorImpl(
    private val themeSettings: ThemeSettings
) : SettingsInteractor {

    override fun isDayOrNight(): Boolean {
        return themeSettings.isDayOrNight()
    }

    override fun appThemeSwitch(): Boolean {
        return themeSettings.appThemeSwitch()
    }


}