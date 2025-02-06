package com.example.androidproject.model

import com.google.gson.annotations.SerializedName

data class GetChats(
    val chats: List<Chats>
)

data class Chats(
    val id: Int,
    @SerializedName("user_id1")
    val userId1: Int,
    @SerializedName("user_id2")
    val userId2: Int,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("latest_message")
    val latestMessage: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String

)