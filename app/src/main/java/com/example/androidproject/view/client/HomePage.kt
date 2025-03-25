package com.example.androidproject.view.client

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.gestures.scrollBy
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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.data.preferences.AccountManager
import com.example.androidproject.data.WebSocketManager
import com.example.androidproject.data.WebSocketNotificationManager
import com.example.androidproject.model.client.resumesItem
import com.example.androidproject.view.Categories
import com.example.androidproject.view.WindowSize
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.extras.LoadingUI
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.view.theme.myGradient4
import com.example.androidproject.viewmodel.Resumes.GetResumesViewModel
import com.example.androidproject.viewmodel.report.ReportTradesmanViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavController, getResumesViewModel: GetResumesViewModel, reportTradesmanViewModel: ReportTradesmanViewModel) {
    Log.i("Screen" , "HomeScreen")
    val windowSize = rememberWindowSizeClass()
    val context = LocalContext.current

    // ✅ Correctly remember LazyListState for scrolling detection
    val listState = rememberLazyListState()

    val isScrolled by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 50 }
    }

    // ✅ Smoothly animate colors based on scroll position
    val backgroundColor by animateColorAsState(
        targetValue = if (isScrolled) Color.White else Color(0xFF42C2AE),
        animationSpec = tween(durationMillis = 300),
        label = "backgroundColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (isScrolled) Color.Black else Color.White,
        animationSpec = tween(durationMillis = 300),
        label = "textColor"
    )

    val boxColor by animateColorAsState(
        targetValue = if (isScrolled) Color(0xFFEDEFEF) else Color(0xFF42C2AE),
        animationSpec = tween(durationMillis = 300),
        label = "boxColor"
    )

    val userId = AccountManager.getAccount()?.id
    LaunchedEffect(Unit) {
        WebSocketManager.connect(userId.toString())
        WebSocketNotificationManager.initialize(context, userId.toString())
    }
    val poppinsFont = FontFamily(
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_bold, FontWeight.Bold)
    )


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
            .background(Color(0xFFEDEFEF))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(boxColor, RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp))
        ){}
        Column(modifier = Modifier.fillMaxSize()) {

            // Provide navController to the SearchField
            HomeTopSection(navController,windowSize, backgroundColor, textColor, poppinsFont )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState // ✅ Ensure listState is used properly
            ) {
                item { Spacer(modifier = Modifier
                    .height(20.dp)) }
                item { ExploreNow(windowSize,
                    poppinsFont = FontFamily(
                        Font(R.font.poppins_regular, FontWeight.Normal),
                        Font(R.font.poppins_medium, FontWeight.Medium),
                        Font(R.font.poppins_bold, FontWeight.Bold)
                    ),
                    listState = listState
                ) }
                item { Spacer(modifier = Modifier
                    .height(20.dp)
                ) }
                item { CategoryRow(categories, navController,
                    poppinsFont = FontFamily(
                    Font(R.font.poppins_regular, FontWeight.Normal),
                    Font(R.font.poppins_medium, FontWeight.Medium),
                    Font(R.font.poppins_bold, FontWeight.Bold)
                ))
                }
                item { Spacer(modifier = Modifier
                    .height(20.dp)
                ) }
                item { TradesmanColumn(getResumesViewModel, navController, reportTradesmanViewModel,
                         poppinsFont = FontFamily(
                        Font(R.font.poppins_regular, FontWeight.Normal),
                        Font(R.font.poppins_medium, FontWeight.Medium),
                        Font(R.font.poppins_bold, FontWeight.Bold)
                )
                ) }
            }
        }

    }
}
@Composable
fun HomeTopSection(navController: NavController,windowSize: WindowSize, bgColor: Color, textColor: Color, poppinsFont: FontFamily) {

    val firstName = AccountManager.getAccount()?.firstName
    val createdAt = AccountManager.getAccount()?.createdAt
    val newUser = LocalDate.now()
    val createdAtDate = createdAt?.let { creationTime ->
        try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val dateTime = LocalDateTime.parse(creationTime, formatter)
            dateTime.toLocalDate()
        } catch (e: Exception) {
            null
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp)
            .background(bgColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(horizontal = 25.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                // Left-aligned text
                Text(
                    text = "Hello $firstName,",
                    fontSize = 14.sp,
                    color = textColor,
                    fontWeight = FontWeight.Light,
                    fontFamily = poppinsFont
                )


                if (createdAtDate == newUser) {
                    Text(
                        text = "Let's get started!",
                        fontSize = 24.sp,
                        color = textColor,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Text(
                        text = "Welcome back!",
                        fontSize = 20.sp,
                        color = textColor,
                        fontWeight = FontWeight.Medium,
                        fontFamily = poppinsFont
                    )
                }

            }
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications Icon",
                tint = textColor,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { navController.navigate("notification") }
            )
        }
    }
}

@Composable
fun FontFamily(x0: Font, x1: Font, x2: Font) {
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

    val trackWidth = 50.dp
    val handleWidth = 15.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
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
fun CategoryRow(categories: List<Categories>, navController: NavController, poppinsFont: FontFamily) {
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
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Categories",
                fontSize = when (windowSize.width) {
                    WindowType.SMALL -> 14.sp
                    WindowType.MEDIUM -> 16.sp
                    WindowType.LARGE -> 18.sp
                },
                fontWeight = FontWeight(500),
                fontFamily = poppinsFont
            )
        }
        LazyRow(
            state = scrollState,
            modifier = Modifier
                .fillMaxWidth()
                .height(cardSize.second)
                .background(Color.Transparent),
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
fun TradesmanColumn(
    getResumesViewModel: GetResumesViewModel,
    navController: NavController,
    reportTradesmanViewModel: ReportTradesmanViewModel,
    poppinsFont: FontFamily
) {
    val windowSize = rememberWindowSizeClass()
    val resumeList = getResumesViewModel.resumePagingData.collectAsLazyPagingItems()
    val loadState = resumeList.loadState
    var displayedResumes by remember { mutableStateOf<List<resumesItem>>(emptyList()) }
    var showLoading by remember { mutableStateOf(false) } // State to control LoadingUI visibility
    val context = LocalContext.current
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // Function to check network connectivity
    fun checkNetworkConnectivity(connectivityManager: ConnectivityManager): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && (
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                )
    }
    val isConnected = remember { mutableStateOf(checkNetworkConnectivity(connectivityManager)) }


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
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Top-Rated",
            fontSize = when (windowSize.width) {
                WindowType.SMALL -> 14.sp
                WindowType.MEDIUM -> 16.sp
                WindowType.LARGE -> 18.sp
            },
            fontWeight = FontWeight(500),
            fontFamily = poppinsFont
        )

        Text(
            modifier = Modifier.clickable {
                navController.navigate("alltradesmen")
            },
            text = "See All",
            color = Color.Gray,
            fontSize = when (windowSize.width) {
                WindowType.SMALL -> 14.sp
                WindowType.MEDIUM -> 16.sp
                WindowType.LARGE -> 18.sp
            },
            fontWeight = FontWeight.Normal,
            fontFamily = poppinsFont
        )

    }

    when {
        // Initial loading state
        loadState.refresh is LoadState.Loading && resumeList.itemCount == 0 -> {
            LoadingUI()
        }
        else -> {
            if (!isConnected.value) {
                if (showLoading) {
                        LoadingUI()
                        LaunchedEffect(Unit) {
                            delay(1500) // Show LoadingUI for 1.5 seconds
                            isConnected.value = checkNetworkConnectivity(connectivityManager)
                            showLoading = false // Hide LoadingUI after delay
                            if (isConnected.value) {
                                resumeList.refresh() // Refresh data after reconnecting
                            }
                        }

                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
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
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .clickable {
                                        showLoading = true // Show LoadingUI on retry
                                    }
                                    .background(Color(0xFF42C2AE), RoundedCornerShape(8.dp))
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
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .background(Color.Transparent)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .background(Color(0xFFEDEFEF)),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (displayedResumes.isEmpty() && resumeList.itemCount > 0) {
                            // Fallback to ensure data is displayed
                            displayedResumes = resumeList.itemSnapshotList.items
                                .filter { it.ratings != null && it.id != null }
                                .sortedByDescending { it.ratings }
                                .take(5)
                        }
                        displayedResumes.forEach { resume ->
                            TradesmanItem(
                                resumes = resume,
                                navController = navController,
                                cardHeight = cardHeight,
                                textSize = textSize,
                                reportTradesmanViewModels = reportTradesmanViewModel
                            )
                        }
                    }
                }
            }
        }
    }


}





@Composable
fun ExploreNow(windowSize: WindowSize, poppinsFont: FontFamily,listState: LazyListState) {
    val titleTextSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
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
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(boxHeight)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(myGradient4)
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
                fontWeight = FontWeight.Light,
                fontFamily = poppinsFont
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
                    .padding(start = 20.dp, top = 16.dp),
            ) {
                Text(
                    text = "What service do you need today?",
                    color = Color.White,
                    fontSize = textSize,
                    fontWeight = FontWeight.Medium,
                    fontFamily = poppinsFont
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .clickable {
                        coroutineScope.launch {
                            // Smoothly scroll to TradesmanColumn (index 4)
                            val steps = 8
                            val durationPerStep = 55L
                            val targetIndex = 4
                            val currentOffset = listState.firstVisibleItemScrollOffset.toFloat()
                            val targetOffset = 1000f

                            val stepSize = (targetOffset - currentOffset) / steps

                            repeat(steps) {
                                listState.scrollBy(stepSize)
                                delay(durationPerStep)
                            }
                            // Ensure final position
                            listState.scrollToItem(index = targetIndex)

                        }
                    }
                                .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp))
                        .background(Color.Transparent)
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Explore Now",
                        color = Color.White,
                        fontSize = buttonTextSize,
                        fontWeight = FontWeight.Light,
                        fontFamily = poppinsFont
                    )
                }
            }

            Image(
                painter = painterResource(R.drawable.explorebg),
                contentDescription = "Workers Images",
                modifier = Modifier
                    .size(imageSize.first, imageSize.second)
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
                .background(Color(0xFFEDEFEF)),
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
                            color = Color(0xFFFFD9C1),
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
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradesmanItem(resumes: resumesItem, navController: NavController, cardHeight: Dp, textSize: TextUnit, reportTradesmanViewModels: ReportTradesmanViewModel) {

    val reportState by reportTradesmanViewModels.reportState.collectAsState()
    var selectedIndex by remember { mutableIntStateOf(-1) }
    var otherReason by remember { mutableStateOf("") }
    var reasonDescription by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showReportSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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
        WindowType.SMALL -> 16.dp
        WindowType.MEDIUM -> 24.dp
        WindowType.LARGE -> 32.dp
    }
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
    var reportSubmissionKey by remember { mutableStateOf<Long?>(null) } // Unique key for each submission


    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { reportDocument = it } }

    // Permission launcher for storage access


    LaunchedEffect(showReportSheet) {
        if (showReportSheet) {
            selectedIndex = -1
            otherReason = ""
            reasonDescription = ""
            reportDocument = null
        }
    }

    // Handle report state changes with a unique key to prevent multiple triggers
    LaunchedEffect(reportState, reportSubmissionKey) {
        if (reportSubmissionKey == null) return@LaunchedEffect // Skip if no submission yet
        when (val report = reportState) {
            is ReportTradesmanViewModel.ReportState.Success -> {
                val responseReport = report.data?.message
                Toast.makeText(context, responseReport, Toast.LENGTH_SHORT).show()
                Log.d("ReportState", "Success: $responseReport")
                showReportSheet = false
                reportSubmissionKey = null // Reset key after handling
                delay(1000)
                reportTradesmanViewModels.resetState()
            }

            is ReportTradesmanViewModel.ReportState.Error -> {
                val errorMessage = report.message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                Log.d("ReportState", "Error: $errorMessage")
                showReportSheet = true
                reportSubmissionKey = null // Reset key after handling
                delay(1000)
                reportTradesmanViewModels.resetState()
            }

            else -> Unit
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .clickable { navController.navigate("booknow/${resumes.id}") }, //implementation here
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(1.dp)


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
                            fontSize = nameTextSize,
                        )

                        // Menu Icon
                        Box {
                            Icon(
                                painter = painterResource(R.drawable.meatball_ic),
                                contentDescription = "Report Icon",
                                modifier = Modifier
                                    .padding(end = 10.dp)
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
                                        showReportSheet = true
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
                    Row(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(70.dp, 30.dp)
                                .background(
                                    color = (Color(0xFFF5F5F5)),
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "P${resumes.workFee}/hr",
                                fontSize = smallTextSize
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(50.dp, 30.dp)
                                .background(
                                    color = (Color(0xFFF5F5F5)),
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center

                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Start Icon",
                                    tint = Color(0xFFFFA500),
                                    modifier = Modifier
                                        .size(iconSize)
                                )
                                Text(
                                    when {
                                        resumes.ratings == 0f -> "0"
                                        else -> String.format("%.1f", resumes.ratings)
                                    },
                                    fontSize = smallTextSize,
                                )
                            }
                        }
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
                                reportTradesmanViewModels.report(selectedReason, reasonDescription, reportDocument!!,context,resumes.userid)
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