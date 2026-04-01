package com.nikhil.todo.domain.repository

import com.nikhil.todo.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasks(): Flow<List<Task>>
    suspend fun insert(task: Task)
    suspend fun delete(task: Task)
    suspend fun update(task: Task)
    suspend fun deleteAll()
}
