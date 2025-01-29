package com.example.androidproject.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

data class WindowSize(
    val width: WindowType,
    val height: WindowType
)

enum class WindowType {
    SMALL, MEDIUM, LARGE
}

@Composable
fun rememberWindowSizeClass(): WindowSize {
    val configuration = LocalConfiguration.current
    return WindowSize(
        width = when {
            configuration.screenWidthDp < 600 -> WindowType.SMALL
            configuration.screenWidthDp < 840 -> WindowType.MEDIUM
            else -> WindowType.LARGE
        },
        height = when {
            configuration.screenHeightDp < 480 -> WindowType.SMALL
            configuration.screenHeightDp < 900 -> WindowType.MEDIUM
            else -> WindowType.LARGE
        }
    )
}
