package com.practicum.myplaylistmaker.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.domain.player.TracksInteractor
import com.practicum.myplaylistmaker.domain.search.SharedPreferencesInteractor
import com.practicum.myplaylistmaker.ui.search.model.SearchScreenState

class SearchViewModel(
    private var searchInteractor: TracksInteractor,
    private var searchHistoryInteractor: SharedPreferencesInteractor,
) : ViewModel() {
    private var stateLiveData =
        MutableLiveData<SearchScreenState>(SearchScreenState.DefaultSearch)

    fun getStateLiveData(): LiveData<SearchScreenState> {
        return stateLiveData
    }

    private val tracksConsumer = object : TracksInteractor.TracksConsumer {
        override fun consume(foundTrack: ArrayList<Track>?, errorMessage: String?) {
            trackResultList.postValue(foundTrack!!)
            stateLiveData.postValue(
                if (foundTrack.isNullOrEmpty())
                    SearchScreenState.NothingFound
                else SearchScreenState.SearchOk(foundTrack)
            )
        }
    }

    private var trackResultList: MutableLiveData<List<Track>> = MutableLiveData<List<Track>>()
    fun searchRequesting(searchExpression: String) {
        stateLiveData.postValue(SearchScreenState.Loading)
        try {
            searchInteractor.searchTracks(searchExpression, tracksConsumer)
        } catch (error: Error) {
            stateLiveData.postValue(SearchScreenState.ConnectionError)
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
        stateLiveData.value= trackHistoryList.value?.let { SearchScreenState.SearchHistory(it) }
    }

    private fun getHistory() : ArrayList<Track> {
        val trackHistoryList = ArrayList<Track>()
        trackHistoryList.addAll(searchHistoryInteractor.read(searchHistoryInteractor.getSharedPreferences()))
        return trackHistoryList
    }

}