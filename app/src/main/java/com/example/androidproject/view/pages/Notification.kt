package com.example.androidproject.view.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidproject.view.NotificationItem

@Composable
fun NotificationScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2)) // Light gray background
    ) {
        NotificationTopSection(navController)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "You have 2 notifications today",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        NotificationSection(title = "Today", notifications = todayNotifications)
        NotificationSection(title = "Previous", notifications = previousNotifications)
    }
}

@Composable
fun NotificationTopSection(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        shape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .size(100.dp)
                .padding(top = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Arrow Back",
                    Modifier
                        .clickable { navController.navigate("main_screen") }
                        .padding(16.dp),
                    tint = Color(0xFF81D796)
                )

                Text(
                    text = "Notifications", // Updated title
                    fontSize = 24.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .weight(1f)
                )
                TextButton (onClick = {}){
                    Text("Clear",
                        color = Color.Gray,
                        textAlign = TextAlign.Center)
                }


            }
        }
        }
}

@Composable
fun NotificationSection(title: String, notifications: List<NotificationItem>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        notifications.forEach { notification ->
            NotificationCard(notification)
        }
    }
}

@Composable
fun NotificationCard(notification: NotificationItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = notification.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = notification.color
            )

            Text(
                text = notification.message,
                fontSize = 12.sp,
                color = Color.Gray
            )

            Text(
                text = notification.time,
                fontSize = 12.sp,
                color = Color.Gray
            )

        }
    }
}

val todayNotifications = listOf(
    NotificationItem(
        title = "New Booking Alert!!",
        message = "Client's name has approved your application.",
        time = "Just Now",
        color = Color(0xFF81D796)
    ),
    NotificationItem(
        title = "New Booking Alert!!",
        message = "Client's name has approved your application.",
        time = "3 min",
        color = Color(0xFF81D796)
    )
)

val previousNotifications = listOf(
    NotificationItem(
        title = " New Booking Alert!",
        message = "Client's name has scheduled a job with you.",
        time = "Feb 1",
        color = Color(0xFFFFA726)
    ),
    NotificationItem(
        title = " New Booking Alert!",
        message = "Client's name has scheduled a job with you.",
        time = "Jan 28",
        color = Color(0xFFFFA726)
    ),
    NotificationItem(
        title = " Application Completed",
        message = "Your application status is declined.",
        time = "Jan 28",
        color = Color(0xFFE57373)
    ),
    NotificationItem(
        title = " Application Update",
        message = "Your application status is declined.",
        time = "Jan 28",
        color = Color(0xFFE57373)
    )
)
