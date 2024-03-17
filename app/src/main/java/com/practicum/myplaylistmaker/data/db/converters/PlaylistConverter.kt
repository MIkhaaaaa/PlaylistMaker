package com.practicum.myplaylistmaker.data.db.converters

import com.practicum.myplaylistmaker.data.db.entity.PlaylistEntity
import com.practicum.myplaylistmaker.domain.models.Playlist

class PlaylistConverter {
    fun mapplaylistEntityToClass(item: PlaylistEntity): Playlist {

        return Playlist(
            item.playlistId,
            item.playlistName,
            item.description,
            item.uri,
            item.trackList,
            item.arrayNumber
        )
    }

    fun mapplaylistClassToEntity(item: Playlist): PlaylistEntity {
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