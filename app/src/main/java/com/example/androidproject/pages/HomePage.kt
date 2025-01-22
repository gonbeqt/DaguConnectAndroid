package com.example.androidproject.pages

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidproject.R
import com.example.androidproject.ui.theme.views.Categories
import com.example.androidproject.ui.theme.views.Tradesman

@Preview
@Composable

fun HomeScreen(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }
    val categories = listOf(
        Categories(R.drawable.plumbing, "Plumber"),
        Categories(R.drawable.electrical, "Electrical"),
        Categories(R.drawable.cleaning, "Cleaning"),
        Categories(R.drawable.carpentry, "Carpentry")
    )
    val tradesman = listOf(
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark)
    )


    Box (
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
        ){
            Column(modifier=Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding( top = 40.dp, start =25.dp, end = 25.dp),
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
                            modifier = Modifier.padding(start = 60.dp)
                                .size(32.dp)
                                .clickable {  },//Implementation here
                            tint = (Color.Gray)

                        )
                        Icon( imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Account Image",
                            modifier = Modifier.padding(start = 15.dp)
                                .size(32.dp)
                                .clickable {  },//Implementation here
                            tint = (Color.Gray)
                        )
                    }

                }
                Spacer(modifier = Modifier.height(30.dp))

                Row(modifier = Modifier.fillMaxWidth()
                    .padding(start = 25.dp, end = 25.dp)
                    .size(50.dp,)
                    .background(Color(0xFFFFFFFF), shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFBEBEBE), shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)),

                    verticalAlignment = Alignment.CenterVertically

                ){
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(32.dp)
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
                Spacer(modifier = Modifier.height(30.dp))
                Row(modifier =Modifier.fillMaxWidth()
                    .size(180.dp)
                    .padding(start = 25.dp, end = 25.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF81D796), Color(0xFF39BFB1)),
                            start = androidx.compose.ui.geometry.Offset(0f, 1f),
                            end = androidx.compose.ui.geometry.Offset(1f, 1f)
                        ), shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)),
                ) {
                    Column(modifier=Modifier.fillMaxHeight().size(150.dp)){
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
                        modifier=Modifier.size(250.dp,250.dp).padding(top = 20.dp)
                       )


                }
                Spacer(modifier =Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 25.dp,end = 25.dp),
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ) {
                    Text(text = "Categories",
                        fontSize = 18.sp,
                        fontWeight = FontWeight(500),
                        modifier=Modifier.padding(top = 10.dp))
                    TextButton(onClick = {}) {
                        Text(text = "View All",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            modifier=Modifier.padding(bottom = 10.dp))
                    }
                }

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(120.dp).background(Color.White),
                    contentPadding = PaddingValues(12.dp),
                ) {
                    items(categories.size) { index ->
                        val category = categories[index]
                        CategoryItem(category)
                    }
                }
                Spacer(modifier =Modifier.height(5.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 25.dp,end = 25.dp),
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ) {
                    Text(text = "Top-Rated",
                        fontSize = 18.sp,
                        fontWeight = FontWeight(500),
                        modifier=Modifier.padding(top = 10.dp))
                    TextButton(onClick = {}) {
                        Text(text = "See All",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            modifier=Modifier.padding(bottom = 10.dp))
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .size(411.dp).padding(start = 5.dp)
                    ,

                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(tradesman.size) { index ->
                        val trade = tradesman[index]
                        TradesmanItem(trade)
                    }
                }
            }


    }




}

@Composable
fun CategoryItem(category: Categories) {
    Card(
        modifier = Modifier
            .size(120.dp, 100.dp)
            .clickable {  }, //implementation here
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

@Composable
fun TradesmanItem(trade: Tradesman) {
    Card(
        modifier = Modifier
            .size(390.dp, 120.dp)
            .clickable { }, //implementation here
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),


    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD9D9D9)),
            contentAlignment = Alignment.CenterStart
        ){
            Row (modifier = Modifier.fillMaxWidth()){
                Image(painter = painterResource(trade.imageResId),
                    contentDescription = "Tradesman Image",
                    modifier = Modifier.size(100.dp,100.dp).padding(start = 10.dp))
                Column(modifier = Modifier.size(250.dp,100.dp)
                    .padding(start=10.dp)
                    )
                {
                   Text(text = trade.username,
                       color = Color.Black,
                       fontWeight = FontWeight(500),
                       fontSize = 20.sp,
                       modifier = Modifier.padding(top = 10.dp)

                   )
                    Text(text = trade.category,
                        color = Color.Black,
                        fontSize = 16.sp,
                    )
                    Row (modifier = Modifier.size(185.dp,110.dp)){
                        Box (modifier = Modifier.size(70.dp,50.dp)
                            .padding(top = 10.dp)
                            .background(
                                color = (Color(0xFFFFF2DD)),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                            )
                        ){
                            Text(text = trade.rate,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 5.dp, start = 8.dp))
                        }
                        Box (modifier = Modifier.size(70.dp,50.dp)
                            .padding(top = 10.dp, start = 10.dp)
                            .background(
                                color = (Color(0xFFFFF2DD)),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                            )
                        ){
                            Icon(imageVector = Icons.Default.Star, contentDescription = "Start Icon",
                                tint = Color(0xFFFFA500),modifier = Modifier.size(25.dp).padding(top =7.dp, start = 2.dp))
                            Text(text = trade.reviews.toString(),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 5.dp, start = 28.dp))
                        }
                    }



                }
                Image(painter = painterResource(trade.bookmark),
                    contentDescription = "Bookmark Image",
                    modifier = Modifier.size(50.dp)
                        .padding(end = 5.dp)
                        .clickable {  }
                )
            }




        }


    }
}
