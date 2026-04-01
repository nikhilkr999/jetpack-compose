package com.nikhil.movieapp.data.api

import com.nikhil.movieapp.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") api_key : String,
        @Query("page") page : Int
    ) : MovieResponse
}