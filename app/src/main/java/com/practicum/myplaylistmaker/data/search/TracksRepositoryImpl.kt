package com.practicum.myplaylistmaker.data.search

import com.practicum.myplaylistmaker.data.search.request.NetworkClient
import com.practicum.myplaylistmaker.data.search.request.TrackRequest
import com.practicum.myplaylistmaker.data.search.responce.TrackResponce
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.domain.player.TracksRepository
import com.practicum.myplaylistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TracksRepositoryImpl(
    private val networkClient: NetworkClient
) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<ArrayList<Track>>> = flow {
        try {
            val response = networkClient.doRequest(TrackRequest(expression))

            when (response.resultCode) {
                -1 -> {
                   emit(Resource.Error("Проверьте подключение к интернету", null))
                }

                200 -> {
                   emit(Resource.Success((response as TrackResponce).results.map { track ->
                        Track(
                            track.trackName,
                            addTime = System.currentTimeMillis(),
                            track.artistName,
                            track.trackTimeMillis,
                            track.artworkUrl100,
                            track.trackId,
                            track.collectionName,
                            track.releaseDate,
                            track.primaryGenreName,
                            track.country,
                            track.previewUrl,
                            track.isFavorite
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


}
