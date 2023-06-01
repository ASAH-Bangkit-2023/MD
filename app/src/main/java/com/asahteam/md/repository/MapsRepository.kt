package com.asahteam.md.repository

import androidx.lifecycle.liveData
import com.asahteam.md.remote.response.ResultResponse
import com.asahteam.md.remote.retrofit.ApiService

class MapsRepository private constructor(private val service: ApiService) {
    fun getMaps(lat: Double, lng: Double) = liveData {
        try {
            emit(ResultResponse.Loading)
            val response = service.getMaps(
                "tempat pembuangan akhir tpa tps sampah",
                20000,
                "$lat, $lng",
                "AIzaSyAiiczEiM2ixmsdUT7mCww4RqQhI92cDus"
            )
            emit(ResultResponse.Success(response))
            if (response.results.isEmpty()) {
                emit(ResultResponse.NotFound)
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: MapsRepository? = null

        fun getInstance(service: ApiService): MapsRepository =
            instance ?: synchronized(this) {
                MapsRepository(service).apply {
                    instance = this
                }
            }
    }
}