package com.example.androidproject.view.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androidproject.ViewModelSetups
import com.example.androidproject.model.Chats
import com.example.androidproject.viewmodel.chats.GetChatViewModel


@Composable
fun MessageScreen(modifier: Modifier = Modifier,navController: NavController, viewModel: GetChatViewModel ) {
    val chatState by viewModel.chatState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getChats()
    }

    when (chatState) {
        is GetChatViewModel.ChatState.Loading -> {
            // Show loading indicator
            Text(text = "Loading...")
        }
        is GetChatViewModel.ChatState.Success -> {
            val chats = (chatState as GetChatViewModel.ChatState.Success).data
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // search Bar
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .size(50.dp,)
                        .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFBEBEBE), shape = RoundedCornerShape(8.dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(42.dp)
                            .padding(start = 16.dp)
                    )
                    TextField(
                        value = "",
                        onValueChange = { /* Handle input */ },
                        placeholder = { Text(text = "Search...") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Chat List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(chats.size) { index -> // chat list size
                        ChatListItem(chats = chats[index])
                    }
                }
            }
        }
        is GetChatViewModel.ChatState.Error -> {
            val errorMessage = (chatState as GetChatViewModel.ChatState.Error).message
            Text(text = "Error: $errorMessage")
        }
        is GetChatViewModel.ChatState.Idle -> {
            //
        }
    }
}

@Composable
fun ChatListItem(chats: Chats) {
    val date = ViewModelSetups.formatDateTime(chats.createdAt)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        // Profile Icon
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(62.dp)
                .padding(8.dp),
            tint = Color.Gray
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Text Section
        Column(modifier = Modifier.weight(1f)) {
//            Text(
//                text = chats.receiverName,
//                style = MaterialTheme.typography.bodyLarge,
//                fontWeight = FontWeight.Bold,
//                color = Color.Black
//            )
            Text(
                text = chats.latestMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        // Timestamp
        Text(
            text = date,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}