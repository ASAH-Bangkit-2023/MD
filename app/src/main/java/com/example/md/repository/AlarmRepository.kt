package com.example.md.repository

import com.example.md.local.preference.AppPreference

class AlarmRepository(private val pref: AppPreference) {
    suspend fun saveAlarm(hour: String, repeat: Long) {
        pref.saveAlarm(hour, repeat)
    }

    suspend fun cancelAlarm() {
        pref.cancelAlarm()
    }

    fun getAlarm() = pref.getAlarm()
}