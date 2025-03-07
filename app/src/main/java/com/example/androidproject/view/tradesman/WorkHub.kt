package com.example.androidproject.view.tradesman

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
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
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.ViewModelSetups
import com.example.androidproject.model.JobApplicationData
import com.example.androidproject.model.client.GetTradesmanBooking
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.view.theme.myGradient3
import com.example.androidproject.viewmodel.job_application.PutJobApplicationStatusViewModel
import com.example.androidproject.viewmodel.bookings.GetTradesmanBookingViewModel
import com.example.androidproject.viewmodel.bookings.UpdateBookingClientViewModel
import com.example.androidproject.viewmodel.bookings.UpdateBookingTradesmanViewModel
import com.example.androidproject.viewmodel.factories.bookings.UpdateBookingClientViewModelFactory
import com.example.androidproject.viewmodel.job_application.ViewJobApplicationViewModel
import com.example.androidproject.viewmodel.job_application.tradesman.GetMyJobApplicationViewModel
import java.sql.Types.NULL

@Composable
fun BookingsTradesman(modifier: Modifier = Modifier, navController: NavController, updateBookingClientViewModel: UpdateBookingClientViewModel, getMyJobApplications: GetMyJobApplicationViewModel, getTradesmanBooking: GetTradesmanBookingViewModel, putJobApplicationStatusViewModel: PutJobApplicationStatusViewModel, viewJobsApplication: ViewJobApplicationViewModel, initialTabIndex: Int = 0 ) {// Default to 0 if not provided
    val windowSize = rememberWindowSizeClass()
    val textSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }


    var selectedTabIndex by remember { mutableIntStateOf(initialTabIndex) } // Use initialTabIndex
    var selectedSection by remember { mutableStateOf("My Jobs") }

    // Define tab titles based on selected section
    val myJobsTabs = listOf("All", "Pending", "Declined", "Active", "Completed", "Cancelled")
    val myApplicantsTabs = listOf("All", "Pending", "Declined", "Active", "Completed", "Cancelled")
    val tabTitles = if (selectedSection == "My Jobs") myJobsTabs else myApplicantsTabs

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
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
                text = "Work Hub",
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
            // Right-aligned icons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications Icon",
                    tint = Color(0xFF3CC0B0),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        // Add this Divider **outside** the Box to create a visible separator
        Divider(
            color = Color.Black, // Change color if needed
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
                        selectedTabIndex = selectedTabIndex,
                        modifier = Modifier.fillMaxWidth(),
                        edgePadding = 5.dp
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = { Text(title, fontSize = textSize) },
                                modifier = Modifier.background(Color.White)
                            )
                        }
                    }

                    // Content changes based on the selected tab and section
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFD9D9D9))
                            .padding(8.dp)
                    ) {
                        when (selectedSection) {
                            "My Jobs" -> when (selectedTabIndex) {
                                0 -> AllBookingsTradesmanContent(getTradesmanBooking)
                                1 -> PendingBookingsTradesmanContent(navController,getTradesmanBooking,updateBookingClientViewModel)
                                2 -> DeclinedBookingsTradesmanContent(navController,getTradesmanBooking)
                                3 -> ActiveBookingsTradesmanContent(navController,getTradesmanBooking)
                                4 -> CompletedBookingsTradesmanContent(navController,getTradesmanBooking)
                                5 -> CancelledBookingsTradesmanContent(navController,getTradesmanBooking)
                            }

                            "My Submissions" -> when (selectedTabIndex) {
                                0 -> AllMySubmissionsTradesmanContent(getMyJobApplications)
                                1 -> PendingMySubmissionsTradesmanContent(navController, getMyJobApplications, putJobApplicationStatusViewModel, viewJobsApplication)
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
    }
}
@Composable
fun JobsTradesmanTopSection(navController: NavController, selectedSection: String, onSectionSelected: (String) -> Unit) {
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
                .background(if (selectedSection == "My Jobs") myGradient3 else SolidColor(Color.Transparent))
                .weight(1f)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            TextButton(
                onClick = { onSectionSelected("My Jobs") },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (selectedSection == "My Jobs") Color.White else Color.Black
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "My Jobs",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Right-aligned clickable text with box
        Box(
            modifier = Modifier
                .background(
                    if (selectedSection == "My Submissions") myGradient3 else SolidColor(
                        Color.Transparent
                    )
                )
                .padding(4.dp)
                .weight(1f),
        ) {
            TextButton(
                onClick = { onSectionSelected("My Submissions") },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (selectedSection == "My Submissions") Color.White else Color.Black
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "My Submissions",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

    }
    Divider(
        color = Color.Black, // Change color if needed
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
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))

        ,
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
@Composable
fun PendingBookingsTradesmanContent(navController: NavController, getTradesmanBooking: GetTradesmanBookingViewModel, updateBookingClientViewModel: UpdateBookingClientViewModel) {
    val bookingPendingstate = getTradesmanBooking.TradesmanBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        bookingPendingstate.refresh()
    }
    val bookingPending = bookingPendingstate.itemSnapshotList.items.filter { it.bookingstatus == "Pending" }
    LazyColumn(
        modifier = Modifier
            .padding(bottom = 80.dp)
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))

        ,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(bookingPending.size) { index ->
            val Pending = bookingPending[index]
            PendingTradesmanItem(Pending,navController,updateBookingClientViewModel)
        }
    }
}
@Composable
fun DeclinedBookingsTradesmanContent(navController: NavController,getTradesmanBooking: GetTradesmanBookingViewModel) {
    val declinedBookingState = getTradesmanBooking.TradesmanBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        declinedBookingState.refresh()
    }

    val declinedBookings = declinedBookingState.itemSnapshotList.items.filter { it.bookingstatus == "Declined" }

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

@Composable
fun ActiveBookingsTradesmanContent(navController: NavController,getTradesmanBooking: GetTradesmanBookingViewModel) {
   val activeBookingstate = getTradesmanBooking.TradesmanBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        activeBookingstate.refresh()
    }
    val activeBookings = activeBookingstate.itemSnapshotList.items.filter { it.bookingstatus == "Active" }
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

@Composable
fun CompletedBookingsTradesmanContent(navController: NavController,getTradesmanBooking: GetTradesmanBookingViewModel) {
    val completedBookingstate = getTradesmanBooking.TradesmanBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        completedBookingstate.refresh()
    }
    val completedBooking = completedBookingstate.itemSnapshotList.items.filter { it.bookingstatus == "Pending" }

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
@Composable
fun CancelledBookingsTradesmanContent(navController: NavController,getTradesmanBooking: GetTradesmanBookingViewModel) {
    val cancelledBookingstate = getTradesmanBooking.TradesmanBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        cancelledBookingstate.refresh()
    }

    val cancelledBookings = cancelledBookingstate.itemSnapshotList.items.filter { it.bookingstatus == "Cancelled" }

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



//Design For Items
@Composable
fun AllTradesmanItem(allBooking: GetTradesmanBooking) {
    val date = ViewModelSetups.formatDateTime(allBooking.bookingdate)
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

    Card(
        modifier = Modifier
            .size(cardHeight.first, cardHeight.second)
            .clickable { }, // Add implementation for click if needed
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tradesman image
                Image(
                     painter = painterResource(R.drawable.pfp),
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
                        text = allBooking.clientfullname,
                        color = Color.Black,
                        fontWeight = FontWeight(500),
                        fontSize = nameTextSize,
                    )



                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                color = (Color(0xFFFFF2DD)),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star Icon",
                                tint = Color(0xFFFFA500),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.size(4.dp))
                        }
                    }
                    Text(
                        text = "Weekdays Selected",
                        color = Color.Black,
                        fontSize = taskTextSize,

                        )
                    Text(
                        text = date,
                        color = Color.Gray,
                        fontSize = smallTextSize,

                        )
                }

                Text(
                    text = allBooking.bookingstatus,
                    fontSize = smallTextSize,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(end = 8.dp)


                )

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
    val date = ViewModelSetups.formatDateTime(pending.bookingdate)

    LaunchedEffect(updateWorkStatus) {
        when (val updateStatus = updateWorkStatus) {
            is UpdateBookingClientViewModel.UpdateClientWorkStatus.Loading -> {
                // Show loading dialog
            }
            is UpdateBookingClientViewModel.UpdateClientWorkStatus.Success -> {
                updateBookingClientViewModel.resetState()
                // Set the selectedTab based on the work status
                if (updateStatus.status == "Accepted") {
                    navController.navigate("main_screen?selectedItem=1&selectedTab=3") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                    }
                } else if (updateStatus.status == "Declined") {
                    navController.navigate("main_screen?selectedItem=1&selectedTab=2") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                    }
                }


            }

            is UpdateBookingClientViewModel.UpdateClientWorkStatus.Error -> {
                val errorMessage = updateStatus.message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                updateBookingClientViewModel.resetState()
            }

            else -> Unit
        }
    }

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
            .size(cardHeight.first,cardHeight.second)
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
                    Image(
                        painter = painterResource(R.drawable.pfp),
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
                            text = pending.clientfullname,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = nameTextSize,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Row(
                            modifier = Modifier.padding(top = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Rate Box

                            Box(
                                modifier = Modifier
                                    .background(
                                        color = (Color(0xFFFFF2DD)),
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                            12.dp
                                        )
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Star Icon",
                                        tint = Color(0xFFFFA500),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.size(4.dp))
                                }
                            }
                        }
                        Text(
                            text = "Weekdays Selected",
                            color = Color.Black,
                            fontSize = taskTextSize,
                        )
                        Text(
                            text = date,
                            color = Color.Gray,
                            fontSize = smallTextSize,
                        )
                    }
                }

                // Spacer between text and buttons
                Spacer(modifier = Modifier.height(20.dp))

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
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color(0xFF42C2AE), shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(text = "Approve", color = Color(0xFF42C2AE), fontSize = smallTextSize)
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
    val date = ViewModelSetups.formatDateTime(declined.bookingdate)
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
            .size(cardHeight.first,cardHeight.second)
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
                    Image(
                        painter = painterResource(R.drawable.pfp),
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
                            text = "Karlos Rivo",
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = nameTextSize,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = "Electrician",
                            color = Color.Black,
                            fontSize = taskTextSize,
                        )
                        Row(
                            modifier = Modifier.padding(top = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Reviews Box
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = (Color(0xFFFFF2DD)),
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                            12.dp
                                        )
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Star Icon",
                                        tint = Color(0xFFFFA500),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.size(4.dp))
                                }
                            }
                        }
                        Text(
                            text = "Weekdays Selected",
                            color = Color.Black,
                            fontSize = taskTextSize,
                        )
                        Text(
                            text = date,
                            color = Color.Gray,
                            fontSize = smallTextSize,
                        )
                    }
                }
                // Spacer between text and buttons
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

                            .clickable { }
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

@Composable
fun ActiveTradesmanItem(active: GetTradesmanBooking, navController: NavController) {
    val date = ViewModelSetups.formatDateTime(active.bookingdate)
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
            .size(cardHeight.first,cardHeight.second)
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
                    Image(
                        painter = painterResource(R.drawable.pfp),
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
                            text = active.clientfullname,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = nameTextSize,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = "Electrician",
                            color = Color.Black,
                            fontSize = taskTextSize,
                        )
                        Row(
                            modifier = Modifier.padding(top = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {


                            // Reviews Box
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = (Color(0xFFFFF2DD)),
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                            12.dp
                                        )
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Star Icon",
                                        tint = Color(0xFFFFA500),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.size(4.dp))
                                }
                            }
                        }
                        Text(
                            text = "Weekdays Selected",
                            color = Color.Black,
                            fontSize = taskTextSize,
                        )
                        Text(
                            text = date,
                            color = Color.Gray,
                            fontSize = smallTextSize,
                        )
                    }

                }

                // Spacer between text and buttons
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Space out the buttons
                ) {
                    Box(
                        modifier = Modifier
                            .clickable { }
                            .background(
                                color = Color(0xFFC51B1B),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {


                        Text(text = "Cancel", fontSize = smallTextSize,color = Color.White,)

                    }

                    Box(
                        modifier = Modifier
                            .clickable { }
                            .background(
                                color = Color(0xFF42C2AE),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(text = "Completed", color = Color.White, fontSize = smallTextSize)
                    }
                }
            }
        }
    }

}
@Composable
fun CompletedItem(completed: GetTradesmanBooking, navController: NavController) {
    val date = ViewModelSetups.formatDateTime(completed.bookingdate)
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
            .size(cardHeight.first,cardHeight.second)
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
                    Image(
                        painter = painterResource(R.drawable.pfp),
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
                            text = completed.clientfullname,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = nameTextSize,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Row(
                            modifier = Modifier.padding(top = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            // Reviews Box
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = (Color(0xFFFFF2DD)),
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                            12.dp
                                        )
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Star Icon",
                                        tint = Color(0xFFFFA500),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.size(4.dp))
                                    Text(
                                        text = "4",
                                        fontSize = smallTextSize
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Weekdays Selected",
                            color = Color.Black,
                            fontSize = taskTextSize,
                        )
                        Text(
                            text = date,
                            color = Color.Gray,
                            fontSize = smallTextSize,
                        )
                    }

                }

                // Spacer between text and buttons
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Space out the buttons
                ) {
                    Box(
                        modifier = Modifier
                            .clickable { }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Book Again", fontSize = smallTextSize)
                    }
                    Box(
                        modifier = Modifier
                            .clickable { }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color(0xFFECAB1E), shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(text = "Rate", color = Color(0xFFECAB1E), fontSize = smallTextSize)
                    }
                }
            }
        }
    }

}


@Composable
fun CancelledItem(cancel: GetTradesmanBooking, navController: NavController) {
    val date = ViewModelSetups.formatDateTime(cancel.bookingdate)
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
            .size(cardHeight.first,cardHeight.second)
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
                    Image(
                        painter = painterResource(R.drawable.pfp),
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
                            text = cancel.clientfullname,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = nameTextSize,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Row(
                            modifier = Modifier.padding(top = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {


                            // Reviews Box
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = (Color(0xFFFFF2DD)),
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                            12.dp
                                        )
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Star Icon",
                                        tint = Color(0xFFFFA500),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.size(4.dp))

                                }
                            }
                        }
                        Text(
                            text = "Weekdays Selected",
                            color = Color.Black,
                            fontSize = taskTextSize,
                        )
                        Text(
                            text = date,
                            color = Color.Gray,
                            fontSize = smallTextSize,
                        )
                    }

                }

                // Spacer between text and buttons
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

                            .clickable { }
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



@Composable
fun AllMySubmissionsTradesmanContent(getMyJobApplications: GetMyJobApplicationViewModel) {
    val myJobs = getMyJobApplications.jobApplicationPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        getMyJobApplications.refreshJobApplicants()
    }

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


@Composable
fun PendingMySubmissionsTradesmanContent(
    navController: NavController,
    getMyJobApplications: GetMyJobApplicationViewModel,
    putJobApplicationStatusViewModel: PutJobApplicationStatusViewModel,
    viewJobsApplication: ViewJobApplicationViewModel
) {
    val myJob = getMyJobApplications.jobApplicationPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        getMyJobApplications.refreshJobApplicants()
    }

    LazyColumn(
        modifier = Modifier
            .padding(bottom = 70.dp)
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9)),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(myJob.itemCount) { index ->
            val job = myJob[index]
            if (job != null && job.status == "Pending") {
                PendingMySubmissionsTradesmanItem(
                    myJob = job,
                    navController = navController,
                    putJobApplicationStatusViewModel = putJobApplicationStatusViewModel,
                    onJobCancelled = {
                        myJob.refresh() // Refresh list when job is canceled
                    }
                )
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

@Composable
fun ActiveMySubmissionsTradesmanContent(navController: NavController, getMyJobApplications: GetMyJobApplicationViewModel, viewJobsApplication: ViewJobApplicationViewModel, putJobApplicationStatusViewModel: PutJobApplicationStatusViewModel) {

    val myJob = getMyJobApplications.jobApplicationPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        myJob.refresh()
    }

    val activeApplication = myJob.itemSnapshotList.items.filter { it.status == "Active" }

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

@Composable
fun CompletedMySubmissionsTradesmanContent(navController: NavController, getMyJobApplications: GetMyJobApplicationViewModel, viewJobsApplication: ViewJobApplicationViewModel) {
    val myJob = getMyJobApplications.jobApplicationPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        myJob.refresh()
    }

    val completedApplication = myJob.itemSnapshotList.items.filter { it.status == "Completed" }

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
@Composable
fun CancelledMySubmissionsTradesmanContent(navController: NavController, getMyJobApplications: GetMyJobApplicationViewModel, viewJobsApplication: ViewJobApplicationViewModel) {
    val myJob = getMyJobApplications.jobApplicationPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        myJob.refresh()
    }

    val cancelledApplication = myJob.itemSnapshotList.items.filter { it.status == "Cancelled" }

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



//Design For Items
@Composable
fun AllMySubmissionsTradesmanItem(myJob: JobApplicationData) {
    val createdAt = ViewModelSetups.formatDateTime(myJob.createdAt)
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
                        Text(text = myJob.status, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Service: $jobType ",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Applied on $createdAt",
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
    putJobApplicationStatusViewModel: PutJobApplicationStatusViewModel,
    onJobCancelled: () -> Unit
) {
    val createdAt = ViewModelSetups.formatDateTime(myJob.createdAt)
    val deadline = ViewModelSetups.formatDateTime(myJob.jobDeadline)
    val putState by putJobApplicationStatusViewModel.putJobApplicationState.collectAsState()
    val windowSize = rememberWindowSizeClass()
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
                Toast.makeText(navController.context, "Application cancelled", Toast.LENGTH_SHORT).show()
                putJobApplicationStatusViewModel.resetState()
                onJobCancelled() // Refresh list
            }
            else -> {}
        }
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
                            text = "Address: ${myJob.jobAddress}",
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
                                navController.navigate("canceltradesmannow/${myJob.id}")
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
                        Text(text = "Cancel Application ", fontSize = 16.sp)
                    }
                    Box(
                        modifier = Modifier
                            .clickable {
                                navController.navigate("tradesmanapply/${myJob.jobId}")
                            }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color(0xFFECAB1E), shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Job Details", color = Color(0xFFECAB1E), fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
@Composable
fun ActiveMySubmissionsTradesmanItem(myJob: JobApplicationData, navController: NavController, putJobApplicationStatusViewModel: PutJobApplicationStatusViewModel) {

    val createdAt = ViewModelSetups.formatDateTime(myJob.createdAt)
    val deadline = ViewModelSetups.formatDateTime(myJob.jobDeadline)
    val putJobState by putJobApplicationStatusViewModel.putJobApplicationState.collectAsState()
    var cancelledClicked by remember { mutableStateOf(false) }
    var completedClicked by remember { mutableStateOf(false) }
    val context = LocalContext.current
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
                    Toast.makeText(context, putJob.data.message(), Toast.LENGTH_SHORT).show()
                    putJobApplicationStatusViewModel.resetState()
                }
            }
        }
    }

    val windowSize = rememberWindowSizeClass()
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Space out the buttons
                ) {
                    Box(
                        modifier = Modifier
                            .clickable {
                                putJobApplicationStatusViewModel.updateJobApplicationStatus(
                                    myJob.id,
                                    "Cancelled",
                                    "",
                                )
                                cancelledClicked = true
                            }
                            .background(
                                color = Color(0xFFC51B1B),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Cancel", fontSize = 16.sp)
                    }
                    Box(
                        modifier = Modifier
                            .clickable {
                                putJobApplicationStatusViewModel.updateJobApplicationStatus(
                                    myJob.id,
                                    "Completed",
                                    "",
                                )
                                completedClicked = true
                            }
                            .background(
                                color = Color(0xFF42C2AE),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color(0xFFECAB1E), shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Completed", color = Color(0xFFECAB1E), fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
@Composable
fun DeclinedMySubmissionsTradesmanItem(myJob: JobApplicationData, navController: NavController, ) {
    val createdAt = ViewModelSetups.formatDateTime(myJob.createdAt)
    val deadline = ViewModelSetups.formatDateTime(myJob.jobDeadline)
    val windowSize = rememberWindowSizeClass()
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

                            .clickable { }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                            .padding(vertical = 8.dp),  // Adjust padding as needed
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Apply Again", fontSize = 14.sp)
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Space out the buttons
                ) {
                    Box(
                        modifier = Modifier
                            .clickable {}
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Apply Again", fontSize = 16.sp)
                    }
                    Box(
                        modifier = Modifier
                            .clickable { }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color(0xFFECAB1E), shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(text = "Rate", color = Color(0xFFECAB1E), fontSize = 16.sp)
                    }
                }
            }
        }
    }
}


@Composable
fun CancelledMySubmissionsTradesmanItem(myJob: JobApplicationData, navController: NavController) {
    val createdAt = ViewModelSetups.formatDateTime(myJob.createdAt)
    val deadline = ViewModelSetups.formatDateTime(myJob.jobDeadline)
    val windowSize = rememberWindowSizeClass()
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
                            .clickable { }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                            .padding(vertical = 8.dp),  // Adjust padding as needed
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Apply Again", fontSize = 14.sp)
                    }
                }

            }
        }
    }
}
