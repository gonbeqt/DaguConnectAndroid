package com.example.androidproject.model.client

data class BookTradesmanRequest(
    val address: String,
    val booking_date: String,
    val phone_number: String,
    val task_description: String,
    val task_type: String
)

data class BookTradesmanResponse (
    var message: String
)