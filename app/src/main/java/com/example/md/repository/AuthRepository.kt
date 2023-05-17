package com.example.md.repository

import androidx.lifecycle.liveData
import com.example.md.local.data.User
import com.example.md.local.preference.AppPreference
import com.example.md.remote.request.LoginRequest
import com.example.md.remote.request.RegisterRequest
import com.example.md.remote.response.ResultResponse
import com.example.md.remote.retrofit.ApiService

class AuthRepository(private val preference: AppPreference, private val service: ApiService) {

    fun login(username: String, password: String) = liveData {
        emit(ResultResponse.Loading)
        try {
            val response = service.login(LoginRequest(username, password))
            if (response.error.isNotEmpty()) {
                emit(ResultResponse.Error(response.error))
            } else {
                emit(ResultResponse.Success(response))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.message.toString()))
        }
    }

    fun register(username: String, password: String, confirmPassword: String) = liveData {
        emit(ResultResponse.Loading)
        try {
            val response = service.register(RegisterRequest(username, password, confirmPassword))
            if (response.error.isNotEmpty()) {
                emit(ResultResponse.Error(response.error))
            } else {
                emit(ResultResponse.Success(response))
            }
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.message.toString()))
        }
    }

    suspend fun saveUser(user: User) {
        preference.saveUser(user.username)
    }

    suspend fun logout() {
        preference.logOut()
    }
}