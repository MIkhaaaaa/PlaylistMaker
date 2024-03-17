package com.practicum.myplaylistmaker.di.playlist

import androidx.room.Room
import com.practicum.myplaylistmaker.data.db.converters.PlaylistConverter
import com.practicum.myplaylistmaker.data.db.dao.PlayistDataBase
import com.practicum.myplaylistmaker.data.db.impl.PlaylistRepositoryImpl
import com.practicum.myplaylistmaker.domain.playlist.PlaylistInteractor
import com.practicum.myplaylistmaker.domain.playlist.PlaylistInteractorImpl
import com.practicum.myplaylistmaker.domain.playlist.PlaylistRepository
import com.practicum.myplaylistmaker.ui.library.viewModels.NewPlaylistViewModel
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

    single <PlaylistRepository> {
        PlaylistRepositoryImpl(get(),get(),get())
    }

    single <PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

    viewModel {
        NewPlaylistViewModel(get(), get())
    }
}