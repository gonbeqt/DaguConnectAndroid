package com.example.androidproject.data

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
object WebSocketNotificationManager {
    private val TAG = "WebSocketNotifManager"
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var isInitialized = false
    private lateinit var context: Context // We'll need a context for notifications

    fun initialize(context: Context, userId: String) {
        if (isInitialized) {
            Log.d(TAG, "Already initialized")
            return
        }

        this.context = context.applicationContext
        Log.d(TAG, "WebSocketNotificationManager initialized with userId: $userId")

        // Start WebSocket connection
        WebSocketManager.connect(userId)

        // Collect WebSocket notifications
        scope.launch {
            Log.d(TAG, "Starting to collect WebSocket notifications")
            WebSocketManager.notifications.collect { notification ->
                notification?.let {
                    Log.d(TAG, "Collected notification: Title=${it.title}, Message=${it.message}")
                    showNotification(it.title, it.message)
                } ?: Log.d(TAG, "Notification was null")
            }
        }

        isInitialized = true
    }

    // Cleanup method
    fun cleanup() {
        Log.d(TAG, "Cleaning up WebSocketNotificationManager")
        WebSocketManager.disconnect()
        scope.cancel()
        isInitialized = false
    }

    private fun showNotification(title: String?, messageBody: String?) {
        Log.d(TAG, "Showing notification: Title=$title, Body=$messageBody")

        val channelId = "default_channel_id"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "App notifications" }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Replace with your app’s icon
            .setContentTitle(title ?: "Notification")
            .setContentText(messageBody ?: "New message received")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(messageBody ?: "New message received"))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        Log.d(TAG, "Notification posted")
    }
}