package com.example.androidproject.view.tradesman

import androidx.compose.foundation.Image
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.view.theme.myGradient3
import java.sql.Types.NULL

@Composable
fun TradesmanPendingDetails(modifier: Modifier = Modifier, navController: NavController) {

    var showApproveDialog by remember { mutableStateOf(false) }
    var showDeclineDialog by remember { mutableStateOf(false) }
    var showJobApproveDialog by remember { mutableStateOf(false) }
    var showDeclineReasons by remember { mutableStateOf(false) }
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
    

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)
            )
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            shape = RoundedCornerShape(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
            ) {
                Row(modifier = Modifier
                    .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable {},
                        tint = Color(0xFF81D796)
                    )
                    Text(
                        text = "Work Details",
                        fontSize = 24.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.weight(1f)
                            .padding(start = 8.dp)
                    )
                }
            }
        }


        Column(modifier = Modifier.weight(1f)){

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
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
                        Text(
                            text = "Your approval is pending: Approve or Decline",
                            fontSize = nameTextSize,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(200.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    shape = RoundedCornerShape(0.dp, 0.dp, 15.dp, 15.dp) // Keep card shape
                ) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 18.dp, horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Client’s Information",
                            fontSize = nameTextSize,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
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
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Client image
                            Image(
                                painter = painterResource(id = R.drawable.pfp),
                                contentDescription = "Tradesman Image",
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
                                    text = "Ezekiel Vidal",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = nameTextSize,
                                )
                                Text(
                                    text = "09576947632",
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = smallTextSize,
                                )


                                Text(
                                    text = "21 Road St. Dagupan City, Pangasinan",
                                    color = Color.Gray,
                                    fontSize = smallTextSize,
                                )
                            }
                        }

                    }

                }
                Spacer(Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
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

                                text = "Task Information",
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
                                        text = "Job Date: March 23, 2025",
                                        fontSize = nameTextSize,
                                        color = Color.Black,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }
                            }
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
                                            text = "Optional Details:",
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
                                        Text(modifier = Modifier.padding(16.dp),text = "Our house needs to be repainted. For further details just contact or chat me.")
                                    }
                                }

                            }

                        }

                    }
                }
                Spacer(Modifier.height(10.dp))

                // Third Column with Card and content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
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
                                        text = "Contact Client",
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
                                        .size(32.dp)
                                )
                            }


                        }

                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp) // Space out the buttons
            ) {
                Box(
                    modifier = Modifier
                        .clickable { showDeclineDialog = true }
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                        .weight(1f)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Decline", fontSize = 14.sp)
                }

                Box(
                    modifier = Modifier
                        .clickable { showApproveDialog = true }
                        .background(
                            color = Color(0xFF42C2AE),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .weight(1f)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Text(text = "Approve", color = Color.White, fontSize = 14.sp)
                }
            }
        }
    }
    if (showApproveDialog) {
        AlertDialog(
            onDismissRequest = { showApproveDialog = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_approve_decline),
                        contentDescription = "Approval Icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Approve Job",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                }
            },
            text = { Text("Once approved, this job will be marked as active. Proceed?") },
            confirmButton = {
                Button(
                    onClick = {
                        showApproveDialog = false
                        showJobApproveDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42C2AE))
                ) {
                    Text("Confirm", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showApproveDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }

    if (showDeclineDialog) {
        AlertDialog(
            onDismissRequest = { showDeclineDialog = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_approve_decline),
                        contentDescription = "Approval Icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Decline Job",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                }
            },
            text = { Text("Once declined, this job may not be available again. Proceed?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeclineDialog = false
                        showDeclineReasons = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Confirm", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeclineDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }

    if (showJobApproveDialog) {
        AlertDialog(
            onDismissRequest = { showJobApproveDialog = false },
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_jobapproved_checked),
                    contentDescription = "Job Approve Icon",
                    modifier = Modifier.size(60.dp)
                )
            },
            title = { Text("Job Approved!") },
            text = { Text("Reach out to the client for more project details.") },
            confirmButton = {
                Button(
                    onClick = {
                        showJobApproveDialog = false

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42C2AE)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),

                    ) {
                    Text("OK", color = Color.White)
                }
            }
        )
    }

    if (showDeclineReasons) {
        var selectedReason by remember { mutableStateOf<String?>(null) }

        val reasons = listOf(
            "Workload concerns",
            "Schedule conflicts",
            "Relocation issues",
            "Committed to a contract project",
            "Short notice start date",
            "Personal Reasons",
            "Other"
        )

        AlertDialog(
            onDismissRequest = { showDeclineReasons = false },
            title = {
                Text(
                    text = "Reason for Declination",
                    fontSize = 18.sp,
                    color = Color(0xFF42C2AE)
                )
            },
            text = {
                Column {
                    reasons.forEach { reason ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedReason = reason },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selectedReason == reason,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedReason = reason
                                    }
                                }
                            )
                            Text(reason, fontSize = 14.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeclineReasons = false
                    },
                    enabled = selectedReason != null,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42C2AE)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit", color = Color.White)
                }
            }
        )
    }
}

