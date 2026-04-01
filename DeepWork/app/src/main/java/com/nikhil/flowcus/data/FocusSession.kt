package com.nikhil.flowcus.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val taskId: Int?,
    val startTime: Long,
    val durationSeconds: Long,
    val date: Long = System.currentTimeMillis()
)
