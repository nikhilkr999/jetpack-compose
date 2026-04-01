package com.nikhil.todo.domain.model

data class Task(
    val id : Int = 0,
    val title : String,
    val isCompleted : Boolean = false
)
