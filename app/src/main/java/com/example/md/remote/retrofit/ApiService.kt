package com.example.md.remote.retrofit

import com.example.md.remote.request.LoginRequest
import com.example.md.remote.request.RegisterRequest
import com.example.md.remote.response.LoginResponse
import com.example.md.remote.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse
}