package com.example.androidproject.view.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
val  RoyalPurple = Color(0xFF804FB3)
val  Amethyst = Color(0xFF9969C7)
val  DarkGreen = Color(0xFF122826)

val myGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF030706), // 0% (Dark color at the bottom)
        Color(0xFF265147), // 30%
        Color(0xFF21473E), // 50%
        Color(0xFF418A79), // 100% (Lightest color at the top)
    ),
    start = Offset(0f, Float.POSITIVE_INFINITY), // Start from the bottom
    end = Offset(0f, 0f) // Move upwards
)
