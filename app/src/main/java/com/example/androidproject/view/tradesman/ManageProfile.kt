package com.example.androidproject.view.tradesman

import android.widget.Toast
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import com.example.androidproject.viewmodel.Tradesman_Profile.UpdateTradesmanDetailViewModel
import com.google.accompanist.flowlayout.FlowRow


@Composable
fun ManageProfile(modifier: Modifier = Modifier, navController: NavController,updateTradesmanDetailViewModel :UpdateTradesmanDetailViewModel){
    val updateDetailState by updateTradesmanDetailViewModel.updateTradesmanDetailState.collectAsState()
    var selectedLocation by remember { mutableStateOf("Select location") }
    var estimatedRate by remember { mutableStateOf("") } // Changed to String for simplicity
    var aboutMe by remember { mutableStateOf("")}
    val context = LocalContext.current

    LaunchedEffect(updateDetailState) {
        when(val updateDetails = updateDetailState){
            is UpdateTradesmanDetailViewModel.UpdateTradesmanDetailState.Loading-> {
                //loading
            }
            is UpdateTradesmanDetailViewModel.UpdateTradesmanDetailState.Success->{
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()
                updateTradesmanDetailViewModel.resetState()
                // Navigate to the "profile" screen and clear the back stack
                navController.navigate("main_screen?selectedItem=4&selectedTab=0") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }

            }
            is UpdateTradesmanDetailViewModel.UpdateTradesmanDetailState.Error -> {
                val error = updateDetails.message
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                updateTradesmanDetailViewModel.resetState()
            }
            else -> Unit
        }
    }
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
                    .clickable{  navController.navigate("main_screen?selectedItem=4&selectedTab=0") },
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Status:", fontWeight = FontWeight.Normal, fontSize = 16.sp, color = Color.DarkGray)
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
                    Text(text = "Preferred location:", fontWeight = FontWeight.Normal, fontSize = 16.sp)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    ) {
                        CustomDropdown(
                            selectedOption = selectedLocation,
                            onOptionSelected = { selectedLocation = "$it, Pangasinan" }
                        )

                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    Text(text = "Estimated rate:", fontWeight = FontWeight.Normal, fontSize = 16.sp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        // Placeholder
                        if (estimatedRate.isEmpty()) {
                            Text("₱ Enter amount", color = Color.Gray, fontSize = 16.sp)
                        }

                        BasicTextField(
                            value = estimatedRate,
                            onValueChange = { newValue ->
                                // Allow only digits
                                estimatedRate = newValue.filter { it.isDigit() }
                            },
                            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { innerTextField ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("₱ ", fontSize = 16.sp) // Peso sign as static prefix
                                    innerTextField()
                                }
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    Text(text = "About Me:", fontWeight = FontWeight.Normal, fontSize = 16.sp, color = Color.DarkGray)

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
                            if (aboutMe.isEmpty()) {
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
            item {
                Button(
                    onClick = {
                        updateTradesmanDetailViewModel.updateTradesmanDetails(aboutMe,selectedLocation,estimatedRate.toInt(), "090712312322")
                              },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42C2AE)),
                    modifier = Modifier.padding(16.dp).fillMaxWidth().background(Color(0xFF42C2AE), RoundedCornerShape(8.dp))
                ) {
                    Text(text = "Save Changes")
                }
            }
        }

    }
}

@Composable
fun CustomDropdown(
    selectedOption: String,
    onOptionSelected: (String) -> Unit,

) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Agno", "Aguilar", "Alcala", "Anda", "Asingan", "Balungao", "Bani", "Basista", "Bautista",
        "Bayambang", "Binalonan", "Binmaley", "Bolinao", "Bugallon", "Burgos", "Calasiao",
        "Dagupan City", "Dasol", "Infanta", "Labrador", "Laoac", "Lingayen", "Mabini", "Malasiqui",
        "Manaoag", "Mangaldan", "Mangatarem", "Mapandan", "Natividad", "Pozorrubio", "Rosales",
        "San Fabian", "San Jacinto", "San Manuel", "San Nicolas", "San Quintin", "Santa Barbara",
        "Santa Maria", "Santo Tomas", "Sison", "Sual", "Tayug", "Umingan", "Urbiztondo",
        "Urdaneta City", "Villasis")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .clickable { expanded = true }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = selectedOption,
                fontSize = 16.sp,
                color = if (selectedOption == "Select location") Color.Gray else Color.Black
            )

            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Dropdown",
                tint = Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { expanded = true }
            )
        }
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.width(330.dp).background(Color.White)
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
