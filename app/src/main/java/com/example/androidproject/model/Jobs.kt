package com.example.androidproject.model

import android.os.Parcelable.Creator
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
// Add jobs
data class RequestJobs(
    val salary: Double,
    @SerializedName("applicant_limit_count")
    val applicantLimitCount: Int,
    @SerializedName("job_type")
    val jobType: String,
    @SerializedName("job_description")
    val jobDescription: String,
    val address: String,
    val status: String,
    val deadline: String,
)

data class PostJobResponse(
    val message: String
)

//Get all jobs
data class JobsResponse(
    val jobs: List<GetJobs>,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)

data class GetJobs(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("client_fullname")
    val clientFullname: String,
    @SerializedName("client_profile_id")
    val clientProfileId: Int,
    @SerializedName("client_profile_picture")
    val clientProfilePicture: String?,
    val salary: Double,
    @SerializedName("applicant_limit_count")
    val applicantLimitCount: Int,
    @SerializedName("job_type")
    val jobType: String,
    @SerializedName("job_description")
    val jobDescription: String,
    val address: String,
    val status: String,
    val deadline: String,
    val longitude: Double?,
    val latitude: Double?,
    @SerializedName("created_at")
    val createdAt: String
)

data class Job(
    val job: ViewJob
)

data class ViewJob(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("client_fullname")
    val clientFullname: String,
    @SerializedName("client_profile_picture")
    val clientProfile: String?,
    val salary: String?,
    @SerializedName("job_type")
    val jobType: String,
    @SerializedName("job_description")
    val jobDescription: String,
    val location: String,
    val status: String,
    val deadline: String,
    @SerializedName("created_at")
    val createdAt: String
)
