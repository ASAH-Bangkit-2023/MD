package com.asahteam.md.injection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.asahteam.md.local.preference.AppPreference
import com.asahteam.md.remote.retrofit.ApiConfig
import com.asahteam.md.repository.AuthRepository
import com.asahteam.md.repository.BlogRepository
import com.asahteam.md.repository.ReminderRepository

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

    fun getBlogRepository(context: Context): BlogRepository {
        val dataStore = AppPreference.getInstace(context.dataStore)
        return BlogRepository.getInstance(apiService, dataStore)
    }
}