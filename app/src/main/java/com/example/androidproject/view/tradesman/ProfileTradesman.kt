package com.example.androidproject.view.tradesman

import LogoutViewModel
import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import com.example.androidproject.view.theme.myGradient4
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.androidproject.R
import com.example.androidproject.data.WebSocketManager
import com.example.androidproject.data.preferences.AccountManager
import com.example.androidproject.data.preferences.TokenManager
import com.example.androidproject.view.WindowType

import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.model.client.viewResume
import com.example.androidproject.view.extras.LoadingUI
import com.example.androidproject.view.extras.SnackbarController
import com.example.androidproject.view.theme.myGradient4
import com.example.androidproject.viewmodel.Tradesman_Profile.UpdateTradesmanActiveStatusViewModel
import com.example.androidproject.viewmodel.Tradesman_Profile.UpdateTradesmanProfileViewModel
import com.example.androidproject.viewmodel.Tradesman_Profile.ViewTradesmanProfileViewModel
import com.example.androidproject.viewmodel.ratings.ViewRatingsViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ProfileTradesman(
    modifier: Modifier = Modifier,
    navController: NavController,
    logoutViewModel: LogoutViewModel,
    viewTradesmanProfileViewModel: ViewTradesmanProfileViewModel,
    updateTradesmanProfileViewModel: UpdateTradesmanProfileViewModel,
    updateTradesmanActiveStatusViewModel: UpdateTradesmanActiveStatusViewModel,
    viewRatingsViewModel: ViewRatingsViewModel,
    initialTabIndex: Int = 0
) {
    // Function to check network connectivity
    fun checkNetworkConnectivity(connectivityManager: ConnectivityManager): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }

    val context = LocalContext.current
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val isConnected = remember { mutableStateOf(checkNetworkConnectivity(connectivityManager)) }



    // State for profile update
    val updateProfileState by updateTradesmanProfileViewModel.updateTradesmanProfileState.collectAsState()

    LaunchedEffect(updateProfileState) {
        when (val updatingProfile = updateProfileState) {
            is UpdateTradesmanProfileViewModel.UpdateTradesmanProfileState.Success -> {
                updateTradesmanProfileViewModel.resetState()
                SnackbarController.show("Profile updated successfully")
            }
            is UpdateTradesmanProfileViewModel.UpdateTradesmanProfileState.Error -> {
                val errorMessage = updatingProfile.message
                SnackbarController.show(errorMessage)
            }
            else -> Unit
        }
    }

    val updateTradesmanActiveStatusState by updateTradesmanActiveStatusViewModel.updateStatusState.collectAsState()

    var showLoading by remember { mutableStateOf(true) }
    var showRetryLoading  by remember { mutableStateOf(false) }

    var selectedTabIndex by remember { mutableIntStateOf(initialTabIndex) }
    val tabNames = listOf("Job Profile", "General")
    val viewTradesmanProfilestate by viewTradesmanProfileViewModel.viewTradesmanProfileResumeState.collectAsState()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            updateTradesmanProfileViewModel.updateTradesmanProfile(selectedImageUri!!, context)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val imagesGranted = permissions[Manifest.permission.READ_MEDIA_IMAGES] == true
        val userSelectedGranted = permissions[Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED] == true

        when {
            imagesGranted -> imageLauncher.launch("image/*")
            userSelectedGranted -> imageLauncher.launch("image/*")
            else -> {
                val shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
                if (shouldShowRationale) {
                    SnackbarController.show("Permission is required to select a profile picture")
                } else {
                    SnackbarController.show("Permission denied. Enable it in Settings.")
                    val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:${context.packageName}")
                    context.startActivity(intent)
                }
            }
        }
    }


    // Fetch profile data when the composable is first launched or when retrying
    LaunchedEffect(showLoading) {
        if (showLoading && isConnected.value) {
            viewTradesmanProfileViewModel.viewTradesmanProfile()
        }
    }

// Handle retry loading animation
    LaunchedEffect(showRetryLoading) {
        if (showRetryLoading) {
            delay(1500) // Show LoadingUI for 1.5 seconds
            isConnected.value = checkNetworkConnectivity(connectivityManager)
            if (isConnected.value) {
                showLoading = true // Trigger data fetch if internet is back
            }
            showRetryLoading = false // Hide loading animation
        }
    }
    LaunchedEffect(updateTradesmanActiveStatusState) {
        when (val updatingStatus = updateTradesmanActiveStatusState) {
            is UpdateTradesmanActiveStatusViewModel.UpdateStatusState.Success -> {
                updateTradesmanActiveStatusViewModel.resetState()
                viewTradesmanProfileViewModel.viewTradesmanProfile()
                SnackbarController.show ("Status updated successfully")

            }
            is UpdateTradesmanActiveStatusViewModel.UpdateStatusState.Error -> {
                val errorMessage = updatingStatus.message
                SnackbarController.show(errorMessage)
            }
            else -> Unit
        }
    }

    // Use a top-level Box to layer content and snackbar
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Profile",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }

            if (!isConnected.value) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if(showRetryLoading){
                        LoadingUI()
                    }else{

                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No Internet Connection",
                            fontSize = 18.sp,
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Please check your internet and try again.",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .clickable {
                                    showRetryLoading = true // Start retry loading animation
                                }
                                .background(Color(0xFF3CC0B0), RoundedCornerShape(8.dp))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Retry",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                    when (val profileState = viewTradesmanProfilestate) {
                        is ViewTradesmanProfileViewModel.ViewTradesmanProfileState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                LoadingUI()
                            }
                        }

                        is ViewTradesmanProfileViewModel.ViewTradesmanProfileState.Success -> {
                            val isUpdating =
                                updateTradesmanActiveStatusState is UpdateTradesmanActiveStatusViewModel.UpdateStatusState.Loading
                            val tradesmanDetails = profileState.data
                            var isAvailable by remember { mutableStateOf(tradesmanDetails.isActiveBoolean) }
                            var previousAvailability by remember { mutableStateOf(isAvailable) }

                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)) {
                                Box(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(130.dp)
                                            .background(
                                                myGradient4,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(16.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(100.dp)
                                                .background(Color.White, RoundedCornerShape(50.dp))
                                        ) {
                                            AsyncImage(
                                                model = selectedImageUri
                                                    ?: tradesmanDetails.profilePic,
                                                contentDescription = "Tradesman Image",
                                                modifier = Modifier
                                                    .size(100.dp)
                                                    .clip(CircleShape),
                                                contentScale = ContentScale.Crop
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFF42C2AE))
                                                    .align(Alignment.BottomEnd)
                                                    .clickable {
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                                            permissionLauncher.launch(
                                                                arrayOf(
                                                                    Manifest.permission.READ_MEDIA_IMAGES,
                                                                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                                                                )
                                                            )
                                                        } else {
                                                            if (ContextCompat.checkSelfPermission(
                                                                    context,
                                                                    Manifest.permission.READ_MEDIA_IMAGES
                                                                ) == PackageManager.PERMISSION_GRANTED
                                                            ) {
                                                                imageLauncher.launch("image/*")
                                                            } else {
                                                                permissionLauncher.launch(
                                                                    arrayOf(
                                                                        Manifest.permission.READ_MEDIA_IMAGES
                                                                    )
                                                                )
                                                            }
                                                        }
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = "Edit Profile Picture",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Column {
                                            Row {
                                                Text(
                                                    text = tradesmanDetails.tradesmanFullName
                                                        ?: "N/A",
                                                    color = Color.White,
                                                    style = TextStyle(
                                                        fontSize = 18.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))

                                                    Icon(
                                                        painter = painterResource(id = if (tradesmanDetails.isApprove == 0) R.drawable.unverified_ic else R.drawable.verified_ic),
                                                        contentDescription = "Profile Verified",
                                                        tint = Color.Black,
                                                        modifier = Modifier.size(24.dp)
                                                    )


                                            }
                                            Text(
                                                text = tradesmanDetails.email ?: "N/A",
                                                color = Color.White,
                                                style = TextStyle(fontSize = 14.sp)
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row {
                                                Box(
                                                    modifier = Modifier
                                                        .width(100.dp)
                                                        .height(30.dp)
                                                        .clip(RoundedCornerShape(50.dp))
                                                        .background(Color.White)
                                                        .clickable(enabled = !isUpdating) {
                                                            previousAvailability = isAvailable
                                                            isAvailable = !isAvailable
                                                            updateTradesmanActiveStatusViewModel.updateStatusState(
                                                                isAvailable
                                                            )
                                                        },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.Center
                                                    ) {
                                                        Icon(
                                                            Icons.Default.Circle,
                                                            contentDescription = if (isAvailable) "Available" else "Unavailable",
                                                            tint = if (isAvailable) Color.Green else Color.Red,
                                                            modifier = Modifier.size(16.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text(
                                                            text = if (isAvailable) "Available" else "Unavailable",
                                                            color = Color.Black,
                                                            style = TextStyle(fontSize = 14.sp)
                                                        )
                                                    }
                                                }
                                                Box(
                                                    modifier = Modifier
                                                        .padding(start = 10.dp)
                                                        .size(26.dp)
                                                        .background(
                                                            Color.White,
                                                            RoundedCornerShape(50.dp)
                                                        )
                                                        .clickable { navController.navigate("availabilitystatus") }
                                                        .border(
                                                            1.dp,
                                                            Color.Gray,
                                                            RoundedCornerShape(50.dp)
                                                        ),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        Icons.Default.QuestionMark,
                                                        contentDescription = "Edit profile and skills",
                                                        tint = Color.Black
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Column {
                                TabRow(
                                    selectedTabIndex = selectedTabIndex,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    tabNames.forEachIndexed { index, title ->
                                        Tab(
                                            selected = selectedTabIndex == index,
                                            onClick = { selectedTabIndex = index },
                                            text = { Text(title, fontSize = 14.sp) }
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .padding(10.dp)
                                ) {
                                    when (selectedTabIndex) {
                                        0 -> JobProfile(
                                            navController,
                                            tradesmanDetails,
                                            viewRatingsViewModel,
                                        )

                                        1 -> SettingsTradesmanScreen(navController, logoutViewModel)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(bottom = 76.dp),
                                        contentAlignment = Alignment.BottomCenter
                                    ) {
                                        SnackbarController.ObserveSnackbar()
                                    }
                                }


                            }

                        }

                        is ViewTradesmanProfileViewModel.ViewTradesmanProfileState.Error -> {

                        }

                        else -> Unit
                    }


            }

        }

    }

}


@Composable
fun JobProfile(navController: NavController, tradesmanDetails: viewResume,viewRatingsViewModel : ViewRatingsViewModel) {
    var scale by remember { mutableStateOf(1f) }
    val windowSize = rememberWindowSizeClass()
    val context = LocalContext.current
    var downloadId by remember { mutableStateOf<Long?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val ratingsState by viewRatingsViewModel.viewRatingsState.collectAsState()
    val tradesmanId = tradesmanDetails.userid

    LaunchedEffect(Unit) {
        viewRatingsViewModel.viewRatings(tradesmanId)
    }

        val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 18.sp
        WindowType.MEDIUM -> 20.sp
        WindowType.LARGE -> 22.sp
    }
    val taskTextSize = when (windowSize.width) {
        WindowType.SMALL -> 18.sp
        WindowType.MEDIUM -> 20.sp
        WindowType.LARGE -> 22.sp
    }
    val smallTextSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }
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
    val displayDetails = if (tradesmanDetails.isApprove == 0) {
        tradesmanDetails.copy(
            specialty = "N/A",
            aboutMe = "N/A",
            preferredWorkLocation = "N/A",
            workFee = 0,
            phoneNumber = "N/A" // Provide a default non-null value for phonenumber
            // Add other non-nullable fields here if they exist
        )
    } else {
        tradesmanDetails
    }



    // Rest of the composable remains the same...
    Column(modifier = Modifier
        .padding(2.dp)
        .padding(bottom = 100.dp)
        .verticalScroll(rememberScrollState()))
    {
        Box(modifier = Modifier.border(0.5.dp, Color.LightGray, RoundedCornerShape(10.dp))) {
            Column(modifier = Modifier.padding(10.dp)) {
                if (tradesmanDetails.isApprove == 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .scale(animatedScale)
                            .background(Color(0xFF3E5CE1), RoundedCornerShape(10.dp))
                            .clickable {
                                navController.navigate("profileverification/${tradesmanDetails.statusOfApproval}")
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
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)){
                    Box(
                        modifier = Modifier
                            .clickable { }
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color(0xFF122826), shape = RoundedCornerShape(12.dp))
                            .weight(1f)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Public View", color = Color(0xFF122826), fontSize = 14.sp)
                    }

                    Box(
                        modifier = Modifier
                            .clickable { }
                            .background(
                                color = Color(0xFF122826),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .weight(1f)
                            .clickable { navController.navigate("manageprofile/${tradesmanDetails.preferredWorkLocation}/${tradesmanDetails.phoneNumber}/${tradesmanDetails.workFee}/${tradesmanDetails.aboutMe}") }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Manage Profile", color = Color.White, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Specialty : ",
                        color = Color.Gray,
                        fontSize = nameTextSize,
                        fontWeight = FontWeight.Medium
                    )
                        Text(
                            text = displayDetails.specialty?.replace("_"," ").takeIf { it != "null" } ?: "N/A",
                            color = Color.Black,
                            fontSize = taskTextSize,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)

                        )

                }

                Spacer(modifier = Modifier.height(10.dp))
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Preferred Location :",
                            color = Color.Gray,
                            fontSize = nameTextSize,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = displayDetails.preferredWorkLocation?.let { "$it, Pangasinan" } ?: "N/A",
                            color = Color.Black,
                            fontSize = taskTextSize,
                            fontWeight = FontWeight.Medium
                        )
                    }

                }
                Spacer(Modifier.height(10.dp))
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Phone Number :",
                            color = Color.Gray,
                            fontSize = nameTextSize,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = tradesmanDetails.phoneNumber ?: "N/A",
                            color = Color.Black,
                            fontSize = taskTextSize,
                            fontWeight = FontWeight.Medium

                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Est. Rate :",
                        fontSize = nameTextSize,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = displayDetails.workFee?.takeIf { it != 0 }?.let { "₱ $it /hr" } ?: "N/A",
                        fontSize = taskTextSize,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Text(
                        text = "Trades Credential:" ,
                                fontSize = nameTextSize,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )

                        Text(
                            text = "View File",
                            color = Color.Blue,
                            fontSize = taskTextSize,
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .clickable {
                                    val fileUrl = tradesmanDetails.documents
                                    val fileName = "trade_credential_${tradesmanDetails.tradesmanFullName}.pdf"
                                    if (fileUrl != null) {
                                        try {
                                            downloadId = downloadFileTradesman(context, fileUrl, fileName)
                                            SnackbarController.show("Download success")
                                        } catch (e: Exception) {
                                            SnackbarController.show("Download failed")

                                        }
                                    } else {
                                        SnackbarController.show("No file available")

                                    }
                                }
                        )

                }
                Spacer(modifier = Modifier.height(10.dp))
                Column {

                   Text(
                       text = "About Me:",
                       fontSize = nameTextSize,
                       color = Color.Gray,
                       fontWeight = FontWeight.Medium
                   )

                    Text(
                        text = displayDetails.aboutMe ?: "N/A",
                        fontSize = taskTextSize,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )

                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 0.dp, end = 0.dp, top = 0.dp, bottom = 16.dp)) {

            Text(text = "Ratings", fontSize = nameTextSize, fontWeight = FontWeight.Bold)
            Text(text = "Feedback from satisfied clients", fontSize = taskTextSize, color = Color.Gray)
        }
        when (val viewRatings = ratingsState) {
            is ViewRatingsViewModel.ViewRatingsState.Loading -> {
                // Loading state (Optional: Show a progress indicator)
            }
            is ViewRatingsViewModel.ViewRatingsState.Success -> {
                val ratingsList = viewRatings.data

                if (ratingsList.isEmpty()) {
                    // Ensure the text is properly centered
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                        ,
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
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { showDialog = true },
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
                            if (showDialog) {
                                AlertDialog(
                                    containerColor = Color.White,
                                    onDismissRequest = { showDialog = false },
                                    title = {

                                        Text(
                                            text = "Feedback Details",
                                            modifier = Modifier.padding(start = 8.dp)
                                        )

                                    },
                                    text = {
                                        Column(
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .verticalScroll(
                                                    rememberScrollState()
                                                )
                                        ) {
                                            // Profile Section
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(bottom = 8.dp)
                                            ) {
                                                AsyncImage(
                                                    model = ratings.client_profile,
                                                    contentDescription = "Profile Picture",
                                                    modifier = Modifier
                                                        .size(75.dp)
                                                        .clip(CircleShape)
                                                        .border(1.dp, Color.Gray, CircleShape)
                                                )
                                                Column {
                                                    Text(
                                                        text = ratings?.client_name ?: "Unknown",
                                                        modifier = Modifier.padding(start = 8.dp),
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 20.sp
                                                    )
                                                    Row(
                                                        modifier = Modifier.padding(bottom = 8.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Star,
                                                            contentDescription = "Rating",
                                                            tint = Color.Yellow
                                                        )
                                                        Text(
                                                            text = "${ratings?.ratings ?: 0} stars",
                                                            modifier = Modifier.padding(start = 4.dp)
                                                        )
                                                    }
                                                }

                                            }

                                            // Ratings Section


                                            // Comments Section
                                            Text(
                                                text = "Comments:",
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(top = 8.dp),
                                                fontSize = 20.sp
                                            )

                                            // Add state for showing full text
                                            var showFullText by remember { mutableStateOf(false) }
                                            val message = ratings?.message ?: "No comments available"
                                            val maxPreviewLength = 100 // Adjust this value as needed

                                            if (message.length > maxPreviewLength) {
                                                Column {
                                                    Text(
                                                        text = if (showFullText) message else "${message.take(maxPreviewLength)}......",
                                                        modifier = Modifier.padding(top = 4.dp),
                                                        fontSize = 16.sp,
                                                        color = if (message.isEmpty()) Color.Gray else Color.Black
                                                    )
                                                    TextButton(
                                                        onClick = { showFullText = !showFullText },
                                                        modifier = Modifier
                                                            .align(Alignment.End)
                                                            .padding(top = 4.dp)
                                                    ) {
                                                        Text(
                                                            text = if (showFullText) "See Less" else "See More",
                                                            color = Color.Blue,
                                                            fontSize = 14.sp
                                                        )
                                                    }
                                                }
                                            } else {
                                                Text(
                                                    text = message,
                                                    modifier = Modifier.padding(top = 4.dp),
                                                    fontSize = 16.sp,
                                                    color = if (message.isEmpty()) Color.Gray else Color.Black
                                                )
                                            }
                                        }
                                    },
                                    confirmButton = {
                                        TextButton(onClick = { showDialog = false }) {
                                            Text("Close", fontSize = 16.sp, color = Color.Black)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

            }
            is ViewRatingsViewModel.ViewRatingsState.Error -> {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp), contentAlignment = Alignment.Center) {
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
            else -> Unit
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
                    modifier = Modifier
                        .size(24.dp)
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
            SnackbarController.show("Logout successful")
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
            logoutViewModel.resetLogoutResult()
            WebSocketManager.disconnect()
        }
    }
    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .fillMaxSize()
        .padding(bottom = 100.dp)) {

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
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(16.dp), // Aligns Row to center-start
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

fun downloadFileTradesman(context: Context, fileUrl: String, fileName: String): Long {
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val uri = Uri.parse(fileUrl)

    val request = DownloadManager.Request(uri).apply {
        setTitle(fileName)
        setDescription("Downloading tradesman credential")
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        setMimeType("application/pdf")
    }

    return downloadManager.enqueue(request)
}

