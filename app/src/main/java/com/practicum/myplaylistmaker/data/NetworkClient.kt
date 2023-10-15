package com.practicum.myplaylistmaker.data

import com.practicum.myplaylistmaker.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}