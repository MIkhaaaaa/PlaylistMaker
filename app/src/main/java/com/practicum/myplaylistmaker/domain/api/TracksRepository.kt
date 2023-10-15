package com.practicum.myplaylistmaker.domain.api

import com.practicum.myplaylistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): ArrayList<Track>
}