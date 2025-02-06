package com.example.androidproject.model.client

import com.google.gson.annotations.SerializedName


data class resumesItem(
    @SerializedName("created_at")
    val createdat: String,
    val email: String,
    val id: Int,
    @SerializedName("prefered_work_location")
    val preferedworklocation: List<String>,
    @SerializedName("profile_pic")
    val profilepic: String,
    val specialties: List<String>,
    @SerializedName("tradesman_full_name")
    val tradesmanfullname: String,
    @SerializedName("updated_at")
    val updatedat: String,
    @SerializedName("user_id")
    val userid: Int,
    @SerializedName("work_fee")
    val workfee: Int,
    @SerializedName("about_me")
    val aboutme: String
)








