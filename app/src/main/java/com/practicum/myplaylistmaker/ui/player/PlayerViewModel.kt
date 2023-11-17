package com.practicum.myplaylistmaker.ui.player

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.myplaylistmaker.domain.models.PlayerState
import com.practicum.myplaylistmaker.domain.player.AudioPlayerInteractor
import com.practicum.myplaylistmaker.util.Creator

class PlayerViewModel(
    private val playerInteractor: AudioPlayerInteractor,
):ViewModel(){

    var stateLiveData = MutableLiveData<PlayerState>()

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

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayerViewModel(
                        Creator.providePlayerInteractor()
                    ) as T
                }
            }
    }
}