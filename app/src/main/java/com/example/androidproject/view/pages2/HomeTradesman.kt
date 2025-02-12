package com.example.androidproject.view.pages2

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.androidproject.R
import com.example.androidproject.ViewModelSetups
import com.example.androidproject.model.GetJobs
import com.example.androidproject.model.JobsResponse
import com.example.androidproject.view.Tradesman
import com.example.androidproject.view.WindowSize
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.viewmodel.jobs.GetJobsViewModel

@Composable
fun HomeTradesman( modifier: Modifier, navController: NavController, getJobsViewModel: GetJobsViewModel){

    val windowSize = rememberWindowSizeClass()
    val textSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }
    var selectedTabIndex by remember { mutableStateOf(0) }

    // Tab titles
    val tabTitles = listOf("Top Matches", "Recent Posted Jobs")
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

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
                                            modifier = Modifier.fillMaxWidth(), // Fills the width inside each Tab
                                            color = if (selectedTabIndex == index) Color.Black else Color.Gray
                                        )
                                    },
                                    modifier = Modifier.weight(1f) // Ensures equal distribution across the width
                                )
                            }
                        }
                        // Content changes based on the selected tab
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFFD9D9D9))

                        ) {
                            when (selectedTabIndex) {
                                0 -> TopMatches(navController, getJobsViewModel)
                                1 -> RecentJobs(navController)

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
    Row(
        modifier = Modifier
            .padding(top = 10.dp, start = 25.dp, end = 25.dp)
            .fillMaxWidth()
            .height(70.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left-aligned text
        Text(
            text = "Schedule",
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium
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
            Icon(
                imageVector = Icons.Default.Message,
                contentDescription = "Message Icon",
                tint = Color(0xFF3CC0B0),
                modifier = Modifier
                    .size(32.dp)
                    .clickable { navController.navigate("message_screen") }
            )
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TopMatches(navController: NavController, getJobsViewModel: GetJobsViewModel) {
    val jobsList = getJobsViewModel.jobsPagingData.collectAsLazyPagingItems()


    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFD9D9D9))) {
                LazyColumn(
                    modifier = Modifier.padding(bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(jobsList.itemCount) { index ->
                        val job = jobsList[index]
                        if (job != null) {
                            TopMatchesItem(job, navController)
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
                                CircularProgressIndicator()
                            }
                        }
                    }
        }
    }
}


@Composable
fun TopMatchesItem(getJobs: GetJobs, navController: NavController){
    val getJobsDate = ViewModelSetups.formatDateTime(getJobs.createdAt)
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
                Image(
                    painter = painterResource(id = R.drawable.pfp),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Text(text = getJobs.clientFullname,
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight(500),
                    modifier = Modifier.padding(start = 20.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Column {
                    Text(text = "Looking for ${getJobs.jobType}", fontSize = 24.sp, color = Color.Black, fontWeight = FontWeight(500))
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
                    Text(text = "Est. Budget: ${getJobs.salary} pesos", fontSize = 12.sp)
                    Text(text = "Location: ${getJobs.location}", fontSize = 12.sp)
                    TextButton(onClick = {
                    }) {
                        Text(text = "5 Applicant",fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight(500),
                            style = TextStyle(textDecoration = TextDecoration.Underline),
                            modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun RecentJobs(navController: NavController){
    val tradesman = listOf(
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9D9D9))
             .padding(bottom = 100.dp)


        ,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(tradesman.size) { index ->
            val trade = tradesman[index]
            RecentJobsItem(trade,navController)
        }
    }
}

@Composable
fun RecentJobsItem(trade: Tradesman, navController: NavController){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("tradesmanapply")

            },
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.pfp),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Text(text = trade.username,
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight(500),
                    modifier = Modifier.padding(start = 20.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Column {
                    Text(text = trade.category, fontSize = 24.sp, color = Color.Black, fontWeight = FontWeight(500))
                    Text(text = "Posted on 1 min ago - Active ")

                }
                Spacer(modifier = Modifier.weight(1f))

            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color.Gray),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Description of the Plumbing Repair service", fontSize = 12.sp)
                    Text(text = "Est. Budget: P200/hr", fontSize = 12.sp)
                    Text(text = "Location: Lingayen, Pangasinan", fontSize = 16.sp)


                }
            }
        }
    }
}