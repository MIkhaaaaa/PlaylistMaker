package com.practicum.myplaylistmaker.data.search.requestAndResponse

import com.practicum.myplaylistmaker.data.search.requestAndResponse.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}