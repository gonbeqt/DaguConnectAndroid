package com.example.androidproject.view.pages2

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ProfileTradesman(modifier: Modifier = Modifier, navController: NavController) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                .fillMaxWidth()
                .height(70.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left-aligned text
            Text(
                text = "Profile",
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium
            )

            // Right-aligned icons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications Icon",
                    tint = Color(0xFF3CC0B0),
                    modifier = Modifier.size(32.dp)
                )
                Icon(
                    imageVector = Icons.Default.Message,
                    contentDescription = "Message Icon",
                    tint = Color(0xFF3CC0B0),
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { navController.navigate("message_screen") }
                )
            }
        }

        // Profile Info
        Column(
            modifier = Modifier.fillMaxWidth().background(Color.White)
        ) {
            Box(modifier = Modifier.padding(10.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF81D796), Color(0xFF39BFB1))
                            ), shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.White, RoundedCornerShape(50.dp))
                    ) {
                        Text(text = "👤", modifier = Modifier.align(Alignment.Center), fontSize = 50.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.padding(top = 5.dp)) {
                        Text(text = "Client’s Name", color = Color.White, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
                        Text(text = "Lorem@gmail.com", color = Color.White, style = TextStyle(fontSize = 14.sp))
                        Text(text = "Dagupan, Philippines", color = Color.White, style = TextStyle(fontSize = 14.sp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(30.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Default.Circle, contentDescription = "Active", tint = Color.Yellow, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Available", color = Color.Black, style = TextStyle(fontSize = 14.sp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = Color.LightGray)
                }
            }
        }

        // Additional Layout from Image
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically){
                Text(text = "Status : Available", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Box(modifier = Modifier
                    .size(30.dp)
                    .background(Color.White, RoundedCornerShape(50.dp))
                    .border(2.dp, Color.LightGray, RoundedCornerShape(50.dp)),contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit Status",
                        tint = Color.LightGray
                    )
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically){
                Text(text = "Est. Rate : ₱500", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Box(modifier = Modifier
                    .size(30.dp)
                    .background(Color.White, RoundedCornerShape(50.dp))
                    .border(2.dp, Color.LightGray, RoundedCornerShape(50.dp)),contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit Estimated Rate",
                        tint = Color.LightGray
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column{
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    Text(text = "About Me", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Box(modifier = Modifier
                        .size(30.dp)
                        .background(Color.White, RoundedCornerShape(50.dp))
                        .border(2.dp, Color.LightGray, RoundedCornerShape(50.dp)),contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit About Me",
                            tint = Color.LightGray
                        )
                    }
                }
                Text(text = "Descripton about yourself", fontSize = 18.sp, fontWeight = FontWeight.Normal)

            }
            Spacer(modifier = Modifier.height(8.dp))

            Column{
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Specialties", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Box(modifier = Modifier
                        .size(30.dp)
                        .background(Color.White, RoundedCornerShape(50.dp))
                        .border(2.dp, Color.LightGray, RoundedCornerShape(50.dp)),contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Specialties",
                            tint = Color.LightGray
                        )
                    }                }
                Spacer(modifier = Modifier.height(14.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(40.dp)
                                .background(
                                    Color.LightGray,
                                    RoundedCornerShape(50.dp)
                                )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Ratings and Testimonials", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = "Feedback from satisfied clients", fontSize = 14.sp, color = Color.Gray)
                Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text = "No ratings yet.", fontSize = 14.sp, color = Color.Gray)
                        Text(
                            text = "Showcase your services to earn reviews!",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

        }
    }
}
