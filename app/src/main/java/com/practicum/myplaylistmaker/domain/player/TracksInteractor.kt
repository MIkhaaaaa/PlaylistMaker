package com.practicum.myplaylistmaker.domain.player

import com.practicum.myplaylistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(expression: String) : Flow<Pair<ArrayList<Track>?,String?>>

}