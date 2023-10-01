package com.practicum.myplaylistmaker

import android.media.MediaPlayer
import com.practicum.myplaylistmaker.data.PlayerRepositoryImpl
import com.practicum.myplaylistmaker.data.network.RetrofitNetworkClient
import com.practicum.myplaylistmaker.data.network.TracksRepositoryImpl
import com.practicum.myplaylistmaker.domain.api.AudioPlayerInteractor
import com.practicum.myplaylistmaker.domain.api.AudioPlayerRepository
import com.practicum.myplaylistmaker.domain.api.TracksInteractor
import com.practicum.myplaylistmaker.domain.api.TracksRepository
import com.practicum.myplaylistmaker.domain.impl.PlayerInteractorImpl
import com.practicum.myplaylistmaker.domain.impl.TracksInteractorImpl

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

}