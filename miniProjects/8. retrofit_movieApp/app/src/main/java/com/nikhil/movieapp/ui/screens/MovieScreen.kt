package com.nikhil.movieapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nikhil.movieapp.util.UiState
import com.nikhil.movieapp.viewmodel.MovieViewModel

@Composable
fun MovieScreen(
    viewModel: MovieViewModel, // Automatically provides the VM
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is UiState.Success -> {
                val movies = (uiState as UiState.Success).data
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(movies) { movie ->
                        MovieItem(movie)
                    }
                }
            }

            is UiState.Error -> {
                Text(
                    text = (uiState as UiState.Error).message,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}