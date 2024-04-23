package com.practicum.myplaylistmaker.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.myplaylistmaker.di.favorites.favorites
import com.practicum.myplaylistmaker.di.player.playerModule
import com.practicum.myplaylistmaker.di.playlist.playlistModule
import com.practicum.myplaylistmaker.di.search.historyModule
import com.practicum.myplaylistmaker.di.search.repositoryModule
import com.practicum.myplaylistmaker.di.search.searchInteractorModule
import com.practicum.myplaylistmaker.di.search.searchRepositoryModule
import com.practicum.myplaylistmaker.di.search.searchViewModelModule
import com.practicum.myplaylistmaker.di.settings.shareSettingsViewModule
import com.practicum.myplaylistmaker.domain.settings.SettingsInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
const val HISTORY_KEY = "history_key"

class App: Application(),KoinComponent {
    var currentTheme: Boolean = false

    companion object {
        lateinit var instance: App
            private set

        lateinit var savedHistory: SharedPreferences
    }
    override fun onCreate() {
        super.onCreate()

        savedHistory = applicationContext.getSharedPreferences(HISTORY_KEY, Context.MODE_PRIVATE)


        switchTheme(currentTheme)
         startKoin {
            androidContext(this@App)
            modules(
                playerModule,
                searchInteractorModule,
                searchRepositoryModule,
                historyModule,
                searchViewModelModule,
                shareSettingsViewModule,
                repositoryModule,
                favorites,
                playlistModule
            )
        }


       val settingInteractor = getKoin().get<SettingsInteractor>()
        instance = this
        currentTheme = settingInteractor.isDayOrNight()
        switchTheme(currentTheme)

    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_NO
            } else {
                AppCompatDelegate.MODE_NIGHT_YES
            }
        )
    }

}