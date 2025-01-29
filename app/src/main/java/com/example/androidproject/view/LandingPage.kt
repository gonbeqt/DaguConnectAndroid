package com.example.androidproject.view

import android.app.Activity
import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.androidproject.R


@Preview(showBackground = true)
@Composable
fun LandingPageScreenPreview() {
    // Use a mock or placeholder NavController for preview purposes
    LandingPageScreen(navController = rememberNavController())
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LandingPageScreen(navController: NavController){
    val windowSize = rememberWindowSizeClass()

    var mainText by remember { mutableStateOf("Find & Offer Local Services with Ease") }
    var messageText by remember { mutableStateOf("Connect with local residents in Dagupan City, to find or offer services, showcase your skills transact easily,and enjoy a secure platform"
    ) }
    var currentImage by remember { mutableStateOf(R.drawable.landing1,
    ) }

    var clickCount by remember { mutableStateOf(0) }
    var buttonText by remember { mutableStateOf("Next") }

    val header = listOf(
        "Find & Offer Local Services with Ease",
        "Connect with trusted local professionals",
        "Easily manage services and transactions"
    )

    val message = listOf(
        "Connect with local residents in Dagupan City, to find or offer services, showcase your skills transact easily,and enjoy a secure platform",
        "Work with trusted professionals in Dagupan City to fulfill your needs while maintaining trust and quality.",
        "Effortlessly manage all your transactions and services through a user-friendly and reliable platform."
    )
    val images = listOf(
        R.drawable.landing1,
        R.drawable.landing2,
        R.drawable.landing3
    )
    val context = LocalContext.current
    val activity = (LocalContext.current as? Activity)

    LaunchedEffect(Unit) {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isShown", true).apply()
    }
    Box(modifier = Modifier.fillMaxSize().background(Color.White),
    ){
        Image(
            painter = painterResource(id = R.drawable.background_image_auth), // Corrected name
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Row(
                modifier = Modifier.fillMaxWidth().padding( top = 25.dp),
                horizontalArrangement = Arrangement.spacedBy(290.dp)
            ) {
                IconButton(onClick = {activity?.finish()} ){
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                }
                TextButton(onClick = {navController.navigate("login")}) {
                    Text(text = "Skip", color = Color.Gray)
                }


            }
        Column(modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {


            AnimatedContent(
                targetState = currentImage,
                transitionSpec = {
                    slideInHorizontally(initialOffsetX = { -it }) with slideOutHorizontally(targetOffsetX = { it })
                }
            ) {targetImage ->
                val imageSize = when (windowSize.width) {
                    WindowType.SMALL -> 250.dp
                    WindowType.MEDIUM -> 350.dp
                    WindowType.LARGE -> 400.dp
                }

                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp).size(imageSize)) {
                    Image(
                        painter = painterResource(id = targetImage),
                        contentDescription = "LandingPage",
                        modifier = Modifier.fillMaxSize().size(imageSize),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }




            AnimatedContent(
                targetState = mainText,
                transitionSpec = {
                    slideInHorizontally(initialOffsetX = { -it }) with slideOutHorizontally(targetOffsetX = { it })
                }
            ) { targetText ->
                val fontSize = when (windowSize.width) {
                    WindowType.SMALL -> 20.sp
                    WindowType.MEDIUM -> 24.sp
                    WindowType.LARGE -> 28.sp
                }

                Text(
                    text = targetText,
                    fontWeight = FontWeight(700),
                    color = Color.Black,
                    fontSize = fontSize,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp, start = 15.dp, end = 15.dp)
                )

            }

            AnimatedContent(
                targetState = messageText,
                transitionSpec = {
                    slideInHorizontally(initialOffsetX = { -it }) with slideOutHorizontally(targetOffsetX = { it })
                }
            ) { targetMessage ->
                val fontSize = when (windowSize.width) {
                    WindowType.SMALL -> 16.sp
                    WindowType.MEDIUM -> 20.sp
                    WindowType.LARGE -> 22.sp
                }

                Text(
                    text = targetMessage,
                    color = Color.Gray,
                    fontSize = fontSize,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 30.dp, start = 15.dp, end = 15.dp)
                )
            }
            Spacer(modifier = Modifier.height(50.dp))

            Button( onClick = {
                if (clickCount < 1 ){
                    clickCount ++
                    mainText = header[clickCount]
                    messageText = message[clickCount]
                    currentImage = images[clickCount]


                }

                else{
                    buttonText = "Get Started"
                    mainText = header[2]
                    messageText = message[2]
                    currentImage = images[2]
                    clickCount ++

                }
                if (clickCount == 3) {
                    navController.navigate("signup")
                }


            },
                modifier = Modifier.width(200.dp).height(50.dp),    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B7)) // Sets button background color


            ) {
                Text(text = buttonText, fontSize = 16.sp)

            }

        }



    }

}



