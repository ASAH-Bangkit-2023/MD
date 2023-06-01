package com.asahteam.md.ui.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asahteam.md.repository.MapsRepository
import com.asahteam.md.ui.map.MapsViewModel

class MapsViewModelFactory private constructor(private val repository: MapsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: MapsViewModelFactory? = null
        fun getInstance(): MapsViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MapsViewModelFactory(
                    com.asahteam.md.injection.Injection.getMapsRepository()
                )
            }.also { INSTANCE = it }
    }
}