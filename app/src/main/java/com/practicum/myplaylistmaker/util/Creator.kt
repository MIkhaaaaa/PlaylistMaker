package com.practicum.myplaylistmaker.util

import android.content.Context
import android.media.MediaPlayer
import com.practicum.myplaylistmaker.App.App
import com.practicum.myplaylistmaker.data.player.PlayerRepositoryImpl
import com.practicum.myplaylistmaker.data.search.history.TrackHistoryRepositoryImpl
import com.practicum.myplaylistmaker.data.search.requestAndResponse.RetrofitNetworkClient
import com.practicum.myplaylistmaker.data.search.TracksRepositoryImpl
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

    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(context))
    }

     fun provideTracksIteractor (context: Context) : TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
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