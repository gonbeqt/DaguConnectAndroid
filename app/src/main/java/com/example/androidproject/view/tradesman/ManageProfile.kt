package com.example.androidproject.view.tradesman

import android.util.Log
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import com.example.androidproject.view.extras.SnackbarController
import com.example.androidproject.viewmodel.Tradesman_Profile.UpdateTradesmanDetailViewModel
import com.google.accompanist.flowlayout.FlowRow


@Composable
fun ManageProfile(
    modifier: Modifier = Modifier,
    navController: NavController,
    updateTradesmanDetailViewModel: UpdateTradesmanDetailViewModel,
    workLocation: String,
    number: String,
    rate: String,
    about: String
) {
    val updateDetailState by updateTradesmanDetailViewModel.updateTradesmanDetailState.collectAsState()

    // Initialize state with passed parameters
    var selectedLocation by remember { mutableStateOf(workLocation ?: "Select location") }
    var phoneNumber by remember { mutableStateOf(if (number.equals("null", ignoreCase = true)) "" else number ?: "") }
    var estimatedRate by remember { mutableStateOf(rate ?: "") }
    var aboutMe by remember { mutableStateOf(about ?: "") }

    val context = LocalContext.current
    var isPhoneValid by remember { mutableStateOf(phoneNumber.isEmpty() || "^09[0-9]{9}$".toRegex().matches(phoneNumber)) }
    val phoneRegex = "^09[0-9]{9}$".toRegex()

    // Store initial values for change detection
    val initialLocation by remember { mutableStateOf(workLocation ?: "Select location") }
    val initialPhoneNumber by remember { mutableStateOf(if (number.equals("null", ignoreCase = true)) "" else number ?: "") }
    val initialRate by remember { mutableStateOf(rate ?: "") }
    val initialAbout by remember { mutableStateOf(about ?: "") }

    // Check if any field has changed
    val hasChanges by remember {
        derivedStateOf {
            selectedLocation != initialLocation ||
                    phoneNumber != initialPhoneNumber ||
                    estimatedRate != initialRate ||
                    aboutMe != initialAbout
        }
    }

    // Update isPhoneValid whenever phoneNumber changes
    LaunchedEffect(phoneNumber) {
        isPhoneValid = phoneNumber.isEmpty() || phoneRegex.matches(phoneNumber)
    }

    LaunchedEffect(updateDetailState) {
        when (val updateDetails = updateDetailState) {
            is UpdateTradesmanDetailViewModel.UpdateTradesmanDetailState.Loading -> {
                // Loading
            }
            is UpdateTradesmanDetailViewModel.UpdateTradesmanDetailState.Success -> {
                SnackbarController.show("Updated Successfully")
                updateTradesmanDetailViewModel.resetState()
                navController.navigate("main_screen?selectedItem=4&selectedTab=0") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
            is UpdateTradesmanDetailViewModel.UpdateTradesmanDetailState.Error -> {
                SnackbarController.show(updateDetails.message)
                updateTradesmanDetailViewModel.resetState()
            }
            else -> Unit
        }
    }

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(WindowInsets.systemBars.asPaddingValues())
        ) {
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
                        .clickable { navController.navigate("main_screen?selectedItem=4&selectedTab=0") },
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
                    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                        Text(
                            text = "Preferred location:",
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        ) {
                            CustomDropdown(
                                selectedOption = selectedLocation,
                                onOptionSelected = { selectedLocation = it }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                        Text(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = "Phone Number:",
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

                                BasicTextField(
                                    value = phoneNumber,
                                    onValueChange = {  newValue ->
                                        // Filter to digits only and limit to 10 characters
                                        val filteredValue = newValue.filter { it.isDigit() }.take(11)
                                        phoneNumber = when {
                                            filteredValue.isEmpty() -> ""
                                            else -> filteredValue
                                        }

                                        // Validate using regex
                                        isPhoneValid = phoneNumber.isEmpty() || phoneRegex.matches(phoneNumber)
                                    },
                                    textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier
                                        .padding(start = 4.dp)
                                        .fillMaxWidth(),
                                    decorationBox = { innerTextField ->
                                        Box {
                                            if (phoneNumber.isEmpty()) {
                                                Text(
                                                    text = "eg. 09876543211",
                                                    color = Color.Gray,
                                                    style = TextStyle(fontSize = 14.sp)
                                                )
                                            }
                                            innerTextField()
                                        }
                                    }
                                )
                            }
                        }
                        if (!isPhoneValid && phoneNumber.isNotEmpty()) {
                            Text(
                                text = "Phone number must be 11 digits starting with 09 (e.g., 09876543211)",
                                color = Color.Red,
                                style = TextStyle(fontSize = 12.sp),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                        Text(
                            text = "Estimated rate:",
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            BasicTextField(
                                value = estimatedRate,
                                onValueChange = { newText ->
                                    val filteredText = newText.filter { it.isDigit() }
                                    val trimmedText = if (filteredText.startsWith("0") && filteredText.length > 1) {
                                        filteredText.dropWhile { it == '0' }
                                    } else {
                                        filteredText
                                    }
                                    estimatedRate = trimmedText
                                },
                                textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                decorationBox = { innerTextField ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("₱ ", fontSize = 16.sp, color = Color.Black)
                                        Box {
                                            if (estimatedRate.isEmpty()) {
                                                Text(
                                                    text = "Enter amount",
                                                    color = Color.Gray,
                                                    fontSize = 14.sp
                                                )
                                            }
                                            innerTextField()
                                        }
                                    }
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                        Text(
                            text = "About Me:",
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                        BasicTextField(
                            value = aboutMe,
                            onValueChange = { newText ->
                                if (newText.length <= 500) {
                                    aboutMe = newText
                                } else {
                                    aboutMe = newText.substring(0, 500)
                                    SnackbarController.show("Character count exceeds")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, color = Color.Black),
                            decorationBox = { innerTextField ->
                                Box {
                                    if (aboutMe.isEmpty()) {
                                        Text(
                                            text = "Example: I'm a licensed plumber with over 7 years of experience...",
                                            fontSize = 16.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Button(
                        onClick = {
                            updateTradesmanDetailViewModel.updateTradesmanDetails(
                                aboutMe,
                                selectedLocation,
                                estimatedRate.toIntOrNull() ?: 0,
                                phoneNumber
                            )
                        },
                        enabled = hasChanges && isPhoneValid,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (hasChanges && isPhoneValid) Color(0xFF42C2AE) else Color.Gray,
                            disabledContainerColor = Color.Gray
                        ),
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .background(
                                color = if (hasChanges && isPhoneValid) Color(0xFF42C2AE) else Color.Gray,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Text(text = "Save Changes")
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 26.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            SnackbarController.ObserveSnackbar()
        }
    }
}

@Composable
fun CustomDropdown(
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf(
        "Agno", "Aguilar", "Alcala", "Anda", "Asingan", "Balungao", "Bani", "Basista", "Bautista",
        "Bayambang", "Binalonan", "Binmaley", "Bolinao", "Bugallon", "Burgos", "Calasiao",
        "Dagupan City", "Dasol", "Infanta", "Labrador", "Laoac", "Lingayen", "Mabini", "Malasiqui",
        "Manaoag", "Mangaldan", "Mangatarem", "Mapandan", "Natividad", "Pozorrubio", "Rosales",
        "San Fabian", "San Jacinto", "San Manuel", "San Nicolas", "San Quintin", "Santa Barbara",
        "Santa Maria", "Santo Tomas", "Sison", "Sual", "Tayug", "Umingan", "Urbiztondo",
        "Urdaneta City", "Villasis"
    )

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
            Text(
                text = selectedOption,
                fontSize = 16.sp,
                color = if (selectedOption == "Select location") Color.Gray else Color.Black
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Dropdown",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier
            .width(330.dp)
            .background(Color.White)
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