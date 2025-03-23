package com.example.androidproject.view.client

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.androidproject.view.WindowType
import com.example.androidproject.view.rememberWindowSizeClass
import com.example.androidproject.view.theme.myGradient3
import com.example.androidproject.viewmodel.bookings.GetTradesmanBookingViewModel

@Preview
@Composable
fun ApplicantPendingDetails( modifier: Modifier = Modifier) {
    val windowSize = rememberWindowSizeClass()
    val nameTextSize = when (windowSize.width) {
        WindowType.SMALL -> 16.sp
        WindowType.MEDIUM -> 18.sp
        WindowType.LARGE -> 20.sp
    }
    val taskTextSize = when (windowSize.width) {
        WindowType.SMALL -> 14.sp
        WindowType.MEDIUM -> 16.sp
        WindowType.LARGE -> 18.sp
    }
    val smallTextSize = when (windowSize.width) {
        WindowType.SMALL -> 12.sp
        WindowType.MEDIUM -> 14.sp
        WindowType.LARGE -> 16.sp
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)
            )
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            shape = RoundedCornerShape(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
            ) {
                Row(modifier = Modifier
                    .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable {},
                        tint = Color(0xFF81D796)
                    )
                    Text(
                        text = "Applicant Details",
                        fontSize = 24.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.weight(1f)
                            .padding(start = 8.dp)
                    )
                }
            }
        }


        Column(modifier = Modifier.weight(1f)){

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(brush = myGradient3)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Your approval is pending: Approve or Decline",
                            fontSize = nameTextSize,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(220.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    shape = RoundedCornerShape(0.dp, 0.dp, 15.dp, 15.dp) // Keep card shape
                ) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 18.dp, horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Tradesman's Basic Information",
                            fontSize = nameTextSize,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                        )
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            thickness = 0.5.dp,
                            color = Color.Gray
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model ="" ,
                                contentDescription = "Client Image",
                                modifier = Modifier
                                    .size(100.dp)
                            )
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 10.dp)
                            ) {
                                Row{
                                    Text(
                                        text = "Full Name:",
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = taskTextSize,
                                    )
                                    Text(
                                        text = "Ezekiel Vidal",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = taskTextSize,
                                    )
                                }
                                Row{
                                    Text(
                                        text = "Birthday:",
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = taskTextSize,
                                    )
                                    Text(
                                        text = "July 4, 1980",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = taskTextSize,
                                    )
                                }
                                Row{
                                    Text(
                                        text = "Age:",
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = taskTextSize,
                                    )
                                    Text(
                                        text = "49 years old",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = taskTextSize,
                                    )
                                }
                                Row{
                                    Text(
                                        text = "Address:",
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = taskTextSize,
                                    )
                                    Text(
                                        text = "096 National Road, Pangapisan North, Lingayen, Pangasinan",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = taskTextSize,
                                    )
                                }
                                Row{
                                    Text(
                                        text = "Credential:",
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = taskTextSize,
                                    )
                                    Text(
                                        text = "View",
                                        textDecoration = TextDecoration.Underline,
                                        color = Color.Blue,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = taskTextSize,
                                    )
                                }
                            }


                            // Tradesman details

                        }

                    }

                }
                Spacer(Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(Color.White),
                        shape = RoundedCornerShape(15.dp) // Keep card shape
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(

                                text = "Reason to Hire this Tradesman",
                                fontSize = nameTextSize,
                                color = Color.Black,
                                fontWeight = FontWeight.Medium
                            )
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                thickness = 0.5.dp,
                                color = Color.Gray
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF5F5F5))
                                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                            ) {
                                Text(modifier = Modifier.padding(16.dp),text = "Reason to hire tradesman", fontSize = nameTextSize, color = Color.Black)

                            }

                        }

                    }
                }
                Spacer(Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(Color.White),
                        shape = RoundedCornerShape(15.dp) // Keep card shape
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(

                                text = "Contact Information",
                                fontSize = nameTextSize,
                                color = Color.Black,
                                fontWeight = FontWeight.Medium
                            )
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                thickness = 0.5.dp,
                                color = Color.Gray
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        tint = Color.Blue,
                                        contentDescription = "Phone Number",
                                        modifier = Modifier
                                            .size(24.dp)
                                    )
                                    Text(
                                        text = "Phone Number:",
                                        fontSize = nameTextSize,
                                        color = Color.Black,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                    Text(
                                        text = "0912345678",
                                        fontSize = nameTextSize,
                                        color = Color.Black,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = "Email",
                                        tint = Color.Blue,
                                        modifier = Modifier
                                            .size(24.dp)
                                    )
                                    Text(
                                        text = "Email:",
                                        fontSize = nameTextSize,
                                        color = Color.Black,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                    Text(
                                        text = "vidalezeqwer@gmail.com",
                                        fontSize = nameTextSize,
                                        color = Color.Black,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }
                            }

                        }

                    }
                }
                Spacer(Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(Color.White),
                        shape = RoundedCornerShape(15.dp) // Keep card shape
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(

                                text = "Other Information",
                                fontSize = nameTextSize,
                                color = Color.Black,
                                fontWeight = FontWeight.Medium
                            )
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                thickness = 0.5.dp,
                                color = Color.Gray
                            )
                            Row (modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){

                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row {
                                        Icon(
                                            imageVector = Icons.Default.Money,
                                            tint = Color.Black,
                                            contentDescription = "Estimated Rate:",
                                            modifier = Modifier
                                                .size(24.dp)
                                        )
                                        Column {
                                            Text(
                                                text = "Estimated Rate",
                                                fontSize = nameTextSize,
                                                color = Color.Black,
                                                modifier = Modifier.padding(start = 10.dp)
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .padding(start = 10.dp)
                                                    .size(70.dp, 30.dp)
                                                    .background(
                                                        color = (Color(0xFFF5F5F5)),
                                                        shape = RoundedCornerShape(12.dp)
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "P100/hr",
                                                    fontSize = smallTextSize
                                                )
                                            }
                                        }
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row {
                                        Icon(
                                            imageVector = Icons.Default.RateReview,
                                            tint = Color.Black,
                                            contentDescription = "Rate Review",
                                            modifier = Modifier
                                                .size(24.dp)
                                        )
                                        Column {
                                            Text(
                                                text = "Ratings",
                                                fontSize = nameTextSize,
                                                color = Color.Black,
                                                modifier = Modifier.padding(start = 10.dp)
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .padding(start = 10.dp)
                                                    .size(50.dp, 30.dp)
                                                    .background(
                                                        color = (Color(0xFFF5F5F5)),
                                                        shape = RoundedCornerShape(12.dp)
                                                    ),
                                                contentAlignment = Alignment.Center

                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Star,
                                                        contentDescription = "Start Icon",
                                                        tint = Color(0xFFFFA500),
                                                        modifier = Modifier
                                                            .size(16.dp)
                                                    )
                                                    Text(
                                                        text = "0",
                                                        fontSize = smallTextSize,
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
                Spacer(Modifier.height(10.dp))

                // Third Column with Card and content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(Color.White),
                        shape = RoundedCornerShape(15.dp) // Keep card shape
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(

                                text = "Support Center",
                                fontSize = nameTextSize,
                                color = Color.Black,
                                fontWeight = FontWeight.Medium
                            )
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                thickness = 0.5.dp,
                                color = Color.Gray
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Message,
                                        contentDescription = "Message Icon",
                                        modifier = Modifier
                                            .size(24.dp)
                                    )
                                    Text(
                                        text = "Contact Tradesman",
                                        fontSize = nameTextSize,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "Arrow Right Icon",
                                    modifier = Modifier
                                        .size(32.dp)
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Help,
                                        contentDescription = "Help Icon",
                                        modifier = Modifier
                                            .size(24.dp)
                                    )
                                    Text(
                                        text = "Help",
                                        fontSize = nameTextSize,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "Arrow Right Icon",
                                    modifier = Modifier
                                        .size(32.dp)
                                )
                            }


                        }

                    }
                }
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                        }
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "View Job Post", fontSize = nameTextSize)
                }

            }
            
        }
    }

}

