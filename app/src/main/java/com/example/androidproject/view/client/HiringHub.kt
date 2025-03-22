package com.example.androidproject.view.client

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.ViewModelSetups
import com.example.androidproject.data.WebSocketManager
import com.example.androidproject.data.preferences.AccountManager
import com.example.androidproject.model.JobApplicantData
import com.example.androidproject.model.client.GetClientsBooking
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.extras.LoadingUI
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.viewmodel.bookings.GetClientBookingViewModel
import com.example.androidproject.viewmodel.bookings.UpdateBookingTradesmanViewModel
import com.example.androidproject.viewmodel.job_application.PutJobApplicationStatusViewModel
import com.example.androidproject.viewmodel.job_application.ViewJobApplicationViewModel
import com.example.androidproject.viewmodel.job_application.client.GetMyJobApplicantsViewModel
import kotlinx.coroutines.delay
import java.sql.Types.NULL
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun BookingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    getClientsBooking: GetClientBookingViewModel,
    updateBookingTradesmanViewModel: UpdateBookingTradesmanViewModel,
    getMyJobApplicants: GetMyJobApplicantsViewModel,
    viewJobsApplication: ViewJobApplicationViewModel,
    putJobApplicationStatus: PutJobApplicationStatusViewModel,
    initialTabIndex: Int = 0,
    initialSection: Int = 0
) {
    Log.i("Screen", "BookingsScreen")
    val windowSize = rememberWindowSizeClass()
    val textSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }

    var selectedTabIndex by remember { mutableIntStateOf(initialTabIndex) }
    var selectedSection by remember { mutableIntStateOf(initialSection) }

    val myJobsTabs = listOf("All", "Pending", "Declined", "Active", "Completed", "Cancelled")
    val myApplicantsTabs = listOf("All", "Pending", "Declined", "Active", "Completed", "Cancelled")
    val tabTitles = if (selectedSection == 0) myJobsTabs else myApplicantsTabs

    fun checkNetworkConnectivity(connectivityManager: ConnectivityManager): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }
    val context = LocalContext.current
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val isConnected = remember { mutableStateOf(checkNetworkConnectivity(connectivityManager)) }
    var showLoading by remember { mutableStateOf(false) } // State to control LoadingUI visibility


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                Modifier.fillMaxWidth().height(70.dp).shadow(0.2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 25.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Bookings",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications Icon",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { navController.navigate("notification") }
                    )
                }
            }

            BookingsTopSection(navController, selectedSection) { section ->
                selectedSection = section
                selectedTabIndex = 0
            }

            Column(modifier = Modifier.fillMaxSize()) {
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.fillMaxWidth(),
                    edgePadding = 5.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = Color(0xFF3CC0B0)
                        )
                    }
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    title,
                                    fontSize = textSize,
                                    color = if (selectedTabIndex == index) Color(0xFF3CC0B0) else Color.Black
                                )
                            },
                            modifier = Modifier.background(Color.White),
                        )
                    }
                }
                        if (!isConnected.value) {
                            if (showLoading) {
                                LoadingUI()
                                LaunchedEffect(Unit) {
                                    delay(1500) // Show LoadingUI for 1.5 seconds
                                    isConnected.value = checkNetworkConnectivity(connectivityManager)
                                    showLoading = false // Hide LoadingUI after delay
                                }
                            } else {
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
                                        Box(
                                            modifier = Modifier
                                                .clickable {
                                                    showLoading = true // Show LoadingUI on retry
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
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFFD9D9D9))
                                    .padding(16.dp)
                            ) {
                                when (selectedSection) {
                                    0 -> when (selectedTabIndex) {
                                        0 -> AllBookingsContent(getClientsBooking, navController)
                                        1 -> PendingBookingsContent(getClientsBooking, navController, updateBookingTradesmanViewModel)
                                        2 -> DeclinedBookingsContent(getClientsBooking, navController)
                                        3 -> ActiveBookingsContent(getClientsBooking, navController, updateBookingTradesmanViewModel)
                                        4 -> CompletedBookingsContent(getClientsBooking, navController)
                                        5 -> CancelledBookingsContent(getClientsBooking, navController)
                                    }
                                    1 -> when (selectedTabIndex) {
                                        0 -> AllApplicantsContent(getMyJobApplicants, viewJobsApplication)
                                        1 -> PendingApplicantsContent(navController, getMyJobApplicants, viewJobsApplication, putJobApplicationStatus)
                                        2 -> DeclinedApplicantsContent(navController, getMyJobApplicants, viewJobsApplication)
                                        3 -> ActiveApplicantsContent(navController, getMyJobApplicants, viewJobsApplication, putJobApplicationStatus)
                                        4 -> CompletedApplicantsContent(navController, getMyJobApplicants, viewJobsApplication)
                                        5 -> CancelledApplicantsContent(navController, getMyJobApplicants, viewJobsApplication)
                                    }
                                }
                            }
                        }
            }
        }
    }
}

@Composable
fun BookingsTopSection(navController: NavController, selectedSection: Int, onSectionSelected: (Int) -> Unit) {
    val windowSize = rememberWindowSizeClass()

    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 18.sp
        WindowType.MEDIUM -> 20.sp
        WindowType.LARGE -> 22.sp
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left-aligned clickable text with box
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(if (selectedSection == 0) Color(0xFF3CC0B0  ) else (Color.Transparent))
                .weight(1f)
                .clickable {
                    onSectionSelected(0)
                },
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "My Clients",
                fontSize = nameTextSize,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = if (selectedSection == 0) Color.White else Color.Black
            )

        }

        // Right-aligned clickable text with box
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(if (selectedSection == 1) Color(0xFF3CC0B0  ) else (Color.Transparent))
                .weight(1f)
                .clickable {
                    onSectionSelected(1)
                },
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "My Applicants",
                fontSize = nameTextSize,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = if (selectedSection == 1) Color.White else Color.Black
            )

        }
    }
}


@Composable
fun AllBookingsContent(getClientsBooking: GetClientBookingViewModel,navController: NavController) {
    val allBooking = getClientsBooking.ClientBookingPagingData.collectAsLazyPagingItems()

    // Example: Call this after adding a new resume
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
            // Display "No Pending Booking" when the list is empty
            Text(
                text = "No Clients",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .size(420.dp)
                    .background(Color(0xFFD9D9D9))

                ,
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {

                items(allBooking.itemCount) { index ->
                    val clientbooking = allBooking[index]
                    if (clientbooking != null) {
                        AllItem(clientbooking,navController)
                        Log.d("ALLBOOKINGS", "AllBookingsContent: $clientbooking")
                    }
                }
            }
        }
    }

}


@Composable
fun PendingBookingsContent(getClientBooking: GetClientBookingViewModel, navController:NavController,updateBookingTradesmanViewModel: UpdateBookingTradesmanViewModel) {
    val pending = getClientBooking.ClientBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        pending.refresh()
    }
    // Filter the bookings to get only those with status "Pending"
    val pendingBookings = pending.itemSnapshotList.items.filter { it.bookingStatus == "Pending" }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9)),
        contentAlignment = Alignment.Center // Center the content
    ) {
        if (pendingBookings.isEmpty()) {
            // Display "No Pending Booking" when the list is empty
            Text(
                text = "No Pending Clients ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else {
            // Display the LazyColumn when there are pending bookings
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(pendingBookings.size) { index ->
                    val pendingBooking = pendingBookings[index]
                    PendingItem(pendingBooking, navController,updateBookingTradesmanViewModel)
                }
            }
        }
    }
}
@Composable
fun DeclinedBookingsContent(getClientBooking: GetClientBookingViewModel,navController: NavController) {
    val declined = getClientBooking.ClientBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        declined.refresh()
    }

    // Filter the bookings to get only those with status "Declined"
    val declinedBookings = declined.itemSnapshotList.items.filter { it.bookingStatus == "Declined" }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9)),
        contentAlignment = Alignment.Center // Center the content
    ) {
        if (declinedBookings.isEmpty()) {
            // Display "No Pending Booking" when the list is empty
            Text(
                text = "No Declined Clients ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .size(420.dp)
                    .background(Color(0xFFD9D9D9))
                ,
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(declinedBookings.size) { index ->
                    val declinedbooking = declinedBookings[index]
                    DeclinedItem(declinedbooking, navController )
                }
            }
        }
    }




}

@Composable
fun ActiveBookingsContent(getClientBooking: GetClientBookingViewModel,navController:NavController,updateWorkStatusViewModel:UpdateBookingTradesmanViewModel) {
    val active = getClientBooking.ClientBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        active.refresh()
    }
    // Filter the bookings to get only those with status "Active"
    val activeBooking = active.itemSnapshotList.items.filter { it.bookingStatus == "Active" }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9)),
        contentAlignment = Alignment.Center // Center the content
    ) {
        if (activeBooking.isEmpty()) {
            // Display "No Pending Booking" when the list is empty
            Text(
                text = "No Active Clients",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .size(420.dp)
                    .background(Color(0xFFD9D9D9))
                ,
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(activeBooking.size) { index ->
                    val activebooking = activeBooking[index]
                    ActiveItems(activebooking,navController,updateWorkStatusViewModel)
                }
            }
        }
    }
}

@Composable
fun CompletedBookingsContent(getClientBooking: GetClientBookingViewModel,navController: NavController) {
    val completed = getClientBooking.ClientBookingPagingData.collectAsLazyPagingItems()
    LaunchedEffect(Unit) {
        completed.refresh()
    }
    // Filter the bookings to get only those with status "Completed"
    val completedBookings = completed.itemSnapshotList.items.filter { it.bookingStatus == "Completed" }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9)),
        contentAlignment = Alignment.Center // Center the content
    ) {
        if (completedBookings.isEmpty()) {
            // Display "No Pending Booking" when the list is empty
            Text(
                text = "No Completed Clients",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .size(420.dp)
                    .background(Color(0xFFD9D9D9))

                ,
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(completedBookings.size) { index ->
                    val completedbooking = completedBookings[index]
                    CompletedItem(completedbooking, navController )
                }
            }
        }
    }


}
@Composable
fun CancelledBookingsContent(getClientBooking: GetClientBookingViewModel,navController: NavController) {
    val cancelled = getClientBooking.ClientBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        cancelled.refresh()
    }

    // Filter the bookings to get only those with status "Completed"
    val completedBookings = cancelled.itemSnapshotList.items.filter { it.bookingStatus == "Cancelled" }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9)),
        contentAlignment = Alignment.Center // Center the content
    ) {
        if (completedBookings.isEmpty()) {
            // Display "No Pending Booking" when the list is empty
            Text(
                text = "No Cancelled Clients",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .size(420.dp)
                    .background(Color(0xFFD9D9D9))
                ,
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(completedBookings.size) { index ->
                    val cancelledbooking = completedBookings[index]
                    CancelledItem(cancelledbooking, navController )
                }
            }
        }
    }

}





//Design For Items
@Composable
fun AllItem(allBooking : GetClientsBooking,navController: NavController) {
    val bookingDate = ViewModelSetups.formatDateTime(allBooking.bookingDate)

    val windowSize = rememberWindowSizeClass()

    // Responsive dimensions
    val cardWidth = when (windowSize.width) {
        WindowType.SMALL -> 380.dp
        WindowType.MEDIUM -> 390.dp
        WindowType.LARGE -> 400.dp
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
        "pending" -> Color(0xFFECAB1E)
        "cancelled", "declined" -> Color.Red
        "completed" -> Color.Blue
        "active" -> Color.Green
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .width(cardWidth)
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Profile Image
            AsyncImage(
                model = allBooking.tradesmanProfile,
                contentDescription = "Tradesman Image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            // Main Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                // Header Row (Name and Status)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = allBooking.tradesmanFullName,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = nameTextSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = allBooking.bookingStatus,
                        fontSize = smallTextSize,
                        fontWeight = FontWeight.Bold,
                        color = statusColor,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                // Task Type
                Text(
                    text = allBooking.taskType.replace("_", " "),
                    color = Color.Black,
                    fontSize = taskTextSize,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Rating and Fee Row
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Fee Box
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "P${allBooking.workFee}/hr",
                            fontSize = smallTextSize,
                            color = Color.Black
                        )
                    }

                    // Rating Box
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color(0xFFFFA500),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (allBooking.ratings == 0f) "0" else String.format("%.1f", allBooking.ratings),
                                fontSize = smallTextSize,
                                color = Color.Black
                            )
                        }
                    }
                }

                // Date Information
                Column(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = "Weekdays Selected",
                        color = Color.Black,
                        fontSize = smallTextSize,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = bookingDate,
                        color = Color.Gray,
                        fontSize = smallTextSize,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}


//Design for activeItems
@Composable
fun ActiveItems(activeBooking: GetClientsBooking,navController:NavController,updateWorkStatusViewModel:UpdateBookingTradesmanViewModel) {
    val updateWorkState by updateWorkStatusViewModel.workStatusState.collectAsState()
    var showCompletedDialog by remember { mutableStateOf(false) }
    var showCancelledDialog by remember { mutableStateOf(false) }
    var showJobCompletedDialog by remember { mutableStateOf(false) }
    var showCancelledReason by remember { mutableStateOf(false) }
    var buttonSubmit by remember { mutableStateOf(false) }
    var cancelReason by remember { mutableStateOf("") }
    val  context = LocalContext.current
    val windowSize = rememberWindowSizeClass()
    val cardWidth = when (windowSize.width) {
        WindowType.SMALL -> 380.dp
        WindowType.MEDIUM -> 390.dp
        WindowType.LARGE -> 400.dp
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

    LaunchedEffect(updateWorkState) {
        when (val updateWorkStatusState= updateWorkState) {
            is UpdateBookingTradesmanViewModel.UpdateWorkStatus.Success -> {
                Toast.makeText(context, "Booking Successfully Completed", Toast.LENGTH_SHORT).show()
                updateWorkStatusViewModel.resetState()
                if(updateWorkStatusState.status == "Completed"){
                    // Navigate to the "profile" screen and clear the back stack
                    WebSocketManager.sendNotificationBookingToTradesman(
                        activeBooking.resumeId.toString(),
                        "A book job has been marked as completed!!",
                        "${AccountManager.getAccount()?.firstName + " " + AccountManager.getAccount()?.lastName} has marked the active book into completed."
                    )
                    navController.navigate("main_screen?selectedItem=1&selectedTab=4") {
                        navController.popBackStack()
                    }
                }else if(updateWorkStatusState.status == "Cancelled"){
                    // Navigate to the "profile" screen and clear the back stack
                    WebSocketManager.sendNotificationBookingToTradesman(
                        activeBooking.resumeId.toString(),
                        "An active booked job has been cancelled!",
                        "${AccountManager.getAccount()?.firstName + " " + AccountManager.getAccount()?.lastName} has cancelled the active service. due to $cancelReason"
                    )
                    navController.navigate("main_screen?selectedItem=1&selectedTab=5") {
                        navController.popBackStack()
                    }
                }

            }
            is UpdateBookingTradesmanViewModel.UpdateWorkStatus.Error -> {
                val errorMessage = updateWorkStatusState.message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                updateWorkStatusViewModel.resetState()
            }
            else -> Unit
        }
    }

    val bookingDate = ViewModelSetups.formatDateTime(activeBooking.bookingDate)

    Card(
        modifier = Modifier
            .width(cardWidth)
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tradesman image
                AsyncImage(
                    model = activeBooking.tradesmanProfile,
                    contentDescription = "Tradesman Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )

                // Tradesman details
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = activeBooking.tradesmanFullName,
                        color = Color.Black,
                        fontWeight = FontWeight(500),
                        fontSize = nameTextSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = activeBooking.taskType.replace("_", " "),
                        color = Color.Black,
                        fontSize = taskTextSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "P${activeBooking.workFee}/hr",
                                fontSize = smallTextSize
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = Color(0xFFFFA500),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (activeBooking.ratings == 0f) "0" else String.format(
                                        "%.1f",
                                        activeBooking.ratings
                                    ),
                                    fontSize = smallTextSize
                                )
                            }
                        }
                    }
                    Text(
                        text = "Weekdays Selected",
                        color = Color.Black,
                        fontSize = taskTextSize,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = bookingDate,
                        color = Color.Gray,
                        fontSize = smallTextSize
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            ) {
                Button(
                    onClick = { showCancelledDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC51B1B),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel", fontSize = smallTextSize)
                }
                Spacer(Modifier.width(16.dp))
                Button(
                    onClick = {
                        showCompletedDialog = true
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF42C2AE),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)


                ) {
                    Text("Completed", fontSize = smallTextSize)
                }

            }
        }
    }
    if (showCompletedDialog) {
        AlertDialog(
            containerColor = Color.White,

            onDismissRequest = { showCompletedDialog = false },
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
                        text = "Completed Job",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                }
            },
            text = { Text("Once Completed, this job will be marked as Completed. Proceed?") },
            confirmButton = {
                Button(
                    onClick = {
                        showCompletedDialog = false
                        showJobCompletedDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42C2AE))
                ) {
                    Text("Confirm", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showCompletedDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }

    if (showCancelledDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showCancelledDialog = false },
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
                        showCancelledDialog = false
                        showCancelledReason = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Confirm", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showCancelledDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancel", color = Color.White)
                }
            },
        )
    }

    if (showJobCompletedDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showJobCompletedDialog = false },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_jobapproved_checked),
                    contentDescription = "Job Approve Icon",
                    modifier = Modifier.size(60.dp)
                )
            },
            title = { Text("Job Completed!") },
            text = { Text("Reach out to the client for more project details.") },
            confirmButton = {
                Button(
                    onClick = { showJobCompletedDialog = false
                        buttonSubmit = true
                        updateWorkStatusViewModel.updateWorkStatus(
                            "Completed",
                            "",
                            activeBooking.id
                        )

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42C2AE)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("OK", color = Color.White)
                }
            }
        )
    }

    if (showCancelledReason) {
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
            containerColor = Color.White,
            onDismissRequest = { showCancelledReason = false },
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
                        buttonSubmit = true
                        showCancelledReason = false
                        selectedReason?.let {
                            updateWorkStatusViewModel.updateWorkStatus(
                                "Cancelled",
                                it,
                                activeBooking.id
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
fun PendingItem(pendingBooking : GetClientsBooking, navController:NavController,updateBookingTradesmanViewModel: UpdateBookingTradesmanViewModel) {
    val bookingDate = ViewModelSetups.formatDateTime(pendingBooking.bookingDate)
    val updateBookingState by updateBookingTradesmanViewModel.workStatusState.collectAsState()
    var showCancelDialog by remember { mutableStateOf(false) }
    var showCancelReasons by remember { mutableStateOf(false) }
    var cancelReason by remember { mutableStateOf("") }
    LaunchedEffect(updateBookingState) {
        when (updateBookingState) {
            is UpdateBookingTradesmanViewModel.UpdateWorkStatus.Success -> {
                WebSocketManager.sendNotificationBookingToTradesman(
                    pendingBooking.resumeId.toString(),
                    "A client has cancelled a booking!",
                    "${AccountManager.getAccount()?.firstName + " " + AccountManager.getAccount()?.lastName} has cancelled the booking request, due to $cancelReason."
                )
                Toast.makeText(navController.context, "Application cancelled", Toast.LENGTH_SHORT).show()
                updateBookingTradesmanViewModel.resetState()
                navController.navigate("main_screen?selectedItem=1&selectedTab=5") {
                    navController.popBackStack()
                }

            }
            else -> {}
        }
    }

    val windowSize = rememberWindowSizeClass()
    val cardWidth = when (windowSize.width) {
        WindowType.SMALL -> 380.dp
        WindowType.MEDIUM -> 390.dp
        WindowType.LARGE -> 400.dp
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
            .width(cardWidth)
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tradesman image
                AsyncImage(
                    model = pendingBooking.tradesmanProfile,
                    contentDescription = "Tradesman Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )

                // Tradesman details
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = pendingBooking.tradesmanFullName,
                        color = Color.Black,
                        fontWeight = FontWeight(500),
                        fontSize = nameTextSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = pendingBooking.taskType.replace("_", " "),
                        color = Color.Black,
                        fontSize = taskTextSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "P${pendingBooking.workFee}/hr",
                                fontSize = smallTextSize
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = Color(0xFFFFA500),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (pendingBooking.ratings == 0f) "0" else String.format("%.1f", pendingBooking.ratings),
                                    fontSize = smallTextSize
                                )
                            }
                        }
                    }
                    Text(
                        text = "Weekdays Selected",
                        color = Color.Black,
                        fontSize = taskTextSize,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = bookingDate,
                        color = Color.Gray,
                        fontSize = smallTextSize
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = { showCancelDialog = true},
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.Gray),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel", fontSize = smallTextSize, color = Color.Black)
                }
                Spacer(Modifier.width(16.dp))
                OutlinedButton(
                    onClick = { navController.navigate("bookingdetails/${pendingBooking.resumeId}") },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFECAB1E)),
                    modifier = Modifier.weight(1f),

                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFECAB1E))
                ) {
                    Text("Booking Details", fontSize = smallTextSize)
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
                                updateBookingTradesmanViewModel.updateWorkStatus("Cancelled", it, pendingBooking.id)
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
}
@Composable
fun DeclinedItem(declineBooking: GetClientsBooking, navController:NavController) {
    val bookingDate = ViewModelSetups.formatDateTime(declineBooking.bookingDate)
    val windowSize = rememberWindowSizeClass()
    val cardWidth = when (windowSize.width) {
        WindowType.SMALL -> 380.dp
        WindowType.MEDIUM -> 390.dp
        WindowType.LARGE -> 400.dp
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
            .width(cardWidth)
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tradesman image
                AsyncImage( // Changed from Image to AsyncImage for consistency
                    model = declineBooking.tradesmanProfile, // Use dynamic URL instead of static resource
                    contentDescription = "Tradesman Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )

                // Tradesman details
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = declineBooking.tradesmanFullName,
                        color = Color.Black,
                        fontWeight = FontWeight(500),
                        fontSize = nameTextSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = declineBooking.taskType.replace("_", " "),
                        color = Color.Black,
                        fontSize = taskTextSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "P${declineBooking.workFee}/hr",
                                fontSize = smallTextSize
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = Color(0xFFFFA500),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (declineBooking.ratings == 0f) "0" else String.format(
                                        "%.1f",
                                        declineBooking.ratings
                                    ),
                                    fontSize = smallTextSize
                                )
                            }
                        }
                    }
                    Text(
                        text = "Weekdays Selected",
                        color = Color.Black,
                        fontSize = taskTextSize,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = bookingDate,
                        color = Color.Gray,
                        fontSize = smallTextSize
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = { navController.navigate("cancelleddetails") },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.Gray),
                    modifier = Modifier.weight(1f),

                    ) {
                    Text("Declined Details", fontSize = smallTextSize, color = Color.Black)
                }
                Spacer(Modifier.width(16.dp))
                OutlinedButton(
                    onClick = { navController.navigate("booknow/${declineBooking.resumeId}") },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFECAB1E)),
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFECAB1E))

                ) {
                    Text("Book Again", fontSize = smallTextSize, color = Color(0xFFECAB1E) )
                }
            }
        }
    }
}
@Composable
fun CompletedItem(completedBooking: GetClientsBooking, navController:NavController) {
    val bookingDate = ViewModelSetups.formatDateTime(completedBooking.bookingDate)
    val windowSize = rememberWindowSizeClass()
    val cardWidth = when (windowSize.width) {
        WindowType.SMALL -> 380.dp
        WindowType.MEDIUM -> 390.dp
        WindowType.LARGE -> 400.dp
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
            .width(cardWidth)
            .wrapContentHeight(), // Let content determine height
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp) // Increased padding for better spacing
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tradesman image
                AsyncImage(
                    model = completedBooking.tradesmanProfile,
                    contentDescription = "Tradesman Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape) // Modern touch
                )

                // Tradesman details
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = completedBooking.tradesmanFullName,
                        color = Color.Black,
                        fontWeight = FontWeight(500),
                        fontSize = nameTextSize,
                        maxLines = 1, // Prevent overflow
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = completedBooking.taskType.replace("_", " "),
                        color = Color.Black,
                        fontSize = taskTextSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "P${completedBooking.workFee}/hr",
                                fontSize = smallTextSize
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = Color(0xFFFFA500),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (completedBooking.ratings == 0f) "0" else String.format(
                                        "%.1f",
                                        completedBooking.ratings
                                    ),
                                    fontSize = smallTextSize
                                )
                            }
                        }
                    }
                    Text(
                        text = "Weekdays Selected",
                        color = Color.Black,
                        fontSize = taskTextSize,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = bookingDate,
                        color = Color.Gray,
                        fontSize = smallTextSize
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = { navController.navigate("booknow/${completedBooking.resumeId}") },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.Gray),
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Book Again", fontSize = smallTextSize, color = Color.Black)
                }
                Spacer(Modifier.width(16.dp))
                OutlinedButton(
                    onClick = { navController.navigate("rateandreviews/${completedBooking.resumeId}/${completedBooking.tradesmanId}/${completedBooking.id}") },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFECAB1E)),
                    modifier = Modifier.weight(1f),

                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFECAB1E))
                ) {
                    Text("Rate", fontSize = smallTextSize, color =  Color(0xFFECAB1E) )
                }
            }
        }
    }
}

@Composable
fun CancelledItem(cancelledBooking: GetClientsBooking, navController:NavController) {
    val bookingDate = ViewModelSetups.formatDateTime(cancelledBooking.bookingDate)
    val windowSize = rememberWindowSizeClass()
    val cardWidth = when (windowSize.width) {
        WindowType.SMALL -> 380.dp
        WindowType.MEDIUM -> 390.dp
        WindowType.LARGE -> 400.dp
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
            .width(cardWidth)
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tradesman image
                AsyncImage(
                    model = cancelledBooking.tradesmanProfile,
                    contentDescription = "Tradesman Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )

                // Tradesman details
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = cancelledBooking.tradesmanFullName,
                        color = Color.Black,
                        fontWeight = FontWeight(500),
                        fontSize = nameTextSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = cancelledBooking.taskType.replace("_", " "),
                        color = Color.Black,
                        fontSize = taskTextSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "P${cancelledBooking.workFee}/hr",
                                fontSize = smallTextSize
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = Color(0xFFFFA500),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (cancelledBooking.ratings == 0f) "0" else String.format(
                                        "%.1f",
                                        cancelledBooking.ratings
                                    ),
                                    fontSize = smallTextSize
                                )
                            }
                        }
                    }
                    Text(
                        text = "Weekdays Selected",
                        color = Color.Black,
                        fontSize = taskTextSize,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = bookingDate,
                        color = Color.Gray,
                        fontSize = smallTextSize
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = { navController.navigate("cancelleddetails") },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.Gray),
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Cancelled Details", fontSize = smallTextSize, color = Color.Black)
                }
                Spacer(Modifier.width( 16.dp))
                OutlinedButton(
                    onClick = { navController.navigate("booknow/${cancelledBooking.resumeId}") },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFECAB1E)),
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFECAB1E))
                ) {
                    Text("Book Again", fontSize = smallTextSize)
                }
            }
        }
    }
}


//MY APPLICANTS
//MY APPLICANTS
@Composable
fun AllApplicantsContent(getMyJobApplicant: GetMyJobApplicantsViewModel, viewJobsApplication: ViewJobApplicationViewModel) {
    val myJobs = getMyJobApplicant.jobApplicantsPagingData.collectAsLazyPagingItems()

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
            // Display "No Pending Booking" when the list is empty
            Text(
                text = "No Pending Applicants",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .size(420.dp)
                    .background(Color(0xFFD9D9D9)),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(myJobs.itemCount) { index ->
                    val myJob = myJobs[index]
                    if (myJob != null) {
                        AllApplicantsItem(myJob)
                    }

                }
            }
        }
    }

}


@Composable
fun PendingApplicantsContent(navController: NavController, getMyJobApplicant: GetMyJobApplicantsViewModel, viewJobsApplication: ViewJobApplicationViewModel, putJobApplicationStatus: PutJobApplicationStatusViewModel) {
    val myJob = getMyJobApplicant.jobApplicantsPagingData.collectAsLazyPagingItems()

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
            // Display "No Pending Booking" when the list is empty
            Text(
                text = "No Pending Applicants",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .size(420.dp)
                    .background(Color(0xFFD9D9D9)),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(pendingApplication.size) { index ->
                    val pendingJobs = pendingApplication[index]
                    PendingApplicantsItem(pendingJobs, navController, putJobApplicationStatus)
                }
            }
        }
    }
}
@Composable
fun DeclinedApplicantsContent(navController: NavController, getMyJobApplicant: GetMyJobApplicantsViewModel, viewJobsApplication: ViewJobApplicationViewModel) {
    val myJob = getMyJobApplicant.jobApplicantsPagingData.collectAsLazyPagingItems()

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
            // Display "No Pending Booking" when the list is empty
            Text(
                text = "No Declined Applicants",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .size(420.dp)
                    .background(Color(0xFFD9D9D9)),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(declinedApplication.size) { index ->
                    val declineJob = declinedApplication[index]
                    DeclinedApplicantsItem(declineJob, navController)
                }
            }
        }
    }
}

@Composable
fun ActiveApplicantsContent(navController: NavController, getMyJobApplicant: GetMyJobApplicantsViewModel, viewJobsApplication: ViewJobApplicationViewModel, putJobApplicationStatus: PutJobApplicationStatusViewModel) {
    val myJob = getMyJobApplicant.jobApplicantsPagingData.collectAsLazyPagingItems()

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
            // Display "No Pending Booking" when the list is empty
            Text(
                text = "No Active Applicants",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .size(420.dp)
                    .background(Color(0xFFD9D9D9)),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(activeApplication.size) { index ->
                    val activeJobs = activeApplication[index]
                    ActiveApplicantsItem(activeJobs, navController, putJobApplicationStatus)
                }
            }
        }
    }
}

@Composable
fun CompletedApplicantsContent(navController: NavController, getMyJobApplicant: GetMyJobApplicantsViewModel, viewJobsApplication: ViewJobApplicationViewModel) {
    val myJob = getMyJobApplicant.jobApplicantsPagingData.collectAsLazyPagingItems()

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

            // Display "No Pending Booking" when the list is empty
            Text(
                text = "No Completed Applicants",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .size(420.dp)
                    .background(Color(0xFFD9D9D9)),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(completedApplication.size) { index ->
                    val completedJobs = completedApplication[index]
                    CompletedApplicantsItem(completedJobs, navController)
                }
            }
        }
    }


}
@Composable
fun CancelledApplicantsContent(navController: NavController, getMyJobApplicant: GetMyJobApplicantsViewModel, viewJobsApplication: ViewJobApplicationViewModel) {
    val myJob = getMyJobApplicant.jobApplicantsPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        myJob.refresh()
    }

    val cancelledApplication = myJob.itemSnapshotList.items.filter { it.status == "Cancelled" }

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9)),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(cancelledApplication.size) { index ->
            val cancelledJobs = cancelledApplication[index]
            CancelledApplicantsItem(cancelledJobs, navController )
        }
    }
}



//Design For Items
@Composable
fun AllApplicantsItem(myJob: JobApplicantData) {
    var jobType = myJob.jobType
    if (jobType == "Electrical_work") {
        jobType = "Electrical Work"
    }
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 400.dp to 190.dp
        WindowType.MEDIUM -> 410.dp to 200.dp
        WindowType.LARGE -> 420.dp to 210.dp
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
    val statusColor = when (myJob.status.lowercase()) {
        "pending" -> Color(0xFFECAB1E)
        "cancelled", "declined" -> Color.Red
        "completed" -> Color.Blue
        "active" -> Color.Green
        else -> Color.Gray // Default color
    }
    Card(
        modifier = Modifier
            .size(cardHeight.first, cardHeight.second),
        shape = RoundedCornerShape(8.dp),
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
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = myJob.tradesmanFullname,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = nameTextSize
                        )
                        Text(text = myJob.status, fontSize = taskTextSize, color = statusColor)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Service: $jobType",
                        color = Color.Black,
                        fontSize = taskTextSize
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Applied on ${myJob.createdAt}",
                        color = Color.Black,
                        fontSize = taskTextSize
                    )
                    Text(
                        text = "Submission Deadline: ${myJob.jobDeadline}",
                        color = Color.Black,
                        fontSize = taskTextSize
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Address: ${myJob.jobAddress}",
                        color = Color.Black,
                        fontSize = taskTextSize
                    )
                }
            }
        }
    }
}



@Composable
fun PendingApplicantsItem(myJob: JobApplicantData, navController: NavController, putJobApplicationStatus: PutJobApplicationStatusViewModel) {
    val putJob by putJobApplicationStatus.putJobApplicationState.collectAsState()
    var showApproveDialog by remember { mutableStateOf(false) }
    var showDeclineDialog by remember { mutableStateOf(false) }
    var showJobApproveDialog by remember { mutableStateOf(false) }
    var showDeclineReasons by remember { mutableStateOf(false) }
    var buttonSubmit by remember { mutableStateOf(false) }
    var reason by remember { mutableStateOf("") }
    var declined by remember { mutableStateOf(false) }

    when(val jobput =putJob) {

        is PutJobApplicationStatusViewModel.PutJobApplicationState.Loading -> {

        }

        is PutJobApplicationStatusViewModel.PutJobApplicationState.Error -> {

        }

        is PutJobApplicationStatusViewModel.PutJobApplicationState.Success -> {
            if (buttonSubmit) {
                Toast.makeText(LocalContext.current, "Job Application Updated", Toast.LENGTH_SHORT).show()
                putJobApplicationStatus.resetState()
                if(jobput.status == "Active"){
                    WebSocketManager.sendNotificationJobToTradesman(
                        myJob.resumeId.toString(),
                        "Job Application accepted!",
                        "Your job application from ${myJob.clientFullname} has been accepted!"
                    )

                    navController.navigate("main_screen?selectedItem=1&selectedTab=3&selectedSection=1") {
                        navController.popBackStack()
                    }
                }else if(jobput.status == "Declined"){
                    WebSocketManager.sendNotificationJobToTradesman(
                        myJob.resumeId.toString(),
                        "${myJob.clientFullname} Declined Your Job Application.",
                        "Your application was declined by ${myJob.clientFullname} due to $reason."
                    )
                    navController.navigate("main_screen?selectedItem=1&selectedTab=2&selectedSection=1") {
                        navController.popBackStack()
                    }
                }
            }
        }
        else -> Unit
    }


    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 400.dp to 250.dp
        WindowType.MEDIUM -> 410.dp to 260.dp
        WindowType.LARGE -> 420.dp to 270.dp
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
    Card(
        modifier = Modifier
            .size(cardHeight.first, cardHeight.second),
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
                            text = myJob.tradesmanFullname,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = nameTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Service: ${myJob.jobType}",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Applied at: ${myJob.createdAt}",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Text(
                            text = "Submission Deadline: ${myJob.jobDeadline}",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Address: ${myJob.jobAddress}",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                    }
                }
                Row(
                    Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clickable { showDeclineDialog = true }
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                                .padding(8.dp)
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Decline Job ", fontSize = taskTextSize)
                        }

                        Box(
                            modifier = Modifier
                                .clickable { showApproveDialog = true }
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(1.dp, Color(0xFFECAB1E), shape = RoundedCornerShape(12.dp))
                                .padding(8.dp)
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = " Accept Job",
                                color = Color(0xFFECAB1E),
                                fontSize = taskTextSize
                            )
                        }
                    }
                }

            }
        }
        if (showApproveDialog) {
            AlertDialog(
                containerColor = Color.White,

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
                            declined = false
                            showApproveDialog = false
                            showJobApproveDialog = true
                            putJobApplicationStatus.updateJobApplicationStatus(myJob.id, "Active", "")
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
                containerColor = Color.White,
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
                            Log.d("My jobs", myJob.id.toString())
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
                },
            )
        }

        if (showJobApproveDialog) {
            AlertDialog(
                containerColor = Color.White,
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
                        onClick = { showJobApproveDialog = false
                            buttonSubmit = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42C2AE)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
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
                containerColor = Color.White,
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
                            reason = selectedReason ?: ""
                            declined = true
                            buttonSubmit = true
                            showDeclineReasons = false
                            selectedReason?.let {
                                putJobApplicationStatus.updateJobApplicationStatus(
                                    myJob.id,
                                    "Declined",
                                    it
                                )
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

}
@Composable
fun ActiveApplicantsItem(myJob: JobApplicantData, navController: NavController, putJobApplicationStatus: PutJobApplicationStatusViewModel) {


    val putJobState by putJobApplicationStatus.putJobApplicationState.collectAsState()

    var showCompletedDialog by remember { mutableStateOf(false) }
    var showCancelledDialog by remember { mutableStateOf(false) }
    var showJobCompletedDialog by remember { mutableStateOf(false) }
    var showCancelledReason by remember { mutableStateOf(false) }
    var buttonSubmit by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var cancelledClicked by remember { mutableStateOf(false) }
    var completedClicked by remember { mutableStateOf(false) }
    var cancelReason by remember { mutableStateOf("") }

    LaunchedEffect(putJobState) {
        if (cancelledClicked || completedClicked) {
            when (val putJob = putJobState) {
                is PutJobApplicationStatusViewModel.PutJobApplicationState.Idle -> {

                }

                is PutJobApplicationStatusViewModel.PutJobApplicationState.Loading -> {

                }

                is PutJobApplicationStatusViewModel.PutJobApplicationState.Error -> {
                    Toast.makeText(context, putJob.message, Toast.LENGTH_SHORT).show()
                }

                is PutJobApplicationStatusViewModel.PutJobApplicationState.Success -> {
                    if (putJob.status =="Completed"){
                        WebSocketManager.sendNotificationJobToTradesman(
                            myJob.resumeId.toString(),
                            "Job was marked as completed!",
                            "${myJob.clientFullname} has marked your ${myJob.jobType} service as completed!"
                        )
                        Toast.makeText(context, "Job Completed", Toast.LENGTH_SHORT).show()
                        navController.navigate("main_screen?selectedItem=1&selectedTab=4&selectedSection=1") {
                            navController.popBackStack()
                        }
                    }else if(putJob.status == "Cancelled"){
                        WebSocketManager.sendNotificationJobToTradesman(
                            myJob.resumeId.toString(),
                            "An active job was cancelled!",
                            "${myJob.clientFullname} has cancelled ${myJob.jobType} service due to $cancelReason!"
                        )
                        Toast.makeText(context, "Job Cancelled", Toast.LENGTH_SHORT).show()
                        navController.navigate("main_screen?selectedItem=1&selectedTab=5&selectedSection=1") {
                            navController.popBackStack()
                        }
                    }

                    putJobApplicationStatus.resetState()
                }
            }
        }
    }

    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 400.dp to 250.dp
        WindowType.MEDIUM -> 410.dp to 260.dp
        WindowType.LARGE -> 420.dp to 270.dp
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
    Card(
        modifier = Modifier
            .size(cardHeight.first, cardHeight.second)
            .clickable { }, // Add implementation for click if needed
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
                            text = myJob.tradesmanFullname,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = nameTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Service: ${myJob.jobType}",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Applied on: ${myJob.createdAt}",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Text(
                            text = "Submission Deadline: ${myJob.jobDeadline}",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Address: ${myJob.jobAddress}",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                    }
                }

                Row(
                    Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clickable(indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) { showCancelledDialog = true }
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                                .padding(8.dp)
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Cancel Job ", fontSize = taskTextSize)
                        }
                        Box(
                            modifier = Modifier
                                .clickable(indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    showCompletedDialog = true

                                }
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(1.dp, Color(0xFFECAB1E), shape = RoundedCornerShape(12.dp))
                                .padding(8.dp)
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = "Completed",
                                color = Color(0xFFECAB1E),
                                fontSize = taskTextSize
                            )
                        }
                    }
                }
            }
        }
        if (showCompletedDialog) {
            AlertDialog(
                containerColor = Color.White,

                onDismissRequest = { showCompletedDialog = false },
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
                            text = "Completed Job",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.weight(1f)
                        )
                    }
                },
                text = { Text("Once Completed, this job will be marked as Completed. Proceed?") },
                confirmButton = {
                    Button(
                        onClick = {
                            showCompletedDialog = false
                            showJobCompletedDialog = true
                            putJobApplicationStatus.updateJobApplicationStatus(myJob.id, "Active", "")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42C2AE))
                    ) {
                        Text("Confirm", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showCompletedDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Cancel", color = Color.White)
                    }
                }
            )
        }

        if (showCancelledDialog) {
            AlertDialog(
                containerColor = Color.White,
                onDismissRequest = { showCancelledDialog = false },
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
                            showCancelledDialog = false
                            showCancelledReason = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Confirm", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showCancelledDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Cancel", color = Color.White)
                    }
                },
            )
        }

        if (showJobCompletedDialog) {
            AlertDialog(
                containerColor = Color.White,
                onDismissRequest = { showJobCompletedDialog = false },
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_jobapproved_checked),
                        contentDescription = "Job Approve Icon",
                        modifier = Modifier.size(60.dp)
                    )
                },
                title = { Text("Job Completed!") },
                text = { Text("Reach out to the client for more project details.") },
                confirmButton = {
                    Button(
                        onClick = { showJobCompletedDialog = false
                            buttonSubmit = true
                            putJobApplicationStatus.updateJobApplicationStatus(
                                myJob.id,
                                "Completed",
                                "",
                            )

                            completedClicked = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42C2AE)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("OK", color = Color.White)
                    }
                }
            )
        }

        if (showCancelledReason) {
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
                containerColor = Color.White,
                onDismissRequest = { showCancelledReason = false },
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
                            buttonSubmit = true
                            showCancelledReason = false
                            selectedReason?.let {
                                putJobApplicationStatus.updateJobApplicationStatus(
                                    myJob.id,
                                    "Cancelled",
                                    it
                                )
                                cancelReason = it
                            }
                            completedClicked = true

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
}

@Composable
fun DeclinedApplicantsItem(myJob: JobApplicantData, navController: NavController) {
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 400.dp to 250.dp
        WindowType.MEDIUM -> 410.dp to 260.dp
        WindowType.LARGE -> 420.dp to 270.dp
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
    Card(
        modifier = Modifier
            .size(cardHeight.first, cardHeight.second)
            .clickable { }, // Add implementation for click if needed
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
                            text = myJob.tradesmanFullname,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = nameTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Service: ${myJob.jobType}",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Applied on: ${myJob.createdAt}",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Text(
                            text = "Submission Deadline: ${myJob.jobDeadline}",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Address: ${myJob.jobAddress}",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                    }
                }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center // Aligns content to the end
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .clickable { navController.navigate("canceljobapplicationsdetails") }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )

                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                            .padding(
                                vertical = 8.dp,
                                horizontal = 36.dp
                            ), // Added horizontal padding for spacing
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Declined Details",
                            fontSize = taskTextSize,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CompletedApplicantsItem(myJob: JobApplicantData, navController: NavController) {
    var jobType = myJob.jobType

    if (jobType == "Electrical_work") {
        jobType = "Electrical Work"
    }
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 400.dp to 250.dp
        WindowType.MEDIUM -> 410.dp to 260.dp
        WindowType.LARGE -> 420.dp to 270.dp
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
    Card(
        modifier = Modifier
            .size(cardHeight.first, cardHeight.second)
            .clickable { }, // Add implementation for click if needed
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
                            text = myJob.tradesmanFullname,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = nameTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Service: $jobType",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Applied on: ${myJob.createdAt}",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Text(
                            text = "Submission Deadline: ${myJob.jobDeadline}",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Address: ${myJob.jobAddress}",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                    }
                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center // Aligns content to the end
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .clickable { }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )

                            .border(1.dp, Color(0xFFECAB1E), shape = RoundedCornerShape(12.dp))
                            .padding(
                                vertical = 8.dp,
                                horizontal = 56.dp
                            ), // Added horizontal padding for spacing
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Rate", fontSize = taskTextSize, color = Color(0xFFECAB1E))
                    }
                }
            }

        }
    }
}

@Composable
fun CancelledApplicantsItem(myJob: JobApplicantData, navController: NavController) {
    var jobType = myJob.jobType

    if (jobType == "Electrical_work") {
        jobType = "Electrical Work"
    }
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 400.dp to 250.dp
        WindowType.MEDIUM -> 410.dp to 260.dp
        WindowType.LARGE -> 420.dp to 270.dp
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
    Card(
        modifier = Modifier
            .size(cardHeight.first, cardHeight.second)
            .clickable { }, // Add implementation for click if needed
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
                            text = myJob.tradesmanFullname,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = nameTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Service: $jobType",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Applied on: ${myJob.createdAt}",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Text(
                            text = "Submission Deadline: ${myJob.jobDeadline}",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Address: ${myJob.jobAddress}",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                    }
                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center // Aligns content to the end
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .clickable { navController.navigate("canceljobapplicationsdetails") }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                            .padding(
                                vertical = 8.dp,
                                horizontal = 36.dp
                            ), // Added horizontal padding for spacing
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Cancelled Details", fontSize = taskTextSize, color =  Color.Black)
                    }
                }
            }
        }
    }
}
