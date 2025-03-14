package com.example.androidproject.view

import LogoutViewModel
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.androidproject.R
import com.example.androidproject.data.preferences.AccountManager
import com.example.androidproject.data.preferences.TokenManager
import com.example.androidproject.view.theme.myGradient
import com.example.androidproject.viewmodel.LoginViewModel
import kotlinx.coroutines.delay

@Preview(showBackground = true)
@Composable
fun LogInScreenPreview() {
    //LogInScreen(navController = rememberNavController())
}
@Composable
fun LogInScreen(navController: NavController, viewModel: LoginViewModel,logoutViewModel:LogoutViewModel) {

    val logoutResult by logoutViewModel.logoutResult.collectAsState()
    val windowSize = rememberWindowSizeClass()
    val loginState by viewModel.loginState.collectAsState()
    val context = LocalContext.current
    var showSuspendedDialog by remember { mutableStateOf(false) }

    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    LaunchedEffect(logoutResult) {
        logoutResult?.let {
            // Clear tokens and navigate regardless of result
            TokenManager.clearToken()
            AccountManager.clearAccountData()
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
            logoutViewModel.resetLogoutResult()
        }
    }

    // Observe login state changes
    LaunchedEffect(loginState) {
        when (val loginStatus =loginState) {
            is LoginViewModel.LoginState.Success -> {
                val response = loginStatus.data
                val account = response?.user
                val isSuspended = account?.suspend == true

                if (isSuspended) {
                    showSuspendedDialog = true
                    return@LaunchedEffect
                }else{
                    snackbarMessage = "Login successful"
                    showSnackbar = true
                    navController.navigate("main_screen") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                viewModel.resetState()
            }
            is LoginViewModel.LoginState.Error -> {
                snackbarMessage = loginStatus.message
                showSnackbar = true
                viewModel.resetState()
            }
            else -> {}
        }
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val cardOffsetY = remember { Animatable(500f) } // Start off-screen to the bottom
    val currentBackStackEntry = navController.currentBackStackEntryAsState()

    // Launch animation when composable is composed
    LaunchedEffect(currentBackStackEntry.value) {
        cardOffsetY.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(myGradient)
            .padding(WindowInsets.systemBars.asPaddingValues()),
        contentAlignment = Alignment.Center
    ) {
        // Set an image as the background
        Image(
            painter = painterResource(id = R.drawable.authbg),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize()
                .offset(y = (-150).dp)
        )

        // Card with adaptive size and padding
        Card(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .offset(y = cardOffsetY.value.dp)
                .align(Alignment.BottomCenter)
                .size(
                    width = when (windowSize.width) {
                        WindowType.SMALL -> 500.dp
                        WindowType.MEDIUM -> 350.dp
                        else -> 400.dp
                    },
                    height = when (windowSize.height) {
                        WindowType.SMALL -> 400.dp
                        WindowType.MEDIUM -> 450.dp
                        else -> 450.dp
                    }
                ),
            shape = RoundedCornerShape(
                topEnd = 20.dp,
                topStart = 20.dp
            ),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxSize()
            ) {
                // Create references for the components
                val (welcomeText, emailField, passwordField, forgotPassword, loginButton, signUpText) = createRefs()

                // Create guidelines
                val verticalGuideline1= createGuidelineFromStart(0.05f) // 10% from the start
                val verticalGuideline2 = createGuidelineFromStart(0.95f) // 10% from the start

                val horizontalGuideline = createGuidelineFromTop(0.1f) // 20% from the top

                // Welcome Text
                WelcomeText(
                    windowSize = windowSize,
                    modifier = Modifier.constrainAs(welcomeText) {
                        top.linkTo(horizontalGuideline)
                        start.linkTo(verticalGuideline1)
                    }
                )

                // Email Field
                EmailField(
                    email = email,
                    onEmailChange = { email = it },
                    windowSize = windowSize,
                    modifier = Modifier.constrainAs(emailField) {
                        top.linkTo(welcomeText.bottom, margin = 24.dp)
                        start.linkTo(verticalGuideline1)
                        end.linkTo(verticalGuideline2)
                        width = Dimension.fillToConstraints
                    }
                )

                // Password Field
                PasswordField(
                    password = password,
                    onPasswordChange = { password = it },
                    windowSize = windowSize,
                    modifier = Modifier.constrainAs(passwordField) {
                        top.linkTo(emailField.bottom, margin = 16.dp)
                        start.linkTo(verticalGuideline1)
                        end.linkTo(verticalGuideline2)
                        width = Dimension.fillToConstraints
                    }
                )

                // Forgot Password
                ForgotPassword(
                    windowSize = windowSize,
                    modifier = Modifier.constrainAs(forgotPassword) {
                        top.linkTo(passwordField.bottom, margin = 16.dp)
                        end.linkTo(verticalGuideline2)
                    },
                    navController = navController
                    )

                // Login Button
                LoginButton(
                    navController = navController,
                    viewModel = viewModel,
                    email = email,
                    password = password,
                    windowSize = windowSize,
                    onShowSnackbar = { message ->
                        snackbarMessage = message
                        showSnackbar = true
                    },
                    modifier = Modifier.constrainAs(loginButton) {
                        top.linkTo(forgotPassword.bottom, margin = 24.dp)
                        start.linkTo(verticalGuideline1)
                        end.linkTo(verticalGuideline2)
                        width = Dimension.fillToConstraints
                    }
                )

                // Sign Up Button
                SignUpButton(
                    navController = navController,
                    windowSize = windowSize,
                    modifier = Modifier.constrainAs(signUpText) {
                        top.linkTo(loginButton.bottom, margin = 24.dp)
                        start.linkTo(verticalGuideline1)
                        end.linkTo(verticalGuideline2)
                    }
                )
            }
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
                duration = 3000L,
                onDismiss = { showSnackbar = false }
            )
        }
    }
    // Suspension Dialog
    if (showSuspendedDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuspendedDialog = false
                viewModel.resetState()
            },
            title = {
                Text(
                    text = "Account Suspended",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Text(
                    text = "Your account has been suspended. Please contact support for more information.",
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuspendedDialog = false
                        viewModel.resetState()
                        val token = TokenManager.getToken()
                        if (token != null) {
                            logoutViewModel.logout()
                        } else {
                            // Handle case where token is null
                            TokenManager.clearToken()
                            AccountManager.clearAccountData()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF122826))
                ) {
                    Text("OK", color = Color.White)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun WelcomeText(windowSize: WindowSize, modifier: Modifier = Modifier) {
    Text(
        text = "Welcome Back!",
        fontSize = when (windowSize.width) {
            WindowType.SMALL -> 24.sp
            WindowType.MEDIUM -> 28.sp
            else -> 32.sp
        },
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = modifier
    )
}

@Composable
fun EmailField(
    email: String,
    onEmailChange: (String) -> Unit,
    windowSize: WindowSize,
    modifier: Modifier = Modifier
) {
    var emailError by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = email.trim(),
            onValueChange = {
                onEmailChange(it)
                emailError = it.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
            },
            isError = emailError,
            label = { Text("Email") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon", tint = Color.Black
                )
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Blue,
                unfocusedIndicatorColor = Color.Gray,
                focusedLabelColor = Color.Blue,
                unfocusedLabelColor = Color.Gray,
                cursorColor = Color.Black
            ),
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black)
        )

        if (emailError) {
            Text(
                text = "● Invalid email format.",
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth()
            )
        }
    }
}
@Composable
fun PasswordField(password: String,
                  onPasswordChange: (String) -> Unit,

                  windowSize: WindowSize, modifier: Modifier = Modifier) {
    var passwordVisible by remember { mutableStateOf(false) }

    val icon = if (passwordVisible)
        painterResource(id = R.drawable.visibility_on)
    else
        painterResource(id = R.drawable.visibility_off)
    Column(modifier = modifier) {
    OutlinedTextField(
        value = password.trim(),
        onValueChange = {
            onPasswordChange(it)
        },
        label = { Text("Password") },
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon", tint = Color.Black) },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(painter = icon, contentDescription = "Toggle Password Visibility", modifier = Modifier.size(24.dp), tint = Color.Black)
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            ,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Blue,
            unfocusedIndicatorColor = Color.Gray,
            focusedLabelColor = Color.Blue,
            unfocusedLabelColor = Color.Gray,
            cursorColor = Color.Black
        ),
        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black)
    )

    }
}
@Composable
fun ForgotPassword(windowSize: WindowSize, modifier: Modifier = Modifier,navController: NavController) {
    Text(
        text = "Forgot Password?",
        color = Color.Gray,
        fontSize = when (windowSize.width) {
            WindowType.SMALL -> 12.sp
            else -> 14.sp
        },
        modifier = modifier.clickable { navController.navigate("resetpassword")}
    )
}
@Composable
fun LoginButton(navController: NavController, viewModel: LoginViewModel, email: String, password: String, windowSize: WindowSize,onShowSnackbar: (String) -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = {
            if (email.isEmpty() || password.isEmpty()) {
                onShowSnackbar("Fields cannot be empty")
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                onShowSnackbar("Invalid email format")
            } else if (password.length < 8) {
                onShowSnackbar("Password must be at least 8 characters")

            }
            else {
                viewModel.login(email, password)
            }
        },
        modifier = modifier
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF122826))
    ) {
        Text(text = "Log In", color = Color.White, fontSize = 16.sp)
    }


}

@Composable
fun SignUpButton(navController: NavController, windowSize: WindowSize, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(
            text = "Don't have an account? ",
            color = Color.Gray,
            fontSize = when (windowSize.width) {
                WindowType.SMALL -> 12.sp
                else -> 14.sp
            }
        )
        Text(
            text = "Sign Up",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = when (windowSize.width) {
                WindowType.SMALL -> 12.sp
                else -> 14.sp
            },
            modifier = Modifier.clickable { navController.navigate("signup") }
        )
    }
}

@Composable
fun CustomDurationSnackbar(
    message: String,
    show: Boolean,
    duration: Long = 3000L,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = show,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Snackbar(
            modifier = Modifier
                .wrapContentWidth()
                .padding(bottom = 8.dp)
                .padding(horizontal = 16.dp)
                .shadow(5.dp)
                ,
            containerColor = Color.Gray,
            contentColor = Color.White,

            content = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = message, fontSize = 16.sp, color = Color.White)
                }
            }
        )
    }

    // Handle duration
    LaunchedEffect(show) {
        if (show) {
            delay(duration)
            onDismiss()
        }
    }
}