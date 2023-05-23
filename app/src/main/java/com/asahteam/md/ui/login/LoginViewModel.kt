package com.asahteam.md.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asahteam.md.local.data.User
import com.asahteam.md.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    fun login(username: String, password: String) = repository.login(username, password)

    fun saveUser(user: User) {
        viewModelScope.launch {
            repository.saveUser(user)
        }
    }
}