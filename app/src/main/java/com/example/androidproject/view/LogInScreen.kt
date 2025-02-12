package com.example.androidproject.view

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.androidproject.R
import com.example.androidproject.view.theme.myGradient
import com.example.androidproject.viewmodel.LoginViewModel

@Preview(showBackground = true)
@Composable
fun LogInScreenPreview() {
    //LogInScreen(navController = rememberNavController())
}

@Composable
fun LogInScreen(navController: NavController, viewModel: LoginViewModel){
    val windowSize = rememberWindowSizeClass()
    val loginState by viewModel.loginState.collectAsState()
    val context = LocalContext.current

    // Observe login state changes
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginViewModel.LoginState.Success -> {
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                navController.navigate("main_screen") {
                    popUpTo("login") { inclusive = true }
                }
                viewModel.resetState()
            }
            is LoginViewModel.LoginState.Error -> {
                Toast.makeText(context, (loginState as LoginViewModel.LoginState.Error).message, Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }


    var email by remember {
        mutableStateOf("") }

    var password by remember {
        mutableStateOf("") }

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
            .background(myGradient),
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
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Welcome Back!",
                    fontSize = when (windowSize.width) {
                        WindowType.SMALL -> 24.sp
                        WindowType.MEDIUM -> 28.sp
                        else -> 32.sp
                    },
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.offset(x = (-110).dp)
                )

                InputFieldForLogin(email = email,
                    onEmailChange = { email = it },
                    password = password,
                    onPasswordChange = { password = it },
                    windowSize,
                    )

                ForgotPassword(windowSize)

                Spacer(modifier = Modifier.height(10.dp))
                ButtonLogin(navController, viewModel, email, password,  windowSize)

                Spacer(modifier = Modifier.height(10.dp))
                SignUpButton(navController, windowSize)
            }


        }

    }
}

@Composable
fun InputFieldForLogin(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    windowSize: WindowSize
    ){


    Spacer(modifier = Modifier.height(10.dp))

    EmailField(email = email, onEmailChange = onEmailChange, windowSize)

    Spacer(modifier = Modifier.height(10.dp))

    PasswordField(password = password, onPasswordChange = onPasswordChange, windowSize)

}
@Composable
fun EmailField(email: String, onEmailChange: (String) -> Unit, windowSize: WindowSize){
    var emailError by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = email,
        onValueChange = {
            onEmailChange(it)
            emailError =  it.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
        },
        isError = emailError,

        label = { Text("Email") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Email Icon"
            )
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth(
                when (windowSize.width) {
                    WindowType.SMALL -> 0.9f
                    else -> 0.8f
                }
            )
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
            modifier = Modifier.offset(x = 70.dp)
                .padding(top = 10.dp)
        )
    }
}

@Composable
fun PasswordField(password: String, onPasswordChange: (String) -> Unit, windowSize: WindowSize){
    var passwordError by remember { mutableStateOf(false) }

    var passwordVisible by remember { mutableStateOf(false) }
    val icon = if (passwordVisible)
        painterResource(id = R.drawable.visibility_on)
    else
        painterResource(id = R.drawable.visibility_off)
    val iconSize = when (windowSize.width) {
        WindowType.SMALL -> 20.dp // Smaller screen
        WindowType.MEDIUM -> 24.dp // Medium screen
        WindowType.LARGE -> 30.dp // Larger screen
    }
    OutlinedTextField(
        value = password,
        onValueChange = {
            onPasswordChange(it)
            passwordError = it.isNotEmpty() && it.length < 8

        },
        label = { Text("Password") },
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon") },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(painter = icon, contentDescription = "Toggle Password Visibility", modifier = Modifier.size(iconSize))
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        isError = passwordError,
        modifier = Modifier
            .fillMaxWidth(
                when (windowSize.width) {
                    WindowType.SMALL -> 0.9f
                    else -> 0.8f
                }
            )
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
    if (passwordError) {
        Text(
            text = "● At least 8 characters required.",
            color = Color.Red,
            fontSize = 14.sp,
            modifier = Modifier.offset(x = 70.dp)
                .padding(top = 10.dp)
        )
    }
}

@Composable
fun ForgotPassword(windowSize: WindowSize) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)

            ,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = "Forgot Password?",
            color = Color.Gray,
            fontSize = when (windowSize.width) {
                WindowType.SMALL -> 12.sp
                else -> 14.sp
            }
        )
    }
}

@Composable
fun ButtonLogin(navController: NavController, viewModel: LoginViewModel, email: String, password: String, windowSize: WindowSize){
    val context = LocalContext.current

    Button(
        onClick = {
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
            } else if (password.length < 8) {
                Toast.makeText(context, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(email, password)
            }
        },
        modifier = Modifier
            .fillMaxWidth(
                when (windowSize.width) {
                    WindowType.SMALL -> 0.9f
                    else -> 0.8f
                }
            )
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF122826))
    ) {
        Text(text = "Log In", color = Color.White, fontSize = 16.sp)
    }
}

@Composable
fun SignUpButton(navController: NavController, windowSize: WindowSize) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
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