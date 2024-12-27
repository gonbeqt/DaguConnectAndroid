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
import androidx.compose.foundation.clickable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidproject.R

@Preview
@Composable
fun LogInScreen(){

    val context = LocalContext.current
    var email by remember {
        mutableStateOf("") }
    var password by remember {
        mutableStateOf("") }
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
                .padding(start = 50.dp) // Add padding to the right
                .size(350.dp, 400.dp), // Adjust card size

            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 0.dp,  // No radius on the top-right corner
                bottomStart = 20.dp,
                bottomEnd = 0.dp // No radius on the bottom-right corner
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ){
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {

                Text(
                    text = "Welcome Back",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue,
                    modifier = Modifier.padding(bottom = 16.dp, top = 16.dp) // Optional padding below the title
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
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon")
                    },
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 36.dp, top = 5.dp, bottom = 5.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        modifier = Modifier
                            .clickable(onClick = {}) // clickable text
                            .padding(0.dp),
                        text = "Forgot Password?",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            Toast.makeText(context, "Log In Successful", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f) // 80% width
                            .height(50.dp), // Set height
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                    )
                    {
                        Text(text = "Log In", color = Color.White)
                    }
                }



                Spacer(modifier = Modifier.height(5.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly)
                {
                    TextButton(onClick = {}) {
                        Row {
                            Text(
                                text = "Don't have an account? ",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Sign Up",
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