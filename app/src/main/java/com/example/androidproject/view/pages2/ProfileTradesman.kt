package com.example.androidproject.view.pages2

import LogoutViewModel
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidproject.R
import com.example.androidproject.data.preferences.AccountManager
import com.example.androidproject.data.preferences.TokenManager


@Composable
fun ProfileTradesman(modifier: Modifier = Modifier, navController: NavController,logoutViewModel: LogoutViewModel) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabNames = listOf("My Resume", "General")

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
                    imageVector = Icons.Default.Settings,
                    contentDescription = "User Account",
                    tint = Color(0xFF3CC0B0),
                    modifier = Modifier.size(32.dp)
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
                        Text(text = "Tradesman’s Name", color = Color.White, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
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
                }
            }
        }


        // Tab Selection
        Column {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
            ) {
                tabNames.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title, fontSize = 14.sp) },
                    )
                }
            }

            // Tab Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(10.dp)
            ) {
                when (selectedTabIndex) {
                    0 -> MyResume(navController)
                    1 -> SettingsTradesmanScreen(navController,logoutViewModel)
                }
            }
        }
    }
}

@Composable
fun MyResume(navController: NavController){
    // Additional Layout from Image
    Column(modifier = Modifier.padding(8.dp)) {
        Box(modifier = Modifier
            .border(0.5.dp, Color.LightGray, RoundedCornerShape(10.dp))) {
            Column(modifier = Modifier
                .padding(10.dp)) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black, RoundedCornerShape(10.dp))
                    .clickable {navController.navigate("manageprofile")  },
                    contentAlignment = Alignment.Center){
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                        ){
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(30.dp)
                                .background(Color.Black, RoundedCornerShape(10.dp))
                                .border(2.dp, Color.White, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit profile and skills",
                                tint = Color.White
                            )
                        }

                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Status : Available",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )


                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Est. Rate : ₱500", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                }
                Spacer(modifier = Modifier.height(10.dp))
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "About Me", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                    }
                    Text(
                        text = "Descripton about yourself",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )

                }
                Spacer(modifier = Modifier.height(10.dp))

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Specialties", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
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

@Composable
fun GeneralTradesmanSettings(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF9F9F9),
                shape = RoundedCornerShape(8.dp))) {

            Column(modifier = Modifier.padding(10.dp)) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Text(
                        text = title,
                        modifier = Modifier.padding(start = 14.dp)
                    )

                }
                Text(
                    text = description,
                    modifier = Modifier.padding(top = 5.dp, start = 10.dp)
                )

            }

        }
    }

}


@Composable
fun SettingsTradesmanScreen(navController: NavController,logoutViewModel: LogoutViewModel) {

    val logoutResult by logoutViewModel.logoutResult.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(logoutResult) {
        logoutResult?.let {
            // Clear tokens and navigate regardless of result
            TokenManager.clearToken()
            AccountManager.clearAccountData()
            Toast.makeText(context, "logout successful", Toast.LENGTH_SHORT).show()
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
            logoutViewModel.resetLogoutResult()
        }
    }
    Column {

        GeneralTradesmanSettings(
            icon = ImageVector.vectorResource(id = R.drawable.ic_notification),
            title = "Notification",
            description = "Manage alerts and updates.",
            onClick = { /* Handle Notification click */ }
        )
        GeneralTradesmanSettings(
            icon = ImageVector.vectorResource(id = R.drawable.ic_privacy),
            title = "Privacy",
            description = "Change your password.",
            onClick = {navController.navigate("changepassword")}
        )
        Text(
            text = "Help and Support", fontWeight = FontWeight(500),
            fontSize = 20.sp, modifier = Modifier.padding( 12.dp)
        )
        GeneralTradesmanSettings(
            icon = ImageVector.vectorResource(id = R.drawable.ic_about),
            title = "About Us",
            description = "Know more about our team.",
            onClick = {navController.navigate("aboutus")}
        )
        GeneralTradesmanSettings(
            icon = ImageVector.vectorResource(id = R.drawable.ic_report),
            title = "Report a Problem",
            description = "Report a problem.",
            onClick = { /* Handle Report a Problem click */ }
        )
        Text(
            text = "Log Out", fontWeight = FontWeight(500),
            fontSize = 20.sp, modifier = Modifier.padding( 12.dp)
        )
        GeneralTradesmanSettings(
            icon = ImageVector.vectorResource(id = R.drawable.ic_logout),
            title = "Log Out",
            description = "",
            onClick = {
                val token = TokenManager.getToken()
                if (token != null) {
                    logoutViewModel.logout()
                } else {
                    // Handle case where token is null
                    TokenManager.clearToken()
                    AccountManager.clearAccountData()
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            }
        )
    }
}