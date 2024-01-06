package com.practicum.myplaylistmaker.data.db.impl

import com.practicum.myplaylistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow


interface FavoritesInteractor {
    fun favoritesTracks() : Flow<ArrayList<Track>>
}