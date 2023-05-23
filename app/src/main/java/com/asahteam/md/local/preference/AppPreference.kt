package com.asahteam.md.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.asahteam.md.local.data.Reminder
import com.asahteam.md.local.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreference private constructor(private val dataStore: DataStore<Preferences>) {
    private val HOUR = stringPreferencesKey("hour")
    private val REPEAT = longPreferencesKey("repeat")
    private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")

    fun getUser(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[ACCESS_TOKEN] ?: "",
                preferences[REFRESH_TOKEN] ?: ""
            )
        }
    }

    suspend fun saveUser(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun logOut() {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = ""
            preferences[REFRESH_TOKEN] = ""
        }
    }

    fun getAlarm(): Flow<Reminder> {
        return dataStore.data.map { preferences ->
            Reminder(
                preferences[HOUR] ?: "",
                preferences[REPEAT] ?: 0
            )
        }
    }

    suspend fun saveAlarm(hour: String, repeat: Long) {
        dataStore.edit { preferences ->
            preferences[HOUR] = hour
            preferences[REPEAT] = repeat
        }
    }

    suspend fun cancelAlarm() {
        dataStore.edit { preferences ->
            preferences[HOUR] = ""
            preferences[REPEAT] = 0
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppPreference? = null

        fun getInstace(dataStore: DataStore<Preferences>): AppPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = AppPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}