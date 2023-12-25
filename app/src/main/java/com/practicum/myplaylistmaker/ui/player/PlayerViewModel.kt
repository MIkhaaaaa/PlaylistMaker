package com.practicum.myplaylistmaker.ui.player

import android.util.Log
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
        Log.d("stateLiveData", stateLiveData.value.toString())
    }
    fun play() {
        playerInteractor.play()
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

    fun putTime(): LiveData<String> {
        getTimeLiveData()
        timer.value?.let { Log.d("время в модели", it) }
        return timer
    }

    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY = 300L
    }
}