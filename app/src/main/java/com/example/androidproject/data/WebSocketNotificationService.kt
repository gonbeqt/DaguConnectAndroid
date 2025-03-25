package com.example.androidproject.data

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.androidproject.MainActivity
import com.example.androidproject.data.preferences.AccountManager
import com.example.androidproject.data.preferences.NotificationSettingManager
import com.example.androidproject.view.client.AccountSettings
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

        scope.launch {
            Log.d(TAG, "Starting to collect WebSocket notifications")
            WebSocketManager.notifications.collect { notification ->
                notification?.let {
                    if (NotificationSettingManager.getNotification() == true || NotificationSettingManager.getNotification() == null) {
                        Log.d(TAG, "Collected notification: Title=${it.title}, Message=${it.message}")
                        showNotification(it.title, it.message)
                    } else {
                        Log.d(TAG, "Notification is turned off: ${NotificationSettingManager.getNotification()}.")
                    }
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

        val channelId = "daguconnect_channel_id"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "App notifications" }
            notificationManager.createNotificationChannel(channel)
        }

        // Create an Intent to launch the app's MainActivity
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            if (AccountManager.getAccount()?.isClient == true){
                putExtra("navigate_to", "main_screen?selectedItem=1")
            } else {
                putExtra("navigate_to", "main_screen?selectedItem=1")
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Replace with your app’s icon
            .setContentTitle(title ?: "Notification")
            .setContentText(messageBody ?: "New message received")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(messageBody ?: "New message received"))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        Log.d(TAG, "Notification posted")
    }
}