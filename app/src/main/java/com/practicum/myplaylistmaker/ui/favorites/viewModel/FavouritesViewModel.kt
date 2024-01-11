package com.practicum.myplaylistmaker.ui.favorites.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.myplaylistmaker.data.db.impl.FavoritesInteractor
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.domain.search.SharedPreferencesInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavouritesViewModel(
    private val favouritesInteractor: FavoritesInteractor,
    private val searchHistoryInteractor: SharedPreferencesInteractor
):ViewModel() {
    var trackResultList: MutableLiveData<List<Track>?> = MutableLiveData<List<Track>?>()

    fun favouritesMaker() : LiveData<List<Track>?> {
        viewModelScope.launch {
            while (true) {
                delay (300)
                favouritesInteractor.favouritesGet()
                    .collect { trackList ->
                        if (!trackList.isNullOrEmpty()) {
                            trackResultList.postValue(trackList)
                        } else trackResultList.postValue(emptyList())
                    }
            }
        }
        return trackResultList
    }

    fun addItem(item: Track) {
        searchHistoryInteractor.editArray(item)
    }
}