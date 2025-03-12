package com.example.androidproject.view.client

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.data.WebSocketManager
import com.example.androidproject.data.preferences.AccountManager
import com.example.androidproject.model.Conversation
import com.example.androidproject.viewmodel.messeges.GetMessagesViewModel
import org.json.JSONObject

// Data class to represent a message with a sender/receiver flag
data class Message(val text: String, val isSent: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagingScreen(getMessages: GetMessagesViewModel, receiverId: Int, chatId:Int, receipientName: String, receipientProfile: String, navController: NavController) {
    // Mutable state to hold the list of messages
    val pagingMessages = getMessages.messagesPagingData.collectAsLazyPagingItems()
    val newMessages by getMessages.newMessages.collectAsState()
    val userId = AccountManager.getAccount()?.id
    val webSocketMessages by WebSocketManager.incomingMessages.collectAsState()
    val listState = rememberLazyListState()

    // Combine paged messages and real-time messages
    val allMessages by remember {
        derivedStateOf {
            val paged = pagingMessages.itemSnapshotList.items
            val new = newMessages + webSocketMessages.mapNotNull { json ->
                try {
                    val obj = JSONObject(json)
                    // Check if it's a message with "type" and "data"
                    if (obj.has("type") && obj.getString("type") == "message") {
                        val data = obj.getJSONObject("data")
                        Conversation(
                            id = data.getInt("id"),
                            userId = data.getString("user_id").toInt(),
                            receiverId = data.getString("receiver_id").toInt(),
                            chatId = data.optInt("chat_id", chatId),
                            message = data.getString("message"),
                            isRead = data.getInt("is_read"),
                            createdAt = data.getString("created_at")
                        )
                    } else {
                        // Skip non-message types like "authenticated"
                        null
                    }
                } catch (e: Exception) {
                    Log.e("MessagingScreen", "Error parsing WebSocket message: $json", e)
                    null
                }
            }
            paged + new
        }
    }

    LaunchedEffect(allMessages.size) {
        if (allMessages.isNotEmpty()) {
            listState.animateScrollToItem(0) // Scroll to the bottom (reverseLayout = true)
        }
    }

    // Connect to WebSocket when the screen is composed
    LaunchedEffect(Unit) {
        Log.d("MessagingScreen", "profile: $receipientProfile")
        WebSocketManager.connect(userId.toString())
        getMessages.refreshMessages()
    }

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
                                .clickable { navController.navigate("message_screen") }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        AsyncImage(
                            model = receipientProfile,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(32.dp)
                                .padding(end = 8.dp)
                        )
                        Text(
                            text = receipientName,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomInputBar(onSend = { newMessage ->
                WebSocketManager.sendMessage(userId.toString(), receiverId.toString(), newMessage) // Receiver ID hardcoded as "2" for now
                getMessages.addLocalMessage(newMessage, receiverId, chatId)
            })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (allMessages.isEmpty()) {
                Text(
                    text = "Start a conversation",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp, color = Color.Gray),
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    reverseLayout = true // Messages load from bottom to top
                ) {
                    items(allMessages.size) { index ->
                        val message = allMessages[allMessages.size - 1 - index]
                        MessageComposable(
                            message = message.message,
                            isSent = userId?.toInt() == message.userId
                        )
                    }
                }
            }
        }
    }

    // Cleanup on dispose
    DisposableEffect(Unit) {
        onDispose {
            WebSocketManager.disconnect()
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
