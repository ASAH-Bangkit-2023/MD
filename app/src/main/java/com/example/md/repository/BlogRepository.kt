package com.example.md.repository

import androidx.lifecycle.liveData
import com.example.md.remote.response.ResultResponse
import com.example.md.remote.retrofit.ApiService

class BlogRepository(private val service: ApiService) {
    fun getBlog() = liveData {
        emit(ResultResponse.Loading)
        try {
            val response = service.getBlog()
            emit(ResultResponse.Success(response))
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.message.toString()))
        }
    }
}