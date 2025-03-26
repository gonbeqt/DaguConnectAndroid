package com.example.androidproject.view.client

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.R
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
    var chatIds by remember { mutableStateOf(chatId) }
    if (chatId.toInt() == 0){
        chatIds = chatIds
    }
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
                            val utcTimeMillis = localTimeMillis - offset
                            dateFormat.format(Date(utcTimeMillis))
                        } else {
                            createdAt
                        }
                        val conversation = Conversation(
                            id = data.getInt("id"),
                            userId = data.getString("user_id").toInt(),
                            receiverId = data.getString("receiver_id").toInt(),
                            chatId = data.getInt("chat_id"), // Use the chat_id from WebSocket
                            message = data.getString("message"),
                            isRead = data.getInt("is_read"),
                            createdAt = adjustedCreatedAt
                        )
                        // Filter by chatId
                        if (conversation.chatId == chatId) {
                            conversation
                        } else {
                            null
                        }
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
                        dateFormat.parse(message.createdAt)?.time ?: Long.MAX_VALUE
                    } catch (e: Exception) {
                        Log.e("MessagingScreen", "Error parsing createdAt: ${message.createdAt}", e)
                        Long.MAX_VALUE
                    }
                }
        }
    }

    var selectedIndex by remember { mutableIntStateOf(-1) }
    var otherReason by remember { mutableStateOf("") }
    var reasonDescription by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showReportSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val reasons = listOf(
        "Abusive or Harassing Behavior",
        "Inappropriate Content or Language",
        "Fraudulent Activity or Scam",
        "Poor Quality of Service",
        "Unprofessional Conduct",
        "Safety Concerns",
        "Others"
    )
    var reportDocument by remember { mutableStateOf<Uri?>(null) }
    var showMenu by remember { mutableStateOf(false) }
    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { reportDocument = it } }
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

            // Wait for messages to update
            if (allMessages.isNotEmpty()) {
                listState.animateScrollToItem(allMessages.lastIndex)
            }

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
                                .clickable { navController.navigate("main_screen?selectedItem=3"){
                                    navController.popBackStack()
                                } }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        AsyncImage(
                            model = receipientProfile,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(32.dp)
                                .padding(end = 8.dp)
                        )
                        Row(modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween){
                            Text(
                                text = receipientName,
                                fontWeight = FontWeight.Medium,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Box {
                                Icon(
                                    imageVector = Icons.Outlined.Report,
                                    contentDescription = "Report Icon",
                                    modifier = Modifier
                                        .padding(end = 10.dp)
                                        .size(24.dp)
                                        .clickable { showMenu = true }
                                )

                                // Popup Menu
                                DropdownMenu(
                                    expanded = showMenu,
                                    onDismissRequest = { showMenu = false },
                                    modifier = Modifier.background(Color.White)
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Report", textAlign = TextAlign.Center) },
                                        onClick = {
                                            showMenu = false
                                            showReportSheet = true
                                        }
                                    )
                                }
                            }
                        }

                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomInputBar(onSend = { newMessage ->
                WebSocketManager.sendMessage(userId.toString(), receiverId.toString(), newMessage, chatId)
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
                        chatIds = message.chatId
                        MessageComposable(
                            message = message.message,
                            isSent = userId?.toInt() == message.userId
                        )
                    }
                }
            }
        }
    }
    if (showReportSheet) {
        ModalBottomSheet(
            onDismissRequest = { showReportSheet = false },
            sheetState = bottomSheetState,
            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Report", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    IconButton(onClick = { showReportSheet = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Column(modifier = Modifier.padding(top = 16.dp)) {

                    reasons.forEachIndexed { index, reason ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        ) {
                            Checkbox(
                                checked = selectedIndex == index,
                                onCheckedChange = {
                                    selectedIndex = if (selectedIndex == index) -1 else index
                                }
                            )
                            if (reason == "Others") {
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = reason,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )

                                    if (selectedIndex == index) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        TextField(
                                            value = otherReason,
                                            onValueChange = { otherReason = it },
                                            placeholder = { Text("Enter other reason") },
                                            singleLine = true,
                                            modifier = Modifier
                                                .heightIn(min = 40.dp),
                                            colors = TextFieldDefaults.colors(
                                                focusedContainerColor = Color.Transparent,
                                                unfocusedContainerColor = Color.Transparent,
                                                focusedIndicatorColor = Color.Blue,
                                                unfocusedIndicatorColor = Color.Gray,
                                                cursorColor = Color.Black
                                            )
                                        )
                                    }
                                }
                            } else {
                                Text(
                                    text = reason,
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }

                        }
                    }
                }
                if (selectedIndex != -1) {
                    Text(
                        text = "Tell us the Problem",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    UploadFieldScreenShot(
                        label = "Screenshot",
                        uri = reportDocument,
                        fileType = "image",
                        onUploadClick = {
                            documentPickerLauncher.launch("image/*")
                        },
                        onViewClick = {
                            reportDocument?.let { uri ->
                                openScreenShot(context, uri)
                            }
                        }
                    )

                    OutlinedTextField(
                        value = reasonDescription,
                        onValueChange = { reasonDescription = it },
                        placeholder = { Text("Enter Your Explanation") },
                        shape = RoundedCornerShape(16.dp),
                        maxLines = 3,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Blue,
                            unfocusedIndicatorColor = Color.Gray,
                            focusedLabelColor = Color.Blue,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.Black
                        )
                    )
                }


                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { showReportSheet = false },
                        modifier = Modifier.size(110.dp, 45.dp)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {

                        },
                        modifier = Modifier.size(110.dp, 45.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(
                                0xFF42C2AE
                            )
                        )
                    ) {
                        Text("Submit", color = Color.White)
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
            .background(Color.White)
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
                containerColor = Color(0xFFDFFFFA),
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
