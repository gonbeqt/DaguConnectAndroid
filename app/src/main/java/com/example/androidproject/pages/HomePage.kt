package com.example.androidproject.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidproject.R


@Composable

fun HomeScreen(modifier: Modifier = Modifier) {
        Box (
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
        ){
            Row(
                modifier = Modifier.fillMaxWidth().padding( top = 40.dp, start =25.dp, end = 25.dp),
                horizontalArrangement = Arrangement.spacedBy(90.dp)
            ){
                //Should be Logo
                Image( painter = painterResource(id = R.drawable.visibility_on),
                    contentDescription = "LOGO",
                    contentScale = ContentScale.Crop
                )
                Row (modifier=Modifier.fillMaxWidth()){
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location Image",
                        modifier = Modifier.size(26.dp),
                        tint = (Color.Gray)
                    )

                    Text(text = "Location", fontSize = 16.sp, modifier = Modifier.padding(start = 5.dp))
                    Icon( imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications Image",
                        modifier = Modifier.padding(start = 60.dp).size(26.dp),
                        tint = (Color.Gray)

                    )
                    Icon( imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Account Image",
                        modifier = Modifier.padding(start = 15.dp).size(26.dp),
                        tint = (Color.Gray)
                    )
                }

            }




    }

}

