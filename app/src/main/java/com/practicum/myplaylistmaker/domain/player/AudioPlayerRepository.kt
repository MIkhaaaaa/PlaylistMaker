package com.practicum.myplaylistmaker.domain.player

import com.practicum.myplaylistmaker.domain.models.PlayerState

interface AudioPlayerRepository {
    fun play()
    fun pause()
    fun preparePlayer(url: String, state: PlayerState)
    fun timeTransfer(): String
    fun playerStateReporter(): PlayerState

}


