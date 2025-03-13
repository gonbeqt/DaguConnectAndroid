package com.example.androidproject.view.tradesman

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.androidproject.ViewModelSetups
import com.example.androidproject.view.extras.LoadingUI
import com.example.androidproject.view.theme.myGradient3
import com.example.androidproject.viewmodel.jobs.ViewJobViewModel

@Composable
fun TradesmanApply(
    jobId: String,
    navController: NavController,
    viewModel: ViewJobViewModel
) {
    val viewJobState by viewModel.jobState.collectAsState()
    val jobID = jobId.toIntOrNull() ?: return

    LaunchedEffect(Unit) {
        viewModel.getJobById(jobID)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(myGradient3)
            .padding(WindowInsets.systemBars.asPaddingValues())

    ) {
        // Top Bar with Back Arrow and Title
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .background(myGradient3)
                    .fillMaxWidth()
                    .size(80.dp)
                    .padding(top = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Arrow Back",
                        modifier = Modifier
                            .clickable { navController.navigate("main_screen") }
                            .padding(start = 16.dp, top = 12.dp, end = 12.dp, bottom = 14.dp),
                        tint = Color(0xFF81D796)
                    )
                    Text(
                        text = "Job Details",
                        fontSize = 20.sp,
                        color = Color.White,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .weight(1f)
                    )
                }
            }
        }

        // Main Content Based on ViewJobState
        when (val viewJob = viewJobState) {
            is ViewJobViewModel.JobState.Loading -> {
                LoadingUI()
            }

            is ViewJobViewModel.JobState.Success -> {
                val job = viewJob.data
                val date = ViewModelSetups.formatDateTime(job.job.createdAt)
                val deadline = ViewModelSetups.formatDateTime(job.job.deadline)
                var jobType = job.job.jobType
                if (jobType == "Ac_technician") {
                    jobType = "AC Technician"
                }
                val items = listOf(jobType)
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    colors = CardDefaults.cardColors(Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        // Client Info Section
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = job.job.clientProfile,
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 15.dp)
                            ) {
                                Text(
                                    text = "Client",
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = job.job.clientFullname.orEmpty(),
                                    color = Color.Black,
                                    fontWeight = FontWeight(500),
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                                Box(
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = "Bookmark Icon",
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.size(4.dp))
                                        Text(
                                            text = "Deadline: $deadline",
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        // Job Title and Posting Info
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Hiring: $jobType",
                                fontSize = 24.sp,
                                fontWeight = FontWeight(500)
                            )
                        }
                        Text(
                            text = "Posted on $date - Active",
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )

                        // Job Description Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            border = BorderStroke(0.5.dp, Color(0xFFD9D9D9)),
                            colors = CardDefaults.cardColors(Color.White),
                            shape = RectangleShape
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp, horizontal = 20.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = job.job.jobDescription)
                            }
                        }

                        // Salary and Location Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(0.5.dp, Color(0xFFD9D9D9)),
                            colors = CardDefaults.cardColors(Color.White),
                            shape = RectangleShape
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp, horizontal = 20.dp)
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = "Estimated Salary")
                                    Text(
                                        text = "${job.job.salary} Pesos",
                                        color = Color.Gray
                                    )
                                }
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = "Location Icon",
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = job.job.address.orEmpty())
                                    }
                                }
                            }
                        }

                        // Specialty Required Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            border = BorderStroke(0.5.dp, Color(0xFFD9D9D9)),
                            colors = CardDefaults.cardColors(Color.White),
                            shape = RectangleShape
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp, horizontal = 10.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Specialty Required",
                                    color = Color.Black,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight(500),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    items.forEach { item ->
                                        Box(
                                            modifier = Modifier
                                                .size(110.dp, 50.dp)
                                                .background(Color(0xFFF1F1F1))
                                                .padding(4.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                        ) {
                                            Text(
                                                text = item,
                                                modifier = Modifier.align(Alignment.Center),
                                                color = Color.Black
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        // Other Services and Status
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Other services needed by this client (0)",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight(500)
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(50.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Status: ${job.job.status}",
                                    fontSize = 14.sp,
                                    fontStyle = FontStyle.Normal,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                // Apply Button at the Bottom
                Column(
                    modifier = Modifier
                        .background(Color.Black)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { navController.navigate("hiringdetails/${job.job.id}") },
                        modifier = Modifier.width(200.dp),
                        colors = ButtonDefaults.buttonColors(Color.White),
                        border = BorderStroke(1.dp, Color.Black)
                    ) {
                        Text(
                            text = "Apply Now",
                            fontSize = 14.sp,
                            fontWeight = FontWeight(500),
                            color = Color.Black
                        )
                    }
                }
            }

            is ViewJobViewModel.JobState.Error -> {
                val errorMessage = (viewJobState as ViewJobViewModel.JobState.Error).message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: $errorMessage",
                        fontSize = 18.sp,
                        color = Color.Red
                    )
                }
            }

            is ViewJobViewModel.JobState.Idle -> {
                // No UI to display in Idle state
            }
        }
    }
}