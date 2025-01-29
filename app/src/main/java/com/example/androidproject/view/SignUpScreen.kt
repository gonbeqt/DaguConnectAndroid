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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Work
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
import com.example.androidproject.viewmodel.RegisterViewModel

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    // Use a mock or placeholder NavController for preview purposes
    //SignUpScreen(navController = rememberNavController())
}

@Composable
fun SignUpScreen(navController: NavController, viewModel: RegisterViewModel) {
    val windowSize = rememberWindowSizeClass()
    val registerState by viewModel.registerState.collectAsState()

    var firstName by remember {
        mutableStateOf("")
    }
    var lastName by remember {
        mutableStateOf("")
    }
    var userName by remember {
        mutableStateOf("")
    }
    var age by remember {
        mutableStateOf(0)
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var confirmPassword = password
    var isClient by remember {
        mutableStateOf(false)
    }

    // Animatable for the card's X offset
    val cardOffsetX = remember { Animatable(-500f) } // Start off-screen to the left
    val currentBackStackEntry = navController.currentBackStackEntryAsState()

    // Launch animation when composable is composed
    LaunchedEffect(currentBackStackEntry.value) {
        cardOffsetX.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }

    // Determine card size and padding based on screen width
    val cardWidth = when (windowSize.width) {
        WindowType.SMALL -> 0.9f
        WindowType.MEDIUM -> 0.7f
        WindowType.LARGE -> 0.5f
    }
    val cardHeight = when (windowSize.height) {
        WindowType.SMALL -> 620.dp
        WindowType.MEDIUM -> 750.dp
        WindowType.LARGE -> 800.dp
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.CenterStart
    ) {
        // Set an image as the background
        Image(
            painter = painterResource(id = R.drawable.background_image_auth),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Card(
            modifier = Modifier
                .offset(x = cardOffsetX.value.dp) // Apply animation offset
                .fillMaxWidth(cardWidth)
                .height(cardHeight),
            shape = RoundedCornerShape(
                topEnd = 20.dp,
                bottomEnd = 20.dp
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {

                InputFieldForSignUp(
                    firstName = firstName,
                    onFirstNameChange = { firstName = it },
                    lastName = lastName,
                    onLastNameChange = { lastName = it },
                    userName = userName,
                    onUserNameChange = { userName = it },
                    age = age,
                    onAgeChange = { age = it },
                    email = email,
                    onEmailChange = { email = it },
                    password = password,
                    onPasswordChange = { password = it },
                    windowSize)
                Spacer(modifier = Modifier.height(10.dp))

                Roles(isClient = isClient, onIsClientChange = {isClient = it})

                Spacer(modifier = Modifier.height(30.dp))
                RegistrationButton(navController, viewModel, firstName, lastName, userName, email, age.toInt(), isClient, password, confirmPassword, registerState, windowSize)

                Spacer(modifier = Modifier.height(5.dp))
                RegistrationLoginButton(navController)


            }

        }
    }

}
@Composable
fun InputFieldForSignUp(
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    userName: String,
    onUserNameChange: (String) -> Unit,
    age: Int,
    onAgeChange: (Int) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    windowSize: WindowSize
    ){
    val fieldWidth = when (windowSize.width) {
        WindowType.SMALL -> 0.9f
        WindowType.MEDIUM -> 0.8f
        WindowType.LARGE -> 0.7f
    }
    var passwordVisible by remember { mutableStateOf(false) }
    val icon = if (passwordVisible)
        painterResource(id = R.drawable.visibility_on)
    else
        painterResource(id = R.drawable.visibility_off)

    Text(
        text = "Create an Account",
        fontSize = when (windowSize.width) {
            WindowType.SMALL -> 20.sp
            WindowType.MEDIUM -> 24.sp
            else -> 28.sp
        },
        fontWeight = FontWeight.Bold,
        color = Color.Blue,
        modifier = Modifier.padding(bottom = 16.dp, top = 16.dp) // Optional padding below the title
    )

    OutlinedTextField(
        value = firstName,
        onValueChange = onFirstNameChange, // Updates the `firstName` state
        label = { Text("First Name")},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "First Name Icon"
            )
        },
        singleLine = true,

        modifier = Modifier
            .fillMaxWidth(fieldWidth) // Adjust width as needed
            .heightIn(min = 56.dp), // Adjust height as needed
        shape = RoundedCornerShape(16.dp), // Set corner radius here
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Blue,
            unfocusedIndicatorColor = Color.Gray,
            focusedLabelColor = Color.Blue,
            unfocusedLabelColor = Color.Gray,
            cursorColor = Color.Black
        ),
        textStyle = TextStyle(
            fontSize = 16.sp, // Adjust text size for visibility
            color = Color.Black // Ensure text is visible
        )

    )
    Spacer(modifier = Modifier.height(10.dp))
    OutlinedTextField(
        value = lastName,
        onValueChange = onLastNameChange,
        label = { Text("Last Name") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Last Name Icon"
            )
        },
        singleLine = true,

        modifier = Modifier
            .fillMaxWidth(fieldWidth) // Adjust width as needed
            .heightIn(min = 56.dp), // Adjust height as needed
        shape = RoundedCornerShape(16.dp), // Set corner radius here
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Blue,
            unfocusedIndicatorColor = Color.Gray,
            focusedLabelColor = Color.Blue,
            unfocusedLabelColor = Color.Gray,
            cursorColor = Color.Black
        ),
        textStyle = TextStyle(
            fontSize = 16.sp, // Adjust text size for visibility
            color = Color.Black // Ensure text is visible
        )
    )
    Spacer(modifier = Modifier.height(10.dp))
    OutlinedTextField(
        value = userName,
        onValueChange = onUserNameChange,
        label = { Text("Username") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Username Icon"
            )
        },
        singleLine = true,

        modifier = Modifier
            .fillMaxWidth(fieldWidth) // Adjust width as needed
            .heightIn(min = 56.dp), // Adjust height as needed
        shape = RoundedCornerShape(16.dp), // Set corner radius here
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Blue,
            unfocusedIndicatorColor = Color.Gray,
            focusedLabelColor = Color.Blue,
            unfocusedLabelColor = Color.Gray,
            cursorColor = Color.Black
        ),
        textStyle = TextStyle(
            fontSize = 16.sp, // Adjust text size for visibility
            color = Color.Black // Ensure text is visible
        )
    )
    Spacer(modifier = Modifier.height(10.dp))
    OutlinedTextField(
        value = age.toString(),
        onValueChange = {
            newValue -> // Convert String back to Int safely
            val intValue = newValue.toIntOrNull() ?: 0
            onAgeChange(intValue)
        },
        label = { Text("Age") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Last Name Icon"
            )
        },
        singleLine = true,

        modifier = Modifier
            .fillMaxWidth(fieldWidth) // Adjust width as needed
            .heightIn(min = 56.dp), // Adjust height as needed
        shape = RoundedCornerShape(16.dp), // Set corner radius here
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Blue,
            unfocusedIndicatorColor = Color.Gray,
            focusedLabelColor = Color.Blue,
            unfocusedLabelColor = Color.Gray,
            cursorColor = Color.Black
        ),
        textStyle = TextStyle(
            fontSize = 16.sp, // Adjust text size for visibility
            color = Color.Black // Ensure text is visible
        )
    )
    Spacer(modifier = Modifier.height(10.dp))
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
            .fillMaxWidth(fieldWidth) // Adjust width as needed
            .heightIn(min = 56.dp), // Adjust height as needed
        shape = RoundedCornerShape(16.dp), // Set corner radius here
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Blue,
            unfocusedIndicatorColor = Color.Gray,
            focusedLabelColor = Color.Blue,
            unfocusedLabelColor = Color.Gray,
            cursorColor = Color.Black
        ),
        textStyle = TextStyle(
            fontSize = 16.sp, // Adjust text size for visibility
            color = Color.Black // Ensure text is visible
        )
    )
    Spacer(modifier = Modifier.height(10.dp))
    OutlinedTextField(value = password,
        onValueChange = onPasswordChange,
        label = { Text("Password") },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon")
        },

        trailingIcon = {
            IconButton(onClick = {
                passwordVisible = !passwordVisible
            }) {
                Icon(
                    painter = icon, contentDescription = "Visible", modifier = Modifier.size(22.dp))

            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth(fieldWidth) // Adjust width as needed
            .heightIn(min = 56.dp), // Adjust height as needed
        shape = RoundedCornerShape(16.dp), // Set corner radius here
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Blue,
            unfocusedIndicatorColor = Color.Gray,
            focusedLabelColor = Color.Blue,
            unfocusedLabelColor = Color.Gray,
            cursorColor = Color.Black
        ),
        textStyle = TextStyle(
            fontSize = 16.sp, // Adjust text size for visibility
            color = Color.Black // Ensure text is visible
        )
    )
}
@Composable
fun Roles(isClient: Boolean, onIsClientChange: (Boolean) -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth().padding(start = 15.dp,end = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // First Card
        Card(
            modifier = Modifier.size(70.dp)
                .weight(1f)
                .padding(8.dp)
                .clickable {onIsClientChange (true)}, //Implementation here
            colors = CardDefaults.cardColors(Color.Blue)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)

            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Person Icon",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Client",
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }

        // Second Card
        Card(
            modifier = Modifier.size(70.dp)
                .weight(1f)
                .padding(8.dp)
                .clickable { onIsClientChange(false) }, //Implementation here
            colors = CardDefaults.cardColors(Color.Blue)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)

            ) {
                Icon(
                    imageVector = Icons.Default.Work,
                    contentDescription = "Person Icon",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tradesman",
                    fontSize = 12.sp,
                    color = Color.White,
                )
            }
        }
    }
}
@Composable
fun RegistrationButton(navController: NavController, viewModel: RegisterViewModel, firstName: String, lastName: String, userName: String, email: String, age: Int, isClient: Boolean, password: String, confirmPassword: String, registerState: RegisterViewModel.RegisterState, windowSize: WindowSize){
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = {
                viewModel.register(firstName, lastName, userName, email, age, isClient, password, confirmPassword)
                when (registerState) {
                    is RegisterViewModel.RegisterState.Loading -> {
                        // Do nothing
                    }
                    is RegisterViewModel.RegisterState.Success -> {
                        Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                        navController.navigate("login")
                    }
                    is RegisterViewModel.RegisterState.Error -> {
                        Toast.makeText(context, registerState.message, Toast.LENGTH_SHORT).show()
                        Log.i("Register screen error", "Register error $registerState.message")
                    }
                    RegisterViewModel.RegisterState.Idle -> {
                        // Do nothing
                    }
                }

            },
            modifier = Modifier
                .fillMaxWidth(0.6f) // 80% width
                .height(50.dp), // Set height
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
        )
        {
            Text(text = "Sign Up", color = Color.White)
        }
    }
}
@Composable
fun RegistrationLoginButton(navController: NavController){
    Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly)
    {
        Row {
            Text(
                modifier = Modifier.clickable(onClick = {navController.navigate("login")}),
                text = "Don't have an account? ",
                color = Color.Gray,
                fontSize = 12.sp
            )
            Text(
                modifier = Modifier.clickable(onClick = {navController.navigate("login")}),
                text = "Sign In",
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

}