package com.practicum.myplaylistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TrackHistory {
    private val savedHistory = App.getSharedPreferences()
    private val gson = Gson()

    var counter = 0
    var trackHistoryList = App.trackHistoryList

    fun editArray(newHistoryTrack: Track) {
        var json = ""
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

    fun read(sharedPreferences: SharedPreferences): ArrayList<Track> {
        val json = sharedPreferences.getString(HISTORY_KEY, null) ?: return ArrayList()
        return Gson().fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
    }
    fun clearAllHistory(){
        savedHistory.edit().clear().apply()
    }


    private fun saveHistory() {
        var json = ""
        json = gson.toJson(trackHistoryList)
        savedHistory.edit()
            .clear()
            .putString(HISTORY_KEY, json)
            .apply()
        counter = trackHistoryList.size
    }
}