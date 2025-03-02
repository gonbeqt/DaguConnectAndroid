package com.example.androidproject.model

import com.google.gson.annotations.SerializedName

data class GetChats(
    val chats: List<Chats>,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("last_page")
    val lastPage: Int
)

data class Chats(
    val id: Int,
    @SerializedName("user1_id")
    val userId1: Int,
    @SerializedName("user2_id")
    val userId2: Int,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("latest_message")
    val latestMessage: String,
    @SerializedName("profile_picture")
    val profilePicture: String,
    @SerializedName("last_sender_id")
    val lastSenderId: Int,
    @SerializedName("is_read")
    val isRead: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)