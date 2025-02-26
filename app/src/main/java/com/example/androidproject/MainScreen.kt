package com.example.androidproject

import LogoutViewModel
import android.app.Activity
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidproject.data.preferences.AccountManager
import com.example.androidproject.view.pages.HomeScreen
import com.example.androidproject.view.pages.BookingsScreen
import com.example.androidproject.view.pages.BookmarkedScreen
import com.example.androidproject.view.pages.ProfileScreen
import com.example.androidproject.view.pages.ScheduleScreen
import com.example.androidproject.view.pages2.BookingsTradesman
import com.example.androidproject.view.pages2.BookmarkedTradesman
import com.example.androidproject.view.pages2.HomeTradesman
import com.example.androidproject.view.pages2.ProfileTradesman
import com.example.androidproject.view.pages2.ScheduleTradesman
import com.example.androidproject.viewmodel.jobs.GetJobsViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.androidproject.data.preferences.TokenManager
import com.example.androidproject.view.pages.MessageScreen
import com.example.androidproject.view.theme.myGradient3
import com.example.androidproject.viewmodel.Resumes.GetResumesViewModel
import com.example.androidproject.viewmodel.Resumes.ViewResumeViewModel
import com.example.androidproject.viewmodel.Tradesman_Profile.ViewTradesmanProfileViewModel
import com.example.androidproject.viewmodel.bookings.GetClientBookingViewModel
import com.example.androidproject.viewmodel.bookings.UpdateWorkStatusViewModel
import com.example.androidproject.viewmodel.chats.GetChatViewModel
import com.example.androidproject.viewmodel.client_profile.GetClientProfileViewModel
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
    viewModel:GetChatViewModel,
    reportViewModel: ReportViewModel,
    postJobsViewModel: PostJobViewModel,
    getMyJobsViewModel: GetMyJobsViewModel,
    getClientProfileViewModel: GetClientProfileViewModel,
    updateWorkStatusViewModel : UpdateWorkStatusViewModel,
    getRecentJobsViewModel: GetRecentJobsViewModel,
    viewTradesmanProfileViewModel : ViewTradesmanProfileViewModel
    ) {
    val context = LocalContext.current
    val navItems = listOf(
        NavigationItem("Home", Icons.Default.Home),
        NavigationItem("Bookings", Icons.Default.ListAlt),
        NavigationItem("Schedule", Icons.Default.CalendarMonth),
        NavigationItem("Message", Icons.Default.Message),
        NavigationItem("Profile", Icons.Default.Person)
    )

    // Track the selected item for bottom navigation
    var selectedItem by remember { mutableStateOf(0) }

    // Observe the selectedTab from savedStateHandle
    val selectedTabState = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<Int>("selectedTab")
        ?.observeAsState()

    // Extract the value from the State object, default to 0 if null
    val selectedTab = selectedTabState?.value ?: 0

    // Update selectedItem and clear the result when a tab is received
    LaunchedEffect(selectedTab) {
        if (selectedTab == 5) {
            selectedItem = 1 // Switch to Bookings tab
            navController.currentBackStackEntry?.savedStateHandle?.remove<Int>("selectedTab") // Clear the result
        }else if(selectedTab == 4){
            selectedItem = 1 // Switch to Bookings tab
            navController.currentBackStackEntry?.savedStateHandle?.remove<Int>("selectedTab") // Clear the result
        }
    }
    // Track the navigation history using a stack
    val navigationStack = remember { mutableStateListOf<Int>() }

    val activity = LocalContext.current as ComponentActivity
    val getJobsViewModel = remember { ViewModelSetups.setupGetJobsViewModel(activity) }

    // Handle back press or swipe gesture
    BackHandler(enabled = true) {
        Log.d("BackHandler", "Selected Item: $selectedItem")
        if (selectedItem == 0) {
            // Close the app if on the home screen (selectedItem == 0 corresponds to HomeScreen or HomeTradesman)
            (context as? Activity)?.finishAffinity()
        } else if (navigationStack.isNotEmpty()) {
            // Pop the last item from the stack to get the previous selected item
            val previousItem = navigationStack.removeAt(navigationStack.size - 1)
            selectedItem = previousItem
        } else {
            // If the stack is empty and not on the home screen, navigate back to the home screen
            selectedItem = 0
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = {
                            // Remove the old entry of the clicked item from the stack (if it exists)
                            navigationStack.removeAll { it == index }
                            // Add the current selected item to the stack before updating it
                            if (selectedItem != index) {
                                navigationStack.add(selectedItem)
                            }
                            // Update the selected item
                            selectedItem = index
                        },
                        icon = {
                            Icon(imageVector = item.icon, contentDescription = "Icon")
                        },
                        label = {
                            (Text(text = item.nav_label, fontSize = 10.sp))
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
            viewTradesmanProfileViewModel
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
    viewTradesmanProfileViewModel: ViewTradesmanProfileViewModel
) {
    val role = AccountManager.getAccount()?.isClient
    if (role == true) {
        when (selectedItem) {
            0 -> HomeScreen(modifier = modifier.padding(bottom = 0.1.dp),navController,getResumesViewModel,reportViewModel)
            1 -> BookingsScreen(modifier.padding(bottom = 0.1.dp),navController,getClientsBooking,updateWorkStatusViewModel,selectedTab)
            2 -> ScheduleScreen(modifier.padding(bottom = 0.1.dp),navController)
            3 -> MessageScreen(modifier.padding(bottom = 0.1.dp),navController, viewModel)
            4 -> ProfileScreen(
                modifier = modifier.padding(bottom = 0.1.dp), navController, logoutViewModel, postJobsViewModel, getMyJobsViewModel, getClientProfileViewModel
            )
        }
    } else {
        when (selectedItem) {
            0 -> HomeTradesman(modifier = Modifier, navController, getJobsViewModel, getRecentJobsViewModel)
            1 -> BookingsTradesman(modifier = Modifier, navController)
            2 -> ScheduleTradesman(modifier.padding(bottom = 0.1.dp), navController)
            3 -> MessageScreen(modifier.padding(bottom = 0.1.dp), navController, viewModel)
            4 -> ProfileTradesman(modifier = Modifier, navController, logoutViewModel,viewTradesmanProfileViewModel)
        }
    }
}


