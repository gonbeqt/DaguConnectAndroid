package com.example.androidproject.view.pages.Categories

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.model.client.resumesItem
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.viewmodel.Resumes.GetResumesViewModel

@Composable
fun Roofing(navController: NavController,getResumesViewModel: GetResumesViewModel){
    val roofingList = getResumesViewModel.resumePagingData.collectAsLazyPagingItems()
    var displayedResumes by remember { mutableStateOf<List<resumesItem>>(emptyList()) }

    val dismissedResumes by getResumesViewModel.dismissedResumes
    LaunchedEffect(roofingList.itemSnapshotList, dismissedResumes) {
        Log.d("TradesmanColumn", "Updating displayed resumes")
        displayedResumes = roofingList.itemSnapshotList.items
            .filter { it.id !in dismissedResumes } // Remove dismissed
    }
    // Example: Call this after adding a new resume
    LaunchedEffect(Unit) {
        getResumesViewModel.invalidatePagingSource()
    }




    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(WindowInsets.statusBars.asPaddingValues())

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {
            // First Card (Header)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(0.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Background Image
                    Image(
                        painter = painterResource(R.drawable.roofingbg),
                        contentDescription = "Roofing Background",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF214A4C).copy(alpha = 0.6f))
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .align(Alignment.TopStart), // Align to the top for the icon
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Arrow Back",
                            modifier = Modifier
                                .clickable { navController.popBackStack() }
                                .padding(8.dp)
                                .size(24.dp),
                            tint = Color.White
                        )
                    }

                    Text(
                        text = "Roofing Work",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 36.dp, horizontal = 15.dp)
                            .align(Alignment.BottomStart)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .absoluteOffset(y = (-20).dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(Color(0xFFF9F9F9))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // "About" Section
                        Text(
                            text = "About",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "Find skilled roofing workers for installations, repairs, and maintenance, ensuring durable and weatherproof protection for your home.",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Start
                        )
                        Text(
                            text = "Expert",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        LazyColumn(

                            modifier = Modifier
                                .fillMaxSize() // Ensure LazyColumn takes up the remaining space
                                .background(Color.White),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            val filteredList = roofingList.itemSnapshotList.items.filter {it.specialties.contains("Roofing") && it.id !in dismissedResumes  }

                            if (filteredList.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillParentMaxSize()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "No Roofing workers",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Gray,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            } else {
                                items(filteredList.size) { index ->
                                    val roofingList = filteredList[index]
                                    if (roofingList != null && roofingList.id !in dismissedResumes) {
                                        RoofingItem(roofingList, navController){
                                            getResumesViewModel.dismissResume(roofingList.id)
                                        }
                                    }
                                }
                            }
                            item {
                                if (roofingList.loadState.append == LoadState.Loading) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                        }


                    }
                }
            }
        }
    }
}

@Composable
fun RoofingItem(roofing: resumesItem, navController: NavController,onUninterested: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var reportText by remember { mutableStateOf("") }
    val windowSize = rememberWindowSizeClass()
    val iconSize = when (windowSize.width) {
        WindowType.SMALL -> 25.dp
        WindowType.MEDIUM -> 35.dp
        WindowType.LARGE -> 45.dp
    }
    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 18.sp
        WindowType.MEDIUM -> 20.sp
        WindowType.LARGE -> 22.sp
    }
    val smallTextSize = when (windowSize.width) {
        WindowType.SMALL -> 14.sp
        WindowType.MEDIUM -> 16.sp
        WindowType.LARGE -> 18.sp
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    )  {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            // Profile Picture
            AsyncImage(
                model = roofing.profilepic,
                contentDescription = roofing.tradesmanfullname,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(25.dp)) // Apply rounded corners
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Name and Category
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Row (Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween){
                    Text(
                        text = roofing.tradesmanfullname,
                        fontSize = nameTextSize,
                        fontWeight = FontWeight.Bold
                    )
                    Box {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Menu Icon",
                            modifier = Modifier
                                .size(iconSize)
                                .clickable { showMenu = true }
                        )

                        // Popup Menu
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Report") },
                                onClick = {
                                    showMenu = false
                                    showReportDialog = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Uninterested") },
                                onClick = {
                                    showMenu = false
                                    onUninterested()
                                }
                            )
                        }
                    }
                }

                Row(modifier = Modifier.size(185.dp, 110.dp)) {
                    Box(
                        modifier = Modifier
                            .size(80.dp, 45.dp)
                            .padding(top = 10.dp)
                            .background(
                                color = (Color(0xFFD9D9D9)),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Text(
                            text = "P${roofing.workfee}/hr",
                            fontSize = smallTextSize,
                            modifier = Modifier.padding(top = 5.dp, start = 8.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(70.dp, 45.dp)
                            .padding(top = 10.dp, start = 10.dp)
                            .background(
                                color = (Color(0xFFD9D9D9)),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star, contentDescription = "Start Icon",
                            tint = Color(0xFFFFA500), modifier = Modifier
                                .size(25.dp)
                                .padding(top = 7.dp, start = 2.dp)
                        )
                        Text(
                            text = "4",
                            fontSize = smallTextSize,
                            modifier = Modifier.padding(top = 5.dp, start = 28.dp)
                        )
                    }

                }
            }
        }
    }
    if (showReportDialog) {
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = { Text("Report Tradesman") },
            text = {
                Column {
                    Text("Please enter a reason for reporting this tradesman:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = reportText,
                        onValueChange = { reportText = it },
                        placeholder = { Text("Enter report reason...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (reportText.isNotBlank()) {
                            println("Report submitted: $reportText")
                            showReportDialog = false
                        }
                    }
                ) {
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReportDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}