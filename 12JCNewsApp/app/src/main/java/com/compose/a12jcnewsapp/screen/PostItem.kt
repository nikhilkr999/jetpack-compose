package com.compose.a12jcnewsapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.compose.a12jcnewsapp.retrofit.Post
import com.compose.a12jcnewsapp.util.GenerateRandomColors

@Composable
fun PostItem(post: Post){
    // Generate Random Color for the card back
    val backgroundColor = GenerateRandomColors().getRandomColor()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = Color.Black
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = post.title, style = MaterialTheme.typography.labelLarge)
            Text(text = post.body, style = MaterialTheme.typography.bodySmall)
        }
    }

}