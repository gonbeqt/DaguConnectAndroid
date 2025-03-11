package com.example.androidproject.view.client

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.model.client.resumesItem
import com.example.androidproject.view.Categories
import com.example.androidproject.view.WindowSize
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.view.theme.myGradient2
import com.example.androidproject.view.tradesman.UploadField
import com.example.androidproject.view.tradesman.openFile
import com.example.androidproject.viewmodel.Resumes.GetResumesViewModel
import com.example.androidproject.viewmodel.report.ReportViewModel


@Composable
fun HomeScreen( modifier: Modifier = Modifier,navController: NavController,getResumesViewModel: GetResumesViewModel,reportViewModel: ReportViewModel) {
    Log.i("Screen" , "HomeScreen")
    val windowSize = rememberWindowSizeClass()


    val categories = listOf(    
        Categories(R.drawable.carpentry, "Carpentry"),
        Categories(R.drawable.painting, "Painter"),
        Categories(R.drawable.welding, "Welding"),
        Categories(R.drawable.electrician, "Electrician"),
        Categories(R.drawable.plumbing, "Plumbing"),
        Categories(R.drawable.masonry, "Masonry"),
        Categories(R.drawable.roofing, "Roofing"),
        Categories(R.drawable.airconrepair, "AC Repair"),
        Categories(R.drawable.mechanics, "Mechanics"),
        Categories(R.drawable.cleaning, "Cleaning")

    )





    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Provide navController to the SearchField
            HomeTopSection(navController,windowSize )
            Spacer(modifier = Modifier.height(5.dp))

            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {
                Spacer(modifier = Modifier.height(30.dp))
                ExploreNow(windowSize)

                Spacer(modifier = Modifier.height(20.dp))
                CategoryRow(categories,navController)

                Spacer(modifier = Modifier.height(5.dp))
                TradesmanColumn(getResumesViewModel,navController,reportViewModel)
            }
        }
    }
}
@Composable
fun HomeTopSection(navController: NavController,windowSize: WindowSize) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp)
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(horizontal = 25.dp), // Added padding inside for spacing
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left-aligned text
            Text(
                text = "Home",
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium
            )

                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications Icon",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(35.dp)
                        .clickable { navController.navigate("notification") }
                )


        }
    }
}
@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun CategoryScrollIndicator(scrollState: LazyListState, itemCount: Int, visibleItems: Int) {
    val progress by remember {
        derivedStateOf {
            val lastIndex = (itemCount - visibleItems).coerceAtLeast(0)

            val totalItems = scrollState.layoutInfo.totalItemsCount
            val lastVisibleIndex = scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val isAtEnd = lastVisibleIndex == totalItems - 1

            val itemOffsetFraction = scrollState.firstVisibleItemScrollOffset /
                    (scrollState.layoutInfo.visibleItemsInfo.firstOrNull()?.size?.toFloat() ?: 1f)

            val rawProgress = (scrollState.firstVisibleItemIndex.toFloat() + itemOffsetFraction) / lastIndex.toFloat()

            if (isAtEnd) 1f else rawProgress.coerceIn(0f, 1f)
        }
    }

    val trackWidth = 100.dp
    val handleWidth = 15.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(trackWidth)
                .height(8.dp)
                .background(Color.Gray, shape = RoundedCornerShape(50))
        ) {
            Box(
                modifier = Modifier
                    .width(handleWidth)
                    .height(8.dp)
                    .offset(x = progress * (trackWidth - handleWidth))
                    .background(Color(0xFF3CC0B0), shape = RoundedCornerShape(50))
            )
        }
    }
}





@Composable
fun CategoryRow(categories: List<Categories>, navController: NavController) {
    val windowSize = rememberWindowSizeClass()
    val cardSize = when (windowSize.width) {
        WindowType.SMALL -> 100.dp to 80.dp
        WindowType.MEDIUM -> 120.dp to 100.dp
        WindowType.LARGE -> 140.dp to 120.dp
    }

    val scrollState = rememberLazyListState()
    val itemCount = categories.size
    val visibleItems = 3

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 15.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Categories",
                fontSize = when (windowSize.width) {
                    WindowType.SMALL -> 18.sp
                    WindowType.MEDIUM -> 20.sp
                    WindowType.LARGE -> 22.sp
                },
                fontWeight = FontWeight(500),
            )
        }

        LazyRow(
            state = scrollState,
            modifier = Modifier
                .fillMaxWidth()
                .height(cardSize.second)
                .background(Color(0xFFECECEC)),
        ) {
            items(categories) { category ->
                CategoryItem(category) {
                    when (category.name) {
                        "Plumbing" -> navController.navigate("plumbing")
                        "Cleaning" -> navController.navigate("cleaning")
                        "Carpentry" -> navController.navigate("carpentry")
                        "Electrician" -> navController.navigate("electrician")
                        "AC Repair" -> navController.navigate("acrepair")
                        "Masonry" -> navController.navigate("masonry")
                        "Mechanics" -> navController.navigate("mechanics")
                        "Painter" -> navController.navigate("painting")
                        "Roofing" -> navController.navigate("roofing")
                        "Welding" -> navController.navigate("welding")
                    }
                }
            }
        }

        CategoryScrollIndicator(scrollState, itemCount, visibleItems)
    }
}




@Composable
fun TradesmanColumn(getResumesViewModel: GetResumesViewModel, navController: NavController, reportViewModel: ReportViewModel) {
    val windowSize = rememberWindowSizeClass()
    val resumeList = getResumesViewModel.resumePagingData.collectAsLazyPagingItems()

    var displayedResumes by remember { mutableStateOf<List<resumesItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        getResumesViewModel.refreshResumes()
    }

    LaunchedEffect(resumeList.itemSnapshotList) {
        displayedResumes = resumeList.itemSnapshotList.items
            .filter { it.ratings != null && it.id != null } // Add more validation
            .sortedByDescending { it.ratings }
            .take(5)
    }

    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 120.dp
        WindowType.MEDIUM -> 140.dp
        WindowType.LARGE -> 160.dp
    }

    val textSize = when (windowSize.width) {
        WindowType.SMALL -> 14.sp
        WindowType.MEDIUM -> 16.sp
        WindowType.LARGE -> 18.sp
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, end = 25.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Top-Rated",
            fontSize = 18.sp,
            fontWeight = FontWeight(500),
            modifier = Modifier.padding(top = 15.dp)
        )
        TextButton(onClick = { navController.navigate("alltradesman") }) {
            Text(
                text = "See All",
                color = Color.Gray,
                fontSize = 16.sp,
            )
        }
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(Color(0xFFECECEC)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            displayedResumes.forEach { resume ->
                TradesmanItem(
                    resumes = resume,
                    navController = navController,
                    cardHeight = cardHeight,
                    textSize = textSize,
                    reportViewModels = reportViewModel
                )
            }
        }
    }
}





@Composable
fun ExploreNow(windowSize: WindowSize) {
    val titleTextSize = when (windowSize.width) {
        WindowType.SMALL -> 14.sp
        WindowType.MEDIUM -> 18.sp
        WindowType.LARGE -> 20.sp
    }
    val textSize = when (windowSize.width) {
        WindowType.SMALL -> 16.sp
        WindowType.MEDIUM -> 20.sp
        WindowType.LARGE -> 22.sp
    }
    val buttonTextSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 16.sp
        WindowType.LARGE -> 18.sp
    }

    val imageSize = when (windowSize.width) {
        WindowType.SMALL -> 150.dp to 150.dp
        WindowType.MEDIUM -> 270.dp to 270.dp
        WindowType.LARGE -> 320.dp to 320.dp
    }

    val boxHeight = when (windowSize.width) {
        WindowType.SMALL -> 160.dp
        WindowType.MEDIUM -> 180.dp
        WindowType.LARGE -> 200.dp
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(boxHeight)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(myGradient2)
    ) {
        // DaguConnect Row positioned at the top-left
        Row(
            modifier = Modifier
                .fillMaxWidth()
                ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.logoexplore),
                contentDescription = "logo explore",
                modifier = Modifier.size(40.dp)
            )

            Text(
                text = "DaguConnect",
                color = Color.White,
                fontSize = titleTextSize,
                fontWeight = FontWeight.Light
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.dp, top = 20.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "What service do you need today?",
                    color = Color.White,
                    fontSize = textSize,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp))
                        .background(Color.Transparent)
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Explore Now",
                        color = Color.White,
                        fontSize = buttonTextSize,
                        fontWeight = FontWeight.Light,
                    )
                }
            }

            Image(
                painter = painterResource(R.drawable.explorebg),
                contentDescription = "Workers Images",
                modifier = Modifier
                    .size(imageSize.first, imageSize.second)
                    .padding(start = 12.dp)
            )
        }
    }
}

@Composable
fun CategoryItem(category: Categories, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() } // Prevent ripple effect

    Card(
        modifier = Modifier
            .size(120.dp, 100.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null // Removes the default ripple effect
            ) { onClick() },
        shape = CircleShape
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFECECEC)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .shadow(3.dp, shape = CircleShape)
                        .background(
                            color = Color.White,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(category.imageResId),
                        contentDescription = category.name,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Text(
                    text = category.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun TradesmanItem(resumes: resumesItem, navController: NavController, cardHeight: Dp, textSize: TextUnit, reportViewModels: ReportViewModel) {
    var selectedIndex by remember { mutableStateOf(-1) }
    var otherReason by remember { mutableStateOf("") }
    var reasonDescription by remember { mutableStateOf("") }
    val reportState by reportViewModels.reportState.collectAsState()
    val context = LocalContext.current

    val reasons = listOf(
        "Abusive or Harassing Behavior",
        "Inappropriate Content or Language",
        "Fraudulent Activity or Scam",
        "Poor Quality of Service",
        "Unprofessional Conduct",
        "Safety Concerns",
        "Others"
    )
    val windowSize = rememberWindowSizeClass()

    val iconSize = when (windowSize.width) {
        WindowType.SMALL -> 25.dp
        WindowType.MEDIUM -> 35.dp
        WindowType.LARGE -> 45.dp
    }
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
    var showReportDialog by remember { mutableStateOf(false) }
    var reportDocument by remember { mutableStateOf<Uri?>(null) }
    var showMenu by remember { mutableStateOf(false) }


    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { reportDocument = it } }

    // Permission launcher for storage access


    LaunchedEffect(showReportDialog) {
        if(showReportDialog){
            selectedIndex = -1
            otherReason = ""
            reasonDescription = ""
            reportDocument = null
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .clickable { navController.navigate("booknow/${resumes.id}") }, //implementation here
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)


        ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),

            contentAlignment = Alignment.CenterStart
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = resumes.profilePic,
                    contentDescription = "Tradesman Image",
                    modifier = Modifier
                        .size(cardHeight - 20.dp)
                        .padding(start = 10.dp)
                )
                Column(
                    modifier = Modifier
                        .size(250.dp, 100.dp)
                        .padding(start = 24.dp)
                )
                {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = resumes.tradesmanFullName,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = taskTextSize,
                        )

                        // Menu Icon
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
                    Text(
                        text = resumes.specialty
                            .replace("_", " "),
                        color = Color.Black,
                        fontSize = taskTextSize,
                        )
                    Row(modifier = Modifier.size(185.dp, 110.dp)) {
                        Box(
                            modifier = Modifier
                                .size(70.dp, 50.dp)
                                .padding(top = 15.dp, end = 5.dp)
                                .background(
                                    color = (Color(0xFFF5F5F5)),
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            Text(
                                text = "P${resumes.workFee}/hr",
                                fontSize = smallTextSize,
                                modifier = Modifier.padding(top = 5.dp, start = 8.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(70.dp, 50.dp)
                                .padding(top = 15.dp, start = 10.dp, end = 10.dp)
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
                                    resumes.ratings == 0f -> "0"
                                    else -> String.format("%.1f", resumes.ratings)
                                },
                                fontSize = smallTextSize,
                                modifier = Modifier.padding(top = 5.dp, start = 28.dp)
                            )
                        }
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
                                        reportViewModels.report(selectedReason, reasonDescription, reportDocument!!,context,resumes.userid)
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
                                    is ReportViewModel.ReportState.Loading -> {
                                        //do nothing
                                    }
                                    is ReportViewModel.ReportState.Success -> {
                                        val responsereport = report.data?.message
                                        Toast.makeText(context, responsereport, Toast.LENGTH_SHORT).show()

                                        reportViewModels.resetState()
                                        // Close the dialog
                                        showReportDialog = false
                                    }
                                    is ReportViewModel.ReportState.Error -> {
                                        val errorMessage = report.message
                                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                        showReportDialog = true
                                        reportViewModels.resetState()
                                    }
                                    else -> Unit
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
fun UploadFieldScreenShot(label: String, uri: Uri?, fileType: String = "image", onUploadClick: () -> Unit, onViewClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .padding(top = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            Modifier
                .clickable { if (uri != null) onViewClick() }
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${uri?.lastPathSegment ?: "No file uploaded"}",
                fontSize = 14.sp,
                color = if (uri != null) Color.Blue else Color.Gray
            )
            if (uri != null) {
                Icon(
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = "View File",
                    tint = Color.Blue,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .clickable { onUploadClick() }
                .background(Color(0xFF3E5CE1), RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.upload_ic),
                    contentDescription = "Add File",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Add File", fontSize = 10.sp, color = Color.White)
            }
        }
    }
    Text(
        text = "Accepted file types: ${if (fileType == "image") ".jpg, .jpeg, .png" else ".pdf"}",
        fontStyle = FontStyle.Italic,
        fontSize = 10.sp,
        color = Color.Gray,
        modifier = Modifier.padding(start = 18.dp)
    )
}
fun openScreenShot(context: Context, uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, context.contentResolver.getType(uri))
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    try {
        context.startActivity(intent)
        Log.d("openFile", "Attempting to open URI: $uri")

    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No app available to open this file", Toast.LENGTH_SHORT).show()
    }
}