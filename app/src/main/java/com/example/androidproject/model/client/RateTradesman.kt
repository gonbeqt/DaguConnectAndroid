package com.example.androidproject.model.client

data class rateTradesmanRequest(
    val message : String,
    val rating : Int
)

data class rateTradesmanResponse(
    val message : String
)