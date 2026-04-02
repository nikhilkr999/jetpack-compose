package com.nikhil.flowcus.ui.feature_timer

import com.nikhil.flowcus.data.Task

enum class TimerMode(val label: String, val seconds: Int) {
    FOCUS("Focus", 25 * 60),
    SHORT_BREAK("Short break", 5 * 60),
    LONG_BREAK("Long break", 15 * 60)
}

data class TimerState(
    val timeRemaining: Long = 1500L,
    val totalTime: Long = 1500L,
    val isRunning: Boolean = false,
    val progress: Float = 1.0f,
    val selectedTask: Task? = null,
    val startTimeMillis: Long = 0L,
    val selectedAudio: AudioOption? = null,
    val isFullScreen: Boolean = false,
    val selectedMode: TimerMode = TimerMode.FOCUS,
    val modeRemainders: Map<TimerMode, Long> = TimerMode.entries.associateWith { it.seconds.toLong() },
    val modeTotals: Map<TimerMode, Long> = TimerMode.entries.associateWith { it.seconds.toLong() },
    val appLockEnabled: Boolean = true,
    val isDarkMode: Boolean = true
)

data class AudioOption(
    val name: String,
    val icon: Int,
    val audioResId: Int
)
