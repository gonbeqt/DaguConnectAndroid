package com.example.androidproject.ui.theme.views.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.androidproject.R
import com.example.androidproject.ui.theme.views.Categories
import com.example.androidproject.ui.theme.views.Tradesman
import com.example.androidproject.ui.theme.views.WindowSize
import com.example.androidproject.ui.theme.views.WindowType
import com.example.androidproject.ui.theme.views.rememberWindowSizeClass


@Composable
fun HomeScreen( modifier: Modifier = Modifier,navController: NavController) {
    val windowSize = rememberWindowSizeClass()

    val selectedCategory = remember { mutableStateOf<String?>(null) }

    val categories = listOf(
        Categories(R.drawable.plumbing, "Plumber"),
        Categories(R.drawable.electrical, "Electrical"),
        Categories(R.drawable.cleaning, "Cleaning"),
        Categories(R.drawable.carpentry, "Carpentry")
    )

    val tradesmen = listOf(
        Tradesman(R.drawable.pfp, "Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp, "Alex", "Electrical", "P600/hr", 4.8, R.drawable.bookmark),
        Tradesman(R.drawable.pfp, "Liam", "Cleaning", "P450/hr", 4.2, R.drawable.bookmark),
        Tradesman(R.drawable.pfp, "Liam", "Carpentry", "P450/hr", 4.2, R.drawable.bookmark),
        Tradesman(R.drawable.pfp, "Liam", "Cleaning", "P450/hr", 4.2, R.drawable.bookmark),
        Tradesman(R.drawable.pfp, "Liam", "Carpentry", "P450/hr", 4.2, R.drawable.bookmark),
        Tradesman(R.drawable.pfp, "Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp, "Alex", "Electrical", "P600/hr", 4.8, R.drawable.bookmark),
        Tradesman(R.drawable.pfp, "Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp, "Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark)
    )

    val filteredTradesmen = if (selectedCategory.value != null) {
        tradesmen.filter { it.category == selectedCategory.value }
    } else {
        tradesmen
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Provide navController to the SearchField
            SearchField(navController,windowSize )
            Spacer(modifier = Modifier.height(5.dp))

            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {
                Spacer(modifier = Modifier.height(30.dp))
                ExploreNow(windowSize)

                Spacer(modifier = Modifier.height(20.dp))
                CategoryRow(categories, selectedCategory)

                Spacer(modifier = Modifier.height(5.dp))
                TradesmanColumn(filteredTradesmen, selectedCategory,navController)
            }
        }
    }
}
@Composable
fun SearchField(navController: NavController,windowSize: WindowSize) {
    var searchQuery by remember { mutableStateOf("") }
    val iconSize = when (windowSize.width) {
        WindowType.SMALL -> 24.dp
        WindowType.MEDIUM -> 28.dp
        WindowType.LARGE -> 32.dp
    }

    val textSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .size(90.dp)
            .background(Color.White)
            .padding(horizontal = 25.dp)
    ) {
        Row(
            modifier = Modifier
                .width(280.dp)
                .height(70.dp)
                .padding(top = 20.dp)
                .background(
                    Color(0xFFFFFFFF),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    1.dp,
                    Color(0xFF3CC0B0),
                    shape = RoundedCornerShape(8.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color(0xFF3CC0B0),
                modifier = Modifier
                    .size(iconSize)
                    .padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.weight(1f),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, color = Color(0xFF3CC0B0)),
                decorationBox = { innerTextField ->
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Search for services or tradespeople...",
                            style = androidx.compose.ui.text.TextStyle(fontSize = textSize, color = Color.Gray)
                        )
                    }
                    innerTextField()
                }
            )
        }

        // Notification and Message Icons outside the search field
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications Icon",
                tint = Color(0xFF3CC0B0),
                modifier = Modifier
                    .size(iconSize)
            )
            Icon(
                imageVector = Icons.Default.Message,
                contentDescription = "Message Icon",
                tint = Color(0xFF3CC0B0),
                modifier = Modifier
                    .size(iconSize)
                    .clickable {
                        navController.navigate("message_screen")

                    }
            )
        }
    }
}

@Composable
fun CategoryRow(categories: List<Categories>, selectedCategory: MutableState<String?>) {
    val windowSize = rememberWindowSizeClass()
    val cardSize = when (windowSize.width) {
        WindowType.SMALL -> 100.dp to 80.dp
        WindowType.MEDIUM -> 120.dp to 100.dp
        WindowType.LARGE -> 140.dp to 120.dp
    }

    val iconSize = when (windowSize.width) {
        WindowType.SMALL -> 20.dp
        WindowType.MEDIUM -> 30.dp
        WindowType.LARGE -> 40.dp
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, end = 25.dp),
             horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Categories",
            fontSize = when (windowSize.width) {
                WindowType.SMALL -> 16.sp
                WindowType.MEDIUM -> 18.sp
                WindowType.LARGE -> 20.sp
            },
            fontWeight = FontWeight(500),
            modifier = Modifier.padding(top = 10.dp)
        )
        TextButton(onClick = {}) {
            Text(
                text = "View All",
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardSize.second)
            .background(Color(0xFFECECEC)),
    ) {
        items(categories.size) { index ->
            val category = categories[index]
            CategoryItem(category) {
                // Set the clicked category
                selectedCategory.value = category.name
            }
        }
    }
}

@Composable
fun TradesmanColumn(tradesmen: List<Tradesman>,selectedCategory: MutableState<String?>,navController: NavController) {
    val showDialogAllTradesman = remember { mutableStateOf(false) }
    val windowSize = rememberWindowSizeClass()
    val cardHeight = when (windowSize.width) {
        WindowType.SMALL -> 120.dp
        WindowType.MEDIUM -> 140.dp
        WindowType.LARGE -> 160.dp
    }

    val textSize = when (windowSize.width) {
        WindowType.SMALL -> 14.sp
        WindowType.MEDIUM -> 16.sp
        WindowType.LARGE -> 18.sp
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, end = 25.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Top-Rated",
            fontSize = 18.sp,
            fontWeight = FontWeight(500),
            modifier = Modifier.padding(top = 10.dp)
        )
        TextButton(onClick = { showDialogAllTradesman.value = true
            selectedCategory.value = null}) {
            Text(
                text = "See All",
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(Color(0xFFECECEC)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            tradesmen.forEach { trade ->
                TradesmanItem(trade,navController = navController, cardHeight, textSize)
            }
        }
    }
    if (showDialogAllTradesman.value) {
            Dialog(onDismissRequest = { showDialogAllTradesman.value = false }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFECECEC)),
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "All Tradesmen",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(16.dp)
                            )
                            TextButton(onClick = { showDialogAllTradesman.value = false }) {
                                Text(text = "Close", fontSize = 16.sp, color = Color.Black, modifier = Modifier.padding(top = 7.dp))
                            }
                        }
                        Box(Modifier.verticalScroll(rememberScrollState())
                        )
                        {
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .background(Color(0xFFECECEC)),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                tradesmen.forEach { trade ->
                                    TradesmanItem(trade,navController = navController, cardHeight, textSize)
                                }
                            }
                        }


                    }
                }
            }

    }


}




@Composable
fun ExploreNow(windowSize: WindowSize){
    val textSize = when (windowSize.width) {
        WindowType.SMALL -> 16.sp
        WindowType.MEDIUM -> 18.sp
        WindowType.LARGE -> 20.sp
    }

    val imageSize = when (windowSize.width) {
        WindowType.SMALL -> 250.dp to 250.dp
        WindowType.MEDIUM -> 300.dp to 300.dp
        WindowType.LARGE -> 350.dp to 350.dp
    }
    Row(modifier = Modifier
        .fillMaxWidth()
        .size(180.dp)
        .padding(start = 25.dp, end = 25.dp)
        .background(
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFF81D796), Color(0xFF39BFB1)),
                start = androidx.compose.ui.geometry.Offset(0f, 1f),
                end = androidx.compose.ui.geometry.Offset(1f, 1f)
            ), shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
        ),
    ) {
        Column(modifier= Modifier
            .fillMaxHeight()
            .size(150.dp)){
            Text(text ="What service do you need today?",
                color = Color.White,
                fontSize = textSize,
                fontWeight = FontWeight(500),
                modifier = Modifier.padding(start = 20.dp, top = 20.dp)
            )
            Button(onClick = {},
                modifier = Modifier.padding(start = 20.dp, top = 20.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF0B2103)),) {
                Text(text = "Explore Now")
            }


        }
        Image(painter = painterResource(R.drawable.workers),
            contentDescription = "Workers Images",
            modifier= Modifier
                .size(imageSize.first, imageSize.second)
                .padding(top = 20.dp)
        )


    }
}
@Composable
fun CategoryItem(category: Categories,onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(120.dp, 100.dp)

            .clickable { onClick() }, //implementation here
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFECECEC)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            color = (Color(0xFFFFF2DD)),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(category.imageResId),
                        contentDescription = category.name,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Text(
                    text = category.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }

}

@Composable
fun TradesmanItem(trade: Tradesman, navController: NavController, cardHeight: Dp, textSize: TextUnit) {
    val windowSize = rememberWindowSizeClass()
    val iconSize = when (windowSize.width) {
        WindowType.SMALL -> 30.dp
        WindowType.MEDIUM -> 40.dp
        WindowType.LARGE -> 50.dp
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .clickable { navController.navigate("booknow")}, //implementation here
        shape = RoundedCornerShape(8.dp),


        ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(trade.imageResId),
                    contentDescription = "Tradesman Image",
                    modifier = Modifier
                        .size(cardHeight - 20.dp)
                        .padding(start = 10.dp)
                )
                Column(
                    modifier = Modifier
                        .size(250.dp, 100.dp)
                        .padding(start = 10.dp)
                )
                {
                    Text(
                        text = trade.username,
                        color = Color.Black,
                        fontWeight = FontWeight(500),
                        fontSize = textSize,
                        modifier = Modifier.padding(top = 10.dp)

                    )
                    Text(
                        text = trade.category,
                        color = Color.Black,
                        fontSize = textSize,
                        )
                    Row(modifier = Modifier.size(185.dp, 110.dp)) {
                        Box(
                            modifier = Modifier
                                .size(70.dp, 50.dp)
                                .padding(top = 10.dp)
                                .background(
                                    color = (Color(0xFFFFF2DD)),
                                    shape =RoundedCornerShape(12.dp)
                                )
                        ) {
                            Text(
                                text = trade.rate,
                                fontSize = textSize,
                                modifier = Modifier.padding(top = 5.dp, start = 8.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(70.dp, 50.dp)
                                .padding(top = 10.dp, start = 10.dp)
                                .background(
                                    color = (Color(0xFFFFF2DD)),
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star, contentDescription = "Start Icon",
                                tint = Color(0xFFFFA500), modifier = Modifier
                                    .size(25.dp)
                                    .padding(top = 7.dp, start = 2.dp)
                            )
                            Text(
                                text = trade.reviews.toString(),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 5.dp, start = 28.dp)
                            )
                        }
                    }


                }
                Image(painter = painterResource(trade.bookmark),
                    contentDescription = "Bookmark Image",
                    modifier = Modifier
                        .size(iconSize)
                        .padding(end = 5.dp)
                        .clickable { }
                )
            }


        }


    }



}
