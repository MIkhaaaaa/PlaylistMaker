package com.practicum.myplaylistmaker.domain.impl

import com.practicum.myplaylistmaker.domain.api.AudioPlayerInteractor
import com.practicum.myplaylistmaker.domain.api.AudioPlayerRepository
import com.practicum.myplaylistmaker.domain.models.PlayerState

class PlayerInteractorImpl(private val mediaPlayer: AudioPlayerRepository) : AudioPlayerInteractor  {
    var playerState = PlayerState.STATE_DEFAULT
    override fun play() {
        mediaPlayer.play()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun preparePlayer(url: String,listener: AudioPlayerRepository.PlayerStateListener) {
        mediaPlayer.preparePlayer(url, listener)

    }
    override fun timeTransfer(): String {
        return mediaPlayer.timeTransfer()
    }

    override fun playerStateReporter(): PlayerState {
        return mediaPlayer.playerStateReporter()
    }

}