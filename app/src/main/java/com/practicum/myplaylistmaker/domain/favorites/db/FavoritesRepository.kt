package com.practicum.myplaylistmaker.domain.favorites.db

import com.practicum.myplaylistmaker.data.search.requestAndResponse.TrackDto
import com.practicum.myplaylistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun addTrack (track:TrackDto)
    fun deleteTrack (track: TrackDto)
    fun getFavourites():Flow<List<TrackDto>>
    fun checkFavourites(id:Long):Flow<Boolean>
}