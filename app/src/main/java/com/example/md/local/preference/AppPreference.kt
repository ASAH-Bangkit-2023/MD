package com.example.md.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.md.local.data.Reminder
import com.example.md.local.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreference private constructor(private val dataStore: DataStore<Preferences>) {
    private val HOUR = stringPreferencesKey("hour")
    private val REPEAT = longPreferencesKey("repeat")

    private val USERNAME = stringPreferencesKey("username")

    fun getUser(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[USERNAME] ?: ""
            )
        }
    }

    suspend fun saveUser(username: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME] = username
        }
    }

    suspend fun logOut() {
        dataStore.edit { preferences ->
            preferences[USERNAME] = ""
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