package com.example.androidproject.view.tradesman


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.androidproject.view.theme.myGradient3
import coil.compose.AsyncImage
import com.example.androidproject.LoadingUI
import com.example.androidproject.ViewModelSetups
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.viewmodel.jobs.ViewJobViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradesmanApply(
    jobId: String,
    navController: NavController,
    viewModel: ViewJobViewModel
) {
    val windowSize = rememberWindowSizeClass()
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp // Get screen height in dp

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
    val viewJobState by viewModel.jobState.collectAsState()
    val jobID = jobId.toIntOrNull() ?: return
    val coroutineScope = rememberCoroutineScope()

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

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()

    ) {
        val (scaffoldRef, buttonRef) = createRefs()

        BottomSheetScaffold(
            modifier = Modifier
                .constrainAs(scaffoldRef) {
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
                    }

                    is ViewJobViewModel.JobState.Success -> {
                        val job = (viewJobState as ViewJobViewModel.JobState.Success).data
                        val date = ViewModelSetups.formatDateTime(job.job.createdAt)
                        val deadline = ViewModelSetups.formatDateTime(job.job.deadline)
                        var jobType = job.job.jobType
                        if (jobType == "Ac_technician") {
                            jobType = "AC Technician"
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)

                        ) {
                            Box(
                                modifier = Modifier
                                    .background(Color.Transparent)
                                    .fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
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
                                                    text = "099384773839",
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
                                        thickness = 0.3.dp,
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
                                        Text(
                                            text = "Posted on $date - ${job.job.status}",
                                        )
                                        Text(
                                            text = "Job Date: $deadline",
                                        )
                                        Text(
                                            text = "Applicants(0)",
                                        )

                                        Spacer(Modifier.height(16.dp))

                                        Divider(
                                            color = Color.Gray,
                                            thickness = 0.3.dp,
                                        )

                                        Spacer(Modifier.height(16.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Job Description",
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(text = job.job.jobDescription)
                                        }

                                        Spacer(Modifier.height(16.dp))

                                        Divider(
                                            color = Color.Gray,
                                            thickness = 0.3.dp,
                                        )

                                        Spacer(Modifier.height(16.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Location and Salary",
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }

                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 10.dp),
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.LocationOn,
                                                    contentDescription = " Preferred Location",
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
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            verticalArrangement = Arrangement.Center
                                        ) {
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

                                        Divider(
                                            color = Color.Gray,
                                            thickness = 0.3.dp,
                                        )

                                        Spacer(Modifier.height(16.dp))

                                        // Other Services and Status
                                        Column(modifier = Modifier.fillMaxWidth()) {
                                            Row(
                                                modifier = Modifier
                                                    .padding(bottom = 100.dp)
                                                    .fillMaxWidth(),
                                                horizontalArrangement = Arrangement.Start,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Other services needed by this client (0)",
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }

                    is ViewJobViewModel.JobState.Error -> {
                        val errorMessage = (viewJobState as ViewJobViewModel.JobState.Error).message
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Error: $errorMessage",
                                fontSize = 18.sp,
                                color = Color.Red
                            )
                        }
                    }

                    is ViewJobViewModel.JobState.Idle -> {
                        // No UI to display in Idle state
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Arrow Back",
                            modifier = Modifier
                                .clickable { navController.navigate("main_screen") }
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            )
        }
        when (viewJobState) {
            is ViewJobViewModel.JobState.Loading -> {
                LoadingUI()
            }

            is ViewJobViewModel.JobState.Success -> {
                val job = (viewJobState as ViewJobViewModel.JobState.Success).data
                Button(
                    onClick = {
                        navController.navigate("hiringdetails/${job.job.id}")
                    },
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3CC0B0)
                    )
                ) {
                    Text(
                        text = "Apply for Job",
                        fontSize = nameTextSize,
                        color = Color.White
                    )
                }
            }
            is ViewJobViewModel.JobState.Error -> {
                val errorMessage = (viewJobState as ViewJobViewModel.JobState.Error).message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: $errorMessage",
                        fontSize = 18.sp,
                        color = Color.Red
                    )
                }
            }

            is ViewJobViewModel.JobState.Idle -> {
                // No UI to display in Idle state
            }
        }

    }
}