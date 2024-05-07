package com.practicum.myplaylistmaker.ui.library.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myplaylistmaker.domain.models.Playlist
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.domain.playlist.PlaylistInteractor
import com.practicum.myplaylistmaker.domain.playlistScreen.PlaylistScreenInteractor
import com.practicum.myplaylistmaker.domain.settings.SettingsInteractor
import kotlinx.coroutines.launch

class PlaylistScreenViewModel(
    private val playlistScreenInteractor: PlaylistScreenInteractor,
    private val settingsInteractor: SettingsInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    fun isAppThemeDark() :Boolean{
        return settingsInteractor.isDayOrNight()
    }

    val trackList : MutableLiveData<List<Track>> = MutableLiveData(emptyList())
    fun getTrackList (playlist: Playlist) {
        viewModelScope.launch {
            playlistScreenInteractor.getTrackList(playlist).collect {
                    list -> trackList.postValue(list)
            }
        }
    }

    fun deletePlaylist (playlist: Playlist) {
        playlistInteractor.deletePlaylist(playlist)
    }

    fun deleteTrack (track: Track, playlist: Playlist){
        playlist.trackArray = playlist.trackArray.filter { it != track.trackId }
        playlist.arrayNumber = playlist.arrayNumber?.minus(1)
        playlistInteractor.update(track, playlist)
    }

    val playlistTime: MutableLiveData<String> = MutableLiveData("")
    fun getPlaylistTime (playlist: Playlist) {
        viewModelScope.launch {
            playlistScreenInteractor.timeCounting(playlist).collect{
                    readyTime -> playlistTime.postValue(readyTime)
            }
        }
    }

    val updatedPlaylist : MutableLiveData<Playlist> = MutableLiveData()
    fun getPlaylist (searchId: Int) {
        viewModelScope.launch {
            playlistInteractor.findPlaylist(searchId).collect{
                updatedPlaylist.postValue(it)
            }
        }
    }
}