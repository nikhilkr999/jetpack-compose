package com.nikhil.postapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PostEntity :: class], version = 1, exportSchema = false)
abstract class PostsDatabase : RoomDatabase() {
    companion object{
        @Volatile
        private var INSTANCE : PostsDatabase ?= null

        fun getInstance(context : Context) : PostsDatabase{
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context,
                        PostsDatabase :: class.java,
                        "posts_database"
                    ).build()
                }
                INSTANCE = instance
                return instance
            }

        }
    }
}