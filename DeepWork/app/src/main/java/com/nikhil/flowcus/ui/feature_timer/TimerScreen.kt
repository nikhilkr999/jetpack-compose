package com.nikhil.flowcus.ui.feature_timer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nikhil.flowcus.data.Task

@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    viewModel: TimerViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    var showTaskDialog by remember { mutableStateOf(false) }
    var showDurationDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Deep Work",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = { showDurationDialog = true },
                enabled = !state.isRunning && state.timeRemaining == state.totalTime
            ) {
                Icon(Icons.Default.Settings, contentDescription = "Set Duration")
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        TimerDisplay(state)
        
        Spacer(modifier = Modifier.height(32.dp))

        // Task Selection
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { if (!state.isRunning) showTaskDialog = true }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1.0f)) {
                    Text("Focusing on:", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = state.selectedTask?.title ?: "No task selected",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
                Icon(Icons.Default.Add, contentDescription = "Select Task")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            val isPaused = !state.isRunning && state.timeRemaining < state.totalTime && state.timeRemaining > 0
            val isNotStarted = !state.isRunning && state.timeRemaining == state.totalTime

            if (state.isRunning) {
                Button(
                    onClick = { viewModel.pauseTimer() },
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Text("Pause")
                }
            } else if (isPaused) {
                Button(
                    onClick = { viewModel.resumeTimer() },
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Text("Resume")
                }
            } else {
                Button(
                    onClick = { viewModel.startTimer() },
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Text("Start")
                }
            }
            
            if (state.isRunning || isPaused) {
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedButton(
                    onClick = { viewModel.stopTimer() },
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Text("Stop")
                }
            }
        }
    }

    if (showTaskDialog) {
        TaskSelectionDialog(
            tasks = tasks,
            onTaskSelected = { 
                viewModel.onTaskSelected(it)
                showTaskDialog = false
            },
            onDismiss = { showTaskDialog = false }
        )
    }

    if (showDurationDialog) {
        DurationDialog(
            currentMinutes = (state.totalTime / 60).toInt(),
            onConfirm = { minutes ->
                viewModel.setTimerDuration(minutes)
                showDurationDialog = false
            },
            onDismiss = { showDurationDialog = false }
        )
    }
}

@Composable
fun DurationDialog(
    currentMinutes: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(currentMinutes.toString()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Focus Duration") },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it.filter { char -> char.isDigit() } },
                label = { Text("Minutes") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        },
        confirmButton = {
            TextButton(onClick = { 
                text.toIntOrNull()?.let { onConfirm(it) }
            }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun TaskSelectionDialog(
    tasks: List<Task>,
    onTaskSelected: (Task) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Task") },
        text = {
            if (tasks.isEmpty()) {
                Text("No tasks available. Go to the Tasks tab to add some!")
            } else {
                LazyColumn {
                    items(tasks) { task ->
                        ListItem(
                            headlineContent = { Text(task.title) },
                            modifier = Modifier.clickable { onTaskSelected(task) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}

@Composable
fun TimerDisplay(state: TimerState) {
    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = { state.progress },
            modifier = Modifier.size(250.dp),
            strokeWidth = 12.dp,
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Text(
            text = formatTime(state.timeRemaining),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(minutes, secs)
}
