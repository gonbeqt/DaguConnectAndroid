package com.example.androidproject

import LogoutViewModel
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.RetrofitInstance
import com.example.androidproject.data.preferences.AccountManager
import com.example.androidproject.data.preferences.TokenManager
import com.example.androidproject.model.client.GetClientsBooking
import com.example.androidproject.view.ClientPov.AboutUs
import com.example.androidproject.view.pages.AllTradesman
import com.example.androidproject.view.pages.Categories.ACRepair
import com.example.androidproject.view.pages.Categories.Cleaning
import com.example.androidproject.view.pages.Categories.Electrician
import com.example.androidproject.view.pages.Categories.Masonry
import com.example.androidproject.view.pages.Categories.Mechanics
import com.example.androidproject.view.pages.Categories.Painting
import com.example.androidproject.view.pages.Categories.Plumbing
import com.example.androidproject.view.pages.Categories.Roofing
import com.example.androidproject.view.pages.Categories.Welding
import com.example.androidproject.view.pages.ChangePassword
import com.example.androidproject.view.pages.EmailVerification
import com.example.androidproject.view.pages.ReportProblem
import com.example.androidproject.view.Feedback
import com.example.androidproject.view.LandingPage2
import com.example.androidproject.view.LandingPageScreen
import com.example.androidproject.view.LogInScreen
import com.example.androidproject.view.SignUpScreen
import com.example.androidproject.view.Tradesman
import com.example.androidproject.view.pages.AccountSettings
import com.example.androidproject.view.pages.BookNow
import com.example.androidproject.view.pages.BookingDetails
import com.example.androidproject.view.pages.BookingsScreen
import com.example.androidproject.view.pages.CancelNow
import com.example.androidproject.view.pages.CancelledDetails
import com.example.androidproject.view.pages.CancelledItem
import com.example.androidproject.view.pages.CancelledJobApplicationDetails
import com.example.androidproject.view.pages.Categories.Carpentry
import com.example.androidproject.view.pages.ConfirmBook
import com.example.androidproject.view.pages.MessageScreen
import com.example.androidproject.view.pages.NotificationScreen
import com.example.androidproject.view.pages.RateAndReviews
import com.example.androidproject.view.pages2.AvailabilityStatus
import com.example.androidproject.view.pages2.BookingsTradesman
import com.example.androidproject.view.pages2.BookmarkedTradesman
import com.example.androidproject.view.pages2.CancelTradesmanNow
import com.example.androidproject.view.pages2.HiringDetails
import com.example.androidproject.view.pages2.HomeTradesman
import com.example.androidproject.view.pages2.ManageProfile
import com.example.androidproject.view.pages2.MyJobApplicationDetails
import com.example.androidproject.view.pages2.ProfileTradesman
import com.example.androidproject.view.pages2.ProfileVerification
import com.example.androidproject.view.pages2.ScheduleTradesman
import com.example.androidproject.view.pages2.TradesmanApply
import com.example.androidproject.view.theme.AndroidProjectTheme
import com.example.androidproject.viewmodel.LoginViewModel
import com.example.androidproject.viewmodel.RegisterViewModel
import com.example.androidproject.viewmodel.Resumes.GetResumesViewModel
import com.example.androidproject.viewmodel.Resumes.ViewResumeViewModel
import com.example.androidproject.viewmodel.Tradesman_Profile.ViewTradesmanProfileViewModel
import com.example.androidproject.viewmodel.bookings.BooktradesmanViewModel
import com.example.androidproject.viewmodel.bookings.GetClientBookingViewModel
import com.example.androidproject.viewmodel.bookings.UpdateWorkStatusViewModel
import com.example.androidproject.viewmodel.bookings.ViewClientBookingViewModel
import com.example.androidproject.viewmodel.chats.GetChatViewModel
import com.example.androidproject.viewmodel.client_profile.GetClientProfileViewModel
import com.example.androidproject.viewmodel.factories.LoginViewModelFactory
import com.example.androidproject.viewmodel.factories.LogoutViewModelFactory
import com.example.androidproject.viewmodel.factories.RegisterViewModelFactory
import com.example.androidproject.viewmodel.factories.Tradesman_Profile.ViewTradesmaProfileViewModelFactory
import com.example.androidproject.viewmodel.factories.bookings.BookTradesmanViewModelFactory
import com.example.androidproject.viewmodel.factories.bookings.GetClientBookingViewModelFactory
import com.example.androidproject.viewmodel.factories.bookings.UpdateWorkStatusViewModelFactory
import com.example.androidproject.viewmodel.factories.bookings.ViewClientBookingViewModelFactory
import com.example.androidproject.viewmodel.factories.chats.GetChatViewModelFactory
import com.example.androidproject.viewmodel.factories.client_profile.GetClientProfileViewModelFactory
import com.example.androidproject.viewmodel.factories.job_application.PostJobApplicationViewModelFactory
import com.example.androidproject.viewmodel.factories.job_application.tradesman.GetMyJobApplicationViewModelFactory
import com.example.androidproject.viewmodel.factories.jobs.GetJobsViewModelFactory
import com.example.androidproject.viewmodel.factories.jobs.GetMyJobsViewModelFactory
import com.example.androidproject.viewmodel.factories.jobs.GetRecentJobsViewModelFactory
import com.example.androidproject.viewmodel.factories.jobs.PostJobViewModelFactory
import com.example.androidproject.viewmodel.factories.jobs.ViewJobViewModelFactory
import com.example.androidproject.viewmodel.factories.ratings.RateTradesmanViewModelFactory
import com.example.androidproject.viewmodel.factories.ratings.ViewRatingsViewModelFactory
import com.example.androidproject.viewmodel.factories.report.ReportViewModelFactory
import com.example.androidproject.viewmodel.factories.resumes.GetResumesViewModelFactory
import com.example.androidproject.viewmodel.factories.resumes.ViewResumeViewModelFactory
import com.example.androidproject.viewmodel.job_application.PostJobApplicationViewModel
import com.example.androidproject.viewmodel.job_application.tradesman.GetMyJobApplicationViewModel
import com.example.androidproject.viewmodel.jobs.GetJobsViewModel
import com.example.androidproject.viewmodel.jobs.GetMyJobsViewModel
import com.example.androidproject.viewmodel.jobs.GetRecentJobsViewModel
import com.example.androidproject.viewmodel.jobs.PostJobViewModel
import com.example.androidproject.viewmodel.jobs.ViewJobViewModel
import com.example.androidproject.viewmodel.ratings.RateTradesmanViewModel
import com.example.androidproject.viewmodel.ratings.ViewRatingsViewModel
import com.example.androidproject.viewmodel.report.ReportViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val isShown = sharedPreferences.getBoolean("isShown", false)
        // Initialize TokenManager
        TokenManager.init(this)
        AccountManager.init(this)

        // Determine the start destination based on token and user role
        val startDestination = when {
            !isShown -> "landing_page"
            TokenManager.isLoggedIn() -> {
                val role = AccountManager.getAccount()?.isClient
                if (role == true) "main_screen" else "main_screen"
            }
            else -> "login"
        }
        val trade = Tradesman(R.drawable.pfp, "Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark)
        TokenManager.init(this)

        val apiService = RetrofitInstance.create(ApiService::class.java)

        val reportVMFactory = ReportViewModelFactory(apiService,this)
        val reportViewModel = ViewModelProvider(this, reportVMFactory)[ReportViewModel::class.java]

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

        val viewRatingViewModelFactory = ViewRatingsViewModelFactory(apiService, this)
        val viewRatingsViewModel = ViewModelProvider(this, viewRatingViewModelFactory)[ViewRatingsViewModel::class.java]

        val bookTradesmanVMFactory = BookTradesmanViewModelFactory(apiService, this)
        val bookingTradesmanViewModel = ViewModelProvider(this, bookTradesmanVMFactory)[BooktradesmanViewModel::class.java]

        val postJobViewModelFactory = PostJobViewModelFactory(apiService, this)
        val postJobsViewModel = ViewModelProvider(this, postJobViewModelFactory)[PostJobViewModel::class.java]

        val getMyJobsViewModelFactory = GetMyJobsViewModelFactory(apiService, this)
        val getMyJobsViewModel = ViewModelProvider(this, getMyJobsViewModelFactory)[GetMyJobsViewModel::class.java]

        val getClientProfileViewModelFactory = GetClientProfileViewModelFactory(apiService, this)
        val getClientProfileViewModel = ViewModelProvider(this, getClientProfileViewModelFactory)[GetClientProfileViewModel::class.java]

        val UpdateWorkStatusVMFactory = UpdateWorkStatusViewModelFactory(apiService, this)
        val updateWorkStatusViewModel = ViewModelProvider(this, UpdateWorkStatusVMFactory)[UpdateWorkStatusViewModel::class.java]

        val ratetradesManVMfactory = RateTradesmanViewModelFactory(apiService, this)
        val rateTradesmanViewModel = ViewModelProvider(this, ratetradesManVMfactory)[RateTradesmanViewModel::class.java]

        val postJobApplicationViewModelFactory = PostJobApplicationViewModelFactory(apiService, this)
        val postJobApplicationViewModel = ViewModelProvider(this, postJobApplicationViewModelFactory)[PostJobApplicationViewModel::class.java]

        val getRecentJobsViewModelFactory = GetRecentJobsViewModelFactory(apiService, this)
        val getRecentJobsViewModel = ViewModelProvider(this, getRecentJobsViewModelFactory)[GetRecentJobsViewModel::class.java]

        val viewTradesmanProfileVMFactory = ViewTradesmaProfileViewModelFactory(apiService, this)
        val viewTradesmanProfileViewModel = ViewModelProvider(this, viewTradesmanProfileVMFactory)[ViewTradesmanProfileViewModel::class.java]

        val getMyJobApplicationViewModelFactory = GetMyJobApplicationViewModelFactory(apiService, this)
        val getMyJobApplicationViewModel = ViewModelProvider(this, getMyJobApplicationViewModelFactory)[GetMyJobApplicationViewModel::class.java]

        setContent {
            AndroidProjectTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = startDestination) {
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
                        MainScreen(
                            navController,
                            logoutViewModel,
                            getClientBookingViewModel,
                            getResumesViewModel,
                            modifier = Modifier,
                            getChatsViewModel,
                            reportViewModel,
                            postJobsViewModel,
                            getMyJobsViewModel,
                            getClientProfileViewModel,
                            updateWorkStatusViewModel,
                            getRecentJobsViewModel,
                            viewTradesmanProfileViewModel,
                            getMyJobApplicationViewModel)
                    }
                    composable("message_screen") {
                        MessageScreen(modifier=Modifier, navController, getChatsViewModel)
                    }

                    composable("booknow/{resumeId}") { backStackEntry ->
                        val resumeId = backStackEntry.arguments?.getString("resumeId")?: ""
                        Log.d("rateid",resumeId)
                        BookNow(viewResumeViewModel, navController,resumeId,viewRatingsViewModel)
                    }
                    composable("confirmbook/{resumeId}/{tradesmanId}") {backStackEntry ->
                        val resumeId = backStackEntry.arguments?.getString("resumeId")?: ""
                        val tradesmanId = backStackEntry.arguments?.getString("tradesmanId")?:""
                        ConfirmBook(viewResumeViewModel,navController,resumeId,tradesmanId,bookingTradesmanViewModel)
                    }
                    composable("bookingdetails/{resumeId}") {backStackEntry ->
                        val resumeId = backStackEntry.arguments?.getString("resumeId")?: ""
                        BookingDetails(viewClientBookingViewModel,navController,resumeId)
                    }
                    composable("cancelleddetails") {
                        CancelledDetails(trade,navController)
                    }
                    composable("cancelnow/{resumeId}/{bookingstatus}/{bookingId}"){ backStackEntry ->
                        val resumeId = backStackEntry.arguments?.getString("resumeId")?: ""
                        val bookingStatus = backStackEntry.arguments?.getString("bookingstatus")?: ""
                        val bookingId = backStackEntry.arguments?.getString("bookingId")?: ""
                        CancelNow(updateWorkStatusViewModel,viewClientBookingViewModel,navController,resumeId,bookingStatus,bookingId)
                    }

                    composable("booking") {
                        BookingsScreen(modifier = Modifier,navController,getClientBookingViewModel,updateWorkStatusViewModel)
                    }
                    composable("rateandreviews/{resumeId}/{tradesmanId}") { backStackEntry ->
                        val resumeId = backStackEntry.arguments?.getString("resumeId")?: ""
                        val bookingId = backStackEntry.arguments?.getString("tradesmanId")?: ""
                        RateAndReviews(rateTradesmanViewModel,viewClientBookingViewModel,navController,resumeId,bookingId)
                    }

                    composable("acrepair"){
                        ACRepair(navController,getResumesViewModel,reportViewModel)
                    }
                    composable("plumbing") {
                        Plumbing(navController,getResumesViewModel,reportViewModel)
                    }
                    composable("carpentry") {
                        Carpentry(navController,getResumesViewModel,reportViewModel)
                    }
                    composable("electrician") {
                        Electrician(navController,getResumesViewModel,reportViewModel)
                    }
                    composable("masonry"){
                        Masonry(navController,getResumesViewModel,reportViewModel)
                    }
                    composable("cleaning") {
                        Cleaning(navController,getResumesViewModel,reportViewModel)
                    }
                    composable("mechanics"){
                        Mechanics(navController,getResumesViewModel,reportViewModel)
                    }
                    composable("painting"){
                        Painting(navController,getResumesViewModel,reportViewModel)
                    }
                    composable("roofing"){
                        Roofing(navController,getResumesViewModel,reportViewModel)
                    }
                    composable("welding"){
                        Welding(navController,getResumesViewModel,reportViewModel)
                    }
                    composable("alltradesman"){
                        AllTradesman(navController,getResumesViewModel,reportViewModel)
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
                    }                    //Pang CANCEL


                    //CANCELLED DETAILS
                    composable("canceljobapplicationsdetails") {
                        CancelledJobApplicationDetails(navController)
                    }

                    //Tradesman Routes
                    composable("hometradesman") {
                        HomeTradesman(modifier = Modifier,navController, getJobsViewModel, getRecentJobsViewModel)
                    }
                    composable("tradesmanapply/{jobId}") { backStackEntry ->
                        val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
                        Log.e("Job ID" , jobId)
                        TradesmanApply(jobId, navController, viewJobViewModel)
                    }
                    composable("hiringdetails/{jobId}") { backStackEntry ->
                        val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
                        HiringDetails(jobId, modifier = Modifier, navController, postJobApplicationViewModel)
                    }
                    composable("bookingstradesman") {
                        BookingsTradesman(modifier = Modifier,navController, getMyJobApplicationViewModel)
                    }
                    composable("bookmarkedtradesman") {
                        BookmarkedTradesman(modifier = Modifier,navController)
                    }
                    composable("scheduletradesman") {
                        ScheduleTradesman(modifier = Modifier,navController)
                    }
                    composable("profiletradesman") {
                         ProfileTradesman(modifier = Modifier, navController,logoutViewModel,viewTradesmanProfileViewModel)
                    }
                    composable("manageprofile") {
                        ManageProfile(modifier = Modifier, navController)
                    }
                    composable("availabilitystatus") {
                        AvailabilityStatus(modifier = Modifier, navController)
                    }
                    composable("profileverification") {
                        ProfileVerification(modifier = Modifier, navController)
                    }
                    composable("canceltradesmannow") {
                        CancelTradesmanNow(navController)
                    }
                    composable("myjobapplicationdetails") {
                        MyJobApplicationDetails(navController)
                    }

                }
            }
        }
    }
}


