package com.asahteam.md.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.asahteam.md.repository.AuthRepository

class MainViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun getUser() = authRepository.getUser().asLiveData()
}