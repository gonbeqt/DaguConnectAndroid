
package com.example.androidproject.view.pages2

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.androidproject.R


@Composable
fun ProfileVerification(modifier: Modifier = Modifier, navController: NavController) {
    var currentStep by remember { mutableStateOf(1) }
    var progressPercentage by remember { mutableStateOf(0) }
    var estimatedRate by remember { mutableStateOf("") }
    val isTyping = estimatedRate.isNotEmpty()
    var selectedJob by remember { mutableStateOf("Select job category") }
    var selectedLocation by remember { mutableStateOf("Select location") }
    var aboutMe by remember { mutableStateOf("") }
    val context = LocalContext.current
    var selectedID by remember { mutableStateOf("Select type of valid ID") }
    var selectedDocument by remember { mutableStateOf("Select type of document") }



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
                        .padding(10.dp)
                ) {
                    if (currentStep == 1) { //first page
                        Text(
                            modifier = Modifier.padding(start = 14.dp, top = 14.dp),
                            text = "Please provide your job details to continue.",
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            modifier = Modifier.padding(start = 14.dp),
                            text = "Job Title",
                            fontSize = 14.sp,
                            color = Color.DarkGray
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
                            text = "Preferred Location",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )

                        Box(modifier = Modifier.padding(start = 14.dp, end = 14.dp)) {
                            JobSelectionDropdown(
                                label = "",
                                options = listOf(
                                    "Agno", "Aguilar", "Alcala", "Anda", "Asingan", "Balungao", "Bani", "Basista", "Bautista",
                                    "Bayambang", "Binalonan", "Binmaley", "Bolinao", "Bugallon", "Burgos", "Calasiao",
                                    "Dagupan City", "Dasol", "Infanta", "Labrador", "Laoac", "Lingayen", "Mabini", "Malasiqui",
                                    "Manaoag", "Mangaldan", "Mangatarem", "Mapandan", "Natividad", "Pozorrubio", "Rosales",
                                    "San Fabian", "San Jacinto", "San Manuel", "San Nicolas", "San Quintin", "Santa Barbara",
                                    "Santa Maria", "Santo Tomas", "Sison", "Sual", "Tayug", "Umingan", "Urbiztondo",
                                    "Urdaneta City", "Villasis"
                                ),
                                selectedOption = selectedLocation,
                                onOptionSelected = { selectedLocation = "$it, Pangasinan" }
                            )
                        }


                        Spacer(modifier = Modifier.height(16.dp))

                        Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = "Estimated rate:",
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 14.dp, vertical = 18.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "₱",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        style = TextStyle(fontSize = 14.sp),
                                        color = if (isTyping) Color.Black else Color.Gray
                                    )

                                    BasicTextField(
                                        value = estimatedRate,
                                        onValueChange = { newText ->
                                            estimatedRate = if (newText.startsWith("₱")) {
                                                newText.substring(1) // remove extra peso signs
                                            } else newText
                                        },
                                        textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number),
                                        modifier = Modifier
                                            .padding(start = 4.dp)
                                            .fillMaxWidth(),
                                        decorationBox = { innerTextField ->
                                            if (estimatedRate.isEmpty()) {
                                                Text(text = "Enter Amount",
                                                    color = Color.Gray,
                                                    style = TextStyle(fontSize = 14.sp),
                                                    fontSize = 14.sp,
                                                )
                                            }
                                            innerTextField()
                                        }
                                    )
                                }


                            }
                        }
                    }else if (currentStep == 2){ //second page
                        // PERSONAL DETAILS SCREEN
                        Text(
                            modifier = Modifier.padding(start = 14.dp, top = 14.dp),
                            text = "Tell us about yourself.",
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            modifier = Modifier.padding(start = 14.dp),
                            text = "About Me",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )

                        Box(
                            modifier = Modifier
                                .padding(top = 4.dp, start = 14.dp, end = 14.dp)
                                .fillMaxWidth()
                                .height(300.dp)
                                .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
                                .padding(10.dp) // Adds padding inside the box
                        ) {
                            BasicTextField(
                                value = aboutMe,
                                onValueChange = { newText ->
                                    if (newText.length <= 1000) {
                                        // If it's within 1000 chars, just set it.
                                        aboutMe = newText
                                    } else {
                                        // If the pasted text is more than 1000 chars, truncate it.
                                        aboutMe = newText.substring(0, 1000)
                                        Toast.makeText(context, "Character count exceeds", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                textStyle = TextStyle(fontSize = 14.sp),
                                modifier = Modifier.fillMaxSize(),
                                decorationBox = { innerTextField ->
                                    if (aboutMe.isEmpty()) {
                                        Text(text = "Enter response here...",
                                            fontSize = 14.sp,
                                            color = Color.Gray,
                                            style = TextStyle(fontSize = 14.sp)
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                        }

                        Row(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "${aboutMe.length}/1000",
                                fontSize = 14.sp,
                                color = if (aboutMe.length >= 910) Color.Red else Color.Black
                            )
                        }

                    }
                    else if (currentStep == 3) { // Third page
                        Text(
                            modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 14.dp),
                            text = "Please upload a valid ID and certifications to verify your legitimacy as a tradesman.",
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            modifier = Modifier.padding(start = 14.dp),
                            text = "Valid ID",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )

                        Box(
                            modifier = Modifier.padding(start = 14.dp, end = 14.dp)
                        ) {
                            JobSelectionDropdown(
                                label = "",
                                options = listOf(
                                    "Passport",
                                    "Driver’s License",
                                    "National ID (PhilSys ID)",
                                    "Social Security System (SSS) ID",
                                    "Government Service Insurance System (GSIS) ID",
                                    "Professional Regulation Commission (PRC) ID",
                                    "Unified Multi-Purpose ID (UMID)",
                                    "Postal ID",
                                    "Voter’s ID",
                                    "Taxpayer Identification Number (TIN) ID",
                                    "Senior Citizen ID",
                                    "Person with Disability (PWD) ID",
                                    "Barangay Clearance with photo",
                                    "Indigenous People’s (IP) ID",
                                    "Other Valid ID with photo"
                                ),
                                selectedOption = selectedID,
                                onOptionSelected = { selectedID = it }
                            )
                        }

                        Row(
                            Modifier.fillMaxWidth().padding(start = 14.dp, end = 14.dp, top = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Front ID: No file uploaded",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(4.dp)
                            )
                            Box(
                                modifier = Modifier.clickable { }
                                    .background(Color(0xFF3E5CE1), RoundedCornerShape(4.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.upload_ic),
                                        contentDescription = "Add File",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Add File",
                                        fontSize = 12.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Text(
                            modifier = Modifier.padding(start = 18.dp, top = 4.dp),
                            text = "Accepted file types: .jpg, .jpeg, .png",
                            fontStyle = FontStyle.Italic,
                            fontSize = 10.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            Modifier.fillMaxWidth().padding(start = 14.dp, end = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Back ID: No file uploaded",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(4.dp)
                            )
                            Box(
                                modifier = Modifier.clickable { }
                                    .background(Color(0xFF3E5CE1), RoundedCornerShape(4.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.upload_ic),
                                        contentDescription = "Add File",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Add File",
                                        fontSize = 12.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Text(
                            modifier = Modifier.padding(start = 18.dp, top = 4.dp),
                            text = "Accepted file types: .jpg, .jpeg, .png",
                            fontStyle = FontStyle.Italic,
                            fontSize = 10.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            modifier = Modifier.padding(start = 14.dp),
                            text = "Trade Credential",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )

                        Box(
                            modifier = Modifier.padding(start = 14.dp, end = 14.dp)
                        ) {
                            JobSelectionDropdown(
                                label = "",
                                options = listOf(
                                    "Trade Certification",
                                    "License/Registration from a Trade Authority",
                                    "Apprenticeship Completion Certificate",
                                    "Union Membership Card",
                                    "Training Course Certificate",
                                    "Contractor/Business License",
                                    "Work Experience Letter from Employers",
                                    "Professional Association Membership",
                                    "Compliance Certifications",
                                    "Government-Issued Skill Assessment",
                                    "Other Valid Document with Photo"
                                ),
                                selectedOption = selectedDocument,
                                onOptionSelected = { selectedDocument = it }
                            )
                        }

                        Row(
                            Modifier.fillMaxWidth().padding(start = 14.dp, end = 14.dp, top = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "File: No file uploaded",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(4.dp)
                            )
                            Box(
                                modifier = Modifier.clickable { }
                                    .background(Color(0xFF3E5CE1), RoundedCornerShape(4.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.upload_ic),
                                        contentDescription = "Add File",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Add File",
                                        fontSize = 12.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Text(
                            modifier = Modifier.padding(start = 18.dp, top = 4.dp),
                            text = "Accepted file type: .pdf",
                            fontStyle = FontStyle.Italic,
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }

                    else if (currentStep == 4) { //fourth page
                        Text(
                            modifier = Modifier.padding(start = 14.dp, top = 14.dp),
                            text = "Your're almost done! Kindly review your details and submit when ready.",
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Column(modifier = Modifier.padding(start = 14.dp, end = 14.dp)) {
                            Text("Job:")
                            Text("Preferred Location:")
                            Text("Estimated Rate:")
                            Text("Phone Number:")
                            Text("About Me:")
                            Text("Valid ID:")
                            Text("Trade Credential:")
                        }
                    } else if (currentStep == 5) {//fifth page
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.pendingapproval_ic),
                                contentDescription = "Pending Approval",
                                modifier = Modifier.size(100.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "Pending Approval",
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                                Text(
                                    text = "Profile submitted! Please wait for confirmation to complete verification. This may take a few minutes.",
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )

                            }
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
                                .background(Color(0xFF42C2AE), shape = RoundedCornerShape(6.dp))
                                .clickable {
                                    if (currentStep == 1){
                                        currentStep = 2
                                        progressPercentage = 25
                                    }else if (currentStep == 2){
                                        currentStep = 3
                                        progressPercentage = 50
                                    }else if (currentStep == 3){
                                        currentStep = 4
                                        progressPercentage = 75
                                    }else if(currentStep == 4){
                                        currentStep = 5
                                        progressPercentage = 100
                                    }

                                }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when(progressPercentage){
                                    75 -> "Submit"
                                    100 -> "Done"
                                    else -> "Next"

                                },
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
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

    val isPlaceholder = selectedOption == "Select job category" || selectedOption == "Select location" || selectedOption == "Select type of valid ID" || selectedOption == "Select type of document"
    val textColor = if (isPlaceholder) Color.Gray else Color.Black  // text gray when unselected

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },  // keep label gray
            readOnly = true,
            textStyle = TextStyle(color = textColor),  // apply correct text color
            trailingIcon = {
                Icon(
                    Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown",
                    modifier = Modifier
                        .clickable { expanded = true }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.Transparent,
                focusedBorderColor = Color.LightGray,
                unfocusedBorderColor = Color.Gray,
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(310.dp).background(Color.White)//width and bg color of the dropdown
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
