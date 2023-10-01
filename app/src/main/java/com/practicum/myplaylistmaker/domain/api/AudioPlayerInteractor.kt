package com.practicum.myplaylistmaker.domain.api

import com.practicum.myplaylistmaker.domain.models.PlayerState

interface AudioPlayerInteractor {
    fun play()
    fun pause()
    fun preparePlayer(url: String,listener: AudioPlayerRepository.PlayerStateListener)
    fun timeTransfer() :String
    fun playerStateReporter() : PlayerState


}
