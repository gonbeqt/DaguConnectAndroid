package com.example.androidproject.view.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.ViewModelSetups
import com.example.androidproject.view.Tradesman
import com.example.androidproject.view.theme.myGradient3
import com.example.androidproject.viewmodel.bookings.ViewClientBookingViewModel

@Composable
fun CancelNow(viewClientBookingViewModel: ViewClientBookingViewModel,navController: NavController,resumeId: String) {
    var Cancel by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(-1) }
    var otherReason by remember { mutableStateOf("") }

    val reasons = listOf(
        "Change of Mind",
        "Found a Different Service Provider",
        "No Longer Needed",
        "Scheduled Time Conflict",
        "Personal Reasons",
        "Others"
    )
    val resumeId = resumeId.toIntOrNull()?: return
    val viewClientBookingstate by viewClientBookingViewModel.viewClientBookingState.collectAsState()

    LaunchedEffect(Unit) {
        viewClientBookingViewModel.viewClientBooking(resumeId)
    }


    when (val viewClientBooking = viewClientBookingstate) {
        is ViewClientBookingViewModel.ViewClientBookings.Loading -> {
            //do nothing
        }
        is ViewClientBookingViewModel.ViewClientBookings.Success -> {
            val viewclientbooking = viewClientBooking.data
            val getbookdate = ViewModelSetups.formatDateTime(viewclientbooking.bookingdate)
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
                                    model = viewclientbooking.tradesmanprofile,
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
                                        text = viewclientbooking.tradesmanfullname,
                                        color = Color.Black,
                                        fontWeight = FontWeight(500),
                                        fontSize = 20.sp,
                                        modifier = Modifier.padding(top = 10.dp)
                                    )
                                    Text(
                                        text = viewclientbooking.tasktype,
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
                                                text = "P${viewclientbooking.workfee}/hr",
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
                                    Image(
                                        painter = painterResource(R.drawable.pfp),
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
                                                text = viewclientbooking.clientfullname,
                                                color = Color.Black,
                                                fontWeight = FontWeight(500),
                                                fontSize = 18.sp,
                                                modifier = Modifier.padding(top = 10.dp)
                                            )
                                            Text(
                                                text =viewclientbooking.phonenumber,
                                                color = Color.Gray,
                                                fontWeight = FontWeight(500),
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(top = 10.dp)
                                            )
                                        }

                                        Text(
                                            text = viewclientbooking.address,
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
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(80.dp)
                        .background(Color.White),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Box(
                        modifier = Modifier
                            .clickable { Cancel = true }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color(0xFFB5B5B5), shape = RoundedCornerShape(12.dp))
                            .padding(8.dp)
                            .size(300.dp, 30.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "Cancel Appointment",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight(500)
                        )
                    }
                }


            }
        }
        is ViewClientBookingViewModel.ViewClientBookings.Error -> {

        }
        else -> Unit
    }

    if (Cancel) {
        Dialog(onDismissRequest = { Cancel = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                     ,
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
                                            selectedIndex = if (selectedIndex == index) -1 else index
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
                                onClick = { Cancel = false },
                                modifier = Modifier.size(110.dp, 45.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF42C2AE
                                    )
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

