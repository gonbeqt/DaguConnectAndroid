package com.example.androidproject.model

import com.google.gson.annotations.SerializedName

data class GetMessages (
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("receiver_id")
    val receiverId: Int,
    @SerializedName("chat_id")
    val chatId: Int,
    val message: String,
    @SerializedName("is_read")
    val isRead: Boolean,
    @SerializedName("created_at")
    val createdAt: String
)