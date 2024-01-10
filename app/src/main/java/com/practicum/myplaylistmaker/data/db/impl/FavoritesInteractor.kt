package com.practicum.myplaylistmaker.data.db.impl

import com.practicum.myplaylistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow


interface FavoritesInteractor {
    fun favouritesAdd (track:Track)
    fun favouritesDelete (track: Track)
    fun favouritesGet(): Flow<List<Track>>
    fun favouritesCheck(id:Long):Flow<Boolean>
}