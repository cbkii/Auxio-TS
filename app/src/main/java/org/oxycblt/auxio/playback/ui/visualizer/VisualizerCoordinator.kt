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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.oxycblt.auxio.ui.UISettings
import timber.log.Timber as L

class VisualizerCoordinator(
    private val context: Context,
    private val isPlayingFlow: StateFlow<Boolean>,
    private val audioSessionIdFlow: StateFlow<Int?>,
    private val uiSettings: UISettings,
) : DefaultLifecycleObserver, UISettings.Listener {

    private val _state = MutableStateFlow<VisualizerState>(VisualizerState.Disabled)
    val state: StateFlow<VisualizerState> = _state.asStateFlow()

    private var visualizer: Visualizer? = null
    private var currentSessionId: Int? = null
    private var watchdogJob: Job? = null
    private var generation = 0
    private var retryCount = 0
    private var permissionDenied = false
    private var active = false

    private var monitorJob: Job? = null

    override fun onStart(owner: LifecycleOwner) {
        active = true
        uiSettings.registerListener(this)
        monitorJob =
            owner.lifecycleScope.launch {
                launch { isPlayingFlow.collect { updateState() } }
                launch { audioSessionIdFlow.collect { updateState() } }
            }
    }

    override fun onStop(owner: LifecycleOwner) {
        active = false
        uiSettings.unregisterListener(this)
        monitorJob?.cancel()
        releaseVisualizer()
    }

    override fun onVisualizerModeChanged() {
        retryCount = 0
        permissionDenied = false
        updateState(forceRestart = true)
    }

    fun onPermissionResult(granted: Boolean) {
        if (granted) {
            retryCount = 0
            permissionDenied = false
            updateState(forceRestart = true)
        } else {
            permissionDenied = true
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

        if (permissionDenied) {
            releaseVisualizer()
            _state.value = VisualizerState.PermissionDenied
            return
        }

        val hasPermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED
        if (!hasPermission) {
            releaseVisualizer()
            _state.value = VisualizerState.PermissionRequired
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
            } catch (e: Exception) {}

            var lastFftMs = 0L
            val listenerStatus =
                candidate.setDataCaptureListener(
                    object : Visualizer.OnDataCaptureListener {
                        override fun onWaveFormDataCapture(
                            v: Visualizer,
                            waveform: ByteArray,
                            rate: Int,
                        ) {
                            val now = android.os.SystemClock.uptimeMillis()
                            if (now - lastFftMs < FFT_PREFERENCE_WINDOW_MS) return
                            if (!hasUsableWaveform(waveform)) return
                            if (generation == currentGeneration && currentSessionId == sessionId) {
                                _state.value =
                                    VisualizerState.Live(
                                        waveform.copyOf(),
                                        rate,
                                        now,
                                        VisualizerState.FrameSource.WAVEFORM,
                                    )
                            }
                        }

                        override fun onFftDataCapture(v: Visualizer, fft: ByteArray, rate: Int) {
                            if (!hasUsableFft(fft)) return
                            val now = android.os.SystemClock.uptimeMillis()
                            lastFftMs = now
                            if (generation == currentGeneration && currentSessionId == sessionId) {
                                _state.value =
                                    VisualizerState.Live(
                                        fft.copyOf(),
                                        rate,
                                        now,
                                        VisualizerState.FrameSource.FFT,
                                    )
                            }
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
                "Visualizer started session=$sessionId captureSize=$targetSize rate=$targetRate attempt=$retryCount"
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
            candidateToRelease?.let { candidate ->
                try {
                    if (candidate.enabled) candidate.enabled = false
                } catch (e: Exception) {}
                try {
                    candidate.setDataCaptureListener(null, 0, false, false)
                } catch (e: Exception) {}
                try {
                    candidate.release()
                } catch (e: Exception) {}
            }
        }
    }

    private fun scheduleWatchdog(sessionId: Int, currentGeneration: Int) {
        watchdogJob?.cancel()
        watchdogJob =
            kotlinx.coroutines
                .CoroutineScope(
                    kotlinx.coroutines.Dispatchers.Main + kotlinx.coroutines.SupervisorJob()
                )
                .launch {
                    while (true) {
                        delay(VISUALIZER_WATCHDOG_INTERVAL_MS)
                        if (currentGeneration != generation || currentSessionId != sessionId)
                            return@launch
                        val currentState = _state.value
                        val now = android.os.SystemClock.uptimeMillis()
                        val hasFreshFrame =
                            currentState is VisualizerState.Live &&
                                (now - currentState.receivedAtUptimeMs <= VISUALIZER_STALE_AFTER_MS)
                        if (hasFreshFrame) continue

                        if (retryCount < MAX_VISUALIZER_RETRIES) {
                            retryCount++
                            L.w(
                                "Visualizer produced no recent usable frame; retrying session=$sessionId attempt=$retryCount"
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

    private fun releaseVisualizer() {
        watchdogJob?.cancel()
        watchdogJob = null
        generation++
        val activeVisualizer = visualizer
        visualizer = null
        currentSessionId = null
        if (activeVisualizer != null) {
            try {
                if (activeVisualizer.enabled) activeVisualizer.enabled = false
            } catch (e: Exception) {}
            try {
                activeVisualizer.setDataCaptureListener(null, 0, false, false)
            } catch (e: Exception) {}
            try {
                activeVisualizer.release()
            } catch (e: Exception) {}
        }
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

    companion object {
        private const val FFT_PREFERENCE_WINDOW_MS = 300L
        private const val VISUALIZER_WATCHDOG_INTERVAL_MS = 1_500L
        private const val VISUALIZER_STALE_AFTER_MS = 2_000L
        private const val MAX_VISUALIZER_RETRIES = 1
        private const val MIN_WAVEFORM_RANGE = 4
    }
}
