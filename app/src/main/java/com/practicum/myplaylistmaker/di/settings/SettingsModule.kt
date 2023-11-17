package com.practicum.myplaylistmaker.di.settings

import com.practicum.myplaylistmaker.data.settings.impl.ThemeSettingsImpl
import com.practicum.myplaylistmaker.data.settings.interactor.SettingsInteractorImpl
import com.practicum.myplaylistmaker.data.sharing.ExternalNavigatorImpl
import com.practicum.myplaylistmaker.domain.settings.SettingsInteractor
import com.practicum.myplaylistmaker.domain.settings.model.ThemeSettings
import com.practicum.myplaylistmaker.domain.sharing.ExternalNavigator
import com.practicum.myplaylistmaker.domain.sharing.SharingInteractor
import com.practicum.myplaylistmaker.domain.sharing.impl.SharingInteractorImpl
import com.practicum.myplaylistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsSharingModule = module {

    single<ThemeSettings> {
        ThemeSettingsImpl(get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    viewModel { SettingsViewModel(get(), get()) }
}