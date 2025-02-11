package com.example.androidproject.view.pages

import LogoutViewModel
import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.androidproject.R
import com.example.androidproject.data.preferences.AccountManager
import com.example.androidproject.data.preferences.TokenManager
import com.example.androidproject.view.ServicePosting
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(modifier: Modifier = Modifier,navController:NavController,logoutViewModel:LogoutViewModel) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabNames = listOf("My Posts", "General")
    var postsList by remember { mutableStateOf<List<ServicePosting>>(emptyList()) }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
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
                text = "My Profile",
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



        // for profile info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Box(modifier = Modifier.padding(10.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF81D796), Color(0xFF39BFB1)),
                                start = Offset(0f, 1f),
                                end = Offset(1f, 1f)
                            ), shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color.White, RoundedCornerShape(30.dp))
                    ) {
                        // Placeholder for profile image
                        Text(
                            text = "👤",
                            modifier = Modifier.align(Alignment.Center),
                            fontSize = 30.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.padding(top = 5.dp)) {
                        Text(
                            text = "Client’s Name",
                            color = Color.White,
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Lorem@gmail.com",
                            color = Color.White,
                            style = TextStyle(fontSize = 14.sp)
                        )
                        Text(
                            text = "Dagupan, Philippines",
                            color = Color.White,
                            style = TextStyle(fontSize = 14.sp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = Color.LightGray
                    )
                }
            }

            // tab selection
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
            ) {
                tabNames.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title, fontSize = 14.sp) },
                    )
                }
            }

        }

// Cards Section (Placed Outside the White Background)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp) // Separation from the white background
        ) {
            when (selectedTabIndex) {
                0 -> MyPostsTab(servicePostings = postsList) // Pass postsList
                1 -> SettingsScreen(navController, logoutViewModel)
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomEnd // Ensures FAB stays at bottom-end
            ) {
                // Pass the callback function to update the postsList
                FabPosting(
                    onPostNewService = { newPost ->
                        postsList = postsList + newPost
                    },
                    onDeadlineChange = { deadline ->
                        println("Selected Deadline: $deadline") // Handle deadline change if needed
                    }
                )
            }
        }


    }

}




@Composable
fun MyPostsTab(servicePostings: List<ServicePosting>) {
    LazyColumn( // Make it scrollable
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(servicePostings) { posting ->
            PostsCard(
                servicePosting = posting,
                onEditClick = { title, description, rate, deadline ->
                    // Handle edit functionality here if needed
                },
                onApplicantsClick = { /* Handle applicants click */ }

            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PostsCard(
    servicePosting: ServicePosting,
    onEditClick: (String, String, String,String) -> Unit,
    onApplicantsClick: () -> Unit,
) {
    var isDialogVisible by remember { mutableStateOf(false) }
    var editableTitle by remember { mutableStateOf(servicePosting.title) }
    var editableDescription by remember { mutableStateOf(servicePosting.description) }
    var editableLocation by remember { mutableStateOf(servicePosting.location) }
    var editableDeadline by remember { mutableStateOf(servicePosting.deadline ?: "Select Deadline") } // Added deadline
    var editableRate by remember { mutableStateOf(servicePosting.rate) }

    val originalTitle = remember { servicePosting.title }
    val originalDescription = remember { servicePosting.description }
    val originalRate = remember { servicePosting.rate }
    val originalDeadline = remember { servicePosting.deadline ?: "Select Deadline" }

    val selectedCategories = remember { mutableStateListOf<String>().apply {
        addAll(servicePosting.category.split(", ").filter { it.isNotBlank() })
    } }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .clip(RoundedCornerShape(8.dp))

    ) {
        Box(modifier = Modifier.background(color = Color.White)) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = editableTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(
                        onClick = { isDialogVisible = true }, // Show dialog when clicked
                        colors = ButtonDefaults.buttonColors(Color.White),
                        modifier = Modifier
                            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                            .height(40.dp)
                            .width(130.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Edit Post",
                                color = Color.Black,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 8.dp, end = 2.dp),
                                textAlign = TextAlign.Start
                            )
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Icon",
                                tint = Color.Black,
                                modifier = Modifier.padding(end = 8.dp).size(15.dp)
                            )
                        }
                    }
                }


                Text(
                    text = editableDescription,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(text = "Location: $editableLocation", fontSize = 16.sp)

                Text(
                    text = "Est. Budget: ${editableRate}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "Category: ${
                        if (servicePosting.category.isNotEmpty()) servicePosting.category else "Uncategorized"
                    }",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    text = "${servicePosting.applicantsCount} Applicants",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onApplicantsClick() }
                )
                Text(text = "Deadline: $editableDeadline", fontSize = 16.sp, color = Color.Red) // Display Deadline

                // Other card content
                Text(
                    text = "Posted on ${servicePosting.postedDate} - ${if (servicePosting.isActive) "Active" else "Inactive"}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }

    // pop up dialog for editing posting
    if (isDialogVisible) {
        Dialog(onDismissRequest = { isDialogVisible = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = "Edit Post",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "Update the details of your service need",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    // Title TextField with Border
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)) // Add border
                    ) {
                        TextField(
                            value = editableTitle,
                            onValueChange = { editableTitle = it },
                            label = { Text("Title") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(8.dp)),                        maxLines = 1,
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.Black
                            )
                        )
                    }

                    // Description TextField with Border
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)) // Add border
                    ) {
                        TextField(
                            value = editableDescription,
                            onValueChange = { editableDescription = it },
                            label = { Text("Description") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(8.dp)),                        maxLines = 1,
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.Black
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)) // Add border
                    ){
                        TextField(value = editableLocation,
                            onValueChange = { editableLocation = it },
                            label = { Text("Location") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(8.dp)),                        maxLines = 1,
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.Black
                            ))

                    }

                    // Rate TextField with Border
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)) // Add border
                    ) {
                        TextField(
                            value = editableRate,
                            onValueChange = { editableRate = it },
                            label = { Text("Estimated Budget") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(8.dp)),                        maxLines = 1,
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.Black
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)) // Add border
                    ) {
                        TextField(
                            value = editableDeadline,
                            onValueChange = { editableDeadline = it },
                            label = { Text("Deadline") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(8.dp)),                        maxLines = 1,
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.Black
                            )
                        )
                    }
                    Column(
                        modifier = Modifier.padding(5.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)

                    ) {
                        Text(
                            text = "Select Service Category",
                            fontWeight = FontWeight.Bold
                        )

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)

                        ) {
                            val categories = listOf(
                                "Carpentry",
                                "Painting",
                                "Welding",
                                "Electrician",
                                "Plumbing",
                                "Masonry",
                                "Roofing",
                                "AC Repair",
                                "Mechanics",
                                "Cleaning"
                            )

                            categories.forEach { category ->
                                val isSelected = selectedCategories.contains(category)
                                Box(
                                    modifier = Modifier

                                        .border(1.dp, Color.Gray, RoundedCornerShape(30.dp))
                                        .clip(RoundedCornerShape(30.dp))
                                        .background(if (isSelected) Color.Gray else Color.White)
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = category,
                                        color = if (isSelected) Color.White else Color.Black
                                    )
                                }
                            }
                        }
                    }


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(onClick = {
                            // Restore the original values if cancel is clicked
                            editableTitle = originalTitle
                            editableDescription = originalDescription
                            editableRate = originalRate
                            editableDeadline = originalDeadline
                            isDialogVisible = false
                        }) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            // Save the new values
                            isDialogVisible = false
                            onEditClick(editableTitle, editableDescription, editableRate,editableDeadline)
                        }) {
                            Text("Save")
                        }
                    }
                }

            }
        }
    }
}


@Composable
fun GeneralSettings(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF9F9F9),
                shape = RoundedCornerShape(8.dp))) {

            Column(modifier = Modifier.padding(10.dp)) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Text(
                        text = title,
                        modifier = Modifier.padding(start = 14.dp)
                    )

                }
                Text(
                    text = description,
                    modifier = Modifier.padding(top = 5.dp, start = 10.dp)
                )

            }

        }
    }

}


@Composable
fun SettingsScreen(navController: NavController, logoutViewModel: LogoutViewModel) {
    val logoutResult by logoutViewModel.logoutResult.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(logoutResult) {
        logoutResult?.let {
            // Clear tokens and navigate regardless of result
            TokenManager.clearToken()
            AccountManager.clearAccountData()
            Toast.makeText(context, "logout successful", Toast.LENGTH_SHORT).show()
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
            logoutViewModel.resetLogoutResult()
        }
    }
    Column {

        GeneralSettings(
            icon = ImageVector.vectorResource(id = R.drawable.ic_notification),
            title = "Notification",
            description = "Manage alerts and updates.",
            onClick = { /* Handle Notification click */ }
        )
        GeneralSettings(
            icon = ImageVector.vectorResource(id = R.drawable.ic_privacy),
            title = "Privacy",
            description = "Change your password.",
            onClick = { navController.navigate("emailverification") }
        )
        Text(
            text = "Help and Support", fontWeight = FontWeight(500),
            fontSize = 20.sp, modifier = Modifier.padding( 12.dp)
        )
        GeneralSettings(
            icon = ImageVector.vectorResource(id = R.drawable.ic_about),
            title = "About Us",
            description = "Know more about our team.",
            onClick = { navController.navigate("aboutus") }
        )
        GeneralSettings(
            icon = ImageVector.vectorResource(id = R.drawable.ic_report),
            title = "Report a Problem",
            description = "Report a problem.",
            onClick = { /* Handle Report a Problem click */ }
        )
        Text(
            text = "Log Out", fontWeight = FontWeight(500),
            fontSize = 20.sp, modifier = Modifier.padding( 12.dp)
        )
        GeneralSettings(
            icon = ImageVector.vectorResource(id = R.drawable.ic_logout),
            title = "Log Out",
            description = "",
            onClick = {
                val token = TokenManager.getToken()
                if (token != null) {
                    logoutViewModel.logout("Bearer $token")
                } else {
                    // Handle case where token is null
                    TokenManager.clearToken()
                    AccountManager.clearAccountData()
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
    fun FabPosting(
        onPostNewService: (ServicePosting) -> Unit,
        onDeadlineChange:(String) ->Unit
    ) {
        var isDialogVisible by remember { mutableStateOf(false) }
        var title by remember { mutableStateOf("") } // Use simple variables for input
        var description by remember { mutableStateOf("") }
        var location by remember { mutableStateOf("") }
        var rate by remember { mutableStateOf("") }
        var selectedCategories = remember { mutableStateListOf<String>() }
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
     val context = LocalContext.current
     val today = LocalDate.now() // Get today's date
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var deadline by remember { mutableStateOf("") }


    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val pickedDate = LocalDate.of(year, month + 1, dayOfMonth)
            if (!pickedDate.isBefore(today)) { // Ensure it's today or later
                selectedDate = pickedDate
                val formattedDate = pickedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                deadline = formattedDate
                onDeadlineChange(formattedDate)
            } else {
                Toast.makeText(context, "You cannot select a past date!", Toast.LENGTH_SHORT).show()
            }
        },
        today.year,  // Default year
        today.monthValue - 1, // Default month (zero-based index)
        today.dayOfMonth // Default day
    ).apply {
        datePicker.minDate = System.currentTimeMillis() // Set minimum selectable date to today
    }
        FloatingActionButton(
            onClick = { isDialogVisible = true },
            containerColor = Color.Gray,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Icon")
        }

        if (isDialogVisible) {
            Dialog(onDismissRequest = { isDialogVisible = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "Create New Post",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = "Provide details of your new service",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )

                        // Title TextField
                        TextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Title") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(8.dp)),                        maxLines = 1,
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.Black
                            )
                        )

                        // Description TextField
                        TextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(8.dp)),                        maxLines = 1,
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.Black
                            )
                        )
                        TextField(
                            value = location,
                            onValueChange = { location = it },
                            label = { Text("Location") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(8.dp)),

                            shape = RoundedCornerShape(8.dp), // Rounded Corners
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.White, // White background
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.Black
                            )
                        )


                        // Rate TextField
                        TextField(
                            value = rate,
                            onValueChange = { rate = it },
                            label = { Text("Estimated Budget") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(8.dp)),                        maxLines = 1,
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.Black
                            )
                        )
                        Button(
                            onClick = { datePickerDialog.show() },
                            modifier = Modifier
                                .width(360.dp)
                                .heightIn(min = 56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            ),
                            border = BorderStroke(1.dp, Color.Gray),

                            ) {
                            Row (Modifier.fillMaxWidth().offset(x = (-10).dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically){
                                Icon(imageVector = Icons.Default.CalendarToday,
                                    contentDescription = "Calendar Icon",
                                    tint = Color.Gray)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (deadline.isNotEmpty()) deadline else "Select Deadline",
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }

                        }

                        Column(
                            modifier = Modifier.padding(5.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(text = "Select Service Category", fontWeight = FontWeight.Bold)

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val categories = listOf(
                                    "Carpentry",
                                    "Painting",
                                    "Welding",
                                    "Electrician",
                                    "Plumbing",
                                    "Masonry",
                                    "Roofing",
                                    "AC Repair",
                                    "Mechanics",
                                    "Cleaning"
                                )

                                categories.forEach { category ->
                                    val isSelected = selectedCategories.contains(category)
                                    Box(
                                        modifier = Modifier
                                            .clickable {
                                                if (isSelected) {
                                                    selectedCategories.remove(category) // Remove if already selected
                                                } else if (selectedCategories.size < 3) {
                                                    selectedCategories.add(category) // Add only if less than 3
                                                }
                                            }
                                            .border(1.dp, Color.Gray, RoundedCornerShape(30.dp))
                                            .clip(RoundedCornerShape(30.dp))
                                            .background(if (isSelected) Color.Gray else Color.White)
                                            .padding(horizontal = 12.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = category,
                                            color = if (isSelected) Color.White else Color.Black
                                        )
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(onClick = { isDialogVisible = false }) {
                                Text("Cancel")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = {
                                val newPost = ServicePosting(
                                    title = title,
                                    description = description,
                                    location = location,
                                    rate = rate.toString(),
                                    deadline = deadline,
                                    postedDate = currentDate.toString(),
                                    isActive = true,
                                    category = if (selectedCategories.isNotEmpty()) selectedCategories.joinToString(", ") else "Uncategorized",
                                    applicantsCount = 0
                                )
                                isDialogVisible = false
                                onPostNewService(newPost) // Send the new post to the parent Composable
                            }) {
                                Text("Post")
                            }
                        }
                    }
                }
            }
        }
    }
