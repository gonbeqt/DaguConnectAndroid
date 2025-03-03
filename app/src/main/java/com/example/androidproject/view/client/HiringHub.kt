package com.example.androidproject.view.client

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.ViewModelSetups
import com.example.androidproject.model.JobApplicantData
import com.example.androidproject.model.client.GetClientsBooking
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.viewmodel.bookings.GetClientBookingViewModel
import com.example.androidproject.viewmodel.bookings.UpdateWorkStatusViewModel
import com.example.androidproject.viewmodel.job_application.PutJobApplicationStatusViewModel
import com.example.androidproject.viewmodel.job_application.ViewJobApplicationViewModel
import com.example.androidproject.viewmodel.job_application.client.GetMyJobApplicantsViewModel
import java.sql.Types.NULL


@Composable
fun BookingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    getClientsBooking: GetClientBookingViewModel,
    updateWorkStatusViewModel: UpdateWorkStatusViewModel,
    getMyJobApplicants: GetMyJobApplicantsViewModel,
    viewJobsApplication: ViewJobApplicationViewModel,
    putJobApplicationStatus: PutJobApplicationStatusViewModel,
    initialTabIndex: Int = 0 // Default to 0 if not provided
) {
    Log.i("Screen", "BookingsScreen")
    val windowSize = rememberWindowSizeClass()
    val textSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }
    var selectedTabIndex by remember { mutableIntStateOf(initialTabIndex) } // Use initialTabIndex
    var selectedSection by remember { mutableStateOf("My Clients") }

    val myJobsTabs = listOf("All", "Pending", "Declined", "Active", "Completed", "Cancelled")
    val myApplicantsTabs = listOf("All", "Pending", "Declined", "Active", "Completed", "Cancelled")
    val tabTitles = if (selectedSection == "My Jobs") myJobsTabs else myApplicantsTabs

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
          Row(Modifier.fillMaxWidth().height(70.dp).shadow(1.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically

          )  {
              Row(
                  modifier = Modifier
                      .padding(horizontal = 25.dp)
                      .fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically

              ) {
                  Text(
                      text = "Bookings",
                      fontSize = 28.sp,
                      fontWeight = FontWeight.Medium
                  )
                  Icon(
                      imageVector = Icons.Default.Notifications,
                      contentDescription = "Notifications Icon",
                      tint = Color.Black,
                      modifier = Modifier
                          .size(35.dp)
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

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFD9D9D9))
                        .padding(16.dp)
                ) {
                    when (selectedSection) {
                        "My Clients" -> when (selectedTabIndex) {
                            0 -> AllBookingsContent(getClientsBooking, navController)
                            1 -> PendingBookingsContent(getClientsBooking, navController)
                            2 -> DeclinedBookingsContent(getClientsBooking, navController)
                            3 -> ActiveBookingsContent(getClientsBooking, navController, updateWorkStatusViewModel)
                            4 -> CompletedBookingsContent(getClientsBooking, navController)
                            5 -> CancelledBookingsContent(getClientsBooking, navController)
                        }
                        "My Applicants" -> when (selectedTabIndex) {
                            0 -> AllApplicantsContent(getMyJobApplicants, viewJobsApplication)
                            1 -> PendingApplicantsContent(navController, getMyJobApplicants, viewJobsApplication, putJobApplicationStatus)
                            2 -> DeclinedApplicantsContent(navController, getMyJobApplicants, viewJobsApplication)
                            3 -> ActiveApplicantsContent(navController, getMyJobApplicants, viewJobsApplication )
                            4 -> CompletedApplicantsContent(navController, getMyJobApplicants, viewJobsApplication)
                            5 -> CancelledApplicantsContent(navController, getMyJobApplicants, viewJobsApplication)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookingsTopSection(navController: NavController, selectedSection: String, onSectionSelected: (String) -> Unit) {
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
                .background(if (selectedSection == "My Clients") Color(0xFF3CC0B0  ) else (Color.Transparent))
                .weight(1f)
                .clickable {
                    onSectionSelected("My Clients")
                },
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "My Clients",
                fontSize = nameTextSize,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = if (selectedSection == "My Clients") Color.White else Color.Black
            )

        }

        // Right-aligned clickable text with box
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(if (selectedSection == "My Applicants") Color(0xFF3CC0B0  ) else (Color.Transparent))
                .weight(1f)
                .clickable {
                    onSectionSelected("My Applicants")
                },
            contentAlignment = Alignment.Center
        ) {

                Text(
                    text = "My Applicants",
                    fontSize = nameTextSize,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    color = if (selectedSection == "My Applicants") Color.White else Color.Black
                )

        }
    }
}


@Composable
fun AllBookingsContent(getClientsBooking: GetClientBookingViewModel,navController: NavController) {
    val allBooking = getClientsBooking.ClientBookingPagingData.collectAsLazyPagingItems()

    // Example: Call this after adding a new resume
    LaunchedEffect(Unit) {
        getClientsBooking.invalidatePagingSource()
    }
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

           /* items(booking.size) { index ->
                val bookings = booking[index]
                AllItem(bookings,navController)
            }*/





    }

}


@Composable
fun PendingBookingsContent(getClientBooking: GetClientBookingViewModel, navController:NavController) {
    val clientbookingState = getClientBooking.ClientBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        getClientBooking.invalidatePagingSource()
    }
    // Filter the bookings to get only those with status "Pending"
    val pendingBookings = clientbookingState.itemSnapshotList.items.filter { it.bookingStatus == "Pending" }
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))

        ,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(pendingBookings.size) { index ->
            val pendingbookings = pendingBookings[index]
            PendingItem(pendingbookings,navController)
        }
    }




}
@Composable
fun DeclinedBookingsContent(getClientBooking: GetClientBookingViewModel,navController: NavController) {
    val clientbookingState = getClientBooking.ClientBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        getClientBooking.invalidatePagingSource()
    }

    // Filter the bookings to get only those with status "Declined"
    val declinedBookings = clientbookingState.itemSnapshotList.items.filter { it.bookingStatus == "Declined" }
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

@Composable
fun ActiveBookingsContent(getClientBooking: GetClientBookingViewModel,navController:NavController,updateWorkStatusViewModel:UpdateWorkStatusViewModel) {
    val clientbookingState = getClientBooking.ClientBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        getClientBooking.invalidatePagingSource()
    }
    // Filter the bookings to get only those with status "Active"
    val activeBooking = clientbookingState.itemSnapshotList.items.filter { it.bookingStatus == "Active" }
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

@Composable
fun CompletedBookingsContent(getClientBooking: GetClientBookingViewModel,navController: NavController) {
    val clientbookingState = getClientBooking.ClientBookingPagingData.collectAsLazyPagingItems()
    LaunchedEffect(Unit) {
        getClientBooking.invalidatePagingSource()
    }
    // Filter the bookings to get only those with status "Completed"
    val completedBookings = clientbookingState.itemSnapshotList.items.filter { it.bookingStatus == "Completed" }
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
@Composable
fun CancelledBookingsContent(getClientBooking: GetClientBookingViewModel,navController: NavController) {
    val clientbookingState = getClientBooking.ClientBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        getClientBooking.invalidatePagingSource()
    }

    // Filter the bookings to get only those with status "Completed"
    val completedBookings = clientbookingState.itemSnapshotList.items.filter { it.bookingStatus == "Cancelled" }
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





//Design For Items
@Composable
fun AllItem(allBooking : GetClientsBooking,navController: NavController) {
    val bookingDate = ViewModelSetups.formatDateTime(allBooking.bookingDate)

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
        "pending" -> Color(0xFFECAB1E)
        "cancelled", "declined" -> Color.Red
        "completed" -> Color.Blue
        "active" -> Color.Green
        else -> Color.Gray // Default color
    }
    Card(
        modifier = Modifier
            .size(cardHeight.first,cardHeight.second)
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
                AsyncImage(
                    model = allBooking.tradesmanProfile,
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
                        text = allBooking.tradesmanFullName,
                        color = Color.Black,
                        fontWeight = FontWeight(500),
                        fontSize = nameTextSize,
                    )
                    Text(
                        text = allBooking.taskType
                            .replace("_"," "),
                        color = Color.Black,
                        fontSize =taskTextSize,
                    )


                        // Rate Box
                        Box(
                            modifier = Modifier
                                .background(
                                    color = (Color(0xFFFFF2DD)),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "P ${allBooking.workFee}/hr",
                                fontSize = smallTextSize,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
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
                                Text(
                                    text =  when {
                                        allBooking.ratings == 0f -> "0"
                                        else -> String.format("%.1f", allBooking.ratings)
                                    },
                                    fontSize = smallTextSize
                                )
                            }
                        }



                    Text(
                        text = "Weekdays Selected",
                        color = Color.Black,
                        fontSize = taskTextSize,

                        )
                    Text(
                        text = bookingDate,
                        color = Color.Gray,
                        fontSize = smallTextSize,

                        )
                }

                    Text(
                        text = allBooking.bookingStatus,
                        fontSize = smallTextSize,
                        fontWeight = FontWeight.Bold,
                        color = statusColor,
                        modifier = Modifier.padding(end = 8.dp)


                    )

            }
        }
    }
}


//Design for activeItems
@Composable
fun ActiveItems(activeBooking: GetClientsBooking,navController:NavController,updateWorkStatusViewModel:UpdateWorkStatusViewModel) {
    val updateworkstatusstate by updateWorkStatusViewModel.workStatusState.collectAsState()
    val  context = LocalContext.current
    var isCompleted by remember { mutableStateOf(false) }


    LaunchedEffect(updateworkstatusstate) {
        when (val updateWorkStatusState= updateworkstatusstate) {
            is UpdateWorkStatusViewModel.UpdateWorkStatus.Success -> {
                Toast.makeText(context, "Booking Successfully Completed", Toast.LENGTH_SHORT).show()
                isCompleted = true
                updateWorkStatusViewModel.resetState()
            }
            is UpdateWorkStatusViewModel.UpdateWorkStatus.Error -> {
                val errorMessage = updateWorkStatusState.message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                updateWorkStatusViewModel.resetState()
            }
            else -> Unit
        }
    }
    if(!isCompleted){
        val bookingDate = ViewModelSetups.formatDateTime(activeBooking.bookingDate)
        val windowSize = rememberWindowSizeClass()
        val cardHeight = when (windowSize.width) {
            WindowType.SMALL -> 380.dp to 240.dp
            WindowType.MEDIUM -> 390.dp to 250.dp
            WindowType.LARGE -> 400.dp to 260.dp
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
                .clickable { },
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(2.dp)

        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.CenterStart
            ) {
                Column( // Using Column to stack elements vertically inside the Card
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Tradesman image
                        AsyncImage(
                            model = activeBooking.tradesmanProfile,
                            contentDescription = "Tradesman Image",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(start = 10.dp)
                        )

                        // Tradesman details
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()

                                .padding(start = 10.dp)
                        ) {
                            Text(
                                text = activeBooking.tradesmanFullName,
                                color = Color.Black,
                                fontWeight = FontWeight(500),
                                fontSize = nameTextSize,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                            Text(
                                text = activeBooking.taskType
                                    .replace("_"," "),
                                color = Color.Black,
                                fontSize = taskTextSize,
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
                                    Text(
                                        text = "P${activeBooking.workFee}/hr",
                                        fontSize = smallTextSize,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )
                                }

                                // Reviews Box
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = (Color(0xFFFFF2DD)),
                                            shape = RoundedCornerShape(12.dp)
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
                                            text =  when {
                                                activeBooking.ratings == 0f -> "0"
                                                else -> String.format("%.1f", activeBooking.ratings)
                                            },
                                            fontSize = smallTextSize
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Weekdays Selected",
                                color = Color.Black,
                                fontSize = taskTextSize,
                                modifier = Modifier.fillMaxWidth()

                            )
                            Text(
                                text = bookingDate,
                                color = Color.Gray,
                                fontSize = smallTextSize,

                                )
                        }

                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp) // Space out the buttons
                    ) {
                        Box(
                            modifier = Modifier
                                .clickable { navController.navigate("cancelnow/${activeBooking.resumeId}/${activeBooking.bookingStatus}/${activeBooking.id}") }
                                .background(
                                    color = Color(0xFFC51B1B),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .weight(1f)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {


                            Text(text = "Cancel", fontSize = smallTextSize, color = Color.White)

                        }

                        Box(
                            modifier = Modifier
                                .clickable {
                                    updateWorkStatusViewModel.updateWorkStatus("Completed", NULL.toString(),activeBooking.id)
                                }
                                .background(
                                    color = Color(0xFF42C2AE),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .weight(1f)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = "Completed",
                                color = Color.White,
                                fontSize = smallTextSize
                            )
                        }
                    }

                }
            }
        }
    }


}


@Composable
fun PendingItem(pendingBooking : GetClientsBooking, navController:NavController) {
    val bookingDate = ViewModelSetups.formatDateTime(pendingBooking.bookingDate)
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 380.dp to 240.dp
        WindowType.MEDIUM -> 390.dp to 250.dp
        WindowType.LARGE -> 400.dp to 260.dp
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
                    AsyncImage(
                        model = pendingBooking.tradesmanProfile,
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
                            text = pendingBooking.tradesmanFullName,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = nameTextSize,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = pendingBooking.taskType
                                .replace("_"," "),
                            color = Color.Black,
                            fontSize = taskTextSize,
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
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "P${pendingBooking.workFee}/hr",
                                    fontSize = smallTextSize,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }

                            // Reviews Box
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
                                    Text(
                                        text =  when {
                                            pendingBooking.ratings == 0f -> "0"
                                            else -> String.format("%.1f", pendingBooking.ratings)
                                        },
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
                            text = bookingDate,
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
                            .clickable { navController.navigate("cancelnow/${pendingBooking.resumeId}/${pendingBooking.bookingStatus}/${pendingBooking.id}") }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {


                            Text(text = "Cancel Appointment", fontSize = smallTextSize)

                    }

                    Box(
                        modifier = Modifier
                            .clickable { navController.navigate("bookingdetails/${pendingBooking.resumeId}") }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color(0xFFECAB1E), shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {

                            Text(text = "Booking Details", color = Color(0xFFECAB1E), fontSize = smallTextSize)
                        }
                }
            }
        }
    }
}
@Composable
fun DeclinedItem(declineBooking: GetClientsBooking, navController:NavController) {
    val bookingDate = ViewModelSetups.formatDateTime(declineBooking.bookingDate)
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 380.dp to 240.dp
        WindowType.MEDIUM -> 390.dp to 250.dp
        WindowType.LARGE -> 400.dp to 260.dp
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
            .size(cardHeight.first, cardHeight.second),
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
                        painter = painterResource(id = R.drawable.pfp),
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
                            text = declineBooking.tradesmanFullName,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = nameTextSize,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = declineBooking.taskType
                                .replace("_"," "),
                            color = Color.Black,
                            fontSize = taskTextSize,
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
                                Text(
                                    text = "P${declineBooking.workFee}/hr",
                                    fontSize = smallTextSize,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }

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
                                        text =  when {
                                            declineBooking.ratings == 0f -> "0"
                                            else -> String.format("%.1f", declineBooking.ratings)
                                        },
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
                            text = bookingDate,
                            color = Color.Gray,
                            fontSize =smallTextSize,
                        )
                    }

                }

                // Spacer between text and buttons
                Spacer(modifier = Modifier.height(20.dp))



                    Box(
                        modifier = Modifier
                            .clickable { navController.navigate("booknow/${declineBooking.resumeId}") }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color(0xFFECAB1E), shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(text = "Book Again", color = Color(0xFFECAB1E), fontSize = smallTextSize)
                    }
                }
        }
    }
}
@Composable
fun CompletedItem(completedBooking: GetClientsBooking, navController:NavController) {
    val bookingDate = ViewModelSetups.formatDateTime(completedBooking.bookingDate)
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 380.dp to 240.dp
        WindowType.MEDIUM -> 390.dp to 250.dp
        WindowType.LARGE -> 400.dp to 260.dp
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
        , // Add implementation for click if needed
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
                        model = completedBooking.tradesmanProfile,
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
                            text = completedBooking.tradesmanFullName,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = nameTextSize,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = completedBooking.taskType
                                .replace("_"," "),
                            color = Color.Black,
                            fontSize = taskTextSize,
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
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "P${completedBooking.workFee}",
                                    fontSize = smallTextSize,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }

                            // Reviews Box
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
                                    Text(
                                        text = when {
                                            completedBooking.ratings == 0f -> "0"
                                            else -> String.format("%.1f", completedBooking.ratings)
                                        },
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
                            text = bookingDate,
                            color = Color.Gray,
                            fontSize = smallTextSize
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
                            .clickable { navController.navigate("booknow/${completedBooking.resumeId}")}
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
                            .clickable { navController.navigate("rateandreviews/${completedBooking.resumeId}/${completedBooking.tradesmanId}") }
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
fun CancelledItem(cancelledBooking: GetClientsBooking, navController:NavController) {
    val bookingDate = ViewModelSetups.formatDateTime(cancelledBooking.bookingDate)
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 380.dp to 240.dp
        WindowType.MEDIUM -> 390.dp to 250.dp
        WindowType.LARGE -> 400.dp to 260.dp
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
            .size(cardHeight.first, cardHeight.second),
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
                        model = cancelledBooking.tradesmanProfile,
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
                            text = cancelledBooking.tradesmanFullName,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = nameTextSize,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = cancelledBooking.taskType
                                .replace("_"," "),
                            color = Color.Black,
                            fontSize = taskTextSize,
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
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "P${cancelledBooking.workFee}/hr",
                                    fontSize = smallTextSize,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }

                            // Reviews Box
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
                                    Text(
                                        text =   when {
                                            cancelledBooking.ratings == 0f -> "0"
                                            else -> String.format("%.1f", cancelledBooking.ratings)
                                        },
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
                            text = bookingDate,
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
                            .clickable { navController.navigate("cancellationdetails")}
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Cancellation  Details", fontSize = smallTextSize)
                    }
                    Box(
                        modifier = Modifier
                            .clickable { navController.navigate("booknow/${cancelledBooking.resumeId}") }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color(0xFFECAB1E), shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Book Again", color = Color(0xFFECAB1E), fontSize = smallTextSize)
                    }
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
        getMyJobApplicant.invalidatePagingSource()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))

        ,
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


@Composable
fun PendingApplicantsContent(navController: NavController, getMyJobApplicant: GetMyJobApplicantsViewModel, viewJobsApplication: ViewJobApplicationViewModel, putJobApplicationStatus: PutJobApplicationStatusViewModel) {
    val myJob = getMyJobApplicant.jobApplicantsPagingData.collectAsLazyPagingItems()


    val pendingApplication = myJob.itemSnapshotList.items.filter { it.status == "Pending" }

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))

        ,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(pendingApplication.size) { index ->
            val pendingJobs = pendingApplication[index]
            PendingApplicantsItem(pendingJobs, navController, putJobApplicationStatus)
        }
    }
}
@Composable
fun DeclinedApplicantsContent(navController: NavController, getMyJobApplicant: GetMyJobApplicantsViewModel, viewJobsApplication: ViewJobApplicationViewModel) {
    val myJob = getMyJobApplicant.jobApplicantsPagingData.collectAsLazyPagingItems()

    val declinedApplication = myJob.itemSnapshotList.items.filter { it.status == "Declined" }
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))

        ,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(declinedApplication.size) { index ->
            val declineJob = declinedApplication[index]
            DeclinedApplicantsItem(declineJob, navController)
        }
    }
}

@Composable
fun ActiveApplicantsContent(navController: NavController, getMyJobApplicant: GetMyJobApplicantsViewModel, viewJobsApplication: ViewJobApplicationViewModel) {
    val myJob = getMyJobApplicant.jobApplicantsPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        getMyJobApplicant.invalidatePagingSource()
    }

    val activeApplication = myJob.itemSnapshotList.items.filter { it.status == "Active" }
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))

        ,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(activeApplication.size) { index ->
            val activeJobs = activeApplication[index]
            ActiveApplicantsItem(activeJobs, navController)
        }
    }
}

@Composable
fun CompletedApplicantsContent(navController: NavController, getMyJobApplicant: GetMyJobApplicantsViewModel, viewJobsApplication: ViewJobApplicationViewModel) {
    val myJob = getMyJobApplicant.jobApplicantsPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        getMyJobApplicant.invalidatePagingSource()
    }

    val completedApplication = myJob.itemSnapshotList.items.filter { it.status == "Completed" }

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))

        ,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(completedApplication.size) { index ->
            val completedJobs = completedApplication[index]
            CompletedApplicantsItem(completedJobs, navController )
        }
    }
}
@Composable
fun CancelledApplicantsContent(navController: NavController, getMyJobApplicant: GetMyJobApplicantsViewModel, viewJobsApplication: ViewJobApplicationViewModel) {
    val myJob = getMyJobApplicant.jobApplicantsPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        getMyJobApplicant.invalidatePagingSource()
    }

    val cancelledApplication = myJob.itemSnapshotList.items.filter { it.status == "Cancelled" }

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))
        ,
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
        WindowType.SMALL -> 400.dp to 170.dp
        WindowType.MEDIUM -> 410.dp to 180.dp
        WindowType.LARGE -> 420.dp to 190.dp
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
            .size(cardHeight.first, cardHeight.second),
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
                    Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                        Text(
                            text = myJob.tradesmanFullname,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = nameTextSize
                        )
                        Text(text = myJob.status, fontSize = taskTextSize)
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
    when(putJob) {
        is PutJobApplicationStatusViewModel.PutJobApplicationState.Idle -> {

        }

        is PutJobApplicationStatusViewModel.PutJobApplicationState.Loading -> {

        }

        is PutJobApplicationStatusViewModel.PutJobApplicationState.Error -> {

        }

        is PutJobApplicationStatusViewModel.PutJobApplicationState.Success -> {
            if (buttonSubmit) {
                Toast.makeText(LocalContext.current, "Job Application Updated", Toast.LENGTH_SHORT).show()
                putJobApplicationStatus.resetState()
                navController.navigate("main_screen")
            }
        }
    }

    val reasons = listOf(
        "Change of Mind",
        "Found a Different Service Provider",
        "No Longer Needed",
        "Scheduled Time Conflict",
        "Personal Reasons",
        "Others"
    )
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
                Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.End
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
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Cancel Job ", fontSize = nameTextSize)
                        }

                        Box(
                            modifier = Modifier
                                .clickable { showApproveDialog = true}
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(1.dp, Color(0xFFECAB1E), shape = RoundedCornerShape(12.dp))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = " Accept Job",
                                color = Color(0xFFECAB1E),
                                fontSize = nameTextSize
                            )
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
                            buttonSubmit = true
                            showDeclineReasons = false
                            putJobApplicationStatus.updateJobApplicationStatus(myJob.id, "Cancelled", selectedReason)

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
                            fontSize = 18.sp
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
                    horizontalArrangement = Arrangement.End // Aligns content to the end
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .clickable { navController.navigate("canceljobapplicationsdetails")}
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )

                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                            .padding(vertical = 8.dp, horizontal = 56.dp), // Added horizontal padding for spacing
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Details", fontSize = nameTextSize, color =  Color.Black)
                    }
                }
            }
        }
    }
}
@Composable
fun ActiveApplicantsItem(myJob: JobApplicantData, navController: NavController) {
    var cancel by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(-1) }
    var otherReason by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val reasons = listOf(
        "Change of Mind",
        "Found a Different Service Provider",
        "No Longer Needed",
        "Scheduled Time Conflict",
        "Personal Reasons",
        "Others"
    )
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
                                .clickable { cancel = true }
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Cancel Job ", fontSize = nameTextSize)
                        }
                        Box(
                            modifier = Modifier
                                .clickable { }
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(1.dp, Color(0xFFECAB1E), shape = RoundedCornerShape(12.dp))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = "Completed",
                                color = Color(0xFFECAB1E),
                                fontSize = nameTextSize
                            )
                        }
                    }
                }
            }
        }
        if (cancel) {
            Dialog(onDismissRequest = { cancel = false }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .border(2.dp, Color(0xFFB5B5B5), shape = RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)), // Dark background for contrast
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Reason for Cancellation",
                                fontSize = 20.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )

                            Column(modifier = Modifier.padding(top = 16.dp)) {
                                reasons.forEachIndexed { index, reason ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    ) {
                                        Checkbox(
                                            checked = selectedIndex == index,
                                            onCheckedChange = {
                                                selectedIndex =
                                                    if (selectedIndex == index) -1 else index
                                            },
                                            colors = CheckboxDefaults.colors(
                                                uncheckedColor = Color.Black,
                                                checkedColor = Color(0xFF42C2AE)
                                            )
                                        )
                                        Text(
                                            text = reason,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.Black,
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                }
                            }

                            if (selectedIndex == reasons.lastIndex) {
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = otherReason,
                                    onValueChange = { otherReason = it },
                                    placeholder = { Text("Enter your reason") },
                                    shape = RoundedCornerShape(16.dp),
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 56.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Blue,
                                        unfocusedIndicatorColor = Color.Gray,
                                        focusedLabelColor = Color.Blue,
                                        unfocusedLabelColor = Color.Gray,
                                        cursorColor = Color.Black
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = description,
                                onValueChange = { description = it },
                                placeholder = { Text("Enter your description") },
                                shape = RoundedCornerShape(16.dp),
                                maxLines = 3,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 100.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Blue,
                                    unfocusedIndicatorColor = Color.Gray,
                                    focusedLabelColor = Color.Blue,
                                    unfocusedLabelColor = Color.Gray,
                                    cursorColor = Color.Black
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = { cancel = false },
                                    modifier = Modifier.size(110.dp, 45.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(
                                            0xFF42C2AE
                                        )
                                    )
                                ) {
                                    Text("Cancel", color = Color.White)
                                }
                                Button(
                                    onClick = {

                                    },
                                    modifier = Modifier.size(110.dp, 45.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF42C2AE)
                                    )
                                ) {
                                    Text("Submit", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}
@Composable
fun CompletedApplicantsItem(myJob: JobApplicantData, navController: NavController) {
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
                    horizontalArrangement = Arrangement.End // Aligns content to the end
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
                            .padding(vertical = 8.dp, horizontal = 56.dp), // Added horizontal padding for spacing
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Rate", fontSize = nameTextSize, color = Color(0xFFECAB1E))
                    }
                }
            }

        }
    }
}


@Composable
fun CancelledApplicantsItem(myJob: JobApplicantData, navController: NavController) {
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
                            fontSize =taskTextSize
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
                    horizontalArrangement = Arrangement.End // Aligns content to the end
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .clickable {navController.navigate("canceljobapplicationsdetails") }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )

                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                            .padding(vertical = 8.dp, horizontal = 56.dp), // Added horizontal padding for spacing
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Details", fontSize = nameTextSize, color =  Color.Black)
                    }
                }
            }
        }
    }
}
