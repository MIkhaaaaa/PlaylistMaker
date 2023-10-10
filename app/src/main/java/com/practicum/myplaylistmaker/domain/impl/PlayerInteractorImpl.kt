package com.practicum.myplaylistmaker.domain.impl

import com.practicum.myplaylistmaker.domain.api.AudioPlayerInteractor
import com.practicum.myplaylistmaker.domain.api.AudioPlayerRepository
import com.practicum.myplaylistmaker.domain.models.PlayerState

class PlayerInteractorImpl(private val mediaPlayer: AudioPlayerRepository) : AudioPlayerInteractor  {
    override fun play() {
        mediaPlayer.play()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun preparePlayer(url: String,listener: AudioPlayerInteractor.PlayerStateListener) {
        mediaPlayer.preparePlayer(url)
        listener.onStateChanged(playerStateReporter())

    }
    override fun timeTransfer(): String {
        return mediaPlayer.timeTransfer()
    }

    override fun playerStateReporter(): PlayerState {
        return mediaPlayer.playerStateReporter()
    }

}