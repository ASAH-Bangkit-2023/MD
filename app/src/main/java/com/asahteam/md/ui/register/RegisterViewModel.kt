package com.asahteam.md.ui.register

import androidx.lifecycle.ViewModel
import com.asahteam.md.repository.AuthRepository

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {
    fun register(username: String, password: String, email: String, fullName: String) =
        repository.register(username, password, fullName, email)
}