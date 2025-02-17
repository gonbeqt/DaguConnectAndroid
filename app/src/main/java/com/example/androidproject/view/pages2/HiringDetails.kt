package com.example.androidproject.view.pages2

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.androidproject.R
import com.example.androidproject.view.theme.myGradient3

@Composable

fun HiringDetails(modifier: Modifier, navController: NavController) {
    var responseText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(myGradient3)
    ) {

        // Top Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier
                    .background(myGradient3)
                    .fillMaxWidth()
                    .size(80.dp)
                    .padding(top = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Arrow Back",
                        modifier = Modifier
                            .clickable { }
                            .padding(start = 16.dp, top = 12.dp, end = 12.dp, bottom = 14.dp),
                        tint = Color(0xFF81D796)
                    )
                    Text(
                        text = "Why Should We Hire You?",
                        fontSize = 20.sp,
                        color = Color.White,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .weight(1f)
                    )
                }
            }
        }

        // Main Content Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.whyhire),
                        tint = Color(0xFF42C2AE),
                        contentDescription = "Why should we hire you",
                        modifier = Modifier
                            .size(80.dp)
                            .padding(top = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Tell us why you're the best candidate:",
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Input field with placeholder
                    Box(
                        modifier = Modifier
                            .width(350.dp)
                            .height(350.dp)
                            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        if (responseText.isEmpty()) {
                            Text(
                                "Enter response here...",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                modifier = Modifier.align(Alignment.TopStart)
                            )
                        }
                        BasicTextField(
                            value = responseText,
                            onValueChange = { responseText = it },
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.End,

                    ) {
                        Text("Words: 0/300", color = Color.Gray, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.weight(1f)) // pushes the buttons to the bottom

                    // Buttons at the bottom
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color(0xFF42C2AE), RoundedCornerShape(12.dp))
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { }
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Cancel", fontSize = 14.sp, color = Color(0xFF42C2AE))
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = Color(0xFF42C2AE),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { }
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Submit", fontSize = 14.sp, color = Color.White)
                        }
                    }
                }
            }
        }

    }
}



