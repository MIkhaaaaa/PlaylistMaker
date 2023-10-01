package com.practicum.myplaylistmaker.domain.api

import com.practicum.myplaylistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTrack: ArrayList<Track>)
    }
}