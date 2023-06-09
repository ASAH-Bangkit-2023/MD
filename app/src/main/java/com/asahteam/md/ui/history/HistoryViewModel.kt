package com.asahteam.md.ui.history

import androidx.lifecycle.ViewModel
import com.asahteam.md.repository.ScanRepository

class HistoryViewModel(private val repository: ScanRepository) : ViewModel() {
    fun getHistory() =
        repository.getHistory()
}