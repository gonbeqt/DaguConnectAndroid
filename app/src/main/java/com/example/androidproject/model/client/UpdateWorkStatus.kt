package com.example.androidproject.model.client


    data class TradesmanWorkStatusRequest(
        val work_status: String,
        val cancel_reason: String
    )

    data class TradesmanWorkStatusResponse(
        val message: String
    )


    data class ClientWorkStatusRequest(
        val book_status: String,
        val cancel_reason: String
    )

    data class ClientWorkStatusResponse(
        val message: String
    )


