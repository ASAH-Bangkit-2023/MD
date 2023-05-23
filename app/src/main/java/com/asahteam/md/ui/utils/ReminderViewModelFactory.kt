package com.asahteam.md.ui.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asahteam.md.repository.ReminderRepository
import com.asahteam.md.ui.reminder.ReminderViewModel

class ReminderViewModelFactory private constructor(private val repository: ReminderRepository) :
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
        private var INSTANCE: ReminderViewModelFactory? = null
        fun getInstance(context: Context): ReminderViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ReminderViewModelFactory(
                    com.asahteam.md.injection.Injection.getReminderRepository(context)
                )
            }.also { INSTANCE = it }
    }
}