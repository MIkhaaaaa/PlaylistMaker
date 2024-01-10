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

    override fun addTrack(track: TrackDto) {
        track.isFavorite = true
        track.addTime = System.currentTimeMillis()
        dataBase.favouritesDao().insertTrack(track)
    }

    override fun deleteTrack(track: TrackDto) {
        track.isFavorite = false
        trackDbConvertor.mapTrackToFavourite(track)?.let { dataBase.favouritesDao().deleteTrack(it) }
    }

    override fun getFavourites(): Flow<List<TrackDto>> = flow {
        val favourites = dataBase.favouritesDao().queryTrack()

        Log.d("Избранное репозиторий", favourites.toString())
        if (favourites != null) {
            val favouritesConverted =
                dataBase.favouritesDao().queryTrack().map { trackDbConvertor.mapFavouriteToTrack(it) }
            emit(favouritesConverted)
        } else {
            emit(emptyList())
        }
    }


    override fun checkFavourites(id: Long): Flow<Boolean> = flow {
        emit(dataBase.favouritesDao().queryTrackId(id) != null)
    }

}