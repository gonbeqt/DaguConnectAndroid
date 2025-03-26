    package com.example.androidproject.model.client

import com.google.gson.annotations.SerializedName
data class ResumesResponse(
    @SerializedName("resume")
    val resumes: List<resumesItem>,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)

data class resumesItem(
    @SerializedName("created_at")
    val createdAt: String? = null, // Nullable with default
    val email: String,
    val id: Int,
    @SerializedName("prefered_work_location")
    val preferredWorkLocation: String,
    @SerializedName("profile_pic")
    val profilePic: String,
    val specialty:  String,
    @SerializedName("tradesman_full_name")
    val tradesmanFullName: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userid: Int,
    @SerializedName("work_fee")
    val workFee: Int,
    val ratings: Float,
    @SerializedName("about_me")
    val aboutMe: String,
    val is_Active: Int
)

data class SubmitResumeResponse(
    val message: String
)



    data class viewResume(
        @SerializedName("created_at")
        val createdAt: String,
        val email: String,
        val id: Int,
        @SerializedName("prefered_work_location")
        val preferredWorkLocation: String,
        @SerializedName("profile_pic")
        val profilePic: String,
        @SerializedName("phone_number")
        val phoneNumber : String,
        val specialty: String?,
        @SerializedName("tradesman_full_name")
        val tradesmanFullName: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_id")
        val userid: Int,
        @SerializedName("work_fee")
        val workFee: Int,
        val ratings: Float,
        val documents: String? ,
        @SerializedName("about_me")
        val aboutMe: String,
        @SerializedName("is_active")
        val isActive: Int,
        @SerializedName("is_approve")
        val isApprove: Int,
        @SerializedName("status_of_approval")
        val statusOfApproval: String?,
        val birthdate: String
    ){
        // Helper property to convert Int to Boolean
        val isActiveBoolean: Boolean
            get() = isActive != 0
    }


    data class UpdateTradesmanDetailsRequest(
        @SerializedName("about_me")
        val aboutMe : String,
        @SerializedName("prefered_work_location")
        val preferredWorkLocation: String,
        @SerializedName("work_fee")
        val workFee : Int,
        @SerializedName("phone_number")
        val phoneNumber : String
    )


    data class UpdateTradesmanDetailsResponse(
        val message: String
    )








