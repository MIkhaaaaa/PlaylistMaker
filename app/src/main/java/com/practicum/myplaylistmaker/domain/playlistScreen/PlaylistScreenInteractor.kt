package com.practicum.myplaylistmaker.domain.playlistScreen

import com.practicum.myplaylistmaker.domain.models.Playlist
import com.practicum.myplaylistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistScreenInteractor {
    fun getTrackList (playlist: Playlist) : Flow<List<Track>>
    fun timeCounting (playlist: Playlist) : Flow<String>
}