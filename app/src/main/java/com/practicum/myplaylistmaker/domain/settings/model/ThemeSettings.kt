package com.practicum.myplaylistmaker.domain.settings.model

interface ThemeSettings {
    fun isDayOrNight ():Boolean
    fun appThemeSwitch() :Boolean
}