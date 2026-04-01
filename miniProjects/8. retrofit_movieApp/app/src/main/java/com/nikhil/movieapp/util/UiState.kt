package com.nikhil.movieapp.util

import com.nikhil.movieapp.data.model.Movie

sealed class UiState {
    object Loading : UiState()
    data class Success (val data : List<Movie>) : UiState()
    data class Error(val message : String) : UiState()
}