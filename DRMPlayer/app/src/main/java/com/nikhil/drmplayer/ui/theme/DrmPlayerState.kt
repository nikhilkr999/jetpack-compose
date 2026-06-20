package com.nikhil.drmplayer.ui.theme

data class DrmPlayerState(
    val securityLevel: String = "Detecting...",
    val statusMessage: String = "Initializing...",
    val isError: Boolean = false,
    val isPlaying: Boolean = false
)