package com.example.md.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.md.local.data.User
import com.example.md.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    fun login(username: String, password: String) = repository.login(username, password)

    fun saveUser(user: User) {
        viewModelScope.launch {
            repository.saveUser(user)
        }
    }
}