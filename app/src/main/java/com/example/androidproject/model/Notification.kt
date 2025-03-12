package com.example.androidproject.model

import com.google.gson.annotations.SerializedName


data class GetNotification(
    val notifications: List<Notification>,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)

data class Notification(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("notification_title")
    val notificationTitle: String,
    @SerializedName("notification_type")
    val notificationType: String,
    val message: String,
    @SerializedName("job_id")
    val jobId: Int?,
    @SerializedName("chat_id")
    val chatId: Int?,
    @SerializedName("report_id")
    val reportId: Int?,
    @SerializedName("is_read")
    val isRead: Int,
    @SerializedName("created_at")
    val createdAt: String
)