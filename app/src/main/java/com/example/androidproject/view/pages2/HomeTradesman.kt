package com.example.androidproject.view.pages2

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.androidproject.R
import com.example.androidproject.view.Tradesman
import com.example.androidproject.view.WindowSize
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass

@Composable
fun HomeTradesman( modifier: Modifier, navController: NavController){
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
                                0 -> TopMatches(navController)
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
    var searchQuery by remember { mutableStateOf("") }
    val iconSize = when (windowSize.width) {
        WindowType.SMALL -> 32.dp
        WindowType.MEDIUM -> 34.dp
        WindowType.LARGE -> 36.dp
    }

    val textSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .size(90.dp)
            .background(Color.White)
            .padding(horizontal = 25.dp)
    ) {
        Row(
            modifier = Modifier
                .width(280.dp)
                .height(70.dp)
                .padding(top = 20.dp)
                .background(
                    Color(0xFFFFFFFF),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    1.dp,
                    Color(0xFF3CC0B0),
                    shape = RoundedCornerShape(8.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color(0xFF3CC0B0),
                modifier = Modifier
                    .size(iconSize)
                    .padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.weight(1f),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, color = Color(0xFF3CC0B0)),
                decorationBox = { innerTextField ->
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Search for services or tradespeople...",
                            style = androidx.compose.ui.text.TextStyle(fontSize = textSize, color = Color.Gray)
                        )
                    }
                    innerTextField()
                }
            )
        }

        // Notification and Message Icons outside the search field
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications Icon",
                tint = Color(0xFF3CC0B0),
                modifier = Modifier
                    .size(iconSize)
            )
            Icon(
                imageVector = Icons.Default.Message,
                contentDescription = "Message Icon",
                tint = Color(0xFF3CC0B0),
                modifier = Modifier
                    .size(iconSize)
                    .clickable {
                        navController.navigate("message_screen")

                    }
            )
        }
    }
}
@Composable
fun TopMatches(navController: NavController){
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
            .padding(top = 5.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(tradesman.size) { index ->
            val trade = tradesman[index]
            TopMatchesItem(trade,navController)
        }
    }
}


@Composable
fun TopMatchesItem(trade: Tradesman, navController: NavController){
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
                Image(painter = painterResource(R.drawable.bookmark),
                    contentDescription = "Bookmark",
                    Modifier.size(32.dp)
                        .clickable {  }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color.Gray),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Description of the Plumbing Repair service", fontSize = 12.sp)
                    Text(text = "Est. Budget: P200/hr", fontSize = 12.sp)
                    Text(text = "Location: Lingayen, Pangasinan", fontSize = 12.sp)
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
            .padding(top = 5.dp)

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
                Image(painter = painterResource(R.drawable.bookmark),
                    contentDescription = "Bookmark",
                    Modifier.size(32.dp)
                        .clickable {  }
                )
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