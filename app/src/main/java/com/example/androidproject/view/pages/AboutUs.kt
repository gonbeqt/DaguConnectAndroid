package com.example.androidproject.view.ClientPov

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidproject.R

@Composable
fun AboutUs(navController: NavController){
        val faqs = listOf(
            "What is DaguConnect?" to "DaguConnect is a platform that connects clients with tradespeople.",
            "How do I sign up as a tradesperson?" to "You can sign up through the DaguConnect website or mobile app.",
            "Can clients contact tradespeople directly?" to "Yes, clients can contact tradespeople via the app.",
            "How do I receive job requests?" to "Job requests will appear in your dashboard once a client selects your service.",
            "Is there a way to verify a tradesperson’s credibility?" to "Yes, we have a review and rating system for verification.",
            "How are payments handled?" to "Payments are securely processed through our platform.",
            "What should I do if I encounter an issue?" to "You can reach out to our support team for assistance."
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Custom Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                IconButton(onClick = { navController.navigate("main_screen") }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back",
                        tint = Color(0xFF81D796)
                    )
                }
                Text(
                    text = "FAQs",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            // FAQ List
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(top = 16.dp)
            ) {
                item {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_faq), // Replace with actual drawable
                            contentDescription = "FAQ Icon",
                            tint = Color(0xFF00A99D),
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Frequently Asked Questions", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("Looking for answers? We're here to help!", fontSize = 14.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                items(faqs) { (question, answer) ->
                    ExpandableCard(question, answer)
                }
            }
        }

}

@Composable
fun ExpandableCard(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(Color(0xFFEDEDED))
    ) {
            Column(
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = question,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand/Collapse",
                        tint = Color(0xFF00A99D)
                    )
                }
                if (expanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = answer, fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    }

