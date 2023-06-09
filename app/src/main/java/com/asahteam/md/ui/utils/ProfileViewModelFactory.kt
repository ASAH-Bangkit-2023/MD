package com.asahteam.md.ui.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asahteam.md.injection.Injection
import com.asahteam.md.repository.AuthRepository
import com.asahteam.md.repository.PointRepository
import com.asahteam.md.ui.profile.ProfileViewModel

class ProfileViewModelFactory private constructor(
    private val repository: PointRepository,
    private val authRepository: AuthRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(repository, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: ProfileViewModelFactory? = null
        fun getInstance(context: Context): ProfileViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProfileViewModelFactory(
                    Injection.getPointRepository(context),
                    Injection.getAuthRepository(context)
                )
            }.also { INSTANCE = it }
    }
}