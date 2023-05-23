package com.asahteam.md.repository

import com.asahteam.md.local.preference.AppPreference

class ReminderRepository private constructor(private val pref: AppPreference) {
    suspend fun saveAlarm(hour: String, repeat: Long) {
        pref.saveAlarm(hour, repeat)
    }

    suspend fun cancelAlarm() {
        pref.cancelAlarm()
    }

    fun getAlarm() = pref.getAlarm()

    companion object {
        @Volatile
        private var instance: ReminderRepository? = null

        fun getInstance(preference: AppPreference): ReminderRepository =
            instance ?: synchronized(this) {
                ReminderRepository(preference).apply {
                    instance = this
                }
            }
    }
}