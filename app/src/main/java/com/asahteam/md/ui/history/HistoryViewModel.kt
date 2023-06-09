package com.asahteam.md.ui.history

import androidx.lifecycle.ViewModel
import com.asahteam.md.repository.ScanRepository
import okhttp3.MultipartBody

class HistoryViewModel(private val repository: ScanRepository) : ViewModel() {
    fun getHistory() =
        repository.getHistory()
}