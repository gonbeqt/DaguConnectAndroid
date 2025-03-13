package com.example.androidproject.view.client

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.androidproject.viewmodel.Tradesman_Profile.UpdateTradesmanProfileViewModel
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
                Toast.makeText(context, responseMessage, Toast.LENGTH_SHORT).show()
                navController.navigate("main_screen?selectedItem=4") {
                    navController.popBackStack()
                }
                updateClientProfileAddressViewModel.resetState()
            }
            is UpdateClientProfileAddressViewModel.UpdateClientProfileAddressState.Error -> {
                val error = updateClientDetails.message
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
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
                        listOf(
                            "First Name: ${profile.fullname.split(" ").first()}",
                            "Last Name: ${profile.fullname.split(" ").last()}",
                            "Email: ${profile.email}"
                        ).forEach { text ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                                    .background(Color.White)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = text,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Editable Contact Number Field
                        OutlinedTextField(
                            value = contactNumber,
                            onValueChange = { contactNumber = it },
                            label = { Text("Contact Number") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color.Blue,
                                unfocusedBorderColor = Color.Gray,
                                containerColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Editable Location Field
                        OutlinedTextField(
                            value = location,
                            onValueChange = { location = it },
                            label = { Text("Location") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            maxLines = 3,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color.Blue,
                                unfocusedBorderColor = Color.Gray,
                                containerColor = Color.White
                            )
                        )

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
    }
}
