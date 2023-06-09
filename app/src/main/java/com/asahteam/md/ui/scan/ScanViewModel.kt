package com.asahteam.md.ui.scan

import androidx.lifecycle.ViewModel
import com.asahteam.md.repository.ScanRepository
import okhttp3.MultipartBody

class ScanViewModel(private val repository: ScanRepository) : ViewModel() {
    fun scanWaste(image: MultipartBody.Part) =
        repository.scanWaste(image)
}