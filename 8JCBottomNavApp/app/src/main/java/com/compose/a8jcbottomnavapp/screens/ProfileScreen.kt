package com.compose.a8jcbottomnavapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen(
    id: Int,
    showDetails: Boolean,
    navigateToSettings: ()->Unit



){

    Column(modifier = Modifier.fillMaxWidth().padding(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(text = "Profile ID: $id", fontSize = 40.sp)
        Text(text = "Show Details: $showDetails", fontSize = 40.sp)
        Button(onClick = navigateToSettings) {
            Text(text = "Go to Settings")
        }
    }
}