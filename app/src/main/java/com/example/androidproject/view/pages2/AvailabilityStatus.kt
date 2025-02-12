package com.example.androidproject.view.pages2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidproject.R

@Preview
@Composable
fun AvailabilityStatusScreen (modifier: Modifier = Modifier){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 8.dp)
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 16.dp),
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 16.dp),
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.Black
            )
            Text(
                text = "Help",
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )
        }
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(R.drawable.availabilitystatus),
                contentDescription = "Workers Images",
                modifier = Modifier
                    .height(180.dp)
                    .width(240.dp)
            )
        }
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.Center) {
            Text(
                text = "Let clients know you're currently ready and available for service.",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

        }
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp , vertical = 16.dp)) {
            Text(
                text = "How does this work?",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Green
            )

        }
        Column{
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp ), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Verified, contentDescription = "Close", tint = Color.Black)
                Text(modifier = Modifier.padding(start = 10.dp),
                    text = "Set your Availability",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            Text(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                text = "Turn on your status to make it \"Available\" to let clients know you're ready to offer your services.",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column{
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp ), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Verified, contentDescription = "Close", tint = Color.Black)
                Text(modifier = Modifier.padding(start = 10.dp),
                    text = "Client's View",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            Text(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                text = "Clients will be able to see your active status and can reach out for assistance.",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column{
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp ), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Verified, contentDescription = "Close", tint = Color.Black)
                Text(modifier = Modifier.padding(start = 10.dp),
                    text = "Work on your terms",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            Text(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                text = "You can easily turn off your status to \"Unavailable\" whenever you need a break or are not taking new requests.",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Black
            )
        }

    }

}