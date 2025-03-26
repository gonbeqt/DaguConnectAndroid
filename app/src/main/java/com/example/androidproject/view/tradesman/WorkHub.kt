package com.example.androidproject.view.tradesman

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.ViewModelSetups
import com.example.androidproject.data.WebSocketManager
import com.example.androidproject.model.JobApplicationData
import com.example.androidproject.model.client.GetTradesmanBooking
import com.example.androidproject.utils.NetworkUtils.checkNetworkConnectivity
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.extras.LoadingUI
import com.example.androidproject.view.extras.SnackbarController
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.viewmodel.job_application.PutJobApplicationStatusViewModel
import com.example.androidproject.viewmodel.bookings.GetTradesmanBookingViewModel
import com.example.androidproject.viewmodel.bookings.UpdateBookingClientViewModel
import com.example.androidproject.viewmodel.job_application.ViewJobApplicationViewModel
import com.example.androidproject.viewmodel.job_application.tradesman.GetMyJobApplicationViewModel
import kotlinx.coroutines.delay
import java.sql.Types.NULL
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun BookingsTradesman(modifier: Modifier = Modifier, navController: NavController, updateBookingClientViewModel: UpdateBookingClientViewModel, getMyJobApplications: GetMyJobApplicationViewModel, getTradesmanBooking: GetTradesmanBookingViewModel, putJobApplicationStatusViewModel: PutJobApplicationStatusViewModel, viewJobsApplication: ViewJobApplicationViewModel,  initialTabIndex: Int = 0, initialSection: Int = 0) {// Default to 0 if not provided
    val windowSize = rememberWindowSizeClass()

    val iconSize = when (windowSize.width) {
        WindowType.SMALL -> 24.dp
        WindowType.MEDIUM -> 32.dp
        WindowType.LARGE -> 40.dp
    }
    val textSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }


    var selectedTabIndex by remember { mutableIntStateOf(initialTabIndex) }
    var selectedSection by remember { mutableIntStateOf(initialSection) }


    // Function to check network connectivity using NetworkCapabilities (modern approach)


    val context = LocalContext.current
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val isConnected = remember { mutableStateOf(checkNetworkConnectivity(connectivityManager)) }

    // State to track loading during retry
    var showLoading by remember { mutableStateOf(false) }


    // Define tab titles based on selected section
    val myJobsTabs = listOf("All", "Pending", "Declined", "Active", "Completed", "Cancelled")
    val myApplicantsTabs = listOf("All", "Pending", "Declined", "Active", "Completed", "Cancelled")
    val tabTitles = if (selectedSection == 0) myJobsTabs else myApplicantsTabs


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        Row(
            modifier = Modifier //top nav
                .padding(top = 8.dp, start = 25.dp, end = 25.dp, bottom = 8.dp)
                .fillMaxWidth()
                .height(50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left-aligned text
            Text(
                text = "Job Hub",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal
            )
            // Right-aligned icons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                        .clickable { navController.navigate("notification") }
                )
            }
        }
        // Add this Divider **outside** the Box to create a visible separator
        Divider(
            color = Color.Gray, // Change color if needed
            thickness = 0.3.dp, // Adjust thickness for better visibility
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                JobsTradesmanTopSection(navController, selectedSection) { section ->
                    selectedSection = section
                    selectedTabIndex = 0
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    // Tabs (Fixed Choices)
                    ScrollableTabRow(
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                modifier = Modifier
                                    .tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                color = Color(0xFF122826), // Change this to your desired color
                                height = 2.dp // Adjust thickness if needed
                            )
                        },
                        selectedTabIndex = selectedTabIndex,
                        modifier = Modifier.fillMaxWidth(),
                        edgePadding = 5.dp
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = { Text(title, fontSize = textSize, color = if (selectedTabIndex == index) Color(0xFF122826) else Color.Gray) },
                                modifier = Modifier.background(Color.White)
                            )
                        }
                    }

                        // Handle different states based on connectivity and data loading
                        if (!isConnected.value) {
                            if(showLoading){
                                LoadingUI()
                                LaunchedEffect(Unit) {
                                    delay(1500) // Show LoadingUI for 1.5 seconds
                                    isConnected.value = checkNetworkConnectivity(connectivityManager)
                                    showLoading = false // Hide LoadingUI after delay
                                }
                            }
                            // No internet connection
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "No Internet Connection",
                                        fontSize = 18.sp,
                                        color = Color.Red,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Please check your internet and try again.",
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    // Retry button (only fetch data when clicked)
                                    Box(
                                        modifier = Modifier
                                            .clickable {
                                                showLoading = true
                                            }
                                            .background(Color(0xFF3CC0B0), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = "Retry",
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        } else {
                            // Content changes based on the selected tab and section
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFFD9D9D9))
                                    .padding(8.dp)
                            ) {
                                when (selectedSection) {
                                    0 -> when (selectedTabIndex) {
                                        0 -> AllBookingsTradesmanContent(getTradesmanBooking)
                                        1 -> PendingBookingsTradesmanContent(navController,getTradesmanBooking,updateBookingClientViewModel)
                                        2 -> DeclinedBookingsTradesmanContent(navController,getTradesmanBooking)
                                        3 -> ActiveBookingsTradesmanContent(navController,getTradesmanBooking)
                                        4 -> CompletedBookingsTradesmanContent(navController,getTradesmanBooking)
                                        5 -> CancelledBookingsTradesmanContent(navController,getTradesmanBooking)
                                    }

                                    1 -> when (selectedTabIndex) {
                                        0 -> AllMySubmissionsTradesmanContent(getMyJobApplications)
                                        1 -> PendingMySubmissionsTradesmanContent(navController, getMyJobApplications, putJobApplicationStatusViewModel)
                                        2 -> DeclinedMySubmissionsTradesmanContent(navController, getMyJobApplications, viewJobsApplication)
                                        3 -> ActiveMySubmissionsTradesmanContent(navController, getMyJobApplications, viewJobsApplication, putJobApplicationStatusViewModel)
                                        4 -> CompletedMySubmissionsTradesmanContent(navController, getMyJobApplications, viewJobsApplication)
                                        5 -> CancelledMySubmissionsTradesmanContent(navController, getMyJobApplications, viewJobsApplication )
                                    }
                                }
                            }

                        }




                }


            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 76.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                SnackbarController.ObserveSnackbar()
            }
        }
    }
}
@Composable
fun JobsTradesmanTopSection(navController: NavController, selectedSection: Int, onSectionSelected: (Int) -> Unit) {
    val windowSize = rememberWindowSizeClass()
    val headerTextSize = when (windowSize.width) {
        WindowType.SMALL -> 16.sp
        WindowType.MEDIUM -> 18.sp
        WindowType.LARGE -> 20.sp
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left-aligned clickable text with box
        Box(
            modifier = Modifier
                .background(
                    if (selectedSection == 0) SolidColor(Color(0xFF122826)) else SolidColor(
                        Color.Transparent
                    )
                )
                .weight(1f)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            TextButton(
                onClick = { onSectionSelected(0) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (selectedSection == 0) Color.White else Color.Black
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "My Jobs",
                    fontSize = headerTextSize,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Right-aligned clickable text with box
        Box(
            modifier = Modifier
                .background(
                    if (selectedSection == 1) SolidColor(Color(0xFF122826)) else SolidColor(
                        Color.Transparent
                    )
                )
                .padding(4.dp)
                .weight(1f),
        ) {
            TextButton(
                onClick = { onSectionSelected(1) },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (selectedSection == 1) Color.White else Color.Black
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "My Applications",
                    fontSize = headerTextSize,
                    fontWeight = FontWeight.Medium
                )
            }
        }

    }
    Divider(
        color = Color.Gray, // Change color if needed
        thickness = 0.3.dp, // Adjust thickness for better visibility
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun AllBookingsTradesmanContent(getTradesmanBooking: GetTradesmanBookingViewModel) {
    val allBooking = getTradesmanBooking.TradesmanBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        allBooking.refresh()
    }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9)),
        contentAlignment = Alignment.Center // Center the content
    ) {
        if (allBooking.itemCount == 0) {
            // Display "No Declined Jobs" when the list is empty
            Text(
                text = "No Jobs",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .size(420.dp)
                    .background(Color(0xFFD9D9D9)),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(allBooking.itemCount) { index ->
                    val clients = allBooking[index]
                    if (clients != null) {
                        AllTradesmanItem(clients)
                    }
                }
            }
        }
    }
}
@Composable
fun PendingBookingsTradesmanContent(navController: NavController, getTradesmanBooking: GetTradesmanBookingViewModel, updateBookingClientViewModel: UpdateBookingClientViewModel ) {
    val bookingPendingstate = getTradesmanBooking.TradesmanBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        bookingPendingstate.refresh()
    }
    val bookingPending =
        bookingPendingstate.itemSnapshotList.items.filter { it.bookingStatus == "Pending" }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9)),
        contentAlignment = Alignment.Center // Center the content
    ) {
        if (bookingPending.isEmpty()) {
            // Display "No Pending Jobs" when the list is empty
            Text(
                text = "No Pending Jobs",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .fillMaxHeight()
                    .size(420.dp)
                    .background(Color(0xFFD9D9D9)),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(bookingPending.size) { index ->
                    val Pending = bookingPending[index]
                    PendingTradesmanItem(Pending, navController, updateBookingClientViewModel)
                }
            }
        }
    }

}
@Composable
fun DeclinedBookingsTradesmanContent(navController: NavController,getTradesmanBooking: GetTradesmanBookingViewModel) {
    val declinedBookingState = getTradesmanBooking.TradesmanBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        declinedBookingState.refresh()
    }

    val declinedBookings = declinedBookingState.itemSnapshotList.items.filter { it.bookingStatus == "Declined" }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9)),
        contentAlignment = Alignment.Center // Center the content
    ) {
        if (declinedBookings.isEmpty()) {
            // Display "No Declined Jobs" when the list is empty
            Text(
                text = "No Declined Jobs",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .size(420.dp)
                    .padding(bottom = 70.dp)
                    .background(Color(0xFFD9D9D9))
                ,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(declinedBookings.size) { index ->
                    val declined = declinedBookings[index]
                    DeclinedTradesmanItem(declined,navController)
                }
            }
        }
    }

}

@Composable
fun ActiveBookingsTradesmanContent(navController: NavController,getTradesmanBooking: GetTradesmanBookingViewModel) {
   val activeBookingstate = getTradesmanBooking.TradesmanBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        activeBookingstate.refresh()
    }
    val activeBookings = activeBookingstate.itemSnapshotList.items.filter { it.bookingStatus == "Active" }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9)),
        contentAlignment = Alignment.Center // Center the content
    ) {
        if (activeBookings.isEmpty()) {
            // Display "No Active Jobs" when the list is empty
            Text(
                text = "No Active Jobs",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else{

            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .size(420.dp)
                    .padding(bottom = 70.dp)
                    .background(Color(0xFFD9D9D9))
                ,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(activeBookings.size) { index ->
                    val active = activeBookings[index]
                    ActiveTradesmanItem(active, navController)
                }
            }
        }
    }
}

@Composable
fun CompletedBookingsTradesmanContent(navController: NavController,getTradesmanBooking: GetTradesmanBookingViewModel) {
    val completedBookingstate = getTradesmanBooking.TradesmanBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        completedBookingstate.refresh()
    }
    val completedBooking = completedBookingstate.itemSnapshotList.items.filter { it.bookingStatus == "Completed" }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9)),
        contentAlignment = Alignment.Center // Center the content
    ) {
        if (completedBooking.isEmpty()) {
            // Display "No Completed Jobs" when the list is empty
            Text(
                text = "No Completed Jobs",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .size(420.dp)
                    .padding(bottom = 70.dp)

                    .background(Color(0xFFD9D9D9))
                ,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(completedBooking.size) { index ->
                    val Pending = completedBooking[index]
                    CompletedItem(Pending, navController )
                }
            }
        }
    }

}
@Composable
fun CancelledBookingsTradesmanContent(navController: NavController,getTradesmanBooking: GetTradesmanBookingViewModel) {
    val cancelledBookingstate = getTradesmanBooking.TradesmanBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        cancelledBookingstate.refresh()
    }

    val cancelledBookings = cancelledBookingstate.itemSnapshotList.items.filter { it.bookingStatus == "Cancelled" }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9)),
        contentAlignment = Alignment.Center // Center the content
    ) {
        if (cancelledBookings.isEmpty()) {
            // Display "No Cancelled Jobs" when the list is empty
            Text(
                text = "No Cancelled Jobs",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .size(420.dp)
                    .padding(bottom = 70.dp)

                    .background(Color(0xFFD9D9D9))
                ,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(cancelledBookings.size) { index ->
                    val cancel = cancelledBookings[index]
                    CancelledItem(cancel, navController )
                }
            }
        }
    }
}



//Design For Items
@Composable
fun AllTradesmanItem(allBooking: GetTradesmanBooking) {
    val date = ViewModelSetups.formatDateTime(allBooking.bookingDate)
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 470.dp to 210.dp
        WindowType.MEDIUM -> 480.dp to 220.dp
        WindowType.LARGE -> 490.dp to 230.dp
    }
    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 18.sp
        WindowType.MEDIUM -> 20.sp
        WindowType.LARGE -> 22.sp
    }
    val taskTextSize = when (windowSize.width) {
        WindowType.SMALL -> 14.sp
        WindowType.MEDIUM -> 16.sp
        WindowType.LARGE -> 18.sp
    }
    val smallTextSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }
    val statusColor = when (allBooking.bookingStatus.lowercase()) {
        "pending" -> Color(0xFFFFA500) // Orange
        "active" -> Color(0xFF00FF00)  // Green
        "completed" -> Color(0xFF0000FF) // Blue
        "declined", "cancelled" -> Color(0xFFFF0000) // Red
        else -> Color.Gray // Default fallback
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }, // Add implementation for click if needed
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            Column( // Using Column to stack elements vertically inside the Card
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tradesman image
                    AsyncImage(
                        model = allBooking.clientProfile,
                        contentDescription = "Tradesman Image",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(start = 10.dp)
                    )
                    // Tradesman details
                    Column(
                        modifier = Modifier
                            .padding(start = 10.dp)

                    ) {

                        Text(
                            text = allBooking.clientFullName,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium,
                            fontSize = nameTextSize
                        )

                        Row {
                            Text(
                                text = "Job Date:",
                                color = Color.Black,
                                fontSize = taskTextSize
                            )
                            Text(
                                text = date,
                                color = Color.Gray,
                                fontSize = taskTextSize,
                            )
                        }
                        Text(
                            text = allBooking.bookingStatus,
                            color = statusColor,
                            fontSize = taskTextSize,
                        )

                    }
                }
            }
        }
    }
}


@Composable
fun PendingTradesmanItem(pending: GetTradesmanBooking, navController: NavController, updateBookingClientViewModel :  UpdateBookingClientViewModel) {
    val updateWorkStatus by updateBookingClientViewModel.clientWorkStatusState.collectAsState()
    val  context = LocalContext.current
    var showApproveDialog by remember { mutableStateOf(false) }
    var showDeclineDialog by remember { mutableStateOf(false) }
    var showJobApproveDialog by remember { mutableStateOf(false) }
    var showDeclineReasons by remember { mutableStateOf(false) }
    val date = ViewModelSetups.formatDateTime(pending.bookingDate)
    var cancelReason by remember { mutableStateOf("") }
    LaunchedEffect(updateWorkStatus) {
        when (val updateStatus = updateWorkStatus) {
            is UpdateBookingClientViewModel.UpdateClientWorkStatus.Loading -> {
                // Show loading dialog
            }
            is UpdateBookingClientViewModel.UpdateClientWorkStatus.Success -> {
                updateBookingClientViewModel.resetState()
                // Set the selectedTab based on the work status
                if (updateStatus.status == "Accepted") {
                    SnackbarController.show("Jobs has been Accepted")
                    WebSocketManager.sendNotificationBookingToClient(
                        pending.id.toString(),
                        "Tradesman has accepted your booking request!",
                        "${pending.tradesmanFullName} has accepted your booked service request. Start a conversation to futhermore give details.",
                    )
                    navController.navigate("main_screen?selectedItem=1&selectedTab=3"){
                        navController.popBackStack()
                    }
                } else if (updateStatus.status == "Declined") {
                    SnackbarController.show("Jobs has been Declined")
                    WebSocketManager.sendNotificationBookingToClient(
                        pending.userId.toString(),
                        "Tradesman has declined your booking request!",
                        "${pending.tradesmanFullName} has declined your booked service request due to $cancelReason.",
                    )
                    navController.navigate("main_screen?selectedItem=1&selectedTab=2") {
                        navController.popBackStack()
                    }
                }


            }

            is UpdateBookingClientViewModel.UpdateClientWorkStatus.Error -> {
                val errorMessage = updateStatus.message
                    SnackbarController.show(errorMessage)
                updateBookingClientViewModel.resetState()
            }

            else -> Unit
        }
    }

    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 390.dp to 180.dp
        WindowType.MEDIUM -> 400.dp to 190.dp
        WindowType.LARGE -> 410.dp to 200.dp
    }
    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 18.sp
        WindowType.MEDIUM -> 20.sp
        WindowType.LARGE -> 22.sp
    }
    val taskTextSize = when (windowSize.width) {
        WindowType.SMALL -> 14.sp
        WindowType.MEDIUM -> 16.sp
        WindowType.LARGE -> 18.sp
    }
    val smallTextSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }
    Card(
        modifier = Modifier
            .clickable { navController.navigate("tradesmandetails/${pending.id}/${pending.bookingStatus}") }
            .fillMaxWidth()
        ,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            Column( // Using Column to stack elements vertically inside the Card
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tradesman image
                    AsyncImage(
                        model = pending.clientProfile,
                        contentDescription = "Tradesman Image",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(start = 10.dp)
                    )

                    // Tradesman details
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp)
                    ) {
                        Text(
                            text = pending.clientFullName,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium,
                            fontSize = nameTextSize,
                        )
                        Row {
                            Text(
                                text = "Job Date:",
                                color = Color.Black,
                                fontSize = taskTextSize,
                            )
                            Text(
                                modifier = Modifier.padding(start = 5.dp),
                                text = date,
                                color = Color.Gray,
                                fontSize = taskTextSize,
                            )
                        }
                    }
                }

                // Spacer between text and buttons
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Space out the buttons
                ) {
                    Box(
                        modifier = Modifier
                            .clickable { showDeclineDialog = true }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Decline", fontSize = smallTextSize)
                    }

                    Box(
                        modifier = Modifier
                            .clickable { showApproveDialog = true }
                            .background(
                                color = Color(0xFF42C2AE),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color(0xFF42C2AE), shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(text = "Approve", color = Color.White, fontSize = smallTextSize)
                    }
                }
            }
        }
    }
    if (showApproveDialog) {
        AlertDialog(
            onDismissRequest = { showApproveDialog = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_approve_decline),
                        contentDescription = "Approval Icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Approve Job",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                }
            },
            text = { Text("Once approved, this job will be marked as active. Proceed?") },
            confirmButton = {
                Button(
                    onClick = {
                        showApproveDialog = false
                        showJobApproveDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42C2AE))
                ) {
                    Text("Confirm", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showApproveDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }

    if (showDeclineDialog) {
        AlertDialog(
            onDismissRequest = { showDeclineDialog = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_approve_decline),
                        contentDescription = "Approval Icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Decline Job",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                }
            },
            text = { Text("Once declined, this job may not be available again. Proceed?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeclineDialog = false
                        showDeclineReasons = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Confirm", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeclineDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }

    if (showJobApproveDialog) {
        AlertDialog(
            onDismissRequest = { showJobApproveDialog = false },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_jobapproved_checked),
                    contentDescription = "Job Approve Icon",
                    modifier = Modifier.size(60.dp)
                )
            },
            title = { Text("Job Approved!") },
            text = { Text("Reach out to the client for more project details.") },
            confirmButton = {
                Button(
                    onClick = {
                                showJobApproveDialog = false
                            updateBookingClientViewModel.updateClientWorkStatus(
                                "Accepted",
                                NULL.toString(),
                                pending.id
                            )

                              },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42C2AE)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),

                ) {
                    Text("OK", color = Color.White)
                }
            }
        )
    }

    if (showDeclineReasons) {
        var selectedReason by remember { mutableStateOf<String?>(null) }

        val reasons = listOf(
            "Workload concerns",
            "Schedule conflicts",
            "Relocation issues",
            "Committed to a contract project",
            "Short notice start date",
            "Personal Reasons",
            "Other"
        )

        AlertDialog(
            onDismissRequest = { showDeclineReasons = false },
            title = {
                Text(
                    text = "Reason for Declination",
                    fontSize = 18.sp,
                    color = Color(0xFF42C2AE)
                )
            },
            text = {
                Column {
                    reasons.forEach { reason ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedReason = reason },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selectedReason == reason,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedReason = reason
                                    }
                                }
                            )
                            Text(reason, fontSize = 14.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeclineReasons = false
                        selectedReason?.let {
                            updateBookingClientViewModel.updateClientWorkStatus(
                                "Declined",
                                it,
                                pending.id
                            )
                            cancelReason = it
                        }
                    },
                    enabled = selectedReason != null,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42C2AE)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit", color = Color.White)
                }
            }
        )
    }
}





    @Composable
fun DeclinedTradesmanItem(declined: GetTradesmanBooking, navController: NavController) {
    val date = ViewModelSetups.formatDateTime(declined.bookingDate)
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 390.dp to 180.dp
        WindowType.MEDIUM -> 400.dp to 190.dp
        WindowType.LARGE -> 410.dp to 210.dp
    }
        val nameTextSize = when (windowSize.width) {
            WindowType.SMALL -> 18.sp
            WindowType.MEDIUM -> 20.sp
            WindowType.LARGE -> 22.sp
        }
        val taskTextSize = when (windowSize.width) {
            WindowType.SMALL -> 14.sp
            WindowType.MEDIUM -> 16.sp
            WindowType.LARGE -> 18.sp
        }
        val smallTextSize = when (windowSize.width) {
            WindowType.SMALL -> 12.sp
            WindowType.MEDIUM -> 14.sp
            WindowType.LARGE -> 16.sp
        }

    Card(
        modifier = Modifier
            .clickable { navController.navigate("tradesmandetails/${declined.id}/${declined.bookingStatus}") }
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            Column( // Using Column to stack elements vertically inside the Card
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tradesman image
                    AsyncImage(
                        model = declined.clientProfile,
                        contentDescription = "Tradesman Image",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(start = 10.dp)
                    )
                    // Tradesman details
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text =declined.clientFullName,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = nameTextSize,
                        )
                        Row {
                            Text(
                                text = "Job Date:",
                                color = Color.Black,
                                fontSize = taskTextSize
                            )
                            Text(
                                text = date,
                                color = Color.Gray,
                                fontSize = taskTextSize,
                            )
                        }

                    }
                }
                // Spacer between text and buttons
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("tradesmandetails/${declined.id}/${declined.bookingStatus}") }
                        .background(
                            color = Color.Transparent,
                        )
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                        .padding(vertical = 8.dp),  // Adjust padding as needed
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Job Details", fontSize = smallTextSize)
                }
            }
        }
    }
}

@Composable
fun ActiveTradesmanItem(active: GetTradesmanBooking, navController: NavController) {
    val date = ViewModelSetups.formatDateTime(active.bookingDate)
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 390.dp to 240.dp
        WindowType.MEDIUM -> 400.dp to 250.dp
        WindowType.LARGE -> 410.dp to 260.dp
    }
    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 18.sp
        WindowType.MEDIUM -> 20.sp
        WindowType.LARGE -> 22.sp
    }
    val taskTextSize = when (windowSize.width) {
        WindowType.SMALL -> 14.sp
        WindowType.MEDIUM -> 16.sp
        WindowType.LARGE -> 18.sp
    }
    val smallTextSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("tradesmandetails/${active.id}/${active.bookingStatus}") }
        ,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            Column( // Using Column to stack elements vertically inside the Card
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tradesman image
                    AsyncImage(
                        model = active.clientProfile,
                        contentDescription = "Tradesman Image",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(start = 10.dp)
                    )

                    // Tradesman details
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp)
                    ) {
                        Text(
                            text = active.clientFullName,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium,
                            fontSize = nameTextSize,
                        )
                        Row {
                            Text(
                                text = "Job Date:",
                                color = Color.Black,
                                fontSize = taskTextSize,
                            )
                            Text(
                                modifier = Modifier.padding(start = 5.dp),
                                text = date,
                                color = Color.Gray,
                                fontSize = taskTextSize,
                            )
                        }
                    }
                }

                // Spacer between text and buttons
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("tradesmandetails/${active.id}/${active.bookingStatus}") }
                        .background(
                            color = Color.Transparent,
                        )
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                        .padding(vertical = 8.dp),  // Adjust padding as needed
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Job Details", fontSize = smallTextSize)
                }
            }
        }
    }
}
@Composable
fun CompletedItem(completed: GetTradesmanBooking, navController: NavController) {
    val date = ViewModelSetups.formatDateTime(completed.bookingDate)
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 390.dp to 240.dp
        WindowType.MEDIUM -> 400.dp to 250.dp
        WindowType.LARGE -> 410.dp to 260.dp
    }
    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 18.sp
        WindowType.MEDIUM -> 20.sp
        WindowType.LARGE -> 22.sp
    }
    val taskTextSize = when (windowSize.width) {
        WindowType.SMALL -> 14.sp
        WindowType.MEDIUM -> 16.sp
        WindowType.LARGE -> 18.sp
    }
    val smallTextSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }
    Card(
        modifier = Modifier
            .clickable { navController.navigate("tradesmandetails/${completed.id}/${completed.bookingStatus}") }
            .fillMaxWidth()
        ,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            Column( // Using Column to stack elements vertically inside the Card
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tradesman image
                    AsyncImage(
                        model = completed.clientProfile,
                        contentDescription = "Tradesman Image",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(start = 10.dp)
                    )
                    // Tradesman details
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = completed.clientFullName,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = taskTextSize,
                        )
                        Row {
                            Text(
                                text = "Job Date:",
                                color = Color.Black,
                                fontSize = taskTextSize
                            )
                            Text(
                                text = date,
                                color = Color.Gray,
                                fontSize = taskTextSize,
                            )
                        }

                    }
                }
                // Spacer between text and buttons
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("tradesmandetails/${completed.id}/${completed.bookingStatus}") }
                        .background(
                            color = Color.Transparent,
                        )
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                        .padding(vertical = 8.dp),  // Adjust padding as needed
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Job Details", fontSize = smallTextSize)
                }
            }
        }
    }

}


@Composable
fun CancelledItem(cancel: GetTradesmanBooking, navController: NavController) {
    val date = ViewModelSetups.formatDateTime(cancel.bookingDate)
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 390.dp to 240.dp
        WindowType.MEDIUM -> 400.dp to 250.dp
        WindowType.LARGE -> 410.dp to 260.dp
    }
    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 18.sp
        WindowType.MEDIUM -> 20.sp
        WindowType.LARGE -> 22.sp
    }
    val taskTextSize = when (windowSize.width) {
        WindowType.SMALL -> 14.sp
        WindowType.MEDIUM -> 16.sp
        WindowType.LARGE -> 18.sp
    }
    val smallTextSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }
    Card(
        modifier = Modifier
            .clickable { navController.navigate("tradesmandetails/${cancel.id}/${cancel.bookingStatus}") }
            .fillMaxWidth()
        ,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            Column( // Using Column to stack elements vertically inside the Card
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tradesman image
                    AsyncImage(
                        model = cancel.clientProfile,
                        contentDescription = "Tradesman Image",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(start = 10.dp)
                    )
                    // Tradesman details
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = cancel.clientFullName,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = taskTextSize,
                        )
                        Row {
                            Text(
                                text = "Job Date:",
                                color = Color.Black,
                                fontSize = 12.sp
                            )
                            Text(
                                text = date,
                                color = Color.Gray,
                                fontSize = taskTextSize,
                            )
                        }

                    }
                }
                // Spacer between text and buttons
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("tradesmandetails/${cancel.id}/${cancel.bookingStatus}") }
                        .background(
                            color = Color.Transparent,
                        )
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                        .padding(vertical = 8.dp),  // Adjust padding as needed
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Job Details", fontSize = smallTextSize)
                }
            }
        }
    }
}



@Composable
fun AllMySubmissionsTradesmanContent(getMyJobApplications: GetMyJobApplicationViewModel) {
    val myJobs = getMyJobApplications.jobApplicationPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        myJobs.refresh()
    }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9)),
        contentAlignment = Alignment.Center // Center the content
    ) {
        if (myJobs.itemCount == 0) {
            // Display "No Submissions" when the list is empty
            Text(
                text = "No Applications",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(bottom = 70.dp)

                    .size(420.dp)
                    .background(Color(0xFFD9D9D9))

                ,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(myJobs.itemCount) { index ->
                    val myJob = myJobs[index]
                    if (myJob != null) {
                        AllMySubmissionsTradesmanItem(myJob)
                    }
                }
            }
        }
    }


}


@Composable
fun PendingMySubmissionsTradesmanContent(
    navController: NavController,
    getMyJobApplications: GetMyJobApplicationViewModel,
    putJobApplicationStatusViewModel: PutJobApplicationStatusViewModel
) {
    val myJob = getMyJobApplications.jobApplicationPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        myJob.refresh()
    }
    val pendingApplication = myJob.itemSnapshotList.items.filter { it.status == "Pending" }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9)),
        contentAlignment = Alignment.Center // Center the content
    ) {
        if (pendingApplication.isEmpty()) {
            // Display "No Pending Submissions" when the list is empty
            Text(
                text = "No Pending Applications",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else{
            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 70.dp)
                    .fillMaxHeight()
                    .size(420.dp)
                    .background(Color(0xFFD9D9D9)),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(pendingApplication.size) { index ->
                    val pendingJobs = pendingApplication[index]
                    PendingMySubmissionsTradesmanItem(
                        pendingJobs,
                        navController,
                        putJobApplicationStatusViewModel,

                    )
                }
            }
        }
    }


}
@Composable
fun DeclinedMySubmissionsTradesmanContent(navController: NavController, getMyJobApplications: GetMyJobApplicationViewModel, viewJobsApplication: ViewJobApplicationViewModel) {
    val myJob = getMyJobApplications.jobApplicationPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        myJob.refresh()
    }
    val declinedApplication = myJob.itemSnapshotList.items.filter { it.status == "Declined" }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9)),
        contentAlignment = Alignment.Center // Center the content
    ) {
        if (declinedApplication.isEmpty()) {
            // Display "No Active Submissions" when the list is empty
            Text(
                text = "No Decline Applications",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else{
            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 70.dp)

                    .fillMaxHeight()
                    .size(420.dp)
                    .background(Color(0xFFD9D9D9))
                ,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(declinedApplication.size) { index ->
                    val declinedJobs = declinedApplication[index]
                    DeclinedMySubmissionsTradesmanItem(declinedJobs, navController)
                }
            }
        }
    }

}

@Composable
fun ActiveMySubmissionsTradesmanContent(navController: NavController, getMyJobApplications: GetMyJobApplicationViewModel, viewJobsApplication: ViewJobApplicationViewModel, putJobApplicationStatusViewModel: PutJobApplicationStatusViewModel) {

    val myJob = getMyJobApplications.jobApplicationPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        myJob.refresh()
    }

    val activeApplication = myJob.itemSnapshotList.items.filter { it.status == "Active" }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9)),
        contentAlignment = Alignment.Center // Center the content
    ) {
        if (activeApplication.isEmpty()) {
            // Display "No Active Submissions" when the list is empty
            Text(
                text = "No Active Applications",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else{

            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 70.dp)

                    .fillMaxHeight()
                    .size(420.dp)
                    .background(Color(0xFFD9D9D9))
                ,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(activeApplication.size) { index ->
                    val activeJobs = activeApplication[index]
                    ActiveMySubmissionsTradesmanItem(activeJobs,navController, putJobApplicationStatusViewModel)
                }
            }
        }
    }

}

@Composable
fun CompletedMySubmissionsTradesmanContent(navController: NavController, getMyJobApplications: GetMyJobApplicationViewModel, viewJobsApplication: ViewJobApplicationViewModel) {
    val myJob = getMyJobApplications.jobApplicationPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        myJob.refresh()
    }

    val completedApplication = myJob.itemSnapshotList.items.filter { it.status == "Completed" }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9)),
        contentAlignment = Alignment.Center // Center the content
    ) {
        if (completedApplication.isEmpty()) {
            // Display "No Completed Submissions" when the list is empty
            Text(
                text = "No Completed Applications",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else{
            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 70.dp)

                    .fillMaxHeight()
                    .size(420.dp)
                    .background(Color(0xFFD9D9D9))

                ,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(completedApplication.size) { index ->
                    val completedJobs = completedApplication[index]
                    CompletedMySubmissionsTradesmanItem(completedJobs, navController )
                }
            }
        }
    }
}
@Composable
fun CancelledMySubmissionsTradesmanContent(navController: NavController, getMyJobApplications: GetMyJobApplicationViewModel, viewJobsApplication: ViewJobApplicationViewModel) {
    val myJob = getMyJobApplications.jobApplicationPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        myJob.refresh()
    }

    val cancelledApplication = myJob.itemSnapshotList.items.filter { it.status == "Cancelled" }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9)),
        contentAlignment = Alignment.Center // Center the content
    ) {
        if (cancelledApplication.isEmpty()) {
            // Display "No Declined Submissions" when the list is empty
            Text(
                text = "No Cancelled Applications",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(bottom = 70.dp)

                    .size(420.dp)
                    .background(Color(0xFFD9D9D9))
                ,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(cancelledApplication.size) { index ->
                    val calledJobs = cancelledApplication[index]
                    CancelledMySubmissionsTradesmanItem(calledJobs, navController )
                }
            }
        }
    }

}



//Design For Items
@Composable
fun AllMySubmissionsTradesmanItem(myJob: JobApplicationData) {
    val updatedAt = ViewModelSetups.formatDateTime(myJob.jobDateStatus)
    val deadline = ViewModelSetups.formatDateTime(myJob.jobDeadline)

    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 390.dp to 190.dp
        WindowType.MEDIUM -> 400.dp to 200.dp
        WindowType.LARGE -> 410.dp to 210.dp
    }
    var jobType = myJob.jobType

    if (jobType == "Electrical_work") {
        jobType = "Electrical Work"
    }
    val statusColor = when (myJob.status.lowercase()) {
        "pending" -> Color(0xFFFFA500) // Orange
        "active" -> Color(0xFF00FF00)  // Green
        "completed" -> Color(0xFF0000FF) // Blue
        "declined", "cancelled" -> Color(0xFFFF0000) // Red
        else -> Color.Gray // Default fallback
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }, // Add implementation for click if needed
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    model = myJob.clientProfilePicture, // Use URL here
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                // Tradesman details
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                        Text(
                            text = myJob.clientFullname,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(text = myJob.status, color = statusColor, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Service: $jobType ",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Applied on $updatedAt",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Due-Date: $deadline",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Address: ${myJob.jobAddress}",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}


@Composable
fun PendingMySubmissionsTradesmanItem(
    myJob: JobApplicationData,
    navController: NavController,
    putJobApplicationStatusViewModel: PutJobApplicationStatusViewModel
) {
    val windowSize = rememberWindowSizeClass()
    var ifCancelledClicked by remember { mutableStateOf(false) }
    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 18.sp
        WindowType.MEDIUM -> 20.sp
        WindowType.LARGE -> 22.sp
    }
    val taskTextSize = when (windowSize.width) {
        WindowType.SMALL -> 14.sp
        WindowType.MEDIUM -> 16.sp
        WindowType.LARGE -> 18.sp
    }
    val smallTextSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }
    var showCancelDialog by remember { mutableStateOf(false) }
    var showCancelReasons by remember { mutableStateOf(false) }

    val createdAt = ViewModelSetups.formatDateTime(myJob.createdAt)
    val deadline = ViewModelSetups.formatDateTime(myJob.jobDeadline)
    val putState by putJobApplicationStatusViewModel.putJobApplicationState.collectAsState()
    var cancelReason by remember { mutableStateOf("") }
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 400.dp to 270.dp
        WindowType.MEDIUM -> 410.dp to 280.dp
        WindowType.LARGE -> 420.dp to 280.dp
    }
    var jobType = myJob.jobType

    if (jobType == "Electrical_work") {
        jobType = "Electrical Work"
    }
    LaunchedEffect(putState) {
        when (putState) {
            is PutJobApplicationStatusViewModel.PutJobApplicationState.Success -> {
                WebSocketManager.sendNotificationJobToClient(
                    myJob.clientId.toString(),
                    "A job application was declined",
                    "${myJob.tradesmanFullname} has declined the job application due to ${cancelReason}"
                )
                SnackbarController.show("Application declined")
                putJobApplicationStatusViewModel.resetState()
                navController.navigate("main_screen?selectedItem=1&selectedTab=2&selectedSection=1") {
                    navController.popBackStack()
                }

            }
            else -> {}
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("tradesmanapplication/${myJob.id}/${myJob.jobId}/${myJob.status}") }, // Add implementation for click if needed
        shape = RoundedCornerShape(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Tradesman image
                    AsyncImage(
                        model = myJob.clientProfilePicture, // Use URL here
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(62.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    // Tradesman details
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = myJob.clientFullname,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Service: $jobType",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Applied at: $createdAt",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Due-Date: $deadline",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Address: ${myJob.jobAddress}, Pangasinan",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Space out the buttons
                ) {
                    Box(
                        modifier = Modifier
                            .clickable {
                                showCancelDialog = true
                            }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Cancel Application ", fontSize = smallTextSize)
                    }
                    Box(
                        modifier = Modifier
                            .clickable { navController.navigate("tradesmanapplication/${myJob.id}/${myJob.jobId}/${myJob.status}") }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color(0xFFECAB1E), shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Job Details", color = Color(0xFFECAB1E), fontSize = smallTextSize)
                    }
                }
            }
        }
    }



    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_approve_decline),
                        contentDescription = "Approval Icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cancel Job",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                }
            },
            text = { Text("Once Cancelled, this job may not be available again. Proceed?") },
            confirmButton = {
                Button(
                    onClick = {
                        showCancelDialog = false
                        showCancelReasons = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Confirm", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showCancelDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }


    if (showCancelReasons) {
        var selectedReason by remember { mutableStateOf<String?>(null) }

        val reasons = listOf(
            "Workload concerns",
            "Schedule conflicts",
            "Relocation issues",
            "Committed to a contract project",
            "Short notice start date",
            "Personal Reasons",
            "Other"
        )

        AlertDialog(
            onDismissRequest = { showCancelReasons = false },
            title = {
                Text(
                    text = "Reason for Cancellation",
                    fontSize = 18.sp,
                    color = Color(0xFF42C2AE)
                )
            },
            text = {
                Column {
                    reasons.forEach { reason ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedReason = reason },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selectedReason == reason,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedReason = reason
                                    }
                                }
                            )
                            Text(reason, fontSize = 14.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showCancelReasons = false
                        selectedReason?.let {
                            putJobApplicationStatusViewModel.updateJobApplicationStatus(myJob.id,"Cancelled",it)
                            cancelReason = it
                        }
                    },
                    enabled = selectedReason != null,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42C2AE)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit", color = Color.White)
                }
            }
        )
    }
}
@Composable
fun ActiveMySubmissionsTradesmanItem(myJob: JobApplicationData, navController: NavController, putJobApplicationStatusViewModel: PutJobApplicationStatusViewModel) {
    val windowSize = rememberWindowSizeClass()
    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 18.sp
        WindowType.MEDIUM -> 20.sp
        WindowType.LARGE -> 22.sp
    }
    val taskTextSize = when (windowSize.width) {
        WindowType.SMALL -> 14.sp
        WindowType.MEDIUM -> 16.sp
        WindowType.LARGE -> 18.sp
    }
    val smallTextSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }
    val createdAt = ViewModelSetups.formatDateTime(myJob.createdAt)
    val deadline = ViewModelSetups.formatDateTime(myJob.jobDeadline)
    val putJobState by putJobApplicationStatusViewModel.putJobApplicationState.collectAsState()
    var cancelledClicked by remember { mutableStateOf(false) }
    var completedClicked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(putJobState) {
        if (cancelledClicked || completedClicked) {
            when (val putJob = putJobState) {


                is PutJobApplicationStatusViewModel.PutJobApplicationState.Loading -> {

                }

                is PutJobApplicationStatusViewModel.PutJobApplicationState.Error -> {
                    SnackbarController.show(putJob.message)
                }

                is PutJobApplicationStatusViewModel.PutJobApplicationState.Success -> {
                    SnackbarController.show( putJob.data.message())

                    putJobApplicationStatusViewModel.resetState()
                    navController.navigate("main_screen?selectedItem=1&selectedTab=4&selectedSection=1") {
                        navController.popBackStack()
                    }
                }else -> Unit
            }
        }
    }

    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 410.dp to 270.dp
        WindowType.MEDIUM -> 420.dp to 280.dp
        WindowType.LARGE -> 430.dp to 280.dp
    }
    var jobType = myJob.jobType

    if (jobType == "Electrical_work") {
        jobType = "Electrical Work"
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("tradesmanapplication/${myJob.id}/${myJob.jobId}/${myJob.status}") },
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Tradesman image
                    AsyncImage(
                        model = myJob.clientProfilePicture, // Use URL here
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(62.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    // Tradesman details
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = myJob.clientFullname,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Service: $jobType",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Applied on: $createdAt",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Due-Date: $deadline",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Address: ${myJob.jobAddress}",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("tradesmanapplication/${myJob.id}/${myJob.jobId}/${myJob.status}") }
                        .background(
                            color = Color.Transparent,
                        )
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                        .padding(vertical = 8.dp),  // Adjust padding as needed
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Job Details", fontSize = smallTextSize)
                }
            }
        }
    }
}
@Composable
fun DeclinedMySubmissionsTradesmanItem(myJob: JobApplicationData, navController: NavController, ) {
    val windowSize = rememberWindowSizeClass()

    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 18.sp
        WindowType.MEDIUM -> 20.sp
        WindowType.LARGE -> 22.sp
    }
    val taskTextSize = when (windowSize.width) {
        WindowType.SMALL -> 14.sp
        WindowType.MEDIUM -> 16.sp
        WindowType.LARGE -> 18.sp
    }
    val smallTextSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }
    val createdAt = ViewModelSetups.formatDateTime(myJob.createdAt)
    val deadline = ViewModelSetups.formatDateTime(myJob.jobDeadline)
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 400.dp to 270.dp
        WindowType.MEDIUM -> 410.dp to 280.dp
        WindowType.LARGE -> 420.dp to 290.dp
    }
    var jobType = myJob.jobType

    if (jobType == "Electrical_work") {
        jobType = "Electrical Work"
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("tradesmanapplication/${myJob.id}/${myJob.jobId}/${myJob.status}") } ,
        shape = RoundedCornerShape(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Tradesman image
                    AsyncImage(
                        model = myJob.clientProfilePicture, // Use URL here
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(62.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    // Tradesman details
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = myJob.clientFullname,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Service: $jobType",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Applied at: $createdAt",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Due-Date: $deadline",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Address: ${myJob.jobAddress}, Pangasinan",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Space out the buttons
                ) {
                    Box(
                        modifier = Modifier
                            .clickable {
                                navController.navigate("tradesmanapply/${myJob.jobId}")
                            }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Apply Again", fontSize = smallTextSize)
                    }
                    Box(
                        modifier = Modifier
                            .clickable { navController.navigate("tradesmanapplication/${myJob.id}/${myJob.jobId}/${myJob.status}") }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color(0xFFECAB1E), shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Job Details", color = Color(0xFFECAB1E), fontSize = smallTextSize)
                    }
                }
            }
        }
    }
}
@Composable
fun CompletedMySubmissionsTradesmanItem(myJob: JobApplicationData, navController: NavController) {
    val createdAt = ViewModelSetups.formatDateTime(myJob.createdAt)
    val deadline = ViewModelSetups.formatDateTime(myJob.jobDeadline)
    val windowSize = rememberWindowSizeClass()

    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 18.sp
        WindowType.MEDIUM -> 20.sp
        WindowType.LARGE -> 22.sp
    }
    val taskTextSize = when (windowSize.width) {
        WindowType.SMALL -> 14.sp
        WindowType.MEDIUM -> 16.sp
        WindowType.LARGE -> 18.sp
    }
    val smallTextSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 400.dp to 270.dp
        WindowType.MEDIUM -> 410.dp to 280.dp
        WindowType.LARGE -> 420.dp to 290.dp
    }
    var jobType = myJob.jobType

    if (jobType == "Electrical_work") {
        jobType = "Electrical Work"
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("tradesmanapplication/${myJob.id}/${myJob.jobId}/${myJob.status}") },
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Tradesman image
                    AsyncImage(
                        model = myJob.clientProfilePicture, // Use URL here
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(62.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    // Tradesman details
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = myJob.clientFullname,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Service: $jobType",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Applied on: $createdAt",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Due-Date: $deadline",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Address: ${myJob.jobAddress}",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("tradesmanapplication/${myJob.id}/${myJob.jobId}/${myJob.status}") }
                        .background(
                            color = Color.Transparent,
                        )
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                        .padding(vertical = 8.dp),  // Adjust padding as needed
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Job Details", fontSize = smallTextSize)

                }
            }
        }
    }
}


@Composable
fun CancelledMySubmissionsTradesmanItem(myJob: JobApplicationData, navController: NavController) {
    val windowSize = rememberWindowSizeClass()
    val updatedAt = ViewModelSetups.formatDateTime(myJob.jobDateStatus)
    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 18.sp
        WindowType.MEDIUM -> 20.sp
        WindowType.LARGE -> 22.sp
    }
    val taskTextSize = when (windowSize.width) {
        WindowType.SMALL -> 14.sp
        WindowType.MEDIUM -> 16.sp
        WindowType.LARGE -> 18.sp
    }
    val smallTextSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 400.dp to 270.dp
        WindowType.MEDIUM -> 410.dp to 280.dp
        WindowType.LARGE -> 420.dp to 290.dp
    }
    var jobType = myJob.jobType

    if (jobType == "Electrical_work") {
        jobType = "Electrical Work"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("tradesmanapplication/${myJob.id}/${myJob.jobId}/${myJob.status}") }
,        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Tradesman image
                    AsyncImage(
                        model = myJob.clientProfilePicture, // Use URL here
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(62.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    // Tradesman details
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = myJob.clientFullname,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Service: $jobType",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Cancelled on: $updatedAt",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Cancelled reason: ${myJob.cancelledReason}",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Cancelled by: ${myJob.cancelledBy}",
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("tradesmanapply/${myJob.jobId}") }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                            .padding(vertical = 8.dp),  // Adjust padding as needed
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Apply Again", fontSize = smallTextSize)
                    }
                }

            }
        }
    }
}
