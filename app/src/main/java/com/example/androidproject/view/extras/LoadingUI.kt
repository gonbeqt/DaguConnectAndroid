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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun LoadingUI() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Square white box with shadow behind the CircularProgressIndicator
        Box(
            modifier = Modifier
                .size(60.dp) // Define the size of the square box
                .shadow(
                    elevation = 8.dp, // Shadow elevation for the 3D effect
                    shape = RoundedCornerShape(12.dp), // Rounded corners for a polished look
                    clip = true // Clip the shadow to the shape
                )
                .background(Color.White) // White background for the box
        )

        // CircularProgressIndicator on top of the square box
        CircularProgressIndicator(
            color = Color(0xFF122826), // Match the app's color scheme
            modifier = Modifier
                .size(40.dp) // Size of the CircularProgressIndicator
        )
    }
}
