package com.example.androidproject.view.client.Categories

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.model.client.resumesItem
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.client.UploadFieldScreenShot
import com.example.androidproject.view.client.openScreenShot
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.view.theme.myGradient5
import com.example.androidproject.viewmodel.Resumes.GetResumesViewModel
import com.example.androidproject.viewmodel.report.ReportTradesmanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Electrician(navController: NavController, getResumesViewModel: GetResumesViewModel, reportTradesmanViewModel: ReportTradesmanViewModel) {
    val electricianList = getResumesViewModel.resumePagingData.collectAsLazyPagingItems()
    var displayedResumes by remember { mutableStateOf<List<resumesItem>>(emptyList()) }

    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp

    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        confirmValueChange = { it != SheetValue.Hidden }
    )
    var showFullText by remember { mutableStateOf(false) }
    val aboutme ="Connect with certified electricians for safe and reliable electrical installations, repairs, and troubleshooting."
    val maxPreviewLength = 70

    val scaffoldState = remember {
        BottomSheetScaffoldState(
            bottomSheetState = bottomSheetState,
            snackbarHostState = SnackbarHostState()
        )
    }
    val electricianImages = listOf(
        R.drawable.electricianbg1,
        R.drawable.electricianbg2,
        R.drawable.electricianbg3
    )

    val dismissedResumes by getResumesViewModel.dismissedResumes
    LaunchedEffect(electricianList.itemSnapshotList, dismissedResumes) {
        Log.d("TradesmanColumn", "Updating displayed resumes")
        displayedResumes = electricianList.itemSnapshotList.items
            .filter { it.id !in dismissedResumes } // Remove dismissed
    }
    val poppinsFont = FontFamily(
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_bold, FontWeight.Bold)
    )
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
    LaunchedEffect(Unit) {
        getResumesViewModel.refreshResumes()
    }


    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetContainerColor = Color.White,
        sheetPeekHeight = screenHeightDp * 0.82f,
        sheetContent = {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    // "About" Section
                    Text(
                        text = "About",
                        fontSize = nameTextSize,
                        fontFamily = poppinsFont,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )

                    if (aboutme.length > maxPreviewLength) {
                        Column {
                            Text(
                                text = if (showFullText) aboutme else "${aboutme.take(maxPreviewLength)}...",
                                modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp),
                                fontSize = taskTextSize,
                                fontFamily = poppinsFont,
                                fontWeight = FontWeight.Normal,
                                color = if (aboutme.isEmpty()) Color.Gray else Color.Gray
                            )
                            Text(
                                modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp).clickable(interactionSource = remember { MutableInteractionSource() }
                                    ,indication = null){ showFullText = !showFullText},
                                text = if (showFullText) "See Less" else "See More",
                                color = Color.Blue,
                                fontSize = taskTextSize,
                                fontFamily = poppinsFont,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.End
                            )

                        }
                    } else {
                        Text(
                            text = aboutme,
                            modifier = Modifier.padding(top = 4.dp),
                            fontSize = taskTextSize,
                            color = if (aboutme.isEmpty()) Color.Gray else Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Expert",
                        fontSize = nameTextSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFont,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        val filteredList = electricianList.itemSnapshotList.items.filter {
                            it.specialty.contains("Electrical_work") && it.id !in dismissedResumes
                        }

                        if (filteredList.isEmpty()) {
                            Box(Modifier.fillMaxWidth().height(400.dp)
                                ,contentAlignment = Alignment.Center)
                            {
                                Text(
                                    text = "No Electrician workers",
                                    fontSize = nameTextSize,
                                    fontFamily = poppinsFont,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            filteredList.forEach { electrician ->
                                if (electrician.id !in dismissedResumes) {
                                    ElectricianItem (
                                        electrician,
                                        navController,
                                        reportTradesmanViewModel
                                    ) {
                                        getResumesViewModel.dismissResume(electrician.id)
                                    }
                                }
                            }
                        }

                        if (electricianList.loadState.append is LoadState.Loading) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    ) {
        // Header Section (Outside the BottomSheet)
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RectangleShape
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                AutoSlidingImagePager(
                    imageResIds = electricianImages,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(myGradient5)
                )
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding( vertical = 8.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Arrow Back",
                            modifier = Modifier
                                .clickable {
                                    navController.navigate("main_screen"){
                                        navController.popBackStack()
                                    }
                                }
                                .padding(8.dp)
                                .size(24.dp),
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Electrical Solutions",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFont,
                        color = Color.White,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElectricianItem(electrician: resumesItem, navController: NavController, reportTradesmanViewModel:ReportTradesmanViewModel, onUninterested: () -> Unit) {
    var selectedIndex by remember { mutableIntStateOf(-1) }
    var otherReason by remember { mutableStateOf("") }
    var reasonDescription by remember { mutableStateOf("") }
    var showMenu by remember { mutableStateOf(false) }
    var showReportSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var reportSubmissionKey by remember { mutableStateOf<Long?>(null) }
    val reasons = listOf(
        "Abusive or Harassing Behavior",
        "Inappropriate Content or Language",
        "Fraudulent Activity or Scam",
        "Poor Quality of Service",
        "Unprofessional Conduct",
        "Safety Concerns",
        "Others"
    )
    val reportState by reportTradesmanViewModel.reportState.collectAsState()
    val context = LocalContext.current
    val windowSize = rememberWindowSizeClass()
    val iconSize = when (windowSize.width) {
        WindowType.SMALL -> 16.dp
        WindowType.MEDIUM -> 24.dp
        WindowType.LARGE -> 32.dp
    }
    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 18.sp
        WindowType.MEDIUM -> 20.sp
        WindowType.LARGE -> 22.sp
    }
    val smallTextSize = when (windowSize.width) {
        WindowType.SMALL -> 14.sp
        WindowType.MEDIUM -> 16.sp
        WindowType.LARGE -> 18.sp
    }
    var reportDocument by remember { mutableStateOf<Uri?>(null) }

    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { reportDocument = it } }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 8.dp)
            .clickable {  navController.navigate("booknow/${electrician.id}") },

        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            // Profile Picture
            AsyncImage(
                model = electrician.profilePic,
                contentDescription = electrician.tradesmanFullName,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(25.dp)) // Apply rounded corners
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Name and Category
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Row (Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween){
                    Text(
                        text = electrician.tradesmanFullName,
                        fontSize = nameTextSize,
                        fontWeight = FontWeight.Bold
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
                                text = { Text("Report") },
                                onClick = {
                                    showMenu = false
                                    showReportSheet = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Uninterested") },
                                onClick = {
                                    showMenu = false
                                    onUninterested()
                                }
                            )
                        }
                    }
                }

                Row(modifier = Modifier.size(185.dp, 110.dp)) {
                    Box(
                        modifier = Modifier
                            .size(80.dp, 45.dp)
                            .padding(top = 10.dp)
                            .background(
                                color = (Color(0xFFF5F5F5)),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Text(
                            text = "P${electrician.workFee}/hr",
                            fontSize = smallTextSize,
                            modifier = Modifier.padding(top = 5.dp, start = 8.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(70.dp, 45.dp)
                            .padding(top = 10.dp, start = 10.dp)
                            .background(
                                color = (Color(0xFFF5F5F5)),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star, contentDescription = "Start Icon",
                            tint = Color(0xFFFFA500), modifier = Modifier
                                .size(25.dp)
                                .padding(top = 7.dp, start = 2.dp)
                        )
                        Text(
                            when {
                                electrician.ratings == 0f -> "0"
                                else -> String.format("%.1f", electrician.ratings)
                            },
                            fontSize = smallTextSize,
                            modifier = Modifier.padding(top = 5.dp, start = 28.dp)
                        )
                    }

                }
            }
        }
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
                        modifier = Modifier.size(110.dp, 45.dp)
                    ) {
                        Text("Cancel")
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
                                reportSubmissionKey = System.currentTimeMillis() // Set unique key
                                reportTradesmanViewModel.report(selectedReason, reasonDescription, reportDocument!!,context,electrician.userid)
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
                    LaunchedEffect(reportState) {
                        when(val report = reportState){
                            is ReportTradesmanViewModel.ReportState.Loading -> {
                                //do nothing
                            }
                            is ReportTradesmanViewModel.ReportState.Success -> {
                                val responsereport = report.data?.message
                                Toast.makeText(context, responsereport, Toast.LENGTH_SHORT).show()

                                reportTradesmanViewModel.resetState()
                                // Close the dialog
                                showReportSheet = false
                            }
                            is ReportTradesmanViewModel.ReportState.Error -> {
                                val errorMessage = report.message
                                Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                                showReportSheet = true
                                reportTradesmanViewModel.resetState()
                            }
                            else -> Unit
                        }
                    }

                }
            }
        }
    }
}
