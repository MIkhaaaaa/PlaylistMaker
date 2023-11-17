package com.practicum.myplaylistmaker.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.myplaylistmaker.App.App
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.domain.player.TracksInteractor
import com.practicum.myplaylistmaker.domain.search.SharedPreferencesInteractor
import com.practicum.myplaylistmaker.ui.search.liveData.ScreenState
import com.practicum.myplaylistmaker.util.Creator

class SearchViewModel(
    private var searchInteractor: TracksInteractor,
    private var searchHistoryInteractor: SharedPreferencesInteractor,
) : ViewModel() {
    private var stateLiveData =
        MutableLiveData<ScreenState>(ScreenState.DefaultSearch)

    fun getStateLiveData(): LiveData<ScreenState> {
        return stateLiveData
    }

    private val tracksConsumer = object : TracksInteractor.TracksConsumer {
        override fun consume(foundTrack: ArrayList<Track>?, errorMessage: String?) {
            trackResultList.postValue(foundTrack!!)
            stateLiveData.postValue(
                if (foundTrack.isNullOrEmpty())
                    ScreenState.NothingFound
                else ScreenState.SearchOk(foundTrack)
            )
        }
    }

    private var trackResultList: MutableLiveData<List<Track>> = MutableLiveData<List<Track>>()
    fun searchRequesting(searchExpression: String) {
        stateLiveData.postValue(ScreenState.Loading)
        try {
            searchInteractor.searchTracks(searchExpression, tracksConsumer)
        } catch (error: Error) {
            stateLiveData.postValue(ScreenState.ConnectionError)
        }
    }


    private var trackHistoryList: MutableLiveData<List<Track>> =
        MutableLiveData<List<Track>>().apply {
            value = emptyList()
        }

    fun addItem(item: Track) {
        searchHistoryInteractor.editArray(item)
    }

    fun clearHistory() {
        searchHistoryInteractor.clearAllHistory()
    }

    fun provideHistory(): LiveData<List<Track>> {
        val history = getHistory()
        trackHistoryList.value=history
        if (history.isNullOrEmpty()) {
            trackHistoryList.postValue(emptyList())
        }
        return trackHistoryList
    }

    fun clearTrackList() {
        trackResultList.value = emptyList()
        stateLiveData.value= trackHistoryList.value?.let { ScreenState.SearchHistory(it) }
    }


//    companion object {
//        fun getViewModelFactory(): ViewModelProvider.Factory =
//            object : ViewModelProvider.Factory {
//                @Suppress("UNCHECKED_CAST")
//                override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                    return SearchViewModel(
//                        Creator.provideTracksIteractor(App.instance),
//                        Creator.provideSharedPreferenceInteractor(),
//                    ) as T
//                }
//            }
//    }

    private fun getHistory() : ArrayList<Track> {
        val trackHistoryList = ArrayList<Track>()
        trackHistoryList.addAll(searchHistoryInteractor.read(searchHistoryInteractor.getSharedPreferences()))
        return trackHistoryList
    }

}