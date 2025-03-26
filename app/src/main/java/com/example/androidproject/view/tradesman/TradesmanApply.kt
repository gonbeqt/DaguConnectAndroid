package com.example.androidproject.view.tradesman

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.PhoneIphone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.example.androidproject.LoadingUI
import com.example.androidproject.ViewModelSetups
import com.example.androidproject.model.GetJobs
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.viewmodel.jobs.GetClientPostedJobsViewModel
import com.example.androidproject.viewmodel.jobs.ViewJobViewModel
import coil.compose.AsyncImage
import com.example.androidproject.view.theme.myGradient3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradesmanApply(
    jobId: String,
    navController: NavController,
    viewModel: ViewJobViewModel,
    getClientPostedJobsViewModel: GetClientPostedJobsViewModel
) {
    val clientPostedJobState by getClientPostedJobsViewModel.jobState.collectAsState()
    val viewJobState by viewModel.jobState.collectAsState()

    val windowSize = rememberWindowSizeClass()
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp

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

    val jobID = jobId.toIntOrNull() ?: return

    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        confirmValueChange = { it != SheetValue.Hidden }
    )
    val scaffoldState = BottomSheetScaffoldState(
        bottomSheetState = bottomSheetState,
        snackbarHostState = remember { SnackbarHostState() }
    )

    LaunchedEffect(Unit) {
        viewModel.getJobById(jobID)
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (scaffoldRef, buttonRef) = createRefs()

        BottomSheetScaffold(
            modifier = Modifier.constrainAs(scaffoldRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(buttonRef.top)
                height = Dimension.fillToConstraints
            },
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            scaffoldState = scaffoldState,
            sheetContainerColor = Color.White,
            sheetPeekHeight = screenHeightDp * 0.90f,
            sheetContent = {
                when (viewJobState) {
                    is ViewJobViewModel.JobState.Loading -> {
                        // Loading handled outside scaffold
                    }
                    is ViewJobViewModel.JobState.Success -> {
                        val job = (viewJobState as ViewJobViewModel.JobState.Success).data
                        val date = ViewModelSetups.formatDateTime(job.job.createdAt)
                        val deadline = ViewModelSetups.formatDateTime(job.job.deadline)
                        val clientId = job.job.userId

                        LaunchedEffect(clientId) {
                            if (clientId != null) {
                                getClientPostedJobsViewModel.getClientPostedJobs(clientId)
                            }
                        }

                        var jobType = job.job.jobType
                        if (jobType == "Ac_technician") {
                            jobType = "AC Technician"
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                                .padding(12.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            // Client Info Section
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 0.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = job.job.clientProfile,
                                    contentDescription = "Profile Image",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 15.dp)
                                ) {
                                    Text(
                                        text = job.job.clientFullname.orEmpty(),
                                        color = Color.Black,
                                        fontWeight = FontWeight(500),
                                        fontSize = 20.sp,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Outlined.PhoneIphone,
                                            contentDescription = "Phone Number",
                                            tint = Color(0xFF3CC0B0),
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.size(4.dp))
                                        Text(
                                            text = "099384773839", // Replace with dynamic data if available
                                            fontSize = taskTextSize,
                                            fontWeight = FontWeight.Normal
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = "Location",
                                            tint = Color(0xFF3CC0B0),
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.size(4.dp))
                                        Text(
                                            text = "${job.job.address}, Pangasinan",
                                            fontSize = taskTextSize,
                                            fontWeight = FontWeight.Normal
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(16.dp))
                            Divider(
                                modifier = Modifier.padding(horizontal = 10.dp),
                                color = Color.Gray,
                                thickness = 0.3.dp
                            )
                            Spacer(Modifier.height(8.dp))

                            // Job Title and Posting Info
                            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Looking for $jobType",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Text(text = "Posted on $date - ${job.job.status}")
                                Text(text = "Job Date: $deadline")
                                Text(text = "Applicants(0)") // Replace with dynamic count if available

                                Spacer(Modifier.height(16.dp))
                                Divider(color = Color.Gray, thickness = 0.3.dp)
                                Spacer(Modifier.height(16.dp))

                                // Job Description
                                Text(
                                    text = "Job Description",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = job.job.jobDescription,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )

                                Spacer(Modifier.height(16.dp))
                                Divider(color = Color.Gray, thickness = 0.3.dp)
                                Spacer(Modifier.height(16.dp))

                                // Location and Salary
                                Text(
                                    text = "Location and Salary",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Column(modifier = Modifier.padding(vertical = 10.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = "Preferred Location",
                                            tint = Color(0xFF3CC0B0),
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.size(4.dp))
                                        Text(
                                            text = "Preferred Location",
                                            fontSize = taskTextSize,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    Text(
                                        text = "${job.job.address}, Pangasinan",
                                        fontSize = taskTextSize,
                                        fontWeight = FontWeight.Normal,
                                        modifier = Modifier.padding(start = 28.dp)
                                    )
                                }
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Outlined.Money,
                                            contentDescription = "Estimated Budget",
                                            tint = Color(0xFF3CC0B0),
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.size(4.dp))
                                        Text(
                                            text = "Estimated Budget",
                                            fontSize = taskTextSize,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    Text(
                                        text = "₱${job.job.salary}",
                                        fontSize = taskTextSize,
                                        fontWeight = FontWeight.Normal,
                                        modifier = Modifier.padding(start = 28.dp)
                                    )
                                }

                                Spacer(Modifier.height(16.dp))
                                Divider(color = Color.Gray, thickness = 0.3.dp)
                                Spacer(Modifier.height(16.dp))

                                // Other Services Section
                                when (val state = clientPostedJobState) {
                                    is GetClientPostedJobsViewModel.JobState.Loading -> {
                                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                                    }
                                    is GetClientPostedJobsViewModel.JobState.Success -> {
                                        val jobsAvailable = state.jobs.filter { it.status == "Available" }
                                        Text(
                                            text = "Other services needed by this client (${jobsAvailable.size})",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(bottom = 10.dp)
                                        )
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(420.dp)
                                                .background(Color(0xFFEFEDED))
                                        ) {
                                            if (jobsAvailable.isEmpty()) {
                                                Text(
                                                    text = "No other jobs posted by this client.",
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = Color.Black, // Changed to black for visibility
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier.align(Alignment.Center)
                                                )
                                            } else {
                                                LazyColumn(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .padding(horizontal = 10.dp, vertical = 8.dp)
                                                        .padding(bottom = 60.dp), // Added bottom padding to extend scrollable area
                                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    items(jobsAvailable.size) { index ->
                                                        val jobItem = jobsAvailable[index]
                                                        jobsOfClient(jobItem, navController)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    is GetClientPostedJobsViewModel.JobState.Error -> {
                                        Text(
                                            text = state.message,
                                            color = Color.Red,
                                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    is ViewJobViewModel.JobState.Error -> {
                        val errorMessage = (viewJobState as ViewJobViewModel.JobState.Error).message
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "Error: $errorMessage", fontSize = 18.sp, color = Color.Red)
                        }
                    }
                    is ViewJobViewModel.JobState.Idle -> {
                        // No UI for Idle state
                    }
                }
            },
            topBar = {
                Column(
                    modifier = Modifier
                        .background(myGradient3)
                        .fillMaxWidth()
                        .size(100.dp)
                        .padding(top = 20.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Arrow Back",
                            modifier = Modifier
                                .clickable {navController.popBackStack()}
                                .padding(start = 16.dp, top = 12.dp, end = 12.dp, bottom = 14.dp),
                            tint = Color(0xFF81D796)
                        )
                        Text(
                            text = "Job Details",
                            fontSize = 20.sp,
                            color = Color.White,
                            textAlign = TextAlign.Left,
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .weight(1f)
                        )
                    }
                }
            }
        ) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Transparent))
        }

        when (viewJobState) {
            is ViewJobViewModel.JobState.Loading -> {
                LoadingUI()
            }
            is ViewJobViewModel.JobState.Success -> {
                val job = (viewJobState as ViewJobViewModel.JobState.Success).data
                Button(
                    onClick = { navController.navigate("hiringdetails/${job.job.id}/${job.job.userId}") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 16.dp)
                        .constrainAs(buttonRef) {
                            bottom.linkTo(parent.bottom, margin = 44.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3CC0B0))
                ) {
                    Text(
                        text = "Apply Now",
                        fontSize = nameTextSize,
                        color = Color.White
                    )
                }
            }
            is ViewJobViewModel.JobState.Error -> {
                val errorMessage = (viewJobState as ViewJobViewModel.JobState.Error).message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error: $errorMessage", fontSize = 18.sp, color = Color.Red)
                }
            }
            is ViewJobViewModel.JobState.Idle -> {
                // No UI for Idle state
            }
        }
    }
}

@Composable
fun jobsOfClient(job: GetJobs, navController: NavController) {
    val windowSize = rememberWindowSizeClass()
    val date = ViewModelSetups.formatDateTime(job.createdAt)
    var jobType = job.jobType
    if (jobType == "Electrical_work") {
        jobType = "Electrical Work"
    }

    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 16.sp
        WindowType.MEDIUM -> 18.sp
        WindowType.LARGE -> 20.sp
    }
    val taskTextSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }

    Card(
        modifier = Modifier
            .clickable { navController.navigate("tradesmanapply/${job.id}") }
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box(modifier = Modifier.background(Color.White)) {
            Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = job.clientProfilePicture,
                        contentDescription = "Client Image",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(start = 8.dp),
                    )
                    Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                        Text(
                            text = "Looking for $jobType",
                            color = Color.Black,
                            fontWeight = FontWeight.Medium,
                            fontSize = nameTextSize
                        )
                        Row {
                            Text(text = "Job Posted:", color = Color.Black, fontSize = taskTextSize)
                            Text(
                                modifier = Modifier.padding(start = 4.dp),
                                text = date,
                                color = Color.Gray,
                                fontSize = taskTextSize
                            )
                        }
                    }
                }
            }
        }

    }
}