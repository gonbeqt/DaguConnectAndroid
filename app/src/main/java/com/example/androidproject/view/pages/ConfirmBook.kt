package com.example.androidproject.view.pages

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidproject.R
import com.example.androidproject.view.Tradesman
import com.example.androidproject.view.theme.myGradient3
import java.util.Calendar
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmBook(trade: Tradesman, navController: NavController){
    var taskDescription by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }


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
                .background(myGradient3)
                .verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp) // Rounded top corners
        ) {

            Column(
                modifier = Modifier
                    .background(myGradient3)
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
                        fontSize = 24.sp,
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
                        ,
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


                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedTextColor = Color.Black,
                                        unfocusedTextColor = Color.Black,
                                        cursorColor = Color.Black

                                    ),

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
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedTextColor = Color.Black,
                                        unfocusedTextColor = Color.Black,
                                        cursorColor = Color.Black
                                    ),
                                )
                            }
                            Spacer(Modifier.height(8.dp))

                            DatePickerWithRestrictions { date ->
                                selectedDate = date
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
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedTextColor = Color.Black,
                                        unfocusedTextColor = Color.Black,
                                        cursorColor = Color.Black


                                    ),
                                )
                            }
                            Spacer(Modifier.height(10.dp))
                            Row(
                                Modifier.fillMaxWidth()
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

@Composable
fun DatePickerWithRestrictions(onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Set minimum date (tomorrow)
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    val minDate = calendar.timeInMillis

    // Restore calendar to today (for UI display)
    calendar.add(Calendar.DAY_OF_YEAR, -1)

    var selectedDate by remember { mutableStateOf("Select a Date") }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedDate = "$year/${month + 1}/$dayOfMonth"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        datePicker.minDate = minDate // Restrict past dates
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 5.dp)) {
        Button(
            onClick = { datePickerDialog.show() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                ,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            border = BorderStroke(1.dp, Color.White)
        ) {
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically){
                Icon(imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Calendar Icon"
                , tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = selectedDate,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
             }

        }
    }
}
