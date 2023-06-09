package com.asahteam.md.remote.retrofit

import com.asahteam.md.remote.request.RegisterRequest
import com.asahteam.md.remote.response.*
import okhttp3.MultipartBody
import retrofit2.http.*

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

    @GET("json")
    suspend fun getMaps(
        @Query("keyword") keyword: String,
        @Query("radius") radius: Int,
        @Query("location") location: String,
        @Query("key") key: String
    ): MapsResponse

    @Multipart
    @POST("scan_waste/")
    suspend fun scanWaste(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ) : ScanResponse
}