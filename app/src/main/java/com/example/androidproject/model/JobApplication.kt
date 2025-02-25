package com.example.androidproject.model

import androidx.resourceinspection.annotation.Attribute.IntMap
import com.google.gson.annotations.SerializedName

data class PostJobApplication(
    @SerializedName("qualification_summary")
    val qualificationSummary: String,
)

data class PostJobApplicationResponse(
    val message: String
)
