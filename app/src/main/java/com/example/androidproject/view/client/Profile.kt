package com.example.androidproject.view.client

import LogoutViewModel
import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.ViewModelSetups
import com.example.androidproject.data.WebSocketManager
import com.example.androidproject.data.preferences.AccountManager
import com.example.androidproject.data.preferences.NotificationSettingManager
import com.example.androidproject.data.preferences.TokenManager
import com.example.androidproject.model.GetJobs
import com.example.androidproject.model.UpdateJob
import com.example.androidproject.view.ServicePosting
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.extras.LoadingUI
import com.example.androidproject.view.extras.SnackbarController
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.view.theme.myGradient4
import com.example.androidproject.viewmodel.client_profile.GetClientProfileViewModel
import com.example.androidproject.viewmodel.client_profile.UpdateClientProfilePictureViewModel
import com.example.androidproject.viewmodel.jobs.DeleteJobViewModel
import com.example.androidproject.viewmodel.jobs.GetMyJobsViewModel
import com.example.androidproject.viewmodel.jobs.PostJobViewModel
import com.example.androidproject.viewmodel.jobs.PutJobViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    logoutViewModel: LogoutViewModel,
    postJobViewModel: PostJobViewModel,
    getMyJobsViewModel: GetMyJobsViewModel,
    getClientProfileViewModel: GetClientProfileViewModel,
    putJobs: PutJobViewModel,
    updateClientProfilePictureViewModel : UpdateClientProfilePictureViewModel,
    deleteJobViewModel: DeleteJobViewModel,
    initialTabIndex: Int = 0
) {
    val poppinsFont = FontFamily(
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_bold, FontWeight.Bold)
    )

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

    // State to track loading during retry
    var showLoading by remember { mutableStateOf(false) }

    val profileState by getClientProfileViewModel.getProfileState.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(initialTabIndex) } // Use initialTabIndex
    val tabNames = listOf("My Posts", "General")

    //state of updating the profile picture
    val updateProfilePictureState by updateClientProfilePictureViewModel.updateClientProfileState.collectAsState()
    val deleteJobState by deleteJobViewModel.deleteJobResult.collectAsState()
    val getMyJobsViewModelState = getMyJobsViewModel.jobsPagingData.collectAsLazyPagingItems()
    val loadState = getMyJobsViewModelState.loadState

    LaunchedEffect(deleteJobState) {
        when (deleteJobState) {
            is DeleteJobViewModel.DeleteJobResult.Success -> {
                getMyJobsViewModelState.refresh()
                deleteJobViewModel.resetState()
                SnackbarController.show("Job deleted successfully")
            }
            is DeleteJobViewModel.DeleteJobResult.Error -> {
                deleteJobViewModel.resetState()
            }
            else -> Unit
        }
    }

    LaunchedEffect(Unit) {
        getMyJobsViewModelState.refresh()
    }

    LaunchedEffect(updateProfilePictureState) {
        when(val updateProf = updateProfilePictureState){
            is UpdateClientProfilePictureViewModel.UpdateClientProfilePictureState.Success -> {
                SnackbarController.show("Profile picture updated successfully")
                getClientProfileViewModel.getClientProfile()
                updateClientProfilePictureViewModel.resetState()

            }
            is UpdateClientProfilePictureViewModel.UpdateClientProfilePictureState.Error -> {
                val error = updateProf.message
                updateClientProfilePictureViewModel.resetState()
                SnackbarController.show("Error: $error")
            }
            else->Unit
        }
    }
    LaunchedEffect(Unit) {
        if (isConnected.value && profileState !is GetClientProfileViewModel.ClientProfileState.Success) {
            getClientProfileViewModel.getClientProfile()
        }
    }


    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Define the image launcher
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            updateClientProfilePictureViewModel.updateClientProfile(selectedImageUri!!, context)
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


    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Row(
                Modifier.fillMaxWidth().height(70.dp).shadow(0.2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 25.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Profile",
                        fontSize = 20.sp,
                        fontFamily = poppinsFont,
                        fontWeight = FontWeight.Medium
                    )

                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications Icon",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { navController.navigate("notification") }
                    )


                }
            }


            when {
                loadState.refresh is LoadState.Loading && getMyJobsViewModelState.itemCount == 0 -> {
                    LoadingUI()
                }

                else -> {
                    // Handle different states based on connectivity and data loading
                    if (!isConnected.value) {
                        if (showLoading) {
                            LoadingUI()
                            LaunchedEffect(Unit) {
                                delay(1500)
                                isConnected.value = checkNetworkConnectivity(connectivityManager)
                                showLoading = false // Hide LoadingUI after delay
                                if (isConnected.value) {
                                    getMyJobsViewModelState.refresh() // Refresh data after reconnecting
                                }
                            }
                        }
                        // No internet connection
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
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
                                            showLoading = true
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
                        when (val state = profileState) {
                            is GetClientProfileViewModel.ClientProfileState.Success -> {
                                val profile = state.data
                                // Profile Info Section
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(10.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(130.dp)
                                                .background(
                                                    myGradient4,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(100.dp)
                                                    .background(
                                                        Color.White,
                                                        RoundedCornerShape(50.dp)
                                                    )
                                            ) {
                                                // Profile Icon
                                                AsyncImage(
                                                    model = selectedImageUri
                                                        ?: profile.profilePicture,
                                                    contentDescription = "Profile Image",
                                                    modifier = Modifier
                                                        .size(100.dp)
                                                        .clip(CircleShape),
                                                    contentScale = ContentScale.Crop
                                                )
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = "Edit Profile Picture",
                                                    tint = Color.Gray,
                                                    modifier = Modifier
                                                        .size(20.dp)
                                                        .align(Alignment.TopEnd)
                                                        .background(Color.White, CircleShape)
                                                        .clickable {
                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // Android 14+
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
                                                                        arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                                                                    )
                                                                }
                                                            }
                                                        }
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(16.dp))
                                            Column {
                                                Row(
                                                    Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        text = profile.fullname,
                                                        color = Color.White,
                                                        fontSize = 18.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Icon(
                                                        imageVector = Icons.Default.Edit,
                                                        contentDescription = "Edit Profile Picture",
                                                        tint = Color.White,
                                                        modifier = Modifier
                                                            .size(26.dp)
                                                            .background(
                                                                Color.Transparent,
                                                                shape = CircleShape
                                                            )
                                                            .clickable {
                                                                navController.navigate("accountsettings")
                                                            }
                                                    )
                                                }

                                                Text(text = profile.email, color = Color.White)
                                                Text(text = profile.address, color = Color.White)
                                                Text(
                                                    text = profile.phoneNumber,
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }

                                    // Tabs and Content
                                    TabRow(
                                        selectedTabIndex = selectedTabIndex,
                                        modifier = Modifier.fillMaxWidth(),
                                        indicator = { tabPositions ->
                                            TabRowDefaults.Indicator(
                                                Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                                color = Color(0xFF3CC0B0)
                                            )
                                        }
                                    ) {
                                        tabNames.forEachIndexed { index, title ->
                                            Tab(
                                                modifier = Modifier.background(Color.White),
                                                selected = selectedTabIndex == index,
                                                onClick = { selectedTabIndex = index },
                                                text = {
                                                    Text(
                                                        title, fontSize = 14.sp,
                                                        color = if (selectedTabIndex == index) Color(
                                                            0xFF3CC0B0
                                                        ) else Color.Black
                                                    )
                                                }
                                            )
                                        }
                                    }

                                    // Content based on selected tab
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(top = 2.dp)
                                    ) {
                                        when (selectedTabIndex) {
                                            0 -> MyPostsTab(
                                                getMyJobsViewModel,
                                                postJobViewModel,
                                                putJobs,
                                                deleteJobViewModel
                                            )

                                            1 -> SettingsScreen(navController, logoutViewModel)
                                        }

                                    }
                                }

                            }

                            is GetClientProfileViewModel.ClientProfileState.Error -> {
                                Text(text = "Error: ${state.message}", color = Color.Red)
                            }

                            else -> Unit
                        }
                    }
                }
            }

        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            SnackbarController.ObserveSnackbar()
        }
    }
}





@Composable
fun MyPostsTab(
    getMyJobsViewModel: GetMyJobsViewModel,
    postJobViewModel: PostJobViewModel,
    putJobs: PutJobViewModel,
    deleteJobViewModel: DeleteJobViewModel
) {
    val jobsList = getMyJobsViewModel.jobsPagingData.collectAsLazyPagingItems()
    val postJobState by postJobViewModel.postJobState.collectAsState()
    val putJobState by putJobs.putJobState.collectAsState()

    // Refresh jobs list when a new post or edit is successful
    LaunchedEffect(postJobState) {
        if (postJobState is PostJobViewModel.PostJobState.Success) {
            jobsList.refresh()
            postJobViewModel.resetState() // Reset to avoid repeated triggers
        }
    }
    LaunchedEffect(putJobState) {
        if (putJobState is PutJobViewModel.PutJobState.Success) {
            jobsList.refresh()
            putJobs.resetState() // Reset to avoid repeated triggers
        }
    }

    LazyColumn(
        Modifier.background(Color(0xFFEDEFEF)).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(jobsList.itemCount) { index ->
            val job = jobsList[index]
            if (job != null) {
                PostsCard(
                    onEditClick = { rate, description, location, deadline ->
                        val update = UpdateJob(rate, description, location, deadline)
                        putJobs.updateJobApplicationStatus(job.id, update)
                    },
                    onApplicantsClick = { /* Handle applicants click */ },
                    job = job,
                    putJobs = putJobs,
                    deleteJobViewModel = deleteJobViewModel,
                    getMyJobsViewModel = getMyJobsViewModel
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FabPosting(
            onDeadlineChange = { deadline -> println("Selected Deadline: $deadline") },
            postJobViewModel = postJobViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PostsCard(
    onEditClick: (Int, String, String, String) -> Unit,
    onApplicantsClick: () -> Unit,
    job: GetJobs, // Renamed to `job` for clarity
    putJobs: PutJobViewModel,
    deleteJobViewModel: DeleteJobViewModel,
    getMyJobsViewModel: GetMyJobsViewModel
) {
    val getMyJobsViewModelState = getMyJobsViewModel.jobsPagingData.collectAsLazyPagingItems()
    var expanded by remember { mutableStateOf(false) }
    val windowSize = rememberWindowSizeClass()

    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 18.sp
        WindowType.MEDIUM -> 20.sp
        WindowType.LARGE -> 22.sp
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
    val poppinsFont = FontFamily(
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_bold, FontWeight.Bold)
    )

    val putJobState by putJobs.putJobState.collectAsState()
    val date = ViewModelSetups.formatDateTime(job.createdAt)
    val deadline = ViewModelSetups.formatDateTime(job.deadline)
    var isDialogVisible by remember { mutableStateOf(false) }
    var updateSubmissionKey by remember { mutableStateOf<Long?>(null) }

    // Local editable states
    var editableJobType by remember { mutableStateOf(job.jobType) }
    var editableDescription by remember { mutableStateOf(job.jobDescription) }
    var editableLocation by remember { mutableStateOf(job.address) }
    var editableDeadline by remember { mutableStateOf(job.deadline) }
    var editableBudget by remember { mutableIntStateOf(job.salary) }

    // Store initial values for reset on cancel
    val initialDescription = remember { job.jobDescription }
    val initialLocation = remember { job.address }
    val initialDeadline = remember { job.deadline }
    val initialBudget = remember { job.salary }

    val context = LocalContext.current
    val today = LocalDate.now()
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val pickedDate = LocalDate.of(year, month + 1, dayOfMonth)
            if (!pickedDate.isBefore(today)) {
                selectedDate = pickedDate
                val formattedDate = pickedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                editableDeadline = formattedDate
            } else {
                SnackbarController.show("You cannot select a past date!")
            }
        },
        today.year,
        today.monthValue - 1,
        today.dayOfMonth
    ).apply {
        datePicker.minDate = System.currentTimeMillis()
    }
    val locationOptions = listOf(
        "Agno", "Aguilar", "Alcala", "Anda", "Asingan", "Balungao", "Bani", "Basista", "Bautista",
        "Bayambang", "Binalonan", "Binmaley", "Bolinao", "Bugallon", "Burgos", "Calasiao",
        "Dagupan City", "Dasol", "Infanta", "Labrador", "Laoac", "Lingayen", "Mabini", "Malasiqui",
        "Manaoag", "Mangaldan", "Mangatarem", "Mapandan", "Natividad", "Pozorrubio", "Rosales",
        "San Fabian", "San Jacinto", "San Manuel", "San Nicolas", "San Quintin", "Santa Barbara",
        "Santa Maria", "Santo Tomas", "Sison", "Sual", "Tayug", "Umingan", "Urbiztondo",
        "Urdaneta City", "Villasis"
    )

    // Update local state only on successful edit
    LaunchedEffect(putJobState, updateSubmissionKey) {
        if (updateSubmissionKey == null) return@LaunchedEffect
        when (val state = putJobState) {
            is PutJobViewModel.PutJobState.Success -> {
                updateSubmissionKey = null
                SnackbarController.show("Job updated successfully")
                putJobs.resetState()
                isDialogVisible = false
            }
            is PutJobViewModel.PutJobState.Error -> {
                updateSubmissionKey = null
                SnackbarController.show(state.message)
                putJobs.resetState()
            }
            else -> {}
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Box(modifier = Modifier.background(color = Color.White)) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Looking for ${job.jobType}",
                        fontSize = nameTextSize,
                        fontWeight = FontWeight.Bold
                    )
                    Box {
                        Icon(
                            painter = painterResource(id = R.drawable.meatball_ic),
                            contentDescription = "Edit Post and Delete",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { expanded = true }
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = Color.Black
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Edit", color = Color.Black)
                                    }
                                },
                                onClick = {
                                    isDialogVisible = true
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color.Red
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Delete", color = Color.Red)
                                    }
                                },
                                onClick = {
                                    deleteJobViewModel.deleteJob(job.id)
                                    getMyJobsViewModelState.refresh()
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Text(
                    text = editableDescription,
                    fontSize = taskTextSize,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Job Address:", fontWeight = FontWeight.Medium, color = Color.Gray, fontSize = taskTextSize)
                    Text(text = editableLocation, fontSize = taskTextSize, fontWeight = FontWeight.Medium, color = Color.Black)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Budget:",
                        fontSize = taskTextSize,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "₱ ${editableBudget.toInt()} ",
                        fontSize = taskTextSize,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Service Type:",
                        fontSize = taskTextSize,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                    Text(
                        text = "${job.jobType}",
                        fontSize = taskTextSize,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
                Text(text = "Job Deadline: $deadline", fontSize = taskTextSize, color = Color.Red, fontWeight = FontWeight.Medium,
                )

                Text(
                    text = "Applicants: ${job.totalApplicants}",
                    fontSize = taskTextSize,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onApplicantsClick() }
                )

                Text(
                    text = "Posted on $date - ${job.status}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }

    // Pop-up dialog for editing posting
    if (isDialogVisible) {
        Dialog(onDismissRequest = {
            editableDescription = initialDescription
            editableLocation = initialLocation
            editableDeadline = initialDeadline
            editableBudget = initialBudget
            isDialogVisible = false
        }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .verticalScroll(rememberScrollState()),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = "Edit Post",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "Update the details of your service need",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    OutlinedTextField(
                        value = job.jobType,
                        onValueChange = { /* No action, read-only */ },
                        label = { Text("Job Type") },
                        enabled = false,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Gray,
                            unfocusedIndicatorColor = Color.Gray,
                            focusedLabelColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Gray,
                            disabledContainerColor = Color.Transparent,
                            disabledTextColor = Color.Black
                        )
                    )

                    DropDown(
                        label = "Location",
                        options = locationOptions,
                        selectedOption = editableLocation,
                        onOptionSelected = { editableLocation = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editableBudget.toString(),
                        onValueChange = { newValue ->
                            editableBudget = newValue.toIntOrNull() ?: 0
                        },
                        label = { Text("Estimated Budget") },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Blue,
                            unfocusedIndicatorColor = Color.Gray,
                            focusedLabelColor = Color.Blue,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.Black
                        )
                    )

                    Button(
                        onClick = { datePickerDialog.show() },
                        modifier = Modifier
                            .width(360.dp)
                            .heightIn(min = 56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color.Gray)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().offset(x = (-10).dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Calendar Icon",
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (editableDeadline.isNotEmpty()) editableDeadline else "Select Deadline",
                                fontSize = 16.sp,
                                color = if (editableDeadline.isNotEmpty()) Color.Black else Color.Gray
                            )
                        }
                    }

                    OutlinedTextField(
                        value = editableDescription,
                        onValueChange = { editableDescription = it },
                        label = { Text("Description") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Blue,
                            unfocusedIndicatorColor = Color.Gray,
                            focusedLabelColor = Color.Blue,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.Black
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                editableDescription = initialDescription
                                editableLocation = initialLocation
                                editableDeadline = initialDeadline
                                editableBudget = initialBudget
                                isDialogVisible = false
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xFF3CC0B0))
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                updateSubmissionKey = System.currentTimeMillis()
                                onEditClick(
                                    editableBudget,
                                    editableDescription,
                                    editableLocation,
                                    editableDeadline
                                )
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xFF3CC0B0))
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GeneralSettings(
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
fun SettingsScreen(navController: NavController, logoutViewModel: LogoutViewModel) {
    val logoutResult by logoutViewModel.logoutResult.collectAsState()
    val context = LocalContext.current
    var isChecked by remember { mutableStateOf(NotificationSettingManager.getNotification()) }
    LaunchedEffect(logoutResult) {
        logoutResult?.let {
            // Clear tokens and navigate regardless of result
            TokenManager.clearToken()
            AccountManager.clearAccountData()
            NotificationSettingManager.clearNotificationData()
            SnackbarController.show("Logout successful")
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
            logoutViewModel.resetLogoutResult()
            WebSocketManager.disconnect()
        }
    }
    Column (modifier = Modifier.verticalScroll(rememberScrollState())){

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
                        imageVector =Icons.Outlined.Notifications,
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
                    onCheckedChange = { isChecked = it
                        NotificationSettingManager.saveNotification(isChecked)
                        if (isChecked) {
                            SnackbarController.show("Notification turned on")
                        } else {
                            SnackbarController.show("Notification turned off")
                        }
                                      },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF3CC0B0),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.Gray
                    )
                )
            }
        }
        GeneralSettings(
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
        GeneralSettings(
            icon = ImageVector.vectorResource(id = R.drawable.ic_about),
            title = "About Us",
            description = "Know more about our team.",
            trailingIcon = Icons.Default.ArrowForwardIos,
            onClick = { navController.navigate("aboutus") }
        )
        GeneralSettings(
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
                .padding(bottom = 5.dp)// Added padding to avoid edge clipping
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FabPosting(
    onDeadlineChange: (String) -> Unit,
    postJobViewModel: PostJobViewModel
) {
    val postJobState by postJobViewModel.postJobState.collectAsState()
    var isSubmitting by remember { mutableStateOf(false) }
    var isDialogVisible by remember { mutableStateOf(false) }
    var applicantCount by remember { mutableStateOf("Select applicant count") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Select location") }
    var rate by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Select job category") }
    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val context = LocalContext.current
    val today = LocalDate.now()
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var deadline by remember { mutableStateOf("") }
    var jobType by remember { mutableStateOf("") }

    val applicantOptions = (1..5).map { it.toString() }
    val locationOptions = listOf(
        "Agno", "Aguilar", "Alcala", "Anda", "Asingan", "Balungao", "Bani", "Basista", "Bautista",
        "Bayambang", "Binalonan", "Binmaley", "Bolinao", "Bugallon", "Burgos", "Calasiao",
        "Dagupan City", "Dasol", "Infanta", "Labrador", "Laoac", "Lingayen", "Mabini", "Malasiqui",
        "Manaoag", "Mangaldan", "Mangatarem", "Mapandan", "Natividad", "Pozorrubio", "Rosales",
        "San Fabian", "San Jacinto", "San Manuel", "San Nicolas", "San Quintin", "Santa Barbara",
        "Santa Maria", "Santo Tomas", "Sison", "Sual", "Tayug", "Umingan", "Urbiztondo",
        "Urdaneta City", "Villasis"
    )
    val categoryOptions = listOf(
        "Carpenter", "Painter", "Welder", "Electrician", "Plumber",
        "Mason", "Roofer", "AC Technician", "Mechanic", "Cleaner"
    )

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val pickedDate = LocalDate.of(year, month + 1, dayOfMonth)
            if (!pickedDate.isBefore(today)) {
                selectedDate = pickedDate
                val formattedDate = pickedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                deadline = formattedDate
                onDeadlineChange(formattedDate)
            } else {
                SnackbarController.show("You cannot select a past date!")
            }
        },
        today.year,
        today.monthValue - 1,
        today.dayOfMonth
    ).apply {
        datePicker.minDate = System.currentTimeMillis()
    }

    LaunchedEffect(postJobState) {
        when (postJobState) {
            is PostJobViewModel.PostJobState.Success -> {
                val message = postJobState as PostJobViewModel.PostJobState.Success
                SnackbarController.show(message.message.message)
                postJobViewModel.resetState()
            }
            is PostJobViewModel.PostJobState.Error -> {
                val message = postJobState as PostJobViewModel.PostJobState.Error
                postJobViewModel.resetState()
                SnackbarController.show(message.message)
            }
            is PostJobViewModel.PostJobState.Loading -> {}
            else -> Unit
        }
    }

    FloatingActionButton(
        onClick = {
            applicantCount = "Select applicant count"
            description = ""
            location = "Select location"
            rate = ""
            selectedCategory = "Select job category"
            selectedDate = null
            deadline = ""
            jobType = ""
            isDialogVisible = true
        },
        containerColor = Color.Gray,
        contentColor = Color.White,
        shape = CircleShape
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Icon")
    }

    if (isDialogVisible) {
        Dialog(onDismissRequest = { isDialogVisible = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .verticalScroll(rememberScrollState()),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = "Create New Post",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "Provide details of your new service",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    // Applicants Count Dropdown
                    DropDown(
                        label = "Applicants Count",
                        options = applicantOptions,
                        selectedOption = applicantCount,
                        onOptionSelected = { applicantCount = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Description TextField
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 50.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Blue,
                            unfocusedIndicatorColor = Color.Gray,
                            focusedLabelColor = Color.Blue,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.Black
                        )
                    )

                    // Location Dropdown
                    DropDown(
                        label = "Location",
                        options = locationOptions,
                        selectedOption = location,
                        onOptionSelected = { location = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Estimated Budget TextField
                    OutlinedTextField(
                        value = rate,
                        onValueChange = { rate = it },
                        label = { Text("Estimated Budget") },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 50.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Blue,
                            unfocusedIndicatorColor = Color.Gray,
                            focusedLabelColor = Color.Blue,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.Black
                        )
                    )

                    // Deadline Button
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = { datePickerDialog.show() },
                        modifier = Modifier
                            .width(360.dp)
                            .heightIn(min = 56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color.Gray)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().offset(x = (-10).dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Calendar Icon",
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (deadline.isNotEmpty()) deadline else "Select Deadline",
                                fontSize = 16.sp,
                                color = if (deadline.isNotEmpty()) Color.Black else Color.Gray                            )
                        }
                    }

                    // Category Dropdown
                    DropDown(
                        label = "Service Category",
                        options = categoryOptions,
                        selectedOption = selectedCategory,
                        onOptionSelected = {
                            selectedCategory = it
                            jobType = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { isDialogVisible = false },
                            colors = ButtonDefaults.buttonColors(Color(0xFF3CC0B0))
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                isSubmitting = true

                                // Validate empty fields or placeholder values
                                if (rate.isEmpty() ||
                                    applicantCount == "Select applicant count" ||
                                    selectedCategory == "Select job category" ||
                                    description.isEmpty() ||
                                    location == "Select location" ||
                                    deadline.isEmpty()
                                ) {
                                    SnackbarController.show("Please fill in all fields.")
                                    isSubmitting = false
                                    return@Button
                                }

                                val rateInt = rate.toDoubleOrNull()
                                val applicantCountInt = applicantCount.toIntOrNull()

                                if (applicantCountInt == null) {
                                    SnackbarController.show("Invalid Applicant Count!")
                                    isSubmitting = false
                                    return@Button
                                }

                                if (rateInt == null) {
                                    SnackbarController.show("Invalid Budget!")
                                    isSubmitting = false
                                    return@Button
                                }

                                val finalJobType = if (jobType == "AC Technician") "Ac_technician" else jobType

                                postJobViewModel.postJob(
                                    salary = rateInt,
                                    applicantLimitCount = applicantCountInt,
                                    jobType = finalJobType,
                                    jobDescription = description,
                                    location = location,
                                    status = "available",
                                    deadline = deadline
                                )

                                isDialogVisible = false
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xFF3CC0B0))
                        ) {
                            Text("Post")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors()
) {
    var expanded by remember { mutableStateOf(false) }

    val isPlaceholder = selectedOption == "Select job category" ||
            selectedOption == "Select location" ||
            selectedOption == "Select applicant count"
    val textColor = if (isPlaceholder) Color.Gray else Color.Black

    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            textStyle = TextStyle(color = textColor),
            trailingIcon = {
                Icon(
                    Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown",
                    modifier = Modifier.clickable { expanded = true }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.Transparent,
                focusedBorderColor = Color.LightGray,
                unfocusedBorderColor = Color.Gray,
            ),
            shape = RoundedCornerShape(12.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(310.dp).background(Color.White)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}