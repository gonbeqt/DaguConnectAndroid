package com.example.androidproject.view.tradesman

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.androidproject.R
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.viewmodel.bookings.GetClientBookingViewModel
import com.example.androidproject.viewmodel.bookings.GetTradesmanBookingViewModel


@Composable
fun TradesmanDeclinationDetails(jobId: String,status:String, modifier: Modifier = Modifier, navController: NavController ,getTradesmanBooking: GetTradesmanBookingViewModel
) {

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
    val jobID = jobId.toIntOrNull() ?: return
    val bookingStatus = status.ifEmpty { return }

    val bookingDeclinedState = getTradesmanBooking.TradesmanBookingPagingData.collectAsLazyPagingItems()
    LaunchedEffect(Unit) {
        bookingDeclinedState.refresh()
    }

    // Find the booking with the matching jobId and "Pending" status
    val selectedBooking = bookingDeclinedState.itemSnapshotList.items
        .firstOrNull { it.id == jobID }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
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
                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable {navController.popBackStack()},
                        tint = Color(0xFF81D796)
                    )
                    Text(
                        text = "${bookingStatus} Details",
                        fontSize = 24.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.weight(1f)
                            .padding(start = 8.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Column(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center

                ) {
                    Icon(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .size(100.dp),
                        painter = painterResource(id = R.drawable.service_unavailable_ic),
                        contentDescription = "Job has been decline",
                        tint = Color(0xFF42C2AE)
                    )
                    Text(
                        modifier = Modifier
                            .padding(0.dp, 10.dp, 0.dp, 20.dp),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF42C2AE),
                        fontSize = 20.sp,
                        text = " Job offer ${bookingStatus.lowercase()}"
                    )
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        thickness = 0.5.dp,
                        color = Color.Gray
                    )
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                fontWeight = FontWeight.Normal,
                                fontSize = smallTextSize,
                                color = Color.Gray,
                                text = "Requested by"
                            )
                            if (selectedBooking != null) {
                                Text(
                                    fontSize = smallTextSize,
                                    text = selectedBooking.clientFullName
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                fontWeight = FontWeight.Normal,
                                fontSize = smallTextSize,
                                color = Color.Gray,
                                text = "Request Date"
                            )
                            if (selectedBooking != null) {
                                Text(
                                    fontSize = smallTextSize,
                                    text = selectedBooking.bookingDateStatus
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                fontWeight = FontWeight.Normal,
                                fontSize = smallTextSize,
                                color = Color.Gray,
                                text = "Reason"
                            )
                            if (selectedBooking != null) {
                                Text(
                                    fontSize = smallTextSize,
                                    text = selectedBooking.cancelReason
                                )
                            }
                        }
                    }

                }
            }

            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (selectedBooking != null) {
                            navController.navigate("tradesmandetails/${jobID}/${bookingStatus}")
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
                Text(text = "Job Details", fontSize = nameTextSize)
            }
        }

    }
}

