package com.example.androidproject.view.client

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.view.theme.myGradient3
import com.example.androidproject.viewmodel.bookings.GetClientBookingViewModel

@Composable
fun ClientDetails(modifier: Modifier = Modifier, resumeId: String,status:String, getClientBooking: GetClientBookingViewModel, navController: NavController) {
    val resumeID = resumeId.toIntOrNull() ?: return
    val bookingStatus = status.ifEmpty { return }

    val bookingPendingState = getClientBooking.ClientBookingPagingData.collectAsLazyPagingItems()
    LaunchedEffect(Unit) {
        bookingPendingState.refresh()
    }

    // Find the booking with the matching jobId and "Pending" status
    val selectedBooking = bookingPendingState.itemSnapshotList.items
        .firstOrNull { it.id == resumeID}
    val windowSize = rememberWindowSizeClass()
    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 16.sp
        WindowType.MEDIUM -> 18.sp
        WindowType.LARGE -> 20.sp
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)
            )
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            shape = RoundedCornerShape(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
            ) {
                Row(modifier = Modifier
                    .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable {
                            when(bookingStatus){
                                "Pending" -> navController.navigate("main_screen?selectedItem=1&selectedTab=1&selectedSection=0") {
                                    navController.popBackStack()
                                }
                                "Active" -> navController.navigate("main_screen?selectedItem=1&selectedTab=3&selectedSection=0") {
                                    navController.popBackStack()
                                }
                                "Completed" -> navController.navigate("main_screen?selectedItem=1&selectedTab=4&selectedSection=0") {
                                    navController.popBackStack()
                                }
                                "Declined" -> navController.navigate("main_screen?selectedItem=1&selectedTab=3&selectedSection=0") {
                                    navController.popBackStack()
                                }
                                "Cancelled" -> navController.navigate("main_screen?selectedItem=1&selectedTab=5&selectedSection=0") {
                                    navController.popBackStack()
                                }

                            }
                        },
                        tint = Color(0xFF81D796)
                    )
                    Text(
                        text = "Booking Details",
                        fontSize = 24.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.weight(1f)
                            .padding(start = 8.dp)
                    )
                }
            }
        }


        Column(modifier = Modifier.weight(1f)){

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(brush = myGradient3)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Your booking is ${bookingStatus}",
                            fontSize = nameTextSize,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(200.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    shape = RoundedCornerShape(0.dp, 0.dp, 15.dp, 15.dp) // Keep card shape
                ) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 18.dp, horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Tradesman’s Information",
                            fontSize = nameTextSize,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                        )
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            thickness = 0.5.dp,
                            color = Color.Gray
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (selectedBooking != null) {
                                AsyncImage(
                                    model =selectedBooking.tradesmanProfile ,
                                    contentDescription = "Client Image",
                                    modifier = Modifier
                                        .size(100.dp)
                                )
                            }
                            // Tradesman details
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 10.dp)
                            ) {
                                if (selectedBooking != null) {
                                    Text(
                                        text = selectedBooking.tradesmanFullName,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = nameTextSize,
                                    )
                                }
                                if (selectedBooking != null) {
                                    Text(
                                        text = selectedBooking.phoneNumber,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = smallTextSize,
                                    )
                                }


                                if (selectedBooking != null) {
                                    Text(
                                        text = selectedBooking.address,
                                        color = Color.Gray,
                                        fontSize = smallTextSize,
                                    )
                                }
                            }
                        }

                    }

                }
                Spacer(Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(Color.White),
                        shape = RoundedCornerShape(15.dp) // Keep card shape
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(

                                text = "Task Information",
                                fontSize = nameTextSize,
                                color = Color.Black,
                                fontWeight = FontWeight.Medium
                            )
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                thickness = 0.5.dp,
                                color = Color.Gray
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Job Date",
                                        modifier = Modifier
                                            .size(32.dp)
                                    )
                                    Text(
                                        text = "Job Date:",
                                        fontSize = nameTextSize,
                                        color = Color.Black,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                    if (selectedBooking != null) {
                                        Text(
                                            text = selectedBooking.bookingDate,
                                            fontSize = nameTextSize,
                                            color = Color.Black,
                                            modifier = Modifier.padding(start = 10.dp)
                                        )
                                    }
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Work,
                                            contentDescription = "Help Icon",
                                            modifier = Modifier
                                                .size(32.dp)
                                        )
                                        Text(
                                            text = "Optional Details:",
                                            fontSize = nameTextSize,
                                            color = Color.Black,
                                            modifier = Modifier.padding(start = 10.dp)
                                        )

                                    }
                                    Spacer(Modifier.height(10.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFFF5F5F5))
                                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                                    ) {
                                        if (selectedBooking != null) {
                                            Text(modifier = Modifier.padding(16.dp),text = selectedBooking.taskDescription, fontSize = nameTextSize, color = Color.Black)
                                        }
                                    }
                                }

                            }

                        }

                    }
                }
                Spacer(Modifier.height(10.dp))

                // Third Column with Card and content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(Color.White),
                        shape = RoundedCornerShape(15.dp) // Keep card shape
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(

                                text = "Support Center",
                                fontSize = nameTextSize,
                                color = Color.Black,
                                fontWeight = FontWeight.Medium
                            )
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                thickness = 0.5.dp,
                                color = Color.Gray
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Message,
                                        contentDescription = "Message Icon",
                                        modifier = Modifier
                                            .size(32.dp)
                                    )
                                    Text(
                                        text = "Contact Tradesman",
                                        fontSize = nameTextSize,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "Arrow Right Icon",
                                    modifier = Modifier
                                        .size(32.dp)
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Help,
                                        contentDescription = "Help Icon",
                                        modifier = Modifier
                                            .size(32.dp)
                                    )
                                    Text(
                                        text = "Help",
                                        fontSize = nameTextSize,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "Arrow Right Icon",
                                    modifier = Modifier
                                        .size(32.dp)
                                )
                            }
                        }

                    }
                }

                Spacer(Modifier.height(10.dp))
                when(bookingStatus) {
                    "Pending", "Active", "Completed" ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    when(bookingStatus){
                                        "Pending" -> navController.navigate("main_screen?selectedItem=1&selectedTab=1&selectedSection=0") {
                                            navController.popBackStack()
                                        }
                                        "Active" -> navController.navigate("main_screen?selectedItem=1&selectedTab=3&selectedSection=0") {
                                            navController.popBackStack()
                                        }
                                        "Completed" -> navController.navigate("main_screen?selectedItem=1&selectedTab=4&selectedSection=0") {
                                            navController.popBackStack()
                                        }
                                    }

                                }
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "OK", fontSize = nameTextSize)
                        }
                    "Cancelled","Declined"->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("clientdeclinationdetails/${resumeID}/${bookingStatus}")
                                }
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "${bookingStatus} Details", fontSize = nameTextSize)
                        }

                }

            }

        }
    }

}

