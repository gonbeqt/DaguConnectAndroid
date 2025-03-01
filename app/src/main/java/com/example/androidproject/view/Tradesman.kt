package com.example.androidproject.view

data class Tradesman(
    val imageResId: Int,
    val username: String,
    val category: String,
    val rate: String,
    val reviews: Double,
    val bookmark: Int
)
data class Tradesmandate(
    val imageResId: String,
    val username: String,
    val category: String,
    val rate: String,
    val reviews: Float,
    val bookmark: Int,
    val date: String
)


/*
@Composable
fun ScheduleScreen(modifier: Modifier = Modifier, navController: NavController, getClientsBooking: GetClientBookingViewModel) {
    val clientBooking = getClientsBooking.ClientBookingPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        getClientsBooking.invalidatePagingSource()
    }
    Log.i("Screen", "ScheduleScreen")

    var selectedClientDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedApplicantDate by remember { mutableStateOf(LocalDate.now()) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedFilter by remember { mutableStateOf("My Clients") }

    // Hardcoded applicants list remains unchanged
    val applicants = listOf(
        Tradesmandate("profile", "Sarah", "Carpenter", "P550/hr", 4.3f, R.drawable.bookmark, "2025-02-19"),
        Tradesmandate("profile", "Mike", "Painter", "P480/hr", 4.0f, R.drawable.bookmark, "2025-02-20")
    )

    // Extract dates from clientBooking for filtering
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val clientBookingDates = remember(clientBooking) {
        clientBooking.itemSnapshotList.filterNotNull().mapNotNull {
            try {
                LocalDate.parse(it.bookingdate, dateFormatter)
            } catch (e: Exception) {
                null
            }
        }.toSet()
    }

    val selectedDate = if (selectedFilter == "My Clients") selectedClientDate else selectedApplicantDate

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
                    if (selectedFilter == "My Clients") {
                        selectedClientDate = date
                    } else {
                        selectedApplicantDate = date
                    }
                },
                onMonthChange = { month -> currentMonth = month },
                tradesmen = if (selectedFilter == "My Clients") emptyList() else applicants, // Pass empty list for clients since we use clientBooking
                clientBookingDates = clientBookingDates // Pass the extracted dates
            )


            FilterSection(selectedDate, selectedFilter) { selectedFilter = it }

            if (selectedFilter == "My Clients") {
                MyClientsList(clientBooking, selectedClientDate)
            } else {
                MyApplicantsList(applicants, selectedApplicantDate)
            }
        }
    }
}*/
