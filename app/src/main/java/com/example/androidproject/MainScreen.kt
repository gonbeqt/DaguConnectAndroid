package com.example.androidproject

import LogoutViewModel
import android.app.Activity
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidproject.data.preferences.AccountManager
import com.example.androidproject.view.client.HomeScreen
import com.example.androidproject.view.client.BookingsScreen
import com.example.androidproject.view.client.ProfileScreen
import com.example.androidproject.view.client.ScheduleScreen
import com.example.androidproject.view.tradesman.BookingsTradesman
import com.example.androidproject.view.tradesman.HomeTradesman
import com.example.androidproject.view.tradesman.ProfileTradesman
import com.example.androidproject.view.tradesman.ScheduleTradesman
import com.example.androidproject.viewmodel.jobs.GetJobsViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.androidproject.view.client.MessageScreen
import com.example.androidproject.viewmodel.Resumes.GetResumesViewModel
import com.example.androidproject.viewmodel.Tradesman_Profile.ViewTradesmanProfileViewModel
import com.example.androidproject.viewmodel.bookings.GetClientBookingViewModel
import com.example.androidproject.viewmodel.bookings.GetTradesmanBookingViewModel
import com.example.androidproject.viewmodel.bookings.UpdateWorkStatusViewModel
import com.example.androidproject.viewmodel.chats.GetChatViewModel
import com.example.androidproject.viewmodel.client_profile.GetClientProfileViewModel
import com.example.androidproject.viewmodel.job_application.PutJobApplicationStatusViewModel
import com.example.androidproject.viewmodel.job_application.ViewJobApplicationViewModel
import com.example.androidproject.viewmodel.job_application.client.GetMyJobApplicantsViewModel
import com.example.androidproject.viewmodel.job_application.tradesman.GetMyJobApplicationViewModel
import com.example.androidproject.viewmodel.jobs.GetMyJobsViewModel
import com.example.androidproject.viewmodel.jobs.GetRecentJobsViewModel
import com.example.androidproject.viewmodel.jobs.PostJobViewModel
import com.example.androidproject.viewmodel.report.ReportViewModel


@Composable
fun MainScreen(
    navController: NavController,
    logoutViewModel: LogoutViewModel,
    getClientsBooking: GetClientBookingViewModel,
    getResumesViewModel: GetResumesViewModel,
    modifier: Modifier = Modifier,
    viewModel: GetChatViewModel,
    reportViewModel: ReportViewModel,
    postJobsViewModel: PostJobViewModel,
    getMyJobsViewModel: GetMyJobsViewModel,
    getClientProfileViewModel: GetClientProfileViewModel,
    updateWorkStatusViewModel: UpdateWorkStatusViewModel,
    getRecentJobsViewModel: GetRecentJobsViewModel,
    viewTradesmanProfileViewModel: ViewTradesmanProfileViewModel,
    getMyJobApplications: GetMyJobApplicationViewModel,
    putJobApplicationStatusViewModel: PutJobApplicationStatusViewModel,
    getMyJobApplicantsViewModel: GetMyJobApplicantsViewModel,
    viewJobsApplication: ViewJobApplicationViewModel,
    getTradesmanBooking: GetTradesmanBookingViewModel,
    LoadingUI: @Composable () -> Unit
) {
    val role = AccountManager.getAccount()?.isClient
    val context = LocalContext.current
    val navItems = listOf(
        NavigationItem("Home", Icons.Default.Home),
        if (role == true) {
            NavigationItem("Hiring Hub", Icons.Default.ListAlt)
        } else {
            NavigationItem("Work Hub", Icons.Default.ListAlt)
        },
        NavigationItem("Schedule", Icons.Default.CalendarMonth),
        NavigationItem("Message", Icons.Default.Message),
        NavigationItem("Profile", Icons.Default.Person)
    )

    // Extract arguments from the current destination
    val arguments = navController.currentBackStackEntry?.arguments
    val initialSelectedItem = arguments?.getInt("selectedItem") ?: 0
    val initialSelectedTab = arguments?.getInt("selectedTab") ?: 0

    // Initialize selectedItem with the argument value
    var selectedItem by remember { mutableStateOf(initialSelectedItem) }

    // Observe selectedTab changes (still useful for SavedStateHandle updates if needed)
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val selectedTabState = savedStateHandle?.getLiveData<Int>("selectedTab")?.observeAsState()
    val selectedTab = selectedTabState?.value ?: initialSelectedTab

    // Update selectedItem when selectedTab changes (optional, for compatibility with SavedStateHandle)
    LaunchedEffect(selectedTab) {
        if (selectedTab in 1..5) {
            selectedItem = 1 // Switch to Bookings tab
            savedStateHandle?.remove<Int>("selectedTab") // Clear after use
        }
    }

    // Track the navigation history using a stack
    val navigationStack = remember { mutableStateListOf<Int>() }

    val activity = LocalContext.current as ComponentActivity
    val getJobsViewModel = remember { ViewModelSetups.setupGetJobsViewModel(activity) }

    // Handle back press or swipe gesture
    BackHandler(enabled = true) {
        Log.d("BackHandler", "Selected Item: $selectedItem, Stack: $navigationStack")
        if (selectedItem == 0) {
            (context as? Activity)?.finishAffinity()
        } else if (navigationStack.isNotEmpty()) {
            val previousItem = navigationStack.removeAt(navigationStack.size - 1)
            selectedItem = previousItem
        } else {
            selectedItem = 0
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(containerColor = Color.White, modifier = Modifier.shadow(16.dp)) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = {
                            navigationStack.removeAll { it == index }
                            if (selectedItem != index) {
                                navigationStack.add(selectedItem)
                            }
                            selectedItem = index
                            // Reset selectedTab to 0 when clicking Bookings screen (index 1)
                            if (index == 1) {
                                navController.currentBackStackEntry?.savedStateHandle?.set("selectedTab", 0)
                            }
                        },
                        icon = {
                            Icon(imageVector = item.icon, contentDescription = "Icon")
                        },
                        label = {
                            Text(text = item.nav_label, fontSize = 10.sp)
                        },
                        colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.Black,
                            selectedTextColor = Color.Black,
                            unselectedTextColor = Color.Black,
                            indicatorColor = Color(0xFF3CC0B0)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            selectedItem,
            selectedTab,
            navController,
            getJobsViewModel,
            logoutViewModel,
            getClientsBooking,
            getResumesViewModel,
            viewModel,
            reportViewModel,
            postJobsViewModel,
            getMyJobsViewModel,
            getClientProfileViewModel,
            updateWorkStatusViewModel,
            getRecentJobsViewModel,
            viewTradesmanProfileViewModel,
            getMyJobApplications,
            putJobApplicationStatusViewModel,
            getMyJobApplicantsViewModel,
            viewJobsApplication,
            getTradesmanBooking,
            LoadingUI
        )
    }
}
@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedItem: Int,
    selectedTab: Int, // Add selectedTab parameter
    navController: NavController,
    getJobsViewModel: GetJobsViewModel,
    logoutViewModel: LogoutViewModel,
    getClientsBooking: GetClientBookingViewModel,
    getResumesViewModel: GetResumesViewModel,
    viewModel: GetChatViewModel,
    reportViewModel: ReportViewModel,
    postJobsViewModel: PostJobViewModel,
    getMyJobsViewModel: GetMyJobsViewModel,
    getClientProfileViewModel: GetClientProfileViewModel,
    updateWorkStatusViewModel: UpdateWorkStatusViewModel,
    getRecentJobsViewModel: GetRecentJobsViewModel,
    viewTradesmanProfileViewModel: ViewTradesmanProfileViewModel,
    getMyJobApplications: GetMyJobApplicationViewModel,
    putJobApplicationStatusViewModel: PutJobApplicationStatusViewModel,
    getMyJobApplicantsViewModel: GetMyJobApplicantsViewModel,
    viewJobsApplication: ViewJobApplicationViewModel,
    getTradesmanBooking : GetTradesmanBookingViewModel,
    LoadingUI : @Composable () -> Unit // Add this parameter
) {
    val role = AccountManager.getAccount()?.isClient
    if (role == true) {
        when (selectedItem) {
            0 -> HomeScreen(modifier = modifier.padding(bottom = 0.1.dp),navController,getResumesViewModel,reportViewModel)
            1 -> BookingsScreen(modifier.padding(bottom = 0.1.dp),navController,getClientsBooking,updateWorkStatusViewModel, getMyJobApplicantsViewModel, viewJobsApplication, putJobApplicationStatusViewModel, selectedTab)
            2 -> ScheduleScreen(modifier.padding(bottom = 0.1.dp),navController,getClientsBooking)
            3 -> MessageScreen(modifier.padding(bottom = 0.1.dp),navController, viewModel)
            4 -> ProfileScreen(
                modifier = modifier.padding(bottom = 0.1.dp), navController, logoutViewModel, postJobsViewModel, getMyJobsViewModel, getClientProfileViewModel
            )
        }
    } else {
        when (selectedItem) {
            0 -> HomeTradesman(modifier = Modifier, navController, getJobsViewModel, getRecentJobsViewModel)
            1 -> BookingsTradesman(modifier = Modifier, navController,updateWorkStatusViewModel, getMyJobApplications,getTradesmanBooking, putJobApplicationStatusViewModel, viewJobsApplication)
            2 -> ScheduleTradesman(modifier.padding(bottom = 0.1.dp), navController)
            3 -> MessageScreen(modifier.padding(bottom = 0.1.dp), navController, viewModel)
            4 -> ProfileTradesman(modifier = Modifier, navController, logoutViewModel,viewTradesmanProfileViewModel,LoadingUI)
        }
    }
}




