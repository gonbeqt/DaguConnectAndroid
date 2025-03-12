package com.example.androidproject.view.client

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidproject.viewmodel.ChangePasswordViewModel

@Composable
fun ChangePassword(navController: NavController,changePassword: ChangePasswordViewModel, LoadingUI :@Composable ()-> Unit) {
    val changePasswordState by changePassword.changePassState.collectAsState()
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var oldPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val isLoading by remember { mutableStateOf(false) } // Added loading state
    var passwordError by remember { mutableStateOf<String?>(null) }
    var lengthError by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    LaunchedEffect(changePasswordState) {
        when (val changePass =changePasswordState){

            is ChangePasswordViewModel.ChangePassState.Success->{
                changePassword.resetState()
                Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show()
                // Navigate to the "profile" screen and clear the back stack
                navController.navigate("main_screen?selectedItem=4&selectedTab=1") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
            is ChangePasswordViewModel.ChangePassState.Error -> {
                Toast.makeText(context, changePass.message, Toast.LENGTH_SHORT).show()
            }
          else -> Unit
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
            .background(Color(0xFFECECEC))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Header Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .size(70.dp)
                        .padding(top = 5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Arrow Back",
                            modifier = Modifier
                                .clickable {
                                    navController.navigate("main_screen?selectedItem=4&selectedTab=1") // or whatever your default route is
                                   }
                                .padding(16.dp),
                            tint = Color.Black
                        )

                        Text(
                            text = "Change Password",
                            fontSize = 24.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Left,
                            modifier = Modifier
                                .padding(top = 15.dp)
                                .weight(1f)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFECECEC)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Old Password Input
                        OutlinedTextField(
                            value = oldPassword.trim(),
                            onValueChange = { oldPassword= it },
                            label = { Text("Old Password") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            visualTransformation = if (oldPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { oldPasswordVisible = !oldPasswordVisible }) {
                                    Icon(
                                        imageVector = if (oldPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = if (oldPasswordVisible) "Hide Password" else "Show Password"
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // New Password Input
                        OutlinedTextField(
                            value = newPassword.trim(),
                            onValueChange = {
                                newPassword = it
                                lengthError = if (it.length < 8) "Password must be at least 8 characters" else null
                            },
                            label = { Text("New Password") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                                    Icon(
                                        imageVector = if (newPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = if (newPasswordVisible) "Hide Password" else "Show Password"
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            isError = lengthError != null
                        )

                        if (lengthError != null) {
                            Text(
                                text = lengthError!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Confirm Password Input
                        OutlinedTextField(
                            value = confirmPassword.trim(),
                            onValueChange = {
                                confirmPassword = it
                                passwordError = if (newPassword != it && it.isNotEmpty()) "Passwords do not match" else null
                            },
                            label = { Text("Confirm Password") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                    Icon(
                                        imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = if (confirmPasswordVisible) "Hide Password" else "Show Password"
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            isError = passwordError != null
                        )

                        if (passwordError != null) {
                            Text(
                                text = passwordError!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Confirm Button
                        Button(
                            onClick = {
                                passwordError = if (newPassword != confirmPassword) "Passwords do not match" else null
                                lengthError = if (newPassword.length < 8) "Password must be at least 8 characters" else null
                                if (passwordError == null && lengthError == null) {
                                    changePassword.changePassword(oldPassword, newPassword)

                                }
                            },
                            modifier = Modifier.fillMaxWidth(),

                            enabled = oldPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF122826))

                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
        if (isLoading) {
            LoadingUI()
        }
    }
}

