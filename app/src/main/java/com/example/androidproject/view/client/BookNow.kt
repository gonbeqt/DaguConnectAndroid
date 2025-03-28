package com.example.androidproject.view.client

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.view.theme.myGradient3
import com.example.androidproject.view.tradesman.downloadFileTradesman
import com.example.androidproject.viewmodel.Resumes.ViewResumeViewModel
import com.example.androidproject.viewmodel.ratings.ViewRatingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookNow(
    viewResumeViewModel: ViewResumeViewModel,
    navController: NavController,
    resumeId: String,
    viewRatingsViewModel: ViewRatingsViewModel
) {
    val viewResumeState by viewResumeViewModel.viewResumeState.collectAsState()
    val resumeIdInt = resumeId.toIntOrNull() ?: return
    val context = LocalContext.current
    val windowSize = rememberWindowSizeClass()
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    var showConfirmDialog by remember { mutableStateOf(false) }

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

    LaunchedEffect(Unit) {
        viewResumeViewModel.viewResume(resumeIdInt)
    }

    var downloadId by remember { mutableStateOf<Long?>(null) }
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        confirmValueChange = { it != SheetValue.Hidden }
    )
    val scaffoldState = BottomSheetScaffoldState(
        bottomSheetState = bottomSheetState,
        snackbarHostState = remember { SnackbarHostState() }
    )
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (scaffoldRef, buttonsRef) = createRefs()

        BottomSheetScaffold(
            modifier = Modifier
                .constrainAs(scaffoldRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(buttonsRef.top)
                    height = Dimension.fillToConstraints
                },
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            scaffoldState = scaffoldState,
            sheetContainerColor = Color.White,
            sheetPeekHeight = screenHeightDp * 0.90f,
            sheetContent = {
                when (val state = viewResumeState) {
                    is ViewResumeViewModel.ViewResumeState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is ViewResumeViewModel.ViewResumeState.Success -> {
                        val resume = state.data
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                                .padding(12.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            // Tradesman Info Section
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 0.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = resume.profilePic,
                                    contentDescription = "Tradesman Image",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(start = 10.dp)
                                        .clip(CircleShape)
                                )
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 15.dp)
                                ) {
                                    Text(
                                        text = resume.tradesmanFullName,
                                        color = Color.Black,
                                        fontWeight = FontWeight(500),
                                        fontSize = nameTextSize,
                                        modifier = Modifier.padding(top = 10.dp)
                                    )
                                    resume.specialty?.let {
                                        BoxRow(specialties = it.replace("_", " "))
                                    }
                                    resume.preferredWorkLocation?.let {
                                        Text(
                                            text = it,
                                            color = Color.Black,
                                            fontSize = taskTextSize
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = Color(0xFFF5F5F5),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "Rating",
                                            tint = Color(0xFFFFA500),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = when {
                                                resume.ratings == 0f -> "0"
                                                else -> String.format("%.1f", resume.ratings)
                                            },
                                            fontSize = smallTextSize
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Divider(
                                modifier = Modifier.padding(horizontal = 10.dp),
                                color = Color.Gray,
                                thickness = 0.3.dp
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            // About Me Section
                            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                                Text(
                                    text = "About Me",
                                    color = Color.Black,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight(500)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                var showFullText by remember { mutableStateOf(false) }
                                val aboutMe = resume.aboutMe ?: "No description available"
                                val maxPreviewLength = 100
                                if (aboutMe.length > maxPreviewLength) {
                                    Column {
                                        Text(
                                            text = if (showFullText) aboutMe else "${aboutMe.take(maxPreviewLength)}...",
                                            fontSize = taskTextSize,
                                            color = if (aboutMe.isEmpty()) Color.Gray else Color.Black
                                        )
                                        TextButton(
                                            onClick = { showFullText = !showFullText },
                                            modifier = Modifier.align(Alignment.End)
                                        ) {
                                            Text(
                                                text = if (showFullText) "See Less" else "See More",
                                                color = Color.Blue,
                                                fontSize = smallTextSize
                                            )
                                        }
                                    }
                                } else {
                                    Text(
                                        text = aboutMe,
                                        modifier = Modifier.padding(top = 4.dp),
                                        fontSize = taskTextSize,
                                        color = if (aboutMe.isEmpty()) Color.Gray else Color.Black
                                    )
                                }

                                Divider(color = Color.Gray, thickness = 0.3.dp)
                                Spacer(modifier = Modifier.height(16.dp))

                                // Tradesman Information
                                Text(
                                    text = "Tradesman Information",
                                    color = Color.Black,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight(500)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Preferred Location",
                                        color = Color.Gray,
                                        fontSize = taskTextSize,
                                        fontWeight = FontWeight(500)
                                    )
                                    resume.preferredWorkLocation?.let {
                                        Text(
                                            text = it,
                                            color = Color.Black,
                                            fontSize = taskTextSize,
                                            fontWeight = FontWeight(500)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Est. Rate",
                                        color = Color.Gray,
                                        fontSize = taskTextSize,
                                        fontWeight = FontWeight(500)
                                    )
                                    Text(
                                        text = "₱${resume.workFee}",
                                        color = Color.Black,
                                        fontWeight = FontWeight(500),
                                        fontSize = taskTextSize
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Trade Credential",
                                        color = Color.Gray,
                                        fontSize = taskTextSize,
                                        fontWeight = FontWeight(500)
                                    )
                                    Text(
                                        text = "Download File",
                                        color = Color.Blue,
                                        fontSize = taskTextSize,
                                        textDecoration = TextDecoration.Underline,
                                        fontWeight = FontWeight(500),
                                        modifier = Modifier.clickable {
                                            resume.documents?.let {
                                                showConfirmDialog = true
                                            } ?: Toast.makeText(
                                                context,
                                                "No file available",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                }

                                if (showConfirmDialog) {
                                    AlertDialog(
                                        onDismissRequest = { showConfirmDialog = false },
                                        title = { Text("Download File") },
                                        text = { Text("Would you like to download the credential file?") },
                                        confirmButton = {
                                            TextButton(onClick = {
                                                resume.documents?.let { fileUrl ->
                                                    val fileName = "trade_credential_${resume.tradesmanFullName}.pdf"
                                                    try {
                                                        downloadId = downloadFileTradesman(context, fileUrl, fileName)
                                                        Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show()

                                                        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                                                        val onComplete = object : BroadcastReceiver() {
                                                            override fun onReceive(context: Context, intent: Intent) {
                                                                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                                                                if (id == downloadId) {
                                                                    val query = DownloadManager.Query().setFilterById(id)
                                                                    val cursor = downloadManager.query(query)
                                                                    if (cursor.moveToFirst()) {
                                                                        val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                                                                        if (statusIndex != -1 && cursor.getInt(statusIndex) == DownloadManager.STATUS_SUCCESSFUL) {
                                                                            Toast.makeText(context, "Download completed successfully", Toast.LENGTH_SHORT).show()
                                                                        }
                                                                    }
                                                                    cursor.close()
                                                                    context.unregisterReceiver(this)
                                                                }
                                                            }
                                                        }

                                                        ContextCompat.registerReceiver(
                                                            context,
                                                            onComplete,
                                                            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                                                            ContextCompat.RECEIVER_NOT_EXPORTED
                                                        )
                                                    } catch (e: Exception) {
                                                        Toast.makeText(context, "Download failed: ${e.message}", Toast.LENGTH_LONG).show()
                                                    }
                                                }
                                                showConfirmDialog = false
                                            }) {
                                                Text("Yes")
                                            }
                                        },
                                        dismissButton = {
                                            TextButton(onClick = { showConfirmDialog = false }) {
                                                Text("No")
                                            }
                                        }
                                    )
                                }

                                Divider(color = Color.Gray, thickness = 0.3.dp)
                                Spacer(modifier = Modifier.height(16.dp))

                                // Ratings Section
                                Text(
                                    text = "Ratings",
                                    color = Color.Black,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight(500)
                                )
                                Text(
                                    text = "Feedback from satisfied clients",
                                    color = Color.Black,
                                    fontSize = taskTextSize,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                FeedbackItem(viewRatingsViewModel, resume.userid)
                            }
                        }
                    }
                    is ViewResumeViewModel.ViewResumeState.Error -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("Error: ${state.message}", color = Color.Red)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewResumeViewModel.viewResume(resumeIdInt) }) {
                                Text("Retry")
                            }
                        }
                    }
                    else -> Unit
                }
            },
            topBar = {
                Column(
                    modifier = Modifier
                        .background(myGradient3)
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(top = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    navController.navigate("main_screen")
                                }
                                .padding(start = 16.dp, top = 12.dp, end = 12.dp, bottom = 14.dp),
                            tint = Color.White
                        )
                        Text(
                            text = "Expert Details",
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
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.Transparent)
            )
        }

        // Fixed Buttons at the Bottom
        if (viewResumeState is ViewResumeViewModel.ViewResumeState.Success) {
            val resume = (viewResumeState as ViewResumeViewModel.ViewResumeState.Success).data
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .constrainAs(buttonsRef) {
                        bottom.linkTo(parent.bottom, margin = 44.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = {
                        val encodedProfilePicture = Uri.encode(resume.profilePic)
                        navController.navigate("messaging/0/$resumeId/${resume.tradesmanFullName}/$encodedProfilePicture")
                    },
                    modifier = Modifier
                        .border(1.dp,Color(0xFF42C2AE), shape = RoundedCornerShape(12.dp))
                        .width(150.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)

                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            imageVector = Icons.Default.Message,
                            contentDescription = "Message",
                            tint = Color(0xFF42C2AE)
                        )
                        Text(text = "Chat Me", color = Color(0xFF42C2AE), fontSize = nameTextSize)
                    }
                }

                Button(
                    onClick = { navController.navigate("confirmbook/${resume.id}/${resume.userid}") },
                    modifier = Modifier
                        .width(170.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42C2AE))
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            imageVector = Icons.Default.AddShoppingCart,
                            contentDescription = "Book",
                            tint = Color.White
                        )
                        Text(text = "Book Now", color = Color.White, fontSize = nameTextSize)
                    }
                }
            }
        }
    }
}

@Composable
fun BoxRow(specialties: String) {
    val windowSize = rememberWindowSizeClass()
    val taskTextSize = when (windowSize.width) {
        WindowType.SMALL -> 14.sp
        WindowType.MEDIUM -> 16.sp
        WindowType.LARGE -> 18.sp
    }
    Text(
        text = specialties,
        color = Color.Black,
        fontSize = taskTextSize,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun FeedbackItem(viewRatingsViewModel: ViewRatingsViewModel, tradesmanId: Int) {
    val viewRatingState by viewRatingsViewModel.viewRatingsState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(tradesmanId) {
        viewRatingsViewModel.viewRatings(tradesmanId)
    }

    when (val viewRatings = viewRatingState) {
        is ViewRatingsViewModel.ViewRatingsState.Loading -> {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = "Loading ratings...", fontSize = 16.sp, color = Color.Gray)
            }
        }
        is ViewRatingsViewModel.ViewRatingsState.Success -> {
            val ratingsList = viewRatings.data
            if (ratingsList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No ratings yet",
                        fontSize = 16.sp,
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
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
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
                                            .verticalScroll(rememberScrollState())
                                    ) {
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
                                                    text = ratings.client_name ?: "Unknown",
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
                                                        text = "${ratings.ratings ?: 0} stars",
                                                        modifier = Modifier.padding(start = 4.dp)
                                                    )
                                                }
                                            }
                                        }
                                        Text(
                                            text = "Comments:",
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(top = 8.dp),
                                            fontSize = 20.sp
                                        )
                                        var showFullText by remember { mutableStateOf(false) }
                                        val message = ratings.message ?: "No comments available"
                                        val maxPreviewLength = 100
                                        if (message.length > maxPreviewLength) {
                                            Column {
                                                Text(
                                                    text = if (showFullText) message else "${message.take(maxPreviewLength)}...",
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = viewRatings.message,
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        else -> Unit
    }
}