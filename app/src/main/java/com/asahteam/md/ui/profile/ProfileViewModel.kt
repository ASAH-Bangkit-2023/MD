package com.asahteam.md.ui.profile

import androidx.lifecycle.ViewModel
import com.asahteam.md.repository.PointRepository

class ProfileViewModel(private val repository: PointRepository) : ViewModel() {

    fun getPoint() = repository.getPoint()
}