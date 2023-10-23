package com.practicum.myplaylistmaker.data.search.requestAndResponse


data class TrackResponce (
    val resultCount: Int,
    val results: ArrayList<TrackDto>) : Response()

