package com.example.androidproject.view

data class Tradesman(
    val imageResId: Int,
    val username: String,
    val category: String,
    val rate: String,
    val reviews: Double,
    val bookmark: Int
)
data class Tradesmandate(
    val imageResId: Int,
    val username: String,
    val category: String,
    val rate: String,
    val reviews: Double,
    val bookmark: Int,
    val date: String
)
