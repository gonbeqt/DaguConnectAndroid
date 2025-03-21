package com.example.androidproject.view.client

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.ViewModelSetups
import com.example.androidproject.data.preferences.AccountManager
import com.example.androidproject.model.Chats
import com.example.androidproject.view.extras.LoadingUI
import com.example.androidproject.viewmodel.chats.GetChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    getChatViewModel: GetChatViewModel
) {
    val chatState = getChatViewModel.getChatsPagingData.collectAsLazyPagingItems()
    val loadState = chatState.loadState

    // State for search input and dropdown visibility
    var searchQuery by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    // Filtered list of chats based on search query
    val filteredChats = remember(searchQuery, chatState.itemCount) {
        (0 until chatState.itemCount)
            .mapNotNull { chatState[it] }
            .filter { it.fullName.startsWith(searchQuery, ignoreCase = true) }
    }

    LaunchedEffect(Unit) {
        getChatViewModel.refreshChats()
    }

    when {
        loadState.refresh is LoadState.Loading && chatState.itemCount == 0 -> {
            LoadingUI()
        }
        else -> {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(WindowInsets.statusBars.asPaddingValues())
            ) {
                MessageTopSection(navController)

                // Search Bar with ExposedDropdownMenuBox
                ExposedDropdownMenuBox(
                    expanded = isDropdownExpanded && filteredChats.isNotEmpty(),
                    onExpandedChange = { isDropdownExpanded = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFFBEBEBE), shape = RoundedCornerShape(8.dp))
                            .menuAnchor(), // Anchor the dropdown to this Row
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
                            value = searchQuery,
                            onValueChange = { query ->
                                searchQuery = query
                                isDropdownExpanded = query.isNotEmpty() // Show dropdown when typing
                            },
                            placeholder = { Text(text = "Search...") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                disabledContainerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true
                        )
                    }

                    // Dropdown Menu
                    ExposedDropdownMenu(
                        expanded = isDropdownExpanded && filteredChats.isNotEmpty(),
                        onDismissRequest = { isDropdownExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(Color.White)
                    ) {
                        filteredChats.forEach { chat ->
                            DropdownChatItem(
                                chats = chat,
                                navController = navController,
                                onClick = {
                                    val encodedProfilePicture = Uri.encode(chat.profilePicture)
                                    navController.navigate("messaging/${chat.id}/${chat.userId1}/${chat.fullName}/$encodedProfilePicture")
                                    isDropdownExpanded = false // Close dropdown after selection
                                    searchQuery = "" // Clear search after selection
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(chatState.itemCount) { index ->
                        val chats = chatState[index]
                        if (chats != null) {
                            ChatListItem(chats = chats, navController = navController)
                        }
                    }
                    if (loadState.append is LoadState.Loading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}

// Smaller version of ChatListItem for dropdown
@Composable
fun DropdownChatItem(
    chats: Chats,
    navController: NavController,
    onClick: () -> Unit
) {
    val date = ViewModelSetups.formatDateTime(chats.createdAt)
    val receiverId = if (AccountManager.getAccount()?.id != chats.userId1) chats.userId1 else chats.userId2

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp) // Smaller padding
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), // Reduced padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = chats.profilePicture,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(40.dp) // Smaller profile image
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chats.fullName,
                    style = MaterialTheme.typography.bodyMedium, // Smaller text
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun ChatListItem(chats: Chats, navController: NavController) {
    val date = ViewModelSetups.formatDateTime(chats.createdAt)
    val receiverId = if (AccountManager.getAccount()?.id != chats.userId1) chats.userId1 else chats.userId2

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clickable {
                val encodedProfilePicture = Uri.encode(chats.profilePicture)
                navController.navigate("messaging/${chats.id}/$receiverId/${chats.fullName}/$encodedProfilePicture")
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = chats.profilePicture,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(62.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chats.fullName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = chats.latestMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun MessageTopSection(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(0.2.dp)
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(horizontal = 25.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Message",
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications Icon",
                tint = Color.Black,
                modifier = Modifier
                    .size(35.dp)
                    .clickable { navController.navigate("notification") }
            )
        }
    }
}