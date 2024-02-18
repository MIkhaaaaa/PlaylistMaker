package com.practicum.myplaylistmaker.ui.mediaLibrary

import androidx.lifecycle.ViewModel
import com.practicum.myplaylistmaker.domain.favorites.PlaylistInteractor
import com.practicum.myplaylistmaker.domain.models.Playlist
import com.practicum.myplaylistmaker.domain.settings.SettingsInteractor

class EditPlaylistViewModel(
    private val interactor: PlaylistInteractor
    ,private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    fun addPlayList(
        playlistName: String,
        description: String?,
        uri: String
    ) {
        interactor.addPlaylist(playlistName, description, uri)
    }

    fun deletePlaylist(item: Playlist) {
        interactor.deletePlaylist(item)
    }

    fun isAppThemeDark() :Boolean{
        return settingsInteractor.isDayOrNight()
    }
}