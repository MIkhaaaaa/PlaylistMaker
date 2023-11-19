package com.practicum.myplaylistmaker.domain.settings.model

sealed class ThemeState {
    object LightTheme: ThemeState()
    object DarkTheme: ThemeState()
}