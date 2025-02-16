package com.example.androidproject

import LogoutViewModel
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.Notification
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.RetrofitInstance
import com.example.androidproject.data.preferences.TokenManager
import com.example.androidproject.view.ClientPov.AboutUs
import com.example.androidproject.view.ClientPov.AllTradesman
import com.example.androidproject.view.ClientPov.Categories.ACRepair
import com.example.androidproject.view.ClientPov.Categories.Carpentry
import com.example.androidproject.view.ClientPov.Categories.Cleaning
import com.example.androidproject.view.ClientPov.Categories.Electrician
import com.example.androidproject.view.ClientPov.Categories.Masonry
import com.example.androidproject.view.ClientPov.Categories.Mechanics
import com.example.androidproject.view.ClientPov.Categories.Painting
import com.example.androidproject.view.ClientPov.Categories.Plumbing
import com.example.androidproject.view.ClientPov.Categories.Roofing
import com.example.androidproject.view.ClientPov.Categories.Welding
import com.example.androidproject.view.ClientPov.ChangePassword
import com.example.androidproject.view.ClientPov.EmailVerification
import com.example.androidproject.view.ClientPov.ReportProblem
import com.example.androidproject.view.Feedback
import com.example.androidproject.view.LandingPage2
import com.example.androidproject.view.LandingPageScreen
import com.example.androidproject.view.LogInScreen
import com.example.androidproject.view.SignUpScreen
import com.example.androidproject.view.Tradesman
import com.example.androidproject.view.pages.AcceptNow
import com.example.androidproject.view.pages.AccountSettings
import com.example.androidproject.view.pages.BookNow
import com.example.androidproject.view.pages.BookingDetails
import com.example.androidproject.view.pages.BookingsScreen
import com.example.androidproject.view.pages.CancelNow
import com.example.androidproject.view.pages.CancelledDetails
import com.example.androidproject.view.pages.ConfirmBook
import com.example.androidproject.view.pages.MessageScreen
import com.example.androidproject.view.pages.NotificationScreen
import com.example.androidproject.view.pages.RateAndReviews
import com.example.androidproject.view.pages2.BookingsTradesman
import com.example.androidproject.view.pages2.BookmarkedTradesman
import com.example.androidproject.view.pages2.HomeTradesman
import com.example.androidproject.view.pages2.ProfileTradesman
import com.example.androidproject.view.pages2.ScheduleTradesman
import com.example.androidproject.view.pages2.TradesmanApply
import com.example.androidproject.view.theme.AndroidProjectTheme
import com.example.androidproject.viewmodel.LoginViewModel
import com.example.androidproject.viewmodel.RegisterViewModel
import com.example.androidproject.viewmodel.Resumes.GetResumesViewModel
import com.example.androidproject.viewmodel.Resumes.ViewResumeViewModel
import com.example.androidproject.viewmodel.bookings.GetClientBookingViewModel
import com.example.androidproject.viewmodel.chats.GetChatViewModel
import com.example.androidproject.viewmodel.factories.LoginViewModelFactory
import com.example.androidproject.viewmodel.factories.LogoutViewModelFactory
import com.example.androidproject.viewmodel.factories.RegisterViewModelFactory
import com.example.androidproject.viewmodel.factories.bookings.BookTradesmanViewModelFactory
import com.example.androidproject.viewmodel.factories.bookings.GetClientBookingViewModelFactory
import com.example.androidproject.viewmodel.factories.bookings.ViewClientBookingViewModelFactory
import com.example.androidproject.viewmodel.factories.chats.GetChatViewModelFactory
import com.example.androidproject.viewmodel.factories.jobs.GetJobsViewModelFactory
import com.example.androidproject.viewmodel.factories.jobs.PostJobViewModelFactory
import com.example.androidproject.viewmodel.factories.jobs.ViewJobViewModelFactory
import com.example.androidproject.viewmodel.factories.resumes.GetResumesViewModelFactory
import com.example.androidproject.viewmodel.factories.resumes.ViewResumeViewModelFactory
import com.example.androidproject.viewmodel.jobs.GetJobsViewModel
import com.example.androidproject.viewmodel.jobs.PostJobViewModel
import com.example.androidproject.viewmodel.jobs.ViewJobViewModel

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


        val getClientsBookingVMFactory = GetClientBookingViewModelFactory(apiService,this)
        val getClientBookingViewModel = ViewModelProvider(this,getClientsBookingVMFactory)[GetClientBookingViewModel::class.java]

        val viewClientBookingVMFactory = ViewClientBookingViewModelFactory(apiService,this)
        val viewClientBookingViewModel = ViewModelProvider(this, viewClientBookingVMFactory)[ViewClientBookingViewModel::class.java]

        val getResumesVMFactory = GetResumesViewModelFactory(apiService,this)
        val getResumesViewModel = ViewModelProvider(this, getResumesVMFactory)[GetResumesViewModel::class.java]

        val viewResumesVMFactory = ViewResumeViewModelFactory(apiService,this)
        val viewResumeViewModel = ViewModelProvider(this, viewResumesVMFactory)[ViewResumeViewModel::class.java]

        val logoutVMFactory = LogoutViewModelFactory(apiService)
        val logoutViewModel = ViewModelProvider(this, logoutVMFactory)[LogoutViewModel::class.java]

        val registerVMFactory = RegisterViewModelFactory(apiService)
        val registerViewModel = ViewModelProvider(this, registerVMFactory)[RegisterViewModel::class.java]

        val viewModelFactory = LoginViewModelFactory(apiService, this)
        val loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        val getJobsViewModelFactory = GetJobsViewModelFactory(apiService, this)
        val getJobsViewModel = ViewModelProvider(this, getJobsViewModelFactory)[GetJobsViewModel::class.java]

        val viewJobViewModelFactory = ViewJobViewModelFactory(apiService, this)
        val viewJobViewModel = ViewModelProvider(this, viewJobViewModelFactory)[ViewJobViewModel::class.java]

        val getChatsViewModelFactory = GetChatViewModelFactory(apiService, this)
        val getChatsViewModel = ViewModelProvider(this, getChatsViewModelFactory)[GetChatViewModel::class.java]

        val bookTradesmanVMFactory = BookTradesmanViewModelFactory(apiService, this)
        val bookingTradesmanViewModel = ViewModelProvider(this, bookTradesmanVMFactory)[BooktradesmanViewModel::class.java]

        val postJobViewModelFactory = PostJobViewModelFactory(apiService, this)
        val postJobViewModel = ViewModelProvider(this, postJobViewModelFactory)[PostJobViewModel::class.java]


        setContent {
            AndroidProjectTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main_screen" ) {
                    composable("landing_page") {
                        LandingPageScreen(navController)
                    }
                    composable("landingpage2") {
                        LandingPage2(navController)
                    }
                    composable("signup") {
                        SignUpScreen(navController,registerViewModel)
                    }
                    composable("login") {
                        LogInScreen(navController,loginViewModel)

                    }
                    composable("main_screen"){
                        MainScreen(navController,logoutViewModel,getClientBookingViewModel,getResumesViewModel, postJobViewModel)
                    }
                    composable("message_screen") {
                        MessageScreen(modifier=Modifier, navController, getChatsViewModel)
                    }

                    composable("booknow/{resumeId}") { backStackEntry ->
                        val resumeId = backStackEntry.arguments?.getString("resumeId")?: ""
                        Log.d("rateid",resumeId)
                        BookNow(viewResumeViewModel, navController,resumeId)
                    }
                    composable("confirmbook/{resumeId}/{tradesmanId}") {backStackEntry ->
                        val resumeId = backStackEntry.arguments?.getString("resumeId")?: ""
                        val tradesmanId = backStackEntry.arguments?.getString("tradesmanId")?:""
                        ConfirmBook(viewResumeViewModel,navController,resumeId,tradesmanId,bookingTradesmanViewModel)
                    }
                    composable("bookingdetails") {
                        BookingDetails(trade,navController)
                    }
                    composable("cancelleddetails") {
                        CancelledDetails(trade,navController)
                    }
                    composable("cancelnow"){
                        CancelNow(trade,navController)
                    }
                    composable("acceptnow"){
                        AcceptNow(trade,navController)
                    }
                    composable("booking") {
                        BookingsScreen(modifier = Modifier,navController,getClientBookingViewModel)
                    }
                    composable("rateandreviews/{resumeId}") { backStackEntry ->
                        val resumeId = backStackEntry.arguments?.getString("resumeId")?: ""
                        RateAndReviews(viewClientBookingViewModel,navController,resumeId)
                    }

                    composable("acrepair"){
                        ACRepair(navController,getResumesViewModel)
                    }
                    composable("plumbing") {
                        Plumbing(navController,getResumesViewModel)
                    }
                    composable("carpentry") {
                        Carpentry(navController,getResumesViewModel)
                    }
                    composable("electrician") {
                        Electrician(navController,getResumesViewModel)
                    }
                    composable("masonry"){
                        Masonry(navController,getResumesViewModel)
                    }
                    composable("cleaning") {
                        Cleaning(navController,getResumesViewModel)
                    }
                    composable("mechanics"){
                        Mechanics(navController,getResumesViewModel)
                    }
                    composable("painting"){
                        Painting(navController,getResumesViewModel)
                    }
                    composable("roofing"){
                        Roofing(navController,getResumesViewModel)
                    }
                    composable("welding"){
                        Welding(navController,getResumesViewModel)
                    }
                    composable("alltradesman"){
                        AllTradesman(navController,getResumesViewModel)
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
                    composable("notification"){
                        NotificationScreen(navController)
                    }
                    composable("accountsettings"){
                        AccountSettings(navController)
                    }


                    //Tradesman Routes
                    composable("hometradesman") {
                        HomeTradesman(modifier = Modifier,navController, getJobsViewModel)
                    }
                    composable("tradesmanapply/{jobId}") { backStackEntry ->
                        val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
                        Log.e("Job ID" , jobId)
                        TradesmanApply(jobId, navController, viewJobViewModel)
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

