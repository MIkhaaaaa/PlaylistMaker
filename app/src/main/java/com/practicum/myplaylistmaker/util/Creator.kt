package com.practicum.myplaylistmaker.util

import android.content.Context
import android.media.MediaPlayer
import com.practicum.myplaylistmaker.App.App
import com.practicum.myplaylistmaker.data.player.PlayerRepositoryImpl
import com.practicum.myplaylistmaker.data.search.TracksRepositoryImpl
import com.practicum.myplaylistmaker.data.search.history.TrackHistoryRepositoryImpl
import com.practicum.myplaylistmaker.data.search.requestAndResponse.RetrofitNetworkClient
import com.practicum.myplaylistmaker.data.settings.impl.ThemeSettingsImpl
import com.practicum.myplaylistmaker.domain.player.AudioPlayerInteractor
import com.practicum.myplaylistmaker.domain.player.AudioPlayerRepository
import com.practicum.myplaylistmaker.domain.player.TracksInteractor
import com.practicum.myplaylistmaker.domain.player.TracksRepository
import com.practicum.myplaylistmaker.domain.player.impl.PlayerInteractorImpl
import com.practicum.myplaylistmaker.domain.player.impl.TracksInteractorImpl
import com.practicum.myplaylistmaker.domain.search.SharedPreferencesInteractor
import com.practicum.myplaylistmaker.domain.search.SharedPreferencesInteractorImpl
import com.practicum.myplaylistmaker.domain.search.SharedPreferencesRepository
import com.practicum.myplaylistmaker.domain.settings.SettingsInteractor
import com.practicum.myplaylistmaker.domain.settings.impl.SettingInteractorImpl
import com.practicum.myplaylistmaker.domain.settings.model.ThemeSettings
import com.practicum.myplaylistmaker.domain.sharing.ExternalNavigator
import com.practicum.myplaylistmaker.domain.sharing.SharingInteractor
import com.practicum.myplaylistmaker.data.sharing.ExternalNavigatorImpl
import com.practicum.myplaylistmaker.domain.sharing.impl.SharingInteractorImpl

object Creator {
    private lateinit var application: App

    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(context))
    }

     fun provideTracksIteractor (context: Context) : TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }

    private fun providePlayerRepository() : AudioPlayerRepository {
        return PlayerRepositoryImpl(MediaPlayer())
    }

    fun providePlayerInteractor() : AudioPlayerInteractor {
        return PlayerInteractorImpl(providePlayerRepository())
    }

    private fun getSharedPreferenceRepository(): SharedPreferencesRepository {
        return TrackHistoryRepositoryImpl(App.savedHistory)
    }

    fun provideSharedPreferenceInteractor() : SharedPreferencesInteractor {
        return SharedPreferencesInteractorImpl(getSharedPreferenceRepository())
    }

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