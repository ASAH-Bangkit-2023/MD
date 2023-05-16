package com.example.md.injection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.md.local.preference.AppPreference
import com.example.md.repository.AlarmRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("app")

object Injection {
    fun getAlarmRepository(context: Context): AlarmRepository {
        val dataStore = AppPreference.getInstace(context.dataStore)
        return AlarmRepository(dataStore)
    }
}