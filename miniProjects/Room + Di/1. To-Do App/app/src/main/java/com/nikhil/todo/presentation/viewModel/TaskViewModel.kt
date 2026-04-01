package com.nikhil.todo.presentation.viewModel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.todo.domain.model.Task
import com.nikhil.todo.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks : StateFlow<List<Task>> = _tasks

    init {
        getAlltasks();
    }

    fun getAlltasks(){
        viewModelScope.launch {
            val tasks = repository.getTasks()
            tasks.collect {
                _tasks.value = it
            }

        }
    }

    fun insertTask(task : String){
        viewModelScope.launch {
            repository.insert(Task(title = task))
        }
    }

    fun deleteTask(task : Task){
        viewModelScope.launch {
            repository.delete(task)
        }
    }

    fun updateTask(task : Task){
        viewModelScope.launch {
            repository.update(task)
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}