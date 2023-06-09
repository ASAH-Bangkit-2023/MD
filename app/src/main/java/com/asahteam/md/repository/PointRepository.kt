package com.asahteam.md.repository

import androidx.lifecycle.liveData
import com.asahteam.md.local.preference.AppPreference
import com.asahteam.md.remote.response.ResultResponse
import com.asahteam.md.remote.retrofit.ApiService

class PointRepository
private constructor(
    private val service: ApiService,
    private val preference: AppPreference
) {
    fun getPoint() = liveData {
        emit(ResultResponse.Loading)
        try {
            preference.getUser().collect {
                val response = service.getPoint("Bearer ${it.accessToken}")
                emit(ResultResponse.Success(response))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: PointRepository? = null

        fun getInstance(service: ApiService, preference: AppPreference): PointRepository =
            instance ?: synchronized(this) {
                PointRepository(service, preference).apply {
                    instance = this
                }
            }
    }
}