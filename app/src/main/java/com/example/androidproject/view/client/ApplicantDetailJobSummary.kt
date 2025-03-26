package com.example.androidproject.view.client

import android.util.Log
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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.extras.SnackbarController
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.view.theme.myGradient3
import com.example.androidproject.view.tradesman.downloadFileTradesman
import com.example.androidproject.viewmodel.Resumes.ViewResumeViewModel
import com.example.androidproject.viewmodel.job_application.client.GetMyJobApplicantsViewModel

@Composable
fun ApplicantDetailJobSummary(
    resumeId: String,
    status: String,
    tradesmanId: String,
    modifier: Modifier = Modifier,
    navController: NavController,
    getMyJobApplicant: GetMyJobApplicantsViewModel,
    viewResumeViewModel: ViewResumeViewModel
) {
    val resumeID = resumeId.toIntOrNull() ?: return
    val bookingStatus = status.ifEmpty { return }
    val tradesmanID = tradesmanId.toIntOrNull() ?: return

    Log.d("ApplicantDetails1", "Resume ID: $resumeID,bookings $bookingStatus, tradesman ID: $tradesmanID")
    val context = LocalContext.current
    var downloadId by remember { mutableStateOf<Long?>(null) }

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

    val tradesmanState by viewResumeViewModel.viewResumeState.collectAsState()
    val bookingPendingState = getMyJobApplicant.jobApplicantsPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        bookingPendingState.refresh()
        viewResumeViewModel.viewResume(tradesmanID)
    }

    val selectedBooking = bookingPendingState.itemSnapshotList.items
        .firstOrNull { it.id == resumeID }

    when (val state = tradesmanState) {
        is ViewResumeViewModel.ViewResumeState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Loading...", fontSize = nameTextSize)
            }
        }
        is ViewResumeViewModel.ViewResumeState.Idle -> {

        }
        is ViewResumeViewModel.ViewResumeState.Success -> {
            val resume = state.data
            Box(Modifier.fillMaxSize()) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F5F5))
                        .padding(WindowInsets.systemBars.asPaddingValues())
                ) {
                    // Header Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(0.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier.clickable {
                                    when(bookingStatus){
                                        "Active" -> navController.navigate("main_screen?selectedItem=1&selectedTab=3&selectedSection=1") {
                                            navController.popBackStack()
                                        }
                                        "Completed" -> navController.navigate("main_screen?selectedItem=1&selectedTab=4&selectedSection=1") {
                                            navController.popBackStack()
                                        }
                                        "Cancelled" -> navController.navigate("main_screen?selectedItem=1&selectedTab=5&selectedSection=1") {
                                            navController.popBackStack()
                                        }
                                        "Declined" -> navController.navigate("main_screen?selectedItem=1&selectedTab=2&selectedSection=1") {
                                            navController.popBackStack()
                                        }
                                    }
                                    Log.d("ApplicantDetails2", "Back button clicked")
                                },
                                tint = Color(0xFF81D796)
                            )
                            Text(
                                text = "Job Details",
                                fontSize = 24.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Left,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp)
                            )
                        }
                    }

                    // Scrollable Content
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(10.dp)
                    ) {
                        // Status Card
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
                                when (bookingStatus) {

                                    "Active" -> Text(
                                        text = "The applicant is active.",
                                        fontSize = nameTextSize,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )

                                    "Completed" -> Text(
                                        text = "The applicant has successfully completed the job.",
                                        fontSize = nameTextSize,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )

                                    "Cancelled" -> Text(
                                        text = "The applicant has cancelled.",
                                        fontSize = nameTextSize,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    "Declined" -> Text(
                                        text = "The applicant has declined.",
                                        fontSize = nameTextSize,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )

                                }
                            }
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(Color.White),
                            shape = RoundedCornerShape(0.dp, 0.dp, 15.dp, 15.dp) // Keep card shape
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Task Information",
                                        fontSize = nameTextSize,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black,
                                    )
                                    Text(
                                        modifier = Modifier
                                            .clickable { navController.navigate("tradesmanapply/${selectedBooking?.jobId}") },
                                        text = "View Post",
                                        textDecoration = TextDecoration.Underline,
                                        fontSize = smallTextSize,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Blue,
                                    )

                                }
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
                                                text = "Job Details:",
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
                                            Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                                                Text(text = "Hiring: Electrician", fontSize = nameTextSize, color = Color.Black, fontWeight = FontWeight.Medium)

                                                Spacer(Modifier.height(10.dp))

                                                Text(text = "i am l8ikong foe electrivcina chubachuhcu", fontSize = nameTextSize, color = Color.Black)
                                            }
                                        }
                                    }

                                }
                                Spacer(Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Money,
                                            contentDescription = "Estimated Budget",
                                            modifier = Modifier
                                                .size(32.dp)
                                        )
                                        Text(
                                            text = "Estimated Budget:",
                                            fontSize = nameTextSize,
                                            color = Color.Black,
                                            modifier = Modifier.padding(start = 10.dp)
                                        )
                                        Text(
                                            text = "P100",
                                            fontSize = nameTextSize,
                                            color = Color.Black,
                                            modifier = Modifier.padding(start = 10.dp)
                                        )
                                    }
                                }

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
                                        Text(
                                            text = "March 1, 2025",
                                            fontSize = nameTextSize,
                                            color = Color.Black,
                                            modifier = Modifier.padding(start = 10.dp)
                                        )
                                    }
                                }

                            }

                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Reason to Hire Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(200.dp),
                            colors = CardDefaults.cardColors(Color.White),
                            shape = RoundedCornerShape(15.dp) // Keep card shape
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 18.dp, horizontal = 16.dp)
                                    .fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Tradesman’s Information",
                                        fontSize = nameTextSize,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black,
                                    )
                                    Text(
                                        modifier = Modifier
                                            .clickable{navController.navigate("applicantsdetails/${resumeID}/${bookingStatus}/${tradesmanID}")},
                                        text = "View Resume",
                                        textDecoration = TextDecoration.Underline,
                                        fontSize = smallTextSize,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Blue,
                                    )

                                }
                                HorizontalDivider(
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
                                    AsyncImage(
                                        model = selectedBooking?.tradesmanProfilePicture ?: "",
                                        contentDescription = "Client Image",
                                        modifier = Modifier
                                            .size(100.dp)
                                    )
                                    // Tradesman details
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(start = 10.dp)
                                    ) {
                                        Text(
                                            text = selectedBooking?.tradesmanFullname ?: "N/A",
                                            color = Color.Black,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = nameTextSize,
                                        )
                                        Text(
                                            text = "Phone: ${resume?.phoneNumber ?: "N/A"}",
                                            color = Color.Gray,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = smallTextSize,
                                        )


                                        Text(
                                            text = resume?.preferredWorkLocation ?: "N/A",
                                            color = Color.Gray,
                                            fontSize = smallTextSize,
                                        )
                                    }
                                }

                            }

                        }


                        Spacer(modifier = Modifier.height(10.dp))

                        // Contact Information Card
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
                                            .clickable { navController.navigate("clienthelp") }
                                            .size(32.dp)
                                    )
                                }
                            }

                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // View Job Post Button
                        when (bookingStatus) {

                            "Active", "Completed" ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            when(bookingStatus){

                                                "Active" -> navController.navigate("main_screen?selectedItem=1&selectedTab=3&selectedSection=1") {
                                                    navController.popBackStack()
                                                }
                                                "Completed" -> navController.navigate("main_screen?selectedItem=1&selectedTab=4&selectedSection=1") {
                                                    navController.popBackStack()
                                                }
                                            }
                                        }
                                        .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                                        .padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "OK",
                                        fontSize = nameTextSize,
                                        color = Color.Black
                                    )
                                }

                            "Cancelled" ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {  navController.navigate("applicantsdeclinedetails/${resumeID}/${bookingStatus}/${tradesmanID}") }
                                        .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                                        .padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Cancellation Details",
                                        fontSize = nameTextSize,
                                        color = Color.Black
                                    )
                                }

                        }

                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                SnackbarController.ObserveSnackbar()
            }
        }

        is ViewResumeViewModel.ViewResumeState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Error: ${state.message}",
                    fontSize = nameTextSize,
                    color = Color.Red
                )
            }
            Log.e("ApplicantDetails", state.message)
        }


    }
}