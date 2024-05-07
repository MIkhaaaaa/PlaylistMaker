package com.practicum.myplaylistmaker.domain.playlistScreen.impl

import com.practicum.myplaylistmaker.domain.models.Playlist
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.domain.playlistScreen.PlaylistScreenInteractor
import com.practicum.myplaylistmaker.domain.playlistScreen.PlaylistScreenRepository
import kotlinx.coroutines.flow.Flow

class PlaylistScreenInteractorImpl(private val repository: PlaylistScreenRepository) :
    PlaylistScreenInteractor {

    override fun getTrackList(playlist: Playlist): Flow<List<Track>> {
        return repository.getTrackList(playlist)
    }

    override fun timeCounting(playlist: Playlist): Flow<String> {
        return  repository.timeCounting(playlist)
    }
}