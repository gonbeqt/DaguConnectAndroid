package com.example.androidproject.model

import com.google.gson.annotations.SerializedName

data class ClientProfile(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("full_name")
    val fullname: String,
    val email: String,
    val address: String,
    @SerializedName("profile_picture")
    val profilePicture: String,
    @SerializedName("created_at")
    val createdAt: String
)