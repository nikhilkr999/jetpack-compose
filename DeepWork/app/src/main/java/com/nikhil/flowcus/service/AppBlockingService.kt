package com.nikhil.flowcus.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.nikhil.flowcus.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow

class AppBlockingService : AccessibilityService() {

    companion object {
        val isTimerRunning = MutableStateFlow(false)
        var overlayManager: FocusOverlayManager? = null
        val blockedPackages = mutableSetOf(
            "com.google.android.youtube",
            "com.instagram.android",
            "com.facebook.katana",
            "com.twitter.android",
            "com.zhiliaoapp.musically"
        )
    }

    override fun onServiceConnected() {
        serviceInfo = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
            notificationTimeout = 100
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (!isTimerRunning.value) return
        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return

        val packageName = event.packageName?.toString() ?: return

        // Ignore our own app and system UI
        val allowedPackages = setOf(
            packageName, // our own package is fine
            "com.android.systemui",
            "android"
        )

        // If we are focusing and user tries to open a distracting app or any other app (if App Lock is on)
        // For now, let's stick to blockedPackages or we can implement full lockout logic
        
        if (packageName != applicationContext.packageName && packageName !in setOf("com.android.systemui", "android")) {
            // Show overlay blocker
            overlayManager?.showOverlay(
                onReturn = {
                    val intent = Intent(applicationContext, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    startActivity(intent)
                },
                onStop = {
                    isTimerRunning.value = false
                    // We need a way to communicate back to the TimerService to stop the timer
                    // For now, we'll just hide overlay. The user clicks stop in our app anyway.
                }
            )
        }
    }

    override fun onInterrupt() {}
}
