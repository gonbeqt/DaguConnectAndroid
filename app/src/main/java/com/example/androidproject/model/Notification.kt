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
    val userId: Int,
    val notificationTitle: String,
    val notificationType: String,
    val message: String,
    val jobId: Int?,
    val chatId: Int?,
    val reportId: Int?,
    val isRead: Boolean,
    val createdAt: String
)