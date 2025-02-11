package com.example.androidproject.view

data class ServicePosting(
    val title: String,
    val postedDate: String,
    val isActive: Boolean = true,
    val description: String = "Description of the service",
    val location:String,
    val rate: String = "Estimated Budget",
    val deadline:String,
    val category: String = "",
    val applicantsCount: Int = 0
)
