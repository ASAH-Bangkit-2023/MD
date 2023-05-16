package com.example.md.ui.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.md.injection.Injection
import com.example.md.repository.AlarmRepository
import com.example.md.ui.reminder.ReminderViewModel

class AlarmViewModelFactory private constructor(private val repository: AlarmRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReminderViewModel::class.java)) {
            return ReminderViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: AlarmViewModelFactory? = null
        fun getInstance(context: Context): AlarmViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AlarmViewModelFactory(
                    Injection.getAlarmRepository(context)
                )
            }.also { INSTANCE = it }
    }
}