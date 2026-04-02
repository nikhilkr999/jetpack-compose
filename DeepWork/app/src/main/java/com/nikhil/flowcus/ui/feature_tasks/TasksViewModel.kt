package com.nikhil.flowcus.ui.feature_tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.flowcus.data.Category
import com.nikhil.flowcus.data.Priority
import com.nikhil.flowcus.data.Task
import com.nikhil.flowcus.data.TaskDao
import com.nikhil.flowcus.data.TaskWithTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskDao: TaskDao
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    val tasksWithTime: StateFlow<List<TaskWithTime>> = combine(
        taskDao.getAllTasksWithTime(),
        _selectedCategory
    ) { tasks, category ->
        if (category == null) tasks
        else tasks.filter { it.task.category == category }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onCategorySelected(category: Category?) {
        _selectedCategory.value = category
    }

    fun addTask(title: String, priority: Priority = Priority.MEDIUM, category: Category = Category.OTHER) {
        if (title.isBlank()) return
        viewModelScope.launch {
            taskDao.insertTask(Task(title = title, priority = priority, category = category))
        }
    }

    fun toggleTask(task: Task) {
        viewModelScope.launch {
            taskDao.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
        }
    }
}
