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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.androidproject.R
import com.example.androidproject.ui.theme.views.Categories
import com.example.androidproject.ui.theme.views.Tradesman


@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val selectedCategory = remember { mutableStateOf<String?>(null) }

    val categories = listOf(
        Categories(R.drawable.plumbing, "Plumber"),
        Categories(R.drawable.electrical, "Electrical"),
        Categories(R.drawable.cleaning, "Cleaning"),
        Categories(R.drawable.carpentry, "Carpentry")
    )

    val tradesmans = listOf(
        Tradesman(R.drawable.pfp, "Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp, "Alex", "Electrical", "P600/hr", 4.8, R.drawable.bookmark),
        Tradesman(R.drawable.pfp, "Liam", "Cleaning", "P450/hr", 4.2, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp, "Liam", "Carpentry", "P450/hr", 4.2, R.drawable.bookmark),
        Tradesman(R.drawable.pfp, "Liam", "Cleaning", "P450/hr", 4.2, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp, "Liam", "Carpentry", "P450/hr", 4.2, R.drawable.bookmark),
        Tradesman(R.drawable.pfp, "Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp, "Alex", "Electrical", "P600/hr", 4.8, R.drawable.bookmark),
        Tradesman(R.drawable.pfp, "Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp, "Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),



        )

    val tradesmen = if (selectedCategory.value != null) {
        tradesmans.filter { it.category == selectedCategory.value }
    } else {
        tradesmans
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HomeTopSection()

            Spacer(modifier = Modifier.height(30.dp))
            SearchField()
            Spacer(modifier = Modifier.height(5.dp))

            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),) {
                Spacer(modifier = Modifier.height(30.dp))
                ExploreNow()

                Spacer(modifier = Modifier.height(20.dp))
                CategoryRow(categories, selectedCategory)

                Spacer(modifier = Modifier.height(5.dp))
                TradesmanColumn(tradesmen)
            }


        }
    }
}

@Composable
fun CategoryRow(categories: List<Categories>, selectedCategory: MutableState<String?>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, end = 25.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Categories",
            fontSize = 18.sp,
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
            .height(120.dp)
            .background(Color.White),
        contentPadding = PaddingValues(12.dp),
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
fun TradesmanColumn(tradesmen: List<Tradesman>) {
    val showDialogAllTradesman = remember { mutableStateOf(false) }

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
        TextButton(onClick = { showDialogAllTradesman.value = true}) {
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
            .background(Color.White), // Optional padding for the card
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            tradesmen.forEach { trade ->
                TradesmanItem(trade)
            }
        }
    }
    if (showDialogAllTradesman.value) {
            Dialog(onDismissRequest = { showDialogAllTradesman.value = false }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
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
                                    .background(Color.White),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                tradesmen.forEach { trade ->
                                    TradesmanItem(trade)
                                }
                            }
                        }


                    }
                }
            }


    }


}


@Composable
fun HomeTopSection(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 25.dp, end = 25.dp),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween
    ){
        //Should be Logo
        Image( painter = painterResource(id = R.drawable.visibility_on),
            contentDescription = "LOGO",
            contentScale = ContentScale.Crop
        )
        Row (){
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Image",
                modifier = Modifier.size(32.dp),
                tint = (Color.Gray)
            )

            Text(text = "Location",
                color = Color.Black,fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp, top = 2.dp))
            Icon( imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications Image",
                modifier = Modifier
                    .padding(start = 60.dp)
                    .size(32.dp)
                    .clickable { },//Implementation here
                tint = (Color.Gray)

            )
            Icon( imageVector = Icons.Default.AccountCircle,
                contentDescription = "Account Image",
                modifier = Modifier
                    .padding(start = 15.dp)
                    .size(32.dp)
                    .clickable { },//Implementation here
                tint = (Color.Gray)
            )
        }

    }
}
@Composable
fun SearchField(){
    var searchQuery by remember { mutableStateOf("") }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 25.dp, end = 25.dp)
        .size(50.dp,)
        .background(
            Color(0xFFFFFFFF),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
        )
        .border(
            1.dp,
            Color(0xFFBEBEBE),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
        ),

        verticalAlignment = Alignment.CenterVertically

    ){
        Icon(
            imageVector = androidx.compose.material.icons.Icons.Default.Search,
            contentDescription = "Search Icon",
            tint = Color.Gray,
            modifier = Modifier
                .size(32.dp)
                .padding(start = 10.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        BasicTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.weight(1f),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, color = Color.Black),
            decorationBox = { innerTextField ->
                if (searchQuery.isEmpty()) {
                    Text(
                        text = "Search for services or tradespeople...",
                        style = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, color = Color.Gray)
                    )
                }
                innerTextField()
            }
        )
    }
}
@Composable
fun ExploreNow(){
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
                fontSize = 20.sp,
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
                .size(250.dp, 250.dp)
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
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradesmanItem(trade: Tradesman) {
    val showDialogTradesman = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .size(390.dp, 120.dp)
            .clickable { showDialogTradesman.value = true }, //implementation here
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),


        ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD9D9D9)),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(trade.imageResId),
                    contentDescription = "Tradesman Image",
                    modifier = Modifier
                        .size(100.dp, 100.dp)
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
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 10.dp)

                    )
                    Text(
                        text = trade.category,
                        color = Color.Black,
                        fontSize = 16.sp,
                    )
                    Row(modifier = Modifier.size(185.dp, 110.dp)) {
                        Box(
                            modifier = Modifier
                                .size(70.dp, 50.dp)
                                .padding(top = 10.dp)
                                .background(
                                    color = (Color(0xFFFFF2DD)),
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                                )
                        ) {
                            Text(
                                text = trade.rate,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 5.dp, start = 8.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(70.dp, 50.dp)
                                .padding(top = 10.dp, start = 10.dp)
                                .background(
                                    color = (Color(0xFFFFF2DD)),
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
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
                        .size(50.dp)
                        .padding(end = 5.dp)
                        .clickable { }
                )
            }


        }


    }

    if (showDialogTradesman.value) {
        var taskDescription by remember { mutableStateOf("") }
        Dialog(
            onDismissRequest = { showDialogTradesman.value = false }

        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent) // Transparent background
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize(),
                    shape = RoundedCornerShape(20.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,

                        ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Tradesman Details",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 10.dp)
                            )

                            Button(onClick = { showDialogTradesman.value = false }) {
                                Text("Close", fontSize = 12.sp)
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            , // Adjust card padding for spacing
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFD9D9D9)) // Background for the card
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Tradesman image
                                Image(
                                    painter = painterResource(trade.imageResId),
                                    contentDescription = "Tradesman Image",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(start = 10.dp)
                                )

                                // Tradesman details
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 10.dp)
                                ) {
                                    Text(
                                        text = trade.username,
                                        color = Color.Black,
                                        fontWeight = FontWeight(500),
                                        fontSize = 20.sp,
                                        modifier = Modifier.padding(top = 10.dp)
                                    )
                                    Text(
                                        text = trade.category,
                                        color = Color.Black,
                                        fontSize = 16.sp,
                                    )
                                    Row(
                                        modifier = Modifier.padding(top = 10.dp),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Rate Box
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = Color(0xFFFFF2DD),
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = trade.rate,
                                                fontSize = 14.sp,
                                                modifier = Modifier.padding(horizontal = 4.dp)
                                            )
                                        }
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = Color(0xFFFFF2DD),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "Star Icon",
                                            tint = Color(0xFFFFA500),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.size(4.dp))
                                        Text(
                                            text = trade.reviews.toString(),
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Column {
                                Text(
                                    text = "Task Description",
                                    color = Color.Black,
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFF5F5F5)) // Background color
                                ) {
                                    TextField(
                                        value = taskDescription,
                                        onValueChange = { newValue ->
                                            taskDescription = newValue
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        placeholder = { Text(text = "Enter task description") },
                                        maxLines = 3,
                                        colors = TextFieldDefaults.textFieldColors(
                                            focusedIndicatorColor = Color.Transparent, // Remove focus indicator line
                                            unfocusedIndicatorColor = Color.Transparent // Remove unfocused line
                                        )
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState())
                                        .padding(start = 5.dp, end = 5.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp) // Adds spacing between the boxes
                                ) {
                                    listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday").forEach { day ->
                                        Box(
                                            modifier = Modifier
                                                .size(80.dp, 30.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(Color(0xFFFFF2DD))
                                                .clickable { },
                                            contentAlignment = Alignment.Center // Centers the Text inside the Box
                                        ) {
                                            Text(
                                                text = day,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.align(Alignment.Center),
                                                fontSize = 14.sp// Ensures Text is centered
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}