package com.example.androidproject.model

import com.google.gson.annotations.SerializedName

data class PostJobApplication(
    @SerializedName("qualification_summary")
    val qualificationSummary: String,
)

data class PostJobApplicationResponse(
    val message: String
)

data class GetJobApplicationData(
    val applications: List<JobApplicationData>,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("total_page")
    val totalPage: Int
)

data class JobApplicationData(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("resume_id")
    val resumeId: Int,
    @SerializedName("job_id")
    val jobId: Int,
    @SerializedName("tradesman_profile_picture")
    val tradesmanProfilePicture: String,
    @SerializedName("job_type")
    val jobType: String,
    @SerializedName("qualification_summary")
    val qualificationSummary: String,
    val status: String,
    @SerializedName("created_at")
    val createdAt: String,
)
