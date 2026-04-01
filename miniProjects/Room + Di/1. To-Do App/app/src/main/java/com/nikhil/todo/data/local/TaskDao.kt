package com.nikhil.todo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getTasks() : Flow<List<TaskEntity>>

    @Insert
    suspend fun insertTask(task : TaskEntity)

    @Delete
    suspend fun deleteTask(task : TaskEntity)

    @Update
    suspend fun updateTask(task : TaskEntity)

    @Query("DELETE FROM tasks")
    suspend fun deleteAll()
}
