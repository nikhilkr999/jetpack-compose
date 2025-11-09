package com.compose.a13jcmoviezapp.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.compose.a13jcmoviezapp.retrofit.Movie

@Composable
fun MovieList(movies : List<Movie>){
    LazyColumn {
        items(movies){
            movie -> MovieItem(movie)
        }
    }
}