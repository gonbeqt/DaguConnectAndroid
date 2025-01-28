package com.example.androidproject.ui.theme.views

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
import androidx.compose.material3.TextButton
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.androidproject.R
import com.example.androidproject.api.ApiService
import com.example.androidproject.api.RetrofitInstance
import com.example.androidproject.viewmodels.LoginViewModel
import com.example.androidproject.viewmodels.factories.LoginViewModelFactory

@Preview(showBackground = true)
@Composable
fun LogInScreenPreview() {
    //LogInScreen(navController = rememberNavController())
}

@Composable
fun LogInScreen(navController: NavController, viewModel: LoginViewModel){
    val windowSize = rememberWindowSizeClass()
    val loginState by viewModel.loginState.collectAsState()

    var email by remember {
        mutableStateOf("") }

    var password by remember {
        mutableStateOf("") }

    val cardOffsetX = remember { Animatable(500f) } // Start off-screen to the right

    val currentBackStackEntry = navController.currentBackStackEntryAsState()

    // Launch animation when composable is composed
    LaunchedEffect(currentBackStackEntry.value) {
        cardOffsetX.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.CenterEnd
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background_image_auth),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Card with adaptive size and padding
        Card(
            modifier = Modifier
                .offset(x = cardOffsetX.value.dp)
                .size(
                    width = when (windowSize.width) {
                        WindowType.SMALL -> 300.dp
                        WindowType.MEDIUM -> 350.dp
                        else -> 400.dp
                    },
                    height = when (windowSize.height) {
                        WindowType.SMALL -> 360.dp
                        WindowType.MEDIUM -> 400.dp
                        else -> 450.dp
                    }
                ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Welcome Back",
                    fontSize = when (windowSize.width) {
                        WindowType.SMALL -> 20.sp
                        WindowType.MEDIUM -> 24.sp
                        else -> 28.sp
                    },
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue,
                    modifier = Modifier.padding(bottom = 16.dp, top = 16.dp)
                )

                InputFieldForLogin(email = email,
                    onEmailChange = { email = it },
                    password = password,
                    onPasswordChange = { password = it },
                    windowSize,
                    )

                ForgotPassword(windowSize)

                Spacer(modifier = Modifier.height(10.dp))
                ButtonLogin(navController, windowSize)

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
fun EmailField(email: String, onEmailChange: (String) -> Unit, windowSize:WindowSize){

    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
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
}

@Composable
fun PasswordField(password: String, onPasswordChange: (String) -> Unit, windowSize: WindowSize){
    var passwordVisible by remember { mutableStateOf(false) }
    val icon = if (passwordVisible)
        painterResource(id = R.drawable.visibility_on)
    else
        painterResource(id = R.drawable.visibility_off)
    OutlinedTextField(value = password,
        onValueChange =  onPasswordChange,
    val iconSize = when (windowSize.width) {
        WindowType.SMALL -> 20.dp // Smaller screen
        WindowType.MEDIUM -> 24.dp // Medium screen
        WindowType.LARGE -> 30.dp // Larger screen
    }
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Password") },
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon") },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(painter = icon, contentDescription = "Toggle Password Visibility", modifier = Modifier.size(iconSize))
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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

}

@Composable
fun ForgotPassword(windowSize: WindowSize) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                end = when (windowSize.width) {
                    WindowType.SMALL -> 16.dp
                    else -> 36.dp
                }
            ),
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
fun ButtonLogin(navController: NavController, viewModel: LoginViewModel, email: String, password: String, loginState: LoginViewModel.LoginState, windowSize: WindowSize){
    val context = LocalContext.current
    Button(
        onClick = {
            viewModel.login(email, password)
            when (loginState) {
                is LoginViewModel.LoginState.Loading -> {
                    // Do nothing
                }
                is LoginViewModel.LoginState.Success -> {
                    Log.i("Login screen successful", "Login success")
                    Toast.makeText(context, loginState.data?.message, Toast.LENGTH_SHORT).show()
                    navController.navigate("main_screen")
                }
                is LoginViewModel.LoginState.Error -> {
                    Toast.makeText(context, loginState.message, Toast.LENGTH_SHORT).show()
                    Log.i("Login screen error", "Login error $loginState.message")
                }
                LoginViewModel.LoginState.Idle -> {
                    // Do nothing
                }
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
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
    ) {
        Text(text = "Log In", color = Color.White)
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