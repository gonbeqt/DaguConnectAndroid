package com.example.androidproject.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    var email: String,
    var password: String
)

data class LoginResponse(
    val message: String,
    val token: String
)

data class RegisterRequest (
    @SerializedName("first_name")
    var firstName: String,
    @SerializedName("last_name")
    var lastName: String,
    var username: String,
    var email: String,
    var age: Int,
    var password: String
)

data class RegisterResponse (
    var message: String
)
