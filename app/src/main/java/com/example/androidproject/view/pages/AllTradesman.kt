package com.example.androidproject.view.ClientPov

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidproject.R
import com.example.androidproject.model.client.resumesItem
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.viewmodel.Resumes.GetResumesViewModel

@Composable
fun AllTradesman(navController: NavController, getResumes: GetResumesViewModel) {
    val resumeState by getResumes.resumeState.collectAsState()

    LaunchedEffect(Unit) {
        getResumes.getResumes()
    }

    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 120.dp
        WindowType.MEDIUM -> 140.dp
        WindowType.LARGE -> 160.dp
    }

    val textSize = when (windowSize.width) {
        WindowType.SMALL -> 14.sp
        WindowType.MEDIUM -> 16.sp
        WindowType.LARGE -> 18.sp
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                shape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp) // Rounded top corners
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .size(100.dp)
                        .padding(top = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Arrow Back",
                            Modifier.clickable { navController.navigate("main_screen") }
                                .padding(16.dp),
                            tint = Color.Black
                        )

                        Text(
                            text = "All Tradesman",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 15.dp, end = 50.dp)
                                .weight(1f)
                        )
                    }
                }
            }

            // Remove the verticalScroll modifier and ensure LazyColumn takes up the remaining space
            when (resumeState) {
                is GetResumesViewModel.ResumeState.Loading -> {
                    Text(text = "Loading...")
                }
                is GetResumesViewModel.ResumeState.Success -> {
                    val resume = (resumeState as GetResumesViewModel.ResumeState.Success).data
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize() // Ensure LazyColumn takes up the remaining space
                            .background(Color(0xFFECECEC)),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        itemsIndexed(resume) { index, resumes ->
                            AllTradesmanItem(resumes, navController, cardHeight, textSize)
                        }
                    }
                }
                is GetResumesViewModel.ResumeState.Error -> {
                    val errorMessage = (resumeState as GetResumesViewModel.ResumeState.Error).message
                    Log.d("testeralltradesman", "Error: $errorMessage")
                    Text(
                        text = "Error: $errorMessage",
                        color = Color.Red
                    )
                }
                else -> Unit
            }
        }
    }
}
@Composable
fun AllTradesmanItem(resumes: resumesItem, navController: NavController, cardHeight: Dp, textSize: TextUnit) {
    val windowSize = rememberWindowSizeClass()
    val iconSize = when (windowSize.width) {
        WindowType.SMALL -> 30.dp
        WindowType.MEDIUM -> 40.dp
        WindowType.LARGE -> 50.dp
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .clickable { navController.navigate("booknow/${resumes.id}")}, //implementation here
        shape = RoundedCornerShape(8.dp),


        ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.pfp),
                    contentDescription = "Tradesman Image",
                    modifier = Modifier
                        .size(cardHeight - 20.dp)
                        .padding(start = 10.dp)
                )
                Column(
                    modifier = Modifier
                        .size(250.dp, 100.dp)
                        .padding(start = 10.dp)
                )
                {
                    Text(
                        text = resumes.tradesmanfullname,
                        color = Color.Black,
                        fontWeight = FontWeight(500),
                        fontSize = textSize,
                        modifier = Modifier.padding(top = 10.dp)

                    )
                    Text(
                        text = "${resumes.specialties}"
                            .replace("[", "")  // Remove opening bracket
                            .replace("]", ""),  // Remove closing bracket ,
                        color = Color.Black,
                        fontSize = textSize,
                    )
                    Row(modifier = Modifier.size(185.dp, 110.dp)) {
                        Box(
                            modifier = Modifier
                                .size(70.dp, 50.dp)
                                .padding(top = 10.dp)
                                .background(
                                    color = (Color(0xFFFFF2DD)),
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            Text(
                                text = "P${resumes.workfee}/hr",
                                fontSize = textSize,
                                modifier = Modifier.padding(top = 5.dp, start = 8.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(70.dp, 50.dp)
                                .padding(top = 10.dp, start = 10.dp)
                                .background(
                                    color = (Color(0xFFFFF2DD)),
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
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 5.dp, start = 28.dp)
                            )
                        }
                    }


                }
                Image(painter = painterResource(id = R.drawable.bookmark),
                    contentDescription = "Bookmark Image",
                    modifier = Modifier
                        .size(iconSize)
                        .padding(end = 5.dp)
                        .clickable { }
                )
            }
        }
    }
}