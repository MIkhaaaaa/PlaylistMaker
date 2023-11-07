package com.practicum.myplaylistmaker.domain.player

import com.practicum.myplaylistmaker.domain.models.PlayerState

interface AudioPlayerInteractor {
    fun play()
    fun pause()
    fun timeTransfer() :String
    fun preparePlayer(url: String,listener: PlayerStateListener)
    fun playerStateReporter() : PlayerState

    interface PlayerStateListener {
        fun onStateChanged(state: PlayerState)
    }

}
