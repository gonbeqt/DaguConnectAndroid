package com.example.androidproject

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.androidproject.pages.BookmarkedScreen
import com.example.androidproject.pages.HomeScreen
import com.example.androidproject.pages.MessageScreen
import com.example.androidproject.pages.ScheduleScreen

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navItems = listOf(
        NavigationItem("Home", Icons.Default.Home),
        NavigationItem("Schedule", Icons.Default.CalendarMonth),
        NavigationItem("Bookmark", Icons.Default.CollectionsBookmark),
        NavigationItem("Message", Icons.Default.Message)

    )
    var selectedItem by remember {
        mutableStateOf(0)
    }
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                        },
                        icon = {
                            Icon(imageVector = item.icon, contentDescription = "Icon")
                        },
                        label = {
                            (Text(text = item.nav_label))
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding),selectedItem)
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedItem: Int) {
    when (selectedItem) {
        0 -> HomeScreen()
        1 -> ScheduleScreen()
        2 -> BookmarkedScreen()
        3 -> MessageScreen()
    }
}


