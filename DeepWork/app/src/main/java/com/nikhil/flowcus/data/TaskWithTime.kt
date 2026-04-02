package com.nikhil.flowcus.data

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithTime(
    @Embedded val task: Task,
    val totalFocusSeconds: Long
)
