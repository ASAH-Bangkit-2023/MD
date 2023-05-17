package com.example.md.ui.blog

import androidx.lifecycle.ViewModel
import com.example.md.repository.BlogRepository

class BlogViewModel(private val repository: BlogRepository) : ViewModel() {
    fun getBlog() = repository.getBlog()
}