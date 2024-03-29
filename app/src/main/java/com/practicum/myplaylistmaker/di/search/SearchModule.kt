package com.practicum.myplaylistmaker.di.search

import android.content.Context
import com.google.gson.Gson
import com.practicum.myplaylistmaker.app.HISTORY_KEY
import com.practicum.myplaylistmaker.data.search.request.ITunesAPI
import com.practicum.myplaylistmaker.data.search.request.NetworkClient
import com.practicum.myplaylistmaker.data.search.request.RetrofitNetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val repositoryModule = module {

    single<ITunesAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesAPI::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences(HISTORY_KEY, Context.MODE_PRIVATE)
    }

    factory {
        Gson()
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }
}