package com.asahteam.md.ui.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asahteam.md.repository.PointRepository
import com.asahteam.md.ui.profile.ProfileViewModel

class ProfileViewModelFactory private constructor(private val repository: PointRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: ProfileViewModelFactory? = null
        fun getInstance(context: Context): ProfileViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProfileViewModelFactory(
                    com.asahteam.md.injection.Injection.getPointRepository(context)
                )
            }.also { INSTANCE = it }
    }
}