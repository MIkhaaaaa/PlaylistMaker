package com.practicum.myplaylistmaker.ui.library.viewModels

import androidx.lifecycle.ViewModel
import com.practicum.myplaylistmaker.domain.models.Playlist
import com.practicum.myplaylistmaker.domain.playlist.PlaylistInteractor

class EditPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    fun savePlayList(
        playlist: Playlist,
        playlistName: String,
        description: String?,
        uri: String
    ) {
        playlistInteractor.savePlaylist(playlist, playlistName, description, uri)
    }
    fun deletePlaylist (playlist: Playlist) {
        playlistInteractor.deletePlaylist(playlist)
    }
}