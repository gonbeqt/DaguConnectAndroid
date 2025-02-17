package com.example.androidproject

import LogoutViewModel
import androidx.activity.ComponentActivity
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
import androidx.compose.runtime.getValue
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
import com.example.androidproject.view.pages.MessageScreen
import com.example.androidproject.view.theme.myGradient3
import com.example.androidproject.viewmodel.Resumes.GetResumesViewModel
import com.example.androidproject.viewmodel.bookings.GetClientBookingViewModel
import com.example.androidproject.viewmodel.chats.GetChatViewModel


@Composable
fun MainScreen(navController: NavController,logoutViewModel: LogoutViewModel, getClientsBooking: GetClientBookingViewModel,getResumesViewModel: GetResumesViewModel,modifier: Modifier = Modifier,viewModel:GetChatViewModel) {
    val navItems = listOf(
        NavigationItem("Home", Icons.Default.Home),
        NavigationItem("Bookings", Icons.Default.ListAlt),
        NavigationItem("Schedule", Icons.Default.CalendarMonth),
        NavigationItem("Message", Icons.Default.Message),
        NavigationItem("Profile", Icons.Default.Person)

    )
    var selectedItem by remember {
        mutableStateOf(0)
    }
    val activity = LocalContext.current as ComponentActivity
    val getJobsViewModel = remember { ViewModelSetups.setupGetJobsViewModel(activity) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar (containerColor = Color.White) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                        },
                        icon = {
                            Icon(imageVector = item.icon, contentDescription = "Icon")
                        },
                        label = {
                            (Text(text = item.nav_label, fontSize = 10.sp))
                        },
                        colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White, // Green when selected
                            unselectedIconColor = Color.Black, // Gray when not selected
                            selectedTextColor = Color.Black, // Green text when selected
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
            navController,
            getJobsViewModel,
            logoutViewModel,
            getClientsBooking,
            getResumesViewModel,
            viewModel)
    }
}

@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedItem: Int,
    navController: NavController,
    getJobsViewModel: GetJobsViewModel,
    logoutViewModel: LogoutViewModel,
    getClientsBooking: GetClientBookingViewModel,
    getResumesViewModel: GetResumesViewModel,
    viewModel: GetChatViewModel
) {

    val role = AccountManager.getAccount()?.isClient
    if (role == true) {
        when (selectedItem) {
            0 -> HomeScreen(modifier = modifier.padding(bottom = 0.1.dp),navController,getResumesViewModel)
            1 -> BookingsScreen(modifier.padding(bottom = 0.1.dp),navController,getClientsBooking)
            2 -> ScheduleScreen(modifier.padding(bottom = 0.1.dp),navController)
            3 -> MessageScreen(modifier.padding(bottom = 0.1.dp),navController, viewModel)
            4 -> ProfileScreen(
                modifier = modifier.padding(bottom = 0.1.dp), navController, logoutViewModel
            )
        }
    } else {
        when (selectedItem) {
            0 -> HomeTradesman(modifier = Modifier, navController, getJobsViewModel)
            1 -> BookingsTradesman(modifier = Modifier,navController)
            2 -> ScheduleTradesman(modifier.padding(bottom = 0.1.dp),navController)
            3 -> MessageScreen(modifier.padding(bottom = 0.1.dp),navController, viewModel)
            4 -> ProfileTradesman(modifier = Modifier, navController,logoutViewModel)
        }
    }
}

