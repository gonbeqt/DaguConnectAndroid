package com.example.androidproject.view.tradesman

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.client.UploadFieldScreenShot
import com.example.androidproject.view.client.openScreenShot
import com.example.androidproject.view.extras.SnackbarController
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.view.theme.myGradient3
import com.example.androidproject.viewmodel.job_application.tradesman.GetMyJobApplicationViewModel
import com.example.androidproject.viewmodel.jobs.ViewJobViewModel
import com.example.androidproject.viewmodel.report.ReportClientViewModel
import java.sql.Types.NULL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradesmanApplicationCancelled(jobId: String, jobs :String, modifier: Modifier = Modifier, navController: NavController, getMyJobApplications: GetMyJobApplicationViewModel, getJobs: ViewJobViewModel,reportClientViewModel: ReportClientViewModel) {
    val reportClientState by reportClientViewModel.reportClientState.collectAsState()
    val context = LocalContext.current
    val windowSize = rememberWindowSizeClass()
    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 16.sp
        WindowType.MEDIUM -> 18.sp
        WindowType.LARGE -> 20.sp
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
    var reportDocument by remember { mutableStateOf<Uri?>(null) }
    var showMenu by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(-1) }
    var otherReason by remember { mutableStateOf("") }
    var reasonDescription by remember { mutableStateOf("") }
    var showReportSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var reportSubmissionKey by remember { mutableStateOf<Long?>(null) } // Unique key for each submission
    val reasons = listOf(
        "Abusive or Harassing Behavior",
        "Inappropriate Content or Language",
        "Fraudulent Activity or Scam",
        "Poor Quality of Service",
        "Unprofessional Conduct",
        "Safety Concerns",
        "Others"
    )
    LaunchedEffect(reportClientState, reportSubmissionKey) {
        if (reportSubmissionKey == null) return@LaunchedEffect // Skip if no submission yet
        when (val reportClient = reportClientState) {
            is ReportClientViewModel.ReportClientState.Loading -> {
                // nothing
            }
            is ReportClientViewModel.ReportClientState.Success -> {
                val responseReport = reportClient.data?.message
                if (responseReport != null) {
                    SnackbarController.show(responseReport)
                }
                reportSubmissionKey = null // Reset key after handling
                showReportSheet = false
                reportClientViewModel.resetState()
            }
            is ReportClientViewModel.ReportClientState.Error -> {
                reportSubmissionKey = null // Reset key after handling
                val error = reportClient.message
                SnackbarController.show(error)
            }
            else -> Unit
        }
    }

    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { reportDocument = it } }
    val jobID = jobId.toIntOrNull() ?: return
    val jobS = jobs.toIntOrNull() ?: return

    val jobsState by getJobs.jobState.collectAsState()

    val bookingPendingState = getMyJobApplications.jobApplicationPagingData.collectAsLazyPagingItems()
    LaunchedEffect(Unit) {
        bookingPendingState.refresh()
        getJobs.getJobById(jobS)
    }

    // Find the booking with the matching jobId and "Pending" status
    val selectedBooking = bookingPendingState.itemSnapshotList.items
        .firstOrNull { it.id == jobID && it.status == "Cancelled" }
    when (val getjobs = jobsState ){
        is ViewJobViewModel.JobState.Loading -> {

        }
        is ViewJobViewModel.JobState.Success ->{
            val myjobs = getjobs.data.job
            Box(Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F5F5))
                        .padding(WindowInsets.systemBars.asPaddingValues())

                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .background(Color.White)
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    modifier = Modifier.clickable {},
                                    tint = Color(0xFF81D796)
                                )
                                Text(
                                    text = "Job Details",
                                    fontSize = 24.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 8.dp)
                                )
                            }
                        }
                    }


                    Column(modifier = Modifier.fillMaxWidth()) {

                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(brush = myGradient3)
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Your application is Cancelled",
                                        fontSize = nameTextSize,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .size(200.dp),
                                colors = CardDefaults.cardColors(Color.White),
                                shape = RoundedCornerShape(
                                    0.dp,
                                    0.dp,
                                    15.dp,
                                    15.dp
                                ) // Keep card shape
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(vertical = 18.dp, horizontal = 16.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "Client’s Information",
                                        fontSize = nameTextSize,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black,
                                    )
                                    Divider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 10.dp),
                                        thickness = 0.5.dp,
                                        color = Color.Gray
                                    )
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Client image
                                        if (selectedBooking != null) {
                                            AsyncImage(
                                                model = selectedBooking.clientProfilePicture,
                                                contentDescription = "Tradesman Image",
                                                modifier = Modifier
                                                    .size(100.dp)
                                            )
                                        }

                                        // Tradesman details
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(start = 10.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                if (selectedBooking != null) {
                                                    Text(
                                                        text = selectedBooking.clientFullname,
                                                        color = Color.Black,
                                                        fontWeight = FontWeight.Medium,
                                                        fontSize = nameTextSize,
                                                    )
                                                }
                                                Box {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.meatball_ic),
                                                        contentDescription = "Menu Icon",
                                                        modifier = Modifier
                                                            .size(25.dp)
                                                            .clickable { showMenu = true }
                                                    )
                                                    // Popup Menu
                                                    DropdownMenu(
                                                        expanded = showMenu,
                                                        onDismissRequest = { showMenu = false },
                                                        modifier = Modifier.background(Color.White)
                                                    ) {
                                                        DropdownMenuItem(
                                                            text = {
                                                                Text(
                                                                    "Report",
                                                                    textAlign = TextAlign.Center
                                                                )
                                                            },
                                                            onClick = {
                                                                showMenu = false
                                                                showReportSheet = true
                                                            }
                                                        )
                                                    }
                                                }
                                            }



                                            if (selectedBooking != null) {
                                                Text(
                                                    text = selectedBooking.jobAddress,
                                                    color = Color.Gray,
                                                    fontSize = smallTextSize,
                                                )
                                            }
                                        }
                                    }

                                }

                            }
                            Spacer(Modifier.height(10.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = CardDefaults.cardColors(Color.White),
                                    shape = RoundedCornerShape(15.dp) // Keep card shape
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Text(

                                            text = "Task Information",
                                            fontSize = nameTextSize,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Divider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 10.dp),
                                            thickness = 0.5.dp,
                                            color = Color.Gray
                                        )
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 10.dp, vertical = 8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Outlined.Work,
                                                    tint = Color.Black,
                                                    contentDescription = "Job type",
                                                    modifier = Modifier
                                                        .size(32.dp)
                                                )
                                                Text(
                                                    text = "Job Type:",
                                                    fontSize = nameTextSize,
                                                    color = Color.Black,
                                                    modifier = Modifier.padding(start = 10.dp)
                                                )
                                                if (selectedBooking != null) {
                                                    Text(
                                                        text = selectedBooking.jobType,
                                                        fontSize = nameTextSize,
                                                        color = Color.Black,
                                                        modifier = Modifier.padding(start = 10.dp)
                                                    )
                                                }
                                            }
                                        }

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 10.dp, vertical = 8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.DateRange,
                                                    contentDescription = "Job Date",
                                                    modifier = Modifier
                                                        .size(32.dp)
                                                )
                                                Text(
                                                    text = "Job Date:",
                                                    fontSize = nameTextSize,
                                                    color = Color.Black,
                                                    modifier = Modifier.padding(start = 10.dp)
                                                )
                                                if (myjobs != null) {
                                                    Text(
                                                        text = myjobs.deadline,
                                                        fontSize = nameTextSize,
                                                        color = Color.Black,
                                                        modifier = Modifier.padding(start = 10.dp)
                                                    )
                                                }
                                            }
                                        }
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 10.dp, vertical = 8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Outlined.LocationOn,
                                                    contentDescription = "Job Date",
                                                    modifier = Modifier
                                                        .size(32.dp)
                                                )
                                                Text(
                                                    text = "Job Location:",
                                                    fontSize = nameTextSize,
                                                    color = Color.Black,
                                                    modifier = Modifier.padding(start = 10.dp)
                                                )
                                                if (selectedBooking != null) {
                                                    Text(
                                                        text = " ${selectedBooking.jobAddress}, Pangasinan",
                                                        fontSize = nameTextSize,
                                                        color = Color.Black,
                                                        modifier = Modifier.padding(start = 10.dp)
                                                    )
                                                }
                                            }
                                        }
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(

                                                        imageVector = Icons.Outlined.Work,
                                                        contentDescription = "Task Details",
                                                        modifier = Modifier
                                                            .size(32.dp)
                                                    )
                                                    Text(
                                                        text = "Task:",
                                                        fontSize = nameTextSize,
                                                        color = Color.Black,
                                                        modifier = Modifier.padding(start = 10.dp)
                                                    )

                                                }
                                                Spacer(Modifier.height(10.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clip(RoundedCornerShape(12.dp))
                                                        .background(Color(0xFFF5F5F5))
                                                        .border(
                                                            1.dp,
                                                            Color.Gray,
                                                            shape = RoundedCornerShape(12.dp)
                                                        )
                                                ) {
                                                    if (myjobs != null) {
                                                        Text(
                                                            modifier = Modifier.padding(16.dp),
                                                            text = myjobs.jobDescription,
                                                            fontSize = taskTextSize
                                                        )
                                                    }
                                                }
                                            }

                                        }

                                    }

                                }
                            }
                            Spacer(Modifier.height(10.dp))

                            // Third Column with Card and content
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = CardDefaults.cardColors(Color.White),
                                    shape = RoundedCornerShape(15.dp) // Keep card shape
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Text(

                                            text = "Support Center",
                                            fontSize = nameTextSize,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Divider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 10.dp),
                                            thickness = 0.5.dp,
                                            color = Color.Gray
                                        )

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 10.dp, vertical = 2.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Outlined.Message,
                                                    contentDescription = "Message Icon",
                                                    modifier = Modifier
                                                        .size(32.dp)
                                                )
                                                Text(
                                                    text = "Contact Client",
                                                    fontSize = nameTextSize,
                                                    modifier = Modifier.padding(start = 10.dp)
                                                        .clickable {
                                                            val encodedProfilePicture = Uri.encode(
                                                                selectedBooking?.clientProfilePicture
                                                            )
                                                            navController.navigate("messaging/0/${selectedBooking?.clientId}/${selectedBooking?.clientFullname}/${encodedProfilePicture}")
                                                        }
                                                )
                                            }
                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowRight,
                                                contentDescription = "Arrow Right Icon",
                                                modifier = Modifier
                                                    .size(32.dp)
                                            )
                                        }
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 10.dp, vertical = 2.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Outlined.Help,
                                                    contentDescription = "Help Icon",
                                                    modifier = Modifier
                                                        .size(32.dp)
                                                )
                                                Text(
                                                    text = "Help",
                                                    fontSize = nameTextSize,
                                                    modifier = Modifier.padding(start = 10.dp)
                                                )
                                            }
                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowRight,
                                                contentDescription = "Arrow Right Icon",
                                                modifier = Modifier
                                                    .size(32.dp)
                                            )
                                        }


                                    }

                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = CardDefaults.cardColors(Color.White),
                                    shape = RoundedCornerShape(15.dp) // Keep card shape
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                fontWeight = FontWeight.Normal,
                                                fontSize = smallTextSize,
                                                color = Color.Gray,
                                                text = "Posted on"
                                            )
                                            if (myjobs != null) {
                                                Text(
                                                    fontSize = smallTextSize,
                                                    text = myjobs.createdAt
                                                )
                                            }
                                        }


                                    }

                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { navController.navigate("tradesmanapplicationcanceldetails/${selectedBooking?.id}/${jobS}") }
                                    .background(
                                        color = Color.Transparent,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "Cancelled Details", fontSize = nameTextSize)
                            }
                        }
                    }

                }

            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp)
                    .zIndex(10f),
                contentAlignment = Alignment.BottomCenter
            ) {
                SnackbarController.ObserveSnackbar()
            }

        }
        else -> Unit
    }

    if (showReportSheet) {
        ModalBottomSheet(
            onDismissRequest = { showReportSheet = false },
            sheetState = bottomSheetState,
            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Report", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    IconButton(onClick = { showReportSheet = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Column(modifier = Modifier.padding(top = 16.dp)) {

                    reasons.forEachIndexed { index, reason ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        ) {
                            Checkbox(
                                checked = selectedIndex == index,
                                onCheckedChange = {
                                    selectedIndex = if (selectedIndex == index) -1 else index
                                }
                            )
                            if (reason == "Others") {
                                Column(
                                    modifier = Modifier.fillMaxWidth()
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
                                    color = Color.Black,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }

                        }
                    }
                }
                if (selectedIndex != -1) {
                    Text(
                        text = "Tell us the Problem",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

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
                        onClick = { showReportSheet = false },
                        modifier = Modifier.size(110.dp, 45.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF42C2AE)
                        )
                    ) {
                        Text("Cancel", color = Color.White)
                    }
                    Button(
                        onClick = {
                            if (selectedIndex == -1) {
                                SnackbarController.show("Please select a reason for reporting")
                            } else {
                                val selectedReason = if (selectedIndex == reasons.size - 1) {
                                    otherReason
                                } else {
                                    reasons[selectedIndex]
                                }
                                reportSubmissionKey = System.currentTimeMillis() // Set unique key
                                if (selectedBooking != null) {
                                    reportClientViewModel.reportClient(
                                        selectedBooking.clientId,
                                        selectedReason,
                                        reasonDescription,
                                        reportDocument!!,
                                        context
                                    )
                                }
                                else {
                                    SnackbarController.show("No booking selected to report")
                                }
                            }
                        },
                        modifier = Modifier.size(110.dp, 45.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF42C2AE)
                        )
                    ) {
                        Text("Submit", color = Color.White)
                    }
                }
            }
        }
    }
}

