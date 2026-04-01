package com.nikhil.movieapp.data.repository

import com.nikhil.movieapp.data.api.RetrofitInstance
import com.nikhil.movieapp.data.model.Movie

class MovieRepository {
    private val API_KEY = "008e1953288295b05e66ee8cc72aaabf"

    suspend fun getPopularMovie(page : Int): List<Movie>{
        return RetrofitInstance.api.getPopularMovies(API_KEY, page).results
    }

}