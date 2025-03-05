package com.example.androidproject.view.client

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.androidproject.viewmodel.bookings.ViewClientBookingViewModel
import com.example.androidproject.viewmodel.ratings.RateTradesmanViewModel

@Composable
fun RateAndReviews(rateTradesmanViewModel: RateTradesmanViewModel,viewClientBookingViewModel: ViewClientBookingViewModel, navController: NavController, resumeId: String,tradesmanId :String) {
    val reviewText = remember { mutableStateOf("") }
    val rating = remember { mutableStateOf(0) }
    val ResumeId = resumeId.toIntOrNull() ?: return
    val tradesman_Id = tradesmanId.toIntOrNull()?: return
    val ratetradesmanState by rateTradesmanViewModel.rateTradesmanState.collectAsState()
    val context = LocalContext.current

    val viewBookingState by viewClientBookingViewModel.viewClientBookingState.collectAsState()

    LaunchedEffect(Unit) {
        viewClientBookingViewModel.viewClientBooking(ResumeId)
    }

    LaunchedEffect(ratetradesmanState) {
        when(val ratetradesman = ratetradesmanState){
            is RateTradesmanViewModel.RateTradesman.Loading -> {
                // do nothing
            }
            is RateTradesmanViewModel.RateTradesman.Success -> {
                val successMessage = ratetradesman.data?.message
                Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                // Pass result to MainScreen to switch to Bookings tab with Cancelled selected
                navController.previousBackStackEntry?.savedStateHandle?.set("selectedTab", 4)
                navController.popBackStack("main_screen", inclusive = false)
                rateTradesmanViewModel.resetState()
            }
            is RateTradesmanViewModel.RateTradesman.Error -> {
               val errormessage = ratetradesman.message
                Toast.makeText(context, errormessage, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    when(viewBookingState){
        is ViewClientBookingViewModel.ViewClientBookings.Loading -> {
            Text(text = "Loading...")
        }
        is ViewClientBookingViewModel.ViewClientBookings.Success -> {
            val booking = (viewBookingState as ViewClientBookingViewModel.ViewClientBookings.Success).data

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFECECEC))

            ) {
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
                            .size(70.dp)
                            .padding(top = 5.dp)
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
                                text = "Rate And Reviews",
                                fontSize = 24.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Left,
                                modifier = Modifier
                                    .padding(top = 15.dp)
                                    .weight(1f)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors((Color(0xFFECECEC)))

                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = booking.tradesmanProfile,
                            contentDescription = "Tradesman Image",
                            modifier = Modifier
                                .size(150.dp)
                                .padding(8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = booking.tradesmanFullName,
                            fontSize = 24.sp,
                            color = Color.Black
                        )

                        Text(
                            text = booking.taskType,
                            fontSize = 20.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // 5-star rating
                        Row {
                            for (i in 1..5) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Star",
                                    modifier = Modifier
                                        .size(45.dp)
                                        .clickable { rating.value = i },
                                    tint = if (i <= rating.value) Color(0xFFECAB1E) else Color.Gray
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Review TextField
                        TextField(
                            value = reviewText.value,
                            onValueChange = { reviewText.value = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    width = 1.dp,
                                    color = Color.Gray,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            placeholder = { Text("Write your review here...") },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Submit Button
                        Button(
                            onClick = {
                                rateTradesmanViewModel.rateTradesman(reviewText.value, rating.value, tradesman_Id)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF122826),
                                contentColor = Color.White
                            )

                        ) {
                            Text("Submit", fontSize = 20.sp, fontWeight = FontWeight(500))
                        }
                    }
                }
            }
        }
        is ViewClientBookingViewModel.ViewClientBookings.Error -> {
            Text(text = "Error: ${(viewBookingState as ViewClientBookingViewModel.ViewClientBookings.Error).message}")
        }
        else -> Unit
    }



}