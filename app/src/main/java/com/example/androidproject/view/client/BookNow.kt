package com.example.androidproject.view.client

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.view.theme.myGradient3
import com.example.androidproject.view.tradesman.downloadFileTradesman
import com.example.androidproject.viewmodel.Resumes.ViewResumeViewModel
import com.example.androidproject.viewmodel.ratings.ViewRatingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookNow(viewResumeViewModel: ViewResumeViewModel, navController: NavController, resumeId : String,viewRatingsViewModel: ViewRatingsViewModel) {
    val viewResumeState by viewResumeViewModel.viewResumeState.collectAsState()
    val ResumeId = resumeId.toIntOrNull() ?: return
    val context = LocalContext.current
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

    var downloadId by remember { mutableStateOf<Long?>(null) }




    when(val state = viewResumeState){
        is ViewResumeViewModel.ViewResumeState.Loading -> {
            Text(text = "Loading...")
        }
        is ViewResumeViewModel.ViewResumeState.Success -> {
            val resume = state.data
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
                                    Modifier.clickable ( indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) { navController.navigate("main_screen") }
                                        .padding(16.dp)
                                    ,
                                    tint = Color.White
                                )


                            Text(
                                text = "Expert Details",
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
                                        .padding(start = 15.dp)
                                ) {
                                    Text(
                                        text = resume.tradesmanFullName,
                                        color = Color.Black,
                                        fontWeight = FontWeight(500),
                                        fontSize = nameTextSize,
                                        modifier = Modifier.padding(top = 10.dp)
                                    )

                                    resume.specialty?.let {
                                        BoxRow(specialties = it
                                            .replace("_"," ")

                                        )
                                    }

                                    resume.preferredWorkLocation?.let {
                                        Text(
                                            text = it,
                                            color = Color.Black,
                                            fontSize = taskTextSize
                                        )
                                    }


                                }

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
                                            when {
                                                resume.ratings == 0f -> "0"
                                                else -> String.format("%.1f", resume.ratings)
                                            },
                                            fontSize = smallTextSize
                                        )
                                    }
                                }
                            }

                            // Additional Sections
                            Spacer(modifier = Modifier.height(4.dp))
                            Column(Modifier.padding(horizontal = 10.dp)) {
                                Card(Modifier.fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    ,
                                    colors =CardDefaults.cardColors(Color.White)) {
                                    Column(Modifier.fillMaxSize().shadow(0.5.dp)
                                        .padding(horizontal = 8.dp).padding(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text(
                                            text = "About Me",
                                            color = Color.Black,
                                            fontSize = nameTextSize,
                                            fontWeight = FontWeight(500),

                                        )
                                        Spacer(modifier = Modifier.height(4.dp))

                                        var showFullText by remember { mutableStateOf(false) }
                                        val aboutme = resume.aboutMe ?: "No comments available"
                                        val maxPreviewLength = 100 // Adjust this value as needed

                                        if (aboutme.length > maxPreviewLength) {
                                            Column {
                                                Text(
                                                    text = if (showFullText) aboutme else "${aboutme.take(maxPreviewLength)}...",
                                                    modifier = Modifier.padding(top = 4.dp),
                                                    fontSize = taskTextSize,
                                                    color = if (aboutme.isEmpty()) Color.Gray else Color.Black
                                                )
                                                TextButton(
                                                    onClick = { showFullText = !showFullText },
                                                    modifier = Modifier
                                                        .align(Alignment.End)
                                                ) {
                                                    Text(
                                                        text = if (showFullText) "See Less" else "See More",
                                                        color = Color.Blue,
                                                        fontSize = smallTextSize
                                                    )
                                                }
                                            }
                                        } else {
                                            Text(
                                                text = aboutme,
                                                modifier = Modifier.padding(top = 4.dp),
                                                fontSize = taskTextSize,
                                                color = if (aboutme.isEmpty()) Color.Gray else Color.Black
                                            )
                                        }
                                    }

                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Card(Modifier.fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    ,
                                    colors =CardDefaults.cardColors(Color.White)) {
                                    Column(
                                        Modifier.fillMaxSize().shadow(0.5.dp)
                                            .padding(horizontal = 8.dp).padding(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = "Tradesman Information",
                                            color = Color.Black,
                                            fontSize = nameTextSize,
                                            fontWeight = FontWeight(500)
                                        )

                                        Row(
                                            Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Preferred Location",
                                                color = Color.Gray,
                                                fontSize = nameTextSize,
                                                fontWeight = FontWeight(500)
                                            )

                                            resume.preferredWorkLocation?.let {
                                                Text(
                                                    text = it,
                                                    color = Color.Black,
                                                    fontSize = taskTextSize,
                                                    fontWeight = FontWeight(500)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))

                                        Row(
                                            Modifier.fillMaxWidth()
                                                ,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Est. Rate",
                                                color = Color.Gray,
                                                fontSize = nameTextSize,
                                                fontWeight = FontWeight(500)
                                            )

                                            Text(
                                                text = "₱${resume.workFee}",
                                                color = Color.Black,
                                                fontWeight = FontWeight(500),
                                                fontSize = taskTextSize
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))

                                        Row(
                                            Modifier.fillMaxWidth()
                                                ,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Trade Credential",
                                                color = Color.Gray,
                                                fontSize = nameTextSize,
                                                fontWeight = FontWeight(500)
                                            )

                                            Text(
                                                text = "View File",
                                                color = Color.Blue,
                                                fontSize = 16.sp,
                                                textDecoration = TextDecoration.Underline,
                                                fontWeight = FontWeight(500),
                                                modifier = Modifier
                                                    .clickable {

                                                        val fileUrl =
                                                            resume.documents // Assuming this is the URL to the file
                                                        val fileName =
                                                            "trade_credential_${resume.tradesmanFullName}.pdf" // Customize the file name as needed

                                                        if (fileUrl != null) {
                                                            try {
                                                                downloadId = downloadFileTradesman(
                                                                    context,
                                                                    fileUrl,
                                                                    fileName
                                                                )
                                                                Toast.makeText(
                                                                    context,
                                                                    "Download success",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            } catch (e: Exception) {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Download failed: ${e.message}",
                                                                    Toast.LENGTH_LONG
                                                                ).show()
                                                            }
                                                        } else {
                                                            Toast.makeText(
                                                                context,
                                                                "No file available",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Card(Modifier.fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    ,
                                    colors =CardDefaults.cardColors(Color.White)) {
                                    Column(
                                        Modifier.fillMaxSize().shadow(0.5.dp)
                                            .padding(horizontal = 8.dp).padding(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = "Contact Information",
                                            color = Color.Black,
                                            fontSize = nameTextSize,
                                            fontWeight = FontWeight(500)
                                        )


                                        Row(
                                            Modifier.fillMaxWidth()
                                               ,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Phone Number",
                                                color = Color.Gray,
                                                fontSize = nameTextSize,
                                                fontWeight = FontWeight(500)
                                            )

                                            Text(
                                                text = resume.phoneNumber ?: "N/A",
                                                color = Color.Black,
                                                fontSize = taskTextSize,
                                                fontWeight = FontWeight(500)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))

                                        Row(
                                            Modifier.fillMaxWidth()
                                               ,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Email",
                                                color = Color.Gray,
                                                fontSize = nameTextSize,
                                                fontWeight = FontWeight(500)
                                            )

                                            Text(
                                                text = resume.email,
                                                color = Color.Black,
                                                fontSize = taskTextSize,
                                                fontWeight = FontWeight(
                                                    500
                                                )
                                            )
                                        }
                                    }
                                }


                                Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Ratings",
                                        color = Color.Black,
                                        fontSize = nameTextSize,
                                        fontWeight = FontWeight(500),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)
                                    )

                                    Text(
                                        text = "Feedback from satisfied clients",
                                        color = Color.Black,
                                        fontSize = taskTextSize,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)
                                    )
                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .background(Color(0xFFF9F9F9)),
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        FeedbackItem(viewRatingsViewModel,resume.userid)
                                    }
                                }
                            }
                        }
                        // Tradesman Details Section


                }

                    // Fixed Buttons at the Bottom
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(Color.Transparent)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Box(
                            modifier = Modifier
                                .clickable { navController.navigate("message_screen") }
                                .background(
                                    color = Color(0xFF42C2AE),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(1.dp, Color(0xFF42C2AE), shape = RoundedCornerShape(12.dp) )
                                .width(150.dp)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Message,
                                    contentDescription = "Message Icon",
                                    tint = Color.White
                                )
                                Text(text = "Chat Me", color = Color.White, fontSize = nameTextSize)
                            }
                        }

                    Box(
                        modifier = Modifier
                            .clickable {navController.navigate("confirmbook/${resume.id}/${resume.userid}") }
                            .background(
                                color = Color(0xFF42C2AE),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .width(150.dp)
                            .padding(8.dp),contentAlignment = Alignment.Center


                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,


                            ) {
                            Icon(
                                imageVector = Icons.Default.AddShoppingCart,
                                contentDescription = "Add to Cart Icon",
                                tint = Color.White
                            )
                            Text(
                                text = "Book Now",
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                fontSize = nameTextSize
                            )
                        }
                    }
                }
            }


        }
        is ViewResumeViewModel.ViewResumeState.Error -> {
            Text("Error: ${state.message}")
            Log.e("Error",state.message)
        }
        else -> Unit
    }


}

@Composable
fun BoxRow(specialties: String) {
    val windowSize = rememberWindowSizeClass()

    val taskTextSize = when (windowSize.width) {
        WindowType.SMALL -> 14.sp
        WindowType.MEDIUM -> 16.sp
        WindowType.LARGE -> 18.sp
    }

    Text(
        text = specialties,
        color = Color.Black,
        fontSize = taskTextSize,
        fontWeight = FontWeight.Medium,
        )
}

@Composable
fun FeedbackItem(viewRatingsViewModel: ViewRatingsViewModel, tradesmanId: Int) {
    val viewRatingState by viewRatingsViewModel.viewRatingsState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(tradesmanId) {  // Use tradesmanId as key to re-fetch when it changes
        viewRatingsViewModel.viewRatings(tradesmanId)
    }


    when (val viewratings = viewRatingState) {
        is ViewRatingsViewModel.ViewRatingsState.Loading -> {
            // Loading state (Optional: Show a progress indicator)
        }
        is ViewRatingsViewModel.ViewRatingsState.Success -> {
            val ratingsList = viewratings.data

            if (ratingsList.isEmpty()) {
                // Ensure the text is properly centered
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        ,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No ratings",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            } else {
                Column {
                    ratingsList.forEach { ratings ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { showDialog = true },
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Profile Picture
                                AsyncImage(
                                    model = ratings.client_profile,
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, Color.Gray, CircleShape)
                                )

                                Text(
                                    text = ratings.client_name,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "${ratings.ratings} stars",
                                    color = Color.Gray
                                )
                            }
                        }
                        if (showDialog) {
                            AlertDialog(
                                containerColor = Color.White,
                                onDismissRequest = { showDialog = false },
                                title = {

                                        Text(
                                            text = "Feedback Details",
                                            modifier = Modifier.padding(start = 8.dp)
                                        )

                                },
                                text = {
                                    Column(
                                        modifier = Modifier.padding(16.dp).verticalScroll(
                                            rememberScrollState()
                                        )
                                    ) {
                                        // Profile Section
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        ) {
                                            AsyncImage(
                                                model = ratings.client_profile,
                                                contentDescription = "Profile Picture",
                                                modifier = Modifier
                                                    .size(75.dp)
                                                    .clip(CircleShape)
                                                    .border(1.dp, Color.Gray, CircleShape)
                                            )
                                            Column {
                                                Text(
                                                    text = ratings?.client_name ?: "Unknown",
                                                    modifier = Modifier.padding(start = 8.dp),
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 20.sp
                                                )
                                                Row(
                                                    modifier = Modifier.padding(bottom = 8.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Star,
                                                        contentDescription = "Rating",
                                                        tint = Color.Yellow
                                                    )
                                                    Text(
                                                        text = "${ratings?.ratings ?: 0} stars",
                                                        modifier = Modifier.padding(start = 4.dp)
                                                    )
                                                }
                                            }

                                        }

                                        // Ratings Section


                                        // Comments Section
                                        Text(
                                            text = "Comments:",
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(top = 8.dp),
                                            fontSize = 20.sp
                                        )

                                        // Add state for showing full text
                                        var showFullText by remember { mutableStateOf(false) }
                                        val message = ratings?.message ?: "No comments available"
                                        val maxPreviewLength = 100 // Adjust this value as needed

                                        if (message.length > maxPreviewLength) {
                                            Column {
                                                Text(
                                                    text = if (showFullText) message else "${message.take(maxPreviewLength)}......",
                                                    modifier = Modifier.padding(top = 4.dp),
                                                    fontSize = 16.sp,
                                                    color = if (message.isEmpty()) Color.Gray else Color.Black
                                                )
                                                TextButton(
                                                    onClick = { showFullText = !showFullText },
                                                    modifier = Modifier
                                                        .align(Alignment.End)
                                                        .padding(top = 4.dp)
                                                ) {
                                                    Text(
                                                        text = if (showFullText) "See Less" else "See More",
                                                        color = Color.Blue,
                                                        fontSize = 14.sp
                                                    )
                                                }
                                            }
                                        } else {
                                            Text(
                                                text = message,
                                                modifier = Modifier.padding(top = 4.dp),
                                                fontSize = 16.sp,
                                                color = if (message.isEmpty()) Color.Gray else Color.Black
                                            )
                                        }
                                    }
                                },
                                confirmButton = {
                                    TextButton(onClick = { showDialog = false }) {
                                        Text("Close", fontSize = 16.sp, color = Color.Black)
                                    }
                                }
                            )
                        }
                    }
                }
            }

        }
        is ViewRatingsViewModel.ViewRatingsState.Error -> {
            // Show error message
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = viewratings.message,
                    color = Color.Gray,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        else -> Unit
    }
}



