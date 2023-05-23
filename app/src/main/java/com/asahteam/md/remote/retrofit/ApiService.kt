package com.asahteam.md.remote.retrofit

import com.asahteam.md.remote.request.LoginRequest
import com.asahteam.md.remote.request.RegisterRequest
import com.asahteam.md.remote.response.BlogResponse
import com.asahteam.md.remote.response.LoginResponse
import com.asahteam.md.remote.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("auth/signup")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    @GET("news")
    suspend fun getBlogs(
        @Header("Authorization") token: String
    ): List<BlogResponse>

    @GET("news/{id}")
    suspend fun getBlog(
        @Header("Authorization") token: Int,
        @Path("id") id: String
    ): BlogResponse
}