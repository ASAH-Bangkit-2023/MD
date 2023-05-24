package com.asahteam.md.ui.blog

import androidx.lifecycle.ViewModel
import com.asahteam.md.repository.BlogRepository

class BlogViewModel(private val repository: BlogRepository) : ViewModel() {
    fun getBlogs() = repository.getBlogs()
}