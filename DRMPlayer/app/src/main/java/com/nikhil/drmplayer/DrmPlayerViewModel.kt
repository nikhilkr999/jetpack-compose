package com.nikhil.drmplayer

import android.app.Application
import android.content.Context
import android.media.MediaDrm
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.drm.DefaultDrmSessionManager
import androidx.media3.exoplayer.drm.FrameworkMediaDrm
import androidx.media3.exoplayer.drm.HttpMediaDrmCallback
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.nikhil.drmplayer.ui.theme.DrmPlayerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.util.UUID

@UnstableApi
class DrmPlayerViewModel(application: Application) : AndroidViewModel(application) {

    // Free public Widevine test stream — Google Shaka Player test asset
    private val TEST_VIDEO_URL =
        "https://storage.googleapis.com/shaka-demo-assets/angel-one-widevine/dash.mpd"

    // Free public Widevine license server — no auth required, for testing only
    private val LICENSE_URL =
        "https://cwip-shaka-proxy.appspot.com/no_auth"

    // Widevine UUID — always this exact value, industry standard
    private val WIDEVINE_UUID =
        UUID.fromString("edef8ba9-79d6-4ace-a3c8-27dcd51d21ed")

    private val _state = MutableStateFlow(DrmPlayerState())
    val state: StateFlow<DrmPlayerState> = _state.asStateFlow()

    var player: ExoPlayer? = null
        private set

    init {
        detectSecurityLevel()
    }

    // ── Detect Widevine security level ────────────────────────────────────
    // L1 = hardware TEE (real devices, HD/UHD allowed)
    // L3 = software only (emulators, SD only)
    private fun detectSecurityLevel() {
        viewModelScope.launch {
            try {
                val mediaDrm = MediaDrm(WIDEVINE_UUID)
                val level = mediaDrm.getPropertyString("securityLevel")
                mediaDrm.close()
                _state.update { it.copy(securityLevel = level) }
                Log.d("DRM", "Security level detected: $level")
            } catch (e: Exception) {
                _state.update { it.copy(securityLevel = "Unknown") }
                Log.e("DRM", "Security level detection failed", e)
            }
        }
    }

    // ── Initialize ExoPlayer with Widevine DRM ────────────────────────────
    fun initializePlayer(context: Context) {
        if (player != null) return // already initialized

        _state.update { it.copy(statusMessage = "Building DRM session...") }

        // OkHttp handles the HTTP POST of the license challenge
        // to the license server and returns the license response
        val okHttpClient = OkHttpClient.Builder().build()
        val dataSourceFactory = OkHttpDataSource.Factory(okHttpClient)

        // HttpMediaDrmCallback is the bridge between ExoPlayer's CDM
        // and the license server HTTP endpoint
        val drmCallback = HttpMediaDrmCallback(
            LICENSE_URL,
            dataSourceFactory
        )

        // DrmSessionManager wires everything together:
        // CDM <-> license callback <-> ExoPlayer
        val drmSessionManager = DefaultDrmSessionManager.Builder()
            .setUuidAndExoMediaDrmProvider(
                WIDEVINE_UUID,
                FrameworkMediaDrm.DEFAULT_PROVIDER
            )
            .build(drmCallback)

        player = ExoPlayer.Builder(context)
            .setMediaSourceFactory(
                DefaultMediaSourceFactory(context).setDrmSessionManagerProvider { drmSessionManager }
            )
            .build()
            .also { exoPlayer ->

                val mediaItem = MediaItem.Builder()
                    .setUri(TEST_VIDEO_URL)
                    .build()

                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true

                exoPlayer.addListener(object : Player.Listener {

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        val message = when (playbackState) {
                            Player.STATE_BUFFERING ->
                                "Acquiring DRM license from server..."
                            Player.STATE_READY ->
                                "DRM license acquired — playing"
                            Player.STATE_ENDED ->
                                "Playback complete"
                            Player.STATE_IDLE ->
                                "Player idle"
                            else -> ""
                        }
                        _state.update {
                            it.copy(
                                statusMessage = message,
                                isPlaying = playbackState == Player.STATE_READY,
                                isError = false
                            )
                        }
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        // Map specific DRM errors to readable messages
                        // These are the exact errors you will debug in interviews
                        val reason = when (error.errorCode) {
                            PlaybackException.ERROR_CODE_DRM_LICENSE_EXPIRED ->
                                "DRM license expired"
                            PlaybackException.ERROR_CODE_DRM_SCHEME_UNSUPPORTED ->
                                "No DRM scheme found in stream"
                            PlaybackException.ERROR_CODE_DRM_CONTENT_ERROR ->
                                "DRM content error — KID mismatch or corrupt license"
                            PlaybackException.ERROR_CODE_DRM_SYSTEM_ERROR ->
                                "Widevine CDM system error"
                            PlaybackException.ERROR_CODE_DRM_PROVISIONING_FAILED ->
                                "Widevine device provisioning failed"
                            PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW ->
                                "Stream position error"
                            else ->
                                "Playback error: ${error.message}"
                        }
                        _state.update {
                            it.copy(
                                statusMessage = reason,
                                isError = true,
                                isPlaying = false
                            )
                        }
                        Log.e("DRM", "Player error: ${error.errorCode} — ${error.message}")
                    }
                })
            }

        _state.update { it.copy(statusMessage = "DRM session ready — loading stream...") }
    }

    fun releasePlayer() {
        player?.release()
        player = null
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }
}