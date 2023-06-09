package com.asahteam.md.local.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.asahteam.md.local.data.Reminder
import com.asahteam.md.local.data.User
import com.asahteam.md.local.data.Waste
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreference private constructor(private val dataStore: DataStore<Preferences>) {
    private val hour = stringPreferencesKey("hour")
    private val repeat = longPreferencesKey("repeat")
    private val accessToken = stringPreferencesKey("access_token")
    private val refreshToken = stringPreferencesKey("refresh_token")
    private val dateLogin = intPreferencesKey("date")
    private val wastePoint = intPreferencesKey("waste_point")
    private val dateWaste = intPreferencesKey("date_waste")

    fun getUser(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[accessToken] ?: "",
                preferences[refreshToken] ?: "",
                preferences[dateLogin] ?: 0
            )
        }
    }

    suspend fun saveUser(accessToken: String, refreshToken: String, date: Int) {
        dataStore.edit { preferences ->
            preferences[this.accessToken] = accessToken
            preferences[this.refreshToken] = refreshToken
            preferences[this.dateLogin] = date
        }
    }

    suspend fun logOut() {
        dataStore.edit { preferences ->
            preferences[accessToken] = ""
            preferences[refreshToken] = ""
            preferences[dateLogin] = 0
        }
    }

    fun getAlarm(): Flow<Reminder> {
        return dataStore.data.map { preferences ->
            Reminder(
                preferences[hour] ?: "",
                preferences[repeat] ?: 0
            )
        }
    }

    suspend fun saveAlarm(hour: String, repeat: Long) {
        dataStore.edit { preferences ->
            preferences[this.hour] = hour
            preferences[this.repeat] = repeat
        }
    }

    suspend fun cancelAlarm() {
        dataStore.edit { preferences ->
            preferences[hour] = ""
            preferences[repeat] = 0
        }
    }

    fun getWaste(): Flow<Waste> {
        return dataStore.data.map { preferences ->
            Waste(
                preferences[wastePoint] ?: 0,
                preferences[dateWaste] ?: 0
            )
        }
    }

    suspend fun saveWaste(date: Int) {
        dataStore.edit { preferences ->
            preferences[this.wastePoint] = 1
            preferences[this.dateWaste] = date
        }
    }

    suspend fun resetWaste() {
        dataStore.edit { preferences ->
            preferences[wastePoint] = 0
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): AppPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = AppPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}