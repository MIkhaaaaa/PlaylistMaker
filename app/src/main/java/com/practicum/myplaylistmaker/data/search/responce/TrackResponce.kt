package com.practicum.myplaylistmaker.data.search.responce


data class TrackResponce (
    val resultCount: Int,
    val results: ArrayList<TrackDto>) : Response()

