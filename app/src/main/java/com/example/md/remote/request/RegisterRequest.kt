package com.example.md.remote.request

data class RegisterRequest(
    val username: String,
    val password: String,
    val confirmPassword: String
)
