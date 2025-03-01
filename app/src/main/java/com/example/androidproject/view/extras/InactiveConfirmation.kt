package com.example.androidproject.view.extras

import androidx.compose.runtime.Composable

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InactiveConfirmation(
    onYesClick: () -> Unit = {} // Callback for Yes button
) {
    // Fade-in and slight bounce animation for the tradesman and text
    val transition = updateTransition(targetState = true, label = "fadeIn")
    val alpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 500) },
        label = "alpha"
    ) { if (it) 1f else 0f }

    val bounceOffset by transition.animateDp(
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
        },
        label = "bounce"
    ) { if (it) 0.dp else (-20).dp }

    // Full-screen overlay with centered content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)), // Semi-transparent overlay
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            // Tradesman with bounce animation
            Text(
                text = "👷", // Tradesman emoji
                fontSize = 48.sp,
                modifier = Modifier
                    .offset(y = bounceOffset)
                    .graphicsLayer(alpha = alpha)
            )

            // Message with fade-in
            Text(
                text = "Hello, are you still there?",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    color = Color(0xFF333333) // Dark gray text
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 16.dp)
                    .graphicsLayer(alpha = alpha)
            )

            // Yes button
            Button(
                onClick = onYesClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE), // Purple button
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(100.dp)
                    .height(40.dp)
            ) {
                Text(
                    text = "Yes",
                    fontSize = 16.sp
                )
            }
        }
    }
}