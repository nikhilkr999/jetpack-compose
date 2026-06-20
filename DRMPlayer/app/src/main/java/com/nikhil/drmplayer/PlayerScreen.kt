package com.nikhil.drmplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView

@UnstableApi
@Composable
fun PlayerScreen(viewModel: DrmPlayerViewModel) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Initialize and release player tied to lifecycle
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> viewModel.initializePlayer(context)
                Lifecycle.Event.ON_STOP -> viewModel.releasePlayer()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewModel.releasePlayer()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
    ) {

        // ── Top bar ───────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "DRM Player",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // Security level badge — this is the L1 / L3 indicator
            SecurityLevelBadge(level = state.securityLevel)
        }

        // ── Video player ──────────────────────────────────────────────────
        // AndroidView is how you embed a View-based component in Compose
        // ExoPlayer's PlayerView is still View-based — this is the bridge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        useController = true
                        keepScreenOn = true
                    }
                },
                update = { playerView ->
                    // Attach or detach the ExoPlayer instance
                    playerView.player = viewModel.player
                },
                modifier = Modifier.fillMaxSize()
            )

            // Show spinner while buffering
            if (state.statusMessage.contains("Acquiring") ||
                state.statusMessage.contains("loading") ||
                state.statusMessage.contains("Building")
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = Color(0xFF1DB954),
                    strokeWidth = 3.dp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Status card ───────────────────────────────────────────────────
        StatusCard(
            message = state.statusMessage,
            isError = state.isError
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── DRM info panel ────────────────────────────────────────────────
        DrmInfoPanel()
    }
}

// ── Security level badge ──────────────────────────────────────────────────────
@Composable
fun SecurityLevelBadge(level: String) {
    val isL1 = level.contains("L1", ignoreCase = true)
    val badgeColor = if (isL1) Color(0xFF1B5E20) else Color(0xFF7D5A00)
    val textColor = if (isL1) Color(0xFF69F0AE) else Color(0xFFFFD54F)

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = badgeColor)
    ) {
        Text(
            text = "Widevine $level",
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

// ── Status card ───────────────────────────────────────────────────────────────
@Composable
fun StatusCard(message: String, isError: Boolean) {
    val bgColor = if (isError) Color(0xFF3E1A1A) else Color(0xFF1A1A2E)
    val textColor = if (isError) Color(0xFFEF9A9A) else Color(0xFF90CAF9)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isError) "✕ " else "● ",
                color = textColor,
                fontSize = 14.sp
            )
            Text(
                text = message,
                color = textColor,
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

// ── DRM info panel ────────────────────────────────────────────────────────────
// Shows key DRM facts — makes the app look intentional, not a tutorial
@Composable
fun DrmInfoPanel() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111111))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "DRM Session Info",
                color = Color(0xFF888888),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            DrmInfoRow("Scheme", "Widevine (EDEF8BA9-79D6-4ACE-A3C8-27DCD51D21ED)")
            DrmInfoRow("Stream", "DASH / MPD")
            DrmInfoRow("Encryption", "AES-128-CTR (CENC)")
            DrmInfoRow("License Server", "Shaka Proxy (test)")
            DrmInfoRow("Key Scope", "Per content (single KID)")
        }
    }
}

@Composable
fun DrmInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color(0xFF666666),
            fontSize = 12.sp,
            modifier = Modifier.weight(0.35f)
        )
        Text(
            text = value,
            color = Color(0xFFCCCCCC),
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.weight(0.65f)
        )
    }
}