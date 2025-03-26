package com.example.androidproject.view.client

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidproject.data.preferences.AccountManager
import com.example.androidproject.view.extras.LoadingUI
import com.example.androidproject.view.extras.SnackbarController
import com.example.androidproject.viewmodel.ReportConcernViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ReportProblem(navController: NavController, reportConcernViewModel : ReportConcernViewModel ){
    var issueDescription by remember { mutableStateOf("") }
    val categories = listOf("Booking", "Chats", "Search", "Profile", "Feedback", "Other")
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val email = AccountManager.getAccount()?.email
    var isLoading by remember {mutableStateOf(false)}
    val reportConcernState by reportConcernViewModel.reportConcernState.collectAsState()

    LaunchedEffect(reportConcernState) {
        when(val reportConcern = reportConcernState){
            is ReportConcernViewModel.ReportConcernState.Loading -> {
                isLoading = true

            }
            is ReportConcernViewModel.ReportConcernState.Success -> {
                isLoading = false
                SnackbarController.show("Reported Problem Sent Successfully")
                navController.navigate("main_screen?selectedItem=4&selectedTab=1"){
                    navController.popBackStack()
                    reportConcernViewModel.resetState()
                }
        }
            is ReportConcernViewModel.ReportConcernState.Error -> {
                val error = reportConcern.message
                SnackbarController.show(error)
                isLoading = false
                reportConcernViewModel.resetState()
            }
            else -> Unit
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Enables scrolling
        ) {
            // Back Button and Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack, contentDescription = "Back",
                        tint = Color(0xFF00A99D)
                    )
                }
                Text(
                    text = "Report a Problem",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description Text
            Text(
                text = "Please describe the issue you’re facing, including any details or steps that led to the problem.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Multiline Input Field
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            ) {
                TextField(
                    value = issueDescription,
                    onValueChange = { issueDescription = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White), // Ensures background is white inside border
                    placeholder = { Text("Describe your issue here...") },
                    maxLines = 5,
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        containerColor = Color.White,
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Problem Category Selection
            Text(
                text = "What's the problem related to?",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    Button(
                        onClick = { selectedCategory = category },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedCategory == category) Color(0xFF42C2AE) else Color(
                                0xFFF1F1F1
                            ),
                            contentColor = if (selectedCategory == category) Color.White else Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Text(category)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Send Button
            Button(
                onClick = {
                    selectedCategory?.let {
                        if (email != null) {
                            reportConcernViewModel.reportConcern(
                                email, issueDescription,
                                it
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF42C2AE)
                )
            ) {
                Text("Send", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            SnackbarController.ObserveSnackbar()
        }
    }
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)) // Semi-transparent dark overlay
        )
        LoadingUI()
    }


}
