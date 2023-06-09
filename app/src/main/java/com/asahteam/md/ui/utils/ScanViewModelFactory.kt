package com.asahteam.md.ui.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asahteam.md.injection.Injection
import com.asahteam.md.repository.ScanRepository
import com.asahteam.md.ui.history.HistoryViewModel
import com.asahteam.md.ui.map.MapsViewModel
import com.asahteam.md.ui.scan.ScanViewModel

class ScanViewModelFactory private constructor(private val repository: ScanRepository): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScanViewModel::class.java)) {
            return ScanViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: ScanViewModelFactory? = null
        fun getInstance(context: Context): ScanViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ScanViewModelFactory(
                    Injection.getScanRepository(context)
                )
            }.also { INSTANCE = it }
    }
}