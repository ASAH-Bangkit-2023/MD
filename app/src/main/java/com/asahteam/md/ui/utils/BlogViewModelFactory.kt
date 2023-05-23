package com.asahteam.md.ui.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asahteam.md.repository.BlogRepository
import com.asahteam.md.ui.blog.BlogViewModel

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
                    com.asahteam.md.injection.Injection.getBlogRepository(context)
                )
            }.also { INSTANCE = it }
    }
}