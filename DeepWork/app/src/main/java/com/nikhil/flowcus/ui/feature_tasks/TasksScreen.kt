package com.nikhil.flowcus.ui.feature_tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nikhil.flowcus.data.Category
import com.nikhil.flowcus.data.Priority
import com.nikhil.flowcus.data.Task
import com.nikhil.flowcus.data.TaskWithTime
import com.nikhil.flowcus.ui.feature_timer.TimerViewModel

private val BackgroundDark   = Color(0xFF111116)
private val SurfaceDark      = Color(0xFF1C1C24)
private val Amber            = Color(0xFFE8C47A)
private val TextPrimary      = Color(0xFFF0EAD8)
private val TextSecondary    = Color(0xFF888888)
private val AmberOnDark      = Color(0xFF1A1206)

private val PriorityHigh     = Color(0xFFE57373)
private val PriorityMedium   = Color(0xFFE8C47A)
private val PriorityLow      = Color(0xFF81C784)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    onFocusTask: (Task) -> Unit,
    viewModel: TasksViewModel = hiltViewModel(),
    timerViewModel: TimerViewModel = hiltViewModel()
) {
    val tasksWithTime by viewModel.tasksWithTime.collectAsState()
    val timerState by timerViewModel.uiState.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var newTaskTitle by remember { mutableStateOf("") }
    var newTaskPriority by remember { mutableStateOf(Priority.MEDIUM) }
    var newTaskCategory by remember { mutableStateOf(Category.WORK) }

    var taskToSwitchTo by remember { mutableStateOf<Task?>(null) }
    var showStopWarning by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp)
            ) {
                item {
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "Your Tasks",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextPrimary,
                        fontFamily = FontFamily.Serif,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                    )
                }

                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = selectedCategory == null,
                                onClick = { viewModel.onCategorySelected(null) },
                                label = { Text("All") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Amber.copy(alpha = 0.2f),
                                    selectedLabelColor = Amber,
                                    containerColor = SurfaceDark,
                                    labelColor = TextSecondary
                                ),
                                border = null
                            )
                        }
                        items(Category.entries) { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = { viewModel.onCategorySelected(category) },
                                label = { Text("${category.icon} ${category.label}") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Amber.copy(alpha = 0.2f),
                                    selectedLabelColor = Amber,
                                    containerColor = SurfaceDark,
                                    labelColor = TextSecondary
                                ),
                                border = null
                            )
                        }
                    }
                }
                
                if (tasksWithTime.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(top = 100.dp), contentAlignment = Alignment.Center) {
                            Text("No tasks found.", color = TextMuted)
                        }
                    }
                } else {
                    items(tasksWithTime) { item ->
                        TaskItem(
                            taskWithTime = item,
                            onToggle = { viewModel.toggleTask(item.task) },
                            onDelete = { viewModel.deleteTask(item.task) },
                            onFocus = { 
                                if (timerState.isRunning && timerState.selectedTask?.id != item.task.id) {
                                    taskToSwitchTo = item.task
                                    showStopWarning = true
                                } else {
                                    onFocusTask(item.task)
                                }
                            }
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }
                
                item {
                    Spacer(Modifier.height(100.dp))
                }
            }

            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Amber,
                contentColor = AmberOnDark,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    }

    if (showStopWarning) {
        AlertDialog(
            onDismissRequest = { showStopWarning = false },
            containerColor = SurfaceDark,
            title = { Text("Stop Current Session?", color = TextPrimary, fontFamily = FontFamily.Serif) },
            text = { 
                Text(
                    "You have an ongoing focus session for '${timerState.selectedTask?.title}'. Stop it to focus on '${taskToSwitchTo?.title}'?",
                    color = TextSecondary
                ) 
            },
            confirmButton = {
                Button(
                    onClick = {
                        timerViewModel.stopTimer()
                        taskToSwitchTo?.let { onFocusTask(it) }
                        showStopWarning = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Amber, contentColor = AmberOnDark)
                ) {
                    Text("Stop & Switch")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStopWarning = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            containerColor = SurfaceDark,
            title = { Text("Add New Task", color = TextPrimary, fontFamily = FontFamily.Serif) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    TextField(
                        value = newTaskTitle,
                        onValueChange = { newTaskTitle = it },
                        placeholder = { Text("What's your focus?", color = TextMuted) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = BackgroundDark,
                            unfocusedContainerColor = BackgroundDark,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            cursorColor = Amber,
                            focusedIndicatorColor = Amber
                        )
                    )
                    
                    Column {
                        Text("CATEGORY", color = TextSecondary, fontSize = 10.sp, letterSpacing = 1.sp)
                        Spacer(Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(Category.entries) { category ->
                                FilterChip(
                                    selected = newTaskCategory == category,
                                    onClick = { newTaskCategory = category },
                                    label = { Text("${category.icon} ${category.label}") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Amber.copy(alpha = 0.2f),
                                        selectedLabelColor = Amber,
                                        containerColor = BackgroundDark,
                                        labelColor = TextSecondary
                                    ),
                                    border = null
                                )
                            }
                        }
                    }

                    Column {
                        Text("PRIORITY", color = TextSecondary, fontSize = 10.sp, letterSpacing = 1.sp)
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Priority.entries.forEach { priority ->
                                FilterChip(
                                    selected = newTaskPriority == priority,
                                    onClick = { newTaskPriority = priority },
                                    label = { Text(priority.name.lowercase().replaceFirstChar { it.uppercase() }) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = when(priority) {
                                            Priority.HIGH -> PriorityHigh
                                            Priority.MEDIUM -> PriorityMedium
                                            Priority.LOW -> PriorityLow
                                        }.copy(alpha = 0.2f),
                                        selectedLabelColor = when(priority) {
                                            Priority.HIGH -> PriorityHigh
                                            Priority.MEDIUM -> PriorityMedium
                                            Priority.LOW -> PriorityLow
                                        },
                                        containerColor = BackgroundDark,
                                        labelColor = TextSecondary
                                    ),
                                    border = null
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addTask(newTaskTitle, newTaskPriority, newTaskCategory)
                        newTaskTitle = ""
                        newTaskPriority = Priority.MEDIUM
                        newTaskCategory = Category.WORK
                        showAddDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Amber, contentColor = AmberOnDark)
                ) {
                    Text("Add Task")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
fun TaskItem(
    taskWithTime: TaskWithTime,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onFocus: () -> Unit
) {
    val priorityColor = when (taskWithTime.task.priority) {
        Priority.HIGH -> PriorityHigh
        Priority.MEDIUM -> PriorityMedium
        Priority.LOW -> PriorityLow
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(4.dp)
                    .background(priorityColor)
            )
            
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = taskWithTime.task.isCompleted,
                    onCheckedChange = { onToggle() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Amber,
                        uncheckedColor = TextSecondary,
                        checkmarkColor = AmberOnDark
                    )
                )
                
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${taskWithTime.task.category.icon} ",
                            fontSize = 14.sp
                        )
                        Text(
                            text = taskWithTime.task.title,
                            fontSize = 16.sp,
                            color = if (taskWithTime.task.isCompleted) TextSecondary else TextPrimary,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.Serif
                        )
                    }
                    Text(
                        text = "Focused: ${formatDuration(taskWithTime.totalFocusSeconds)}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                IconButton(
                    onClick = onFocus,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Amber.copy(alpha = 0.1f))
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Focus",
                        tint = Amber,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.width(8.dp))

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red.copy(alpha = 0.4f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

private val TextMuted = Color(0xFF555555)

private fun formatDuration(seconds: Long): String {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    return if (h > 0) {
        "${h}h ${m}m"
    } else if (m > 0) {
        "${m}m ${s}s"
    } else {
        "${s}s"
    }
}
