package com.asahteam.md.repository

import androidx.lifecycle.liveData
import com.asahteam.md.local.preference.AppPreference
import com.asahteam.md.remote.response.ResultResponse
import com.asahteam.md.remote.retrofit.ApiService

class BlogRepository private constructor(
    private val service: ApiService,
    private val preference: AppPreference
) {
    fun getBlogs() = liveData {
        emit(ResultResponse.Loading)
        try {
            preference.getUser().collect {
                val token = "Bearer ${it.accessToken}"
                val response = service.getBlogs(token)
                emit(ResultResponse.Success(response))
                if (response.isEmpty()) {
                    emit(ResultResponse.NotFound)
                }
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.message.toString()))
        }
    }

    fun getBlog(id: Int) = liveData {
        emit(ResultResponse.Loading)
        try {
            preference.getUser().collect {
                val token = "Bearer ${it.accessToken}"
                val response = service.getBlog(token, id)
                emit(ResultResponse.Success(response))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: BlogRepository? = null

        fun getInstance(service: ApiService, preference: AppPreference): BlogRepository =
            instance ?: synchronized(this) {
                BlogRepository(service, preference).apply {
                    instance = this
                }
            }
    }
}