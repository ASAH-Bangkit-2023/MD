package com.example.md.injection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.md.local.preference.AppPreference
import com.example.md.remote.retrofit.ApiConfig
import com.example.md.repository.AlarmRepository
import com.example.md.repository.AuthRepository
import com.example.md.repository.BlogRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("app")

object Injection {
    private val apiService = ApiConfig.getApiService()
    fun getAlarmRepository(context: Context): AlarmRepository {
        val dataStore = AppPreference.getInstace(context.dataStore)
        return AlarmRepository(dataStore)
    }

    fun getAuthRepository(context: Context): AuthRepository {
        val dataStore = AppPreference.getInstace(context.dataStore)
        return AuthRepository(dataStore, apiService)
    }

    fun getBlogRepository(): BlogRepository {
        return BlogRepository(apiService)
    }
}