package com.example.md.ui.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.md.repository.AlarmRepository
import kotlinx.coroutines.launch

class ReminderViewModel(private val repository: AlarmRepository) : ViewModel() {
    fun getAlarm() = repository.getAlarm().asLiveData()

    fun saveAlarm(hour: String, repeat: Long) {
        viewModelScope.launch {
            repository.saveAlarm(hour, repeat)
        }
    }

    fun cancelAlarm() {
        viewModelScope.launch {
            repository.cancelAlarm()
        }
    }
}