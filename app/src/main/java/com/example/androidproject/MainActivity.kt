package com.example.androidproject

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.RetrofitInstance
import com.example.androidproject.data.preferences.TokenManager
import com.example.androidproject.ui.theme.views.pages.ACRepair
import com.example.androidproject.ui.theme.views.pages.Carpentry
import com.example.androidproject.ui.theme.views.pages.Cleaning
import com.example.androidproject.ui.theme.views.pages.Electrician
import com.example.androidproject.ui.theme.views.pages.Plumbing
import com.example.androidproject.view.Feedback
import com.example.androidproject.view.LandingPageScreen
import com.example.androidproject.view.LogInScreen
import com.example.androidproject.view.SignUpScreen
import com.example.androidproject.view.Tradesman
import com.example.androidproject.view.pages.BookNow
import com.example.androidproject.view.pages.BookingDetails
import com.example.androidproject.view.pages.BookingsScreen
import com.example.androidproject.view.pages.CancellationDetails
import com.example.androidproject.view.pages.ConfirmBook
import com.example.androidproject.view.pages.MessageScreen
import com.example.androidproject.view.pages.RateAndReviews
import com.example.androidproject.view.pages2.BookingsTradesman
import com.example.androidproject.view.pages2.BookmarkedTradesman
import com.example.androidproject.view.pages2.HomeTradesman
import com.example.androidproject.view.pages2.ScheduleTradesman
import com.example.androidproject.view.pages2.TradesmanApply
import com.example.androidproject.view.theme.AndroidProjectTheme
import com.example.androidproject.viewmodel.LoginViewModel
import com.example.androidproject.viewmodel.RegisterViewModel
import com.example.androidproject.viewmodel.factories.LoginViewModelFactory
import com.example.androidproject.viewmodel.factories.RegisterViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val isShown = sharedPreferences.getBoolean("isShown", false)
        val startDestination = if (isShown) "login" else "landing_page"
        val trade = Tradesman(R.drawable.pfp, "Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark)
        val feedback = Feedback(R.drawable.pfp, "Ezekiel", 4)
        TokenManager.init(this)

        val apiService = RetrofitInstance.create(ApiService::class.java)

        val registerVMFactory = RegisterViewModelFactory(apiService)
        val registerViewModel = ViewModelProvider(this, registerVMFactory)[RegisterViewModel::class.java]

        val viewModelFactory = LoginViewModelFactory(apiService, this)
        val loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        setContent {
            AndroidProjectTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = startDestination) {
                    composable("landing_page") {
                        LandingPageScreen(navController)
                    }
                    composable("signup") {
                        SignUpScreen(navController, registerViewModel)
                    }
                    composable("login") {
                        LogInScreen(navController, loginViewModel)
                    }
                    composable("main_screen"){
                        MainScreen(navController)
                    }
                    composable("message_screen") {
                        MessageScreen(modifier=Modifier,navController)
                    }

                    composable("booknow") {
                        BookNow(trade,feedback, navController)
                    }
                    composable("confirmbook") {
                        ConfirmBook(trade,navController)
                    }
                    composable("bookingdetails") {
                        BookingDetails(trade,navController)
                    }
                    composable("booking") {
                        BookingsScreen(modifier = Modifier,navController)
                    }
                    composable("rateandreviews") {
                        RateAndReviews(trade,navController)
                    }
                    composable("cancellationdetails") {
                        CancellationDetails(trade,navController)
                    }
                    composable("acrepair"){
                        ACRepair(navController)
                    }
                    composable("plumbing") {
                        Plumbing(navController)
                    }
                    composable("carpentry") {
                        Carpentry(navController)
                    }
                    composable("electrician") {
                        Electrician(navController)
                    }
                    composable("cleaning") {
                        Cleaning(navController)
                    }


                    //Tradesman Routes
                    composable("hometradesman") {
                        HomeTradesman(modifier = Modifier,navController)
                    }
                    composable("tradesmanapply") {
                        TradesmanApply(trade,navController)
                    }
                    composable("bookingstradesman") {
                        BookingsTradesman(modifier = Modifier,navController)
                    }
                    composable("bookmarkedtradesman") {
                        BookmarkedTradesman(modifier = Modifier,navController)
                    }
                    composable("scheduletradesman") {
                        ScheduleTradesman(modifier = Modifier,navController)
                    }

                }
            }
        }
    }
}

