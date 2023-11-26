package com.practicum.myplaylistmaker.di.favorites

import com.practicum.myplaylistmaker.ui.favorites.viewModel.FavouritesViewModel
import com.practicum.myplaylistmaker.ui.favorites.viewModel.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favorites = module {
    viewModel { FavouritesViewModel() }
    viewModel { PlaylistViewModel() }
}