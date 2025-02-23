
package com.example.androidproject.view.pages2

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidproject.view.theme.myGradient3
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController


@Composable
fun ProfileVerification(modifier: Modifier = Modifier, navController: NavController) {
    var currentStep by remember { mutableStateOf(1) }
    var progressPercentage by remember { mutableStateOf(0) }
    var estimatedRate by remember { mutableStateOf("") }
    var selectedJob by remember { mutableStateOf("Select job category") }
    var selectedLocation by remember { mutableStateOf("Select location") }
    var aboutMe by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(myGradient3)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Icon(
                modifier = Modifier.padding(end = 10.dp),
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Arrow Back",
                tint = Color.White
            )
            Text(text = "Verify Profile", fontSize = 20.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.weight(1f)) { // allow this section to take remaining space
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(end = 10.dp),
                    text = "$progressPercentage%",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Complete",
                    color = Color(0xFFC1C1C1),
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(start = 20.dp),
                    text = when (currentStep) {
                        1 -> "Job Details"
                        2 -> "Personal Details"
                        3 -> "Credential and Verification"
                        4 -> "Preview and Submit"
                        else -> "Profile Verification"
                    },
                    color = Color(0xFFC1C1C1),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            StepProgressIndicator(currentStep = currentStep, totalSteps = 5)

            Card(
                modifier = Modifier
                    .padding(top = 14.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    if (currentStep == 1) {
                        Text(
                            modifier = Modifier.padding(start = 14.dp, top = 20.dp),
                            text = "Please provide your job details to continue.",
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            modifier = Modifier.padding(start = 14.dp),
                            text = "Job Title",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Box(modifier = Modifier.padding(start = 14.dp, end = 14.dp)) {
                            JobSelectionDropdown(
                                label = "",
                                options = listOf(
                                    "Plumber", "Carpenter", "Mason", "Cleaner", "Electrician"
                                ),
                                selectedOption = selectedJob,
                                onOptionSelected = { selectedJob = it }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            modifier = Modifier.padding(start = 14.dp),
                            text = "Preferred Work Location",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Box(modifier = Modifier.padding(start = 14.dp, end = 14.dp)) {
                            JobSelectionDropdown(
                                label = "",
                                options = listOf("New York", "Los Angeles", "Chicago", "Houston"),
                                selectedOption = selectedLocation,
                                onOptionSelected = { selectedLocation = it }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Column(modifier = Modifier.padding(start = 14.dp, end = 14.dp)) {
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = "Estimated rate:",
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 10.dp, vertical = 12.dp)
                            ) {
                                BasicTextField( //fixthis shit later
                                    value = estimatedRate,
                                    onValueChange = { newValue ->
                                        val cleanValue = newValue.filter { it.isDigit() }
                                        estimatedRate = if (cleanValue.isNotEmpty()) {
                                            "₱$cleanValue"
                                        } else {
                                            "₱" 
                                        }
                                    },
                                    textStyle = LocalTextStyle.current.copy(
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    ),
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number
                                    ),
                                    cursorBrush = SolidColor(Color.Black),
                                    singleLine = true
                                )

                                // Show placeholder if only ₱ is present
                                if (estimatedRate == "₱") {
                                    Text(
                                        text = "₱ Enter amount",
                                        fontSize = 16.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }else if (currentStep == 2){
                        // PERSONAL DETAILS SCREEN
                        Text(
                            modifier = Modifier.padding(start = 14.dp, top = 20.dp),
                            text = "Tell us about yourself.",
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            modifier = Modifier.padding(start = 14.dp),
                            text = "About Me",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Box(
                            modifier = Modifier
                                .padding(14.dp)
                                .fillMaxWidth()
                                .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
                                .padding(10.dp)
                        ) {
                            BasicTextField(
                                value = aboutMe,
                                onValueChange = { aboutMe = it },
                                textStyle = TextStyle(fontSize = 14.sp)
                            )
                        }

                    }
                    Column (Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(start = 14.dp, end = 14.dp, bottom = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF42C2AE), shape = RoundedCornerShape(10.dp))
                                .clickable {
                                    if (currentStep == 1){
                                        currentStep = 2
                                        progressPercentage = 25
                                    }

                                }
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Next",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun StepProgressIndicator(currentStep: Int, totalSteps: Int = 5) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp), // horizontal padding for better spacing
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 1..totalSteps) {
            Box(
                modifier = Modifier
                    .weight(1f) // ensures equal distribution of width
                    .height(8.dp)
                    .background(
                        color = if (i <= currentStep) Color(0xFF6FCF97) else Color.White,
                        shape = RoundedCornerShape(50)
                    )
                    .border(
                        width = 1.dp,
                        color = if (i <= currentStep) Color(0xFF6FCF97) else Color.White,
                        shape = RoundedCornerShape(50)
                    )
            )
            if (i != totalSteps) {
                Spacer(modifier = Modifier.width(4.dp)) //spacing between steps
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobSelectionDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                Icon(
                    Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown",
                    modifier = Modifier.clickable { expanded = true }
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.Transparent,
                focusedBorderColor = Color.LightGray,
                unfocusedBorderColor = Color.Gray,
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}



