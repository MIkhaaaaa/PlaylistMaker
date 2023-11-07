package com.practicum.myplaylistmaker.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.myplaylistmaker.domain.models.PlayerState
import com.practicum.myplaylistmaker.domain.player.AudioPlayerInteractor
import com.practicum.myplaylistmaker.util.Creator

class PlayerViewModel(
    private val playerInteractor: AudioPlayerInteractor,
):ViewModel(){

    fun createPlayer(url: String, listener: AudioPlayerInteractor.PlayerStateListener) {
        playerInteractor.preparePlayer(url, listener)
    }
    fun play() {
        playerInteractor.play()
    }
    fun pause() {
        playerInteractor.pause()
    }

    fun getTime(): String {
        return playerInteractor.timeTransfer()
    }

    fun playerStateListener(): PlayerState {
        return playerInteractor.playerStateReporter()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                // 1
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayerViewModel(
                        Creator.providePlayerInteractor()
                    ) as T
                }
            }
    }
}