package com.example.md.ui.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.md.injection.Injection
import com.example.md.repository.BlogRepository
import com.example.md.ui.blog.BlogViewModel

class BlogViewModelFactory private constructor(private val repository: BlogRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlogViewModel::class.java)) {
            return BlogViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: BlogViewModelFactory? = null
        fun getInstance(context: Context): BlogViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: BlogViewModelFactory(
                    Injection.getBlogRepository()
                )
            }.also { INSTANCE = it }
    }
}