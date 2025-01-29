package com.example.androidproject.ui.theme.views.pages2

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.androidproject.R
import com.example.androidproject.ui.theme.views.Tradesman

@Composable

fun BookmarkedTradesman(modifier: Modifier = Modifier, navController: NavController) {
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
            BookmarkTradesmanTopSection(navController)

            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .size(420.dp)
                    .background(Color(0xFFD9D9D9))
                ,
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(tradesman.size) { index ->
                    val trade = tradesman[index]
                    BookmarkTradesmanItem(trade)
                }
            }
        }

    }
}
@Composable
fun BookmarkTradesmanTopSection(navController: NavController){
    Row(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
            .height(70.dp)

        ,
        horizontalArrangement = Arrangement.spacedBy(140.dp),
    ) {
        Text(text="Bookmarks ",
            fontSize = 24.sp,
            fontWeight =
            FontWeight(500),
            modifier = Modifier.padding(16.dp),



            )
        Row (modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)){
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications Icon",
                tint = Color(0xFF3CC0B0),
                modifier = Modifier
                    .size(32.dp)

            )
            Icon(
                imageVector = Icons.Default.Message,
                contentDescription = "Message Icon",
                tint = Color(0xFF3CC0B0),
                modifier = Modifier
                    .size(32.dp)

                    .clickable {
                        navController.navigate("message_screen")

                    }
            )
        }

    }
}




//Design For Items
@Composable
fun BookmarkTradesmanItem(trade: Tradesman) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                },
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.pfp),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Text(text = trade.username,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight(500),
                        modifier = Modifier.padding(start = 20.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Column {
                        Text(text = trade.category, fontSize = 24.sp, color = Color.Black, fontWeight = FontWeight(500))
                        Text(text = "Posted on 1 min ago - Active ")

                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Image(painter = painterResource(R.drawable.bookmark),
                        contentDescription = "Bookmark",
                        Modifier.size(32.dp)
                            .clickable {  }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, Color.Gray),
                    colors = CardDefaults.cardColors(Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Description of the Plumbing Repair service", fontSize = 12.sp)
                        Text(text = "Est. Budget: P200/hr", fontSize = 12.sp)
                        Text(text = "Location: Lingayen, Pangasinan", fontSize = 16.sp)


                    }

                }


            }
        }
    }