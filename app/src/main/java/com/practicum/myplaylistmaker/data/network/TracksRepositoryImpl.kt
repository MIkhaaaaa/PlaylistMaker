package com.practicum.myplaylistmaker.data.network

import com.practicum.myplaylistmaker.data.NetworkClient
import com.practicum.myplaylistmaker.data.dto.TrackRequest
import com.practicum.myplaylistmaker.data.dto.TrackResponce
import com.practicum.myplaylistmaker.domain.api.TracksRepository
import com.practicum.myplaylistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient): TracksRepository {
    override fun searchTracks(expression: String): ArrayList<Track> {
        val response = networkClient.doRequest(TrackRequest(expression))
        if (response.resultCode == 200){
            return (response as TrackResponce).results.map {
                Track(it.trackName,it.artistName,
                    it.trackTimeMillis,it.artworkUrl100,it.trackId,
                    it.collectionName,it.releaseDate,
                    it.primaryGenreName,it.country,it.previewUrl)
            } as ArrayList<Track>
        } else {
            return ArrayList()
        }
    }
}