package com.practicum.myplaylistmaker.data.search.history

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.myplaylistmaker.app.HISTORY_KEY
import com.practicum.myplaylistmaker.domain.models.Track
import com.practicum.myplaylistmaker.domain.search.SharedPreferencesRepository


class TrackHistoryRepositoryImpl(
    private val savedHistory: SharedPreferences, private val gson: Gson
) :
    SharedPreferencesRepository {

    private var trackHistoryList = ArrayList<Track>()
    var counter = 0


    override fun editArray(newHistoryTrack: Track): ArrayList<Track> {
        trackHistoryList.clear()
        trackHistoryList.addAll(read(savedHistory))

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
            if (trackHistoryList.size < 3) trackHistoryList.add(0, newHistoryTrack)
            else {
                trackHistoryList.removeAt(2)
                trackHistoryList.add(0, newHistoryTrack)
            }
        }
        saveHistory()
        return trackHistoryList
    }

    override fun read(sharedPreferences: SharedPreferences): ArrayList<Track> {
        val json = sharedPreferences.getString(HISTORY_KEY, null) ?: return ArrayList()
        return gson.fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
    }

    override fun clearAllHistory() {
        savedHistory.edit().clear().apply()
    }


    override fun saveHistory() {
        var json = ""
        json = gson.toJson(trackHistoryList)
        savedHistory.edit()
            .clear()
            .putString(HISTORY_KEY, json)
            .apply()
        counter = trackHistoryList.size
    }

    override fun getSharedPreferences(): SharedPreferences {
        return savedHistory
    }
}