package com.asahteam.md.remote.request

data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String,
    val full_name: String
)
