package com.practicum.myplaylistmaker.data.search.request

import com.practicum.myplaylistmaker.data.search.responce.TrackResponce
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI {
    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): TrackResponce
}