package com.practicum.myplaylistmaker.domain.settings

import com.practicum.myplaylistmaker.domain.settings.model.ThemeState

interface SettingsInteractor {
    fun isDayOrNight ():Boolean

    fun appThemeSwitch(): Boolean
}