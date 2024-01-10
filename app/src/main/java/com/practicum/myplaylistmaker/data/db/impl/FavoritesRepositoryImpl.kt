package com.practicum.myplaylistmaker.data.db.impl

import android.util.Log
import com.practicum.myplaylistmaker.data.db.converters.TrackDbConvertor
import com.practicum.myplaylistmaker.data.db.dao.AppDatabase
import com.practicum.myplaylistmaker.data.search.requestAndResponse.TrackDto
import com.practicum.myplaylistmaker.domain.favorites.db.FavoritesRepository
import com.practicum.myplaylistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val dataBase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
    ) : FavoritesRepository{

    override fun addTrack(track: Track) {
        track.isFavorite = true
        track.addTime = System.currentTimeMillis()
        dataBase.favouritesTrackDao().insertTrack(track)
    }

    override fun deleteTrack(track: Track) {
        track.isFavorite = false
        trackDbConvertor.mapTrackToFavourite(track)?.let { dataBase.favouritesTrackDao().deleteTrack(it) }
    }

    override fun getFavourites(): Flow<List<Track>> = flow {
        val favourites = dataBase.favouritesTrackDao().queryTrack()

        if (favourites != null) {
            val favouritesConverted =
                dataBase.favouritesTrackDao().queryTrack().map { trackDbConvertor.mapFavouriteToTrack(it) }
            emit(favouritesConverted)
        } else {
            emit(emptyList())
        }
    }


    override fun checkFavourites(id: Long): Flow<Boolean> = flow {
        emit(dataBase.favouritesTrackDao().queryTrackId(id) != null)
    }

}