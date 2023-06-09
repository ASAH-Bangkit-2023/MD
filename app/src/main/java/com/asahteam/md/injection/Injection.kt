package com.asahteam.md.injection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.asahteam.md.local.preference.AppPreference
import com.asahteam.md.remote.retrofit.ApiConfig
import com.asahteam.md.remote.retrofit.ApiConfig.base_url
import com.asahteam.md.remote.retrofit.ApiConfig.map_url
import com.asahteam.md.repository.AuthRepository
import com.asahteam.md.repository.BlogRepository
import com.asahteam.md.repository.MapsRepository
import com.asahteam.md.repository.PointRepository
import com.asahteam.md.repository.ReminderRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("app")

object Injection {
    private val apiService = ApiConfig.getApiService(base_url)
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

    fun getPointRepository(context: Context): PointRepository {
        val dataStore = AppPreference.getInstace(context.dataStore)
        return PointRepository.getInstance(apiService, dataStore)
    }

    fun getMapsRepository(): MapsRepository {
        val apiService = ApiConfig.getApiService(map_url)
        return MapsRepository.getInstance(apiService)
    }
}