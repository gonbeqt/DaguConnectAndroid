package com.example.androidproject.model.client

import com.google.gson.annotations.SerializedName

data class ViewClientBooking(
    val address: String,
    @SerializedName("booking_date")
    val bookingDate: String,
    @SerializedName("booking_status")
    val bookingStatus: String,
    @SerializedName("cancel_reason")
    val cancelReason: Any,
    @SerializedName("client_fullname")
    val clientFullName: String,
    @SerializedName("created_at")
    val createdAt: String,
    val id: Int,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("resume_id")
    val resumeId: Int,
    @SerializedName("task_description")
    val taskDescription: String,
    @SerializedName("task_type")
    val taskType: String,
    @SerializedName("tradesman_fullname")
    val tradesmanFullName: String,
    @SerializedName("tradesman_id")
    val tradesmanId: Int,
    @SerializedName("tradesman_profile")
    val tradesmanProfile: String,
    @SerializedName("user_id")
    val userid: Int,
    @SerializedName("work_fee")
    val workFee: Int
)