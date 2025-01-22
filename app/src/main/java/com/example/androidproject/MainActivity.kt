package com.example.androidproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.RetrofitInstance
import com.example.androidproject.data.preferences.TokenManager
import com.example.androidproject.ui.theme.AndroidProjectTheme
import com.example.androidproject.ui.theme.views.LandingPageScreen
import com.example.androidproject.ui.theme.views.LogInScreen
import com.example.androidproject.ui.theme.views.SignUpScreen
import com.example.androidproject.viewmodels.LoginViewModel
import com.example.androidproject.viewmodels.RegisterViewModel
import com.example.androidproject.viewmodels.factories.LoginViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(this)
        val apiService = RetrofitInstance.create(ApiService::class.java)
        val viewModelFactory = LoginViewModelFactory(apiService)
        val loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
        setContent {
            AndroidProjectTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "landing_page") {
                    composable("landing_page") {
                        LandingPageScreen(navController)
                    }
                    composable("signup") {
                        SignUpScreen(navController)
                    }
                    composable("login") {
                        LogInScreen(navController, loginViewModel)
                    }
                    composable("main_screen"){
                        MainScreen(navController)
                    }
                }
            }
        }
    }
}

