package com.example.androidproject.view.pages

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidproject.R
import com.example.androidproject.view.Tradesman
import com.example.androidproject.view.theme.myGradient
import com.example.androidproject.view.theme.myGradient3
import com.example.androidproject.view.theme.myGradient4
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun ScheduleScreen(modifier: Modifier = Modifier,navController: NavController) {
    Log.i("Screen" , "ScheduleScreen")
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val tradesman = listOf(
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark)
    )
    Column(modifier = Modifier.fillMaxSize()
        .background(Color.White)) {
        //calendar section showing the month, days, and selected date
        ScheduleTopSection(navController )
        CalendarSection(
            currentMonth = currentMonth,
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it },
            onMonthChange = { currentMonth = it }
        )

        // Selected date display
        Spacer(modifier = Modifier.height(16.dp))
        Column(Modifier.fillMaxWidth() .padding(16.dp)
        ) {
            Text(
                text = selectedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy")),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
                , color = Color.Black
            )
            Text(
                text = selectedDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .size(420.dp)
                .background(Color.White)
                .padding(bottom = 70.dp)
            ,
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(tradesman.size) { index ->
                val trade = tradesman[index]
                PlumbingRepairCard(trade)
            }
        }
    }
}



@Composable
fun ScheduleTopSection(navController: NavController){
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
                    .clickable { navController.navigate("notification") }
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
@Composable
fun CalendarSection(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChange: (YearMonth) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp), // Set rounded corners
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = myGradient4) // Apply gradient
                .padding(16.dp) // Padding inside the card
        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            //navigation for month (e.g jan to feb)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onMonthChange(currentMonth.minusMonths(1)) }) {
                    Icon(
                        Icons.Default.ArrowBackIos,
                        contentDescription = "Previous Month",
                        tint = Color.White
                    )
                }
                Text(
                    text = "${
                        currentMonth.month.getDisplayName(
                            TextStyle.FULL,
                            Locale.getDefault()
                        )
                    } ${currentMonth.year}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold, color = Color.White
                )
                IconButton(onClick = { onMonthChange(currentMonth.plusMonths(1)) }) {
                    Icon(
                        Icons.Default.ArrowForwardIos,
                        contentDescription = "Next Month",
                        tint = Color.White
                    )
                }
            }

            // days of the week header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN").forEach { day ->
                    Text(
                        text = day,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // display days of the current month
            val daysInMonth = currentMonth.lengthOfMonth()
            val firstDayOfWeek =
                currentMonth.atDay(1).dayOfWeek.value % 7 // Adjust for Monday start

            Column {
                var day = 1 - firstDayOfWeek // Start rendering from the correct position
                while (day <= daysInMonth) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (i in 0..6) {
                            if (day in 1..daysInMonth) {
                                val date = currentMonth.atDay(day)
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            if (date == selectedDate) Color.Black else Color.Transparent,
                                            shape = MaterialTheme.shapes.small
                                        )
                                        .clickable { onDateSelected(date) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = day.toString(),
                                        color = if (date == selectedDate) Color.White else Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.size(40.dp)) // empty spaces for alignment
                            }
                            day++
                        }
                    }
                }
            }
        }

        }
    }
}

@Composable
fun PlumbingRepairCard(trade: Tradesman) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Image( painterResource(trade.imageResId),
                contentDescription = "Tradesman Image",
                modifier = Modifier.size(120.dp,120.dp)
                    .padding(end = 10.dp))
            Column(
                modifier = Modifier.weight(1f)
                    .padding(top = 7.dp)

            ) {
                Text(
                    text = trade.category,
                )
                Text(
                    modifier = Modifier.padding( top = 5.dp),
                    text = trade.username,
                    color = Color.Gray
                )
                Row(
                    modifier = Modifier.padding(top = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.padding(end = 5.dp)
                        .background(
                            color = Color(0xFFFFF2DD),
                            shape = RoundedCornerShape(5.dp)
                        )
                    ) {
                        Row()
                         {
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = trade.rate,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Box(modifier = Modifier
                        .background(
                            color = Color(0xFFFFF2DD),
                            shape = RoundedCornerShape(5.dp)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(5.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color.Yellow
                            )
                            Text(
                                text = trade.reviews.toString(),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween

                ) {
                    Column {
                        Text(
                            text = "Date",
                        )
                        Text(
                            text = "31 January 2024"
                        )
                    }
                    Column {
                        Text(
                            text = "Time",
                        )
                        Text(
                            text = "8:00 AM",
                        )
                    }
                }
            }

        }
    }
}



