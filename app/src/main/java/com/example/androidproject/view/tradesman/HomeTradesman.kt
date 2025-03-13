package com.example.androidproject.view.tradesman

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.ViewModelSetups
import com.example.androidproject.model.GetJobs
import com.example.androidproject.view.WindowSize
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.client.UploadFieldScreenShot
import com.example.androidproject.view.client.openScreenShot
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.viewmodel.jobs.GetJobsViewModel
import com.example.androidproject.viewmodel.jobs.GetRecentJobsViewModel
import com.example.androidproject.viewmodel.report.ReportClientViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun HomeTradesman( modifier: Modifier, navController: NavController, getJobsViewModel: GetJobsViewModel, getRecentJobsViewModel: GetRecentJobsViewModel,reportClientViewModel: ReportClientViewModel,LoadingUI : @Composable () -> Unit,initialTabIndex: Int = 0,){

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


    // State to trigger refresh/recomposition
    var refreshTrigger by remember { mutableIntStateOf(0) }




    var isLoading by remember { mutableStateOf(false) }
    val windowSize = rememberWindowSizeClass()
    val textSize = when (windowSize.width) {
        WindowType.SMALL -> 14.sp
        WindowType.MEDIUM -> 16.sp
        WindowType.LARGE -> 18.sp
    }
    var selectedTabIndex by remember { mutableIntStateOf(initialTabIndex) }

    // Trigger data fetching only when retry is clicked (not automatically on network change)
    LaunchedEffect(refreshTrigger) {
        if (isConnected.value) {
            isLoading = true // Set loading state before fetching
            delay(200.milliseconds) // Add a 500ms delay to ensure loading UI is visible
            isLoading = false // Reset loading state after fetching (or handle errors)
        }
    }
    // Tab titles
    val tabTitles = listOf("Top Matches", "Recent Posted Jobs")
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
            .background(Color.White)
    ) {
        Column(modifier = Modifier .fillMaxWidth()
            .wrapContentSize()
            .background(Color.White)) {

            // Provide navController to the SearchField
            TopSectionHomeTradesman(navController,windowSize )
            Box (
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    Column(modifier = Modifier.fillMaxSize()) {
                        // Tabs (Fixed Choices)
                        TabRow(
                            selectedTabIndex = selectedTabIndex,
                            modifier = Modifier.fillMaxWidth(), // Ensures the TabRow fills the width
                            containerColor = Color.White // Background for the TabRow
                        ) {
                            tabTitles.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTabIndex == index,
                                    onClick = { selectedTabIndex = index },
                                    text = {
                                        Text(
                                            text = title,
                                            fontSize = textSize,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.fillMaxWidth().padding(4.dp), // Fills the width inside each Tab
                                            color = if (selectedTabIndex == index) Color.Black else Color.Gray
                                        )
                                    },
                                    modifier = Modifier.weight(1f) // Ensures equal distribution across the width
                                )
                            }
                        }
                        // Content changes based on the selected tab
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
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0xFFD9D9D9))
                                ) {
                                    when (selectedTabIndex) {
                                        0 -> TopMatches(navController, getJobsViewModel,reportClientViewModel,LoadingUI)
                                        1 -> RecentJobs(navController, getRecentJobsViewModel,reportClientViewModel)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun TopSectionHomeTradesman(navController: NavController, windowSize: WindowSize) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp)
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier //top nav
                .padding(top = 8.dp, start = 25.dp, end = 25.dp, bottom = 8.dp)
                .fillMaxWidth()
                .height(50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left-aligned text
            Text(
                text = "Home",
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
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
            }
        }
    }
    Divider(
        color = Color.Black,
        thickness = 0.3.dp,
        modifier = Modifier.fillMaxWidth()
    )
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TopMatches(navController: NavController, getJobsViewModel: GetJobsViewModel,reportClientViewModel: ReportClientViewModel,LoadingUI : @Composable () -> Unit) {
    val jobsList = getJobsViewModel.jobsPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        getJobsViewModel.refreshJobs()
    }
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFE9E9E9))) {
        LazyColumn(
            modifier = Modifier.padding(bottom = 80.dp, top = 2.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(jobsList.itemCount) { index ->
                val job = jobsList[index]
                if (job != null) {
                    TopMatchesItem(job, navController,reportClientViewModel)
                }
            }
            item {
                if (jobsList.loadState.append == LoadState.Loading) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        LoadingUI()
                    }
                }
            }
        }
    }
}


@Composable
fun TopMatchesItem(getJobs: GetJobs, navController: NavController,reportClientViewModel: ReportClientViewModel) {
    val reportClientState by reportClientViewModel.reportClientState.collectAsState()
    val getJobsDate = ViewModelSetups.formatDateTime(getJobs.createdAt)
    val windowSize = rememberWindowSizeClass()
    val context = LocalContext.current
    val iconSize = when (windowSize.width) {
        WindowType.SMALL -> 24.dp
        WindowType.MEDIUM -> 32.dp
        WindowType.LARGE -> 40.dp
    }


    var reportDocument by remember { mutableStateOf<Uri?>(null) }
    var showMenu by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(-1) }
    var otherReason by remember { mutableStateOf("") }
    var reasonDescription by remember { mutableStateOf("") }
    var showReportDialog by remember { mutableStateOf(false) }
    val reasons = listOf(
        "Abusive or Harassing Behavior",
        "Inappropriate Content or Language",
        "Fraudulent Activity or Scam",
        "Poor Quality of Service",
        "Unprofessional Conduct",
        "Safety Concerns",
        "Others"
    )



    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { reportDocument = it } }
    var jobType = getJobs.jobType

    if (jobType == "Ac_technician") {
        jobType = "AC Technician"
    }


    LaunchedEffect(reportClientState) {
        when (val reportClient = reportClientState){
            is ReportClientViewModel.ReportClientState.Loading->{
                //nothing
            }
            is ReportClientViewModel.ReportClientState.Success->{
                val responseReport = reportClient.data?.message
                Toast.makeText(context, responseReport, Toast.LENGTH_SHORT).show()
                showReportDialog = false
                reportClientViewModel.resetState()
            }
            is ReportClientViewModel.ReportClientState.Error->{
                val error = reportClient.message
                Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
                 showReportDialog = true
                reportClientViewModel.resetState()
            }
            else -> Unit
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("tradesmanapply/${getJobs.id}")
            },
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            Row {
                AsyncImage(
                    model = getJobs.clientProfilePicture, // Use URL here
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = getJobs.clientFullname,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight(500),
                        modifier = Modifier.padding(start = 20.dp)
                    )
                    Box {
                        Icon(
                            painter = painterResource(id = R.drawable.meatball_ic),
                            contentDescription = "Menu Icon",
                            modifier = Modifier
                                .size(iconSize)
                                .clickable { showMenu = true }
                        )

                                // Popup Menu
                                DropdownMenu(
                                    expanded = showMenu,
                                    onDismissRequest = { showMenu = false },
                                    modifier = Modifier.background(Color.White)
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Report", textAlign = TextAlign.Center) },
                                        onClick = {
                                            showMenu = false
                                            showReportDialog = true
                                        }
                                    )
                                }
                            }
                        }

                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Column {
                            Text(
                                text = "Looking for $jobType",
                                fontSize = 24.sp,
                                color = Color.Black,
                                fontWeight = FontWeight(500)
                            )
                            Text(text = "Posted on $getJobsDate - ${getJobs.status} ")
                        }
                        Spacer(modifier = Modifier.weight(1f))

            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color.Gray),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp)) {
                    Text(text = getJobs.jobDescription, fontSize = 14.sp)
                    Text(text = "Est. Budget: ₱ ${getJobs.salary}", fontSize = 14.sp)
                    Text(text = "Location: ${getJobs.address}, Pangasinan", fontSize = 14.sp)
                }
                Row(modifier = Modifier.padding(start = 5.dp)) {
                    TextButton(onClick = {}) {
                        Text(
                            text = "${getJobs.totalApplicants} Applicant",
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            style = TextStyle(textDecoration = TextDecoration.Underline),
                        )
                    }
                }
            }
        }
    }
    if (showReportDialog) {
        Dialog(onDismissRequest = { showReportDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                ,
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .border(2.dp, Color(0xFFB5B5B5), shape = RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)), // Dark background for contrast
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Reason for Report",
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                                Column(modifier = Modifier.padding(top = 16.dp)) {
                                    reasons.forEachIndexed { index, reason ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp)
                                        ) {
                                            Checkbox(
                                                checked = selectedIndex == index,
                                                onCheckedChange = {
                                                    selectedIndex = if (selectedIndex == index) -1 else index
                                                },
                                                colors = CheckboxDefaults.colors(
                                                    uncheckedColor = Color.Black,
                                                    checkedColor = Color(0xFF42C2AE)
                                                )
                                            )

                                            if (reason == "Others") {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = reason,
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        color = Color.Black
                                                    )

                                                    if (selectedIndex == index) {
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        TextField(
                                                            value = otherReason,
                                                            onValueChange = { otherReason = it },
                                                            placeholder = { Text("Enter other reason") },
                                                            singleLine = true,
                                                            modifier = Modifier
                                                                .weight(1f) // Pushes the field to the right
                                                                .heightIn(min = 40.dp),
                                                            colors = TextFieldDefaults.colors(
                                                                focusedContainerColor = Color.Transparent,
                                                                unfocusedContainerColor = Color.Transparent,
                                                                focusedIndicatorColor = Color.Blue,
                                                                unfocusedIndicatorColor = Color.Gray,
                                                                cursorColor = Color.Black
                                                            )
                                                        )
                                                    }
                                                }
                                            } else {
                                                Text(
                                                    text = reason,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = Color.Black,
                                                    modifier = Modifier.padding(start = 8.dp)
                                                )
                                            }
                                        }
                                    }
                                    UploadFieldScreenShot(
                                        label = "Screenshot",
                                        uri = reportDocument,
                                        fileType = "image",
                                        onUploadClick = {
                                            documentPickerLauncher.launch("image/*")
                                        },
                                        onViewClick = {
                                            reportDocument?.let { uri ->
                                                openScreenShot(context, uri)
                                            }
                                        }
                                    )

                                    OutlinedTextField(
                                        value = reasonDescription,
                                        onValueChange = { reasonDescription = it },
                                        placeholder = { Text("Enter Your Explanation") },
                                        shape = RoundedCornerShape(16.dp),
                                        maxLines = 3,

                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(min = 100.dp),
                                        colors = TextFieldDefaults.colors(
                                            focusedContainerColor = Color.Transparent,
                                            unfocusedContainerColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Blue,
                                            unfocusedIndicatorColor = Color.Gray,
                                            focusedLabelColor = Color.Blue,
                                            unfocusedLabelColor = Color.Gray,
                                            cursorColor = Color.Black
                                        )
                                    )

                                }


                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Button(
                                        onClick = { showReportDialog = false },
                                        modifier = Modifier.size(110.dp, 45.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(
                                                0xFF42C2AE
                                            )
                                        )
                                    ) {
                                        Text("Cancel", color = Color.White)
                                    }
                                    Button(
                                        onClick = {
                                            if (selectedIndex == -1) {
                                                // Show a message to the user indicating that they need to select a reason
                                                Toast.makeText(context, "Please select a reason for reporting", Toast.LENGTH_SHORT).show()
                                            } else {
                                                val selectedReason = if (selectedIndex == reasons.size - 1) {
                                                    // If "Others" is selected, use the value from the otherReason field
                                                    otherReason
                                                } else {
                                                    // Otherwise, use the selected reason from the list
                                                    reasons[selectedIndex]
                                                }
                                                reportClientViewModel.reportClient(getJobs.userId,selectedReason,reasonDescription,reportDocument!!,context)
                                            }

                                        },
                                        modifier = Modifier.size(110.dp, 45.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(
                                                0xFF42C2AE
                                            )
                                        )
                                    ) {
                                        Text("Submit", color = Color.White)
                                    }

                                }
                            }

                        }


                    }
                }
            }
        }






@Composable
fun RecentJobs(navController: NavController, getRecentJobsViewModel: GetRecentJobsViewModel,reportClientViewModel: ReportClientViewModel){
    val jobList = getRecentJobsViewModel.jobsPagingData.collectAsLazyPagingItems()
    LazyColumn(
        modifier = Modifier.padding(bottom = 80.dp, top = 2.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),

    ) {
        items(jobList.itemCount) { index ->
            val job = jobList[index]
            if (job != null) {
                RecentJobsItem(job, navController,reportClientViewModel)
            }
        }
    }
}

@Composable
fun RecentJobsItem(getJobs: GetJobs, navController: NavController,reportClientViewModel: ReportClientViewModel){
    val reportClientState by reportClientViewModel.reportClientState.collectAsState()
    val getJobsDate = ViewModelSetups.formatDateTime(getJobs.createdAt)
    val windowSize = rememberWindowSizeClass()
    val context =LocalContext.current
    val iconSize = when (windowSize.width) {
        WindowType.SMALL -> 25.dp
        WindowType.MEDIUM -> 35.dp
        WindowType.LARGE -> 45.dp
    }
    var reportDocument by remember { mutableStateOf<Uri?>(null) }
    var showMenu by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(-1) }
    var otherReason by remember { mutableStateOf("") }
    var reasonDescription by remember { mutableStateOf("") }
    var showReportDialog by remember { mutableStateOf(false) }
    val reasons = listOf(
        "Abusive or Harassing Behavior",
        "Inappropriate Content or Language",
        "Fraudulent Activity or Scam",
        "Poor Quality of Service",
        "Unprofessional Conduct",
        "Safety Concerns",
        "Others"
    )
    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { reportDocument = it } }
    var jobType = getJobs.jobType

    if (jobType == "Ac_technician") {
        jobType = "AC Technician"
    }

    LaunchedEffect(reportClientState) {
        when(val reportClient = reportClientState){
            is ReportClientViewModel.ReportClientState.Loading ->{
                //nothing
            }
            is ReportClientViewModel.ReportClientState.Success ->{
                val responseReport = reportClient.data?.message
                Toast.makeText(context, responseReport, Toast.LENGTH_SHORT).show()
                showReportDialog = false
                reportClientViewModel.resetState()
            }
            is ReportClientViewModel.ReportClientState.Error ->{
             val error = reportClient.message
             Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("tradesmanapply/${getJobs.id}")
            },
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            Row {
                AsyncImage(
                    model = getJobs.clientProfilePicture, // Use URL here
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = getJobs.clientFullname,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight(500),
                        modifier = Modifier.padding(start = 20.dp)
                    )
                    Box {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Menu Icon",
                            modifier = Modifier
                                .size(iconSize)
                                .clickable { showMenu = true }
                        )

                        // Popup Menu
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Report", textAlign = TextAlign.Center) },
                                onClick = {
                                    showMenu = false
                                    showReportDialog = true
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Column {
                    Text(text = "Looking for $jobType", fontSize = 24.sp, color = Color.Black, fontWeight = FontWeight(500))
                    Text(text = "Posted on $getJobsDate - ${getJobs.status} ")

                }
                Spacer(modifier = Modifier.weight(1f))

            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color.Gray),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = getJobs.jobDescription, fontSize = 12.sp)
                    Text(text = "Est. Budget: P${getJobs.salary}", fontSize = 12.sp)
                    Text(text = "Location: ${getJobs.address}", fontSize = 16.sp)


                }
            }
        }
    }
    if (showReportDialog) {
        Dialog(onDismissRequest = { showReportDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .border(2.dp, Color(0xFFB5B5B5), shape = RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)), // Dark background for contrast
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Reason for Report",
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        Column(modifier = Modifier.padding(top = 16.dp)) {
                            reasons.forEachIndexed { index, reason ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Checkbox(
                                        checked = selectedIndex == index,
                                        onCheckedChange = {
                                            selectedIndex =
                                                if (selectedIndex == index) -1 else index
                                        },
                                        colors = CheckboxDefaults.colors(
                                            uncheckedColor = Color.Black,
                                            checkedColor = Color(0xFF42C2AE)
                                        )
                                    )

                                    if (reason == "Others") {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = reason,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color.Black
                                            )

                                            if (selectedIndex == index) {
                                                Spacer(modifier = Modifier.width(8.dp))
                                                TextField(
                                                    value = otherReason,
                                                    onValueChange = { otherReason = it },
                                                    placeholder = { Text("Enter other reason") },
                                                    singleLine = true,
                                                    modifier = Modifier
                                                        .weight(1f) // Pushes the field to the right
                                                        .heightIn(min = 40.dp),
                                                    colors = TextFieldDefaults.colors(
                                                        focusedContainerColor = Color.Transparent,
                                                        unfocusedContainerColor = Color.Transparent,
                                                        focusedIndicatorColor = Color.Blue,
                                                        unfocusedIndicatorColor = Color.Gray,
                                                        cursorColor = Color.Black
                                                    )
                                                )
                                            }
                                        }
                                    } else {
                                        Text(
                                            text = reason,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.Black,
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                }
                            }
                            UploadFieldScreenShot(
                                label = "reportDocument",
                                uri = reportDocument,
                                fileType = "image",
                                onUploadClick = {
                                    documentPickerLauncher.launch("image/*")
                                },
                                onViewClick = {
                                    reportDocument?.let { uri ->
                                        openScreenShot(context, uri)
                                    }
                                }
                            )

                            OutlinedTextField(
                                value = reasonDescription,
                                onValueChange = { reasonDescription = it },
                                placeholder = { Text("Enter Your Explanation") },
                                shape = RoundedCornerShape(16.dp),
                                maxLines = 3,

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 100.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Blue,
                                    unfocusedIndicatorColor = Color.Gray,
                                    focusedLabelColor = Color.Blue,
                                    unfocusedLabelColor = Color.Gray,
                                    cursorColor = Color.Black
                                )
                            )

                        }


                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { showReportDialog = false },
                                modifier = Modifier.size(110.dp, 45.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF42C2AE
                                    )
                                )
                            ) {
                                Text("Cancel", color = Color.White)
                            }
                            Button(
                                onClick = {
                                    if (selectedIndex == -1) {
                                        // Show a message to the user indicating that they need to select a reason
                                        Toast.makeText(context, "Please select a reason for reporting", Toast.LENGTH_SHORT).show()
                                    } else {
                                        val selectedReason = if (selectedIndex == reasons.size - 1) {
                                            // If "Others" is selected, use the value from the otherReason field
                                            otherReason
                                        } else {
                                            // Otherwise, use the selected reason from the list
                                            reasons[selectedIndex]
                                        }
                                        reportClientViewModel.reportClient(getJobs.userId,selectedReason,reasonDescription,reportDocument!!,context)
                                    }

                                },
                                modifier = Modifier.size(110.dp, 45.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF42C2AE
                                    )
                                )
                            ) {
                                Text("Submit", color = Color.White)
                            }

                        }
                    }

                }
            }
        }
    }
}