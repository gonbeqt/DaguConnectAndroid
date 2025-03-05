package com.example.androidproject.view.tradesman


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.model.client.GetClientsBooking
import com.example.androidproject.model.client.GetTradesmanBooking
import com.example.androidproject.view.Tradesman
import com.example.androidproject.view.Tradesmandate
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.view.theme.myGradient4
import com.example.androidproject.viewmodel.bookings.GetClientBookingViewModel
import com.example.androidproject.viewmodel.bookings.GetTradesmanBookingViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun ScheduleTradesman(modifier: Modifier = Modifier, navController: NavController, getTradesmanBooking: GetTradesmanBookingViewModel) {
    val tradesmanBooking = getTradesmanBooking.TradesmanBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        getTradesmanBooking.invalidatePagingSource()
    }
    Log.i("Screen", "ScheduleScreen")

    var selectedClientDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedApplicantDate by remember { mutableStateOf(LocalDate.now()) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedFilter by remember { mutableStateOf("My Jobs") }

    val applicants = listOf(
        Tradesmandate("${R.drawable.pfp}", "Sarah", "Carpenter", "P550/hr", 4.3f, R.drawable.bookmark, "2025-02-19"),
        Tradesmandate("${R.drawable.pfp}", "Mike", "Painter", "P480/hr", 4.0f, R.drawable.bookmark, "2025-02-20")
    )

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val allBookingDates = remember(tradesmanBooking) {
        tradesmanBooking.itemSnapshotList.filterNotNull()
            .filter { it.bookingStatus == "Active" } // Filter for Active bookings
            .mapNotNull { booking ->
                try {
                    LocalDate.parse(booking.bookingDate, dateFormatter)
                } catch (e: Exception) {
                    null
                }
            }.toSet()
    }

    val filteredClients = tradesmanBooking.itemSnapshotList.filterNotNull()
        .filter {
            try {
                val bookingDate = LocalDate.parse(it.bookingDate, dateFormatter)
                bookingDate.isEqual(selectedClientDate) && it.bookingStatus == "Active"
            } catch (e: Exception) {
                false
            }
        }

    val selectedDate = if (selectedFilter == "My Jobs") selectedClientDate else selectedApplicantDate

    Box(
        Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .background(Color.White)

        ) {
            ScheduleTradesmanTopSection(navController)

            CalendarSectionTradesman(
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                onDateSelected = { date ->
                    if (selectedFilter == "My Jobs") {
                        selectedClientDate = date
                    } else {
                        selectedApplicantDate = date
                    }
                },
                onMonthChange = { month -> currentMonth = month },
                allBookingDates = allBookingDates,
                tradesmen = if (selectedFilter == "My Jobs") filteredClients.map {
                    Tradesmandate(
                        it.tradesmanProfile,
                        it.tradesmanFullName,
                        it.taskType,
                        "P${it.workFee}",
                        it.ratings,
                        R.drawable.bookmark,
                        it.bookingDate
                    )
                } else applicants
            )

            FilterSectionTradesman(selectedDate, selectedFilter) { selectedFilter = it }

            if (selectedFilter == "My Jobs") {
                MyJobsList(tradesmanBooking, selectedClientDate)
            } else {
                MySubmissionList(applicants, selectedApplicantDate)
            }
        }
    }
}

@Composable
fun FilterSectionTradesman(
    selectedDate: LocalDate,
    selectedFilter: String,
    onFilterChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
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

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedDate.format(DateTimeFormatter.ofPattern("yyyy MMMM d")),
                fontWeight = FontWeight.Bold,
                fontSize = nameTextSize,
                color = Color.Black
            )

            Box(contentAlignment = Alignment.Center, modifier = Modifier.wrapContentSize(Alignment.Center)) {
                TextButton(onClick = { expanded = true }) {
                    Text(text = selectedFilter, color = Color(0xFF3CC0B0), fontSize = taskTextSize)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color(0xFF3CC0B0))
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        colors = MenuDefaults.itemColors(textColor = Color(0xFF3CC0B0)),
                        text = { Text("My Jobs", fontSize = taskTextSize) },
                        onClick = {
                            onFilterChange("My Jobs")
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        colors = MenuDefaults.itemColors(textColor = Color(0xFF3CC0B0)),
                        text = { Text("My Submission" , fontSize = taskTextSize) },
                        onClick = {
                            onFilterChange("My Submission")
                            expanded = false
                        }
                    )
                }
            }
        }

        Text(
            text = selectedDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
            fontSize = taskTextSize,
            fontWeight = FontWeight.Normal,
            color = Color.Gray
        )
    }
}

@Composable
fun MyJobsList(tradesmanBooking: LazyPagingItems<GetTradesmanBooking>, selectedDate: LocalDate) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val allBookingDates = remember(tradesmanBooking) {
        tradesmanBooking.itemSnapshotList.filterNotNull()
            .filter { it.bookingStatus == "Active" } // Filter for Active bookingStatus
            .mapNotNull { booking ->
                try {
                    LocalDate.parse(booking.bookingDate, dateFormatter)
                } catch (e: Exception) {
                    null
                }
            }.toSet()
    }

    val filteredClients = tradesmanBooking.itemSnapshotList.filterNotNull()
        .filter {
            try {
                val bookingDate = LocalDate.parse(it.bookingDate, dateFormatter)
                bookingDate.isEqual(selectedDate) && it.bookingStatus == "Active" // Filter for Active bookingStatus
            } catch (e: Exception) {
                false // Skip invalid dates
            }
        }

    Log.d("MyClientsList", "Selected Date: $selectedDate, Filtered Clients: $filteredClients, All Booking Dates: $allBookingDates")

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
            modifier = Modifier
                .fillMaxHeight()
                .background(Color.White)
                .padding(bottom = 70.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredClients) { tradesman ->
                MyJobItem(tradesman)
            }
        }
    }
}
@Composable
fun MySubmissionList(applicants: List<Tradesmandate>, selectedDate: LocalDate) {
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
            modifier = Modifier
                .fillMaxHeight()
                .background(Color.White)
                .padding(bottom = 70.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredApplicants) { applicant ->
                MySubmissionItem(applicant)
            }
        }
    }
}

// Rest of the existing composables remain mostly unchanged, but we'll update CalendarSection

@Composable
fun CalendarSectionTradesman(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChange: (YearMonth) -> Unit,
    allBookingDates: Set<LocalDate>, // New parameter for all booking dates
    tradesmen: List<Tradesmandate> // Pass the list of tradesmen
) {
    // Extract dates with data from allBookingDates (for My Clients) or tradesmen (for My Applicants)
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val datesWithData = if (tradesmen.isNotEmpty()) {
        tradesmen.mapNotNull { trade ->
            try {
                LocalDate.parse(trade.date, dateFormatter)
            } catch (e: Exception) {
                null
            }
        }.toSet()
    } else {
        allBookingDates // Use all booking dates for My Clients
    }
    Spacer(Modifier.height(6.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp), // Set rounded corners
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .background(brush = myGradient4) // Apply gradient
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onMonthChange(currentMonth.minusMonths(1)) }) {
                        Icon(
                            Icons.Default.ArrowBackIos,
                            contentDescription = "Previous Month",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)

                        )
                    }
                    Text(
                        text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(onClick = { onMonthChange(currentMonth.plusMonths(1)) }) {
                        Icon(
                            Icons.Default.ArrowForwardIos,
                            contentDescription = "Next Month",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
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
                            fontSize = 10.sp,
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
                                            .size(20.dp)
                                            .background(
                                                if (date == selectedDate) Color.Black else Color.Transparent,
                                                shape = MaterialTheme.shapes.small
                                            )
                                            .clickable( indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) { onDateSelected(date) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = day.toString(),
                                            color = when {
                                                date == selectedDate -> Color.White
                                                hasData -> Color.Yellow // Highlight days with data
                                                else -> Color.White
                                            },
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                } else {
                                    Spacer(modifier = Modifier.size(20.dp)) // Empty spaces for alignment
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
fun ScheduleTradesmanTopSection(navController: NavController){

    Row(Modifier.fillMaxWidth().height(70.dp).shadow(0.2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    )  {
        Row(
            modifier = Modifier
                .padding(horizontal = 25.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text(
                text = "Schedule",
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


@Composable
fun MyJobItem(Tradesman: GetTradesmanBooking) {
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

            AsyncImage(
                model = Tradesman.tradesmanProfile,
                contentDescription = "Tradesman Image",
                modifier = Modifier
                    .size(120.dp, 120.dp)
                    .padding(end = 10.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 7.dp, start = 8.dp)

            ) {
                Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                    Text(
                        modifier = Modifier.padding( top = 5.dp),
                        text = Tradesman.tradesmanFullName,
                        color = Color.Black,
                        fontSize = nameTextSize
                    )
                    Text(
                        modifier = Modifier.padding( top = 5.dp, end = 15.dp),
                        text = Tradesman.bookingStatus,
                        color = Color.Black,
                        fontSize = smallTextSize
                    )
                }

                Text(
                    text = Tradesman.taskType,
                    color = Color.Gray,
                    fontSize = taskTextSize
                )
                Row(
                    modifier = Modifier.padding(top = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier
                        .padding(end = 5.dp)
                        .background(
                            color = Color(0xFFFFF2DD),
                            shape = RoundedCornerShape(5.dp)
                        )
                    ) {
                        Row()
                        {
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = "P${Tradesman.workFee}",
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
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 5.dp),
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",

                                tint = Color.Yellow,

                                )
                            Text(
                                text = "${Tradesman.ratings}",
                                fontSize = smallTextSize

                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround

                ) {
                    Column {
                        Text(
                            text = "Date",
                            fontSize = taskTextSize

                        )
                        Text(
                            text = Tradesman.bookingDate,
                            fontSize = smallTextSize
                        )
                    }
                }
            }

        }
    }
}



@Composable
fun MySubmissionItem(trade: Tradesmandate) {

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

            Image( painterResource(trade.imageResId.toInt()),
                contentDescription = "Tradesman Image",
                modifier = Modifier
                    .size(120.dp, 120.dp)
                    .padding(end = 10.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
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
                    Box(modifier = Modifier
                        .padding(end = 5.dp)
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
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 5.dp),
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
                    modifier = Modifier
                        .padding(top = 5.dp)
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
