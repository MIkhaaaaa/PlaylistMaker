package com.practicum.myplaylistmaker.domain.favorites.db

import com.practicum.myplaylistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun favoritesTrack(): Flow<ArrayList<Track>>
}