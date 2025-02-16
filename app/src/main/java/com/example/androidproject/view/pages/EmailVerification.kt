package com.example.androidproject.view.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun EmailVerification(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var showOtpField by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF81D796))
    ) {
        // Top Header Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .padding(16.dp),
                tint = Color.White
            )

            Text(
                text = "Email Verification",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f) .padding(top = 15.dp, end = 50.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Card containing email verification
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.9f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Email Input
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // OTP Row (Button + OTP TextField)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { showOtpField = true },
                            modifier = Modifier
                        ) {
                            Text("Get OTP")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        if (showOtpField) {
                            OutlinedTextField(
                                value = otp,
                                onValueChange = { otp = it },
                                label = { Text("OTP") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirm Verification Button
                    if (showOtpField) {
                        Button(
                            onClick = { navController.navigate("changepassword") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
    }
}
