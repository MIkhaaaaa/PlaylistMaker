package com.practicum.myplaylistmaker.data.playListDB

import com.practicum.myplaylistmaker.domain.models.Playlist

class PlaylistConverter {
    fun mapPlaylistEntityToClass(item: PlaylistEntity): Playlist {

        return Playlist(
            item.playlistId,
            item.playlistName,
            item.description,
            item.uri,
            item.trackList,
            item.arrayNumber
        )
    }

    fun mapPlaylistClassToEntity(item: Playlist): PlaylistEntity {
        return PlaylistEntity(
            item.playlistId,
            item.playlistName,
            item.description,
            item.uri,
            item.trackArray,
            item.arrayNumber
        )
    }
}