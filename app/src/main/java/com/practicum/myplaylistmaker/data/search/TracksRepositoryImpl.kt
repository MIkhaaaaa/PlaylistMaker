package com.practicum.myplaylistmaker.data.search

import com.practicum.myplaylistmaker.data.db.converters.TrackDbConvertor
import com.practicum.myplaylistmaker.data.db.dao.AppDatabase
import com.practicum.myplaylistmaker.data.search.requestAndResponse.NetworkClient
import com.practicum.myplaylistmaker.data.search.requestAndResponse.TrackDto
import com.practicum.myplaylistmaker.data.search.requestAndResponse.TrackRequest
import com.practicum.myplaylistmaker.data.search.requestAndResponse.TrackResponce
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.domain.player.TracksRepository
import com.practicum.myplaylistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val trackDbConvertor : TrackDbConvertor,
    private val appDatabase: AppDatabase
) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<ArrayList<Track>>> = flow {
        try {
            val response = networkClient.doRequest(TrackRequest(expression))

            when (response.resultCode) {
                -1 -> {
                   emit(Resource.Error("Проверьте подключение к интернету", null))
                }

                200 -> {
                   emit(Resource.Success((response as TrackResponce).results.map {
                        Track(
                            it.trackName, it.artistName,
                            it.trackTimeMillis, it.artworkUrl100, it.trackId,
                            it.collectionName, it.releaseDate,
                            it.primaryGenreName, it.country, it.previewUrl
                        )
                    } as ArrayList<Track>))
                }

                else -> {
                    emit(Resource.Error("Ошибка сервера", null))
                }
            }

        } catch (e: Error){
            throw Exception(e)
        }
    }

    private suspend fun saveTracks(tracks: ArrayList<TrackDto> ){
        val tracksEntity =  tracks.map { track -> trackDbConvertor.map(track)}
        appDatabase.trackDao().insertTracks(tracksEntity)
    }


}
