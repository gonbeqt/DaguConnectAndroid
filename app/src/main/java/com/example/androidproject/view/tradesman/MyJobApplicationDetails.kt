package com.example.androidproject.view.tradesman

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidproject.R
import com.example.androidproject.view.theme.myGradient3

@Composable
fun MyJobApplicationDetails(navController: NavController) {
    Column( // Change Box to Column
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9D9D9))
            .padding(WindowInsets.systemBars.asPaddingValues())

    ) {
        // Main Content Area (Scrollable)

        // Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
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
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Arrow Back",
                        Modifier
                            .clickable { navController.popBackStack() }
                            .padding(16.dp),
                        tint = Color(0xFF81D796)
                    )
                    Text(
                        text = "Job Application Details",
                        fontSize = 24.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .weight(1f) // Ensures the text takes available space and is centered
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp)) // Space between the two columns

        // Second Column with Card and content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp) // Keep card shape
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(brush = myGradient3) // Apply gradient background here
                        .padding(16.dp),
                    contentAlignment = Alignment.Center // Ensure padding is inside the gradient box
                ) {
                    Text(
                        text = "Your appointment is ",
                        fontSize = 20.sp,
                        color = Color.White,
                    )
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(200.dp),
                colors = CardDefaults.cardColors(Color.White),

                shape = RoundedCornerShape(0.dp, 0.dp, 15.dp, 15.dp) // Keep card shape
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.CenterStart
                ) {

                    // Tradesman details
                    Column(
                        modifier = Modifier
                            .padding(start = 20.dp)
                    ) {
                        Text(
                            text = "Qualifaction Summary",
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = 20.sp,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = " ASDSADASDASDSADASD",
                            color = Color.Black,
                            fontSize = 16.sp,
                        )


                    }

                }
            }
            Spacer(Modifier.height(16.dp))

            // Second Column with Card and content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),

                    colors = CardDefaults.cardColors(Color.White),
                    shape = RoundedCornerShape(15.dp) // Keep card shape
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Job’s Information",
                            fontSize = 24.sp,
                            color = Color.Black,
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .padding(vertical = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Tradesman image
                            Image(
                                painter = painterResource(R.drawable.pfp),
                                contentDescription = "Tradesman Image",
                                modifier = Modifier
                                    .size(100.dp)
                            )

                            // Tradesman details
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 10.dp)
                            ) {

                                Text(
                                    text = "LOOKING FOR",
                                    color = Color.Black,
                                    fontWeight = FontWeight(500),
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                                Text(
                                    text = "KARLOS RIVO",
                                    color = Color.Gray,
                                    fontWeight = FontWeight(500),
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Location:",
                                        color = Color.Black,
                                        fontSize = 16.sp,
                                    )
                                    Text(
                                        text = "Lagos, Nigeria",
                                        color = Color.Black,
                                        fontSize = 16.sp,
                                    )
                                }

                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Deadline:",
                                        color = Color.Black,
                                        fontSize = 16.sp,
                                    )
                                    Text(
                                        text = "March 24, 2005",
                                        color = Color.Black,
                                        fontSize = 16.sp,
                                    )
                                }

                            }
                        }

                    }
                }
                Spacer(Modifier.height(16.dp))

                // Third Column with Card and content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),

                        colors = CardDefaults.cardColors(Color.White),
                        shape = RoundedCornerShape(15.dp) // Keep card shape
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(

                                text = "Support Center",
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row() {
                                    Icon(
                                        imageVector = Icons.Default.Message,
                                        contentDescription = "Message Icon",
                                        modifier = Modifier
                                            .size(32.dp)
                                    )
                                    Text(
                                        text = "Contact Client",
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "Arrow Right Icon",
                                    modifier = Modifier
                                        .size(32.dp)
                                )
                            }
                            Spacer(Modifier.height(10.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row() {
                                    Icon(
                                        imageVector = Icons.Default.Help,
                                        contentDescription = "Help Icon",
                                        modifier = Modifier
                                            .size(32.dp)
                                    )
                                    Text(
                                        text = "Help",
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "Arrow Right Icon",
                                    modifier = Modifier
                                        .size(32.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

