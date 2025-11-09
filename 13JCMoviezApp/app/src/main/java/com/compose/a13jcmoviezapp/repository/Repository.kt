package com.compose.a13jcmoviezapp.repository

import android.content.Context
import com.compose.a13jcmoviezapp.retrofit.Movie
import com.compose.a13jcmoviezapp.retrofit.RetrofitInstance
import com.compose.a13jcmoviezapp.room.MovieDao
import com.compose.a13jcmoviezapp.room.MoviesDB

class Repository (context: Context){

    // Repository: Manages data operations & abstract the data sources.
    // Acts as a bridge between different data source(online DB, local DB..)
    // and the rest of the app.
    suspend fun getPopularMoviesFromOnlineApi(api_key: String):List<Movie>{
        return RetrofitInstance.api.getPopularMovies(api_key).results
    }

    // Fetching data from Offline ROOM Database
    private val db = MoviesDB.getInstance(context)
    private val movieDao : MovieDao = db.moviesDao


    suspend fun getMoviesFromDB(): List<Movie>{
        return movieDao.getAllMoviesInDb()
    }


    suspend fun insertMoviesIntoDB(movies:List<Movie>){
        return movieDao.insertMovieList(movies)
    }

    suspend fun insertMovieIntoDB(movie: Movie){
        return movieDao.insert(movie)
    }




}