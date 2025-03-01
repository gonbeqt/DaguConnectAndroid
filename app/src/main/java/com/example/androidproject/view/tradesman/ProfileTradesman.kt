package com.example.androidproject.view.tradesman

import LogoutViewModel
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.data.preferences.AccountManager
import com.example.androidproject.data.preferences.TokenManager
import com.example.androidproject.viewmodel.Tradesman_Profile.ViewTradesmanProfileViewModel
import kotlinx.coroutines.delay
import viewResume
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun ProfileTradesman(
    modifier: Modifier = Modifier,
    navController: NavController,
    logoutViewModel: LogoutViewModel,
    viewTradesmanProfileViewModel: ViewTradesmanProfileViewModel
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabNames = listOf("Job Profile", "General")
    val viewTradesmanProfilestate by viewTradesmanProfileViewModel.viewTradesmanProfileResumeState.collectAsState()

    LaunchedEffect(Unit) {
        viewTradesmanProfileViewModel.viewTradesmanProfile()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        when (val trademanProf = viewTradesmanProfilestate) {
            is ViewTradesmanProfileViewModel.ViewTradesmanProfileState.Loading -> {
                // Show loading indicator
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ViewTradesmanProfileViewModel.ViewTradesmanProfileState.Success -> {
                // Handle success state
                val tradesmanDetails = trademanProf.data
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
                            // Tradesman image
                            AsyncImage(
                                model = tradesmanDetails.profilepic?: "N/A",
                                contentDescription = "Tradesman Image",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(start = 10.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.padding(top = 5.dp)) {
                                Text(text = tradesmanDetails.tradesmanfullname?: "N/A", color = Color.White, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
                                Text(text = tradesmanDetails.email?: "N/A", color = Color.White, style = TextStyle(fontSize = 14.sp))
                                Text(text = tradesmanDetails.preferedworklocation?: "N/A", color = Color.White, style = TextStyle(fontSize = 14.sp))
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
                            0 -> JobProfile(navController,tradesmanDetails)
                            1 -> SettingsTradesmanScreen(navController, logoutViewModel)
                        }
                    }
                }
            }
            is ViewTradesmanProfileViewModel.ViewTradesmanProfileState.Error -> {
                // Handle error state
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error: ${trademanProf.message}", color = Color.Red)
                }
            }
            else -> Unit
        }
    }
}

@Composable
fun JobProfile(navController: NavController, tradesmanDetails: viewResume) {
    var scale by remember { mutableStateOf(1f) }

    val animatedScale by animateFloatAsState(
        targetValue = if (scale == 1f) 1.1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    // Automatically trigger animation on composition
    LaunchedEffect(Unit) {
        while (true) {
            scale = if (scale == 1f) 1.1f else 1f
            delay(800.milliseconds)
        }
    }

    // Check if isapprove is 0, then set all fields to "N/A" or appropriate defaults
    val displayDetails = if (tradesmanDetails.isapprove == 0) {
        tradesmanDetails.copy(
            specialty = "N/A",
            aboutme = "N/A",
            preferedworklocation = "N/A",
            workfee = 0,
            phonenumber = "N/A" // Provide a default non-null value for phonenumber
            // Add other non-nullable fields here if they exist
        )
    } else {
        tradesmanDetails
    }

    // Rest of the composable remains the same...
    Column(modifier = Modifier.padding(8.dp).verticalScroll(rememberScrollState())) {
        Box(modifier = Modifier.border(0.5.dp, Color.LightGray, RoundedCornerShape(10.dp))) {
            Column(modifier = Modifier.padding(10.dp)) {
                if (tradesmanDetails.isapprove == 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .scale(animatedScale)
                            .background(Color(0xFF3E5CE1), RoundedCornerShape(10.dp))
                            .clickable {
                                navController.navigate("profileverification/${tradesmanDetails.statusofapproval}")
                                scale = 1.1f
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = "Verify Profile",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Specialty : ${displayDetails.specialty?.takeIf { it != "null" } ?: "N/A"}",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "About Me:",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = displayDetails.aboutme ?: "N/A",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Normal
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Preferred Location : ${displayDetails.preferedworklocation ?: "N/A"}",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Est. Rate : ${displayDetails.workfee?.takeIf { it != 0 }?.toString() ?: "N/A"}",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Trade Credential :",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Ratings and Testimonials", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = "Feedback from satisfied clients", fontSize = 14.sp, color = Color.Gray)
            Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
    trailingIcon: ImageVector?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                    , tint = Color.Black
                )
                Column(modifier = Modifier.padding(start = 14.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
            trailingIcon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,

                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                        .clickable { onClick() }

                )
            }
        }
    }
}

@Composable
fun SettingsTradesmanScreen(navController: NavController,logoutViewModel: LogoutViewModel) {
    var isChecked by remember { mutableStateOf(true) }
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
    Column(modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize().padding(bottom = 100.dp)) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable { },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector =Icons.Default.NotificationsNone,
                            contentDescription = "Notification Icon",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Black
                        )
                        Column(modifier = Modifier.padding(start = 14.dp)) {
                            Text(
                                text = "Notification",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = "Manage alerts update",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                    Switch(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF3CC0B0),
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.Gray
                        )
                    )
                }
            }
        GeneralTradesmanSettings(
                icon = ImageVector.vectorResource(id = R.drawable.ic_privacy),
                title = "Privacy",
                description = "Change your password.",
                trailingIcon = Icons.Default.ArrowForwardIos,
                onClick = { navController.navigate("changepassword") }
            )
            Text(
                text = "Help and Support", fontWeight = FontWeight(500),
                fontSize = 20.sp, modifier = Modifier.padding( 12.dp)
            )
        GeneralTradesmanSettings(
                icon = ImageVector.vectorResource(id = R.drawable.ic_about),
                title = "About Us",
                description = "Know more about our team.",
                trailingIcon = Icons.Default.ArrowForwardIos,
                onClick = { navController.navigate("aboutus") }
            )
        GeneralTradesmanSettings(
                icon = ImageVector.vectorResource(id = R.drawable.ic_report),
                title = "Report a Problem",
                description = "Report a problem.",
                trailingIcon = Icons.Default.ArrowForwardIos,
                onClick = { navController.navigate("reportproblem") }
            )
        Text(
            text = "Log Out", fontWeight = FontWeight(500), color = Color.Black,
            fontSize = 20.sp, modifier = Modifier.padding( 12.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(horizontal = 16.dp)
                .padding(bottom = 5.dp)
                .clickable {
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
                },
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Added elevation
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.align(Alignment.CenterStart).padding(16.dp), // Aligns Row to center-start
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start

                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_logout),
                        contentDescription = "Logout Icon",
                        modifier = Modifier.padding(start = 5.dp),
                        tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Log Out",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Start,
                        color = Color.Black
                    )
                }
            }
        }
    }
}