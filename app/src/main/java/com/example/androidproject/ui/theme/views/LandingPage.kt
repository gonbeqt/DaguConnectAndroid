package com.example.androidproject.ui.theme.views

import android.widget.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LandingPageScreen(){

    Row(
        modifier = Modifier.fillMaxWidth().padding( top = 25.dp),
        horizontalArrangement = Arrangement.spacedBy(290.dp)
    ) {
        // Back Button with Icon
        IconButton(onClick = {} ){
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(24.dp)

            )
        }
        TextButton(onClick = {}) {
            Text(text = "Skip", color = Color.Gray)
        }


    }
}