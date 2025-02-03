package com.example.androidproject.ui.theme.views.pages

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.androidproject.R
import com.example.androidproject.ui.theme.views.ServicePosting

@Composable
fun ProfileScreen(modifier: Modifier = Modifier,navController:NavController) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabNames = listOf("My Posts", "General")
    var postsList by remember { mutableStateOf<List<ServicePosting>>(emptyList()) }

    var servicePostings = listOf(
        ServicePosting("Plumbing Repair", "January 25, 2025", applicantsCount = 5),
        ServicePosting("Electrical Repair", "January 20, 2025", isActive = false, applicantsCount = 3),
        ServicePosting("Electrical Repair", "January 20, 2025", isActive = false, applicantsCount = 3),
        ServicePosting("Electrical Repair", "January 20, 2025", isActive = false, applicantsCount = 3),
        ServicePosting("Electrical Repair", "January 20, 2025", isActive = false, applicantsCount = 3)
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // top bar
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .height(70.dp)

            ,
            horizontalArrangement = Arrangement.spacedBy(140.dp),
        ) {
            Text(text="My Profile",
                fontSize = 24.sp,
                fontWeight =
                FontWeight(500),
                modifier = Modifier.padding(16.dp),



                )
            Row (modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)){
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
                        .size(32.dp)

                        .clickable {
                            navController.navigate("message_screen")

                        }
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
                                start = androidx.compose.ui.geometry.Offset(0f, 1f),
                                end = androidx.compose.ui.geometry.Offset(1f, 1f)
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
                1 -> SettingsScreen()
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomEnd // Ensures FAB stays at bottom-end
            ) {
                // Pass the callback function to update the postsList
                FabPosting(onPostNewService = { newPost ->
                    postsList = postsList + newPost // Add the new post to the list
                })
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
                onEditClick = { title, description, rate ->
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
    onEditClick: (String, String, String) -> Unit,
    onApplicantsClick: () -> Unit
) {
    var isDialogVisible by remember { mutableStateOf(false) }
    var editableTitle by remember { mutableStateOf(servicePosting.title) }
    var editableDescription by remember { mutableStateOf(servicePosting.description) }
    var editableRate by remember { mutableStateOf(servicePosting.rate) }
    val originalTitle = remember { servicePosting.title }
    val originalDescription = remember { servicePosting.description }
    val originalRate = remember { servicePosting.rate }
    var selectedCategories = remember { mutableStateListOf<String>() }

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
                Text(
                    text = "Est. Budget: ${editableRate}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "Category: ${
                        if (selectedCategories.isEmpty()) "Uncategorized"
                        else selectedCategories.joinToString(", ")
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
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
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
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            )
                        )
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
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
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
                                "Plumbing", "Carpentry", "Electrical",
                                "Home Cleaning", "Painter and Decorator", "Fence Installer"
                            )

                            categories.forEach { category ->
                                val isSelected = selectedCategories.contains(category)
                                Box(
                                    modifier = Modifier
                                        .clickable {
                                            if (isSelected) selectedCategories.remove(category)
                                            else selectedCategories.add(category)
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
                        Button(onClick = {
                            // Restore the original values if cancel is clicked
                            editableTitle = originalTitle
                            editableDescription = originalDescription
                            editableRate = originalRate
                            isDialogVisible = false
                        }) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            // Save the new values
                            isDialogVisible = false
                            onEditClick(editableTitle, editableDescription, editableRate)
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
            .background(Color.LightGray)
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White,
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
fun SettingsScreen() {
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
            onClick = { /* Handle Privacy click */ }
        )
        GeneralSettings(
            icon = ImageVector.vectorResource(id = R.drawable.ic_about),
            title = "About Us",
            description = "Know more about our team.",
            onClick = { /* Handle About Us click */ }
        )
        GeneralSettings(
            icon = ImageVector.vectorResource(id = R.drawable.ic_report),
            title = "Report a Problem",
            description = "Report a problem.",
            onClick = { /* Handle Report a Problem click */ }
        )
        GeneralSettings(
            icon = ImageVector.vectorResource(id = R.drawable.ic_logout),
            title = "Log Out",
            description = "",
            onClick = { /* Handle Log Out click */ }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FabPosting(
    onPostNewService: (ServicePosting) -> Unit // Pass a function to handle new post submission
) {
    var isDialogVisible by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") } // Use simple variables for input
    var description by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var selectedCategories = remember { mutableStateListOf<String>() }

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
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )
                    )

                    // Description TextField
                    TextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )
                    )

                    // Rate TextField
                    TextField(
                        value = rate,
                        onValueChange = { rate = it },
                        label = { Text("Estimated Budget") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )
                    )

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
                                "Plumbing", "Carpentry", "Electrical",
                                "Home Cleaning", "Painter and Decorator", "Fence Installer"
                            )

                            categories.forEach { category ->
                                val isSelected = selectedCategories.contains(category)
                                Box(
                                    modifier = Modifier
                                        .clickable {
                                            if (isSelected) selectedCategories.remove(category)
                                            else selectedCategories.add(category)
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
                                rate = rate.toString(),
                                postedDate = System.currentTimeMillis().toString(), // Current timestamp
                                isActive = true, // Default to active
                                category = selectedCategories.joinToString(", "), // Join selected categories
                                applicantsCount = 0 // Initial count of applicants
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
