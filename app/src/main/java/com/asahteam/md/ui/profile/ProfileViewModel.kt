package com.asahteam.md.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asahteam.md.repository.AuthRepository
import com.asahteam.md.repository.PointRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: PointRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    fun getPoint() = repository.getPoint()
    
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}