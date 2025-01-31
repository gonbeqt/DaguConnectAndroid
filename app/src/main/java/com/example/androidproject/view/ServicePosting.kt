package com.example.androidproject.view

data class ServicePosting(
    val title: String,
    val postedDate: String,
    val isActive: Boolean = true,
    val description: String = "Description of the service",
    val rate: String = "Estimated Budget",
    val category: String = "",
    val applicantsCount: Int = 0
)
