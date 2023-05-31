package com.asahteam.md.remote.retrofit

import com.asahteam.md.remote.request.RegisterRequest
import com.asahteam.md.remote.response.BlogResponse
import com.asahteam.md.remote.response.LoginResponse
import com.asahteam.md.remote.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("auth/login")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("auth/signup")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    @GET("news/")
    suspend fun getBlogs(
        @Header("Authorization") token: String
    ): List<BlogResponse>

    @GET("news/{id}")
    suspend fun getBlog(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): BlogResponse
}