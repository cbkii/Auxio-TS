/*
 * Copyright (c) 2026 Auxio Project
 * VisualizerCoordinator.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.oxycblt.auxio.playback.ui.visualizer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.audiofx.Visualizer
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.oxycblt.auxio.ui.UISettings
import timber.log.Timber as L

/** Owns the bounded Android visualizer lifecycle for the visible Now Playing panel. */
class VisualizerCoordinator(
    context: Context,
    private val isPlayingFlow: StateFlow<Boolean>,
    private val audioSessionIdFlow: StateFlow<Int?>,
    private val uiSettings: UISettings,
) : DefaultLifecycleObserver, UISettings.Listener {

    private val appContext = context.applicationContext
    private val permissionPreferences =
        appContext.getSharedPreferences(PERMISSION_PREFERENCES, Context.MODE_PRIVATE)

    private val _state = MutableStateFlow<VisualizerState>(VisualizerState.Disabled)
    val state: StateFlow<VisualizerState> = _state.asStateFlow()

    private var visualizer: Visualizer? = null
    private var currentSessionId: Int? = null
    private var watchdogJob: Job? = null
    private var monitorJob: Job? = null
    private var activeScope: CoroutineScope? = null
    private var generation = 0
    private var retryCount = 0
    private var permissionDenied =
        permissionPreferences.getBoolean(KEY_PERMISSION_DENIED, false)
    private var active = false

    override fun onStart(owner: LifecycleOwner) {
        if (active) return
        active = true
        activeScope = owner.lifecycleScope
        uiSettings.registerListener(this)
        monitorJob =
            owner.lifecycleScope.launch {
                launch { isPlayingFlow.collect { updateState() } }
                launch { audioSessionIdFlow.collect { updateState() } }
            }
    }

    override fun onStop(owner: LifecycleOwner) {
        if (!active) return
        active = false
        uiSettings.unregisterListener(this)
        monitorJob?.cancel()
        monitorJob = null
        releaseVisualizer()
        activeScope = null
        _state.value = VisualizerState.Disabled
    }

    override fun onDestroy(owner: LifecycleOwner) {
        if (active) {
            onStop(owner)
        } else {
            monitorJob?.cancel()
            monitorJob = null
            releaseVisualizer()
            activeScope = null
        }
    }

    override fun onVisualizerModeChanged() {
        retryCount = 0
        updateState(forceRestart = true)
    }

    fun onPermissionResult(granted: Boolean) {
        if (granted) {
            retryCount = 0
            setPermissionDenied(false)
            updateState(forceRestart = true)
        } else {
            setPermissionDenied(true)
            releaseVisualizer()
            _state.value = VisualizerState.PermissionDenied
        }
    }

    private fun updateState(forceRestart: Boolean = false) {
        if (!active) return
        if (uiSettings.visualizerMode == UISettings.VisualizerMode.OFF) {
            releaseVisualizer()
            _state.value = VisualizerState.Disabled
            return
        }

        if (!isPlayingFlow.value) {
            releaseVisualizer()
            _state.value = VisualizerState.Paused
            return
        }

        val sessionId = audioSessionIdFlow.value?.takeIf { it > 0 }
        if (sessionId == null) {
            releaseVisualizer()
            _state.value = VisualizerState.AwaitingAudioSession
            return
        }

        val hasPermission =
            ContextCompat.checkSelfPermission(appContext, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED
        if (hasPermission) {
            if (permissionDenied) setPermissionDenied(false)
        } else {
            releaseVisualizer()
            _state.value =
                if (permissionDenied) {
                    VisualizerState.PermissionDenied
                } else {
                    VisualizerState.PermissionRequired
                }
            return
        }

        if (forceRestart || (visualizer != null && currentSessionId != sessionId)) {
            retryCount = 0
            releaseVisualizer()
        }

        if (visualizer == null) {
            startVisualizer(sessionId)
        }
    }

    private fun startVisualizer(sessionId: Int) {
        generation++
        val currentGeneration = generation
        _state.value = VisualizerState.Starting
        var candidateToRelease: Visualizer? = null
        try {
            val captureRange = Visualizer.getCaptureSizeRange()
            require(
                captureRange.size >= 2 && captureRange[0] > 0 && captureRange[1] >= captureRange[0]
            ) {
                "Invalid Visualizer capture-size range"
            }

            val candidate = Visualizer(sessionId)
            candidateToRelease = candidate
            val targetSize = 512.coerceIn(captureRange[0], captureRange[1])
            candidate.captureSize = targetSize
            val maxCaptureRate = Visualizer.getMaxCaptureRate()
            val targetRate = minOf(maxCaptureRate, 30_000).coerceAtLeast(1)
            val scalingMode =
                if (retryCount == 0) Visualizer.SCALING_MODE_AS_PLAYED
                else Visualizer.SCALING_MODE_NORMALIZED
            try {
                candidate.scalingMode = scalingMode
            } catch (e: RuntimeException) {
                L.d(e, "Requested visualizer scaling mode unavailable")
            }

            val lastFftMs = AtomicLong(0L)
            val listenerStatus =
                candidate.setDataCaptureListener(
                    object : Visualizer.OnDataCaptureListener {
                        override fun onWaveFormDataCapture(
                            visualizer: Visualizer,
                            waveform: ByteArray,
                            samplingRate: Int,
                        ) {
                            val now = android.os.SystemClock.uptimeMillis()
                            if (now - lastFftMs.get() < FFT_PREFERENCE_WINDOW_MS) return
                            if (!hasUsableWaveform(waveform)) return
                            publishFrame(
                                currentGeneration,
                                sessionId,
                                waveform,
                                samplingRate,
                                now,
                                VisualizerState.FrameSource.WAVEFORM,
                            )
                        }

                        override fun onFftDataCapture(
                            visualizer: Visualizer,
                            fft: ByteArray,
                            samplingRate: Int,
                        ) {
                            if (!hasUsableFft(fft)) return
                            val now = android.os.SystemClock.uptimeMillis()
                            lastFftMs.set(now)
                            publishFrame(
                                currentGeneration,
                                sessionId,
                                fft,
                                samplingRate,
                                now,
                                VisualizerState.FrameSource.FFT,
                            )
                        }
                    },
                    targetRate,
                    true,
                    true,
                )

            if (listenerStatus != Visualizer.SUCCESS) {
                _state.value =
                    VisualizerState.Unavailable("Listener registration failed: $listenerStatus")
                return
            }

            candidate.enabled = true
            visualizer = candidate
            candidateToRelease = null
            currentSessionId = sessionId
            L.i(
                "Visualizer started session=$sessionId captureSize=$targetSize " +
                    "rate=$targetRate scalingMode=$scalingMode attempt=$retryCount"
            )
            scheduleWatchdog(sessionId, currentGeneration)
        } catch (e: RuntimeException) {
            val message =
                when (e) {
                    is SecurityException -> "Visualizer construction denied"
                    is IllegalArgumentException -> "Visualizer rejected session configuration"
                    is IllegalStateException -> "Visualizer entered invalid state"
                    is UnsupportedOperationException -> "Visualizer unsupported on this device"
                    else -> "Visualizer construction failed"
                }
            L.w(e, "$message for session $sessionId")
            visualizer = null
            currentSessionId = null
            _state.value = VisualizerState.Unavailable(message)
        } finally {
            candidateToRelease?.let(::releaseCandidate)
        }
    }

    private fun publishFrame(
        currentGeneration: Int,
        sessionId: Int,
        frame: ByteArray,
        samplingRate: Int,
        receivedAtUptimeMs: Long,
        source: VisualizerState.FrameSource,
    ) {
        val scope = activeScope ?: return
        val safeFrame = frame.copyOf()
        scope.launch {
            if (
                active &&
                    generation == currentGeneration &&
                    currentSessionId == sessionId
            ) {
                _state.value =
                    VisualizerState.Live(
                        safeFrame,
                        samplingRate,
                        receivedAtUptimeMs,
                        source,
                    )
            }
        }
    }

    private fun scheduleWatchdog(sessionId: Int, currentGeneration: Int) {
        watchdogJob?.cancel()
        val scope = activeScope ?: return
        watchdogJob =
            scope.launch {
                while (true) {
                    delay(VISUALIZER_WATCHDOG_INTERVAL_MS)
                    if (
                        !active ||
                            currentGeneration != generation ||
                            currentSessionId != sessionId
                    ) {
                        return@launch
                    }
                    val currentState = _state.value
                    val now = android.os.SystemClock.uptimeMillis()
                    val hasFreshFrame =
                        currentState is VisualizerState.Live &&
                            now - currentState.receivedAtUptimeMs <= VISUALIZER_STALE_AFTER_MS
                    if (hasFreshFrame) continue

                    if (retryCount < MAX_VISUALIZER_RETRIES) {
                        retryCount++
                        L.w(
                            "Visualizer produced no recent usable frame; retrying " +
                                "session=$sessionId attempt=$retryCount"
                        )
                        watchdogJob = null
                        releaseVisualizer()
                        updateState()
                    } else {
                        _state.value =
                            VisualizerState.Unavailable("No usable FFT or waveform frames")
                    }
                    return@launch
                }
            }
    }

    private fun releaseCandidate(candidate: Visualizer) {
        try {
            if (candidate.enabled) candidate.enabled = false
        } catch (e: RuntimeException) {
            L.d(e, "Visualizer candidate disable failed")
        }
        try {
            candidate.setDataCaptureListener(null, 0, false, false)
        } catch (e: RuntimeException) {
            L.d(e, "Visualizer candidate listener cleanup failed")
        }
        try {
            candidate.release()
        } catch (e: RuntimeException) {
            L.d(e, "Visualizer candidate release failed")
        }
    }

    private fun releaseVisualizer() {
        watchdogJob?.cancel()
        watchdogJob = null
        generation++
        val activeVisualizer = visualizer
        visualizer = null
        currentSessionId = null
        if (activeVisualizer != null) releaseCandidate(activeVisualizer)
    }

    private fun setPermissionDenied(denied: Boolean) {
        permissionDenied = denied
        permissionPreferences.edit().putBoolean(KEY_PERMISSION_DENIED, denied).apply()
    }

    private fun hasUsableFft(fft: ByteArray): Boolean {
        for (index in 2 until fft.size) {
            if (fft[index] != 0.toByte()) return true
        }
        return false
    }

    private fun hasUsableWaveform(waveform: ByteArray): Boolean {
        if (waveform.size < 16) return false
        var min = 255
        var max = 0
        for (sample in waveform) {
            val unsigned = sample.toInt() and 0xFF
            if (unsigned < min) min = unsigned
            if (unsigned > max) max = unsigned
        }
        return max - min >= MIN_WAVEFORM_RANGE
    }

    private companion object {
        const val PERMISSION_PREFERENCES = "visualizer_permission_state"
        const val KEY_PERMISSION_DENIED = "record_audio_denied"
        const val FFT_PREFERENCE_WINDOW_MS = 300L
        const val VISUALIZER_WATCHDOG_INTERVAL_MS = 1_500L
        const val VISUALIZER_STALE_AFTER_MS = 2_000L
        const val MAX_VISUALIZER_RETRIES = 1
        const val MIN_WAVEFORM_RANGE = 4
    }
}
