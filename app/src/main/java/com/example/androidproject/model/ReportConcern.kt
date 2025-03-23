package com.example.androidproject.model

import com.google.gson.annotations.SerializedName

data class ReportConcernRequest(
    @SerializedName("user_email")
        val userEmail : String,
    @SerializedName("report_message")
        val reportMessage :String,
    @SerializedName("report_problem")
        val reportProblem : String
        )

data class ReportConcernResponse(
    val message: String

)