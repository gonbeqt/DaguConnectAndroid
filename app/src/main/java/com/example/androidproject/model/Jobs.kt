package com.example.androidproject.model

import android.os.Parcelable.Creator
import com.google.gson.annotations.SerializedName

data class PostJob(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("client_fullname")
    val clientFullName: Int,
    val salary: Double,
    @SerializedName("job_type")
    val jobType: String,
    @SerializedName("job_description")
    val jobDescription: String,
    val status: String,
    val deadline: String,
    @SerializedName("created_at")
    val createdAt: String,
)
