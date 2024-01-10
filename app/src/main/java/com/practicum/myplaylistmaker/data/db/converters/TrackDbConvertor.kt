package com.practicum.myplaylistmaker.data.db.converters

import com.practicum.myplaylistmaker.data.db.entity.TrackEntity
import com.practicum.myplaylistmaker.data.search.requestAndResponse.TrackDto
import com.practicum.myplaylistmaker.domain.models.Track

class TrackDbConvertor {
    fun mapTrackToFavourite(track: TrackDto): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.addTime,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isFavorite
        )
    }

    fun mapFavouriteToTrack(track: TrackEntity): TrackDto {
        return TrackDto(
            track.trackName,
            track.addTime,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.trackId,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

}