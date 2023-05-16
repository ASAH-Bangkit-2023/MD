package com.example.md.local.preference

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.md.local.data.Alarm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreference private constructor(private val dataStore: DataStore<Preferences>) {
    private val HOUR = stringPreferencesKey("hour")
    private val REPEAT = longPreferencesKey("repeat")

    fun getAlarm(): Flow<Alarm> {
        return dataStore.data.map { preferences ->
            Log.e("appPreferenceGet", preferences.toString())
            Alarm(
                preferences[HOUR] ?: "",
                preferences[REPEAT] ?: 0
            )
        }
    }

    suspend fun saveAlarm(hour: String, repeat: Long) {
        Log.e("appPreferenceSave", "$hour : $repeat")
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