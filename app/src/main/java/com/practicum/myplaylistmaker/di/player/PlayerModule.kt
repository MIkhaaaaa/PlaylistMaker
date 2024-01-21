package com.practicum.myplaylistmaker.di.player

import android.media.MediaPlayer
import com.practicum.myplaylistmaker.data.player.PlayerRepositoryImpl
import com.practicum.myplaylistmaker.domain.player.AudioPlayerInteractor
import com.practicum.myplaylistmaker.domain.player.AudioPlayerRepository
import com.practicum.myplaylistmaker.domain.player.impl.PlayerInteractorImpl
import com.practicum.myplaylistmaker.ui.player.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {

    single {
        MediaPlayer()
    }

    factory<AudioPlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    factory<AudioPlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    viewModel { PlayerViewModel(get(), get()) }
}