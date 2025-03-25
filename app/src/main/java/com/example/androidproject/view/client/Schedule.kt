package com.example.androidproject.view.client

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.ViewModelSetups
import com.example.androidproject.model.JobApplicantData
import com.example.androidproject.model.client.GetClientsBooking
import com.example.androidproject.view.ApplicantsDate
import com.example.androidproject.view.Tradesmandate
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.view.theme.myGradient4
import com.example.androidproject.viewmodel.bookings.GetClientBookingViewModel
import com.example.androidproject.viewmodel.job_application.client.GetMyJobApplicantsViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun ScheduleScreen(modifier: Modifier = Modifier, navController: NavController, getClientsBooking: GetClientBookingViewModel, getMyJobApplicantsViewModel: GetMyJobApplicantsViewModel) {
    val clientBooking = getClientsBooking.ClientBookingPagingData.collectAsLazyPagingItems()
    val clientApplicants = getMyJobApplicantsViewModel.jobApplicantsPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        clientBooking.refresh()
        clientApplicants.refresh()
    }

    Log.i("Screen", "ScheduleScreen")

    var selectedClientDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedApplicantDate by remember { mutableStateOf(LocalDate.now()) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedFilter by remember { mutableStateOf("My Tradesman") }


    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val allBookingDates = remember(clientBooking) {
        clientBooking.itemSnapshotList.filterNotNull()
            .filter { it.bookingStatus == "Active" } // Filter for Active bookings
            .mapNotNull { booking ->
                try {
                    LocalDate.parse(booking.bookingDate, dateFormatter)
                } catch (e: Exception) {
                    null
                }
            }.toSet()
    }

    val filteredClients = clientBooking.itemSnapshotList.filterNotNull()
        .filter {
            try {
                val bookingDate = LocalDate.parse(it.bookingDate, dateFormatter)
                bookingDate.isEqual(selectedClientDate) && it.bookingStatus == "Active"
            } catch (e: Exception) {
                false
            }
        }
    val allApplicantDates = remember(clientApplicants) {
        clientApplicants.itemSnapshotList.filterNotNull()
            .filter { it.status == "Active"}
            .mapNotNull { applicants ->
                try {
                    LocalDate.parse(applicants.createdAt, dateFormatter)
                }catch (e: Exception){
                    null
                }
            }.toSet()
    }

    val filteredApplicants = clientApplicants.itemSnapshotList.filterNotNull()
        .filter {
            try {
                val dateApplicants = LocalDate.parse(it.createdAt, dateFormatter)
                dateApplicants.isEqual(selectedApplicantDate) && it.status == "Active"
            } catch (e: Exception) {
                false
            }
        }

    val selectedDate = if (selectedFilter == "My Tradesman") selectedClientDate else selectedApplicantDate

    // Convert both types to Tradesmandate
    val tradesmenList = if (selectedFilter == "My Tradesman") {
        filteredClients.map {
            Tradesmandate(
                it.tradesmanProfile ?: "",
                it.tradesmanFullName,
                it.taskType,
                "P${it.workFee}",
                it.ratings,
                R.drawable.bookmark,
                it.bookingDate
            )
        }
    } else {
        filteredApplicants.map {
            Tradesmandate(
                it.tradesmanProfilePicture ?: "",
                it.tradesmanFullname,
                it.jobType,
                "", // No rate available
                0f, // No rating available
                R.drawable.bookmark,
                it.createdAt
            )
        }
    }

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
            ScheduleTopSection(navController)

            CalendarSection(
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                onDateSelected = { date ->
                    if (selectedFilter == "My Tradesman") {
                        selectedClientDate = date
                    } else {
                        selectedApplicantDate = date
                    }
                },
                onMonthChange = { month -> currentMonth = month },
                allBookingDates = if (selectedFilter == "My Tradesman") allBookingDates else allApplicantDates,
                tradesmen = tradesmenList
            )


            FilterSection(selectedDate, selectedFilter) { selectedFilter = it }

            if (selectedFilter == "My Tradesman") {
                MyClientsList(clientBooking, selectedClientDate)
            } else {
                MyApplicantsList(clientApplicants, selectedApplicantDate)
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
    val poppinsFont = FontFamily(
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_bold, FontWeight.Bold)
    )
    var expanded by remember { mutableStateOf(false) }
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

    Column(
        Modifier
            .fillMaxWidth()
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = selectedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy")),
                fontWeight = FontWeight.Bold,
                fontSize = nameTextSize,
                color = Color.Black
            )

            Box(contentAlignment = Alignment.Center, modifier = Modifier.wrapContentSize(Alignment.Center)) {
                TextButton(onClick = { expanded = true }) {
                    Text(text = selectedFilter, color = Color.Black, fontSize = taskTextSize, fontWeight = FontWeight.Normal, fontFamily = poppinsFont)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Black)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        colors = MenuDefaults.itemColors(textColor =  Color.Black),
                        text = { Text("My Tradesman", fontSize = taskTextSize, fontWeight = FontWeight.Normal, fontFamily = poppinsFont) },
                        onClick = {
                            onFilterChange("My Tradesman")
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        colors = MenuDefaults.itemColors(textColor = Color.Black),
                        text = { Text("My Applicants" , fontSize = taskTextSize, fontWeight = FontWeight.Normal, fontFamily = poppinsFont) },
                        onClick = {
                            onFilterChange("My Applicants")
                            expanded = false
                        }
                    )
                }
            }
        }

        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = selectedDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
            fontSize = 18.sp,
            fontFamily = poppinsFont,
            fontWeight = FontWeight.Normal,
            color = Color.Gray
        )
    }
}

@Composable
fun MyClientsList(clientBooking: LazyPagingItems<GetClientsBooking>, selectedDate: LocalDate) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val windowSize = rememberWindowSizeClass()
    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 16.sp
        WindowType.MEDIUM -> 18.sp
        WindowType.LARGE -> 20.sp
    }
    val poppinsFont = FontFamily(
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_bold, FontWeight.Bold)
    )

    val filteredClients = clientBooking.itemSnapshotList.filterNotNull()
        .filter {
            try {
                val bookingDate = LocalDate.parse(it.bookingDate, dateFormatter)
                bookingDate.isEqual(selectedDate) && it.bookingStatus == "Active" // Filter for Active bookingStatus
            } catch (e: Exception) {
                false // Skip invalid dates
            }
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
                fontSize = nameTextSize,
                fontWeight = FontWeight.Normal,
                fontFamily = poppinsFont,
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
            items(filteredClients) { client ->
                MyClientsItem(client)
            }
        }
    }
}
@Composable
fun MyApplicantsList(allApplicants: LazyPagingItems<JobApplicantData>, selectedDate: LocalDate) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val windowSize = rememberWindowSizeClass()
    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 16.sp
        WindowType.MEDIUM -> 18.sp
        WindowType.LARGE -> 20.sp
    }
    val poppinsFont = FontFamily(
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_bold, FontWeight.Bold)
    )

    val filteredApplicants = allApplicants.itemSnapshotList.filterNotNull()
        .filter {
            try {
                val applicantDate = LocalDate.parse(it.createdAt, dateFormatter)
                applicantDate.isEqual(selectedDate) && it.status == "Active" // Filter for Active bookingStatus
            } catch (e: Exception) {
                false // Skip invalid dates
            }
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
                fontSize = nameTextSize,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.Normal,
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
                MyApplicantItem(applicant)
            }
        }
    }
}

// Rest of the existing composables remain mostly unchanged, but we'll update CalendarSection

@Composable
fun CalendarSection(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChange: (YearMonth) -> Unit,
    allBookingDates: Set<LocalDate>, // New parameter for all booking dates
    tradesmen: List<Tradesmandate> // Pass the list of tradesmen
) {
    // Extract dates with data from allBookingDates (for My Tradesman) or tradesmen (for My Applicants)
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
        allBookingDates // Use all booking dates for Tradesman
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
                .height(240.dp)
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
                                            .size(24.dp)
                                            .background(
                                                color = when {
                                                    date == selectedDate -> Color.Black
                                                    hasData -> Color.Yellow // Background for dates with data
                                                    else -> Color.Transparent
                                                },
                                                shape = RoundedCornerShape(10.dp)
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
                                                hasData -> Color.Black
                                                else -> Color.White
                                            },
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                } else {
                                    Spacer(modifier = Modifier.size(20.dp))
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
    val poppinsFont = FontFamily(
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_bold, FontWeight.Bold)
    )

    Row(Modifier.fillMaxWidth().height(70.dp).shadow(0.2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    )  {
        Row(
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text(
                text = "Schedule",
                fontFamily = poppinsFont,
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications Icon",
                tint = Color.Black,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { navController.navigate("notification") }
            )
        }
    }
}


@Composable
fun MyClientsItem(Clients: GetClientsBooking) {
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
                model = Clients.tradesmanProfile,
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
                        text = Clients.tradesmanFullName,
                        color = Color.Black,
                        fontSize = nameTextSize
                    )
                    Text(
                        modifier = Modifier.padding( top = 5.dp, end = 15.dp),
                        text = Clients.bookingStatus,
                        color = Color.Black,
                        fontSize = smallTextSize
                    )
                }

                Text(
                    text = Clients.taskType,
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
                                text = "P${Clients.workFee}",
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
                                text = "${Clients.ratings}",
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
                            text = Clients.bookingDate,
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
fun MyApplicantItem(trade: JobApplicantData) {

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
                model = trade.tradesmanProfilePicture,
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
                        text = trade.tradesmanFullname,
                        color = Color.Black,
                        fontSize = nameTextSize
                    )
                    Text(
                        modifier = Modifier.padding( top = 5.dp, end = 15.dp),
                        text = trade.status,
                        color = Color.Black,
                        fontSize = smallTextSize
                    )
                }

                Text(
                    text = trade.jobType,
                    color = Color.Gray,
                    fontSize = taskTextSize
                )


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
                            text = trade.createdAt,
                            fontSize = smallTextSize
                        )
                    }

                }
            }

        }
    }
}
