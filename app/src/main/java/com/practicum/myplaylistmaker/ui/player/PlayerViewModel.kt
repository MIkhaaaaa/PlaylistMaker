package com.practicum.myplaylistmaker.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myplaylistmaker.data.db.impl.FavoritesInteractor
import com.practicum.myplaylistmaker.domain.models.PlayerState
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.domain.player.AudioPlayerInteractor
import com.practicum.myplaylistmaker.ui.player.ActivityMediaPlayer.Companion.DELAY_PAUSE
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration

class PlayerViewModel(
    private val playerInteractor: AudioPlayerInteractor,
    private val favouritesInteractor: FavoritesInteractor
):ViewModel(){

    var stateLiveData = MutableLiveData<PlayerState>()
    private var isClickAllowed = true
    var playJob : Job? = null
    var timer = MutableLiveData("00:00")
    val favouritesIndicator = MutableLiveData<Boolean>()
    var favouritesJob:Job?=null
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
                delay(PLAYER_BUTTON_PRESSING_DELAY)
                playerInteractor.timeTransfer().collect() {
                    timer.postValue(it)
                }

        }
        return timer
    }

    fun jobCancel(){
        playJob?.cancel()
    }

    fun onFavoriteClicked(track: Track) {
        if (track.isFavorite) {
            track.trackId?.let { favouritesInteractor.favouritesDelete(track) }
        } else track.trackId?.let {
            favouritesInteractor.favouritesAdd(
                track
            )
        }
    }

    fun favouritesChecker (track: Track) : LiveData<Boolean> {

        favouritesJob=viewModelScope.launch{

            while (true) {
                delay(PLAYER_BUTTON_PRESSING_DELAY)
                track.trackId?.let { id ->
                    favouritesInteractor.favouritesCheck(id)
                        .collect {value ->
                            favouritesIndicator.postValue(value)
                        }
                }
            }
        }
        return favouritesIndicator
    }

    fun formatMilliseconds(milliseconds: Long): String {
        val duration = Duration.ofMillis(milliseconds)
        val minutes = duration.toMinutes()
        val seconds = duration.minusMinutes(minutes).seconds
        return "$minutes:$seconds"
    }

    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY = 300L
    }
}