package com.example.md.injection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.md.local.preference.AppPreference
import com.example.md.remote.retrofit.ApiConfig
import com.example.md.repository.AuthRepository
import com.example.md.repository.BlogRepository
import com.example.md.repository.ReminderRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("app")

object Injection {
    private val apiService = ApiConfig.getApiService()
    fun getReminderRepository(context: Context): ReminderRepository {
        val dataStore = AppPreference.getInstace(context.dataStore)
        return ReminderRepository.getInstance(dataStore)
    }

    fun getAuthRepository(context: Context): AuthRepository {
        val dataStore = AppPreference.getInstace(context.dataStore)
        return AuthRepository.getInstance(dataStore, apiService)
    }

    fun getBlogRepository(): BlogRepository {
        return BlogRepository.getInstance(apiService)
    }
}