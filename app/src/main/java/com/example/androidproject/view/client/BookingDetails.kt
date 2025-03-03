package com.example.androidproject.view.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.ViewModelSetups
import com.example.androidproject.view.theme.myGradient3
import com.example.androidproject.viewmodel.bookings.ViewClientBookingViewModel

@Composable
fun BookingDetails(viewClientBookingViewModel: ViewClientBookingViewModel, navController: NavController,resumeId: String) {
    val resumeId = resumeId.toIntOrNull()?: return
    val viewClientBookingstate by viewClientBookingViewModel.viewClientBookingState.collectAsState()

    LaunchedEffect(Unit) {
        viewClientBookingViewModel.viewClientBooking(resumeId)
    }

    when (val viewClientBooking = viewClientBookingstate) {
        is ViewClientBookingViewModel.ViewClientBookings.Loading -> {
            // Show loading state
        }
        is ViewClientBookingViewModel.ViewClientBookings.Success -> {
            val clientbooking = viewClientBooking.data
            val getbookdate = ViewModelSetups.formatDateTime(clientbooking.bookingDate)
            Column( // Change Box to Column
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFD9D9D9))
            ) {
                // Main Content Area (Scrollable)

                // Header Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .verticalScroll(rememberScrollState()),
                    shape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp) // Rounded top corners
                ) {

                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .size(100.dp)
                            .padding(top = 20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Arrow Back",
                                Modifier
                                    .clickable { navController.popBackStack() }
                                    .padding(16.dp),
                                tint = Color(0xFF81D796)
                            )


                            Text(
                                text = "Bookings Details",
                                fontSize = 24.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Left,
                                modifier = Modifier
                                    .padding(top = 15.dp)
                                    .weight(1f) // Ensures the text takes available space and is centered
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp)) // Space between the two columns

                // Second Column with Card and content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp) // Padding to separate it from the top content
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp) // Keep card shape
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(brush = myGradient3) // Apply gradient background here
                                .padding(16.dp),
                            contentAlignment = Alignment.Center // Ensure padding is inside the gradient box
                        ) {
                            Text(
                                text = "Your appointment is Pending Approval",
                                fontSize = 20.sp,
                                color = Color.White,
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
                                    model = clientbooking.tradesmanProfile,
                                    contentDescription = "Tradesman Image",
                                    modifier = Modifier
                                        .size(120.dp)
                                        .padding(start = 10.dp)
                                )

                                // Tradesman details
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 20.dp)
                                ) {
                                    Text(
                                        text = clientbooking.tradesmanFullName,
                                        color = Color.Black,
                                        fontWeight = FontWeight(500),
                                        fontSize = 20.sp,
                                        modifier = Modifier.padding(top = 10.dp)
                                    )
                                    Text(
                                        text = clientbooking.taskType,
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
                                                text = "P${clientbooking.workFee}/hr",
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
                                                    text = "4.5",
                                                    fontSize = 14.sp
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "Date Selected",
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
                    Spacer(Modifier.height(16.dp))

                    // Second Column with Card and content
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
                                    text = "Client’s Information",
                                    fontSize = 18.sp,
                                    color = Color.Black,
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                        .padding(vertical = 20.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Tradesman image
                                    AsyncImage(
                                        model = clientbooking.clientProfile,
                                        contentDescription = "Tradesman Image",
                                        modifier = Modifier
                                            .size(100.dp)
                                    )

                                    // Tradesman details
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(start = 10.dp)
                                    ) {
                                        Row(Modifier.fillMaxWidth()) {
                                            Text(
                                                text =clientbooking.clientFullName,
                                                color = Color.Black,
                                                fontWeight = FontWeight(500),
                                                fontSize = 18.sp,
                                                modifier = Modifier.padding(top = 10.dp)
                                            )
                                            Text(
                                                text = clientbooking.phoneNumber,
                                                color = Color.Gray,
                                                fontWeight = FontWeight(500),
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(top = 10.dp)
                                            )
                                        }

                                        Text(
                                            text = clientbooking.address,
                                            color = Color.Black,
                                            fontSize = 16.sp,
                                        )
                                    }
                                }

                            }

                        }


                    }
                    Spacer(Modifier.height(16.dp)) // Space between the two columns

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
                                    fontSize = 18.sp,
                                    color = Color.Black
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row() {
                                        Icon(
                                            imageVector = Icons.Default.Message,
                                            contentDescription = "Message Icon",
                                            modifier = Modifier
                                                .size(32.dp)
                                        )
                                        Text(
                                            text = "Contact Tradesman",
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
                                Spacer(Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row() {
                                        Icon(
                                            imageVector = Icons.Default.Help,
                                            contentDescription = "Help Icon",
                                            modifier = Modifier
                                                .size(32.dp)
                                        )
                                        Text(
                                            text = "Help",
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
                }
            }
        }
        is ViewClientBookingViewModel.ViewClientBookings.Error -> {
            // Show error message
        }
        else -> Unit
    }


}
