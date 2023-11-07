package com.practicum.myplaylistmaker.domain.player

import com.practicum.myplaylistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTrack: ArrayList<Track>?, errorMessage: String?)
    }
}