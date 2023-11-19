package com.practicum.myplaylistmaker.di.search

import com.practicum.myplaylistmaker.data.search.history.TrackHistoryRepositoryImpl
import com.practicum.myplaylistmaker.domain.search.SharedPreferencesInteractor
import com.practicum.myplaylistmaker.domain.search.SharedPreferencesInteractorImpl
import com.practicum.myplaylistmaker.domain.search.SharedPreferencesRepository
import org.koin.dsl.module

val historyModule = module {
    single <SharedPreferencesInteractor> {
        SharedPreferencesInteractorImpl(get())
    }

    single<SharedPreferencesRepository> {
        TrackHistoryRepositoryImpl(get(),get())
    }
}