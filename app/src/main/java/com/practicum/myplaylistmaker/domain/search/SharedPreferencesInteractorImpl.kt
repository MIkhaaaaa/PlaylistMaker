package com.practicum.myplaylistmaker.domain.search

import android.content.SharedPreferences
import com.practicum.myplaylistmaker.domain.search.SharedPreferencesInteractor
import com.practicum.myplaylistmaker.domain.search.SharedPreferencesRepository
import com.practicum.myplaylistmaker.domain.models.Track

class SharedPreferencesInteractorImpl(private val preference: SharedPreferencesRepository) :
    SharedPreferencesInteractor {

    override fun getSharedPreferences(): SharedPreferences {
        return preference.getSharedPreferences()
    }

    override fun read(sharedPreferences: SharedPreferences): ArrayList<Track> {
        return preference.read(sharedPreferences)
    }

    override fun clearAllHistory() {
        preference.clearAllHistory()
    }

    override fun saveHistory() {
        preference.saveHistory()
    }

    override fun editArray(newHistoryTrack: Track ) : ArrayList<Track> {
       return preference.editArray(newHistoryTrack)
    }
}