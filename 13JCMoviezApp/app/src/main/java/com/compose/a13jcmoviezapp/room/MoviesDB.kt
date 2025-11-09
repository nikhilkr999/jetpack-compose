package com.compose.a13jcmoviezapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.compose.a13jcmoviezapp.retrofit.Movie

@Database(entities = [Movie::class], version = 1)
abstract class MoviesDB: RoomDatabase() {

    abstract val moviesDao : MovieDao

    // Singleton Design pattern
    // Only one instance of the DB exists, avoiding
    // unnecessary multiple instances associated with
    // repeated DB creation

    // companion object: define a static singleton
    // instance of this DB class
    companion object{

        @Volatile
        private var INSTANCE: MoviesDB ?= null

        fun getInstance(context: Context): MoviesDB{

            synchronized(this){

                var instance = INSTANCE
                if (instance == null) {

                    // Creating the DB Object
                    instance = Room.databaseBuilder(
                        context= context.applicationContext,
                        MoviesDB::class.java,
                        "movies_db"
                    ).build()

                }

                INSTANCE = instance

                return instance

            }

        }


    }
}