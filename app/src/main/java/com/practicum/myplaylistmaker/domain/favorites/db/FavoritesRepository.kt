package com.practicum.myplaylistmaker.domain.favorites.db

import com.practicum.myplaylistmaker.data.search.requestAndResponse.TrackDto
import com.practicum.myplaylistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun addTrack (track:Track)
    fun deleteTrack (track: Track)
    fun getFavourites():Flow<List<Track>>
    fun checkFavourites(id:Long):Flow<Boolean>
}