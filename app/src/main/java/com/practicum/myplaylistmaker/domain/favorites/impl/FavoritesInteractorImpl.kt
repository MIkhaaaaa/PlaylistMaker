package com.practicum.myplaylistmaker.domain.favorites.impl

import com.practicum.myplaylistmaker.data.db.impl.FavoritesInteractor
import com.practicum.myplaylistmaker.domain.favorites.db.FavoritesRepository
import com.practicum.myplaylistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository) : FavoritesInteractor {
    override fun favoritesTracks(): Flow<ArrayList<Track>> {
        return favoritesRepository.favoritesTrack()
    }


}