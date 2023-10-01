package com.practicum.myplaylistmaker.data.network

import com.practicum.myplaylistmaker.TrackResponseOld
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AppleAPI {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackResponseOld>
}
