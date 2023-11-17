package com.practicum.myplaylistmaker.di.search

import com.practicum.myplaylistmaker.domain.player.TracksInteractor
import com.practicum.myplaylistmaker.domain.player.impl.TracksInteractorImpl
import com.practicum.myplaylistmaker.domain.search.SharedPreferencesInteractor
import com.practicum.myplaylistmaker.domain.search.SharedPreferencesInteractorImpl
import org.koin.dsl.module

val searchInteractorModule = module {
    single <TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single <SharedPreferencesInteractor> {
        SharedPreferencesInteractorImpl(get())
    }

}