package com.example.androidproject.model.client

import com.google.gson.annotations.SerializedName



data class GetClientsBooking(
    val address: String,
    @SerializedName("booking_date")
    val bookingdate: String,
    @SerializedName("booking_status")
    val bookingstatus: String,
    @SerializedName("created_at")
    val createdat: String,
    val id: Int,
    @SerializedName("phone_number")
    val phonenumber: String,
    @SerializedName("resume_id")
    val resumeid: Int,
    @SerializedName("task_description")
    val taskdescription: String,
    @SerializedName("task_type")
    val tasktype: String,
    @SerializedName("tradesmanid")
    val tradesman_id: Int,
    @SerializedName("user_id")
    val userid: Int,
    @SerializedName("work_status")
    val workstatus: Any
)