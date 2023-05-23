package com.asahteam.md.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @field:SerializedName("full_name")
    val fullName: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("email")
    val email: String
)
