package com.practicum.myplaylistmaker.data

import android.annotation.SuppressLint
import android.media.MediaPlayer
import com.practicum.myplaylistmaker.domain.api.AudioPlayerRepository
import com.practicum.myplaylistmaker.domain.models.PlayerState
import java.text.SimpleDateFormat

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer ) : AudioPlayerRepository{
    var playerState = PlayerState.STATE_DEFAULT
    override fun play() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
    }


    override fun preparePlayer(url: String, listener: AudioPlayerRepository.PlayerStateListener) {
        if (playerState != PlayerState.STATE_DEFAULT) return
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
            listener.onStateChanged(playerState)
        }
        mediaPlayer.setOnCompletionListener {
            playerState =  PlayerState.STATE_PREPARED
            listener.onStateChanged(playerState)
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun timeTransfer(): String {
        val sdf = SimpleDateFormat("mm:ss")
        return sdf.format(mediaPlayer.currentPosition)
    }

    override fun playerStateReporter(): PlayerState {
        return playerState
    }



}