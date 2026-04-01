package com.nikhil.flowcus.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class, FocusSession::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
    abstract val focusSessionDao: FocusSessionDao

    companion object {
        const val DATABASE_NAME = "flowcus_db"
    }
}
