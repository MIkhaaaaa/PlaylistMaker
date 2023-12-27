package com.practicum.myplaylistmaker.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myplaylistmaker.domain.models.PlayerState
import com.practicum.myplaylistmaker.domain.player.AudioPlayerInteractor
import com.practicum.myplaylistmaker.ui.player.ActivityMediaPlayer.Companion.DELAY_PAUSE
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: AudioPlayerInteractor,
):ViewModel(){

    var stateLiveData = MutableLiveData<PlayerState>()
    private var isClickAllowed = true
    var playJob : Job? = null
    var timer = MutableLiveData("00:00")
    init {
        stateLiveData.value = playerStateListener()
    }

    fun createPlayer(url: String){
        stateLiveData.value?.let {
            playerInteractor.preparePlayer(url, it)
        }
    }
    fun play() {
        playerInteractor.play()
        getTimeLiveData()
        playJob?.start()
    }
    fun pause() {
        playerInteractor.pause()
        playJob?.cancel()
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

    fun getTimeLiveData(): LiveData<String> {
        playJob =viewModelScope.launch {
            while (true) {
                delay(PLAYER_BUTTON_PRESSING_DELAY)
                playerInteractor.timeTransfer().collect() {
                    timer.postValue(it)
                }
            }
        }
        return timer
    }

    fun jobCancel(){
        playJob?.cancel()
    }
    fun resume(){
        playJob?.start()
    }

    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY = 300L
    }
}