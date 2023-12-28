package com.practicum.myplaylistmaker.domain.player

import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<Resource<ArrayList<Track>>>
}