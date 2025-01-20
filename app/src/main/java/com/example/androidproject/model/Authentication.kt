package com.example.androidproject.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    var email: String,
    var password: String
)

data class LoginResponse(
    val message: String,
    val admin: List<AdminData>
)

data class AdminData (
    val token: String,
    val username: String,
    val email: String
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


//Forgot Password Data Classes
data class ForgotPassword(
    var email: String
)

data class VerifyOTP (
    var otp: Int
)
