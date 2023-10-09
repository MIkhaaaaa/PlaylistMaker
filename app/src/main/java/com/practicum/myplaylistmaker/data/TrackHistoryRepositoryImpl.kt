package com.practicum.myplaylistmaker.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.HISTORY_KEY
import com.practicum.myplaylistmaker.domain.SharedPreferencesRepository


class TrackHistoryRepositoryImpl(private val savedHistory: SharedPreferences): SharedPreferencesRepository {

    var trackHistoryList = ArrayList<Track>()
    private val gson = Gson()
    var counter = 0

    override fun editArray(newHistoryTrack: Track) {
        val json = ""
        if (json.isNotEmpty()) {
            if (trackHistoryList.isEmpty()) {
                if (savedHistory.contains(HISTORY_KEY)) {
                    val type = object : TypeToken<ArrayList<Track>>() {}.type
                    trackHistoryList = gson.fromJson(json, type)
                }
            }
        }
        if (trackHistoryList.contains(newHistoryTrack)) {
            trackHistoryList.remove(newHistoryTrack)
            trackHistoryList.add(0, newHistoryTrack)
        } else {
            if (trackHistoryList.size < 5) trackHistoryList.add(0, newHistoryTrack)
            else {
                trackHistoryList.removeAt(4)
                trackHistoryList.add(0, newHistoryTrack)
            }
        }
        saveHistory()
    }

    override fun read(sharedPreferences: SharedPreferences): ArrayList<Track> {
        val json = sharedPreferences.getString(HISTORY_KEY, null) ?: return ArrayList()
        return Gson().fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
    }
    override fun clearAllHistory(){
        savedHistory.edit().clear().apply()
    }


    override  fun saveHistory() {
        var json = ""
        json = gson.toJson(trackHistoryList)
        savedHistory.edit()
            .clear()
            .putString(HISTORY_KEY, json)
            .apply()
        counter = trackHistoryList.size
    }

    override fun getSharedPreferences():SharedPreferences { return savedHistory}
}