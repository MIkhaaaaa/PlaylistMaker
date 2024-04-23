package com.practicum.myplaylistmaker.data.db.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.myplaylistmaker.data.db.entity.PlaylistEntity
import com.practicum.myplaylistmaker.domain.models.Playlist

class PlaylistConverter(private val gson: Gson) {
    fun mapplaylistEntityToClass(item: PlaylistEntity): Playlist {

        return Playlist(
            item.playlistId,
            item.playlistName,
            item.description,
            item.uri,
            gson.fromJson(item.trackList,  object : TypeToken<List<Long>>() {}.type),
            item.arrayNumber
        )
    }

    fun mapplaylistClassToEntity(item: Playlist): PlaylistEntity {
        return PlaylistEntity(
            item.playlistId,
            item.playlistName,
            item.description,
            item.uri,
            gson.toJson(item.trackArray),
            item.arrayNumber
        )
    }
}