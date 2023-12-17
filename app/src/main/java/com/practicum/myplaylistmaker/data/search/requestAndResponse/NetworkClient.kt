package com.practicum.myplaylistmaker.data.search.requestAndResponse

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}