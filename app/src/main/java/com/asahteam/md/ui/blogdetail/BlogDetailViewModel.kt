package com.asahteam.md.ui.blogdetail

import androidx.lifecycle.ViewModel
import com.asahteam.md.repository.BlogRepository

class BlogDetailViewModel(private val repository: BlogRepository) : ViewModel() {
    fun getBlog(id: Int) = repository.getBlog(id)
}