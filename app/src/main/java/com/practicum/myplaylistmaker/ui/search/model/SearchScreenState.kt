package com.practicum.myplaylistmaker.ui.search.model

import com.practicum.myplaylistmaker.domain.models.Track

sealed class SearchScreenState {
    object DefaultSearch : SearchScreenState()
    object Loading : SearchScreenState()
    object NothingFound : SearchScreenState()
    object ConnectionError : SearchScreenState()
    data class SearchHistory(var historyData: List<Track>) : SearchScreenState()
    data class SearchOk(val data: List<Track>) : SearchScreenState()
}