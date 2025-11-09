package com.compose.a13jcmoviezapp.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.a13jcmoviezapp.repository.Repository
import com.compose.a13jcmoviezapp.retrofit.Movie
import kotlinx.coroutines.launch


// ViewModel: Stores & Manages UI-related Data
//            Surviving configuration changes
class MovieViewModel(repository: Repository): ViewModel() {

    // ViewModel uses MutableStateOf<> instead of LiveData
    // Allowing direct Compose state handling

    // when the value of 'movies' changes, Compose will
    // automatically recompose the parts of the UI that depend
    // on this state
    var movies by mutableStateOf<List<Movie>>(emptyList())
        private set // only 'MovieViewModel' can change 'movies'

    //online movies
    var moviesFromApi by mutableStateOf<List<Movie>>(emptyList())

    // The Offline Movies
    var moviesFromRoomDB by mutableStateOf<List<Movie>>(emptyList())
        private set  // only 'MovieViewModel' can change 'moviesFromRoomDB'


    // View Model Scope: launch a coroutine in the scope of ViewModel,
    // which means that coroutine will be tied to the lifecycle of
    // the viewmodel
    init {
        viewModelScope.launch {

            try {
                moviesFromApi =
                    repository.getPopularMoviesFromOnlineApi(api_key = "008e1953288295b05e66ee8cc72aaabf")

                // Insert Movies into ROOM DB
                repository.insertMoviesIntoDB(moviesFromApi)

                // Assigning 'movies' to MoviesFromApi
                movies = moviesFromApi

            } catch (e: Exception) {
                // Fetch The data from ROOM DB
                moviesFromRoomDB= repository.getMoviesFromDB()

                movies = moviesFromRoomDB

            }
        }
    }

}