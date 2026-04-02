package com.nikhil.flowcus.ui.feature_analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.*

private val BackgroundDark   = Color(0xFF111116)
private val SurfaceDark      = Color(0xFF1C1C24)
private val Amber            = Color(0xFFE8C47A)
private val TextPrimary      = Color(0xFFF0EAD8)
private val TextSecondary    = Color(0xFF888888)
private val TextMuted        = Color(0xFF555555)

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val dailyMinutes by viewModel.dailyMinutes.collectAsState()
    val weeklyStats by viewModel.weeklyStats.collectAsState()
    val calendarDays by viewModel.calendarDays.collectAsState()
    val selectedSessions by viewModel.selectedDaySessions.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundDark
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "Analytics",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextPrimary,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                )
            }

            // Summary Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "TODAY'S FOCUS", 
                            style = MaterialTheme.typography.labelLarge,
                            color = TextSecondary,
                            letterSpacing = 2.sp
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "$dailyMinutes",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Normal,
                            color = Amber,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            "MINUTES",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            letterSpacing = 1.sp
                        )
                    }
                }
                Spacer(Modifier.height(32.dp))
            }

            // Focus Calendar
            item {
                Text(
                    text = "FOCUS CALENDAR",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary,
                    letterSpacing = 2.sp,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )
                
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    items(calendarDays) { day ->
                        CalendarDayItem(
                            day = day,
                            onClick = { viewModel.onDateSelected(day.date) }
                        )
                    }
                }
                Spacer(Modifier.height(32.dp))
            }

            // Session History for selected day
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.History, contentDescription = null, tint = Amber, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "SESSION HISTORY",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextSecondary,
                        letterSpacing = 2.sp
                    )
                }
                Spacer(Modifier.height(16.dp))
            }

            if (selectedSessions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No sessions recorded for this day.", color = TextMuted, fontSize = 14.sp)
                    }
                }
            } else {
                items(selectedSessions) { sessionWithTask ->
                    SessionItem(sessionWithTask)
                    Spacer(Modifier.height(12.dp))
                }
            }
            
            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun CalendarDayItem(
    day: CalendarDay,
    onClick: () -> Unit
) {
    val dayName = SimpleDateFormat("E", Locale.getDefault()).format(day.date).uppercase()
    val dateNum = SimpleDateFormat("d", Locale.getDefault()).format(day.date)
    
    // Intensity of Amber based on focus time (max 5 hours for full color)
    val focusIntensity = (day.totalSeconds.toFloat() / 18000f).coerceIn(0f, 1f)
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 4.dp)
    ) {
        Text(dayName, fontSize = 10.sp, color = if (day.isSelected) Amber else TextMuted)
        Spacer(Modifier.height(8.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (day.isSelected) Amber else Color.Transparent)
                .border(
                    width = 1.dp,
                    color = if (day.isSelected) Amber else SurfaceDark,
                    shape = CircleShape
                )
        ) {
            // Visual indicator of activity
            if (!day.isSelected && day.totalSeconds > 0) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Amber.copy(alpha = 0.1f + (focusIntensity * 0.4f)))
                )
            }
            
            Text(
                text = dateNum,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (day.isSelected) BackgroundDark else TextPrimary
            )
        }
    }
}

@Composable
fun SessionItem(sessionWithTask: SessionWithTask) {
    val startTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(sessionWithTask.session.startTime))
    val duration = formatDuration(sessionWithTask.session.durationSeconds)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = sessionWithTask.taskTitle ?: "General Focus",
                    fontSize = 15.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Started at $startTime",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            Text(
                text = duration,
                fontSize = 16.sp,
                color = Amber,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private fun formatDuration(seconds: Long): String {
    val m = seconds / 60
    val s = seconds % 60
    return if (m > 0) "${m}m ${s}s" else "${s}s"
}
