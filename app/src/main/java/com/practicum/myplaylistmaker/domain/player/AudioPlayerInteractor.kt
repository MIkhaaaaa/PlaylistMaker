package com.practicum.myplaylistmaker.domain.player

import com.practicum.myplaylistmaker.domain.models.PlayerState
import kotlinx.coroutines.flow.Flow

interface AudioPlayerInteractor {
    fun play()
    fun pause()
    fun timeTransfer() :Flow<String>
    fun preparePlayer(url: String, state: PlayerState)
    fun playerStateReporter() : PlayerState


}
