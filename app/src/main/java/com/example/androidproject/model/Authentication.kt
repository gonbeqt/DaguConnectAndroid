package com.example.androidproject.model

import com.google.gson.annotations.SerializedName

data class Login(
    var email: String,
    var password: String
)

data class Register (
    @SerializedName("first_name")
    var firstName: String,
    @SerializedName("last_name")
    var lastName: String,
    var username: String,
    var email: String,
    var age: Int,
    var password: String
)


//Forgot Password Data Classes
data class ForgotPassword(
    var email: String
)

data class VerifyOTP (
    var otp: Int
)
