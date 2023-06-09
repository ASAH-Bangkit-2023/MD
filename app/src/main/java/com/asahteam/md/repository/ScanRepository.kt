package com.asahteam.md.repository

import androidx.lifecycle.liveData
import com.asahteam.md.local.preference.AppPreference
import com.asahteam.md.remote.response.ResultResponse
import com.asahteam.md.remote.retrofit.ApiService
import okhttp3.MultipartBody

class ScanRepository private constructor(
    private val service: ApiService,
    private val preference: AppPreference
) {
    fun scanWaste(image: MultipartBody.Part) = liveData {
        emit(ResultResponse.Loading)
        try {
            preference.getUser().collect {
                val token = "Bearer ${it.accessToken}"
                val response = service.scanWaste(token, image)
                emit(ResultResponse.Success(response))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.message.toString()))
        }
    }

    fun getHistory() = liveData {
        emit(ResultResponse.Loading)
        try {
            preference.getUser().collect {
                val token = "Bearer ${it.accessToken}"
                val response = service.getHistory(token)
                emit(ResultResponse.Success(response))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: ScanRepository? = null

        fun getInstance(service: ApiService, preference: AppPreference): ScanRepository =
            instance ?: synchronized(this) {
                ScanRepository(service, preference).apply {
                    instance = this
                }
            }
    }
}