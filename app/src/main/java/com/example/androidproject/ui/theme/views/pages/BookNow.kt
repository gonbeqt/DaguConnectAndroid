package com.example.androidproject.ui.theme.views.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.androidproject.R
import com.example.androidproject.ui.theme.views.Feedback
import com.example.androidproject.ui.theme.views.Tradesman

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookNow(trade: Tradesman,feedback: Feedback, navController: NavController) {



    var booknow by remember { mutableStateOf(false) }

    // Tradesmen list (you can optimize this if needed)
    val tradesmen = listOf(
        Tradesman(R.drawable.pfp, "Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp, "Alex", "Electrical", "P600/hr", 4.8, R.drawable.bookmark)
    )
    val feedbackList = listOf(
        Feedback(R.drawable.pfp, "Ezekiel", 4),
        Feedback(R.drawable.pfp, "Ezekiel", 4),
    Feedback(R.drawable.pfp, "Ezekiel", 4),
    Feedback(R.drawable.pfp, "Ezekiel", 4)
    )



    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Main Content Area (Scrollable)

            // Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF81D796))
                    .verticalScroll(rememberScrollState()),
                    shape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp) // Rounded top corners
            ) {

                Column(
                    modifier = Modifier
                        .background(Color(0xFF81D796))
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
                                .padding(16.dp)
                            ,
                            tint = Color.White
                        )


                        Text(
                            text = "Expert Details",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 15.dp, end = 50.dp)
                                .weight(1f) // Ensures the text takes available space and is centered
                        )
                    }

                }


                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF81D796)),
                        shape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFFF9F9F9))
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
                                Row(
                                    modifier = Modifier.padding(top = 10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                            .clickable { /* Add to Bookmark Action */ }
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Bookmark,
                                                contentDescription = "Bookmark Icon",
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.size(4.dp))
                                            Text(
                                                text = "Add to bookmark",
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Color(0xFFFFF2DD),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Star Icon",
                                        tint = Color(0xFFFFA500),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.size(4.dp))
                                    Text(
                                        text = trade.reviews.toString(),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        // Additional Sections
                        Spacer(modifier = Modifier.height(4.dp))
                        Column(Modifier.padding(horizontal = 10.dp)) {
                            Text(
                                text = "About Me",
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight(500),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )
                            Text(
                                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                        "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                                modifier = Modifier.padding(horizontal = 8.dp),
                                fontWeight = FontWeight(500)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Specialties",
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight(500),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            BoxRow() // Assuming this function exists

                            Text(
                                text = "Ratings And Testimonials",
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight(500),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )

                            Text(
                                text = "Feedback from satisfied clients",
                                color = Color.Black,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .background(Color(0xFFF9F9F9)),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                feedbackList.forEach { feedback ->
                                    FeedbackItem(feedback) // Assuming this function exists
                                }
                            }
                        }
                    }
                    }
                    // Tradesman Details Section


        }

        // Fixed Buttons at the Bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.Transparent)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier
                    .clickable { navController.navigate("message_screen") }
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(1.dp, Color(0xFFECAB1E), shape = RoundedCornerShape(12.dp) )
                    .width(150.dp)
                    .padding(8.dp),
                    contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Message,
                        contentDescription = "Message Icon"
                    )
                    Text(text = "Chat Me")
                }
            }

            Box(
                modifier = Modifier
                    .clickable {navController.navigate("confirmbook") }
                    .background(
                        color = Color(0xFFECAB1E),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .width(150.dp)
                    .padding(8.dp),contentAlignment = Alignment.Center


            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,


                ) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = "Add to Cart Icon",
                        tint = Color.White
                    )
                    Text(
                        text = "Book Now",
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
        }
    }

}

@Composable
fun BoxRow() {
    val items = listOf("Skill 1", "Skill 2", "Skill 3") // Your list of items

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

        horizontalArrangement = Arrangement.Absolute.SpaceAround // Distributes the boxes evenly
    ) {
        items.forEach { item ->
            Box(
                modifier = Modifier
                    .size(120.dp,50.dp)
                    .background(Color(0xFFF1F1F1))
                    .padding(4.dp)
                    .clip(RoundedCornerShape(12.dp))

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

@Composable
fun FeedbackItem(feedback: Feedback) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(8.dp)
            ,
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Set the background color of the card
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Picture
            Image(
                painter = painterResource(feedback.ImageRes),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            )

             Text(
                    text = feedback.username,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                 fontSize = 16.sp
                )
                Text(
                    text = "${feedback.ratingstarsInt} stars",
                    color = Color.Gray
                )

        }
    }
}

