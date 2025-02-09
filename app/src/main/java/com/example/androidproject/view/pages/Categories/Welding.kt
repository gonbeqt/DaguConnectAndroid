package com.example.androidproject.view.ClientPov.Categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.model.client.resumesItem
import com.example.androidproject.view.Tradesman
import com.example.androidproject.viewmodel.Resumes.GetResumesViewModel

@Composable
fun Welding(navController: NavController, getResumesViewModel: GetResumesViewModel){
val ResumeState by getResumesViewModel.resumeState.collectAsState()

    LaunchedEffect(Unit) {
        getResumesViewModel.getResumes()
    }




    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
                        painter = painterResource(R.drawable.weldingbg),
                        contentDescription = "Welding Background",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )

                    // Icon & Title
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
                                .clickable { navController.navigate("main_screen") }
                                .padding(8.dp)
                                .size(24.dp),
                            tint = Color.White
                        )
                    }

                    Text(
                        text = "Welding Work",
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
                            text = "Find skilled carpenters for custom woodwork, repairs, and installations, delivering high-quality craftsmanship.",
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
                        when(ResumeState){
                            is GetResumesViewModel.ResumeState.Loading ->{
                                // Show a loading indicator if needed
                                Text(
                                    text = "Loading...",
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                            }
                            is GetResumesViewModel.ResumeState.Success ->{
                                val resume = (ResumeState as GetResumesViewModel.ResumeState.Success).data

                                val welding = resume.filter{it.specialties.contains("Welding")}
                                // LazyColumn with CompletedItem layout
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .size(500.dp)
                                        .background(Color(0xFFF9F9F9)),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    items(welding.size) { index ->
                                        val resumes = welding[index]
                                        WeldingItem(resumes, navController)
                                    }
                                }
                            }
                            is GetResumesViewModel.ResumeState.Error ->{
                                // Show an error message if needed
                                Text(
                                    text = "Error loading resumes. Please try again later.",
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                            }
                            else -> Unit
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun WeldingItem(resume: resumesItem, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            // Profile Picture
            AsyncImage(
                model = resume.profilepic,
                contentDescription = resume.tradesmanfullname,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(25.dp)) // Apply rounded corners
                    .background(Color.Gray, RoundedCornerShape(25.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Name and Category
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text =  resume.tradesmanfullname,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

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
                            text = "P${resume.workfee}/hr",
                            fontSize = 16.sp,
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
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 5.dp, start = 28.dp)
                        )
                    }

                }
            }
        }
    }
}