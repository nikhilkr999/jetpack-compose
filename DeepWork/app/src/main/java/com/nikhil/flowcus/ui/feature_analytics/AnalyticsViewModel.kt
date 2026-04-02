package com.nikhil.flowcus.ui.feature_analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.flowcus.data.FocusSession
import com.nikhil.flowcus.data.FocusSessionDao
import com.nikhil.flowcus.data.Task
import com.nikhil.flowcus.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class CalendarDay(
    val date: Date,
    val totalSeconds: Long,
    val isSelected: Boolean = false
)

data class SessionWithTask(
    val session: FocusSession,
    val taskTitle: String?
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val focusSessionDao: FocusSessionDao,
    private val taskDao: TaskDao
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(getStartOfDay(Date()).time)
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()

    val dailyMinutes = focusSessionDao.getSessionsSince(getStartOfDay(Date()).time)
        .map { sessions ->
            sessions.sumOf { it.durationSeconds } / 60
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val weeklyStats = focusSessionDao.getSessionsSince(getStartOfWeek().time)
        .map { sessions ->
            sessions.groupBy { getDayOfWeek(it.date) }
                .mapValues { entry -> entry.value.sumOf { it.durationSeconds } / 60 }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    // Data for the horizontal calendar (last 7 days)
    val calendarDays = combine(
        focusSessionDao.getSessionsSince(getStartOfPastDays(7).time),
        _selectedDate
    ) { sessions, selected ->
        (0..6).map { i ->
            val date = getStartOfPastDays(6 - i)
            val daySessions = sessions.filter { isSameDay(Date(it.date), date) }
            CalendarDay(
                date = date,
                totalSeconds = daySessions.sumOf { it.durationSeconds },
                isSelected = isSameDay(date, Date(selected))
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Detailed sessions for the selected day
    val selectedDaySessions = combine(
        _selectedDate,
        focusSessionDao.getAllSessions(),
        taskDao.getAllTasks()
    ) { selected, allSessions, allTasks ->
        val taskMap = allTasks.associateBy { it.id }
        allSessions
            .filter { isSameDay(Date(it.date), Date(selected)) }
            .map { session ->
                SessionWithTask(
                    session = session,
                    taskTitle = session.taskId?.let { taskMap[it]?.title }
                )
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onDateSelected(date: Date) {
        _selectedDate.value = getStartOfDay(date).time
    }

    private fun getStartOfDay(date: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time
    }

    private fun getStartOfWeek(): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        return getStartOfDay(cal.time)
    }

    private fun getStartOfPastDays(days: Int): Date {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -days)
        return getStartOfDay(cal.time)
    }

    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance().apply { time = date1 }
        val cal2 = Calendar.getInstance().apply { time = date2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    private fun getDayOfWeek(timestamp: Long): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timestamp
        return cal.get(Calendar.DAY_OF_WEEK)
    }
}
