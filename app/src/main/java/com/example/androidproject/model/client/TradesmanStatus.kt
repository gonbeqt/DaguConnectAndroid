package com.example.androidproject.model.client

import com.google.gson.annotations.SerializedName


data class UpdateActiveStatusRequest(
    @SerializedName("tradesman_status")
    val tradesmanStatus : Boolean
)

data class UpdateActiveStatusResponse(
    val message: String
)