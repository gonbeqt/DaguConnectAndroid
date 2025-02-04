package com.example.androidproject

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidproject.ui.theme.views.ClientPov.Navigation.BookingsScreen
import com.example.androidproject.ui.theme.views.ClientPov.Navigation.BookmarkedScreen
import com.example.androidproject.ui.theme.views.ClientPov.Navigation.HomeScreen
import com.example.androidproject.ui.theme.views.ClientPov.Navigation.ProfileScreen
import com.example.androidproject.ui.theme.views.ClientPov.Navigation.ScheduleScreen

@Composable
fun MainScreen(navController: NavController,modifier: Modifier = Modifier,) {
    val navItems = listOf(
        NavigationItem("Home", Icons.Default.Home),
        NavigationItem("Bookings", Icons.Default.ListAlt),
        NavigationItem("Schedule", Icons.Default.CalendarMonth),
        NavigationItem("Bookmarks", Icons.Default.CollectionsBookmark),
        NavigationItem("Profile", Icons.Default.Person)

    )
    var selectedItem by remember {
        mutableStateOf(0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar (containerColor = Color.White) {
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
                            (Text(text = item.nav_label, fontSize = 10.sp))
                        }
                    )



                }
            }
        }
    ) { innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding),selectedItem,navController)
    }
}

@Composable

fun ContentScreen(modifier: Modifier = Modifier, selectedItem: Int,navController: NavController) {
    when (selectedItem) {
        0 -> HomeScreen(modifier = modifier.padding(bottom = 0.1.dp),navController)
        1 -> BookingsScreen(modifier.padding(bottom = 0.1.dp),navController)
        2 -> ScheduleScreen(modifier.padding(bottom = 0.1.dp),navController)
        3 -> BookmarkedScreen(modifier.padding(bottom = 0.1.dp),navController)
        4 -> ProfileScreen(modifier.padding(bottom = 0.1.dp),navController)
    }
}

