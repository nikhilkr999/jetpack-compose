package com.nikhil.flowcus.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Priority {
    LOW, MEDIUM, HIGH
}

enum class Category(val label: String, val icon: String) {
    WORK("Work", "💼"),
    STUDY("Study", "🎓"),
    PERSONAL("Personal", "🏠"),
    HEALTH("Health", "🧘"),
    OTHER("Other", "📁")
}

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val category: Category = Category.OTHER,
    val createdAt: Long = System.currentTimeMillis()
)
