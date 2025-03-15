package com.example.androidproject.view

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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.androidproject.viewmodel.ForgotPassViewModel
import com.example.androidproject.viewmodel.ResetPassViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ResetPassword(
    navController: NavController,
    forgotPass: ForgotPassViewModel,
    resetPass: ResetPassViewModel,
    LoadingUI : @Composable () -> Unit
) {
    val forgotPassState by forgotPass.forgotPasswordState.collectAsState()
    val resetPassState by resetPass.resetPassState.collectAsState()

    var email by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showOtpDialog by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf(60) }
    var isOtpVerified by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var lengthError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) } // Added loading state
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    // Countdown logic
    LaunchedEffect(showOtpDialog) {
        if (showOtpDialog) {
            countdown = 60
            coroutineScope.launch {
                while (countdown > 0) {
                    delay(1000L)
                    countdown--
                }
                showOtpDialog = false
            }
        }
    }


    // Reset all state when navigating back to login
    DisposableEffect(navController) {
        onDispose {
            // Reset all state variables
            email = ""
            otp = ""
            newPassword = ""
            confirmPassword = ""
            showOtpDialog = false
            countdown = 60
            isOtpVerified = false
            newPasswordVisible = false
            passwordError = null
            lengthError = null
            confirmPasswordVisible = false
            isLoading = false

            // Reset ViewModel states if needed
            forgotPass.resetState()
            resetPass.resetState()
        }
    }

    LaunchedEffect(resetPassState) {
        when (val resetPassword = resetPassState) {
            is ResetPassViewModel.ResetPassState.Loading -> {
                isLoading = true // Show loading UI
            }
            is ResetPassViewModel.ResetPassState.Success -> {
                resetPass.resetState()
                // Navigate to the "login" screen and clear the back stack
                snackbarMessage = "Password Reset Successfully"
                showSnackbar = true
                navController.navigate("login") {
                    navController.popBackStack()
                    isLoading = false // Hide loading UI
                }
            }
            is ResetPassViewModel.ResetPassState.Error -> {
                isLoading = false // Hide loading UI
                val error = resetPassword.message
                snackbarMessage = error
                showSnackbar = true
            }
            else -> Unit
        }
    }

    // Handle ForgotPassState changes
    LaunchedEffect(forgotPassState) {
        when (val state = forgotPassState) {
            is ForgotPassViewModel.ForgotPasswordState.Loading -> {
                isLoading = true // Show loading UI
            }
            is ForgotPassViewModel.ForgotPasswordState.Success -> {
                isLoading = false // Hide loading UI
                snackbarMessage = "OTP Sent"
                showSnackbar = true
                showOtpDialog = true // Show OTP dialog
            }
            is ForgotPassViewModel.ForgotPasswordState.Error -> {
                isLoading = false // Hide loading UI
                val error = state.message
                snackbarMessage = error
                showSnackbar = true
            }
            else -> Unit
        }
    }

    Box(Modifier.fillMaxSize()
        .padding(paddingValues = WindowInsets.statusBars.asPaddingValues())) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Header Section
            Card(
                modifier = Modifier.fillMaxWidth()
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
                                .clickable(indication = null,
                                    interactionSource = remember { MutableInteractionSource() })
                                { navController.popBackStack() }
                                .padding(16.dp),
                            tint = Color.Black
                        )

                        Text(
                            text = "Forgot Password",
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
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    colors = CardDefaults.cardColors(Color.White),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                    ) {
                        if (!isOtpVerified) {
                            // Email input section
                            Text(
                                text = "Email",
                                fontSize = 18.sp,
                                fontWeight = FontWeight(500),
                                color = Color.Black
                            )
                            OutlinedTextField(
                                value = email.trim(),
                                onValueChange = { email = it },
                                label = { Text("eg. sample@gmail.com") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = Color(0xFF122826),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable {
                                            isLoading = true // Set loading state to true
                                            forgotPass.forgotPassword(email)
                                        }
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                ) {
                                    Text(
                                        text = "Send OTP",
                                        color = Color.White,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        } else {
                            // Password reset section
                            Text(
                                text = "Reset Password",
                                fontSize = 20.sp,
                                fontWeight = FontWeight(500),
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = newPassword.trim(),
                                onValueChange = { newPassword = it },
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
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = confirmPassword.trim(),
                                onValueChange = { confirmPassword = it },
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
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = {
                                    resetPass.resetPassword(otp.toInt(), newPassword)
                                    isLoading =true
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF122826))
                            ) {
                                Text("Confirm")
                            }
                        }
                    }
                }

            }

            when (val forgotPassVerification = forgotPassState) {
                is ForgotPassViewModel.ForgotPasswordState.Success -> {
                    val verification = forgotPassVerification.data
                    // OTP Dialog
                    if (showOtpDialog) {
                        Dialog(onDismissRequest = { /* Prevent dismissal by clicking outside */ }) {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.padding(16.dp),
                                colors = CardDefaults.cardColors(Color.White)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .width(280.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Enter OTP",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    OutlinedTextField(
                                        value = otp.replace(" ", ""),
                                        onValueChange = { otp = it },
                                        label = { Text("OTP") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Time remaining: $countdown seconds",
                                        fontSize = 14.sp,
                                        color = if (countdown <= 10) Color.Red else Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = {
                                            //IF OTP IS CORRECT
                                            if(otp.isEmpty()){
                                                snackbarMessage = "Please enter OTP"
                                                showSnackbar = true
                                                return@Button
                                            }
                                            if (verification != null) {
                                                if (otp.toInt() == verification.token) {
                                                    isOtpVerified = true
                                                    showOtpDialog = false
                                                    snackbarMessage = "OTP Verified"
                                                    showSnackbar = true
                                                } else {
                                                    snackbarMessage = "Invalid OTP"
                                                    showSnackbar = true
                                                }
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF122826))
                                    ) {
                                        Text("Verify")
                                    }
                                }
                            }
                        }
                    }
                }
                else -> Unit
            }
        }

        // Place LoadingUI here, outside the Column but inside the Box
        if (isLoading) {
            LoadingUI()
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            CustomDurationSnackbar(
                message = snackbarMessage,
                show = showSnackbar,
                duration = 5000L,
                onDismiss = { showSnackbar = false }
            )
        }
    }
}


