package com.practicum.myplaylistmaker.data.search.requestAndResponse

import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI {
    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): TrackResponce
}