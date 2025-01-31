package com.example.androidproject.model

import android.os.Parcelable.Creator
import com.google.gson.annotations.SerializedName

data class RequestJobs(
    @SerializedName("client_fullname")
    val clientFullName: Int,
    @SerializedName("client_profile")
    val clientProfile: String?,
    val salary: Double,
    @SerializedName("job_type")
    val jobType: String,
    @SerializedName("job_description")
    val jobDescription: String,
    val status: String,
    val deadline: String,
)

data class JobResponse(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("client_fullname")
    val clientFullname: String,
    @SerializedName("client_profile")
    val clientProfile: String,
    val salary: Double,
    @SerializedName("job_type")
    val jobType: String,
    @SerializedName("job_description")
    val jobDescription: String,
    val status: String,
    val deadline: String,
    @SerializedName("created_at")
    val createdAt: String
)
