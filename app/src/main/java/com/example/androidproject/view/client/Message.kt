package com.example.androidproject.view.client

import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.R
import com.example.androidproject.ViewModelSetups
import com.example.androidproject.data.preferences.AccountManager
import com.example.androidproject.model.Chats
import com.example.androidproject.utils.NetworkUtils.checkNetworkConnectivity
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.extras.LoadingUI
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.viewmodel.chats.GetChatViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    getChatViewModel: GetChatViewModel
) {
    val chatState = getChatViewModel.getChatsPagingData.collectAsLazyPagingItems()

    // State for search input and dropdown visibility
    var searchQuery by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val isConnected = remember { mutableStateOf(checkNetworkConnectivity(connectivityManager)) }

    var showLoading by remember { mutableStateOf(true) }
    var showRetryLoading  by remember { mutableStateOf(false) }
    val windowSize = rememberWindowSizeClass()
    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 16.sp
        WindowType.MEDIUM -> 18.sp
        WindowType.LARGE -> 20.sp
    }
    val poppinsFont = FontFamily(
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_bold, FontWeight.Bold)
    )


// Handle retry loading animation
    LaunchedEffect(showRetryLoading) {
        if (showRetryLoading) {
            delay(1500) // Show LoadingUI for 1.5 seconds
            isConnected.value = checkNetworkConnectivity(connectivityManager)
            if (isConnected.value) {
                showLoading = true // Trigger data fetch if internet is back
            }
            showRetryLoading = false // Hide loading animation
        }
    }

    // Filtered list of chats based on search query
    val filteredChats = remember(searchQuery, chatState.itemCount) {
        (0 until chatState.itemCount)
            .mapNotNull { chatState[it] }
            .filter { it.fullName.startsWith(searchQuery, ignoreCase = true) }
    }

    LaunchedEffect(Unit) {
        getChatViewModel.refreshChats()
    }


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
                        .padding(horizontal = 16.dp)
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
                if(!isConnected.value){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if(showRetryLoading){
                            LoadingUI()
                        }else{
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "No Internet Connection",
                                    fontSize = 18.sp,
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Please check your internet and try again.",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Box(
                                    modifier = Modifier
                                        .clickable {
                                            showRetryLoading = true // Start retry loading animation
                                        }
                                        .background(Color(0xFF3CC0B0), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "Retry",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }else{
                    // Check if chat list is empty
                    if (chatState.itemCount == 0) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No Message Available",
                                fontSize = nameTextSize,
                                color = Color.Black,
                                fontFamily = poppinsFont,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    } else {
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
    var showFullText by remember { mutableStateOf(false) }
    val maxPreviewLength = 20
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
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
                .padding(10.dp),
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
                    modifier = Modifier.fillMaxWidth(),
                    text = chats.fullName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (showFullText) chats.latestMessage else "${chats.latestMessage.take(maxPreviewLength)}...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.weight(1f) // Takes available space to push date to the end
                    )
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun MessageTopSection(navController: NavController) {
    val poppinsFont = FontFamily(
        Font(com.example.androidproject.R.font.poppins_regular, FontWeight.Normal),
        Font(com.example.androidproject.R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_bold, FontWeight.Bold)
    )
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
                text = "Messages",
                fontFamily = poppinsFont,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications Icon",
                tint = Color.Black,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { navController.navigate("notification") }
            )
        }
    }
}