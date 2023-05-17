package com.example.md.ui.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.md.injection.Injection
import com.example.md.repository.AuthRepository
import com.example.md.ui.login.LoginViewModel
import com.example.md.ui.register.RegisterViewModel

class AuthViewModelFactory private constructor(private val repository: AuthRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthViewModelFactory? = null
        fun getInstance(context: Context): AuthViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthViewModelFactory(
                    Injection.getAuthRepository(context)
                )
            }.also { INSTANCE = it }
    }
}