package com.practicum.myplaylistmaker.data.dto


data class TrackResponce (
    val resultCount: Int,
    val results: ArrayList<TrackDto>) : Response()

