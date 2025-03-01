package com.example.androidproject.view.pages

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.example.androidproject.model.client.ratingsItem
import com.example.androidproject.view.Feedback
import com.example.androidproject.view.Tradesman
import com.example.androidproject.view.theme.myGradient3
import com.example.androidproject.viewmodel.Resumes.ViewResumeViewModel
import com.example.androidproject.viewmodel.ratings.ViewRatingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookNow(viewResumeViewModel: ViewResumeViewModel, navController: NavController, resumeId : String,viewRatingsViewModel: ViewRatingsViewModel) {
    val viewResumeState by viewResumeViewModel.viewResumeState.collectAsState()
    val ResumeId = resumeId.toIntOrNull() ?: return
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewResumeViewModel.viewResume(ResumeId)
    }



    when(val state = viewResumeState){
        is ViewResumeViewModel.ViewResumeState.Loading -> {
            Text(text = "Loading...")
        }
        is ViewResumeViewModel.ViewResumeState.Success -> {
            val resume = state.data
            if (resume!= null) {
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
                                        model = resume.profilepic,
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
                                            text = resume.tradesmanfullname,
                                            color = Color.Black,
                                            fontWeight = FontWeight(500),
                                            fontSize = 20.sp,
                                            modifier = Modifier.padding(top = 10.dp)
                                        )
                                        resume.specialty?.let {
                                            BoxRow(specialties = it

                                            )
                                        }
                                        resume.preferedworklocation?.let {
                                            Text(
                                                text = it,
                                                color = Color.Black,
                                                fontSize = 16.sp
                                            )
                                        }


                                    }

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
                                                when {
                                                    resume.ratings == null || resume.ratings == 0f -> "0"
                                                    else -> String.format("%.1f", resume.ratings)
                                                },
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                }

                                // Additional Sections
                                Spacer(modifier = Modifier.height(4.dp))
                                Column(Modifier.padding(horizontal = 10.dp)) {
                                    Text(
                                        text = "About Me",
                                        color = Color.Black,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight(500),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)
                                    )
                                    Text(
                                        text = resume.aboutme,
                                        modifier = Modifier.padding(horizontal = 8.dp),
                                        fontWeight = FontWeight(500),
                                        color = Color.Black,
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row (Modifier.fillMaxWidth()
                                        .padding(horizontal = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween){
                                        Text(
                                            text = "Preferred Location",
                                            color = Color.Black,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight(500))

                                        resume.preferedworklocation?.let {
                                            Text(
                                                text = it,
                                                color = Color.Black,
                                                fontSize = 16.sp,
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row (Modifier.fillMaxWidth()
                                        .padding(horizontal = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween){
                                        Text(
                                            text = "Est. Rate",
                                            color = Color.Black,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight(500))

                                        Text(
                                            text =  "₱${resume.workfee}",
                                            color = Color.Black,
                                            fontSize = 16.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row (Modifier.fillMaxWidth()
                                        .padding(horizontal = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween){
                                        Text(
                                            text = "Trade Credential",
                                            color = Color.Black,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight(500))

                                        Text(
                                            text = "View File",
                                            color = Color.Black,
                                            fontSize = 16.sp,
                                            modifier = Modifier
                                                .clickable {

                                                    val fileUrl = resume.documents // Assuming this is the URL to the file
                                                    val fileName = "trade_credential_${resume.tradesmanfullname}.pdf" // Customize the file name as needed
                                                    if (fileUrl != null) {
                                                        downloadFile(context, fileUrl, fileName)
                                                    }
                                                }
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                        Text(
                                            text = "Contact Information",
                                            color = Color.Black,
                                            fontSize = 18.sp,
                                            modifier = Modifier.padding(horizontal = 10.dp),
                                            fontWeight = FontWeight(500))

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row (Modifier.fillMaxWidth()
                                        .padding(horizontal = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween){
                                        Text(
                                            text = "Phone Number",
                                            color = Color.Black,
                                            fontSize = 18.sp,
                                        )

                                        Text(
                                            text = resume.phonenumber?: "N/A",
                                            color = Color.Black,
                                            fontSize = 16.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row (Modifier.fillMaxWidth()
                                        .padding(horizontal = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween){
                                        Text(
                                            text = "Email",
                                            color = Color.Black,
                                            fontSize = 18.sp,
                                        )

                                        Text(
                                            text = resume.email,
                                            color = Color.Black,
                                            fontSize = 16.sp
                                        )
                                    }


                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Ratings And Testimonials",
                                        color = Color.Black,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight(500),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)
                                    )

                                    Text(
                                        text = "Feedback from satisfied clients",
                                        color = Color.Black,
                                        fontSize = 14.sp,
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
                                Text(text = "Chat Me", color = Color.White)
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
                                    color = Color.White
                                )
                            }
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
    Text(
        text = specialties,
        color = Color.Black,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        )
}

@Composable
fun FeedbackItem(viewRatingsViewModel: ViewRatingsViewModel, tradesmanId: Int) {
    val viewRatingState by viewRatingsViewModel.viewRatingsState.collectAsState()

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
                        .padding(16.dp), // Add some padding for better appearance
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
                                .padding(8.dp),
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



fun downloadFile(context: Context, fileUrl: String, fileName: String) {
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val uri = Uri.parse(fileUrl)

    val request = DownloadManager.Request(uri)
        .setTitle(fileName)
        .setDescription("Downloading")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        .setAllowedOverMetered(true)
        .setAllowedOverRoaming(true)

    downloadManager.enqueue(request)
}

