package com.example.androidproject.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object NotificationSettingManager {
    private const val PREF_NAME = "notification_data"
    private const val ACCOUNT_KEY = "notification"
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveNotification(notification: Boolean) {
        Log.d("NotificationSettings", "Saving notification: $notification")
        checkInitialized()
        preferences.edit().putBoolean(ACCOUNT_KEY, notification).apply()
    }

    fun getNotification(): Boolean {
        checkInitialized()
        return preferences.getBoolean(ACCOUNT_KEY, true) // Default to true
    }

    fun clearNotificationData() {
        checkInitialized()
        preferences.edit().remove(ACCOUNT_KEY).apply()
    }

    private fun checkInitialized() {
        Log.d("NotificationSettings", "Checking initialization...")
        if (!::preferences.isInitialized) {
            throw IllegalStateException("NotificationSettingManager must be initialized before use. Call init(context) first.")
            Log.d("NOTIFICATION SETTING MANAGER", "NOT INITIALIZED")
        }
        Log.d("NOTIFICATION SETTING MANAGER", "INITIALIZED")
    }
}