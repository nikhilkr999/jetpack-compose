package com.nikhil.flowcus.ui.feature_timer

import com.nikhil.flowcus.data.Task

data class TimerState(
    val timeRemaining: Long = 1500L, // 25 minutes in seconds
    val totalTime: Long = 1500L,
    val isRunning: Boolean = false,
    val progress: Float = 1.0f,
    val selectedTask: Task? = null,
    val startTimeMillis: Long = 0L
)
