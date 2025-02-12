package com.example.androidproject.view.pages

import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.ViewModelSetups
import com.example.androidproject.model.client.GetClientsBooking
import com.example.androidproject.view.Tradesman
import com.example.androidproject.view.WindowSize
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.pages2.ActiveApplicantsTradesmanContent
import com.example.androidproject.view.pages2.ActiveBookingsTradesmanContent
import com.example.androidproject.view.pages2.AllApplicantsTradesmanContent
import com.example.androidproject.view.pages2.AllBookingsTradesmanContent
import com.example.androidproject.view.pages2.CancelledApplicantsTradesmanContent
import com.example.androidproject.view.pages2.CancelledBookingsTradesmanContent
import com.example.androidproject.view.pages2.CompletedApplicantsTradesmanContent
import com.example.androidproject.view.pages2.CompletedBookingsTradesmanContent
import com.example.androidproject.view.pages2.DeclinedApplicantsTradesmanContent
import com.example.androidproject.view.pages2.DeclinedBookingsTradesmanContent
import com.example.androidproject.view.pages2.JobsTradesmanTopSection
import com.example.androidproject.view.pages2.PendingApplicantsTradesmanContent
import com.example.androidproject.view.pages2.PendingBookingsTradesmanContent
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.view.theme.myGradient3
import com.example.androidproject.viewmodel.bookings.GetClientBookingViewModel


@Composable
fun BookingsScreen(modifier: Modifier = Modifier,navController: NavController,getClientsBooking: GetClientBookingViewModel) {



    Log.i("Screen" , "BookingsScreen")
    val windowSize = rememberWindowSizeClass()
    val textSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }
    var selectedTabIndex by remember { mutableStateOf(0) }
    var selectedSection by remember { mutableStateOf("My Bookings") }

    // Define tab titles based on selected section
    val myJobsTabs = listOf("All", "Pending", "Declined", "Active", "Completed", "Cancelled")
    val myApplicantsTabs = listOf("All", "Pending", "Declined", "Active", "Completed", "Cancelled")
    val tabTitles = if (selectedSection == "My Jobs") myJobsTabs else myApplicantsTabs
    Box (
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            BookingsTopSection(navController, selectedSection) { section ->
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
                        .padding(16.dp)
                ) {
                    when (selectedSection) {
                        "My Bookings" -> when (selectedTabIndex) {
                                0 -> AllBookingsContent(getClientsBooking,navController)
                                1 -> PendingBookingsContent(getClientsBooking,navController)
                                2 -> DeclinedBookingsContent(getClientsBooking,navController)
                                3 -> ActiveBookingsContent(getClientsBooking)
                                4 -> CompletedBookingsContent(getClientsBooking,navController)
                                5 -> CancelledBookingsContent(getClientsBooking,navController)

                        }
                        "My Applicant" -> when (selectedTabIndex) {
                            0 -> AllApplicantsContent()
                            1 -> PendingApplicantsContent(navController)
                            2 -> DeclinedApplicantsContent(navController)
                            3 -> ActiveApplicantsContent()
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)

        ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left-aligned clickable text with box
        Box(
            modifier = Modifier
                .background(if (selectedSection == "My Bookings") myGradient3 else SolidColor(Color.Transparent))
                .padding(4.dp)
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            TextButton(
                onClick = { onSectionSelected("My Bookings") },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (selectedSection == "My Bookings") Color.White else Color.Black
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "My Bookings",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Right-aligned clickable text with box
        Box(
            modifier = Modifier
                .background(if (selectedSection == "My Applicant") myGradient3 else SolidColor(Color.Transparent))
                .padding(4.dp)
                .weight(1f),
        ) {
            TextButton(
                onClick = { onSectionSelected("My Applicant") },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (selectedSection == "My Applicant") Color.White else Color.Black
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "My Applicant",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


@Composable
fun AllBookingsContent(getClientsBooking: GetClientBookingViewModel,navController: NavController) {
    val clientbookingState by getClientsBooking.clientbookingState.collectAsState()

    LaunchedEffect(Unit) {
        getClientsBooking.getClientBookings()
    }
    when(clientbookingState){
        is GetClientBookingViewModel.GetClientBookings.Loading ->{
            //do nothing
        }
        is GetClientBookingViewModel.GetClientBookings.Success ->{
        val booking = (clientbookingState as GetClientBookingViewModel.GetClientBookings.Success).data
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .size(420.dp)
                    .background(Color(0xFFD9D9D9))

                ,
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(booking.size) { index ->
                    val bookings = booking[index]
                    AllItem(bookings,navController)
                }
            }
        }
        is GetClientBookingViewModel.GetClientBookings.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center, // Centers vertically
                horizontalAlignment = Alignment.CenterHorizontally // Centers horizontally
            ) {
                Text(
                    text = "NO BOOKINGS",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
        else ->  Unit
    }

}


@Composable
fun PendingBookingsContent(getClientBooking: GetClientBookingViewModel, navController:NavController) {
    val clientbookingState by getClientBooking.clientbookingState.collectAsState()

    LaunchedEffect(Unit) {
        getClientBooking.getClientBookings()
    }
    when(clientbookingState){
        is GetClientBookingViewModel.GetClientBookings.Loading ->{
            //do nothing
        }
        is GetClientBookingViewModel.GetClientBookings.Success ->{
            val booking = (clientbookingState as GetClientBookingViewModel.GetClientBookings.Success).data
            val pendingBookings = booking.filter { it.bookingstatus == "Pending" }
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
        is GetClientBookingViewModel.GetClientBookings.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center, // Centers vertically
                horizontalAlignment = Alignment.CenterHorizontally // Centers horizontally
            ) {
                Text(
                    text = "NO BOOKINGS",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
        else ->  Unit

    }

}
@Composable
fun DeclinedBookingsContent(getClientBooking: GetClientBookingViewModel,navController: NavController) {
    val clientbookingState by getClientBooking.clientbookingState.collectAsState()

    LaunchedEffect(Unit) {
        getClientBooking.getClientBookings()
    }

    when(clientbookingState){
        is GetClientBookingViewModel.GetClientBookings.Loading ->{
            //do nothing
        }
        is GetClientBookingViewModel.GetClientBookings.Success ->{
            val booking = (clientbookingState as GetClientBookingViewModel.GetClientBookings.Success).data
            val declinedBookings = booking.filter { it.bookingstatus == "Declined" }
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
        is GetClientBookingViewModel.GetClientBookings.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center, // Centers vertically
                horizontalAlignment = Alignment.CenterHorizontally // Centers horizontally
            ) {
                Text(
                    text = "NO BOOKINGS",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
        else ->  Unit
    }

}

@Composable
fun ActiveBookingsContent(getClientBooking: GetClientBookingViewModel) {
    val clientbookingState by getClientBooking.clientbookingState.collectAsState()

    LaunchedEffect(Unit) {
        getClientBooking.getClientBookings()
    }

    when(clientbookingState){
        is GetClientBookingViewModel.GetClientBookings.Loading ->{
            //do nothing
        }
        is GetClientBookingViewModel.GetClientBookings.Success ->{
            val booking = (clientbookingState as GetClientBookingViewModel.GetClientBookings.Success).data

            val ActiveBookings = booking.filter { it.bookingstatus == "Active" }
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
                    ActiveItems(activebooking)
                }
            }

        }
        is GetClientBookingViewModel.GetClientBookings.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center, // Centers vertically
                horizontalAlignment = Alignment.CenterHorizontally // Centers horizontally
            ) {
                Text(
                    text = "NO BOOKINGS",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
        else ->  Unit
    }

}

@Composable
fun CompletedBookingsContent(getClientBooking: GetClientBookingViewModel,navController: NavController) {
    val clientbookingState by getClientBooking.clientbookingState.collectAsState()
    LaunchedEffect(Unit) {
        getClientBooking.getClientBookings()
    }

    when(clientbookingState){
        is GetClientBookingViewModel.GetClientBookings.Loading ->{
            //do nothing
        }
        is GetClientBookingViewModel.GetClientBookings.Success ->{
            val booking = (clientbookingState as GetClientBookingViewModel.GetClientBookings.Success).data
            val completedBookings = booking.filter { it.bookingstatus == "Completed" }
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
        is GetClientBookingViewModel.GetClientBookings.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center, // Centers vertically
                horizontalAlignment = Alignment.CenterHorizontally // Centers horizontally
            ) {
                Text(
                    text = "NO BOOKINGS",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
        else ->  Unit
    }

}
@Composable
fun CancelledBookingsContent(getClientBooking: GetClientBookingViewModel,navController: NavController) {
    val clientbookingState by getClientBooking.clientbookingState.collectAsState()

    LaunchedEffect(Unit) {
        getClientBooking.getClientBookings()
    }

    when(clientbookingState){
        is GetClientBookingViewModel.GetClientBookings.Loading ->{
            //do nothing
        }
        is GetClientBookingViewModel.GetClientBookings.Success ->{
            val booking = (clientbookingState as GetClientBookingViewModel.GetClientBookings.Success).data
            val cancelledBookings = booking.filter { it.bookingstatus == "Cancelled" }
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .size(420.dp)
                    .background(Color(0xFFD9D9D9))
                ,
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(cancelledBookings.size) { index ->
                    val cancelledbooking = cancelledBookings[index]
                    CancelledItem(cancelledbooking, navController )
                }
            }
        }
        is GetClientBookingViewModel.GetClientBookings.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center, // Centers vertically
                horizontalAlignment = Alignment.CenterHorizontally // Centers horizontally
            ) {
                Text(
                    text = "NO BOOKINGS",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
        else ->  Unit
    }

}





//Design For Items
@Composable
fun AllItem(allBooking : GetClientsBooking,navController: NavController) {
    val getbookdate = ViewModelSetups.formatDateTime(allBooking.bookingdate)
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 390.dp to 170.dp
        WindowType.MEDIUM -> 400.dp to 180.dp
        WindowType.LARGE -> 410.dp to 190.dp
    }
    Card(
        modifier = Modifier
            .size(cardHeight.first,cardHeight.second)
            .clickable { }, // Add implementation for click if needed
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
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
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    Text(
                        text = allBooking.tasktype,
                        color = Color.Black,
                        fontSize = 16.sp,
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
                                text = "P ${allBooking.workfee}/hr",
                                fontSize = 14.sp,
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
                                    text = "4.5",
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                    Text(
                        text = "Weekdays Selected",
                        color = Color.Black,
                        fontSize = 16.sp,

                        )
                    Text(
                        text = getbookdate,
                        color = Color.Gray,
                        fontSize = 14.sp,

                        )
                }

                    Text(
                        text = allBooking.bookingstatus,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 50.dp)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )

            }
        }
    }
}


//Design for activeItems
@Composable
fun ActiveItems(activeBooking: GetClientsBooking) {
    val getbookdate = ViewModelSetups.formatDateTime(activeBooking.bookingdate)
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 390.dp to 170.dp
        WindowType.MEDIUM -> 400.dp to 180.dp
        WindowType.LARGE -> 410.dp to 190.dp
    }
    Card(
        modifier = Modifier
            .size(cardHeight.first,cardHeight.second)
            .clickable { }, // Add implementation for click if needed
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
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
                    model = activeBooking.tradesmanprofile,
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
                        text = activeBooking.tradesmanfullname,
                        color = Color.Black,
                        fontWeight = FontWeight(500),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    Text(
                        text = activeBooking.tasktype,
                        color = Color.Black,
                        fontSize = 16.sp,
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
                                text = "P${activeBooking.workfee}/hr",
                                fontSize = 14.sp,
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
                                    text = "4",
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                    Text(
                        text = "Weekdays Selected",
                        color = Color.Black,
                        fontSize = 16.sp,

                        )
                    Text(
                        text = getbookdate,
                        color = Color.Gray,
                        fontSize = 14.sp,

                        )
                }



            }
        }
    }
}


@Composable
fun PendingItem(pendingBooking : GetClientsBooking, navController:NavController) {
    val getbookdate = ViewModelSetups.formatDateTime(pendingBooking.bookingdate)
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 380.dp to 240.dp
        WindowType.MEDIUM -> 390.dp to 250.dp
        WindowType.LARGE -> 400.dp to 260.dp
    }
    Card(
        modifier = Modifier
            .size(cardHeight.first,cardHeight.second)
            , // Add implementation for click if needed
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
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
                            fontSize = 20.sp,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = pendingBooking.tasktype,
                            color = Color.Black,
                            fontSize = 16.sp,
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
                                    fontSize = 14.sp,
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
                                        text = "4",
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Weekdays Selected",
                            color = Color.Black,
                            fontSize = 16.sp,
                        )
                        Text(
                            text = getbookdate,
                            color = Color.Gray,
                            fontSize = 14.sp,
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
                            .clickable { /* Chat Action */ }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {


                            Text(text = "Cancel Appointment", fontSize = 14.sp)

                    }

                    Box(
                        modifier = Modifier
                            .clickable { navController.navigate("bookingdetails") }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color(0xFFECAB1E), shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {

                            Text(text = "Booking Details", color = Color(0xFFECAB1E), fontSize = 14.sp)
                        }
                }
            }
        }
    }
}
@Composable
fun DeclinedItem(declineBooking: GetClientsBooking, navController:NavController) {
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 380.dp to 240.dp
        WindowType.MEDIUM -> 390.dp to 250.dp
        WindowType.LARGE -> 400.dp to 260.dp
    }
    Card(
        modifier = Modifier
            .size(cardHeight.first, cardHeight.second), // Add implementation for click if needed
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
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
                            fontSize = 20.sp,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = declineBooking.tasktype,
                            color = Color.Black,
                            fontSize = 16.sp,
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
                                    fontSize = 14.sp,
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
                                        text = "4",
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Weekdays Selected",
                            color = Color.Black,
                            fontSize = 16.sp,
                        )
                        Text(
                            text = "Monday",
                            color = Color.Gray,
                            fontSize = 14.sp,
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
                            .clickable { navController.navigate("cancellationdetails") }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {


                        Text(text = "Cancellation  Details", fontSize = 14.sp)

                    }

                    Box(
                        modifier = Modifier
                            .clickable { navController.navigate("booknow") }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color(0xFFECAB1E), shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(text = "Book Again", color = Color(0xFFECAB1E), fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
@Composable
fun CompletedItem(completedBooking: GetClientsBooking, navController:NavController) {
    val getbookdate = ViewModelSetups.formatDateTime(completedBooking.bookingdate)
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 380.dp to 240.dp
        WindowType.MEDIUM -> 390.dp to 250.dp
        WindowType.LARGE -> 400.dp to 260.dp
    }
    Card(
        modifier = Modifier
            .size(cardHeight.first, cardHeight.second)
        , // Add implementation for click if needed
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
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
                            fontSize = 20.sp,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = completedBooking.tasktype,
                            color = Color.Black,
                            fontSize = 16.sp,
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
                                    fontSize = 14.sp,
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
                                        text = "4",
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Weekdays Selected",
                            color = Color.Black,
                            fontSize = 16.sp,
                        )
                        Text(
                            text = getbookdate,
                            color = Color.Gray,
                            fontSize = 14.sp,
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
                            .clickable { navController.navigate("booknow")}
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {


                        Text(text = "Book Again", fontSize = 14.sp)

                    }

                    Box(
                        modifier = Modifier
                            .clickable { navController.navigate("rateandreviews") }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color(0xFFECAB1E), shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(text = "Rate", color = Color(0xFFECAB1E), fontSize = 14.sp)
                    }
                }
            }
        }
    }
}


@Composable
fun CancelledItem(cancelledBooking: GetClientsBooking, navController:NavController) {
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 380.dp to 240.dp
        WindowType.MEDIUM -> 390.dp to 250.dp
        WindowType.LARGE -> 400.dp to 260.dp
    }
    Card(
        modifier = Modifier
            .size(cardHeight.first, cardHeight.second)
        , // Add implementation for click if needed
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
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
                            fontSize = 20.sp,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = cancelledBooking.tasktype,
                            color = Color.Black,
                            fontSize = 16.sp,
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
                                    fontSize = 14.sp,
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
                                        text = "4",
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Weekdays Selected",
                            color = Color.Black,
                            fontSize = 16.sp,
                        )
                        Text(
                            text = "Monday",
                            color = Color.Gray,
                            fontSize = 14.sp,
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


                        Text(text = "Cancellation  Details", fontSize = 14.sp)

                    }

                    Box(
                        modifier = Modifier
                            .clickable { navController.navigate("booknow") }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color(0xFFECAB1E), shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(text = "Book Again", color = Color(0xFFECAB1E), fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

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
fun ActiveApplicantsContent() {
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
        WindowType.SMALL -> 390.dp to 190.dp
        WindowType.MEDIUM -> 400.dp to 200.dp
        WindowType.LARGE -> 410.dp to 210.dp
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
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Service: Plumbing Repair",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Date: Jan 15, 2025",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Time: 10:00 AM",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Location: 123 Elm Street",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}


@Composable
fun PendingApplicantsItem(trade: Tradesman, navController: NavController) {
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 390.dp to 190.dp
        WindowType.MEDIUM -> 400.dp to 200.dp
        WindowType.LARGE -> 410.dp to 210.dp
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
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Service: Plumbing Repair",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Date: Jan 15, 2025",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Time: 10:00 AM",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Location: 123 Elm Street",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                }

            }
        }
    }
}
@Composable
fun DeclinedApplicantsItem(trade: Tradesman, navController: NavController) {
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 390.dp to 190.dp
        WindowType.MEDIUM -> 400.dp to 200.dp
        WindowType.LARGE -> 410.dp to 210.dp
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
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Service: Plumbing Repair",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Date: Jan 15, 2025",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Time: 10:00 AM",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Location: 123 Elm Street",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                }

            }
        }
    }
}
@Composable
fun CompletedApplicantsItem(trade: Tradesman, navController: NavController) {
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 390.dp to 190.dp
        WindowType.MEDIUM -> 400.dp to 200.dp
        WindowType.LARGE -> 410.dp to 210.dp
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
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Service: Plumbing Repair",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Date: Jan 15, 2025",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Time: 10:00 AM",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Location: 123 Elm Street",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                }


            }
        }
    }
}


@Composable
fun CancelledApplicantsItem(trade: Tradesman, navController: NavController) {
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 390.dp to 190.dp
        WindowType.MEDIUM -> 400.dp to 200.dp
        WindowType.LARGE -> 410.dp to 210.dp
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
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Service: Plumbing Repair",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Date: Jan 15, 2025",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Time: 10:00 AM",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Location: 123 Elm Street",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
