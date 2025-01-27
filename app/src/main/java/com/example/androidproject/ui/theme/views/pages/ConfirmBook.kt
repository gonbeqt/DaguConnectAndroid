package com.example.androidproject.ui.theme.views.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.androidproject.R
import com.example.androidproject.ui.theme.views.Tradesman

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmBook(trade:Tradesman,navController: NavController){
    var taskDescription by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    val tradesmen = listOf(
        Tradesman(R.drawable.pfp, "Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp, "Alex", "Electrical", "P600/hr", 4.8, R.drawable.bookmark)
    )
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Main Content Area (Scrollable)

        // Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF81D796))
                .verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp) // Rounded top corners
        ) {

            Column(
                modifier = Modifier
                    .background(Color(0xFF81D796))
                    .fillMaxWidth()
                    .size(100.dp)
                    .padding(top = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Arrow Back",
                        Modifier.clickable { navController.popBackStack() }
                            .padding(16.dp),
                        tint = Color.White
                    )


                    Text(
                        text = "Bookings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 15.dp, end = 50.dp)
                            .weight(1f) // Ensures the text takes available space and is centered
                    )
                }

            }
        }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 100.dp)
                        .background(Color(0xFF81D796)),
                    shape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFF9F9F9))
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
                                painter = painterResource(trade.imageResId),
                                contentDescription = "Tradesman Image",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(start = 10.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 10.dp)
                            ) {
                                Text(
                                    text = trade.username,
                                    color = Color.Black,
                                    fontWeight = FontWeight(500),
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                                Text(
                                    text = trade.category,
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                )
                                Row(
                                    modifier = Modifier.padding(top = 10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Rate Box (Bookmark Option)
                                    Box(
                                        modifier = Modifier
                                            .clickable { }
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Bookmark,
                                                contentDescription = "Star Icon",
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.size(4.dp))
                                            Text(
                                                text = "Add to bookmark",
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }

                            // Tradesman Reviews Box
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Color(0xFFFFF2DD),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
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
                                        text = trade.reviews.toString(),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Address Input
                        Column(Modifier.padding(horizontal = 10.dp))
                        {
                            Text(
                                text = "Address",
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight(500),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                            ) {

                                TextField(
                                    value = address,
                                    onValueChange = { address = it },
                                    modifier = Modifier.fillMaxWidth()
                                        .background(Color.White),
                                    placeholder = { Text(text = "Enter your Address") },
                                    maxLines = 3,


                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = Color.White,
                                        unfocusedIndicatorColor = Color.White,

                                        )

                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Mobile Number",
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight(500),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF5F5F5))
                            ) {
                                TextField(
                                    value = phoneNumber,
                                    onValueChange = { phoneNumber = it },
                                    modifier = Modifier.fillMaxWidth()
                                        .background(Color.White),

                                    placeholder = { Text(text = " +63 | Enter Mobile Number") },
                                    maxLines = 3,
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Week Availability",
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight(500),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )
                            Spacer(Modifier.height(4.dp))



                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(start = 5.dp, end = 5.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                listOf(
                                    "Monday",
                                    "Tuesday",
                                    "Wednesday",
                                    "Thursday",
                                    "Friday",
                                    "Saturday",
                                    "Sunday"
                                ).forEach { day ->
                                    Box(
                                        modifier = Modifier
                                            .size(100.dp, 30.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFFFFF2DD))
                                            .clickable { },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = day,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.align(Alignment.Center),
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(4.dp))

                            Text(
                                text = "Optional Details",
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight(500),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF5F5F5))
                            ) {
                                TextField(
                                    value = taskDescription,
                                    onValueChange = { taskDescription = it },
                                    modifier = Modifier.fillMaxWidth()
                                        .background(Color.White),

                                    placeholder = { Text(text = " Add any special requests or details for the trades person...") },
                                    maxLines = 3,
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )
                            }
                            Spacer(Modifier.height(10.dp))
                            Row(
                                Modifier.fillMaxWidth().background(Color.White)
                                    .padding(vertical = 10.dp)
                            ) {
                                Button(
                                    onClick = {},
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonColors(
                                        Color(0xFFECAB1E), Color.White,
                                        Color(0xFFECAB1E), Color.White
                                    )
                                ) {
                                    Text(text = "Confirm")
                                }
                            }
                        }


                }
          }

     }
}
