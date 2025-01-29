package com.example.androidproject.view.pages2

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidproject.view.Tradesman

@Composable
fun TradesmanApply(trade: Tradesman, navController: NavController) {
    val items = listOf("Skill 1", "Skill 2", "Skill 3") // Your list of items

    Column( // Change Box to Column
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
            shape = RectangleShape
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
                            .clickable { navController.navigate("main_screen") }
                            .padding(16.dp),
                        tint = Color(0xFF81D796)
                    )


                    Text(
                        text = "Bookings Details",
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
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(trade.imageResId),
                        contentDescription = "Tradesman Image",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(start = 10.dp)
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 15.dp)
                    ) {
                        Text(
                            text = trade.username,
                            color = Color.Black,
                            fontWeight = FontWeight(500),
                            fontSize = 20.sp,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        Text(
                            text = trade.category,
                            color = Color.Black,
                            fontSize = 16.sp
                        )

                        Box(
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // First section
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = "Bookmark Icon",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.size(4.dp))
                                    Text(
                                        text = "Location",
                                        fontSize = 12.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp)) // Spacing between the two sections

                                // Second section
                                Row(modifier = Modifier.clickable { /* Add to Bookmark Action */ }
                                    ,verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = "Bookmark Icon",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.size(4.dp))
                                    Text(
                                        text = "Add to bookmark",
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                }
                Spacer(Modifier.height(8.dp))
                Row (modifier = Modifier.fillMaxWidth().padding(horizontal =25.dp), horizontalArrangement = Arrangement.SpaceBetween){
                    Text(text = trade.category, fontSize = 24.sp, fontWeight = FontWeight(500))
                    Text(text = "Submissions (0)")
                }
                Text(text = "Posted on January 5, 2025 - Active",Modifier.padding(horizontal = 25.dp))

                Card(modifier = Modifier.fillMaxWidth().height(150.dp),
                    border = BorderStroke(0.5.dp, Color(0xFFD9D9D9)),
                    colors = CardDefaults.cardColors(Color.White),
                    shape = RectangleShape
                ){
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Description of the Plumbing Repair service")
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    border = BorderStroke(0.5.dp, Color(0xFFD9D9D9)),
                    colors = CardDefaults.cardColors(Color.White),
                    shape = RectangleShape
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 20.dp)
                    ) {
                        // First Column
                        Column(
                            modifier = Modifier
                                .weight(1f) // Allocate equal horizontal space
                        ) {
                            Text(text = "P200/hr")
                            Text(text = "Estimated Budget",color = Color.Gray)
                        }

                        // Second Column
                        Column(
                            modifier = Modifier
                                .weight(1f) // Allocate equal horizontal space
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Location Icon",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Lingayen, Pangasinan")
                            }
                            Text(text = "Location",Modifier.padding(start = 20.dp), color = Color.Gray)
                        }
                    }
                }
                Card(modifier = Modifier.fillMaxWidth().height(150.dp),
                    border = BorderStroke(0.5.dp, Color(0xFFD9D9D9)),
                    colors = CardDefaults.cardColors(Color.White),
                    shape = RectangleShape
                ){
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 10.dp), verticalArrangement = Arrangement.Center,
                       ) {
                        Text(
                            text = "Specialties",
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight(500),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),

                            horizontalArrangement = Arrangement.spacedBy(16.dp) // Distributes the boxes evenly
                        ) {
                            items.forEach { item ->
                                Box(
                                    modifier = Modifier
                                        .size(120.dp,50.dp)
                                        .background(Color(0xFFF1F1F1))
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(12.dp)),

                                ) {
                                    // Content for each Box
                                    Text(
                                        text = item,
                                        modifier = Modifier.align(Alignment.Center),
                                        color = Color.Black,
                                    )
                                }
                            }
                        }
                    }
                }
                Row (Modifier.fillMaxWidth().height(150.dp),horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                    Text(text = "Other services needed by this client (0)", fontSize = 20.sp, fontWeight = FontWeight(500))

                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier.width(200.dp),
                        colors = ButtonDefaults.buttonColors(Color.White),
                        border = BorderStroke(1.dp, Color.Black)
                    ) {
                        Text(
                            text = "Apply Now",
                            fontSize = 16.sp,
                            fontWeight = FontWeight(500),
                            color = Color.Black
                        )
                    }
                }




            }


        }

    }

}