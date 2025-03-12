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
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Verified
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
import com.example.androidproject.data.preferences.AccountManager
import com.example.androidproject.data.preferences.TokenManager
import com.example.androidproject.view.WindowType

import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.model.client.viewResume
import com.example.androidproject.viewmodel.Tradesman_Profile.UpdateTradesmanActiveStatusViewModel
import com.example.androidproject.viewmodel.Tradesman_Profile.UpdateTradesmanProfileViewModel
import com.example.androidproject.viewmodel.Tradesman_Profile.ViewTradesmanProfileViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ProfileTradesman(
    modifier: Modifier = Modifier,
    navController: NavController,
    logoutViewModel: LogoutViewModel,
    viewTradesmanProfileViewModel: ViewTradesmanProfileViewModel,
    updateTradesmanProfileViewModel : UpdateTradesmanProfileViewModel,
    updateTradesmanActiveStatusViewModel : UpdateTradesmanActiveStatusViewModel,
    LoadingUI :  @Composable () -> Unit, // Add this parameter
    initialTabIndex: Int = 0, // Default to 0 if not provided


) {
    // Function to check network connectivity using NetworkCapabilities (modern approach)
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


    //state of changing profile
    val updateProfileState by updateTradesmanProfileViewModel.updateTradesmanProfileState.collectAsState()

    LaunchedEffect(updateProfileState) {
        when (val updatingProfile = updateProfileState){
            is UpdateTradesmanProfileViewModel.UpdateTradesmanProfileState.Success->{
                updateTradesmanProfileViewModel.resetState()
                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            }
            is UpdateTradesmanProfileViewModel.UpdateTradesmanProfileState.Error ->{
                val errorMessage = updatingProfile.message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()

            }
            else-> Unit
        }
    }

    val updateTradesmanActiveStatusState by updateTradesmanActiveStatusViewModel.updateStatusState.collectAsState()



    // State to trigger refresh/recomposition
    var refreshTrigger by remember { mutableIntStateOf(0) }

    // State to track loading during retry
    var isLoading by remember { mutableStateOf(false) }



    var selectedTabIndex by remember { mutableIntStateOf(initialTabIndex) } // Use initialTabIndex
    val tabNames = listOf("Job Profile", "General")
    val viewTradesmanProfilestate by viewTradesmanProfileViewModel.viewTradesmanProfileResumeState.collectAsState()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it  // Update the local state with the new image URI
            //Here ata iimplement ung viewmodel
            updateTradesmanProfileViewModel.updateTradesmanProfile(selectedImageUri!!,context)

        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val imagesGranted = permissions[Manifest.permission.READ_MEDIA_IMAGES] == true
        val userSelectedGranted = permissions[Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED] == true

        when {
            imagesGranted -> {
                // Full access granted, launch your image picker
                imageLauncher.launch("image/*")
            }
            userSelectedGranted -> {
                // Partial access granted, handle selected media
                imageLauncher.launch("image/*") // System picker will show only selected media
            }
            else -> {
                val shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
                if (shouldShowRationale) {
                    Toast.makeText(context, "Permission is required to select a profile picture", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Permission denied. Enable it in Settings.", Toast.LENGTH_LONG).show()
                    val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:${context.packageName}")
                    context.startActivity(intent)
                }
            }
        }
    }

    // Trigger data fetching only when retry is clicked (not automatically on network change)
    LaunchedEffect(refreshTrigger) {
        if (isConnected.value) {
            isLoading = true // Set loading state before fetching
            delay(200.milliseconds) // Add a 500ms delay to ensure loading UI is visible
            viewTradesmanProfileViewModel.viewTradesmanProfile()
            isLoading = false // Reset loading state after fetching (or handle errors)
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(WindowInsets.systemBars.asPaddingValues())

    ) {

        Row(
            modifier = Modifier
                .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                .fillMaxWidth()
                .height(50.dp),
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
                    contentDescription = "User Account Settings",
                    tint = Color(0xFF3CC0B0),
                    modifier = Modifier.size(32.dp).clickable { navController.navigate("accountsettingstradesman") }
                )
            }
        }
        // Handle different states based on connectivity and data loading
        if (!isConnected.value) {
            // No internet connection
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
                    // Retry button (only fetch data when clicked)
                    Box(
                        modifier = Modifier
                            .clickable {
                                // Re-check network connectivity
                                isConnected.value = checkNetworkConnectivity(connectivityManager)
                                if (isConnected.value) {
                                    // Show loading state and trigger data fetch
                                    isLoading = true
                                    refreshTrigger++
                                } else {
                                    // Optionally show a toast if still no internet
                                    Toast.makeText(context, "Still no internet connection", Toast.LENGTH_SHORT).show()
                                }
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
            if (isLoading){
                LoadingUI()
            }else{
                when (val profileState =viewTradesmanProfilestate){
                    is ViewTradesmanProfileViewModel.ViewTradesmanProfileState.Loading -> {
                        // Show loading indicator for initial or ongoing loading
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            LoadingUI()
                        }
                    }
                    is ViewTradesmanProfileViewModel.ViewTradesmanProfileState.Success -> {
                        // Handle success state
                        val isUpdating = updateTradesmanActiveStatusState is UpdateTradesmanActiveStatusViewModel.UpdateStatusState.Loading
                        val tradesmanDetails =profileState.data
                        var isAvailable by remember { mutableStateOf(tradesmanDetails.isActiveBoolean) } // State to track availability
                        var previousAvailability by remember { mutableStateOf(isAvailable) } // Track previous state for rollback
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
                                        // Tradesman image
                                        AsyncImage(
                                            model = selectedImageUri ?: tradesmanDetails.profilePic,
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
                                                .background(Color.Green)
                                                .align(Alignment.BottomEnd)
                                                .clickable {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // Android 14+
                                                        permissionLauncher.launch(arrayOf(
                                                            Manifest.permission.READ_MEDIA_IMAGES,
                                                            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                                                        ))
                                                    } else {
                                                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                                                            imageLauncher.launch("image/*")
                                                        } else {
                                                            permissionLauncher.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
                                                        }
                                                    }
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Edit Profile Picture",
                                                tint = Color.White,
                                                modifier = Modifier
                                                    .size(20.dp)

                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Row{
                                            Text(
                                                text = tradesmanDetails.tradesmanFullName ?: "N/A",
                                                color = Color.White,
                                                style = TextStyle(
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(
                                                painter = painterResource(id = if (tradesmanDetails.isApprove == 0) R.drawable.unverified_ic else R.drawable.verified_ic),                                                contentDescription = "Profile Verified",
                                                tint = Color.Black,
                                                modifier = Modifier.size(24.dp)

                                            )
                                        }
                                        Text(
                                            text = tradesmanDetails.email ?: "N/A",
                                            color = Color.White,
                                            style = TextStyle(fontSize = 14.sp)
                                        )
                                        Text(
                                            text = tradesmanDetails.phoneNumber ?: "N/A",
                                            color = Color.White,
                                            style = TextStyle(fontSize = 14.sp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .width(100.dp)
                                                .height(30.dp)
                                                .clip(RoundedCornerShape(50.dp))
                                                .background(Color.White)
                                                .clickable (enabled = !isUpdating) { // Disable when loading
                                                    previousAvailability = isAvailable // Store previous state
                                                    isAvailable = !isAvailable // Optimistic update
                                                    updateTradesmanActiveStatusViewModel.updateStatusState(isAvailable)
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
                                                    tint = if (isAvailable) Color.Green else Color.Red, // Change color based on state
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
                                    }
                                }
                            }
                        }
// Handle server response
                        LaunchedEffect(updateTradesmanActiveStatusState) {
                            when (val updatingStatus = updateTradesmanActiveStatusState) {
                                is UpdateTradesmanActiveStatusViewModel.UpdateStatusState.Success -> {
                                    updateTradesmanActiveStatusViewModel.resetState()
                                    viewTradesmanProfileViewModel.viewTradesmanProfile() // Refresh profile
                                    Toast.makeText(context, "Status updated successfully", Toast.LENGTH_SHORT).show()
                                    // isAvailable is already updated optimistically, no need to change unless server disagrees
                                }
                                is UpdateTradesmanActiveStatusViewModel.UpdateStatusState.Error -> {
                                    val errorMessage = updatingStatus.message
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                                else -> Unit
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
                                    0 -> JobProfile(navController, tradesmanDetails)
                                    1 -> SettingsTradesmanScreen(navController, logoutViewModel)
                                }
                            }
                        }
                    }
                    is ViewTradesmanProfileViewModel.ViewTradesmanProfileState.Error -> {
                        // Handle error state
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Error: ${profileState.message}",
                                    color = Color.Red,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                // Retry button (only fetch data when clicked)
                                Box(
                                    modifier = Modifier
                                        .clickable {
                                            // Re-check network connectivity
                                            isConnected.value = checkNetworkConnectivity(connectivityManager)
                                            if (isConnected.value) {
                                                // Show loading state and trigger data fetch
                                                isLoading = true
                                                refreshTrigger++
                                            } else {
                                                // Optionally show a toast if still no internet
                                                Toast.makeText(context, "Still no internet connection", Toast.LENGTH_SHORT).show()
                                            }
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
                    }
                    else -> {
                        // Default case (e.g., initial state or unexpected state)
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            LoadingUI()
                        }
                    }
                }
            }
            // Handle different states including loading, success, error, and initial state

        }
    }
}


// Keep the other composables (JobProfile, GeneralTradesmanSettings, SettingsTradesmanScreen) unchanged unless you need specific adjustments.

@Composable
fun JobProfile(navController: NavController, tradesmanDetails: viewResume) {
    var scale by remember { mutableStateOf(1f) }
    val windowSize = rememberWindowSizeClass()
    val context = LocalContext.current
    var downloadId by remember { mutableStateOf<Long?>(null) }


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
        .verticalScroll(rememberScrollState())) {
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
                            .clickable {  }
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
                            .clickable {  }
                            .background(
                                color = Color(0xFF122826),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .weight(1f)
                            .clickable { navController.navigate("manageprofile") }
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
                            text = "About Me:",
                            fontSize = nameTextSize,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Text(
                        text = displayDetails.aboutMe ?: "N/A",
                        fontSize = taskTextSize,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
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
                    }
                    Text(
                        text = displayDetails.preferredWorkLocation?.let { "$it, Pangasinan" } ?: "N/A",
                        color = Color.Black,
                        fontSize = taskTextSize,
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
                                            Toast.makeText(context, "Download success", Toast.LENGTH_SHORT).show()
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Download failed: ${e.message}", Toast.LENGTH_LONG).show()
                                        }
                                    } else {
                                        Toast.makeText(context, "No file available", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        )

                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.fillMaxWidth().padding(start = 0.dp, end = 0.dp, top = 0.dp, bottom = 50.dp)) {
            Text(text = "Ratings", fontSize = nameTextSize, fontWeight = FontWeight.Bold)
            Text(text = "Feedback from satisfied clients", fontSize = taskTextSize, color = Color.Gray)
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

