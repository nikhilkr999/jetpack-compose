package com.nikhil.movieapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.movieapp.data.repository.MovieRepository
import com.nikhil.movieapp.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        getPopularMovies(1)
    }

    fun getPopularMovies(page: Int) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getPopularMovie(page)
                _uiState.value = UiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An unknown error occurred")
                e.printStackTrace()
            }
        }
    }
}