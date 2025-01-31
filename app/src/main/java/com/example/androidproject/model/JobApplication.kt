package com.example.androidproject.model

import androidx.resourceinspection.annotation.Attribute.IntMap
import com.google.gson.annotations.SerializedName

data class PostJobApplication(
    @SerializedName("resume_id")
    val resumeId: Int,
    @SerializedName("job_id")
    val jobId: Int,
    @SerializedName("job_name")
    val jobName: String,
    @SerializedName("tradesman_profile_picture")
    val tradesmanProfilePicture: String,
    @SerializedName("job_type")
    val jobType: String,
    @SerializedName("qualification_summary")
    val qualificationSummary: String,
    val status: String,
    @SerializedName("created_at")
    val createdAt: String
)
