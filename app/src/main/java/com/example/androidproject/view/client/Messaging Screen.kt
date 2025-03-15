package com.example.androidproject.view.client

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// Data class to represent a message with a sender/receiver flag
data class Message(val text: String, val isSent: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagingScreen(
    getMessages: GetMessagesViewModel,
    receiverId: Int,
    chatId: Int,
    receipientName: String,
    receipientProfile: String,
    navController: NavController
) {
    val pagingMessages = getMessages.messagesPagingData.collectAsLazyPagingItems()
    val newMessages by getMessages.newMessages.collectAsState()
    val userId = AccountManager.getAccount()?.id
    val webSocketMessages by WebSocketManager.incomingMessages.collectAsState()
    val listState = rememberLazyListState()
    var sentMessage by remember { mutableStateOf(false) }

    // Combine paged messages and real-time messages, ensuring they are sorted properly
    val allMessages by remember(webSocketMessages, pagingMessages, newMessages) {
        derivedStateOf {
            val paged = pagingMessages.itemSnapshotList.items
            val new = webSocketMessages.mapNotNull { json ->
                try {
                    val obj = JSONObject(json)
                    if (obj.has("type") && obj.getString("type") == "message") {
                        val data = obj.getJSONObject("data")
                        val createdAt = data.getString("created_at")
                        // Convert from server local time (UTC-8) to UTC
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).apply {
                            timeZone = TimeZone.getTimeZone("UTC")
                        }
                        val dateFormatLocal = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).apply {
                            timeZone = TimeZone.getTimeZone("Etc/GMT+8") // Fixed UTC-8
                        }
                        val localDate = dateFormatLocal.parse(createdAt)
                        val adjustedCreatedAt = if (localDate != null) {
                            val localTimeMillis = localDate.time
                            val offset = -8 * 60 * 60 * 1000L // Fixed -8 hours in milliseconds
                            Log.d("MessagingScreen", "WebSocket createdAt: $createdAt, localTimeMillis: $localTimeMillis, offset: $offset")
                            val utcTimeMillis = localTimeMillis - offset // Add 8 hours to convert UTC-8 to UTC
                            Log.d("MessagingScreen", "UTC timeMillis for ${data.getString("message")}: $utcTimeMillis")
                            dateFormat.format(Date(utcTimeMillis))
                        } else {
                            Log.w("MessagingScreen", "Failed to parse createdAt: $createdAt, using original")
                            createdAt
                        }
                        Log.d("MessagingScreen", "Adjusted WebSocket createdAt for ${data.getString("message")}: $adjustedCreatedAt")
                        Conversation(
                            id = data.getInt("id"),
                            userId = data.getString("user_id").toInt(),
                            receiverId = data.getString("receiver_id").toInt(),
                            chatId = data.optInt("chat_id", chatId),
                            message = data.getString("message"),
                            isRead = data.getInt("is_read"),
                            createdAt = adjustedCreatedAt // Adjusted to UTC
                        )
                    } else null
                } catch (e: Exception) {
                    Log.e("MessagingScreen", "Error parsing WebSocket message: $json", e)
                    null
                }
            }.distinctBy { it.id }
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            (paged + new + newMessages)
                .distinctBy { if (it.id == -1) "${it.message}_${it.createdAt}" else it.id.toString() }
                .sortedBy { message ->
                    try {
                        val time = dateFormat.parse(message.createdAt)?.time ?: Long.MAX_VALUE
                        Log.d("MessagingScreen", "Message: ${message.message}, createdAt: ${message.createdAt}, parsed time: $time")
                        time
                    } catch (e: Exception) {
                        Log.e("MessagingScreen", "Error parsing createdAt: ${message.createdAt}", e)
                        Long.MAX_VALUE
                    }
                }
        }
    }

// Scroll to the bottom when a new message arrives
    LaunchedEffect(allMessages) {
        if (allMessages.isNotEmpty()) {
            listState.animateScrollToItem(allMessages.lastIndex)
        }
    }

    // Ensure scrolling when sending a message
    LaunchedEffect(sentMessage) {
        if (sentMessage) {
            getMessages.refreshMessages()
            listState.animateScrollToItem(allMessages.lastIndex)
            sentMessage = false
        }
    }

    LaunchedEffect(Unit) {
        Log.d("MessagingScreen", "profile: $receipientProfile")
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
                WebSocketManager.sendMessage(userId.toString(), receiverId.toString(), newMessage)
                getMessages.refreshMessages()
                sentMessage = true
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
                    reverseLayout = false
                ) {
                    items(allMessages.size) { message ->
                        val message = allMessages[message]
                        MessageComposable(
                            message = message.message,
                            isSent = userId?.toInt() == message.userId
                        )
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
    val context = LocalContext.current
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
                } else {
                    Toast.makeText(context, "Message cannot be empty", Toast.LENGTH_SHORT).show()
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
