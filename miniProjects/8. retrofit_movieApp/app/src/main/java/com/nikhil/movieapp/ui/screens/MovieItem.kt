package com.nikhil.movieapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nikhil.movieapp.data.model.Movie

@Composable
fun MovieItem(movie: Movie) {
    val imageUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"

    Row(modifier = Modifier.padding(8.dp)) {
        AsyncImage(
            model = imageUrl,
            contentDescription = movie.title,
            modifier = Modifier.size(100.dp, 150.dp)
        )

        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(text = movie.title)
            Text(text = movie.overview, maxLines = 3)
        }
    }
}