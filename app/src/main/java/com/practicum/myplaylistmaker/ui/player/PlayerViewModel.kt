package com.practicum.myplaylistmaker.ui.player

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myplaylistmaker.domain.models.PlayerState
import com.practicum.myplaylistmaker.domain.player.AudioPlayerInteractor
import com.practicum.myplaylistmaker.ui.player.ActivityMediaPlayer.Companion.DELAY_PAUSE
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: AudioPlayerInteractor,
):ViewModel(){

    var stateLiveData = MutableLiveData<PlayerState>()
    private var isClickAllowed = true
    init {
        stateLiveData.value = playerStateListener()
    }

    fun createPlayer(url: String){

        stateLiveData.value?.let {
            playerInteractor.preparePlayer(url, it)
        }
        Log.d("stateLiveData", stateLiveData.value.toString())
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

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(DELAY_PAUSE)
                isClickAllowed = true
            }

        }
        return current
    }
}