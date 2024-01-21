package com.practicum.myplaylistmaker.data.search.request

import com.practicum.myplaylistmaker.data.search.responce.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}