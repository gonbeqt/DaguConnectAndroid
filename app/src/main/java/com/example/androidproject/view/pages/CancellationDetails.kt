package com.example.androidproject.view.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
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
import com.example.androidproject.view.Tradesman

@Composable
fun CancellationDetails(trade: Tradesman, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9D9D9))
    ) {
        // Header Section
        HeaderSection(navController)

        // Content Section
        ContentSection(trade)

        // Book Again Button
        BookAgainButton()
    }
}

@Composable
fun HeaderSection(navController: NavController) {

        // Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp)
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
                        text = "Cancellation Details", // Updated title
                        fontSize = 24.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .weight(1f)
                    )
                }
            }
        }

}

@Composable
fun ContentSection(trade: Tradesman) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        // Cancellation Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.declined),
                    contentDescription = "Declined Image",
                    modifier = Modifier.size(100.dp)
                )
                Text(
                    text = "Cancellation Completed",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "on 26-01-25 10:59",
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tradesman Details Card
        TradesmanDetailsCard(trade)

        Spacer(modifier = Modifier.height(16.dp))

        // Client Details Section
        ClientDetails()
    }
}

@Composable
fun TradesmanDetailsCard(trade: Tradesman) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp).
                    padding(top = 15.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(trade.imageResId),
                contentDescription = "Tradesman Image",
                modifier = Modifier.size(90.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = trade.username,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = trade.category,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RatingBox(trade.rate)
                    Spacer(modifier = Modifier.width(8.dp))
                    ReviewsBox(trade.reviews.toString())
                }
            }
        }
    }
}

@Composable
fun ClientDetails() {
    Card(colors = CardDefaults.cardColors(Color.White)){
        Column(modifier = Modifier.padding(20.dp)) {
            LabelValuePair(label = "Requested by", value = "Client")
            Spacer(modifier = Modifier.height(8.dp))
            LabelValuePair(label = "Request date and time", value = "26-01-25 10:59")
            Spacer(modifier = Modifier.height(8.dp))
            LabelValuePair(label = "Reason", value = "Change of mind")
        }
    }

}

@Composable
fun BookAgainButton() {
    Spacer(Modifier.height(150.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(80.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .clickable {  }
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .border(1.dp, Color(0xFFECAB1E), shape = RoundedCornerShape(12.dp))
                .padding(8.dp)
                .size(300.dp, 30.dp),
            contentAlignment = Alignment.Center
        ) {

            Text(text = "Book Again", color = Color(0xFFECAB1E), fontSize = 14.sp, fontWeight = FontWeight(500))
        }
    }

}

@Composable
fun LabelValuePair(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray)
        Text(text = value, color = Color.Gray)
    }
}

@Composable
fun RatingBox(rate: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFFFF2DD), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text = rate, fontSize = 14.sp)
    }
}

@Composable
fun ReviewsBox(reviews: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFFFF2DD), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star Icon",
                tint = Color(0xFFFFA500),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(text = reviews, fontSize = 14.sp)
        }
    }
}
