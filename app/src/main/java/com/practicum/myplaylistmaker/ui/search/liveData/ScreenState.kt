package com.practicum.myplaylistmaker.ui.search.liveData

import com.practicum.myplaylistmaker.domain.models.Track

sealed class ScreenState {
    object DefaultSearch : ScreenState()
    object Loading : ScreenState()
    object NothingFound : ScreenState()
    object ConnectionError : ScreenState()
    data class SearchHistory(var historyData: List<Track>) : ScreenState()
    data class SearchOk(val data: List<Track>) : ScreenState()
}