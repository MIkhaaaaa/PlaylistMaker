package com.practicum.myplaylistmaker.di.playlist

import com.practicum.myplaylistmaker.data.playlistsScreen.PlaylistScreenRepositoryImpl
import com.practicum.myplaylistmaker.domain.playlistScreen.PlaylistScreenInteractor
import com.practicum.myplaylistmaker.domain.playlistScreen.PlaylistScreenRepository
import com.practicum.myplaylistmaker.domain.playlistScreen.impl.PlaylistScreenInteractorImpl
import com.practicum.myplaylistmaker.ui.library.viewModels.EditPlaylistViewModel
import com.practicum.myplaylistmaker.ui.library.viewModels.PlaylistScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistScreenModule = module {

    single<PlaylistScreenInteractor> { PlaylistScreenInteractorImpl(get()) }
    single<PlaylistScreenRepository> { PlaylistScreenRepositoryImpl(get()) }

    viewModel { PlaylistScreenViewModel(get(), get(), get()) }
    viewModel { EditPlaylistViewModel(get()) }
}