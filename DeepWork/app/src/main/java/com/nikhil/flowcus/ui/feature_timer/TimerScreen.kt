package com.nikhil.flowcus.ui.feature_timer

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nikhil.flowcus.data.Task
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// ─── Colors ──────────────────────────────────────────────────────────────────

private val BackgroundDark   = Color(0xFF111116)
private val SurfaceDark      = Color(0xFF1C1C24)
private val SurfaceLight     = Color(0xFF25200F)
private val BorderDark       = Color(0xFF2A2A35)
private val Amber            = Color(0xFFE8C47A)
private val AmberDim         = Color(0xFFC9A85A)
private val TextPrimary      = Color(0xFFF0EAD8)
private val TextSecondary    = Color(0xFF888888)
private val TextMuted        = Color(0xFF555555)
private val AmberOnDark      = Color(0xFF1A1206)

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
            onStart = { viewModel.startTimer() },
            onPause = { viewModel.pauseTimer() },
            onResume = { viewModel.resumeTimer() },
            onStop = { viewModel.stopTimer() }
        )
    } else {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = BackgroundDark
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                TimerHeader(
                    onSettingsClick = { showDurationDialog = true },
                    onFullScreenClick = { viewModel.toggleFullScreen() },
                    isTimerRunning = state.isRunning,
                    isAtStart = state.timeRemaining == state.totalTime
                )

                // Mode tabs
                ModeTabs(
                    selectedMode = state.selectedMode,
                    onModeSelected = { viewModel.onModeSelected(it) },
                    enabled = !state.isRunning
                )

                Spacer(Modifier.height(32.dp))

                // Animated ring
                TimerRing(
                    progress = state.progress,
                    timeRemaining = state.timeRemaining,
                    sessionLabel = if (state.selectedMode == TimerMode.FOCUS) "FOCUS SESSION" else "BREAK TIME"
                )

                Spacer(Modifier.height(32.dp))

                // Task chip
                TaskChip(
                    selectedTask = state.selectedTask,
                    onClick = { if (!state.isRunning) showTaskDialog = true },
                    modifier = Modifier.padding(horizontal = 22.dp)
                )

                Spacer(Modifier.height(24.dp))

                // Start / Pause button
                StartButton(
                    isRunning = state.isRunning,
                    isPaused = !state.isRunning && state.timeRemaining < state.totalTime && state.timeRemaining > 0,
                    modifier = Modifier.padding(horizontal = 22.dp),
                    onStart = { viewModel.startTimer() },
                    onPause = { viewModel.pauseTimer() },
                    onResume = { viewModel.resumeTimer() }
                )

                if (state.isRunning || (state.timeRemaining < state.totalTime && state.timeRemaining > 0)) {
                    Spacer(modifier = Modifier.height(12.dp))
                    TextButton(
                        onClick = { viewModel.stopTimer() },
                        colors = ButtonDefaults.textButtonColors(contentColor = TextSecondary)
                    ) {
                        Text("Stop Session", fontSize = 12.sp)
                    }
                }

                Spacer(Modifier.height(32.dp))

                // Background sounds
                SoundsSection(
                    selectedAudio = state.selectedAudio,
                    onAudioSelected = { viewModel.onAudioSelected(it) }
                )
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
fun TimerHeader(
    onSettingsClick: () -> Unit,
    onFullScreenClick: () -> Unit,
    isTimerRunning: Boolean,
    isAtStart: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(
            text       = "Deep Work",
            fontSize   = 24.sp,
            fontWeight = FontWeight.Normal,
            color      = TextPrimary,
            fontFamily = FontFamily.Serif
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            IconButton(onClick = onFullScreenClick, modifier = Modifier.size(24.dp)) {
                Icon(
                    imageVector = Icons.Default.Fullscreen,
                    contentDescription = "Full Screen",
                    tint = TextPrimary.copy(alpha = 0.5f)
                )
            }
            IconButton(
                onClick = onSettingsClick,
                enabled = !isTimerRunning && isAtStart,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector        = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint               = if (!isTimerRunning && isAtStart) TextPrimary.copy(alpha = 0.5f) else TextMuted
                )
            }
        }
    }
}

@Composable
fun ModeTabs(
    selectedMode: TimerMode,
    onModeSelected: (TimerMode) -> Unit,
    enabled: Boolean
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 22.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(SurfaceDark)
            .padding(4.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TimerMode.entries.forEach { mode ->
                val active = mode == selectedMode
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50))
                        .background(if (active) Amber else Color.Transparent)
                        .clickable(enabled = enabled) { onModeSelected(mode) }
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text       = mode.label,
                        fontSize   = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color      = if (active) AmberOnDark else TextMuted,
                        textAlign  = TextAlign.Center,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TimerRing(
    progress: Float,
    timeRemaining: Long,
    sessionLabel: String,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue    = progress,
        animationSpec  = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label          = "ring_progress"
    )

    val timeText = remember(timeRemaining) {
        val m = timeRemaining / 60
        val s = timeRemaining % 60
        "%02d:%02d".format(m, s)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(240.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawTimerRing(animatedProgress)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text       = timeText,
                fontSize   = 54.sp,
                fontWeight = FontWeight.Normal,
                color      = TextPrimary,
                fontFamily = FontFamily.Serif,
                letterSpacing = 1.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text          = sessionLabel,
                fontSize      = 10.sp,
                color         = TextSecondary,
                letterSpacing = 1.5.sp
            )
        }
    }
}

private fun DrawScope.drawTimerRing(progress: Float) {
    val strokeWidth = 8.dp.toPx()
    val radius      = (size.minDimension - strokeWidth) / 2.2f
    val center      = Offset(size.width / 2f, size.height / 2f)
    val topLeft     = Offset(center.x - radius, center.y - radius)
    val arcSize     = Size(radius * 2, radius * 2)
    val startAngle  = -90f
    val sweepAngle  = 360f * progress

    // Track
    drawArc(
        color      = SurfaceDark,
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter  = false,
        topLeft    = topLeft,
        size       = arcSize,
        style      = Stroke(width = strokeWidth, cap = StrokeCap.Round)
    )
    
    if (progress > 0.001f) {
        // Glow
        drawArc(
            color      = Amber.copy(alpha = 0.12f),
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter  = false,
            topLeft    = topLeft,
            size       = arcSize,
            style      = Stroke(width = strokeWidth * 2.2f, cap = StrokeCap.Round)
        )
        // Main progress arc
        drawArc(
            color      = Amber,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter  = false,
            topLeft    = topLeft,
            size       = arcSize,
            style      = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
        // Tip dot
        val angleRad = (startAngle + sweepAngle) * PI.toFloat() / 180f
        val tipX     = center.x + radius * cos(angleRad)
        val tipY     = center.y + radius * sin(angleRad)
        drawCircle(
            color  = Amber,
            radius = strokeWidth / 1.5f,
            center = Offset(tipX, tipY)
        )
    }
}

@Composable
fun TaskChip(
    selectedTask: Task?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(SurfaceDark)
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text      = "Focusing on",
                fontSize  = 10.sp,
                color     = TextSecondary,
                letterSpacing = 0.5.sp
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text       = selectedTask?.title ?: "No task selected",
                fontSize   = 13.sp,
                fontWeight = FontWeight.Normal,
                color      = TextPrimary
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(BorderDark)
        ) {
            Icon(
                imageVector        = Icons.Default.Add,
                contentDescription = "Add task",
                tint               = TextSecondary,
                modifier           = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun StartButton(
    isRunning: Boolean,
    isPaused: Boolean,
    modifier: Modifier = Modifier,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue   = if (isRunning) AmberDim else Amber,
        animationSpec = tween(200),
        label         = "btn_color"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(50))
            .background(bgColor)
            .clickable {
                when {
                    isRunning -> onPause()
                    isPaused -> onResume()
                    else -> onStart()
                }
            }
    ) {
        Text(
            text          = when {
                isRunning -> "Pause"
                isPaused -> "Resume"
                else -> "Start session"
            },
            fontSize      = 14.sp,
            fontWeight    = FontWeight.Medium,
            color         = AmberOnDark,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun SoundsSection(
    selectedAudio: AudioOption?,
    onAudioSelected: (AudioOption?) -> Unit
) {
    val sounds = getAudioOptions()

    Column(modifier = Modifier.padding(horizontal = 22.dp)) {
        Text(
            text          = "Background sounds",
            fontSize      = 11.sp,
            color         = TextSecondary,
            letterSpacing = 1.sp,
            modifier      = Modifier.padding(bottom = 12.dp)
        )
        
        sounds.chunked(4).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowItems.forEach { option ->
                    SoundButton(
                        option = option,
                        isActive = selectedAudio?.name == option.name,
                        onAudioSelected = {
                            if (selectedAudio?.name == option.name) {
                                onAudioSelected(null)
                            } else {
                                onAudioSelected(option)
                            }
                        }
                    )
                }
                // Placeholder for alignment if row isn't full
                repeat(4 - rowItems.size) {
                    Spacer(Modifier.width(62.dp))
                }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun SoundButton(
    option: AudioOption,
    isActive: Boolean,
    onAudioSelected: () -> Unit
) {
    val iconBg by animateColorAsState(
        targetValue   = if (isActive) SurfaceLight else SurfaceDark,
        animationSpec = tween(200),
        label         = "sound_bg_${option.name}"
    )
    val iconTint by animateColorAsState(
        targetValue   = if (isActive) Amber else TextSecondary,
        animationSpec = tween(200),
        label         = "sound_tint_${option.name}"
    )
    val borderColor = if (isActive) Amber else BorderDark

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
        modifier            = Modifier
            .width(62.dp)
            .clickable { onAudioSelected() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(iconBg)
                .border(1.5.dp, borderColor, CircleShape)
        ) {
            Icon(
                imageVector        = icon,
                contentDescription = option.name,
                tint               = iconTint,
                modifier           = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text      = option.name,
            fontSize  = 10.sp,
            color     = if (isActive) Amber else TextSecondary,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Composable
fun FullScreenTimer(
    state: TimerState,
    onExitFullScreen: () -> Unit,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit
) {
    BackHandler { onExitFullScreen() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            IconButton(
                onClick = onExitFullScreen,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.FullscreenExit, contentDescription = "Exit", tint = TextPrimary.copy(alpha = 0.5f))
            }

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = state.selectedTask?.title ?: "Focusing",
                    fontSize = 18.sp,
                    color = Amber,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Serif
                )
                
                Spacer(modifier = Modifier.height(48.dp))

                TimerRing(
                    progress = state.progress,
                    timeRemaining = state.timeRemaining,
                    sessionLabel = state.selectedMode.label.uppercase()
                )

                Spacer(modifier = Modifier.height(64.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isPaused = !state.isRunning && state.timeRemaining < state.totalTime && state.timeRemaining > 0
                    
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Amber)
                            .clickable { 
                                when {
                                    state.isRunning -> onPause()
                                    isPaused -> onResume()
                                    else -> onStart()
                                }
                            }
                    ) {
                        Icon(
                            imageVector = if (state.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Action",
                            tint = AmberOnDark,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    
                    if (state.isRunning || isPaused) {
                        Spacer(modifier = Modifier.width(32.dp))
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, BorderDark, CircleShape)
                                .clickable { onStop() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Stop",
                                tint = TextSecondary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
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
