package com.practicum.myplaylistmaker.domain.api

import com.practicum.myplaylistmaker.domain.models.PlayerState

interface AudioPlayerRepository {
    fun play()
    fun pause()
    fun preparePlayer(url: String)
    fun timeTransfer(): String
    fun playerStateReporter(): PlayerState

}


