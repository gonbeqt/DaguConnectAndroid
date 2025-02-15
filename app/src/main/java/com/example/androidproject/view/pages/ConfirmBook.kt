package com.example.androidproject.view.pages

import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.view.Tradesman
import com.example.androidproject.view.theme.myGradient3
import com.example.androidproject.viewmodel.Resumes.ViewResumeViewModel
import com.example.androidproject.viewmodel.bookings.BooktradesmanViewModel
import org.json.JSONArray
import java.util.Calendar
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmBook(viewResumeViewModel: ViewResumeViewModel, navController: NavController,resumeId: String,tradesmanId: String, bookingTradesmanViewModel: BooktradesmanViewModel){
    var taskDescription by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTaskType by remember { mutableStateOf<String?>(null) }
    val ResumeId = resumeId.toIntOrNull() ?: return
    val TradesmanId = tradesmanId.toIntOrNull()?: return
    val context = LocalContext.current
    val resumeState by viewResumeViewModel.viewResumeState.collectAsState()
    val bookingState by bookingTradesmanViewModel.bookTradesmanState.collectAsState()

    LaunchedEffect(Unit) {
        viewResumeViewModel.viewResume(ResumeId)
    }




    val tradesmen = listOf(
        Tradesman(R.drawable.pfp, "Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp, "Alex", "Electrical", "P600/hr", 4.8, R.drawable.bookmark)
    )
    when(resumeState){
        is ViewResumeViewModel.ViewResumeState.Loading -> {
            Text(text = "Loading...")
        }
        is ViewResumeViewModel.ViewResumeState.Success -> {
            val resume = (resumeState as ViewResumeViewModel.ViewResumeState.Success).data
            val specialtiesJsonString = resume.specialties // Assuming this is the JSON string
            val values = try {
                JSONArray(specialtiesJsonString).let { jsonArray ->
                    List(jsonArray.length()) { index -> jsonArray.getString(index) }
                }
            } catch (e: Exception) {
                emptyList() // Fallback in case of parsing errors
            }

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
                        .verticalScroll(rememberScrollState())
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
                            AsyncImage(
                                model = resume.profilepic,
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
                                    text = resume.tradesmanfullname,
                                    color = Color.Black,
                                    fontWeight = FontWeight(500),
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                                Text(
                                    text = resume.preferedworklocation
                                        .replace("[","")
                                        .replace("]","")
                                        .replace("\"",""),
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                )
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
                                        text = "4",
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
                            Spacer(modifier = Modifier.height(4.dp))

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
                            Text(
                                text = "Specialties",
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight(500),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    values.forEach { value ->
                                        Box(
                                            modifier = Modifier
                                                .width(100.dp)
                                                .height(40.dp)
                                                .background(
                                                    if (selectedTaskType == value) Color(0xFF122826) else Color.LightGray, // Change color if selected
                                                    RoundedCornerShape(50.dp)
                                                )
                                                .clickable { selectedTaskType = value }, // Update selected value
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = value,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = if (selectedTaskType == value) Color.White else Color.Black // Change text color when selected
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "Select a Date for Your Booking",
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight(500),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )
                            DatePickerWithRestrictions(selectedDate) { date ->
                                selectedDate = date // ✅ Update selectedDate in ConfirmBook
                            }
                            Log.d("DatePickerWithRestrictions", "Selected Date: $selectedDate")
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
                                    onClick = {
                                        selectedTaskType?.let {
                                            bookingTradesmanViewModel.BookTradesman(phoneNumber,address,
                                                it,taskDescription,selectedDate,TradesmanId)
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonColors(
                                        Color(0xFFECAB1E), Color.White,
                                        Color(0xFFECAB1E), Color.White
                                    )
                                ) {
                                    Text(text = "Confirm")
                                }
                                when(bookingState){
                                    is BooktradesmanViewModel.BookTradesmanState.Loading ->{
                                        Text(text = "Loading...")
                                    }
                                    is BooktradesmanViewModel.BookTradesmanState.Success ->{
                                        Toast.makeText(context,"Booking Successful", Toast.LENGTH_SHORT).show()
                                    }
                                    is BooktradesmanViewModel.BookTradesmanState.Error ->{
                                        Text(text = "Error: ${(bookingState as BooktradesmanViewModel.BookTradesmanState.Error).message}")
                                        Toast.makeText(context,"Error: ${(bookingState as BooktradesmanViewModel.BookTradesmanState.Error).message}", Toast.LENGTH_SHORT).show()
                                    }
                                    else -> Unit
                                }
                            }
                        }


                    }
                }

            }
        }
        is ViewResumeViewModel.ViewResumeState.Error -> {
            Text(text = "Error: ${(resumeState as ViewResumeViewModel.ViewResumeState.Error).message}")
        }
        else -> Unit
    }
}
@Composable
fun DatePickerWithRestrictions(selectedDate: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Set minimum date (tomorrow)
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    val minDate = calendar.timeInMillis

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val formattedDate = "$year-${month + 1}-$dayOfMonth"
            onDateSelected(formattedDate) // ✅ Send selected date to ConfirmBook
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        datePicker.minDate = minDate // Restrict past dates
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 5.dp)
    ) {
        Button(
            onClick = { datePickerDialog.show() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            border = BorderStroke(1.dp, Color.White)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Calendar Icon",
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = selectedDate, // ✅ Show updated selected date
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

