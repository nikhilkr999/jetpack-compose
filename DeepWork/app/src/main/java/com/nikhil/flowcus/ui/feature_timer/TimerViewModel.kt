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
                        // Update the remainder for the current active mode
                        val updatedRemainders = state.modeRemainders.toMutableMap()
                        if (running) {
                            updatedRemainders[state.selectedMode] = time
                        }

                        val newTime = if (!running && time == 0L && !wasRunning && state.timeRemaining > 0) {
                            state.timeRemaining
                        } else {
                            time
                        }

                        state.copy(
                            timeRemaining = newTime,
                            isRunning = running,
                            progress = if (state.totalTime > 0) newTime.toFloat() / state.totalTime else 1.0f,
                            modeRemainders = updatedRemainders
                        )
                    }
                    
                    if (wasRunning && !running && time == 0L) {
                        saveSession()
                    }
                }
            }
        }
    }

    fun onModeSelected(mode: TimerMode) {
        if (_uiState.value.isRunning) return
        
        _uiState.update { state ->
            val newTotal = state.modeTotals[mode] ?: mode.seconds.toLong()
            val newRemaining = state.modeRemainders[mode] ?: newTotal
            
            state.copy(
                selectedMode = mode,
                totalTime = newTotal,
                timeRemaining = newRemaining,
                progress = if (newTotal > 0) newRemaining.toFloat() / newTotal else 1.0f
            )
        }
    }

    fun onTaskSelected(task: Task?) {
        _uiState.update { it.copy(selectedTask = task) }
    }

    fun setTimerDuration(minutes: Int) {
        if (_uiState.value.isRunning) return
        val seconds = minutes * 60L
        _uiState.update { state ->
            val updatedTotals = state.modeTotals.toMutableMap()
            val updatedRemainders = state.modeRemainders.toMutableMap()
            
            updatedTotals[state.selectedMode] = seconds
            updatedRemainders[state.selectedMode] = seconds
            
            state.copy(
                totalTime = seconds,
                timeRemaining = seconds,
                progress = 1.0f,
                modeTotals = updatedTotals,
                modeRemainders = updatedRemainders
            )
        }
    }

    fun onAudioSelected(audio: AudioOption?) {
        _uiState.update { it.copy(selectedAudio = audio) }
        timerService?.playAudio(audio?.audioResId ?: 0)
    }

    fun toggleFullScreen() {
        _uiState.update { it.copy(isFullScreen = !it.isFullScreen) }
    }

    fun startTimer() {
        val intent = Intent(application, TimerService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            application.startForegroundService(intent)
        } else {
            application.startService(intent)
        }
        timerService?.startTimer(_uiState.value.timeRemaining) // Use current remainder
        _uiState.update { it.copy(startTimeMillis = System.currentTimeMillis()) }
        
        // Start audio if one was selected
        _uiState.value.selectedAudio?.let {
            timerService?.playAudio(it.audioResId)
        }
    }

    fun pauseTimer() {
        timerService?.pauseTimer()
    }

    fun resumeTimer() {
        timerService?.resumeTimer()
    }

    fun stopTimer() {
        saveSession() 
        timerService?.stopTimer()
        _uiState.update { state ->
            val updatedRemainders = state.modeRemainders.toMutableMap()
            updatedRemainders[state.selectedMode] = state.totalTime
            
            state.copy(
                timeRemaining = state.totalTime,
                progress = 1.0f,
                isRunning = false,
                startTimeMillis = 0L,
                modeRemainders = updatedRemainders
            )
        }
    }

    private fun saveSession() {
        val state = _uiState.value
        if (state.startTimeMillis == 0L) return

        val duration = state.totalTime - state.timeRemaining
        if (duration > 0 && state.selectedMode == TimerMode.FOCUS) {
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
