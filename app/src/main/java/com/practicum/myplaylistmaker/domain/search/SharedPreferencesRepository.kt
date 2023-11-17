package com.practicum.myplaylistmaker.domain.search

import android.content.SharedPreferences
import com.practicum.myplaylistmaker.domain.models.Track

interface SharedPreferencesRepository {
    fun getSharedPreferences(): SharedPreferences
    fun read(sharedPreferences: SharedPreferences): ArrayList<Track>
    fun clearAllHistory()
    fun saveHistory()
    fun editArray(newHistoryTrack: Track) : ArrayList<Track>
}