package com.practicum.myplaylistmaker.ui.library.viewModels

import androidx.lifecycle.ViewModel
import com.practicum.myplaylistmaker.domain.playlist.PlaylistInteractor
import com.practicum.myplaylistmaker.domain.settings.SettingsInteractor

class NewPlaylistViewModel(private val interactor: PlaylistInteractor, private val settingsInteractor: SettingsInteractor) : ViewModel() {

    fun addPlayList(
        playlistName: String,
        description: String?,
        uri: String
    ) {
        interactor.addPlaylist(playlistName, description, uri)
    }

    fun isAppThemeDark() :Boolean{
        return settingsInteractor.isDayOrNight()
    }
}