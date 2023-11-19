package com.practicum.myplaylistmaker.domain.player

import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.util.Resource

interface TracksRepository {
    fun searchTracks(expression: String): Resource<ArrayList<Track>>
}