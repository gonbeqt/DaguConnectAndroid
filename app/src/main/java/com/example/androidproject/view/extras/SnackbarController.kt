package com.example.androidproject.view.extras

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay

data class SnackbarState(
    val message: String = "",
    val show: Boolean = false,
    val duration: Long = 1000L
)

val LocalSnackbarState = compositionLocalOf {
    mutableStateOf(SnackbarState())
}

// Create a global snackbar controller
object SnackbarController {
    private var state by mutableStateOf(SnackbarState())

    fun show(message: String, duration: Long = 1000L) {
        state = SnackbarState(
            message = message,
            show = true,
            duration = duration
        )
    }

    fun hide() {
        state = state.copy(show = false)
    }

    @Composable
    fun ObserveSnackbar() {
        val currentState = state
        CustomDurationSnackbar(
            message = currentState.message,
            show = currentState.show,
            duration = currentState.duration,
            onDismiss = { hide() }
        )
    }
}

@Composable
fun CustomDurationSnackbar(
    message: String,
    show: Boolean,
    duration: Long = 1000L,
    onDismiss: () -> Unit,
) {
    AnimatedVisibility(
        visible = show,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Snackbar(
            modifier = Modifier
                .wrapContentWidth()
                .padding(bottom = 8.dp)
                .padding(horizontal = 16.dp)
                .shadow(5.dp),
            containerColor = Color.Gray,
            contentColor = Color.White,
            content = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = message, fontSize = 16.sp, color = Color.White)
                }
            }
        )
    }

    LaunchedEffect(show) {
        if (show) {
            delay(duration)
            onDismiss()
        }
    }
}