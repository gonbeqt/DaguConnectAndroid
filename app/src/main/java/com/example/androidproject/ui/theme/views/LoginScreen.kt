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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun LoginScreen(){

    val context = LocalContext.current
    val username by remember {
        mutableStateOf("") }
    val password by remember {
        mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)
    )
     {
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {


            OutlinedTextField(value = username,
                onValueChange = {},
                label = { Text("Username")},
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "Username Icon")
                })

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = password,
                onValueChange = {},
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon")
                })


            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly)
            {
                TextButton(onClick = {}) {
                    Text(text = "Forgot Password?",
                        color = Color.Gray,
                        fontSize = 12.sp)
                }
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



            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
            },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue))
            {
                Text(text = "Login", color = Color.White)
            }

        }

    }




}