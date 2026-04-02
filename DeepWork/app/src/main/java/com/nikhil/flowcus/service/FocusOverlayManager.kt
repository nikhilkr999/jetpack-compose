package com.nikhil.flowcus.service

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.*
import android.widget.*

class FocusOverlayManager(private val context: Context) {

    private var windowManager: WindowManager? = null
    private var overlayView: View?            = null

    fun canDrawOverlays(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            Settings.canDrawOverlays(context)
        else true

    fun requestOverlayPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
            )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    fun showOverlay(onReturn: () -> Unit, onStop: () -> Unit) {
        if (!canDrawOverlays() || overlayView != null) return

        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )

        val layout = LinearLayout(context).apply {
            orientation   = LinearLayout.VERTICAL
            gravity       = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#F0111116"))
            layoutParams  = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val iconCircle = TextView(context).apply {
            text     = "🔒"
            textSize = 40f
            gravity  = Gravity.CENTER
            val lp   = LinearLayout.LayoutParams(160, 160).also {
                it.bottomMargin = 32
                it.gravity      = Gravity.CENTER_HORIZONTAL
            }
            layoutParams = lp
            setBackgroundColor(Color.parseColor("#1C1C24"))
        }

        val title = TextView(context).apply {
            text      = "Focus session active"
            textSize  = 24f
            setTextColor(Color.parseColor("#F0EAD8"))
            gravity   = Gravity.CENTER
            typeface  = Typeface.DEFAULT_BOLD
            val lp    = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).also { it.bottomMargin = 12 }
            layoutParams = lp
        }

        val subtitle = TextView(context).apply {
            text      = "You left your focus zone.\nYour session is still running."
            textSize  = 14f
            setTextColor(Color.parseColor("#888888"))
            gravity   = Gravity.CENTER
            val lp    = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).also { it.bottomMargin = 48 }
            layoutParams = lp
        }

        val returnBtn = TextView(context).apply {
            text    = "Return to focus"
            textSize = 15f
            setTextColor(Color.parseColor("#1A1206"))
            gravity = Gravity.CENTER
            setPadding(60, 30, 60, 30)
            setBackgroundColor(Color.parseColor("#E8C47A"))
            val lp  = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).also {
                it.gravity      = Gravity.CENTER_HORIZONTAL
                it.bottomMargin = 16
            }
            layoutParams = lp
            setOnClickListener {
                hideOverlay()
                onReturn()
            }
        }

        val stopBtn = TextView(context).apply {
            text     = "End session"
            textSize = 13f
            setTextColor(Color.parseColor("#888888"))
            gravity  = Gravity.CENTER
            setPadding(40, 20, 40, 20)
            val lp   = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).also { it.gravity = Gravity.CENTER_HORIZONTAL }
            layoutParams = lp
            setOnClickListener {
                hideOverlay()
                onStop()
            }
        }

        layout.addView(iconCircle)
        layout.addView(title)
        layout.addView(subtitle)
        layout.addView(returnBtn)
        layout.addView(stopBtn)

        overlayView = layout
        windowManager?.addView(overlayView, params)
    }

    fun hideOverlay() {
        overlayView?.let {
            try { windowManager?.removeView(it) } catch (e: Exception) { /* already removed */ }
            overlayView = null
        }
    }

    fun isShowing() = overlayView != null
}
