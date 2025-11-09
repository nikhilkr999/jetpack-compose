package com.compose.a13jcmoviezapp.screen

import androidx.compose.runtime.Composable
import com.compose.a13jcmoviezapp.viewModel.MovieViewModel

@Composable
fun MovieScreen(viewModel: MovieViewModel){
    val moviesList = viewModel.moviesFromApi
    MovieList(moviesList)
}