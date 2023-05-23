package com.asahteam.md.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("access_token")
    val accessToken: String,

    @field:SerializedName("refresh_token")
    val refreshToken: String
)
