package com.nikhil.todo.data.repository

import com.nikhil.todo.data.local.TaskDao
import com.nikhil.todo.data.local.TaskEntity
import com.nikhil.todo.domain.model.Task
import com.nikhil.todo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor (private val dao : TaskDao) : TaskRepository{
    override fun getTasks(): Flow<List<Task>> {
        return dao.getTasks().map { list ->
            list.map { it.toDomain() }
        }
    }


    override suspend fun insert(task: Task) {
        dao.insertTask(task.toEntity())
    }

    override suspend fun delete(task: Task) {
        dao.deleteTask(task.toEntity())
    }

    override suspend fun update(task: Task) {
        dao.updateTask(task.toEntity())
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }


}

fun TaskEntity.toDomain() = Task(id, title, isCompleted)

fun Task.toEntity() = TaskEntity(id, title, isCompleted)
