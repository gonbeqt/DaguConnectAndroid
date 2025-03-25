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
fun ClientHelp(navController: NavController) {
    val faqs = listOf(
        "How do I sign up as a client on DaguConnect?" to "Signing up is easy! Download the DaguConnect app, select your role as a Client during registration, and complete your profile with your location and service preferences. This helps us match you with the right tradespeople for your needs.",
        "How can I find a tradesperson for my project?" to "As a client, you can browse through profiles of verified tradespeople on DaguConnect. Use our search filters to find professionals based on skills, location, availability, and ratings, ensuring you connect with the perfect match for your project.",
        "Can I contact tradespeople directly to discuss my needs?" to "Yes! DaguConnect offers secure in-app messaging, allowing you to communicate directly with tradespeople. Discuss your project requirements, ask questions, and get quotes—all within the safety of our platform.",
        "How do I know if a tradesperson is reliable?" to "We’ve got you covered! Clients can view feedback and ratings left by others on a tradesperson’s profile. These reviews are based on completed jobs, helping you make informed decisions and ensuring trust and credibility on the platform.",
        "How do I request a service from a tradesperson?" to "Once you find a tradesperson who fits your needs, simply send a service request through their profile. They’ll receive a notification and can respond to discuss your project details, timeline, and pricing.",
        "How are payments handled for clients?" to "DaguConnect currently facilitates connections between clients and tradespeople, but payments are handled independently. You can discuss and agree on payment terms directly with the tradesperson, ensuring transparency and flexibility.",
        "What should I do if I encounter an issue with a tradesperson?" to "If you face any issues, you can use the “Report a User” feature to flag inappropriate behavior or concerns. Our team will review your report promptly and take necessary actions to resolve the situation.",
        "Is my data secure as a client?" to "Absolutely! We prioritize your privacy by using Secure Token authentication to protect your data. Your personal information and communications are kept safe, so you can use DaguConnect with confidence.",
        "Can I leave feedback after a job is completed?" to "Yes, we encourage clients to leave feedback! After a job is done, you can rate the tradesperson and provide comments on your experience. Your feedback helps other clients and supports tradespeople in building their reputation.",
        "What types of services can I find on DaguConnect?" to "DaguConnect connects you with skilled tradespeople for a wide range of services, including home repairs, maintenance, plumbing, electrical work, carpentry, and more. Whatever your project, we’ll help you find the right professional."
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
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF81D796)
                )
            }
            Text(
                text = "About Us",
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

