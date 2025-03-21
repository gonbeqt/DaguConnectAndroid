package com.example.androidproject.data.preferences

import android.content.Context
import android.content.SharedPreferences

object NotificationSettingManager {
    private const val PREF_NAME = "notification_data"
    private const val ACCOUNT_KEY = "notification"
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveNotification(notification: Boolean) {
        preferences.edit().putBoolean(ACCOUNT_KEY, notification).apply()
    }

    fun getNotification(): Boolean? {
        return if (preferences.contains(ACCOUNT_KEY)) {
            preferences.getBoolean(ACCOUNT_KEY, true) // Default value if not set
        } else {
            null
        }
    }

    fun clearNotificationData(){
        preferences.edit().remove(ACCOUNT_KEY).apply()
    }
}