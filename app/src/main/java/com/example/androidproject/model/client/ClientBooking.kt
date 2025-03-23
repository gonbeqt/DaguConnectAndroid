package com.example.androidproject.model.client

import com.google.gson.annotations.SerializedName


data class GetClientsBookingResponse(
    val bookings: List<GetClientsBooking>,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)

data class GetClientsBooking(
    val address: String,
    val ratings: Float,
    @SerializedName("booking_date")
    val bookingDate: String,
    @SerializedName("booking_status")
    val bookingStatus: String,
    @SerializedName("created_at")
    val createdAt: String,
    val id: Int,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("tradesman_fullname")
    val tradesmanFullName : String,
    @SerializedName("tradesman_profile")
    val tradesmanProfile : String,
    @SerializedName("work_fee")
    val workFee: Int,
    @SerializedName("client_fullname")
    val clientFullName: String,
    @SerializedName("resume_id")
    val resumeId: Int,
    @SerializedName("task_description")
    val taskDescription: String,
    @SerializedName("task_type")
    val taskType: String,
    @SerializedName("tradesman_id")
    val tradesmanId: Int,
    @SerializedName("user_id")
    val userid: Int,
    @SerializedName("cancel_reason")
    val cancelReason: String,
    @SerializedName("booking_date_status")
    val bookingDateStatus: String,

)

 data class GetTradesmanBookingResponse(
     val bookings: List<GetTradesmanBooking>,
     @SerializedName("current_page")
     val currentPage: Int,
     @SerializedName("total_pages")
     val totalPages: Int
 )

data class GetTradesmanBooking(
    val address: String,
    val ratings: Float,
    @SerializedName("booking_date")
    val bookingDate: String,
    @SerializedName("booking_status")
    val bookingStatus: String,
    @SerializedName("created_at")
    val createdAt: String,
    val id: Int,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("tradesman_fullname")
    val tradesmanFullName : String,
    @SerializedName("tradesman_profile")
    val tradesmanProfile : String,
    @SerializedName("work_fee")
    val workFee: Int,
    @SerializedName("client_fullname")
    val clientFullName: String,
    @SerializedName("resume_id")
    val resumeId: Int,
    @SerializedName("client_profile")
    val clientProfile: String,
    @SerializedName("task_description")
    val taskDescription: String,
    @SerializedName("task_type")
    val taskType: String,
    @SerializedName("tradesman_id")
    val tradesmanId: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("cancel_reason")
    val cancelReason: String,
    @SerializedName("booking_date_status")
    val bookingDateStatus: String,

)
