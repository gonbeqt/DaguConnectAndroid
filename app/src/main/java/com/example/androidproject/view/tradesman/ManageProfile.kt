package com.example.androidproject.view.tradesman

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidproject.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.TextRange
import com.google.accompanist.flowlayout.FlowRow


@Composable
fun ManageProfile(modifier: Modifier = Modifier, navController: NavController){
    var estimatedRate by remember { mutableStateOf(TextFieldValue("₱100")) }
    var aboutMe by remember {
        mutableStateOf(
            TextFieldValue("")
        )
    }

    val allSkills = listOf("Skill 1", "Skill 2", "Skill 3", "Skill 4", "Skill 5", "Skill 6", "Skill 7")
    var selectedSkills by remember { mutableStateOf(setOf("Skill 2", "Skill 3", "Skill 7")) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // tob bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 8.dp)
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 16.dp),
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable{navController.navigate("profiletradesman")},
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.Black
            )
            Text(
                text = "Manage Profile and Skills",
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(R.drawable.profileandskills),
                        contentDescription = "Workers Images",
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 10.dp)
                            .height(150.dp)
                            .width(240.dp)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Showcase Your Profile & Expertise",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Update availability, set rates, share your bio, and showcase specialties to attract opportunities.",
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            item {
                var isAvailable by remember { mutableStateOf(true) } // track toggle state

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.padding(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Status:", fontWeight = FontWeight.Normal, fontSize = 16.sp)
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = if (isAvailable) "Available" else "Unavailable", // change text dynamically
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = if (isAvailable) Color.Blue else Color.Red // green when available, red when unavailable
                        )
                        Box(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .size(26.dp)
                                .background(Color.White, RoundedCornerShape(50.dp))
                                .clickable { navController.navigate("availabilitystatus") }
                                .border(2.dp, Color.Black, RoundedCornerShape(50.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.QuestionMark,
                                contentDescription = "Edit profile and skills",
                                tint = Color.Black
                            )
                        }
                    }

                    // Toggle Icon
                    Icon(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(50.dp)
                            .clickable { isAvailable = !isAvailable }, // Toggle state on click
                        imageVector = if (isAvailable) Icons.Default.ToggleOn else Icons.Default.ToggleOff, // Change icon
                        contentDescription = "Toggle status",
                        tint = Color.Black
                    )
                }
            }

            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    Text(text = "Estimated rate:", fontWeight = FontWeight.Normal, fontSize = 16.sp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        // Placeholder
                        if (estimatedRate.text == "₱") {
                            Text("₱ Enter amount", color = Color.Gray, fontSize = 16.sp)
                        }

                        BasicTextField(
                            value = estimatedRate,
                            onValueChange = { newValue ->
                                val text = newValue.text

                                if (text.isEmpty() || text == "₱") {
                                    estimatedRate = TextFieldValue("₱", TextRange(1)) // keeps cursor after ₱
                                } else if (!text.startsWith("₱")) {
                                    estimatedRate = TextFieldValue("₱" + text.filter { it.isDigit() }, TextRange(text.length + 1))
                                } else {
                                    estimatedRate = newValue.copy(selection = TextRange(newValue.text.length)) // keeps cursor at the end
                                }
                            },
                            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    Text(text = "About Me:", fontWeight = FontWeight.Normal, fontSize = 16.sp)

                    // textfield with placeholder
                    BasicTextField(
                        value = aboutMe,
                        onValueChange = { aboutMe = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                        decorationBox = { innerTextField ->
                            if (aboutMe.text.isEmpty()) {
                                Text(
                                    text = "Example: I'm a licensed plumber with over 7 years of experience handling everything from leak repairs to full plumbing system installations.",
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }
                            innerTextField()
                        }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // selected skills section
            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    Text(text = "Specialties", fontWeight = FontWeight.Normal, fontSize = 16.sp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .border(1.dp, Color.DarkGray, RoundedCornerShape(10.dp))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedSkills.isEmpty()) {
                            Text(
                                text = "Provide at least 3 maximum skill set",
                                textAlign = TextAlign.Start,
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        } else {
                            // selected Skills
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                selectedSkills.forEach { skill ->
                                    Box(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .background(Color(0xFFB3E5FC), RoundedCornerShape(50.dp))
                                            .clickable {
                                                selectedSkills = selectedSkills - skill
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Text(text = skill, fontSize = 14.sp, color = Color.Black)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Remove skill",
                                                tint = Color.Black,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // available skill section
            item {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    mainAxisSpacing = 8.dp, // Spacing between items horizontally
                    crossAxisSpacing = 8.dp // Spacing between rows
                ) {
                    allSkills.filter { it !in selectedSkills }.forEach { skill ->
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFE0E0E0), RoundedCornerShape(50.dp))
                                .clickable {
                                    selectedSkills = selectedSkills + skill
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = skill,
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
            item {
                Button(
                    onClick = { navController.navigate("profiletradesman") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42C2AE)),
                    modifier = Modifier.padding(16.dp).fillMaxWidth().background(Color(0xFF42C2AE), RoundedCornerShape(8.dp))
                ) {
                    Text(text = "Save Changes")
                }
            }
        }

    }
}