package com.practicum.myplaylistmaker.data.network

import com.practicum.myplaylistmaker.data.NetworkClient
import com.practicum.myplaylistmaker.data.dto.Response
import com.practicum.myplaylistmaker.data.dto.TrackRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {
    private val iTunesBaseURL = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesAPI::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is TrackRequest){
            val resp = iTunesService.search(dto.expression).execute()
            val body = resp.body() ?: Response()
            return body.apply { resultCode = resp.code() }
        } else {
            return  Response().apply { resultCode = 400 }
        }
    }


}

