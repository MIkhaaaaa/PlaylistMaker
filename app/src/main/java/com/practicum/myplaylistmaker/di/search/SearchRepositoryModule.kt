package com.practicum.myplaylistmaker.di.search

import com.practicum.myplaylistmaker.data.search.TracksRepositoryImpl
import com.practicum.myplaylistmaker.data.search.history.TrackHistoryRepositoryImpl
import com.practicum.myplaylistmaker.domain.player.TracksRepository
import com.practicum.myplaylistmaker.domain.search.SharedPreferencesRepository
import org.koin.dsl.module

val searchRepositoryModule = module {
    single <TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single <SharedPreferencesRepository> {
        TrackHistoryRepositoryImpl(get())
    }

}