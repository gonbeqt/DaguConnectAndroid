package com.example.androidproject.model.client


    data class workstatusRequest(
        val work_status: String,
        val cancel_reason: String
    )

    data class workstatusResponse(
        val message: String
    )
