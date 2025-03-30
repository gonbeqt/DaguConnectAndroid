package com.example.androidproject.view.client

import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.androidproject.view.extras.SnackbarController
import com.example.androidproject.view.tradesman.JobSelectionDropdown
import com.example.androidproject.viewmodel.client_profile.GetClientProfileViewModel
import com.example.androidproject.viewmodel.client_profile.UpdateClientProfileAddressViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettings(navController: NavController, getClientProfileViewModel: GetClientProfileViewModel, updateClientProfileAddressViewModel: UpdateClientProfileAddressViewModel) {
    val profileState by getClientProfileViewModel.getProfileState.collectAsState()
    var contactNumber by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val updateClientDetailsState by updateClientProfileAddressViewModel.updateClientProfileState.collectAsState()
    val context = LocalContext.current

    val phoneRegex = "^09[0-9]{9}$".toRegex()
    var isPhoneValid by remember { mutableStateOf(contactNumber.isEmpty() || "^09[0-9]{9}$".toRegex().matches(contactNumber)) }

    // Update contactNumber and location when profile data is available
    LaunchedEffect(profileState) {
        if (profileState is GetClientProfileViewModel.ClientProfileState.Success) {
            val profile = (profileState as GetClientProfileViewModel.ClientProfileState.Success).data
            contactNumber = profile.phoneNumber ?: "" // Use empty string if phoneNumber is null
            location = profile.address ?: "" // Use empty string if location is null
        }
    }

    LaunchedEffect(updateClientDetailsState) {
        when (val updateClientDetails = updateClientDetailsState) {
            is UpdateClientProfileAddressViewModel.UpdateClientProfileAddressState.Loading -> {
                // Show loading
            }
            is UpdateClientProfileAddressViewModel.UpdateClientProfileAddressState.Success -> {
                val responseMessage = updateClientDetails.data?.message
                if (responseMessage != null) {
                    SnackbarController.show(responseMessage)
                }

                navController.navigate("main_screen?selectedItem=4&selectedTab=1") {
                    navController.popBackStack()
                }
                updateClientProfileAddressViewModel.resetState()
            }
            is UpdateClientProfileAddressViewModel.UpdateClientProfileAddressState.Error -> {
                val error = updateClientDetails.message
                SnackbarController.show(error)            }
            else -> Unit // Handles Idle state or any other unexpected state
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC))
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.statusBars.asPaddingValues()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Arrow Back",
                        modifier = Modifier
                            .clickable {
                                navController.navigate("main_screen?selectedItem=4") {
                                    navController.popBackStack()
                                }
                            }
                            .size(24.dp),
                        tint = Color.Black
                    )
                    Text(
                        text = "Account Settings",
                        fontSize = 24.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (val state = profileState) {
                is GetClientProfileViewModel.ClientProfileState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is GetClientProfileViewModel.ClientProfileState.Success -> {
                    val profile = state.data

                    // Profile Image with Edit Icon
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = selectedImageUri ?: profile.profilePicture,
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // User Details Column
                    Column(
                        modifier = Modifier
                            .widthIn(max = 400.dp)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                      Text(
                          text = "First Name",
                          fontSize = 16.sp,
                          color = Color.Black,
                          modifier = Modifier.padding(horizontal = 4.dp)
                      )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                                    .background(Color.White)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "${profile.fullname.split(" ").first()}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        Text(
                            text = "Last Name",
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 4.dp)

                        )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                                    .background(Color.White)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "${profile.fullname.split(" ").last()}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Email",
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 4.dp)

                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                                    .background(Color.White)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "${profile.email}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            modifier = Modifier.padding(bottom = 8.dp).padding(horizontal = 4.dp)
                            ,
                            text = "Phone Number:",
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                                .padding(horizontal = 14.dp, vertical = 20.dp)

                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {

                                BasicTextField(
                                    value = contactNumber,
                                    onValueChange = {  newValue ->
                                        // Filter to digits only and limit to 11 characters
                                        val filteredValue = newValue.filter { it.isDigit() }.take(11)
                                        contactNumber = when {
                                            filteredValue.isEmpty() -> ""
                                            else -> filteredValue
                                        }

                                        // Validate using regex
                                        isPhoneValid = contactNumber.isEmpty() || phoneRegex.matches(contactNumber)
                                    },
                                    textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier
                                        .padding(start = 4.dp)
                                        .fillMaxWidth(),
                                    decorationBox = { innerTextField ->
                                        Box {
                                            if (contactNumber.isEmpty()) {
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
                        if (!isPhoneValid && contactNumber.isNotEmpty()) {
                            Text(
                                text = "Phone number must be 11 digits starting with 09 (e.g., 09876543211)",
                                color = Color.Red,
                                style = TextStyle(fontSize = 12.sp),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            modifier = Modifier.padding(horizontal = 4.dp)
                            ,
                            text = "Address",
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        // Editable Location Field
                        Box(Modifier.fillMaxWidth()) {
                            JobSelectionDropdown1(
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
                                selectedOption = location,
                                onOptionSelected = { location = "$it, Pangasinan" }
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Buttons
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = { navController.popBackStack() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "Cancel", fontSize = 16.sp)
                            }

                            Button(
                                onClick = {
                                    updateClientProfileAddressViewModel.updateClientProfile(location, contactNumber)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3CC0B0)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "Save", fontSize = 16.sp, color = Color.White)
                            }
                        }
                    }
                }

                is GetClientProfileViewModel.ClientProfileState.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                else -> {
                    Text(
                        text = "No profile data available",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            SnackbarController.ObserveSnackbar()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobSelectionDropdown1(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors()

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
            textStyle = TextStyle(
                color = textColor,
                fontSize = 16.sp // Smaller font size for selected option
            ),  // apply correct text color
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
                    text = { Text(option,fontSize = 16.sp) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
