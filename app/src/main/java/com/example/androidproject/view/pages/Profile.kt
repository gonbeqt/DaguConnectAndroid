package com.example.androidproject.view.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.androidproject.R
import com.example.androidproject.view.ServicePosting

@Preview
@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabNames = listOf("My Posts", "General")

    val servicePostings = listOf(
        ServicePosting("Plumbing Repair", "January 25, 2025", applicantsCount = 5),
        ServicePosting(
            "Electrical Repair", "January 20, 2025", isActive = false, applicantsCount = 3
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        // top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 8.dp)
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 16.dp),
        ) {
            Text(
                text = "Profile",
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color.Cyan,
                modifier = Modifier.padding(end = 16.dp)
            )

            Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = "Chat",
                tint = Color.Cyan
            )
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
                .padding(top = 2.dp) // Add separation from the white background
        ) {
            when (selectedTabIndex) {
                0 -> MyPostsTab(servicePostings)
                1 -> SettingsScreen()
            }
        }
    }
}


@Composable
fun MyPostsTab(servicePostings: List<ServicePosting>) {
    var postsList by remember { mutableStateOf(servicePostings) }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        servicePostings.forEach { posting ->
            PostsCard(
                servicePosting = posting,
                onEditClick = { title, description, rate ->
                    // update the deets in posting card
                    postsList = postsList.map {
                        if (it.title == posting.title) {
                            it.copy(title = title, description = description, rate = rate)
                        } else {
                            it
                        }
                    }
                },
                onApplicantsClick = { /* Handle applicants click */ }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
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
                                modifier = Modifier.padding(start = 8.dp),
                                textAlign = TextAlign.Start
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForwardIos,
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
                    text = "Rate: ${editableRate}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
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

    // Dialog for editing posting
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
                            label = { Text("Rate") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            )
                        )
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