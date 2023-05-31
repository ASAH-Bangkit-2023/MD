package com.asahteam.md.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.asahteam.md.repository.AuthRepository
import kotlinx.coroutines.launch

class MainViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun getUser() = authRepository.getUser().asLiveData()

    fun logOut() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}