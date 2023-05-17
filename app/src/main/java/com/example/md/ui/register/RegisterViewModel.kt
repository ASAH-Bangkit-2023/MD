package com.example.md.ui.register

import androidx.lifecycle.ViewModel
import com.example.md.repository.AuthRepository

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {
    fun register(username: String, password: String, confirmPassword: String) =
        repository.register(username, password, confirmPassword)
}