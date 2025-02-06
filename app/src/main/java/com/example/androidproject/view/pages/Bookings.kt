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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidproject.R
import com.example.androidproject.ViewModelSetups
import com.example.androidproject.model.client.GetClientsBooking
import com.example.androidproject.view.Tradesman
import com.example.androidproject.view.WindowSize
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
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

    // Tab titles
    val tabTitles = listOf("All", "Pending", "Active", "Completed", "Cancelled")
    Box (
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            BookingsTopSection(navController,windowSize)
            Column(modifier = Modifier.fillMaxSize()) {
                // Tabs (Fixed Choices)
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.fillMaxWidth()
                        ,
                    edgePadding = 5.dp
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title, fontSize = textSize)},
                            modifier = Modifier.background(Color.White)
                        )
                    }
                }

                // Content changes based on the selected tab
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFD9D9D9))

                        .padding(16.dp)
                ) {
                    when (selectedTabIndex) {
                        0 -> AllBookingsContent(getClientsBooking,navController)
                        1 -> PendingBookingsContent(getClientsBooking,navController)
                        2 -> ActiveBookingsContent(getClientsBooking)
                        3 -> CompletedBookingsContent(getClientsBooking,navController)
                        4 -> CancelledBookingsContent(getClientsBooking,navController)
                    }
                }
            }
        }

    }

}
@Composable
fun BookingsTopSection(navController: NavController,windowSize: WindowSize) {
    val fontSize = when (windowSize.width) {
        WindowType.SMALL -> 24.sp
        WindowType.MEDIUM -> 28.sp
        WindowType.LARGE -> 32.sp
    }

    val iconSize = when (windowSize.width) {
        WindowType.SMALL -> 32.dp
        WindowType.MEDIUM -> 34.dp
        WindowType.LARGE -> 36.dp
    }

    val horizontalSpacing = when (windowSize.width) {
        WindowType.SMALL -> 16.dp
        WindowType.MEDIUM -> 20.dp
        WindowType.LARGE -> 24.dp
    }

    Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .height(70.dp)

            ,
            horizontalArrangement = Arrangement.spacedBy(140.dp),
        ) {
            Text(text="My Bookings ",
                fontSize = fontSize,
                fontWeight =
                FontWeight(500),
                modifier = Modifier.padding(16.dp),



            )
            Row (modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)){
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications Icon",
                    tint = Color(0xFF3CC0B0),
                    modifier = Modifier
                        .size(32.dp)

                )
                Icon(
                    imageVector = Icons.Default.Message,
                    contentDescription = "Message Icon",
                    tint = Color(0xFF3CC0B0),
                    modifier = Modifier
                        .size(iconSize)

                        .clickable {
                            navController.navigate("message_screen")

                        }
                )
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
            val errorMessage = (clientbookingState as GetClientBookingViewModel.GetClientBookings.Error).message
            Text(text = "Error: $errorMessage")
            Log.d("bookerror", errorMessage)
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
                    val bookings = pendingBookings[index]
                    PendingItem(bookings,navController)
                }
            }

        }
        is GetClientBookingViewModel.GetClientBookings.Error -> {
            val errorMessage = (clientbookingState as GetClientBookingViewModel.GetClientBookings.Error).message
            Text(text = "Error: $errorMessage")
            Log.d("bookerror", errorMessage)
        }
        else ->  Unit

    }

}

@Composable
fun ActiveBookingsContent(getClientBooking: GetClientBookingViewModel) {
    val clientbookingState by getClientBooking.clientbookingState.collectAsState()

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
                    val booking = ActiveBookings[index]
                    ActiveItems(booking)
                }
            }

        }
        is GetClientBookingViewModel.GetClientBookings.Error -> {
            val errorMessage = (clientbookingState as GetClientBookingViewModel.GetClientBookings.Error).message
            Text(text = "Error: $errorMessage")
            Log.d("bookerror", errorMessage)
        }
        else ->  Unit
    }

}

@Composable
fun CompletedBookingsContent(getClientBooking: GetClientBookingViewModel,navController: NavController) {
    val clientbookingState by getClientBooking.clientbookingState.collectAsState()
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
                    val booking = completedBookings[index]
                    CompletedItem(booking, navController )
                }
            }
        }
        is GetClientBookingViewModel.GetClientBookings.Error -> {
            val errorMessage = (clientbookingState as GetClientBookingViewModel.GetClientBookings.Error).message
            Text(text = "Error: $errorMessage")
            Log.d("bookerror", errorMessage)
        }
        else ->  Unit
    }

}
@Composable
fun CancelledBookingsContent(getClientBooking: GetClientBookingViewModel,navController: NavController) {
    val clientbookingState by getClientBooking.clientbookingState.collectAsState()

    when(clientbookingState){
        is GetClientBookingViewModel.GetClientBookings.Loading ->{
            //do nothing
        }
        is GetClientBookingViewModel.GetClientBookings.Success ->{
            val booking = (clientbookingState as GetClientBookingViewModel.GetClientBookings.Success).data
            val cancelledBookings = booking.filter { it.bookingstatus == "Failed" }
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
                    val booking = cancelledBookings[index]
                    CancelledItem(booking, navController )
                }
            }
        }
        is GetClientBookingViewModel.GetClientBookings.Error -> {
            val errorMessage = (clientbookingState as GetClientBookingViewModel.GetClientBookings.Error).message
            Text(text = "Error: $errorMessage")
            Log.d("bookerror", errorMessage)
        }
        else ->  Unit
    }

}



//Design For Items
@Composable
fun AllItem(Booking : GetClientsBooking,navController: NavController) {
    val getbookdate = ViewModelSetups.formatDateTime(Booking.bookingdate)
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
                        text = Booking.tradesmanfullname,
                        color = Color.Black,
                        fontWeight = FontWeight(500),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    Text(
                        text = Booking.tasktype,
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
                                text = "P ${Booking.workfee}/hr",
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
                        text = Booking.bookingstatus,
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
fun ActiveItems(booking: GetClientsBooking) {
    val getbookdate = ViewModelSetups.formatDateTime(booking.bookingdate)
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
                        text = booking.tradesmanfullname,
                        color = Color.Black,
                        fontWeight = FontWeight(500),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    Text(
                        text = booking.tasktype,
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
                                text = "P${booking.workfee}/hr",
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

                Text(
                    text = "All",
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


@Composable
fun PendingItem(booking : GetClientsBooking, navController:NavController) {
    val getbookdate = ViewModelSetups.formatDateTime(booking.bookingdate)
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
                            text = booking.tradesmanfullname,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = 20.sp,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = booking.tasktype,
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
                                    text = "P${booking.workfee}/hr",
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
                    Text(
                        text = "All",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 50.dp)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
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
fun CompletedItem(booking: GetClientsBooking, navController:NavController) {
    val getbookdate = ViewModelSetups.formatDateTime(booking.bookingdate)
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
                            text = booking.tradesmanfullname,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = 20.sp,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = booking.tasktype,
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
                                    text = "P${booking.workfee}",
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
                    Text(
                        text = "All",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 50.dp)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
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
fun CancelledItem(booking: GetClientsBooking, navController:NavController) {
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
                            text = booking.tradesmanfullname,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = 20.sp,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = booking.tasktype,
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
                                    text = "P${booking.workfee}/hr",
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
                    Text(
                        text = "All",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 50.dp)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
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
