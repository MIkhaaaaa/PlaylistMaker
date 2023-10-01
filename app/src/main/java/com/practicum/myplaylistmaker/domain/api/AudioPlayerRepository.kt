package com.practicum.myplaylistmaker.domain.api

import com.practicum.myplaylistmaker.domain.models.PlayerState

interface AudioPlayerRepository {
    fun play()
    fun pause()
    fun preparePlayer(url: String, listener: PlayerStateListener)
    fun timeTransfer(): String
    fun playerStateReporter(): PlayerState
    interface PlayerStateListener {
        fun onStateChanged(state: PlayerState)
    }
}


