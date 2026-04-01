package com.nikhil.flowcus.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.nikhil.flowcus.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow

class AppBlockingService : AccessibilityService() {

    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    
    companion object {
        val isTimerRunning = MutableStateFlow(false)
        val blockedPackages = mutableSetOf(
            "com.google.android.youtube",
            "com.instagram.android",
            "com.facebook.katana",
            "com.twitter.android",
            "com.zhiliaoapp.musically"
        )
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return
            
            if (isTimerRunning.value && blockedPackages.contains(packageName)) {
                // Block the app by redirecting to Flowcus
                val intent = Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    putExtra("BLOCKED_APP", true)
                }
                startActivity(intent)
            }
        }
    }

    override fun onInterrupt() {}
}
