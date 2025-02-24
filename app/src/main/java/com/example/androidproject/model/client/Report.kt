package com.example.androidproject.model.client

data class ReportRequest(
    val report_reason: String,
    val report_details: String,
)

data class ReportResponse(
    var message : String
)