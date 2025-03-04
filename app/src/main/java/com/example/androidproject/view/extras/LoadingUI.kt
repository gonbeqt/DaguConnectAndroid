package com.example.androidproject.view.extras

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun LoadingUI() {
    val infiniteTransition = rememberInfiniteTransition()
    val startAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    // Center the loading arc
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)), // Light background
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(40.dp) // Compact size
        ) {
            drawArc(
                color = Color(0xFF6200EE), // Purple arc
                startAngle = startAngle,
                sweepAngle = 90f, // Quarter-circle arc
                useCenter = false,
                style = Stroke(
                    width = 4.dp.toPx(), // Stroke thickness
                    cap = StrokeCap.Round // Rounded ends for smooth effect
                )
            )
        }
    }
}

