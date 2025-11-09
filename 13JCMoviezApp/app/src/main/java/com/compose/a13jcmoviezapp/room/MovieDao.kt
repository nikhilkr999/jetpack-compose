package com.compose.a13jcmoviezapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.compose.a13jcmoviezapp.retrofit.Movie

@Dao
interface MovieDao {
    @Insert
    suspend fun insert(movie: Movie)

    @Insert
    suspend fun insertMovieList(movies:List<Movie>)

    @Query("SELECT * FROM movies_table")
    suspend fun getAllMoviesInDb(): List<Movie>
}