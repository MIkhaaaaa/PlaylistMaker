package com.practicum.myplaylistmaker.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myplaylistmaker.domain.favorites.db.FavoritesInteractor
import com.practicum.myplaylistmaker.domain.models.PlayerState
import com.practicum.myplaylistmaker.domain.models.Playlist
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.domain.player.AudioPlayerInteractor
import com.practicum.myplaylistmaker.domain.playlist.PlaylistInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration

class PlayerViewModel(
    private val playerInteractor: AudioPlayerInteractor,
    private val favouritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor
):ViewModel(){

    var stateLiveData = MutableLiveData<PlayerState>()
    private var isClickAllowed = true
    var playJob : Job? = null
    var timer = MutableLiveData("00:00")
    private val state = MutableLiveData<Boolean>()
    val _state = state
    val playlistList: MutableLiveData<List<Playlist>> = MutableLiveData<List<Playlist>>(emptyList())
    
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
                delay(PLAYER_BUTTON_PRESSING_DELAY_MILLIS)
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
                delay(PLAYER_BUTTON_PRESSING_DELAY_MILLIS)
                track.trackId?.let { id ->
                    favouritesInteractor.favouritesCheck(id)
                        .collect {value ->
                            _state.postValue(value)
                        }
                }
            }
        }
        return _state
    }

    fun formatMilliseconds(milliseconds: Long): String {
        val duration = Duration.ofMillis(milliseconds)
        val minutes = duration.toMinutes()
        val seconds = duration.minusMinutes(minutes).seconds
        return "$minutes:$seconds"
    }

    fun playlistMaker(): LiveData<List<Playlist>> {
        viewModelScope.launch {
            playlistInteractor.queryPlaylist()
                .collect {
                    if (it.isNotEmpty()) {
                        playlistList.postValue(it)
                    } else {
                        playlistList.postValue(emptyList())
                    }
                }
        }
        return playlistList
    }

    val playlistAdding =MutableLiveData(false)

    fun addTrack(track: Track, playlist: Playlist) {
        if (playlist.trackArray.contains(track.trackId)) {
            playlistAdding.postValue(true)
        } else {
            playlistAdding.postValue(false)
            playlist.trackArray = (playlist.trackArray + track.trackId)!!
            playlist.arrayNumber = (playlist.arrayNumber?.plus(1))!!
            playlistInteractor.update(track, playlist)

        }
    }

    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY_MILLIS = 300L
        const val DELAY_PAUSE = 500L
    }
}