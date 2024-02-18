package com.practicum.myplaylistmaker.di.favorites

import androidx.room.Room
import com.practicum.myplaylistmaker.data.playListDB.PlayistDataBase
import com.practicum.myplaylistmaker.data.playListDB.PlaylistConverter
import com.practicum.myplaylistmaker.data.playListDB.PlaylistRepositoryImpl
import com.practicum.myplaylistmaker.domain.favorites.PlaylistInteractor
import com.practicum.myplaylistmaker.domain.favorites.PlaylistInteractorImpl
import com.practicum.myplaylistmaker.ui.favorites.viewModel.PlaylistViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistModule = module {
    single {
        Room.databaseBuilder(androidContext(), PlayistDataBase::class.java, "playlist_table")
            .allowMainThreadQueries()
            .build()
    }

    factory { PlaylistConverter() }

    single <PlaylistRepositoryImpl> {
        PlaylistRepositoryImpl(get(),get(),get())
    }

    single <PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

    viewModel {
        PlaylistViewModel(get())
    }
}