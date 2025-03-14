package com.example.androidproject.data

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.TimeUnit

data class NotificationData(
    val title: String?,
    val message: String?
)

object WebSocketManager {
    private const val URL = "ws://192.168.1.141:8080" // Replace with your server IP
    private const val TAG = "WebSocketManager"

    private val client = OkHttpClient.Builder()
        .pingInterval(10, TimeUnit.SECONDS) // Keep connection alive
        .build()

    private var webSocket: WebSocket? = null
    private val _incomingMessages = MutableStateFlow<List<String>>(emptyList())
    val incomingMessages = _incomingMessages.asStateFlow()

    // New flow for notification data
    private val _notifications = MutableStateFlow<NotificationData?>(null)
    val notifications = _notifications.asStateFlow()

    private val gson = Gson()

    fun connect(userId: String) {
        if (webSocket != null) {
            Log.d(TAG, "WebSocket already connected.")
            return
        }

        val request = Request.Builder().url(URL).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(TAG, "WebSocket connected!")
                val authMessage = """{"type": "auth", "user_id": "$userId"}"""
                webSocket.send(authMessage)
                Log.d(TAG, "Sent auth message: $authMessage")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d(TAG, "Received message: $text")
                _incomingMessages.update { currentMessages -> listOf(text) + currentMessages }

                // Parse the incoming message and emit notification data if applicable
                try {
                    val jsonObject = gson.fromJson(text, Map::class.java)
                    val type = jsonObject["type"] as? String
                    if (type == "notification") {
                        val data = jsonObject["data"] as? Map<*, *>
                        val title = data?.get("notification_title") as? String
                        val message = data?.get("message") as? String
                        _notifications.value = NotificationData(title, message)
                    }
                } catch (e: JsonSyntaxException) {
                    Log.e(TAG, "Failed to parse WebSocket message: ${e.message}")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "WebSocket failure: ${t.message}", t)
                this@WebSocketManager.webSocket = null
                Handler(Looper.getMainLooper()).postDelayed({
                    Log.d(TAG, "Reconnecting WebSocket...")
                    connect(userId)
                }, 5000)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "WebSocket closing: $reason")
                webSocket.close(code, reason)
                this@WebSocketManager.webSocket = null
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "WebSocket closed: $reason")
                this@WebSocketManager.webSocket = null
            }
        })
    }

    fun sendMessage(userId: String, receiverId: String, message: String) {
        val json = """{"type": "message", "user_id": "$userId", "receiver_id": "$receiverId", "message": "$message", "is_read": 0}"""
        if (webSocket != null) {
            val success = webSocket?.send(json) == true
            Log.d(TAG, if (success) "Sent: $json" else "Failed to send: $json")
        } else {
            Log.e(TAG, "WebSocket is not connected. Cannot send message: $json")
        }
    }

    fun sendNotificationJobToTradesman(resumeId: String, notificationTitle: String, notificationType: String, message: String) {
        val notification = mapOf(
            "type" to "notification",
            "client" to true,
            "resume_id" to resumeId,
            "notification_title" to notificationTitle,
            "notificationType" to notificationType,
            "message" to message
        )
        val json = gson.toJson(notification)
        if (webSocket != null) {
            val success = webSocket?.send(json) == true
            Log.d(TAG, if (success) "Sent: $json" else "Failed to send: $json")
        } else {
            Log.e(TAG, "WebSocket is not connected. Cannot send message: $json")
        }
    }

    fun sendNotificationJobToClient(clientId: String, notificationTitle: String, notificationType: String, message: String) {
        val notification = mapOf(
            "type" to "notification",
            "tradesman" to true,
            "client_id" to clientId,
            "notification_title" to notificationTitle,
            "notificationType" to notificationType,
            "message" to message
        )
        val json = gson.toJson(notification)
        if (webSocket != null) {
            val success = webSocket?.send(json) == true
            Log.d(TAG, if (success) "Sent: $json" else "Failed to send: $json")
        } else {
            Log.e(TAG, "WebSocket is not connected. Cannot send message: $json")
        }
    }

    fun disconnect() {
        Log.d(TAG, "Disconnecting WebSocket...")
        webSocket?.close(1000, "Normal Closure")
        webSocket = null
        Log.d(TAG, "WebSocket disconnected.")
    }
}