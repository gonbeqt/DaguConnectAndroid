package com.example.androidproject.view.client

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

// Data class to represent a message with a sender/receiver flag
data class Message(val text: String, val isSent: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagingScreen(initialMessages: List<Message> = emptyList(),navController: NavController) {
    // Mutable state to hold the list of messages
    val messages = remember { mutableStateListOf(*initialMessages.toTypedArray()) }

    Scaffold(
        modifier = Modifier.padding(WindowInsets.statusBars.asPaddingValues()),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { navController.navigate("main_screen")}
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tradesman's Name",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomInputBar(onSend = { newMessage ->
                // Add the new message as "Sent" (isSent = true)
                messages.add(Message(newMessage, true))
            })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (messages.isEmpty()) {
                Text(
                    text = "Start a conversation",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        color = Color.Gray
                    ),
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()) // Enable scrolling
                        .padding(8.dp)
                ) {
                    messages.forEach { message ->
                        MessageComposable(message = message.text, isSent = message.isSent)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomInputBar(onSend: (String) -> Unit) {
    var messageText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = messageText,
            onValueChange = { messageText = it },
            placeholder = { Text("Say Something...") },
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = {
                if (messageText.isNotBlank()) {
                    onSend(messageText) // Send the message
                    messageText = ""    // Clear the input field
                }
            },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = Color(0xFF39BFB1)
            )
        }
    }
}

@Composable
fun MessageComposable(message: String, isSent: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = if (isSent) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = if (isSent) Color(0xFFDCF8C6) else Color(0xFFF5F5F5), // Sender: light green, Receiver: light gray
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isSent) 16.dp else 4.dp, // Sender: rounded, Receiver: tail-like
                        bottomEnd = if (isSent) 4.dp else 16.dp // Sender: tail-like, Receiver: rounded
                    )
                )
                .padding(12.dp)
        ) {
            Text(
                text = message,
                color = Color.Black,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessagingScreenPreview() {
    MessagingScreen(
        initialMessages = listOf(
            Message("Hello!", true),              // Sent (right)
            Message("Hi, how are you?", false),   // Received (left)
            Message("I'm doing well!", true),     // Sent (right)
            Message("Great to hear!", false),     // Received (left)
            Message("I have a lot to say...", false), // Received (left)
            Message("Like, a lot!", false),       // Received (left)
            Message("Keep going!", false),        // Received (left)
            Message("Cool, I'm listening!", true) // Sent (right)
        ),
                navController = NavController(LocalContext.current)
    )
}

@Preview(showBackground = true)
@Composable
fun MessageComposablePreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        MessageComposable(message = "Hi there!", isSent = false) // Receiver (left)
        MessageComposable(message = "Hey, how's it going?", isSent = true) // Sender (right)
    }
}