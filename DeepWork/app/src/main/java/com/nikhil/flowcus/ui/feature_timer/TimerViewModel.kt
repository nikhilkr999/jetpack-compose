package com.nikhil.flowcus.ui.feature_timer

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.flowcus.data.FocusSession
import com.nikhil.flowcus.data.FocusSessionDao
import com.nikhil.flowcus.data.Task
import com.nikhil.flowcus.data.TaskDao
import com.nikhil.flowcus.service.TimerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val application: Application,
    private val taskDao: TaskDao,
    private val focusSessionDao: FocusSessionDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerState())
    val uiState: StateFlow<TimerState> = _uiState.asStateFlow()

    private var timerService: TimerService? = null
    private var isBound = false

    val tasks = taskDao.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as TimerService.TimerBinder
            timerService = binder.getService()
            isBound = true
            observeService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            timerService = null
            isBound = false
        }
    }

    init {
        Intent(application, TimerService::class.java).also { intent ->
            application.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun observeService() {
        timerService?.let { service ->
            viewModelScope.launch {
                combine(service.timeRemaining, service.isRunning) { time, running ->
                    time to running
                }.collect { (time, running) ->
                    val wasRunning = _uiState.value.isRunning
                    
                    _uiState.update { state ->
                        // Don't let the idle service (00:00) overwrite our initial/preset time
                        val newTime = if (!running && time == 0L && !wasRunning && state.timeRemaining > 0) {
                            state.timeRemaining
                        } else {
                            time
                        }

                        state.copy(
                            timeRemaining = newTime,
                            isRunning = running,
                            progress = if (state.totalTime > 0) newTime.toFloat() / state.totalTime else 1.0f
                        )
                    }
                    
                    if (wasRunning && !running && time == 0L) {
                        // Timer just finished naturally
                        saveSession()
                    }
                }
            }
        }
    }

    fun onTaskSelected(task: Task?) {
        _uiState.update { it.copy(selectedTask = task) }
    }

    fun setTimerDuration(minutes: Int) {
        if (_uiState.value.isRunning) return
        val seconds = minutes * 60L
        _uiState.update { it.copy(
            totalTime = seconds,
            timeRemaining = seconds,
            progress = 1.0f
        ) }
    }

    fun startTimer() {
        val intent = Intent(application, TimerService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            application.startForegroundService(intent)
        } else {
            application.startService(intent)
        }
        timerService?.startTimer(_uiState.value.totalTime)
        _uiState.update { it.copy(startTimeMillis = System.currentTimeMillis()) }
    }

    fun pauseTimer() {
        timerService?.pauseTimer()
    }

    fun resumeTimer() {
        timerService?.resumeTimer()
    }

    fun stopTimer() {
        saveSession() // Save progress before stopping
        timerService?.stopTimer()
        _uiState.update { it.copy(
            timeRemaining = it.totalTime,
            progress = 1.0f,
            isRunning = false,
            startTimeMillis = 0L
        ) }
    }

    private fun saveSession() {
        val state = _uiState.value
        if (state.startTimeMillis == 0L) return

        val duration = state.totalTime - state.timeRemaining
        if (duration > 0) {
            viewModelScope.launch {
                focusSessionDao.insertSession(
                    FocusSession(
                        taskId = state.selectedTask?.id,
                        startTime = state.startTimeMillis,
                        durationSeconds = duration
                    )
                )
            }
        }
        // Reset start time so we don't save the same session twice
        _uiState.update { it.copy(startTimeMillis = 0L) }
    }

    override fun onCleared() {
        super.onCleared()
        if (isBound) {
            application.unbindService(serviceConnection)
            isBound = false
        }
    }
}
