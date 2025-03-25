package com.example.androidproject.data

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import android.content.pm.ServiceInfo

class WebSocketService : Service() {
    private val TAG = "WebSocketService"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val userId = intent?.getStringExtra("userId")
        if (userId == null) {
            Log.w(TAG, "No userId provided, stopping service")
            stopSelf()
            return START_NOT_STICKY
        }

        val channelId = "websocket_service_channel"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "WebSocket Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply { description = "Keeps WebSocket connection alive" }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("DaguConnect")
            .setContentText("App is running in the background.")
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        // Specify the foreground service type for Android 14+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                1,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            startForeground(1, notification)
        }

        if (!WebSocketManager.isConnected()) {
            WebSocketManager.connect(userId)
            WebSocketNotificationManager.initialize(applicationContext, userId)
        }

        Log.d(TAG, "WebSocketService started with userId: $userId")
        return START_STICKY
    }

    override fun onDestroy() {
        WebSocketNotificationManager.cleanup()
        WebSocketManager.disconnect()
        Log.d(TAG, "WebSocketService destroyed")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}