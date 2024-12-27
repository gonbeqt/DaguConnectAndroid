package com.example.androidproject.ui.theme.views

import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidproject.R


@Composable
fun SignUpScreen(navController: NavController){

    val context = LocalContext.current
    var firstName by remember {
        mutableStateOf("") }
    var lastName by remember {
        mutableStateOf("") }
    var age by remember {
        mutableStateOf("") }
    var email by remember {
        mutableStateOf("") }
    var password by remember {
        mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val icon = if (passwordVisible)
        painterResource(id = R.drawable.visibility_on)
    else
        painterResource(id = R.drawable.visibility_off)

    Box(modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ){
        // Set an image as the background
        Image(
            painter = painterResource(id = R.drawable.background_image_auth), // Corrected name
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )



        Card(
            modifier = Modifier
                .padding(end = 50.dp) // Add padding to the right
                .size(350.dp, 600.dp), // Adjust card size

            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 20.dp,  // No radius on the top-right corner
                bottomStart = 0.dp,
                bottomEnd = 20.dp // No radius on the bottom-right corner
            ),
            elevation = CardDefaults.cardElevation(8.dp)


        ){
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {

                Text(
                    text = "Create an Account",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue,
                    modifier = Modifier.padding(bottom = 16.dp, top = 16.dp) // Optional padding below the title
                )

                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it }, // Updates the `firstName` state
                    label = { Text("First Name")},
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "First Name Icon"
                        )
                    },
                    singleLine = true,

                    modifier = Modifier
                        .fillMaxWidth(0.8f) // Adjust width as needed
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
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Last Name Icon"
                        )
                    },
                    singleLine = true,

                    modifier = Modifier
                        .fillMaxWidth(0.8f) // Adjust width as needed
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
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Last Name Icon"
                        )
                    },
                    singleLine = true,

                    modifier = Modifier
                        .fillMaxWidth(0.8f) // Adjust width as needed
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
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email Icon"
                        )
                    },
                    singleLine = true,

                    modifier = Modifier
                        .fillMaxWidth(0.8f) // Adjust width as needed
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
                    onValueChange = { password = it},
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon")
                    },

                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisible = !passwordVisible
                        }) {
                            Icon(
                                painter = icon, contentDescription = "Visible", modifier = Modifier.size(20.dp))

                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(0.8f) // Adjust width as needed
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
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f) // 80% width
                            .height(50.dp), // Set height
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                    )
                    {
                        Text(text = "Sign Up", color = Color.White)
                    }
                }



                Spacer(modifier = Modifier.height(5.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly)
                {
                    TextButton(onClick = {navController.navigate("login")}) {
                        Row {
                            Text(
                                text = "Already have an account? ",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Log In",
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }


            }

        }
    }



}