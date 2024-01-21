package com.practicum.myplaylistmaker.di.favorites

import androidx.room.Room
import com.practicum.myplaylistmaker.data.db.converters.TrackDbConvertor
import com.practicum.myplaylistmaker.data.db.dao.AppDatabase
import com.practicum.myplaylistmaker.domain.favorites.db.FavoritesInteractor
import com.practicum.myplaylistmaker.data.db.impl.FavoritesRepositoryImpl
import com.practicum.myplaylistmaker.domain.favorites.db.FavoritesRepository
import com.practicum.myplaylistmaker.domain.favorites.impl.FavoritesInteractorImpl
import com.practicum.myplaylistmaker.ui.favorites.viewModel.FavouritesViewModel
import com.practicum.myplaylistmaker.ui.favorites.viewModel.PlaylistViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favorites = module {
    viewModel { FavouritesViewModel(get(),get()) }
    viewModel { PlaylistViewModel() }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database")
            .allowMainThreadQueries()
            .build()
    }
    factory { TrackDbConvertor() }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }


}