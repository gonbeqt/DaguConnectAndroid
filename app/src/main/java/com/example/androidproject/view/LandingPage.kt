package com.example.androidproject.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.androidproject.R
import com.example.androidproject.data.preferences.NotificationSettingManager

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LandingPageScreen(navController: NavController) {
    val windowSize = rememberWindowSizeClass()
    val context = LocalContext.current

    // Permission launcher for POST_NOTIFICATIONS
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
        NotificationSettingManager.saveNotification(true)
    }

    LaunchedEffect(Unit) {
        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(permission)
            }
        }
    }

    var mainText by remember { mutableStateOf("Find & Offer Local Services with Ease") }
    var messageText by remember { mutableStateOf("Connect with local residents in Dagupan City, to find or offer services, showcase your skills transact easily,and enjoy a secure platform") }
    var currentImage by remember { mutableStateOf(R.drawable.landing3) }

    var clickCount by remember { mutableStateOf(0) }
    var buttonText by remember { mutableStateOf("Next") }

    val header = listOf(
        "Find & Offer Local Services with Ease",
        "Connect with trusted local professionals",
        "Easily manage services and transactions"
    )
    val isTop = clickCount % 2 == 0

    val message = listOf(
        "Connect with local residents in Dagupan City, to find or offer services, showcase your skills transact easily,and enjoy a secure platform",
        "Work with trusted professionals in Dagupan City to fulfill your needs while maintaining trust and quality.",
        "Effortlessly manage all your transactions and services through a user-friendly and reliable platform."
    )

    val images = listOf(
        R.drawable.landing3,
        R.drawable.landing2,
        R.drawable.landing3
    )

    val activity = (context as? Activity)

    LaunchedEffect(Unit) {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isShown", true).apply()
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .offset(x = (-40).dp, y = if (isTop) 100.dp else 350.dp)
                .background(Color(0xFF66FFB2), shape = CircleShape)
                .zIndex(1f)

        )

        // Circle 2 (Right Corner)
        Box(
            modifier = Modifier
                .size(100.dp)
                .offset(x = 370.dp, y = if (isTop) 350.dp else 100.dp) // Switches between top & bottom
                .background(Color(0xFF122826), shape = CircleShape)
                .zIndex(1f)
        )



        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp)
                .padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { activity?.finish() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
            TextButton(onClick = { navController.navigate("landingpage2") }) {
                Text(text = "Skip", color = Color.Gray)
            }
        }

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = currentImage,
                transitionSpec = {
                    slideInHorizontally(initialOffsetX = { -it }) with slideOutHorizontally(targetOffsetX = { it })
                }
            ) { targetImage ->
                val imageSize = when (windowSize.width) {
                    WindowType.SMALL -> 250.dp
                    WindowType.MEDIUM -> 350.dp
                    WindowType.LARGE -> 400.dp
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp)
                        .size(imageSize)
                        ,
                    colors = CardDefaults.cardColors(containerColor = Color.White)

                ) {
                    Image(
                        painter = painterResource(id = targetImage),
                        contentDescription = "LandingPage",
                        modifier = Modifier
                            .fillMaxSize()
                            .size(imageSize),
                    )
                }
            }

            AnimatedContent(
                targetState = mainText,
                transitionSpec = {
                    slideInHorizontally(initialOffsetX = { -it }) with slideOutHorizontally(targetOffsetX = { it })
                }
            ) { targetText ->
                val fontSize = when (windowSize.width) {
                    WindowType.SMALL -> 20.sp
                    WindowType.MEDIUM -> 24.sp
                    WindowType.LARGE -> 28.sp
                }

                Text(
                    text = targetText,
                    fontWeight = FontWeight(700),
                    color = Color.Black,
                    fontSize = fontSize,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp, start = 15.dp, end = 15.dp)
                )
            }

            AnimatedContent(
                targetState = messageText,
                transitionSpec = {
                    slideInHorizontally(initialOffsetX = { -it }) with slideOutHorizontally(targetOffsetX = { it })
                }
            ) { targetMessage ->
                val fontSize = when (windowSize.width) {
                    WindowType.SMALL -> 16.sp
                    WindowType.MEDIUM -> 20.sp
                    WindowType.LARGE -> 22.sp
                }

                Text(
                    text = targetMessage,
                    color = Color.Gray,
                    fontSize = fontSize,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 30.dp, start = 15.dp, end = 15.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // **3-Dot Indicator**
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(3) { index ->
                    val animatedSize by animateDpAsState(
                        targetValue = if (index == clickCount) 20.dp else 8.dp,
                        animationSpec = tween(durationMillis = 300)
                    )
                    val animatedColor by animateColorAsState(
                        targetValue = if (index == clickCount) Color(0xFF66FFB2) else Color(0xFFBBF7D0),
                        animationSpec = tween(durationMillis = 300)
                    )

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(animatedSize)
                            .background(animatedColor, shape = CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (clickCount < 1) {
                        clickCount++
                        mainText = header[clickCount]
                        messageText = message[clickCount]
                        currentImage = images[clickCount]
                    } else {
                        buttonText = "Get Started"
                        mainText = header[2]
                        messageText = message[2]
                        currentImage = images[2]
                        clickCount++
                    }
                    if (clickCount == 3) {
                        navController.navigate("landingpage2")
                    }
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF122826))
            ) {
                Text(text = buttonText, fontSize = 16.sp)
            }
        }
    }
}


