package com.asahteam.md.ui.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asahteam.md.repository.AuthRepository
import com.asahteam.md.ui.login.LoginViewModel
import com.asahteam.md.ui.main.MainViewModel
import com.asahteam.md.ui.register.RegisterViewModel

class AuthViewModelFactory private constructor(private val repository: AuthRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthViewModelFactory? = null
        fun getInstance(context: Context): AuthViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthViewModelFactory(
                    com.asahteam.md.injection.Injection.getAuthRepository(context)
                )
            }.also { INSTANCE = it }
    }
}