package com.example.androidproject.view.pages

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
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
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
import com.example.androidproject.model.client.GetClientsBooking
import com.example.androidproject.model.client.resumesItem
import com.example.androidproject.view.Tradesman
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.view.theme.myGradient3
import com.example.androidproject.viewmodel.bookings.GetClientBookingViewModel
import com.example.androidproject.viewmodel.bookings.UpdateWorkStatusViewModel
import java.sql.Types.NULL


@Composable
fun BookingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    getClientsBooking: GetClientBookingViewModel,
    updateWorkStatusViewModel: UpdateWorkStatusViewModel,
    initialTabIndex: Int = 0 // Default to 0 if not provided
) {
    Log.i("Screen", "BookingsScreen")
    val windowSize = rememberWindowSizeClass()
    val textSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }
    var selectedTabIndex by remember { mutableStateOf(initialTabIndex) } // Use initialTabIndex
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
                            0 -> AllApplicantsContent()
                            1 -> PendingApplicantsContent(navController)
                            2 -> DeclinedApplicantsContent(navController)
                            3 -> ActiveApplicantsContent(navController)
                            4 -> CompletedApplicantsContent(navController)
                            5 -> CancelledApplicantsContent(navController)

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
    val pendingBookings = clientbookingState.itemSnapshotList.items.filter { it.bookingstatus == "Pending" }
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
    val declinedBookings = clientbookingState.itemSnapshotList.items.filter { it.bookingstatus == "Declined" }
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
    val ActiveBookings = clientbookingState.itemSnapshotList.items.filter { it.bookingstatus == "Active" }
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))

        ,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(ActiveBookings.size) { index ->
            val activebooking = ActiveBookings[index]
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
    val completedBookings = clientbookingState.itemSnapshotList.items.filter { it.bookingstatus == "Completed" }
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
    val completedBookings = clientbookingState.itemSnapshotList.items.filter { it.bookingstatus == "Cancelled" }
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
    val bookingdate = ViewModelSetups.formatDateTime(allBooking.bookingdate)

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
    val statusColor = when (allBooking.bookingstatus.lowercase()) {
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
                    model = allBooking.tradesmanprofile,
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
                        text = allBooking.tradesmanfullname,
                        color = Color.Black,
                        fontWeight = FontWeight(500),
                        fontSize = nameTextSize,
                    )
                    Text(
                        text = allBooking.tasktype,
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
                                text = "P ${allBooking.workfee}/hr",
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
                                        allBooking.ratings == null || allBooking.ratings == 0f -> "0"
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
                        text = bookingdate,
                        color = Color.Gray,
                        fontSize = smallTextSize,

                        )
                }

                    Text(
                        text = allBooking.bookingstatus,
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
        val bookingdate = ViewModelSetups.formatDateTime(activeBooking.bookingdate)
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
                            model = activeBooking.tradesmanprofile,
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
                                text = activeBooking.tradesmanfullname,
                                color = Color.Black,
                                fontWeight = FontWeight(500),
                                fontSize = nameTextSize,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                            Text(
                                text = activeBooking.tasktype,
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
                                        text = "P${activeBooking.workfee}/hr",
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
                                                activeBooking.ratings == null || activeBooking.ratings == 0f -> "0"
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
                                text = bookingdate,
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
                                .clickable { navController.navigate("cancelnow/${activeBooking.resumeid}/${activeBooking.bookingstatus}/${activeBooking.id}") }
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
    val bookingdate = ViewModelSetups.formatDateTime(pendingBooking.bookingdate)
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
                        model = pendingBooking.tradesmanprofile,
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
                            text = pendingBooking.tradesmanfullname,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = nameTextSize,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = pendingBooking.tasktype,
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
                                    text = "P${pendingBooking.workfee}/hr",
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
                                            pendingBooking.ratings == null || pendingBooking.ratings == 0f -> "0"
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
                            text = bookingdate,
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
                            .clickable { navController.navigate("cancelnow/${pendingBooking.resumeid}/${pendingBooking.bookingstatus}/${pendingBooking.id}") }
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
                            .clickable { navController.navigate("bookingdetails/${pendingBooking.resumeid}") }
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
    val bookingdate = ViewModelSetups.formatDateTime(declineBooking.bookingdate)
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
                            text = declineBooking.tradesmanfullname,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = nameTextSize,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = declineBooking.tasktype,
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
                                    text = "P${declineBooking.workfee}/hr",
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
                                            declineBooking.ratings == null || declineBooking.ratings == 0f -> "0"
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
                            text = bookingdate,
                            color = Color.Gray,
                            fontSize =smallTextSize,
                        )
                    }

                }

                // Spacer between text and buttons
                Spacer(modifier = Modifier.height(20.dp))



                    Box(
                        modifier = Modifier
                            .clickable { navController.navigate("booknow/${declineBooking.resumeid}") }
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
    val bookingdate = ViewModelSetups.formatDateTime(completedBooking.bookingdate)
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
                        model = completedBooking.tradesmanprofile,
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
                            text = completedBooking.tradesmanfullname,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = nameTextSize,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = completedBooking.tasktype,
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
                                    text = "P${completedBooking.workfee}",
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
                                            completedBooking.ratings == null || completedBooking.ratings == 0f -> "0"
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
                            text = bookingdate,
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
                            .clickable { navController.navigate("booknow/${completedBooking.resumeid}")}
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
                            .clickable { navController.navigate("rateandreviews/${completedBooking.resumeid}/${completedBooking.tradesmanid}") }
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
    val bookingdate = ViewModelSetups.formatDateTime(cancelledBooking.bookingdate)
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
                        model = cancelledBooking.tradesmanprofile,
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
                            text = cancelledBooking.tradesmanfullname,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = nameTextSize,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = cancelledBooking.tasktype,
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
                                    text = "P${cancelledBooking.workfee}/hr",
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
                                            cancelledBooking.ratings == null || cancelledBooking.ratings == 0f -> "0"
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
                            text = bookingdate,
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
                            .clickable { navController.navigate("booknow/${cancelledBooking.resumeid}") }
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
fun AllApplicantsContent() {
    val tradesman = listOf(
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))

        ,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(tradesman.size) { index ->
            val trade = tradesman[index]
            AllApplicantsItem(trade)

        }
    }
}


@Composable
fun PendingApplicantsContent(navController: NavController) {
    val tradesman = listOf(
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))

        ,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(tradesman.size) { index ->
            val trade = tradesman[index]
            PendingApplicantsItem(trade,navController)
        }
    }
}
@Composable
fun DeclinedApplicantsContent(navController: NavController) {
    val tradesman = listOf(
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))

        ,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(tradesman.size) { index ->
            val trade = tradesman[index]
            DeclinedApplicantsItem(trade,navController)
        }
    }
}

@Composable
fun ActiveApplicantsContent(navController: NavController) {
    val tradesman = listOf(
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))

        ,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(tradesman.size) { index ->
            val trade = tradesman[index]
            ActiveApplicantsItem(trade, navController)
        }
    }
}

@Composable
fun CompletedApplicantsContent(navController: NavController) {
    val tradesman = listOf(
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))

        ,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(tradesman.size) { index ->
            val trade = tradesman[index]
            CompletedApplicantsItem(trade, navController )
        }
    }
}
@Composable
fun CancelledApplicantsContent(navController: NavController) {
    val tradesman = listOf(
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))
        ,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(tradesman.size) { index ->
            val trade = tradesman[index]
            CancelledApplicantsItem(trade, navController )
        }
    }
}



//Design For Items
@Composable
fun AllApplicantsItem(trade: Tradesman) {
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
                Image(
                    painter = painterResource(trade.imageResId),
                    contentDescription = "Tradesman Image",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
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
                            text = trade.username,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = nameTextSize
                        )
                        Text(text = "pending", fontSize = taskTextSize)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Service: Plumbing Repair",
                        color = Color.Black,
                        fontSize = taskTextSize
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Date: Jan 15, 2025",
                        color = Color.Black,
                        fontSize = taskTextSize
                    )
                    Text(
                        text = "Time: 10:00 AM",
                        color = Color.Black,
                        fontSize = taskTextSize
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Location: 123 Elm Street",
                        color = Color.Black,
                        fontSize = taskTextSize
                    )
                }
            }
        }
    }
}



@Composable
fun PendingApplicantsItem(trade: Tradesman, navController: NavController) {
    var Cancel by remember { mutableStateOf(false) }
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
        WindowType.SMALL -> 400.dp to 230.dp
        WindowType.MEDIUM -> 410.dp to 240.dp
        WindowType.LARGE -> 420.dp to 250.dp
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
                    Image(
                        painter = painterResource(trade.imageResId),
                        contentDescription = "Tradesman Image",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )

                    // Tradesman details
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = trade.username,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = nameTextSize
                        )



                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Service: Plumbing Repair",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Date: Jan 15, 2025",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Text(
                            text = "Time: 10:00 AM",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Location: 123 Elm Street",
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
                                .clickable { Cancel = true }
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

                            Text(text = " Accept Job", color = Color(0xFFECAB1E), fontSize = nameTextSize)
                        }
                    }
                }

            }

        }
        if (Cancel) {
            Dialog(onDismissRequest = { Cancel = false }) {
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
                                    onClick = { Cancel = false },
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
fun DeclinedApplicantsItem(trade: Tradesman, navController: NavController) {
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 400.dp to 230.dp
        WindowType.MEDIUM -> 410.dp to 240.dp
        WindowType.LARGE -> 420.dp to 250.dp
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
                    Image(
                        painter = painterResource(trade.imageResId),
                        contentDescription = "Tradesman Image",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )

                    // Tradesman details
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = trade.username,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = nameTextSize
                        )



                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Service: Plumbing Repair",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Date: Jan 15, 2025",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Text(
                            text = "Time: 10:00 AM",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Location: 123 Elm Street",
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
fun ActiveApplicantsItem(trade: Tradesman, navController: NavController) {
    var Cancel by remember { mutableStateOf(false) }
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
        WindowType.SMALL -> 400.dp to 230.dp
        WindowType.MEDIUM -> 410.dp to 240.dp
        WindowType.LARGE -> 420.dp to 250.dp
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
                    Image(
                        painter = painterResource(trade.imageResId),
                        contentDescription = "Tradesman Image",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )

                    // Tradesman details
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = trade.username,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = nameTextSize
                        )



                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Service: Plumbing Repair",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Date: Jan 15, 2025",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Text(
                            text = "Time: 10:00 AM",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Location: 123 Elm Street",
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
                                .clickable { Cancel = true }
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
        if (Cancel) {
            Dialog(onDismissRequest = { Cancel = false }) {
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
                                    onClick = { Cancel = false },
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
fun CompletedApplicantsItem(trade: Tradesman, navController: NavController) {
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 400.dp to 230.dp
        WindowType.MEDIUM -> 410.dp to 240.dp
        WindowType.LARGE -> 420.dp to 250.dp
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
                    Image(
                        painter = painterResource(trade.imageResId),
                        contentDescription = "Tradesman Image",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )

                    // Tradesman details
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = trade.username,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = nameTextSize
                        )



                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Service: Plumbing Repair",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Date: Jan 15, 2025",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Text(
                            text = "Time: 10:00 AM",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Location: 123 Elm Street",
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
fun CancelledApplicantsItem(trade: Tradesman, navController: NavController) {
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 400.dp to 230.dp
        WindowType.MEDIUM -> 410.dp to 240.dp
        WindowType.LARGE -> 420.dp to 250.dp
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
                    Image(
                        painter = painterResource(trade.imageResId),
                        contentDescription = "Tradesman Image",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )

                    // Tradesman details
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = trade.username,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = nameTextSize
                        )



                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Service: Plumbing Repair",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Date: Jan 15, 2025",
                            color = Color.Black,
                            fontSize =taskTextSize
                        )
                        Text(
                            text = "Time: 10:00 AM",
                            color = Color.Black,
                            fontSize = taskTextSize
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Location: 123 Elm Street",
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
