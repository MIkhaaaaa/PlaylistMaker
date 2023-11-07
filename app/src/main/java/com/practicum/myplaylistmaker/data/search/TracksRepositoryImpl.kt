package com.practicum.myplaylistmaker.data.search

import com.practicum.myplaylistmaker.data.search.requestAndResponse.NetworkClient
import com.practicum.myplaylistmaker.data.search.requestAndResponse.TrackRequest
import com.practicum.myplaylistmaker.data.search.requestAndResponse.TrackResponce
import com.practicum.myplaylistmaker.domain.player.TracksRepository
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.util.Resource

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): Resource<ArrayList<Track>> {
        val response = networkClient.doRequest(TrackRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету", null)
            }

            200 -> {
                Resource.Success((response as TrackResponce).results.map {
                    Track(
                        it.trackName, it.artistName,
                        it.trackTimeMillis, it.artworkUrl100, it.trackId,
                        it.collectionName, it.releaseDate,
                        it.primaryGenreName, it.country, it.previewUrl
                    )
                } as ArrayList<Track>)
            }

            else -> {
                Resource.Error("Ошибка сервера", null)
            }
        }
    }
}
