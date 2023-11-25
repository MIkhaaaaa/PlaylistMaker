package com.practicum.myplaylistmaker.di.search

import com.practicum.myplaylistmaker.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchViewModelModule = module {
    viewModel {
        SearchViewModel(
            get(),
            get()
        )
    }
}