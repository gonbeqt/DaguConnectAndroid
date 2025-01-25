package com.example.androidproject

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidproject.ui.theme.AndroidProjectTheme
import com.example.androidproject.ui.theme.views.LandingPageScreen
import com.example.androidproject.ui.theme.views.LogInScreen
import com.example.androidproject.ui.theme.views.SignUpScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val isShown = sharedPreferences.getBoolean("isShown", false)
        val startDestination = if (isShown) "login" else "landing_page"

        setContent {
            AndroidProjectTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = startDestination
                ) {
                    composable("landing_page") {
                        LandingPageScreen(navController)
                    }
                    composable("signup") {
                        SignUpScreen(navController)
                    }
                    composable("login") {
                        LogInScreen(navController)

                    }
                    composable("main_screen"){
                        MainScreen(navController)
                    }

                }
            }

        }
    }
}

