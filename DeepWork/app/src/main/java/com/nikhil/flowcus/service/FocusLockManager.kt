package com.nikhil.flowcus.service

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Build

object FocusLockManager {

    fun lockToApp(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startLockTask()
        }
    }

    fun unlockFromApp(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                activity.stopLockTask()
            } catch (e: Exception) {
                // Already unlocked
            }
        }
    }

    fun isLocked(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return am.lockTaskModeState != ActivityManager.LOCK_TASK_MODE_NONE
    }
}
