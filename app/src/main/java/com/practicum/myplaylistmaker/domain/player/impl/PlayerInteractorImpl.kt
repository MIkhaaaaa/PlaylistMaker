package com.practicum.myplaylistmaker.domain.player.impl

import com.practicum.myplaylistmaker.domain.player.AudioPlayerInteractor
import com.practicum.myplaylistmaker.domain.player.AudioPlayerRepository
import com.practicum.myplaylistmaker.domain.models.PlayerState

class PlayerInteractorImpl(private val mediaPlayer: AudioPlayerRepository) : AudioPlayerInteractor {
    override fun play() {
        mediaPlayer.play()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun preparePlayer(url: String, state: PlayerState) {
        mediaPlayer.preparePlayer(url,state)
    }
    override fun timeTransfer(): String {
        return mediaPlayer.timeTransfer()
    }

    override fun playerStateReporter(): PlayerState {
        return mediaPlayer.playerStateReporter()
    }

}