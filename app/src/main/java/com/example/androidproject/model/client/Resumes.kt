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
    val preferedworklocation: String,
    @SerializedName("profile_pic")
    val profilepic: String,
    val specialty:  String,
    @SerializedName("tradesman_full_name")
    val tradesmanfullname: String,
    @SerializedName("updated_at")
    val updatedat: String,
    @SerializedName("user_id")
    val userid: Int,
    @SerializedName("work_fee")
    val workfee: Int,
    val ratings: Float,
    @SerializedName("about_me")
    val aboutme: String,
    val is_active: Int
)








