package com.practicum.myplaylistmaker.data.player

import android.annotation.SuppressLint
import android.media.MediaPlayer
import com.practicum.myplaylistmaker.domain.models.PlayerState
import com.practicum.myplaylistmaker.domain.player.AudioPlayerRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer ) : AudioPlayerRepository {
    var playerState = PlayerState.STATE_DEFAULT
    override fun play() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
    }


    override fun preparePlayer(url: String, state: PlayerState) {
        if (state != PlayerState.STATE_DEFAULT) return
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState =  PlayerState.STATE_PREPARED
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun timeTransfer(): Flow<String> = flow {
        val sdf = SimpleDateFormat("mm:ss")
            while (true){
                if ((playerState == PlayerState.STATE_PLAYING) or (playerState == PlayerState.STATE_PAUSED)){
                    emit(sdf.format(mediaPlayer.currentPosition))
                } else {
                    emit("00:00")
                }
                delay(DELAY_MILLIS)
            }
    }


    override fun playerStateReporter(): PlayerState {
        return playerState
    }

    companion object {
        const val DELAY_MILLIS = 300L
    }

}