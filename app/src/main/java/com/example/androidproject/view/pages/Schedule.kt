package com.example.androidproject.view.pages

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowDropDown
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
import com.example.androidproject.view.Tradesmandate
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.view.theme.myGradient
import com.example.androidproject.view.theme.myGradient3
import com.example.androidproject.view.theme.myGradient4
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun ScheduleScreen(modifier: Modifier = Modifier, navController: NavController) {
    Log.i("Screen", "ScheduleScreen")

    var selectedClientDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedApplicantDate by remember { mutableStateOf(LocalDate.now()) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedFilter by remember { mutableStateOf("My Clients") }

    val clients = listOf(
        Tradesmandate(R.drawable.pfp, "Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark, "2025-02-17"),
        Tradesmandate(R.drawable.pfp, "John", "Electrician", "P600/hr", 4.2, R.drawable.bookmark, "2025-02-18")
    )

    val applicants = listOf(
        Tradesmandate(R.drawable.pfp, "Sarah", "Carpenter", "P550/hr", 4.3, R.drawable.bookmark, "2025-02-19"),
        Tradesmandate(R.drawable.pfp, "Mike", "Painter", "P480/hr", 4.0, R.drawable.bookmark, "2025-02-20")
    )

    val selectedDate = if (selectedFilter == "My Clients") selectedClientDate else selectedApplicantDate

    Box(
        Modifier.fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .background(Color.White)
        ) {
            ScheduleTopSection(navController)

            CalendarSection(
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                onDateSelected = { date ->
                    if (selectedFilter == "My Clients") {
                        selectedClientDate = date
                    } else {
                        selectedApplicantDate = date
                    }
                },
                onMonthChange = { month -> currentMonth = month },
                tradesmen = if (selectedFilter == "My Clients") clients else applicants
            )

            FilterSection(selectedDate, selectedFilter) { selectedFilter = it }

            if (selectedFilter == "My Clients") {
                MyClientsList(clients, selectedClientDate)
            } else {
                MyApplicantsList(applicants, selectedApplicantDate)
            }
        }
    }
}

@Composable
fun FilterSection(
    selectedDate: LocalDate,
    selectedFilter: String,
    onFilterChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedDate.format(DateTimeFormatter.ofPattern("yyyy MMMM d")),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black
            )

            Box(contentAlignment = Alignment.Center, modifier = Modifier.wrapContentSize(Alignment.Center)) {
                TextButton(onClick = { expanded = true }) {
                    Text(text = selectedFilter, color = Color(0xFF3CC0B0))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color(0xFF3CC0B0))
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        colors = MenuDefaults.itemColors(textColor = Color(0xFF3CC0B0)),
                        text = { Text("My Clients") },
                        onClick = {
                            onFilterChange("My Clients")
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        colors = MenuDefaults.itemColors(textColor = Color(0xFF3CC0B0)),
                        text = { Text("My Applicants") },
                        onClick = {
                            onFilterChange("My Applicants")
                            expanded = false
                        }
                    )
                }
            }
        }

        Text(
            text = selectedDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Gray
        )
    }
}

@Composable
fun MyClientsList(clients: List<Tradesmandate>, selectedDate: LocalDate) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val filteredClients = clients.filter {
        LocalDate.parse(it.date, dateFormatter).isEqual(selectedDate)
    }
    if (filteredClients.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No Bookings Available",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxHeight().background(Color.White).padding(bottom = 70.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredClients) { client ->
                MyClientsItem(client)
            }
        }
    }
}

@Composable
fun MyApplicantsList(applicants: List<Tradesmandate>, selectedDate: LocalDate) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val filteredApplicants = applicants.filter {
        LocalDate.parse(it.date, dateFormatter).isEqual(selectedDate)
    }
    if (filteredApplicants.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No Job Applicant Available",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxHeight().background(Color.White).padding(bottom = 70.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredApplicants) { applicant ->
                MyApplicantItem(applicant)
            }
        }
    }
}

@Composable
fun CalendarSection(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChange: (YearMonth) -> Unit,
    tradesmen: List<Tradesmandate> // Pass the list of tradesmen
) {
    // Extract dates with data
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val datesWithData = tradesmen.mapNotNull { trade ->
        try {
            LocalDate.parse(trade.date, dateFormatter)
        } catch (e: Exception) {
            null
        }
    }.toSet() // Convert to a Set for quick lookup

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp), // Set rounded corners
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(brush = myGradient4) // Apply gradient
                .padding(8.dp) // Padding inside the card
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                // Navigation for month (e.g., Jan to Feb)
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp),
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
                        text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(onClick = { onMonthChange(currentMonth.plusMonths(1)) }) {
                        Icon(
                            Icons.Default.ArrowForwardIos,
                            contentDescription = "Next Month",
                            tint = Color.White
                        )
                    }
                }

                // Days of the week header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN").forEach { day ->
                        Text(
                            text = day,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Display days of the current month
                val daysInMonth = currentMonth.lengthOfMonth()
                val firstDayOfWeek = currentMonth.atDay(1).dayOfWeek.value % 7 // Adjust for Monday start

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
                                    val hasData = datesWithData.contains(date)

                                    Box(
                                        modifier = Modifier
                                            .size(30.dp)
                                            .background(
                                                if (date == selectedDate) Color.Black else Color.Transparent,
                                                shape = MaterialTheme.shapes.small
                                            )
                                            .clickable { onDateSelected(date) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = day.toString(),
                                            color = when {
                                                date == selectedDate -> Color.White
                                                hasData -> Color.Yellow // Highlight days with data
                                                else -> Color.White
                                            },
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                } else {
                                    Spacer(modifier = Modifier.size(30.dp)) // Empty spaces for alignment
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
fun ScheduleTopSection(navController: NavController){
    Row(
        modifier = Modifier
            .padding(horizontal = 25.dp)
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


            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications Icon",
                tint = Color.Black,
                modifier = Modifier.size(35.dp)
                    .clickable { navController.navigate("notification") }
            )

    }
}


@Composable
fun MyClientsItem(trade: Tradesmandate) {

    val windowSize = rememberWindowSizeClass()
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
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
                    .padding(top = 7.dp, start = 8.dp)

            ) {
                Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                    Text(
                        modifier = Modifier.padding( top = 5.dp),
                        text = trade.username,
                        color = Color.Black,
                        fontSize = nameTextSize
                    )
                    Text(
                        modifier = Modifier.padding( top = 5.dp, end = 15.dp),
                        text = "Pending",
                        color = Color.Black,
                        fontSize = smallTextSize
                    )
                }

                Text(
                    text = trade.category,
                    color = Color.Gray,
                    fontSize = taskTextSize
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
                                fontSize = smallTextSize
                            )
                        }
                    }

                    Box(modifier = Modifier
                        .background(
                            color = Color(0xFFFFF2DD),
                            shape = RoundedCornerShape(5.dp),
                        ),
                    ) {
                        Row(
                            modifier = Modifier.padding(5.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(16.dp).padding(top = 5.dp),
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",

                                tint = Color.Yellow,

                            )
                            Text(
                                text = trade.reviews.toString(),
                                fontSize = smallTextSize

                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround

                ) {
                    Column {
                        Text(
                            text = "Date",
                            fontSize = taskTextSize

                        )
                        Text(
                            text = trade.date,
                            fontSize = smallTextSize
                        )
                    }
                    Column {
                        Text(
                            text = "Time",
                            fontSize = taskTextSize

                        )
                        Text(
                            text = "8:00 AM",
                            fontSize = smallTextSize

                        )
                    }
                }
            }

        }
    }
}



@Composable
fun MyApplicantItem(trade: Tradesmandate) {

    val windowSize = rememberWindowSizeClass()
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
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
                    .padding(top = 7.dp, start = 8.dp)

            ) {
                Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                    Text(
                        modifier = Modifier.padding( top = 5.dp),
                        text = trade.username,
                        color = Color.Black,
                        fontSize = nameTextSize
                    )
                    Text(
                        modifier = Modifier.padding( top = 5.dp, end = 15.dp),
                        text = "Pending",
                        color = Color.Black,
                        fontSize = smallTextSize
                    )
                }

                Text(
                    text = trade.category,
                    color = Color.Gray,
                    fontSize = taskTextSize
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
                                fontSize = smallTextSize
                            )
                        }
                    }

                    Box(modifier = Modifier
                        .background(
                            color = Color(0xFFFFF2DD),
                            shape = RoundedCornerShape(5.dp),
                        ),
                    ) {
                        Row(
                            modifier = Modifier.padding(5.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(16.dp).padding(top = 5.dp),
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",

                                tint = Color.Yellow,

                                )
                            Text(
                                text = trade.reviews.toString(),
                                fontSize = smallTextSize

                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround

                ) {
                    Column {
                        Text(
                            text = "Date",
                            fontSize = taskTextSize

                        )
                        Text(
                            text = trade.date,
                            fontSize = smallTextSize
                        )
                    }
                    Column {
                        Text(
                            text = "Time",
                            fontSize = taskTextSize

                        )
                        Text(
                            text = "8:00 AM",
                            fontSize = smallTextSize

                        )
                    }
                }
            }

        }
    }
}
