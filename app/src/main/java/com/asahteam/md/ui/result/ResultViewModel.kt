package com.asahteam.md.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.asahteam.md.repository.PointRepository
import kotlinx.coroutines.launch

class ResultViewModel(
    private val repository: PointRepository
) : ViewModel() {
    fun addPoint(point: Int) = repository.addPoint(point)

    fun getWaste() = repository.getWaste().asLiveData()

    fun resetWaste() {
        viewModelScope.launch {
            repository.resetWaste()
        }
    }

    fun saveWaste(date: Int) {
        viewModelScope.launch {
            repository.saveWaste(date)
        }
    }
}