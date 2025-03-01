package com.example.androidproject.view.extras

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun LoadingTradesmanUI() {
    // Infinite rotation animation for the circular loading
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Center everything on the screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)), // Light background
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Tradesman icon with circular loading
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .drawBehind {
                        // Draw a dashed circular loading ring
                        drawCircle(
                            color = Color(0xFF6200EE), // Purple ring
                            radius = size.width / 2 - 8.dp.toPx(), // Adjust radius
                            style = Stroke(
                                width = 4.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(20f, 20f), // Dash pattern
                                    0f
                                )
                            )
                        )
                    }
                    .rotate(rotation) // Rotate the dashed circle
            ) {
                // Tradesman icon (using emoji for simplicity)
                Text(
                    text = "👷", // Tradesman emoji
                    fontSize = 40.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Loading text
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading, please wait",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 20.sp,
                    color = Color(0xFF6200EE) // Purple text
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}