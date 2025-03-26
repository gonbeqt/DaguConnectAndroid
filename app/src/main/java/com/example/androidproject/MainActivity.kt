package com.example.androidproject

import LogoutViewModel
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.lifecycle.ViewModelProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.RetrofitInstance
import com.example.androidproject.data.WebSocketService
import com.example.androidproject.data.preferences.AccountManager
import com.example.androidproject.data.preferences.NotificationSettingManager
import com.example.androidproject.data.preferences.TokenManager
import com.example.androidproject.view.ClientPov.AboutUs
import com.example.androidproject.view.ClientPov.ClientHelp
import com.example.androidproject.view.client.AllTradesman
import com.example.androidproject.view.client.Categories.ACRepair
import com.example.androidproject.view.client.Categories.Cleaning
import com.example.androidproject.view.client.Categories.Electrician
import com.example.androidproject.view.client.Categories.Masonry
import com.example.androidproject.view.client.Categories.Mechanics
import com.example.androidproject.view.client.Categories.Painting
import com.example.androidproject.view.client.Categories.Plumbing
import com.example.androidproject.view.client.Categories.Roofing
import com.example.androidproject.view.client.Categories.Welding
import com.example.androidproject.view.client.ChangePassword
import com.example.androidproject.view.client.ReportProblem
import com.example.androidproject.view.LandingPage2
import com.example.androidproject.view.LandingPageScreen
import com.example.androidproject.view.LogInScreen
import com.example.androidproject.view.ResetPassword
import com.example.androidproject.view.SignUpScreen
import com.example.androidproject.view.client.AccountSettings
import com.example.androidproject.view.client.ApplicantDeclinationDetails
import com.example.androidproject.view.client.ApplicantDetailJobSummary
import com.example.androidproject.view.client.ApplicantDetails
import com.example.androidproject.view.client.BookNow
import com.example.androidproject.view.client.BookingDetails
import com.example.androidproject.view.client.BookingsScreen
import com.example.androidproject.view.client.CancelNow

import com.example.androidproject.view.client.Categories.Carpentry

import com.example.androidproject.view.client.ClientDeclinationDetails
import com.example.androidproject.view.client.ClientDetails
import com.example.androidproject.view.client.ConfirmBook
import com.example.androidproject.view.client.Message
import com.example.androidproject.view.client.MessageScreen
import com.example.androidproject.view.client.MessagingScreen
import com.example.androidproject.view.client.NotificationScreen
import com.example.androidproject.view.client.RateAndReviews
import com.example.androidproject.view.tradesman.AvailabilityStatus
import com.example.androidproject.view.tradesman.BookingsTradesman
import com.example.androidproject.view.tradesman.CancelTradesmanNow
import com.example.androidproject.view.tradesman.HiringDetails
import com.example.androidproject.view.tradesman.HomeTradesman
import com.example.androidproject.view.tradesman.ManageProfile
import com.example.androidproject.view.tradesman.MyJobApplicationDetails
import com.example.androidproject.view.tradesman.ProfileTradesman
import com.example.androidproject.view.tradesman.ProfileVerification
import com.example.androidproject.view.tradesman.TradesmanApply
import com.example.androidproject.view.theme.AndroidProjectTheme
import com.example.androidproject.view.tradesman.AccountSettingsTradesman

import com.example.androidproject.view.tradesman.TradesmanApplication
import com.example.androidproject.view.tradesman.TradesmanApplicationCancelDetails

import com.example.androidproject.view.tradesman.TradesmanDeclinationDetails
import com.example.androidproject.view.tradesman.TradesmanDetails
import com.example.androidproject.viewmodel.ChangePasswordViewModel
import com.example.androidproject.viewmodel.ForgotPassViewModel
import com.example.androidproject.viewmodel.LoginViewModel
import com.example.androidproject.viewmodel.Tradesman_Profile.UpdateTradesmanProfileViewModel
import com.example.androidproject.viewmodel.RegisterViewModel
import com.example.androidproject.viewmodel.ReportConcernViewModel
import com.example.androidproject.viewmodel.ResetPassViewModel
import com.example.androidproject.viewmodel.Resumes.GetResumesViewModel
import com.example.androidproject.viewmodel.Resumes.SubmitResumeViewModel
import com.example.androidproject.viewmodel.Resumes.ViewResumeViewModel
import com.example.androidproject.viewmodel.Tradesman_Profile.UpdateTradesmanActiveStatusViewModel
import com.example.androidproject.viewmodel.Tradesman_Profile.UpdateTradesmanDetailViewModel
import com.example.androidproject.viewmodel.Tradesman_Profile.ViewTradesmanProfileViewModel
import com.example.androidproject.viewmodel.bookings.BooktradesmanViewModel
import com.example.androidproject.viewmodel.bookings.GetClientBookingViewModel
import com.example.androidproject.viewmodel.bookings.GetTradesmanBookingViewModel
import com.example.androidproject.viewmodel.bookings.UpdateBookingClientViewModel
import com.example.androidproject.viewmodel.bookings.UpdateBookingTradesmanViewModel
import com.example.androidproject.viewmodel.bookings.ViewClientBookingViewModel
import com.example.androidproject.viewmodel.chats.GetChatViewModel
import com.example.androidproject.viewmodel.client_profile.GetClientProfileViewModel
import com.example.androidproject.viewmodel.factories.ChangePasswordViewModelFactory
import com.example.androidproject.viewmodel.factories.ForgotPassViewModelFactory
import com.example.androidproject.viewmodel.client_profile.UpdateClientProfileAddressViewModel
import com.example.androidproject.viewmodel.client_profile.UpdateClientProfilePictureViewModel
import com.example.androidproject.viewmodel.factories.LoginViewModelFactory
import com.example.androidproject.viewmodel.factories.LogoutViewModelFactory
import com.example.androidproject.viewmodel.factories.Tradesman_Profile.UpdateTradesmanProfileViewModelFactory
import com.example.androidproject.viewmodel.factories.RegisterViewModelFactory
import com.example.androidproject.viewmodel.factories.ReportConcernViewModelFactory
import com.example.androidproject.viewmodel.factories.ResetPassViewModelFactory
import com.example.androidproject.viewmodel.factories.Tradesman_Profile.UpdateTradesmanActiveStatusViewModelFactory
import com.example.androidproject.viewmodel.factories.Tradesman_Profile.UpdateTradesmanDetailViewModelFactory
import com.example.androidproject.viewmodel.factories.Tradesman_Profile.ViewTradesmaProfileViewModelFactory
import com.example.androidproject.viewmodel.factories.bookings.BookTradesmanViewModelFactory
import com.example.androidproject.viewmodel.factories.bookings.GetClientBookingViewModelFactory
import com.example.androidproject.viewmodel.factories.bookings.GetTradesmanViewModelFactory
import com.example.androidproject.viewmodel.factories.bookings.UpdateBookingClientViewModelFactory
import com.example.androidproject.viewmodel.factories.bookings.UpdateWorkStatusViewModelFactory
import com.example.androidproject.viewmodel.factories.bookings.ViewClientBookingViewModelFactory
import com.example.androidproject.viewmodel.factories.chats.GetChatViewModelFactory
import com.example.androidproject.viewmodel.factories.client_profile.GetClientProfileViewModelFactory
import com.example.androidproject.viewmodel.factories.client_profile.UpdateClientProfileAddressViewModelFactory
import com.example.androidproject.viewmodel.factories.client_profile.UpdateClientProfilePictureViewModelFactory
import com.example.androidproject.viewmodel.factories.job_application.PostJobApplicationViewModelFactory
import com.example.androidproject.viewmodel.factories.job_application.PutJobApplicationStatusViewModelFactory
import com.example.androidproject.viewmodel.factories.job_application.ViewJobApplicationViewModelFactory
import com.example.androidproject.viewmodel.factories.job_application.client.GetMyJobApplicantsViewModelFactory
import com.example.androidproject.viewmodel.factories.job_application.tradesman.GetMyJobApplicationViewModelFactory
import com.example.androidproject.viewmodel.factories.jobs.DeleteJobViewModelFactory
import com.example.androidproject.viewmodel.factories.jobs.GetClientPostedJobsViewModelFactory
import com.example.androidproject.viewmodel.factories.jobs.GetJobsViewModelFactory
import com.example.androidproject.viewmodel.factories.jobs.GetMyJobsViewModelFactory
import com.example.androidproject.viewmodel.factories.jobs.GetRecentJobsViewModelFactory
import com.example.androidproject.viewmodel.factories.jobs.PostJobViewModelFactory
import com.example.androidproject.viewmodel.factories.jobs.PutJobViewModelFactory
import com.example.androidproject.viewmodel.factories.jobs.ViewJobViewModelFactory
import com.example.androidproject.viewmodel.factories.messeges.GetMessageViewModelFactory
import com.example.androidproject.viewmodel.factories.notification.ClearNotificationViewModelFactory
import com.example.androidproject.viewmodel.factories.notification.GetNotificationViewModelFactory
import com.example.androidproject.viewmodel.factories.ratings.RateTradesmanViewModelFactory
import com.example.androidproject.viewmodel.factories.ratings.ViewRatingsViewModelFactory
import com.example.androidproject.viewmodel.factories.report.ReportClientViewModelFactory
import com.example.androidproject.viewmodel.factories.report.ReportTradesmanViewModelFactory
import com.example.androidproject.viewmodel.factories.resumes.GetResumesViewModelFactory
import com.example.androidproject.viewmodel.factories.resumes.SubmitResumeViewModelFactory
import com.example.androidproject.viewmodel.factories.resumes.ViewResumeViewModelFactory
import com.example.androidproject.viewmodel.job_application.PostJobApplicationViewModel
import com.example.androidproject.viewmodel.job_application.PutJobApplicationStatusViewModel
import com.example.androidproject.viewmodel.job_application.ViewJobApplicationViewModel
import com.example.androidproject.viewmodel.job_application.client.GetMyJobApplicantsViewModel
import com.example.androidproject.viewmodel.job_application.tradesman.GetMyJobApplicationViewModel
import com.example.androidproject.viewmodel.jobs.DeleteJobViewModel
import com.example.androidproject.viewmodel.jobs.GetClientPostedJobsViewModel
import com.example.androidproject.viewmodel.jobs.GetJobsViewModel
import com.example.androidproject.viewmodel.jobs.GetMyJobsViewModel
import com.example.androidproject.viewmodel.jobs.GetRecentJobsViewModel
import com.example.androidproject.viewmodel.jobs.PostJobViewModel
import com.example.androidproject.viewmodel.jobs.PutJobViewModel
import com.example.androidproject.viewmodel.jobs.ViewJobViewModel
import com.example.androidproject.viewmodel.messeges.GetMessagesViewModel
import com.example.androidproject.viewmodel.notifications.ClearNotificationViewModel
import com.example.androidproject.viewmodel.notifications.GetNotificationViewModel
import com.example.androidproject.viewmodel.ratings.RateTradesmanViewModel
import com.example.androidproject.viewmodel.ratings.ViewRatingsViewModel
import com.example.androidproject.viewmodel.report.ReportClientViewModel
import com.example.androidproject.viewmodel.report.ReportTradesmanViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val isShown = sharedPreferences.getBoolean("isShown", false)

        // Initialize managers
        TokenManager.init(this)
        AccountManager.init(this)
        NotificationSettingManager.init(this)

        // Start WebSocketService if user is logged in
        val userId = AccountManager.getAccount()?.id
        if (userId != null) {
            val intent = Intent(this, WebSocketService::class.java).apply {
                putExtra("userId", userId.toString())
            }
            startForegroundService(intent)
        }
        val notificationRoute = intent.getStringExtra("navigate_to")
        val startDestination = notificationRoute ?: when {
            !isShown -> "landing_page"
            TokenManager.isLoggedIn() -> "main_screen" // Default to main_screen for both roles
            else -> "login"
        }
        TokenManager.init(this)

        val apiService = RetrofitInstance.create(ApiService::class.java)

        val reportTradesmanVMFactory = ReportTradesmanViewModelFactory(apiService)
        val reportTradesmanViewModel = ViewModelProvider(this, reportTradesmanVMFactory)[ReportTradesmanViewModel::class.java]

        val reportClientVMFactory = ReportClientViewModelFactory(apiService)
        val reportClientViewModel = ViewModelProvider(this, reportClientVMFactory)[ReportClientViewModel::class.java]

        val getClientsBookingVMFactory = GetClientBookingViewModelFactory(apiService)
        val getClientBookingViewModel = ViewModelProvider(this,getClientsBookingVMFactory)[GetClientBookingViewModel::class.java]

        val viewClientBookingVMFactory = ViewClientBookingViewModelFactory(apiService)
        val viewClientBookingViewModel = ViewModelProvider(this, viewClientBookingVMFactory)[ViewClientBookingViewModel::class.java]

        val getResumesVMFactory = GetResumesViewModelFactory(apiService)
        val getResumesViewModel = ViewModelProvider(this, getResumesVMFactory)[GetResumesViewModel::class.java]

        val viewResumesVMFactory = ViewResumeViewModelFactory(apiService)
        val viewResumeViewModel = ViewModelProvider(this, viewResumesVMFactory)[ViewResumeViewModel::class.java]

        val logoutVMFactory = LogoutViewModelFactory(apiService)
        val logoutViewModel = ViewModelProvider(this, logoutVMFactory)[LogoutViewModel::class.java]

        val registerVMFactory = RegisterViewModelFactory(apiService)
        val registerViewModel = ViewModelProvider(this, registerVMFactory)[RegisterViewModel::class.java]

        val viewModelFactory = LoginViewModelFactory(apiService, this)
        val loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        val getJobsViewModelFactory = GetJobsViewModelFactory(apiService)
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

        //client will update the status if the booking finish or not
        val updateWorkStatusVMFactory = UpdateWorkStatusViewModelFactory(apiService, this)
        val updateBookingTradesmanViewModel = ViewModelProvider(this, updateWorkStatusVMFactory)[UpdateBookingTradesmanViewModel::class.java]

        // tradesman will update the status if the booking is accepted or not
        val updateClientWorkStatusVMFactory = UpdateBookingClientViewModelFactory(apiService)
        val updateBookingClientViewModel = ViewModelProvider(this, updateClientWorkStatusVMFactory)[UpdateBookingClientViewModel::class.java]

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

        val submitResumeVMFactory = SubmitResumeViewModelFactory(apiService, this)
        val submitResumeViewModel = ViewModelProvider(this, submitResumeVMFactory)[SubmitResumeViewModel::class.java]

        val putJobApplicationStatusViewModelFactory = PutJobApplicationStatusViewModelFactory(apiService, this)
        val putJobApplicationStatusViewModel = ViewModelProvider(this, putJobApplicationStatusViewModelFactory)[PutJobApplicationStatusViewModel::class.java]

        val getMyJobApplicantsViewModelFactory = GetMyJobApplicantsViewModelFactory(apiService, this)
        val getMyJobApplicantsViewModel = ViewModelProvider(this, getMyJobApplicantsViewModelFactory)[GetMyJobApplicantsViewModel::class.java]

        val viewJobApplicationViewModelFactory = ViewJobApplicationViewModelFactory(apiService)
        val viewJobApplicationViewModel = ViewModelProvider(this, viewJobApplicationViewModelFactory)[ViewJobApplicationViewModel::class.java]

        val getTradesmanBookingVMFactory = GetTradesmanViewModelFactory(apiService)
        val getTradesmanBookingViewModel = ViewModelProvider(this, getTradesmanBookingVMFactory)[GetTradesmanBookingViewModel::class.java]

        val forgotPassVMFactory = ForgotPassViewModelFactory(apiService)
        val forgotPassViewModel = ViewModelProvider(this, forgotPassVMFactory)[ForgotPassViewModel::class.java]

        val resetPassVMFactory = ResetPassViewModelFactory(apiService)
        val resetPassViewModel = ViewModelProvider(this, resetPassVMFactory)[ResetPassViewModel::class.java]

        val changePasswordVMFactor = ChangePasswordViewModelFactory(apiService)
        val changePasswordViewModel = ViewModelProvider(this, changePasswordVMFactor)[ChangePasswordViewModel::class.java]


        val putJobViewModelFactory = PutJobViewModelFactory(apiService)
        val putJobViewModel = ViewModelProvider(this, putJobViewModelFactory)[PutJobViewModel::class.java]

        val updateClientProfilePictureViewModelFactory = UpdateClientProfilePictureViewModelFactory(apiService)
        val updateClientProfilePictureViewModel = ViewModelProvider(this, updateClientProfilePictureViewModelFactory)[UpdateClientProfilePictureViewModel::class.java]

        val updateClientProfileAddressVMFactory = UpdateClientProfileAddressViewModelFactory(apiService)
        val updateClientProfileAddressViewModel = ViewModelProvider(this, updateClientProfileAddressVMFactory)[UpdateClientProfileAddressViewModel::class.java]

        val updateTradesmanVMFactory = UpdateTradesmanProfileViewModelFactory(apiService)
        val updateTradesmanProfileViewModel = ViewModelProvider(this, updateTradesmanVMFactory)[UpdateTradesmanProfileViewModel::class.java]

        val updateTradesmanActiveStatusVMFactory = UpdateTradesmanActiveStatusViewModelFactory(apiService)
        val updateTradesmanActiveStatusViewModel = ViewModelProvider(this, updateTradesmanActiveStatusVMFactory)[UpdateTradesmanActiveStatusViewModel::class.java]

        val getNotificationViewModelFactory = GetNotificationViewModelFactory(apiService)
        val getNotificationViewModel = ViewModelProvider(this, getNotificationViewModelFactory)[GetNotificationViewModel::class.java]

        val clearNotificationViewModelFactory = ClearNotificationViewModelFactory(apiService)
        val clearNotificationViewModel = ViewModelProvider(this, clearNotificationViewModelFactory)[ClearNotificationViewModel::class.java]

        val deleteJobViewModelFactory = DeleteJobViewModelFactory(apiService)
        val deleteJobViewModel = ViewModelProvider(this, deleteJobViewModelFactory)[DeleteJobViewModel::class.java]

        val reportConcernVMFactory = ReportConcernViewModelFactory(apiService)
        val reportConcernViewModel = ViewModelProvider(this, reportConcernVMFactory)[ReportConcernViewModel::class.java]

        val getClientPostedJobsVMFactory = GetClientPostedJobsViewModelFactory(apiService)
        val getClientPostedJobsViewModel = ViewModelProvider(this, getClientPostedJobsVMFactory)[GetClientPostedJobsViewModel::class.java]

        val updateTradesmanDetailVMFactory = UpdateTradesmanDetailViewModelFactory(apiService)
        val updateTradesmanDetailViewModel = ViewModelProvider(this, updateTradesmanDetailVMFactory)[UpdateTradesmanDetailViewModel::class.java]
        setContent {
            AndroidProjectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("landing_page") {
                            LandingPageScreen(navController)
                        }
                        composable("landingpage2") {
                            LandingPage2(navController)
                        }
                        composable("signup") {
                            SignUpScreen(navController, registerViewModel)
                        }
                        composable("login") {
                            LogInScreen(navController, loginViewModel, logoutViewModel)

                        }

                        composable("resetpassword") {
                            ResetPassword(
                                navController,
                                forgotPassViewModel,
                                resetPassViewModel)
                        }

                        composable(
                            route = "main_screen?selectedItem={selectedItem}&selectedTab={selectedTab}&selectedSection={selectedSection}",
                            arguments = listOf(
                                navArgument("selectedItem") {
                                    type = NavType.IntType
                                    defaultValue = 0
                                },
                                navArgument("selectedTab") {
                                    type = NavType.IntType
                                    defaultValue = 0
                                },
                                navArgument("selectedSection") {
                                    type = NavType.IntType
                                    defaultValue = 0
                                }
                            )
                        ) { backStackEntry ->
                            val selectedItem = backStackEntry.arguments?.getInt("selectedItem") ?: 0
                            val selectedTab = backStackEntry.arguments?.getInt("selectedTab") ?: 0
                            val selectedSection =
                                backStackEntry.arguments?.getInt("selectedSection") ?: 0

                            MainScreen(
                                navController,
                                logoutViewModel,
                                getClientBookingViewModel,
                                getResumesViewModel,
                                modifier = Modifier,
                                getChatsViewModel,
                                reportTradesmanViewModel,
                                postJobsViewModel,
                                getMyJobsViewModel,
                                getClientProfileViewModel,
                                updateBookingTradesmanViewModel,
                                getRecentJobsViewModel,
                                viewTradesmanProfileViewModel,
                                getMyJobApplicationViewModel,
                                putJobApplicationStatusViewModel,
                                getMyJobApplicantsViewModel,
                                viewJobApplicationViewModel,
                                getTradesmanBookingViewModel,
                                putJobViewModel,
                                updateBookingClientViewModel,
                                updateTradesmanProfileViewModel,
                                updateClientProfilePictureViewModel,
                                updateTradesmanActiveStatusViewModel,
                                reportClientViewModel,
                                viewRatingsViewModel,
                                deleteJobViewModel,
                                initialSelectedItem = selectedItem,
                                initialSelectedTab = selectedTab,
                                initialSelectedSection = selectedSection
                            )
                        }
                        composable("message_screen") {
                            MessageScreen(modifier = Modifier, navController, getChatsViewModel)
                        }
                        composable("booknow/{resumeId}") { backStackEntry ->
                            val resumeId = backStackEntry.arguments?.getString("resumeId") ?: ""
                            Log.d("rateid", resumeId)
                            BookNow(
                                viewResumeViewModel,
                                navController,
                                resumeId,
                                viewRatingsViewModel
                            )
                        }
                        composable("confirmbook/{resumeId}/{tradesmanId}") { backStackEntry ->
                            val resumeId = backStackEntry.arguments?.getString("resumeId") ?: ""
                            val tradesmanId =
                                backStackEntry.arguments?.getString("tradesmanId") ?: ""
                            ConfirmBook(
                                viewResumeViewModel,
                                navController,
                                resumeId,
                                tradesmanId,
                                bookingTradesmanViewModel
                            )
                        }
                        composable("bookingdetails/{resumeId}") { backStackEntry ->
                            val resumeId = backStackEntry.arguments?.getString("resumeId") ?: ""
                            BookingDetails(viewClientBookingViewModel, navController, resumeId)
                        }

                        composable("cancelnow/{resumeId}/{bookingstatus}/{bookingId}") { backStackEntry ->
                            val resumeId = backStackEntry.arguments?.getString("resumeId") ?: ""
                            val bookingStatus =
                                backStackEntry.arguments?.getString("bookingstatus") ?: ""
                            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
                            CancelNow(
                                updateBookingTradesmanViewModel,
                                viewClientBookingViewModel,
                                navController,
                                resumeId,
                                bookingStatus,
                                bookingId
                            )
                        }
                        //My Tradesman

                        composable("clientdeclinationdetails/{resumeId}/{status}") {backStackEntry ->
                            val resumeId = backStackEntry.arguments?.getString("resumeId") ?: ""
                            val status = backStackEntry.arguments?.getString("status") ?: ""

                            ClientDeclinationDetails(
                                resumeId,
                                status,
                                modifier = Modifier,
                                navController,
                                getClientBookingViewModel,


                                )
                        }
                        composable("clienthelp") {
                            ClientHelp(navController)
                        }

                        composable("clientdetails/{resumeId}/{status}") {backStackEntry ->
                            val resumeId = backStackEntry.arguments?.getString("resumeId") ?: ""
                            val status = backStackEntry.arguments?.getString("status") ?: ""

                            ClientDetails(
                                modifier = Modifier,
                                resumeId,
                                status,
                                getClientBookingViewModel,
                                navController

                            )
                        }

                        //My Applicants
                        composable("applicantsdetails/{resumeId}/{status}/{tradesmanId}") { backStackEntry ->
                            val resumeId = backStackEntry.arguments?.getString("resumeId") ?: ""
                            val status = backStackEntry.arguments?.getString("status") ?: ""
                            val tradesmanId = backStackEntry.arguments?.getString("tradesmanId") ?: ""

                            ApplicantDetails(
                                resumeId,
                                status,
                                tradesmanId,
                                modifier = Modifier,
                                navController,
                                getMyJobApplicantsViewModel,
                                viewResumeViewModel
                            )
                        }
                        composable("applicantdetailjobsummary/{resumeId}/{status}/{tradesmanId}") { backStackEntry ->
                            val resumeId = backStackEntry.arguments?.getString("resumeId") ?: ""
                            val status = backStackEntry.arguments?.getString("status") ?: ""
                            val tradesmanId = backStackEntry.arguments?.getString("tradesmanId") ?: ""

                            ApplicantDetailJobSummary(
                                resumeId,
                                status,
                                tradesmanId,
                                modifier = Modifier,
                                navController,
                                getMyJobApplicantsViewModel,
                                viewResumeViewModel
                            )
                        }
                        composable("applicantsdeclinedetails/{resumeId}/{status}/{tradesmanId}") { backStackEntry ->
                            val resumeId = backStackEntry.arguments?.getString("resumeId") ?: ""
                            val status = backStackEntry.arguments?.getString("status") ?: ""
                            val tradesmanId = backStackEntry.arguments?.getString("tradesmanId") ?: ""
                            ApplicantDeclinationDetails(
                                resumeId,
                                status,
                                tradesmanId,
                                modifier = Modifier,
                                navController,
                                getMyJobApplicantsViewModel,
                            )
                        }
                        composable("booking") {
                            BookingsScreen(
                                modifier = Modifier,
                                navController,
                                getClientBookingViewModel,
                                updateBookingTradesmanViewModel,
                                getMyJobApplicantsViewModel,
                                viewJobApplicationViewModel,
                                putJobApplicationStatusViewModel
                            )
                        }
                        composable("rateandreviews/{resumeId}/{tradesmanId}/{bookingId}") { backStackEntry ->
                            val resumeId = backStackEntry.arguments?.getString("resumeId") ?: ""
                            val tradesmanId = backStackEntry.arguments?.getString("tradesmanId") ?: ""
                            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
                            RateAndReviews(
                                rateTradesmanViewModel,
                                viewClientBookingViewModel,
                                navController,
                                resumeId,
                                tradesmanId,
                                bookingId
                            )
                        }

                        composable("acrepair") {
                            ACRepair(navController, getResumesViewModel, reportTradesmanViewModel)
                        }
                        composable("plumbing") {
                            Plumbing(navController, getResumesViewModel, reportTradesmanViewModel)
                        }
                        composable("carpentry") {
                            Carpentry(navController, getResumesViewModel, reportTradesmanViewModel)
                        }
                        composable("electrician") {
                            Electrician(navController, getResumesViewModel, reportTradesmanViewModel)
                        }
                        composable("masonry") {
                            Masonry(navController, getResumesViewModel, reportTradesmanViewModel)
                        }
                        composable("cleaning") {
                            Cleaning(navController, getResumesViewModel, reportTradesmanViewModel)
                        }
                        composable("mechanics") {
                            Mechanics(navController, getResumesViewModel, reportTradesmanViewModel)
                        }
                        composable("painting") {
                            Painting(navController, getResumesViewModel, reportTradesmanViewModel)
                        }
                        composable("roofing") {
                            Roofing(navController, getResumesViewModel, reportTradesmanViewModel)
                        }
                        composable("welding") {
                            Welding(navController, getResumesViewModel, reportTradesmanViewModel)
                        }
                        composable("alltradesman") {
                            AllTradesman(navController, getResumesViewModel, reportTradesmanViewModel)
                        }
                        composable("changepassword") {
                            ChangePassword(navController, changePasswordViewModel)
                        }
                        composable("aboutus") {
                            AboutUs(navController)
                        }
                        composable("reportproblem") {
                            ReportProblem(navController,reportConcernViewModel)
                        }
                        composable("notification") {
                            NotificationScreen(navController, getNotificationViewModel, clearNotificationViewModel)
                        }
                        composable("accountsettings") {
                            AccountSettings(navController, getClientProfileViewModel,updateClientProfileAddressViewModel)
                        }

                        composable("messaging/{chatId}/{receiverId}/{receipientName}/{receipientProfile}") { backStackEntry ->
                            val chatId = backStackEntry.arguments?.getString("chatId")?.toIntOrNull() ?: 0
                            val receiverId = backStackEntry.arguments?.getString("receiverId")?.toIntOrNull() ?: 0
                            val receipientName = backStackEntry.arguments?.getString("receipientName") ?: ""
                            val receipientProfile = backStackEntry.arguments?.getString("receipientProfile") ?: ""
                            val getMessagesViewModelFactory = GetMessageViewModelFactory(apiService, chatId, receiverId)
                            val getMessageViewModel = ViewModelProvider(
                                LocalViewModelStoreOwner.current!!,
                                getMessagesViewModelFactory
                            )[GetMessagesViewModel::class.java]

                            MessagingScreen(getMessageViewModel, receiverId, chatId, receipientName, receipientProfile, navController)
                        }

                        //Tradesman Routes
                        composable("hometradesman") {
                            HomeTradesman(
                                modifier = Modifier,
                                navController,
                                getJobsViewModel,
                                getRecentJobsViewModel,
                                reportClientViewModel
                            )
                        }
                        composable("tradesmanapply/{jobId}") { backStackEntry ->
                            val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
                            Log.e("Job ID", jobId)
                            TradesmanApply(jobId, navController, viewJobViewModel, getClientPostedJobsViewModel)
                        }
                        composable("hiringdetails/{jobId}/{clientId}") { backStackEntry ->
                            val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
                            val clientId = backStackEntry.arguments?.getString("clientId") ?: ""
                            HiringDetails(
                                jobId,
                                clientId,
                                modifier = Modifier,
                                navController,
                                postJobApplicationViewModel
                            )
                        }
                        composable("bookingstradesman") {
                            BookingsTradesman(
                                modifier = Modifier,
                                navController,
                                updateBookingClientViewModel,
                                getMyJobApplicationViewModel,
                                getTradesmanBookingViewModel,
                                putJobApplicationStatusViewModel,
                                viewJobApplicationViewModel
                            )
                        }
                        composable("profiletradesman") {
                            ProfileTradesman(
                                modifier = Modifier,
                                navController,
                                logoutViewModel,
                                viewTradesmanProfileViewModel,
                                updateTradesmanProfileViewModel,
                                updateTradesmanActiveStatusViewModel,
                                viewRatingsViewModel,
                            )
                        }
                        composable("manageprofile/{workLocation}/{phoneNumber}/{rate}/{aboutMe}") {backStackEntry ->
                            val workLocation = backStackEntry.arguments?.getString("workLocation") ?: ""
                            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
                            val rate = backStackEntry.arguments?.getString("rate") ?: ""
                            val aboutMe = backStackEntry.arguments?.getString("aboutMe") ?: ""
                            ManageProfile(
                                modifier = Modifier,
                                navController,
                                updateTradesmanDetailViewModel,
                                workLocation,
                                phoneNumber,
                                rate,
                                aboutMe
                            )
                        }
                        composable("availabilitystatus") {
                            AvailabilityStatus(modifier = Modifier, navController)
                        }
                        composable("profileverification/{statusofapproval}") { backStackEntry ->
                            val statusofapproval = backStackEntry.arguments?.getString("statusofapproval") ?: ""
                            ProfileVerification(
                                modifier = Modifier,
                                navController,
                                submitResumeViewModel,
                                statusofapproval
                            )
                        }
                        composable("canceltradesmannow/{jobId}") { backStackEntry ->
                            val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
                            CancelTradesmanNow(
                                jobId,
                                navController,
                                viewJobApplication = viewJobApplicationViewModel,
                                putJobApplicationStatus = putJobApplicationStatusViewModel
                            )
                        }
                        composable("myjobapplicationdetails") {
                            MyJobApplicationDetails(navController)
                        }
                        composable("accountsettingstradesman") {
                            AccountSettingsTradesman(navController)
                        }
                        //My Jobs
                        composable("tradesmandetails/{jobId}/{status}") { backStackEntry ->
                            val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
                            val status = backStackEntry.arguments?.getString("status") ?: ""
                            TradesmanDetails(
                                jobId,
                                status,
                                modifier = Modifier,
                                navController,
                                getTradesmanBookingViewModel,
                                reportClientViewModel)
                        }
                        composable("tradesmandeclineddetails/{jobId}/{status}") {backStackEntry ->
                            val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
                            val status = backStackEntry.arguments?.getString("status") ?: ""

                            TradesmanDeclinationDetails( jobId,
                                status,
                                modifier = Modifier,
                                navController,
                                getTradesmanBookingViewModel
                            )
                        }

                        // MY APPLICATIONS
                        composable("tradesmanapplication/{jobId}/{jobs}/{status}") {backStackEntry ->
                            val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
                            val jobs = backStackEntry.arguments?.getString("jobs") ?: ""
                            val status = backStackEntry.arguments?.getString("status") ?: ""

                            TradesmanApplication(jobId,jobs,status,modifier = Modifier, navController,getMyJobApplicationViewModel,viewJobViewModel,reportClientViewModel)
                        }
                        composable("tradesmanapplicationcanceldetails/{jobId}/{jobs}/{status}") {backStackEntry ->
                            val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
                            val jobs = backStackEntry.arguments?.getString("jobs") ?: ""
                            val status = backStackEntry.arguments?.getString("status") ?: ""

                            TradesmanApplicationCancelDetails(jobId,jobs,status,modifier = Modifier, navController,getMyJobApplicationViewModel)
                        }


                    }
                }
            }
        }
    }
}


