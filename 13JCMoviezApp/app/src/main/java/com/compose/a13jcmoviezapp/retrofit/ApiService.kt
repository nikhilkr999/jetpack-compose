package com.compose.a13jcmoviezapp.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // Define the End Points
    // Retrofit uses annotations to describe the HTTP
    // Request methods and Parameters

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key")
        apiKey: String
    ):MovieResponse

}