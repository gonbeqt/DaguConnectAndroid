package com.example.androidproject.view.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.google.gson.internal.bind.TypeAdapters.FLOAT

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
        Color(0xFF030706),
        Color(0xFF265147),
        Color(0xFF21473E),
        Color(0xFF418A79),
    ),
    start = Offset(0f, Float.POSITIVE_INFINITY),
    end = Offset(0f, 0f) // Move upwards
)
val myGradient2 = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF79F1BB),
        Color(0xFF418A79),
        Color(0xFF327760),
        Color(0xFF418A79),
        Color(0xFF79F1BB),

    )
)
val myGradient3 = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF030706),
        Color(0xFF21473E),
        Color(0xFF418A79),
        Color(0xFF21473E),
        Color(0xFF030706),

        )

)
val myGradient4 = Brush.linearGradient(
    colors = listOf(
        Color(0xFF030706), // Deepest Dark Green (top left)
        Color(0xFF21473E), // Dark Green
        Color(0xFF265147), // Muted Teal Green
        Color(0xFF418A79), // Vibrant Teal Green
        Color(0xFF79F1BB), // Soft Mint Green (glow effect)
    ),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
)

val myGradient5 = Brush.verticalGradient(
    colors = listOf(
        Color.Transparent,
        Color.Black.copy(alpha = 0.6f)
    ),
    startY = 0f,
    endY = Float.POSITIVE_INFINITY
)

