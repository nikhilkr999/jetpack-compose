package com.nikhil.flowcus.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.nikhil.flowcus.MainActivity
import com.nikhil.flowcus.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@AndroidEntryPoint
class TimerService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var timerJob: Job? = null

    private val _timeRemaining = MutableStateFlow(0L)
    val timeRemaining = _timeRemaining.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    private val notificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onBind(intent: Intent?): IBinder = TimerBinder()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    fun startTimer(durationSeconds: Long) {
        if (_isRunning.value) return
        
        _timeRemaining.value = durationSeconds
        _isRunning.value = true

        startForeground(NOTIFICATION_ID, createNotification(_timeRemaining.value))

        timerJob = serviceScope.launch {
            while (_timeRemaining.value > 0) {
                delay(1000L)
                _timeRemaining.value -= 1
                updateNotification(_timeRemaining.value)
            }
            _isRunning.value = false
            onTimerFinished()
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _isRunning.value = false
        updateNotification(_timeRemaining.value)
    }

    fun resumeTimer() {
        if (_isRunning.value) return
        _isRunning.value = true
        timerJob = serviceScope.launch {
            while (_timeRemaining.value > 0) {
                delay(1000L)
                _timeRemaining.value -= 1
                updateNotification(_timeRemaining.value)
            }
            _isRunning.value = false
            onTimerFinished()
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        _isRunning.value = false
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun onTimerFinished() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Session Completed!")
            .setContentText("Great job staying focused!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        notificationManager.notify(COMPLETED_NOTIFICATION_ID, notification)
        stopForeground(STOP_FOREGROUND_DETACH)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Timer Notifications",
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(timeRemaining: Long): Notification {
        val minutes = timeRemaining / 60
        val seconds = timeRemaining % 60
        val timeString = String.format("%02d:%02d", minutes, seconds)

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Focusing...")
            .setContentText("Time remaining: $timeString")
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun updateNotification(timeRemaining: Long) {
        notificationManager.notify(NOTIFICATION_ID, createNotification(timeRemaining))
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val CHANNEL_ID = "timer_channel"
        const val NOTIFICATION_ID = 1
        const val COMPLETED_NOTIFICATION_ID = 2
    }
}
