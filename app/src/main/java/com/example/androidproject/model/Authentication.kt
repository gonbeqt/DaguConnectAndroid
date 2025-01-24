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
    val age: Int,
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
    var firstName: String,
    @SerializedName("last_name")
    var lastName: String,
    var username: String,
    var email: String,
    var age: Int,
    @SerializedName("is_client")
    var isClient: Boolean,
    var password: String
)

data class RegisterResponse (
    var message: String
)
