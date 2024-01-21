package com.practicum.myplaylistmaker.data.search.responce

data class TrackDto(
    val trackName: String?,
    var addTime:Long?,
    val artistName: String?,
    val trackTimeMillis: String?,
    val artworkUrl100: String?,
    val trackId: Long?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    var isFavorite: Boolean = false
)