package com.practicum.myplaylistmaker.data.playListDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_in_playlist_table")
data class TrackInPlaylistEntity(
    @PrimaryKey
    val trackId: Long?,
    val addTime: Long?,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: String?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    var isFavorite: Boolean = false
)