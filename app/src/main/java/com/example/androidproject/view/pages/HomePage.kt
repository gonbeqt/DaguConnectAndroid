package com.example.androidproject.view.pages

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.model.client.resumesItem
import com.example.androidproject.view.Categories
import com.example.androidproject.view.Tradesman
import com.example.androidproject.view.WindowSize
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.view.theme.myGradient
import com.example.androidproject.view.theme.myGradient2
import com.example.androidproject.viewmodel.Resumes.GetResumesViewModel


@Composable
fun HomeScreen( modifier: Modifier = Modifier,navController: NavController,getResumesViewModel: GetResumesViewModel) {
    Log.i("Screen" , "HomeScreen")
    val windowSize = rememberWindowSizeClass()


    val categories = listOf(
        Categories(R.drawable.carpentry, "Carpentry"),
        Categories(R.drawable.painting, "Painting"),
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
                TradesmanColumn(getResumesViewModel,navController)
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
            .padding(top = 10.dp)
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
                    modifier = Modifier.size(35.dp)
                        .clickable { navController.navigate("notification") }
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
    val totalDots = (itemCount - visibleItems).coerceAtLeast(1)
    val currentIndex by remember {
        derivedStateOf { scrollState.firstVisibleItemIndex }
    }

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
                        "Painting" -> navController.navigate("painting")
                        "Roofing" -> navController.navigate("roofing")
                        "Welding" -> navController.navigate("welding")
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(totalDots) { index ->
                val animatedSize by animateDpAsState(
                    targetValue = if (index == currentIndex) 20.dp else 8.dp,
                    animationSpec = tween(durationMillis = 300)
                )
                val animatedColor by animateColorAsState(
                    targetValue = if (index == currentIndex) Color(0xFF3CC0B0) else Color(0xFFBBF7D0),
                    animationSpec = tween(durationMillis = 300)
                )

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(animatedSize)
                        .background(animatedColor, shape = CircleShape)
                )
            }
        }
    }
}



@Composable
fun TradesmanColumn(getResumesViewModel: GetResumesViewModel, navController: NavController) {
    val windowSize = rememberWindowSizeClass()
    val resumeList = getResumesViewModel.resumePagingData.collectAsLazyPagingItems()

    var displayedResumes by remember { mutableStateOf<List<resumesItem>>(emptyList()) }


    LaunchedEffect(Unit) {
        getResumesViewModel.invalidatePagingSource()
    }

    LaunchedEffect(resumeList.itemSnapshotList) {
        displayedResumes = resumeList.itemSnapshotList.items
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
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
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
fun CategoryItem(category: Categories,onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(120.dp, 100.dp)

            .clickable { onClick() }, //implementation here
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
                            color = (Color.White),
                            shape = CircleShape
                        )
                        ,

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
fun TradesmanItem(resumes: resumesItem, navController: NavController, cardHeight: Dp, textSize: TextUnit) {
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
    var showMenu by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var reportText by remember { mutableStateOf("") }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .clickable { navController.navigate("booknow/${resumes.id}")}, //implementation here
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
                    model = resumes.profilepic,
                    contentDescription = "Tradesman Image",
                    modifier = Modifier
                        .size(cardHeight - 20.dp)
                        .padding(start = 10.dp)
                )
                Column(
                    modifier = Modifier
                        .size(250.dp, 100.dp)
                        .padding(start = 10.dp)
                )
                {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = resumes.tradesmanfullname,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = nameTextSize,
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
                                    text = { Text("Report") },
                                    onClick = {
                                        showMenu = false
                                        showReportDialog = true
                                    }
                                )
                            }
                        }
                    }
                    Text(
                        text = "${resumes.specialties}"
                            .replace("[", "")  // Remove opening bracket
                            .replace("]", ""),  // Remove closing bracket ,
                        color = Color.Black,
                        fontSize = taskTextSize,
                        )
                    Row(modifier = Modifier.size(185.dp, 110.dp)) {
                        Box(
                            modifier = Modifier
                                .size(70.dp, 50.dp)
                                .padding(top = 15.dp, end = 5.dp)
                                .background(
                                    color = (Color(0xFFFFF2DD)),
                                    shape =RoundedCornerShape(12.dp)
                                )
                        ) {
                            Text(
                                text = "P${resumes.workfee}/hr",
                                fontSize = smallTextSize,
                                modifier = Modifier.padding(top = 5.dp, start = 8.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(70.dp, 50.dp)
                                .padding(top = 15.dp, start = 10.dp, end = 10.dp)
                                .background(
                                    color = (Color(0xFFFFF2DD)),
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
                                text = "4",
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
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = { Text("Report Tradesman") },
            text = {
                Column {
                    Text("Please enter a reason for reporting this tradesman:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = reportText,
                        onValueChange = { reportText = it },
                        placeholder = { Text("Enter report reason...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (reportText.isNotBlank()) {
                            println("Report submitted: $reportText")
                            showReportDialog = false
                        }
                    }
                ) {
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReportDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
