package com.asahteam.md.ui.result

import androidx.lifecycle.ViewModel
import com.asahteam.md.repository.PointRepository

class ResultViewModel(
    private val repository: PointRepository
) : ViewModel() {
    fun addPoint() = repository.addPoint()
}