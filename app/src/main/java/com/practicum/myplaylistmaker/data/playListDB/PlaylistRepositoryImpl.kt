package com.practicum.myplaylistmaker.data.playListDB

import com.practicum.myplaylistmaker.domain.favorites.PlaylistRepository
import com.practicum.myplaylistmaker.domain.models.Playlist
import com.practicum.myplaylistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val playistDataBase: PlayistDataBase,
    private val converter: PlaylistConverter,
    private val trackInDataBase: TrackInPlaylistDataBase
) : PlaylistRepository {

    override fun addPlaylist(
        playlistName: String,
        description: String?,
        uri: String
    ) {
        val playlist = Playlist(
            null,
            playlistName,
            description,
            uri,
            emptyList(),
            0
        )
        playistDataBase.playlistDao().insertPlaylist(
            converter.mapClassToEntity(playlist)
        )
    }

    override fun deletePlaylist(item: Playlist) {
        converter.mapClassToEntity(item)
            .let { playistDataBase.playlistDao().deletePlaylist(it) }
    }

    override fun queryPlaylist(): Flow<List<Playlist>> = flow {
        val playlistConverted =
            playistDataBase.playlistDao().queryPlaylist()
                .map { converter.mapEntityToClass(it) }
        emit(playlistConverted)
        return@flow

    }

    override fun update(track: Track, playlist: Playlist) {

        playistDataBase.playlistDao().updatePlaylist(converter.mapClassToEntity(playlist))
        trackInDataBase.trackListingDao().insertTrack(track)

    }

    override fun savePlaylist(
        playlist: Playlist,
        playlistName: String,
        description: String?,
        uri: String
    ) {
        TODO("Not yet implemented")
    }

    override fun findPlaylist(searchId: Int): Flow<Playlist> {
        TODO("Not yet implemented")
    }

    override fun getPlayList(): Playlist {
        TODO("Not yet implemented")
    }


}