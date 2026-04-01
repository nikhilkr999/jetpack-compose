package com.nikhil.todo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract val taskDao : TaskDao

    // Singleton Design Pattern
    // Only one instance of the database exists, avoiding
    // unnecessary overhead associated with repeated DB Creation

    // companion object: define a static singleton instance of this DB class
    // @Volatile: prevents any possible race conditions in Multithreading

    companion object{
        @Volatile
        private var INSTANCE : TaskDatabase? = null

        fun getInstance(context: Context): TaskDatabase{
            // ensuring that only one thread can execute the block
            // of code inside the synchronized block at any given time

            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    // Creating the Database Object
                    instance = Room.databaseBuilder(
                        context = context.applicationContext,
                        TaskDatabase::class.java,
                        "task_database"
                    ).build()
                }
                INSTANCE = instance
                return instance
            }
        }
    }
}
