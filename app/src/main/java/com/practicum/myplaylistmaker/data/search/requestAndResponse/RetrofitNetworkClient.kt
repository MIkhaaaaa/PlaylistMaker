package com.practicum.myplaylistmaker.data.search.requestAndResponse

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val context: Context, private val iTunesService: ITunesAPI
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {

        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }

        if (dto is TrackRequest) {

            return withContext(Dispatchers.IO) {
                try {
                    val resp = iTunesService.search(dto.expression)
                    resp.apply { resultCode = 200 }

                } catch (e: Throwable) {
                    Response().apply { resultCode = 500 }
                }

            }

        } else {
            return Response().apply { resultCode = 400 }
        }


    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }


}

