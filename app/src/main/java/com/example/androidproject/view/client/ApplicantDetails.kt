package com.example.androidproject.view.client

import android.net.Uri
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
fun ApplicantDetails(
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
                                        "Pending" -> navController.navigate("main_screen?selectedItem=1&selectedTab=1&selectedSection=1") {
                                            navController.popBackStack()
                                        }
                                        "Active" -> navController.navigate("main_screen?selectedItem=1&selectedTab=3&selectedSection=1") {
                                            navController.popBackStack()
                                        }
                                        "Completed" -> navController.navigate("main_screen?selectedItem=1&selectedTab=4&selectedSection=1") {
                                            navController.popBackStack()
                                        }
                                        "Declined" -> navController.navigate("main_screen?selectedItem=1&selectedTab=2&selectedSection=1") {
                                            navController.popBackStack()
                                        }
                                        "Cancelled" -> navController.navigate("main_screen?selectedItem=1&selectedTab=5&selectedSection=1") {
                                            navController.popBackStack()
                                        }
                                    }
                                                              },
                                tint = Color(0xFF81D796)
                            )
                            Text(
                                text = "Applicant Details",
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
                                    "Pending" -> Text(
                                        text = "Your approval is pending: Approve or Decline",
                                        fontSize = nameTextSize,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )

                                    "Active", -> Text( // Combined identical cases
                                        text = "The applicant is active.",
                                        fontSize = nameTextSize,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )

                                    "Completed", -> Text( // Combined identical cases
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

                        // Tradesman Info Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(0.dp, 0.dp, 15.dp, 15.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Tradesman's Basic Information",
                                    fontSize = nameTextSize,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                                Divider(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    thickness = 0.5.dp,
                                    color = Color.Gray
                                )
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = selectedBooking?.tradesmanProfilePicture ?: "",
                                        contentDescription = "Tradesman Image",
                                        modifier = Modifier.size(100.dp)
                                    )
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row {
                                            Text(
                                                "Full Name:",
                                                color = Color.Gray,
                                                fontSize = taskTextSize
                                            )
                                            Text(
                                                text = selectedBooking?.tradesmanFullname ?: "N/A",
                                                color = Color.Black,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = taskTextSize,
                                                modifier = Modifier.padding(start = 4.dp)
                                            )
                                        }
                                        Row {
                                            Text(
                                                "Birthday:",
                                                color = Color.Gray,
                                                fontSize = taskTextSize
                                            )
                                            Text(
                                                text = resume.birthdate ?: "N/A",
                                                color = Color.Black,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = taskTextSize,
                                                modifier = Modifier.padding(start = 4.dp)
                                            )
                                        }
                                        Row {
                                            Text(
                                                "Age:",
                                                color = Color.Gray,
                                                fontSize = taskTextSize
                                            )
                                            Text(
                                                text = resume.birthdate?.let { birthdate ->
                                                    try {
                                                        // Split "yyyy-MM-dd" into parts
                                                        val parts = birthdate.trim().split("-")
                                                        if (parts.size != 3) throw IllegalArgumentException(
                                                            "Invalid format"
                                                        )

                                                        val birthYear = parts[0].toInt()
                                                        val birthMonth = parts[1].toInt()
                                                        val birthDay = parts[2].toInt()

                                                        val calendar =
                                                            java.util.Calendar.getInstance()
                                                        val todayYear =
                                                            calendar.get(java.util.Calendar.YEAR)
                                                        val todayMonth =
                                                            calendar.get(java.util.Calendar.MONTH) + 1 // 0-based, so add 1
                                                        val todayDay =
                                                            calendar.get(java.util.Calendar.DAY_OF_MONTH)

                                                        // Calculate age
                                                        var age = todayYear - birthYear
                                                        if (todayMonth < birthMonth || (todayMonth == birthMonth && todayDay < birthDay)) {
                                                            age-- // Subtract 1 if birthday hasn’t occurred this year
                                                        }

                                                        Log.d(
                                                            "ApplicantDetails",
                                                            "Birth: $birthdate, Today: $todayYear-$todayMonth-$todayDay, Age: $age"
                                                        )
                                                        age.toString()
                                                    } catch (e: Exception) {
                                                        Log.e(
                                                            "ApplicantDetails",
                                                            "Error parsing '$birthdate': ${e.message}"
                                                        )
                                                        "N/A"
                                                    }
                                                } ?: "N/A",
                                                color = Color.Black,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = taskTextSize,
                                                modifier = Modifier.padding(start = 4.dp)
                                            )
                                        }
                                        Row {
                                            Text(
                                                "Address:",
                                                color = Color.Gray,
                                                fontSize = taskTextSize
                                            )
                                            Text(
                                                text = resume?.preferredWorkLocation ?: "N/A",
                                                color = Color.Black,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = taskTextSize,
                                                modifier = Modifier.padding(start = 4.dp)
                                            )
                                        }
                                        Row {
                                            Text(
                                                "Credential:",
                                                color = Color.Gray,
                                                fontSize = taskTextSize
                                            )
                                            Text(
                                                text = "View",
                                                textDecoration = TextDecoration.Underline,
                                                color = Color.Blue,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = taskTextSize,
                                                modifier = Modifier
                                                    .padding(start = 4.dp)
                                                    .clickable {
                                                        val fileUrl = resume.documents
                                                        val fileName =
                                                            "trade_credential_${resume.tradesmanFullName}.pdf"
                                                        if (fileUrl != null) {
                                                            try {
                                                                downloadId = downloadFileTradesman(
                                                                    context,
                                                                    fileUrl,
                                                                    fileName
                                                                )
                                                                SnackbarController.show("Download success")
                                                            } catch (e: Exception) {
                                                                SnackbarController.show("Download failed")
                                                            }
                                                        } else {
                                                            SnackbarController.show("No file available")

                                                        }
                                                    }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Reason to Hire Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Reason to Hire this Tradesman",
                                    fontSize = nameTextSize,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Medium
                                )
                                Divider(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    thickness = 0.5.dp,
                                    color = Color.Gray
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFF5F5F5))
                                        .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = selectedBooking?.qualificationSummary
                                            ?: "No reason provided",
                                        fontSize = taskTextSize,
                                        color = Color.Black
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Contact Information Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Contact Information",
                                    fontSize = nameTextSize,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Medium
                                )
                                Divider(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    thickness = 0.5.dp,
                                    color = Color.Gray
                                )
                                Row(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = "Phone Number",
                                        tint = Color.Blue,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        text = "Phone: ${resume?.phoneNumber ?: "N/A"}",
                                        fontSize = taskTextSize,
                                        color = Color.Black,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }
                                Row(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = "Email",
                                        tint = Color.Blue,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        text = "Email: ${resume?.email ?: "N/A"}",
                                        fontSize = taskTextSize,
                                        color = Color.Black,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Other Information Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Other Information",
                                    fontSize = nameTextSize,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Medium
                                )
                                Divider(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    thickness = 0.5.dp,
                                    color = Color.Gray
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Money,
                                            contentDescription = "Estimated Rate",
                                            tint = Color.Black,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Column(modifier = Modifier.padding(start = 10.dp)) {
                                            Text(
                                                text = "Estimated Rate",
                                                fontSize = taskTextSize,
                                                color = Color.Black
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .padding(top = 4.dp)
                                                    .background(
                                                        Color(0xFFF5F5F5),
                                                        RoundedCornerShape(12.dp)
                                                    )
                                                    .padding(8.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = resume.workFee.toString() ?: "N/A",
                                                    fontSize = smallTextSize
                                                )
                                            }
                                        }
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.RateReview,
                                            contentDescription = "Ratings",
                                            tint = Color.Black,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Column(modifier = Modifier.padding(start = 10.dp)) {
                                            Text(
                                                text = "Ratings",
                                                fontSize = taskTextSize,
                                                color = Color.Black
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .padding(top = 4.dp)
                                                    .background(
                                                        Color(0xFFF5F5F5),
                                                        RoundedCornerShape(12.dp)
                                                    )
                                                    .padding(8.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = Icons.Default.Star,
                                                        contentDescription = "Star",
                                                        tint = Color(0xFFFFA500),
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Text(
                                                        text = resume?.ratings?.toDouble()
                                                            .toString() ?: "0",
                                                        fontSize = smallTextSize,
                                                        modifier = Modifier.padding(start = 4.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Support Center Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(15.dp)
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
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    thickness = 0.5.dp,
                                    color = Color.Gray
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { /* Add messaging action */ }
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Message,
                                            contentDescription = "Contact Tradesman",
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Text(
                                            text = "Contact Tradesman",
                                            fontSize = taskTextSize,
                                            modifier = Modifier.padding(start = 10.dp)
                                                .clickable{
                                                    val encodedProfilePicture = Uri.encode(
                                                        resume.profilePic
                                                    )
                                                    navController.navigate("messaging/0/${resume.userid}/${resume.tradesmanFullName}/${encodedProfilePicture}")
                                                }
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowRight,
                                        contentDescription = "Arrow Right",
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { /* Add help action */ }
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Help,
                                            contentDescription = "Help",
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Text(
                                            text = "Help",
                                            fontSize = taskTextSize,
                                            modifier = Modifier.padding(start = 10.dp)
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowRight,
                                        contentDescription = "Arrow Right",
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // View Job Post Button
                        when (bookingStatus) {
                            "Pending" ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { navController.navigate("booknow/${tradesmanId}") }
                                        .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                                        .padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "View Job Post",
                                        fontSize = nameTextSize,
                                        color = Color.Black
                                    )
                                }

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
                                        text = "Ok",
                                        fontSize = nameTextSize,
                                        color = Color.Black
                                    )
                                }

                            "Declined", "Cancelled" ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {  navController.navigate("applicantsdeclinedetails/${resumeID}/${bookingStatus}/${tradesmanID}") }
                                        .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                                        .padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${bookingStatus} Details",
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