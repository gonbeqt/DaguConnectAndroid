package com.example.androidproject.view.client

import android.util.Log
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.androidproject.ViewModelSetups.Companion.isNotToday
import com.example.androidproject.ViewModelSetups.Companion.isToday
import com.example.androidproject.view.NotificationItem
import com.example.androidproject.viewmodel.notifications.GetNotificationViewModel

@Composable
fun NotificationScreen(navController: NavController, getNotification: GetNotificationViewModel) {
    val notifications = getNotification.getNotificationPagingData.collectAsLazyPagingItems()

    val todayNotifications = notifications.itemSnapshotList?.filter {
        it?.createdAt?.let { createdAt -> isToday(createdAt) } == true
    }

    val previousNotifications = notifications.itemSnapshotList?.filter {
        it?.createdAt?.let { createdAt -> isNotToday(createdAt) } == false
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2)) // Light gray background
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "You have 2 notifications today",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))


        Text(
            text = "Today"
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .size(320.dp)
                .background(Color(0xFFD9D9D9))

            ,
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            if (todayNotifications != null) {
                items(todayNotifications.size) { index ->
                    val today = todayNotifications[index]
                    if (today != null) {
                        NotificationCardToday()
                    } }
            }
        }
        Text(
            text = "Previous"
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .size(420.dp)
                .background(Color(0xFFD9D9D9))

            ,
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            if (previousNotifications != null) {
                items(previousNotifications.size) { index ->
                    val previous = previousNotifications[index]
                    if (previous != null) {
                        NotificationCardPrevious()
                    } }
            }
        }
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
fun NotificationCardToday() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text ="title",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "message",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Text(
                text = "time",
                fontSize = 12.sp,
                color = Color.Gray
            )

        }
    }
}

@Composable
fun NotificationCardPrevious() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text ="title",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "message",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Text(
                text = "time",
                fontSize = 12.sp,
                color = Color.Gray
            )

        }
    }
}
