package com.example.androidproject.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    var email: String,
    var password: String
)

data class LoginResponse(
    val message: String,
    val token: String?,
    val user: User
)

data class User(
    val id: Int,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val username: String,
    val birthdate: String,
    val suspend: Boolean,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: String,
    val email: String,
    @SerializedName("is_client")
    val isClient: Boolean,
    @SerializedName("created_at")
    val createdAt: String
)

data class RegisterRequest (
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val username: String,
    val birthdate: String,
    val email: String,
    @SerializedName("is_client")
    val isClient: Boolean,
    val password: String,
    @SerializedName("confirm_password")
    val confirmPassword: String
)

data class RegisterResponse (
    var message: String
)
