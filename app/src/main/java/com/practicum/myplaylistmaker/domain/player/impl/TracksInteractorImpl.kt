package com.practicum.myplaylistmaker.domain.player.impl

import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.domain.player.TracksInteractor
import com.practicum.myplaylistmaker.domain.player.TracksRepository
import com.practicum.myplaylistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> {
        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    (Resource.Success(result.data))
                }

                is Resource.Error<*> -> {
                    Resource.Error("null", result.message)
                }
            } as Resource<List<Track>>
        }
    }

}



