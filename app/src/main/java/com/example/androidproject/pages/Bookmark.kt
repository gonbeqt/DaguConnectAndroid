package com.example.androidproject.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidproject.R
import com.example.androidproject.ui.theme.views.Tradesman

@Composable

fun BookmarkedScreen(modifier: Modifier = Modifier) {
    val tradesman = listOf(
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark) ,
        Tradesman(R.drawable.pfp,"Ezekiel", "Plumber", "P500/hr", 4.5, R.drawable.bookmark),
    )
    Box (
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp, start = 25.dp, end = 25.dp),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                //Should be Logo
                Image(
                    painter = painterResource(id = R.drawable.visibility_on),
                    contentDescription = "LOGO",
                    contentScale = ContentScale.Crop
                )
                Row() {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location Image",
                        modifier = Modifier.size(32.dp),
                        tint = (Color.Gray)
                    )

                    Text(
                        text = "Location",
                        color = Color.Black,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 5.dp, top = 2.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications Image",
                        modifier = Modifier.padding(start = 60.dp)
                            .size(32.dp)
                            .clickable { },//Implementation here
                        tint = (Color.Gray)

                    )
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Account Image",
                        modifier = Modifier.padding(start = 15.dp)
                            .size(32.dp)
                            .clickable { },//Implementation here
                        tint = (Color.Gray)
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .size(420.dp)
                ,
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(tradesman.size) { index ->
                    val trade = tradesman[index]
                    BookmarkItem(trade)
                }
            }
        }

    }
}




//Design For Items
@Composable
fun BookmarkItem(trade: Tradesman) {
    Card(
        modifier = Modifier
            .size(390.dp, 120.dp)
            .clickable { }, // Add implementation for click if needed
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD9D9D9)),
            contentAlignment = Alignment.CenterStart
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
                                    color = (Color(0xFFFFF2DD)),
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = trade.rate,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }

                        // Reviews Box
                        Box(
                            modifier = Modifier
                                .background(
                                    color = (Color(0xFFFFF2DD)),
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
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
                }
                    Image(painter = painterResource(trade.bookmark),
                        contentDescription = "Bookmark Image",
                        modifier = Modifier.size(40.dp,40.dp).padding(bottom = 15.dp)
                            .clickable {  })

            }
        }
    }
}
