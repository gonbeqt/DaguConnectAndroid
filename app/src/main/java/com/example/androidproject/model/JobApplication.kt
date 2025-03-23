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
    @SerializedName("client_id")
    val clientId: Int,
    @SerializedName("client_fullname")
    val clientFullname: String,
    @SerializedName("tradesman_fullname")
    val tradesmanFullname: String,
    @SerializedName("tradesman_profile_picture")
    val tradesmanProfilePicture: String,
    @SerializedName("client_profile_picture")
    val clientProfilePicture: String,
    @SerializedName("job_address")
    val jobAddress: String,
    @SerializedName("job_type")
    val jobType: String,
    @SerializedName("job_deadline")
    val jobDeadline: String,
    @SerializedName("qualification_summary")
    val qualificationSummary: String,
    val status: String,
    @SerializedName("cancelled_reason")
    val cancelledReason: String?,
    @SerializedName("cancelled_by")
    val cancelledBy: String?,
    @SerializedName("job_date_status")
    val jobDateStatus: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class UpdateStatus(
    val status: String,
    @SerializedName("cancellation_reason")
    val cancellationReason: String?
)

data class UpdateStatusResponse(
    val message: String
)

data class GetJobApplicantsData(
    val applicants: List<JobApplicantData>,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("total_page")
    val totalPage: Int
)

data class JobApplicantData(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("resume_id")
    val resumeId: Int,
    @SerializedName("job_id")
    val jobId: Int,
    @SerializedName("client_id")
    val clientId: Int,
    @SerializedName("client_fullname")
    val clientFullname: String,
    @SerializedName("tradesman_fullname")
    val tradesmanFullname: String,
    @SerializedName("tradesman_profile_picture")
    val tradesmanProfilePicture: String,
    @SerializedName("client_profile_picture")
    val clientProfilePicture: String,
    @SerializedName("job_address")
    val jobAddress: String,
    @SerializedName("job_type")
    val jobType: String,
    @SerializedName("job_deadline")
    val jobDeadline: String,
    @SerializedName("qualification_summary")
    val qualificationSummary: String,
    val status: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("cancelled_by")
    val cancelledBy: String,
    @SerializedName("cancelled_reason")
    val cancelledReason: String,
    @SerializedName("job_date_status")
    val jobDateStatus: String
)

data class ViewJobApplicationData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("resume_id")
    val resumeId: Int,
    @SerializedName("job_id")
    val jobId: Int,
    @SerializedName("client_id")
    val clientId: Int,
    @SerializedName("client_fullname")
    val clientFullname: String,
    @SerializedName("tradesman_fullname")
    val tradesmanFullname: String,
    @SerializedName("tradesman_profile_picture")
    val tradesmanProfilePicture: String,
    @SerializedName("client_profile_picture")
    val clientProfilePicture: String,
    @SerializedName("job_address")
    val jobAddress: String,
    @SerializedName("job_type")
    val jobType: String,
    @SerializedName("job_deadline")
    val jobDeadline: String,
    @SerializedName("qualification_summary")
    val qualificationSummary: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("cancelled_reason")
    val cancelledReason: String?,
    @SerializedName("cancelled_by")
    val cancelledBy: String?,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("created_at")
    val createdAt: String
)

data class ViewJobApplicationResponse(
    @SerializedName("job_application")
    val jobApplication: ViewJobApplicationData
)
