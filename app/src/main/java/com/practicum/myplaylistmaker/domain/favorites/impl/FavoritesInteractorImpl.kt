package com.practicum.myplaylistmaker.domain.favorites.impl

import com.practicum.myplaylistmaker.domain.favorites.db.FavoritesInteractor
import com.practicum.myplaylistmaker.domain.favorites.db.FavoritesRepository
import com.practicum.myplaylistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val favoritesRepository: FavoritesRepository) :
    FavoritesInteractor {

    override fun favouritesAdd(track:Track) {
        favoritesRepository.addTrack(track)
    }

    override fun favouritesDelete(track: Track) {
        favoritesRepository.deleteTrack(track)
    }

    override fun favouritesGet(): Flow<List<Track>> {
        return favoritesRepository.getFavourites()
    }

    override fun favouritesCheck(id: Long): Flow<Boolean> {
        return favoritesRepository.checkFavourites(id)
    }
}