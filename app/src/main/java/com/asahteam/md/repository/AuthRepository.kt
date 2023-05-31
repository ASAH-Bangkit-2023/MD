package com.asahteam.md.repository

import androidx.lifecycle.liveData
import com.asahteam.md.local.data.User
import com.asahteam.md.local.preference.AppPreference
import com.asahteam.md.remote.request.RegisterRequest
import com.asahteam.md.remote.response.ResultResponse
import com.asahteam.md.remote.retrofit.ApiService

class AuthRepository private constructor(
    private val preference: AppPreference,
    private val service: ApiService
) {

    fun login(username: String, password: String) = liveData {
        emit(ResultResponse.Loading)
        try {
            val response = service.login(username, password)
            emit(ResultResponse.Success(response))
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.message.toString()))
        }
    }

    fun register(username: String, password: String, fullName: String, email: String) = liveData {
        emit(ResultResponse.Loading)
        try {
            val response = service.register(RegisterRequest(username, password, email, fullName))
            emit(ResultResponse.Success(response))
        } catch (e: Exception) {
            emit(ResultResponse.Error(e.toString()))
        }
    }

    suspend fun saveUser(user: User) {
        preference.saveUser(user.accessToken, user.accessToken, user.date)
    }

    suspend fun logout() {
        preference.logOut()
    }

    fun getUser() = preference.getUser()

    companion object {
        @Volatile
        private var instance: AuthRepository? = null

        fun getInstance(preference: AppPreference, service: ApiService): AuthRepository =
            instance ?: synchronized(this) {
                AuthRepository(preference, service).apply {
                    instance = this
                }
            }
    }
}