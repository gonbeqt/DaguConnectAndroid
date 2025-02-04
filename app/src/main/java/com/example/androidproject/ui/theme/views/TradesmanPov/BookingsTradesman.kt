package com.example.androidproject.ui.theme.views.TradesmanPov

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidproject.R
import com.example.androidproject.ui.theme.views.Tradesman
import com.example.androidproject.ui.theme.views.WindowSize
import com.example.androidproject.ui.theme.views.WindowType
import com.example.androidproject.ui.theme.views.rememberWindowSizeClass

@Composable
fun BookingsTradesman(modifier: Modifier = Modifier, navController: NavController) {
    val windowSize = rememberWindowSizeClass()
    val textSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }
    var selectedTabIndex by remember { mutableStateOf(0) }

    // Tab titles
    val tabTitles = listOf("All", "Pending", "Active", "Completed", "Cancelled")
    Box (
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            BookingsTradesmanTopSection(navController,windowSize)
            Column(modifier = Modifier.fillMaxSize()) {
                // Tabs (Fixed Choices)
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.fillMaxWidth()
                    ,
                    edgePadding = 5.dp
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title, fontSize = textSize) },
                            modifier = Modifier.background(Color.White)
                        )
                    }
                }

                // Content changes based on the selected tab
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFD9D9D9))

                        .padding(16.dp)
                ) {
                    when (selectedTabIndex) {
                        0 -> AllBookingsTradesmanContent()
                        1 -> PendingBookingsTradesmanContent(navController)
                        2 -> ActiveBookingsTradesmanContent()
                        3 -> CompletedBookingsTradesmanContent(navController)
                        4 -> CancelledBookingsTradesmanContent(navController)
                    }
                }
            }
        }

    }

}
@Composable
fun BookingsTradesmanTopSection(navController: NavController, windowSize: WindowSize) {
    val fontSize = when (windowSize.width) {
        WindowType.SMALL -> 24.sp
        WindowType.MEDIUM -> 28.sp
        WindowType.LARGE -> 32.sp
    }

    val iconSize = when (windowSize.width) {
        WindowType.SMALL -> 32.dp
        WindowType.MEDIUM -> 34.dp
        WindowType.LARGE -> 36.dp
    }

    val horizontalSpacing = when (windowSize.width) {
        WindowType.SMALL -> 16.dp
        WindowType.MEDIUM -> 20.dp
        WindowType.LARGE -> 24.dp
    }

    Row(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
            .height(70.dp)

        ,
        horizontalArrangement = Arrangement.spacedBy(100.dp),
    ) {
        Text(text="My Bookings ",
            fontSize = fontSize,
            fontWeight =
            FontWeight(500),
            modifier = Modifier.padding(16.dp),



            )
        Row (modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)){
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications Icon",
                tint = Color(0xFF3CC0B0),
                modifier = Modifier
                    .size(32.dp)

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
fun AllBookingsTradesmanContent() {
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
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))

        ,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(tradesman.size) { index ->
            val trade = tradesman[index]
            AllTradesmanItem(trade)
        }
    }
}


@Composable
fun PendingBookingsTradesmanContent(navController: NavController) {
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
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))

        ,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(tradesman.size) { index ->
            val trade = tradesman[index]
            PendingTradesmanItem(trade,navController)
        }
    }
}

@Composable
fun ActiveBookingsTradesmanContent() {
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
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))

        ,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(tradesman.size) { index ->
            val trade = tradesman[index]
            AllTradesmanItem(trade)
        }
    }
}

@Composable
fun CompletedBookingsTradesmanContent(navController: NavController) {
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
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))

        ,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(tradesman.size) { index ->
            val trade = tradesman[index]
            CompletedItem(trade, navController )
        }
    }
}
@Composable
fun CancelledBookingsTradesmanContent(navController: NavController) {
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
            .fillMaxHeight()
            .size(420.dp)
            .background(Color(0xFFD9D9D9))
        ,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(tradesman.size) { index ->
            val trade = tradesman[index]
            CancelledItem(trade, navController )
        }
    }
}



//Design For Items
@Composable
fun AllTradesmanItem(trade: Tradesman) {
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 390.dp to 190.dp
        WindowType.MEDIUM -> 400.dp to 200.dp
        WindowType.LARGE -> 410.dp to 210.dp
    }

    Card(
        modifier = Modifier
            .size(cardHeight.first, cardHeight.second)
            .clickable { }, // Add implementation for click if needed
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Tradesman image
                Image(
                    painter = painterResource(trade.imageResId),
                    contentDescription = "Tradesman Image",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )

                // Tradesman details
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = trade.username,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Service: Plumbing Repair",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Date: Jan 15, 2025",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Time: 10:00 AM",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Location: 123 Elm Street",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Payment: Pending",
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }

                // Status or additional label
                Text(
                    text = "All",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 12.dp)
                )
            }
        }
    }
}


@Composable
fun PendingTradesmanItem(trade: Tradesman, navController: NavController) {
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 390.dp to 190.dp
        WindowType.MEDIUM -> 400.dp to 200.dp
        WindowType.LARGE -> 410.dp to 210.dp
    }

    Card(
        modifier = Modifier
            .size(cardHeight.first, cardHeight.second)
            .clickable { }, // Add implementation for click if needed
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Tradesman image
                Image(
                    painter = painterResource(trade.imageResId),
                    contentDescription = "Tradesman Image",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )

                // Tradesman details
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = trade.username,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Service: Plumbing Repair",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Date: Jan 15, 2025",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Time: 10:00 AM",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Location: 123 Elm Street",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Payment: Pending",
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }

                // Status or additional label
                Text(
                    text = "Pending",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 12.dp)
                )
            }
        }
    }
}
@Composable
fun CompletedItem(trade: Tradesman, navController: NavController) {
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 390.dp to 190.dp
        WindowType.MEDIUM -> 400.dp to 200.dp
        WindowType.LARGE -> 410.dp to 210.dp
    }

    Card(
        modifier = Modifier
            .size(cardHeight.first, cardHeight.second)
            .clickable { }, // Add implementation for click if needed
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Tradesman image
                Image(
                    painter = painterResource(trade.imageResId),
                    contentDescription = "Tradesman Image",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )

                // Tradesman details
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = trade.username,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Service: Plumbing Repair",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Date: Jan 15, 2025",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Time: 10:00 AM",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Location: 123 Elm Street",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Payment: Paid",
                        color = Color.Blue,
                        fontSize = 14.sp
                    )
                }

                // Status or additional label
                Text(
                    text = "Completed",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 12.dp)
                )
            }
        }
    }
}


@Composable
fun CancelledItem(trade: Tradesman, navController: NavController) {
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 390.dp to 190.dp
        WindowType.MEDIUM -> 400.dp to 200.dp
        WindowType.LARGE -> 410.dp to 210.dp
    }

    Card(
        modifier = Modifier
            .size(cardHeight.first, cardHeight.second)
            .clickable { }, // Add implementation for click if needed
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Tradesman image
                Image(
                    painter = painterResource(trade.imageResId),
                    contentDescription = "Tradesman Image",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )

                // Tradesman details
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = trade.username,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Service: Plumbing Repair",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Date: Jan 15, 2025",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Time: 10:00 AM",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Location: 123 Elm Street",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                }

                // Status or additional label
                Text(
                    text = "Cancelled",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 12.dp)
                )
            }
        }
    }
}
