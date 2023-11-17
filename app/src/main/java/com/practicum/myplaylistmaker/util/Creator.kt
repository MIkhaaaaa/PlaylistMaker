package com.practicum.myplaylistmaker.util

import com.practicum.myplaylistmaker.App.App
import com.practicum.myplaylistmaker.data.settings.impl.ThemeSettingsImpl
import com.practicum.myplaylistmaker.data.sharing.ExternalNavigatorImpl
import com.practicum.myplaylistmaker.domain.settings.SettingsInteractor
import com.practicum.myplaylistmaker.domain.settings.impl.SettingInteractorImpl
import com.practicum.myplaylistmaker.domain.settings.model.ThemeSettings
import com.practicum.myplaylistmaker.domain.sharing.ExternalNavigator
import com.practicum.myplaylistmaker.domain.sharing.SharingInteractor
import com.practicum.myplaylistmaker.domain.sharing.impl.SharingInteractorImpl

object Creator {
    private lateinit var application: App


    fun provideSettingInteractor(): SettingsInteractor {
        return SettingInteractorImpl(provideThemeSettings())
    }

    fun provideThemeSettings(): ThemeSettings {
        return ThemeSettingsImpl(application)
    }

    fun provideSharingIneractor(): SharingInteractor {
        return SharingInteractorImpl(provideExternalNavigator())
    }

    fun provideExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(application)
    }

    fun init(application: App) {
        this.application = application
    }

}