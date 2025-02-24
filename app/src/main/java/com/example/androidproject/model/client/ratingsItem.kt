package com.example.androidproject.model.client


data class ratingsItem(
    val client_id: Int,
    val client_name: String,
    val client_profile: String,
    val id: Int,
    val message: String,
    val rated_at: String,
    val ratings: Int,
    val tradesman_fullname: String,
    val tradesman_id: Int
)

data class ratingRequest(
    val message: String,
    val rating: Int,
)

data class ratingResponse(
    val message: String
)