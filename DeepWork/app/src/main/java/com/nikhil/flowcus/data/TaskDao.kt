package com.nikhil.flowcus.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("""
        SELECT tasks.*, IFNULL(SUM(focus_sessions.durationSeconds), 0) as totalFocusSeconds 
        FROM tasks 
        LEFT JOIN focus_sessions ON tasks.id = focus_sessions.taskId 
        GROUP BY tasks.id 
        ORDER BY tasks.createdAt DESC
    """)
    fun getAllTasksWithTime(): Flow<List<TaskWithTime>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}
