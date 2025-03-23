package com.example.androidproject.model

import com.google.gson.annotations.SerializedName

data class GetMessages (
    @SerializedName("requester_id")
    val requesterId: Int,
    val messages: List<Conversation>,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)

data class Conversation (
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("receiver_id")
    val receiverId: Int,
    @SerializedName("chat_id")
    val chatId: Int,
    val message: String,
    @SerializedName("is_read")
    val isRead: Int,
    @SerializedName("created_at")
    val createdAt: String
)

data class PostMessage(
    @SerializedName("receiver_id")
    val receiverId: String,
    val message: String
)

data class PostMessageResponse(
    val message: String
)

data class MessageUser(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("receiver_id")
    val receiverId: Int,
    val message: String,
)
