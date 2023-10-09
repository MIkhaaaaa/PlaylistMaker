package com.practicum.myplaylistmaker

import android.content.SharedPreferences
import android.media.MediaPlayer
import com.practicum.myplaylistmaker.App.App
import com.practicum.myplaylistmaker.data.PlayerRepositoryImpl
import com.practicum.myplaylistmaker.data.TrackHistoryRepositoryImpl
import com.practicum.myplaylistmaker.data.network.RetrofitNetworkClient
import com.practicum.myplaylistmaker.data.network.TracksRepositoryImpl
import com.practicum.myplaylistmaker.domain.SharedPreferencesInteractor
import com.practicum.myplaylistmaker.domain.api.AudioPlayerInteractor
import com.practicum.myplaylistmaker.domain.api.AudioPlayerRepository
import com.practicum.myplaylistmaker.domain.api.TracksInteractor
import com.practicum.myplaylistmaker.domain.api.TracksRepository
import com.practicum.myplaylistmaker.domain.impl.PlayerInteractorImpl
import com.practicum.myplaylistmaker.domain.impl.SharedPreferencesInteractorImpl
import com.practicum.myplaylistmaker.domain.impl.TracksInteractorImpl
import com.practicum.myplaylistmaker.domain.SharedPreferencesRepository as SharedPreferencesRepository

object Creator {

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

     fun provideTracksIteractor () : TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun providePlayerRepository() : AudioPlayerRepository {
        return PlayerRepositoryImpl(MediaPlayer())
    }

    fun providePlayerInteractor() : AudioPlayerInteractor{
        return PlayerInteractorImpl(providePlayerRepository())
    }

    private fun getSharedPreferenceRepository(): SharedPreferencesRepository{
        return TrackHistoryRepositoryImpl(App.savedHistory)
    }

    fun provideSharedPreferenceInteractor() : SharedPreferencesInteractor{
        return SharedPreferencesInteractorImpl(getSharedPreferenceRepository())
    }


}