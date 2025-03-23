package com.example.androidproject.view.client

import android.health.connect.datatypes.units.Length
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.androidproject.ViewModelSetups
import com.example.androidproject.ViewModelSetups.Companion.getDateOnly
import com.example.androidproject.ViewModelSetups.Companion.isNotToday
import com.example.androidproject.ViewModelSetups.Companion.isToday
import com.example.androidproject.model.Notification
import com.example.androidproject.viewmodel.notifications.ClearNotificationViewModel
import com.example.androidproject.viewmodel.notifications.GetNotificationViewModel

@Composable
fun NotificationScreen(navController: NavController, getNotification: GetNotificationViewModel, clearNotification: ClearNotificationViewModel) {
    val notifications = getNotification.getNotificationPagingData.collectAsLazyPagingItems()

    LaunchedEffect(notifications) {
        getNotification.refreshNotification()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2)) // Light gray background
    ) {
        NotificationTopSection(navController, clearNotification)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "You have ${notifications.itemCount} notifications today",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
            ,
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(notifications.itemCount) { index ->
                val notification = notifications[index]
                if (notification != null) {
                    NotificationCardToday(notification)
                    Log.d("NotificationScreen", "Notification Title: ${notification.notificationTitle}")
                }
            }
        }
    }
}

@Composable
fun NotificationTopSection(navController: NavController, clearNotification: ClearNotificationViewModel) {
    val clearNotificationState by clearNotification.clearNotificationResult.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(clearNotificationState) {
        clearNotificationState?.let {
            Toast.makeText(context, "Notifications cleared", Toast.LENGTH_SHORT).show()
        }
    }
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
                        .clickable {
                            if (navController.previousBackStackEntry != null) {
                                navController.navigateUp()

                            } else {
                                navController.navigate("main_screen") {
                                    popUpTo("Home") { inclusive = false }
                                }
                            }
                            Log.d("Navigation", "Previous BackStack Entry: ${navController.previousBackStackEntry?.destination?.route}")

                        }
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
                TextButton (onClick = {

                }){
                    Text("Clear",
                        color = Color.Gray,
                        textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
fun NotificationCardToday(notification: Notification) {
    val createdAt = ViewModelSetups.formatDateTime(notification.createdAt)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text ="${notification.notificationTitle}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "${notification.message}",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Text(
                text = "$createdAt",
                fontSize = 12.sp,
                color = Color.Gray
            )

        }
    }
}

