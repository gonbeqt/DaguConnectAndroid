package com.example.androidproject.model

import com.google.gson.annotations.SerializedName

data class GetChats(
    val chats: List<Chats>
)

data class Chats(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("receiver_id")
    val receiverId: Int,
    @SerializedName("receiver_name")
    val receiverName: String,
    @SerializedName("latest_message")
    val latestMessage: String,
    @SerializedName("created_at")
    val createdAt: String
)