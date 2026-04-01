package com.nikhil.flowcus.ui.feature_timer

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

    if (state.isFullScreen) {
        FullScreenTimer(
            state = state,
            onExitFullScreen = { viewModel.toggleFullScreen() },
            onPause = { viewModel.pauseTimer() },
            onResume = { viewModel.resumeTimer() },
            onStop = { viewModel.stopTimer() }
        )
    } else {
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
                Row {
                    IconButton(
                        onClick = { viewModel.toggleFullScreen() }
                    ) {
                        Icon(Icons.Default.Fullscreen, contentDescription = "Full Screen")
                    }
                    IconButton(
                        onClick = { showDurationDialog = true },
                        enabled = !state.isRunning && state.timeRemaining == state.totalTime
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = "Set Duration")
                    }
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

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Background Sounds",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Music items displayed row by row, 4 items per row
            val audioOptions = getAudioOptions()
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                audioOptions.chunked(4).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        rowItems.forEach { option ->
                            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                AudioChip(
                                    option = option,
                                    isSelected = state.selectedAudio?.name == option.name,
                                    onClick = {
                                        if (state.selectedAudio?.name == option.name) {
                                            viewModel.onAudioSelected(null)
                                        } else {
                                            viewModel.onAudioSelected(option)
                                        }
                                    }
                                )
                            }
                        }
                        // Fill empty slots if row has less than 4 items
                        repeat(4 - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
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
fun FullScreenTimer(
    state: TimerState,
    onExitFullScreen: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit
) {
    BackHandler { onExitFullScreen() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        IconButton(
            onClick = onExitFullScreen,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(Icons.Default.FullscreenExit, contentDescription = "Exit Full Screen")
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = state.selectedTask?.title ?: "Focusing...",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(48.dp))

            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { state.progress },
                    modifier = Modifier.size(320.dp),
                    strokeWidth = 16.dp,
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Text(
                    text = formatTime(state.timeRemaining),
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                if (state.isRunning) {
                    FilledTonalIconButton(
                        onClick = onPause,
                        modifier = Modifier.size(72.dp)
                    ) {
                        Icon(Icons.Default.Pause, contentDescription = "Pause", modifier = Modifier.size(32.dp))
                    }
                } else {
                    FilledIconButton(
                        onClick = onResume,
                        modifier = Modifier.size(72.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Resume", modifier = Modifier.size(32.dp))
                    }
                }
                
                Spacer(modifier = Modifier.width(32.dp))

                OutlinedIconButton(
                    onClick = onStop,
                    modifier = Modifier.size(72.dp)
                ) {
                    Icon(Icons.Default.Stop, contentDescription = "Stop", modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}

@Composable
fun AudioChip(
    option: AudioOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val icon = when (option.name) {
        "Rain" -> Icons.Default.WaterDrop
        "Rain & Thunder" -> Icons.Default.Thunderstorm
        "Forest" -> Icons.Default.Forest
        "Wind" -> Icons.Default.Air
        "Ocean" -> Icons.Default.Waves
        "Instrument" -> Icons.Default.MusicNote
        "lo-fi" -> Icons.Default.Headphones
        "piano" -> Icons.Default.Piano
        else -> Icons.AutoMirrored.Filled.QueueMusic
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant
                )
                .border(
                    width = 2.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = option.name,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = option.name,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}

private fun getAudioOptions(): List<AudioOption> {
    return listOf(
        AudioOption("Rain", 0, com.nikhil.flowcus.R.raw.rain),
        AudioOption("Rain & Thunder", 0, com.nikhil.flowcus.R.raw.thunder),
        AudioOption("Forest", 0, com.nikhil.flowcus.R.raw.forest),
        AudioOption("Wind", 0, com.nikhil.flowcus.R.raw.wind),
        AudioOption("Ocean", 0, com.nikhil.flowcus.R.raw.ocean),
        AudioOption("Instrument", 0, com.nikhil.flowcus.R.raw.instrument),
        AudioOption("lo-fi", 0, com.nikhil.flowcus.R.raw.lo_fi),
        AudioOption("piano", 0, com.nikhil.flowcus.R.raw.minimal_piano)
    )
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
