package com.practicum.myplaylistmaker.domain.player

import com.practicum.myplaylistmaker.domain.models.PlayerState
import kotlinx.coroutines.flow.Flow

interface AudioPlayerRepository {
    fun play()
    fun pause()
    fun preparePlayer(url: String, state: PlayerState)
    fun timeTransfer(): Flow<String>
    fun playerStateReporter(): PlayerState

}


