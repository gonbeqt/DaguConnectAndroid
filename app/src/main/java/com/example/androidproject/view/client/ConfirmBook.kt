package com.example.androidproject.view.client

import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.view.theme.myGradient3
import com.example.androidproject.viewmodel.Resumes.ViewResumeViewModel
import com.example.androidproject.viewmodel.bookings.BooktradesmanViewModel
import org.json.JSONArray
import java.util.Calendar


@Composable
fun ConfirmBook(viewResumeViewModel: ViewResumeViewModel, navController: NavController,resumeId: String,tradesmanId: String, bookingTradesmanViewModel: BooktradesmanViewModel){
    var taskDescription by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("Select A Date") }
    var selectedTaskType by remember { mutableStateOf<String?>(null) }
    val ResumeId = resumeId.toIntOrNull() ?: return
    val TradesmanId = tradesmanId.toIntOrNull()?: return
    val context = LocalContext.current
    val resumeState by viewResumeViewModel.viewResumeState.collectAsState()
    val bookingState by bookingTradesmanViewModel.bookTradesmanState.collectAsState()
    var isValid by remember { mutableStateOf(false) }
    val phoneRegex = "^09[0-9]{9}$".toRegex()
    val windowSize = rememberWindowSizeClass()
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
    LaunchedEffect(Unit) {
        viewResumeViewModel.viewResume(ResumeId)
    }

    when(val resumestate = resumeState){
        is ViewResumeViewModel.ViewResumeState.Loading -> {
            Text(text = "Loading...")
        }
        is ViewResumeViewModel.ViewResumeState.Success -> {
            val resume = resumestate.data
            val specialtiesJsonString = resume.specialty // Assuming this is the JSON string
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
                            .size(70.dp)
                            .padding(top = 5.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Arrow Back",
                                modifier = Modifier
                                    .clickable
                                        (indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    )
                                    { navController.popBackStack() }
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
                        .background(myGradient3),

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
                                model = resume.profilePic,
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
                                    text = resume.tradesmanFullName,
                                    color = Color.Black,
                                    fontWeight = FontWeight(500),
                                    fontSize = nameTextSize,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                                resume.preferredWorkLocation?.let {
                                    Text(
                                        text = it,
                                        color = Color.Black,
                                        fontSize = taskTextSize,
                                    )
                                }
                                resume.specialty?.let {
                                    Text(
                                        text = it
                                            .replace("_"," "),
                                        color = Color.Black,
                                        fontSize = taskTextSize,
                                    )
                                }
                            }

                                // Tradesman Reviews Box
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = Color(0xFFF5F5F5),
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
                                            fontSize = smallTextSize
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Address Input
                            Column(Modifier.padding(horizontal = 24.dp))
                            {
                                Text(
                                    text = "Address",
                                    color = Color.Black,
                                    fontSize = nameTextSize,
                                    fontWeight = FontWeight(500),
                                    modifier = Modifier
                                        .fillMaxWidth()

                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 6.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White)
                                        .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                                ) {
                                    TextField(
                                        value = address,
                                        onValueChange = { address = it },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.White),
                                        placeholder = { Text(text = "eg. 123 Street Name, Barangay, City") },
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

                                Spacer(modifier = Modifier.height(16.dp))

                                    Text(
                                        text = "Mobile Number",
                                        color = Color.Black,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight(500),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 6.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                                            .background(Color(0xFFF5F5F5))
                                    ) {
                                            TextField(
                                                value = phoneNumber,
                                                onValueChange = {
                                                    phoneNumber = it
                                                    isValid =
                                                        phoneNumber.isNotEmpty() && !phoneRegex.matches(
                                                            phoneNumber
                                                        )
                                                },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(Color.White),
                                                placeholder = { Text(text = "eg. 09123456789") },
                                                maxLines = 1,
                                                keyboardOptions = KeyboardOptions.Default.copy(
                                                    keyboardType = KeyboardType.Phone
                                                ),
                                                isError = isValid, // Shows error if the length is not 11
                                                colors = TextFieldDefaults.colors(
                                                    focusedContainerColor = Color.White,
                                                    unfocusedContainerColor = Color.White,
                                                    focusedIndicatorColor = Color.Transparent,
                                                    unfocusedIndicatorColor = Color.Transparent,
                                                    focusedTextColor = Color.Black,
                                                    unfocusedTextColor = Color.Black,
                                                    cursorColor = Color.Black,
                                                    errorIndicatorColor = Color.Red // Error indicator color when invalid
                                                ),

                                            )



                                    }
                                if (isValid) {
                                    Text(
                                        text = "Phone number must start with 09 and 11 numbers only",
                                        color = Color.Red,
                                        style = TextStyle(fontSize = 12.sp),
                                        modifier = Modifier.padding(
                                            top = 4.dp,
                                            start = 4.dp
                                        )
                                    )
                                }


                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Select a Date for Your Booking",
                                    color = Color.Black,
                                    fontSize = nameTextSize,
                                    fontWeight = FontWeight(500),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                                DatePickerWithRestrictions(selectedDate) { date ->
                                    selectedDate = date // ✅ Update selectedDate in ConfirmBook
                                }
                                Log.d("DatePickerWithRestrictions", "Selected Date: $selectedDate")
                                Spacer(Modifier.height(16.dp))

                                Text(
                                    text = "Optional Details",
                                    color = Color.Black,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight(500),
                                    modifier = Modifier
                                        .fillMaxWidth()

                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 6.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFF5F5F5))
                                        .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                                ) {
                                    TextField(
                                        value = taskDescription,
                                        onValueChange = { taskDescription = it },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.White),
                                        placeholder = { Text(text = "Add special requests, details, or preferred time...") },
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
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp)
                                ) {
                                    Button(
                                        onClick = {

                                        resume.specialty?.let {
                                            bookingTradesmanViewModel.BookTradesman(phoneNumber,address,
                                                it.replace(" ","_"),taskDescription,selectedDate,TradesmanId)
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonColors(
                                        Color(0xFF42C2AE), Color.White,
                                        Color(0xFF42C2AE), Color.White
                                    )
                                ) {
                                        Text(text = "Confirm", fontSize = taskTextSize, color = Color.White)
                                }
                                LaunchedEffect(bookingState) {
                                    when (val bookingstate =bookingState) {
                                        is BooktradesmanViewModel.BookTradesmanState.Success -> {
                                            Toast.makeText(context, "Booking Successful", Toast.LENGTH_SHORT).show()
                                            bookingTradesmanViewModel.resetState() // Reset state to prevent re-triggering
                                        }
                                        is BooktradesmanViewModel.BookTradesmanState.Error -> {
                                            val errorMessage = bookingstate.message
                                            Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                                            Log.e("BookTradesman", "Error: $errorMessage") // Debugging
                                            bookingTradesmanViewModel.resetState() // Reset state to prevent repeated error
                                        }
                                        else -> Unit
                                    }
                                }
                        }


                        }
                    }

                }
            }
        }
        is ViewResumeViewModel.ViewResumeState.Error -> {
            Text(text = "Error: ${resumestate.message}")
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
        modifier = Modifier.padding(top = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Button(
            onClick = { datePickerDialog.show() },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),

            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            border = BorderStroke(1.dp, Color.Gray)
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
                    text = selectedDate,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

