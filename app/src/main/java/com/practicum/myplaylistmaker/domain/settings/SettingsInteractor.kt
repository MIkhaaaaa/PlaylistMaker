package com.practicum.myplaylistmaker.domain.settings

import com.practicum.myplaylistmaker.domain.settings.model.ThemeState

interface SettingsInteractor {
    fun themeSwitch ():Boolean

    fun changeTheme(): Boolean
}