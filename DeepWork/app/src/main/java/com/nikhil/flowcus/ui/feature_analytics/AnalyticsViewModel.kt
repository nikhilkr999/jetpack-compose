package com.nikhil.flowcus.ui.feature_analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.flowcus.data.FocusSession
import com.nikhil.flowcus.data.FocusSessionDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val focusSessionDao: FocusSessionDao
) : ViewModel() {

    private val calendar = Calendar.getInstance()

    val dailyStats = focusSessionDao.getSessionsSince(getStartOfDay())
        .map { sessions ->
            sessions.sumOf { it.durationSeconds } / 60 // to minutes
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val weeklyStats = focusSessionDao.getSessionsSince(getStartOfWeek())
        .map { sessions ->
            sessions.groupBy { getDayOfWeek(it.date) }
                .mapValues { entry -> entry.value.sumOf { it.durationSeconds } / 60 }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    private fun getStartOfDay(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private fun getStartOfWeek(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private fun getDayOfWeek(timestamp: Long): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timestamp
        return cal.get(Calendar.DAY_OF_WEEK)
    }
}
