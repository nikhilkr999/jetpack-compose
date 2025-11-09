package com.compose.a11jcnotey.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class NotesDB : RoomDatabase(){

    abstract val notesDao: NoteDao

    // Singleton Design Pattern
    // Only one instance of the database exists, avoiding
    // unnecessary overhead associated with repeated DB Creation

    // companion object: define a static singleton instance of this DB class
    // @Volatile: prevents any possible race conditions in Multithreading

    companion object{
        @Volatile
        private var INSTANCE : NotesDB ?= null

        fun getInstance(context: Context): NotesDB{
            // ensuring that only one thread can execute the block
            // of code inside the synchronized block at any given time
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    // Creating the Database Object
                    instance = Room.databaseBuilder(
                        context = context.applicationContext,
                        NotesDB::class.java,
                        "notes_db"
                    ).build()

                }
                INSTANCE = instance
                return instance
            }


        }
    }




}