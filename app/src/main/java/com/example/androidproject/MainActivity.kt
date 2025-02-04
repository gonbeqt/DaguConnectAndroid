package com.example.androidproject

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidproject.ui.theme.AndroidProjectTheme
import com.example.androidproject.ui.theme.views.ClientPov.AboutUs
import com.example.androidproject.ui.theme.views.Feedback
import com.example.androidproject.ui.theme.views.LandingPageScreen
import com.example.androidproject.ui.theme.views.LogInScreen
import com.example.androidproject.ui.theme.views.SignUpScreen
import com.example.androidproject.ui.theme.views.Tradesman
import com.example.androidproject.ui.theme.views.ClientPov.Categories.ACRepair
import com.example.androidproject.ui.theme.views.ClientPov.AllTradesman
import com.example.androidproject.ui.theme.views.ClientPov.BookNow
import com.example.androidproject.ui.theme.views.ClientPov.BookingDetails
import com.example.androidproject.ui.theme.views.ClientPov.Navigation.BookingsScreen
import com.example.androidproject.ui.theme.views.ClientPov.CancellationDetails
import com.example.androidproject.ui.theme.views.ClientPov.Categories.Carpentry
import com.example.androidproject.ui.theme.views.ClientPov.Categories.Cleaning
import com.example.androidproject.ui.theme.views.ClientPov.ConfirmBook
import com.example.androidproject.ui.theme.views.ClientPov.Categories.Electrician
import com.example.androidproject.ui.theme.views.ClientPov.Categories.Masonry
import com.example.androidproject.ui.theme.views.ClientPov.Categories.Mechanics
import com.example.androidproject.ui.theme.views.ClientPov.Categories.Painting
import com.example.androidproject.ui.theme.views.ClientPov.Navigation.MessageScreen
import com.example.androidproject.ui.theme.views.ClientPov.Categories.Plumbing
import com.example.androidproject.ui.theme.views.ClientPov.Categories.Roofing
import com.example.androidproject.ui.theme.views.ClientPov.Categories.Welding
import com.example.androidproject.ui.theme.views.ClientPov.ChangePassword
import com.example.androidproject.ui.theme.views.ClientPov.EmailVerification
import com.example.androidproject.ui.theme.views.ClientPov.ReportProblem
import com.example.androidproject.ui.theme.views.ClientPov.RateAndReviews
import com.example.androidproject.ui.theme.views.TradesmanPov.BookingsTradesman
import com.example.androidproject.ui.theme.views.TradesmanPov.BookmarkedTradesman
import com.example.androidproject.ui.theme.views.TradesmanPov.HomeTradesman
import com.example.androidproject.ui.theme.views.TradesmanPov.ProfileTradesman
import com.example.androidproject.ui.theme.views.TradesmanPov.ScheduleTradesman
import com.example.androidproject.ui.theme.views.TradesmanPov.TradesmanApply

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val isShown = sharedPreferences.getBoolean("isShown", false)
        val startDestination = if (isShown) "login" else "landing_page"
        val trade = Tradesman(R.drawable.pfp, "Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark)
        val feedback = Feedback(R.drawable.pfp, "Ezekiel", 4)
        setContent {
            AndroidProjectTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main_screen") {
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
                    composable("masonry"){
                        Masonry(navController)
                    }
                    composable("cleaning") {
                        Cleaning(navController)
                    }
                    composable("mechanics"){
                        Mechanics(navController)
                    }
                    composable("painting"){
                        Painting(navController)
                    }
                    composable("roofing"){
                        Roofing(navController)
                    }
                    composable("welding"){
                        Welding(navController)
                    }
                    composable("alltradesman"){
                        AllTradesman(navController)
                    }
                    composable("emailverification"){
                        EmailVerification(navController)
                    }
                    composable("changepassword"){
                        ChangePassword(navController)
                    }
                    composable("aboutus"){
                        AboutUs(navController)
                    }
                    composable("reportproblem"){
                        ReportProblem(navController)
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
                    composable("profiletradesman") {
                        ProfileTradesman(modifier = Modifier,navController)
                    }


                }
            }
        }
    }
}

