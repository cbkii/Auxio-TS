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
import android.os.SystemClock
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.oxycblt.auxio.diagnostics.DiagnosticJournal
import org.oxycblt.auxio.ui.UISettings
import timber.log.Timber as L

/** Owns one Android [Visualizer] for the visible playback-panel lifecycle. */
class VisualizerCoordinator(
    private val context: Context,
    private val isPlayingFlow: StateFlow<Boolean>,
    private val audioSessionIdFlow: StateFlow<Int?>,
    private val uiSettings: UISettings,
    diagnosticJournal: DiagnosticJournal? = null,
) : DefaultLifecycleObserver, UISettings.Listener {

    private val _state = MutableStateFlow<VisualizerState>(VisualizerState.Disabled)
    val state: StateFlow<VisualizerState> = _state.asStateFlow()

    private val runtimeMetrics =
        VisualizerRuntimeMetrics(
            diagnosticJournal ?: VisualizerDiagnosticsResolver.resolve(context)
        )
    private val recoveryTracker = VisualizerRecoveryTracker()
    private var visualizer: Visualizer? = null
    private var currentSessionId: Int? = null
    private var watchdogJob: Job? = null
    private var pauseReleaseJob: Job? = null
    private var monitorJob: Job? = null
    private var activeScope: CoroutineScope? = null
    private var pausedAtUptimeMs = VisualizerRecoveryPolicy.UNSET_UPTIME_MS
    private var generation = 0
    private var permissionDenied = uiSettings.visualizerPermissionDenied
    private var permissionRequestIssued = false
    private var active = false

    override fun onStart(owner: LifecycleOwner) {
        if (active) return
        active = true
        activeScope = owner.lifecycleScope
        recoveryTracker.reset()
        if (hasPermission()) {
            clearPersistedPermissionDenial()
            permissionRequestIssued = false
        } else {
            permissionDenied = uiSettings.visualizerPermissionDenied
        }
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
        releaseVisualizer(resetRecovery = true)
        _state.value = VisualizerState.Disabled
        runtimeMetrics.flush("lifecycle_stop", SystemClock.uptimeMillis())
        activeScope = null
    }

    override fun onVisualizerModeChanged() {
        recoveryTracker.reset()
        if (hasPermission()) {
            clearPersistedPermissionDenial()
            permissionRequestIssued = false
        } else {
            permissionDenied = uiSettings.visualizerPermissionDenied
        }
        updateState(forceRestart = true)
    }

    /** Claims the one permission request allowed for the current unresolved permission state. */
    fun claimPermissionRequest(): Boolean {
        if (!active || permissionDenied || permissionRequestIssued) return false
        if (_state.value !is VisualizerState.PermissionRequired) return false
        permissionRequestIssued = true
        return true
    }

    fun onPermissionResult(granted: Boolean) {
        permissionRequestIssued = false
        if (granted) {
            recoveryTracker.reset()
            clearPersistedPermissionDenial()
            updateState(forceRestart = true)
        } else {
            permissionDenied = true
            uiSettings.visualizerPermissionDenied = true
            releaseVisualizer(resetRecovery = true)
            _state.value = VisualizerState.PermissionDenied
        }
    }

    private fun clearPersistedPermissionDenial() {
        permissionDenied = false
        if (uiSettings.visualizerPermissionDenied) {
            uiSettings.visualizerPermissionDenied = false
        }
    }

    private fun updateState(forceRestart: Boolean = false) {
        if (!active) return
        if (uiSettings.visualizerMode == UISettings.VisualizerMode.OFF) {
            permissionRequestIssued = false
            releaseVisualizer(resetRecovery = true)
            _state.value = VisualizerState.Disabled
            return
        }

        val sessionId = audioSessionIdFlow.value?.takeIf { it > 0 }
        if (!isPlayingFlow.value) {
            if (visualizer != null && currentSessionId != sessionId) {
                releaseVisualizer(resetRecovery = true)
            }
            pauseVisualizer()
            _state.value = VisualizerState.Paused
            return
        }

        if (sessionId == null) {
            releaseVisualizer(resetRecovery = true)
            _state.value = VisualizerState.AwaitingAudioSession
            return
        }

        if (!hasPermission()) {
            releaseVisualizer(resetRecovery = true)
            permissionDenied = permissionDenied || uiSettings.visualizerPermissionDenied
            _state.value =
                if (permissionDenied) VisualizerState.PermissionDenied
                else VisualizerState.PermissionRequired
            return
        }
        clearPersistedPermissionDenial()
        permissionRequestIssued = false

        if (forceRestart || (visualizer != null && currentSessionId != sessionId)) {
            recoveryTracker.reset()
            releaseVisualizer(resetRecovery = false)
        }

        if (visualizer != null) {
            if (_state.value is VisualizerState.Paused && !resumeRetainedVisualizer(sessionId)) {
                startVisualizer(sessionId)
            }
            return
        }

        startVisualizer(sessionId)
    }

    private fun pauseVisualizer() {
        val candidate = visualizer
        if (candidate == null) {
            recoveryTracker.reset()
            pausedAtUptimeMs = VisualizerRecoveryPolicy.UNSET_UPTIME_MS
            return
        }
        if (_state.value is VisualizerState.Paused && pauseReleaseJob != null) return

        watchdogJob?.cancel()
        watchdogJob = null
        recoveryTracker.reset()
        pausedAtUptimeMs = SystemClock.uptimeMillis()
        try {
            if (candidate.enabled) candidate.enabled = false
        } catch (error: RuntimeException) {
            L.w(error, "Visualizer disable on pause failed; releasing session")
            releaseVisualizer(resetRecovery = true)
            return
        }

        val scope = activeScope
        if (scope == null) {
            releaseVisualizer(resetRecovery = true)
            return
        }
        val retainedGeneration = generation
        val retainedSessionId = currentSessionId
        pauseReleaseJob?.cancel()
        pauseReleaseJob =
            scope.launch {
                delay(VisualizerRecoveryPolicy.PAUSE_RETAIN_MS)
                if (
                    active &&
                        !isPlayingFlow.value &&
                        generation == retainedGeneration &&
                        currentSessionId == retainedSessionId &&
                        visualizer === candidate
                ) {
                    pauseReleaseJob = null
                    releaseVisualizer(resetRecovery = true)
                    _state.value = VisualizerState.Paused
                }
            }
    }

    private fun resumeRetainedVisualizer(sessionId: Int): Boolean {
        val candidate = visualizer ?: return false
        val now = SystemClock.uptimeMillis()
        if (
            !VisualizerRecoveryPolicy.canReusePausedSession(
                currentSessionId,
                sessionId,
                pausedAtUptimeMs,
                now,
            )
        ) {
            releaseVisualizer(resetRecovery = true)
            return false
        }

        pauseReleaseJob?.cancel()
        pauseReleaseJob = null
        pausedAtUptimeMs = VisualizerRecoveryPolicy.UNSET_UPTIME_MS
        recoveryTracker.reset()
        recoveryTracker.beginAttempt(now)
        _state.value = VisualizerState.Starting
        return try {
            candidate.enabled = true
            scheduleWatchdog(sessionId, generation)
            L.d("Visualizer resumed retained session=$sessionId")
            true
        } catch (error: RuntimeException) {
            L.w(error, "Visualizer retained-session resume failed for session $sessionId")
            releaseVisualizer(resetRecovery = true)
            false
        }
    }

    private fun startVisualizer(sessionId: Int) {
        generation++
        val currentGeneration = generation
        recoveryTracker.beginAttempt(SystemClock.uptimeMillis())
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
            val targetRate = minOf(Visualizer.getMaxCaptureRate(), 30_000).coerceAtLeast(1)
            val scalingMode =
                if (recoveryTracker.consecutiveRetries == 0) Visualizer.SCALING_MODE_AS_PLAYED
                else Visualizer.SCALING_MODE_NORMALIZED
            try {
                candidate.scalingMode = scalingMode
            } catch (error: RuntimeException) {
                L.d(error, "Requested visualizer scaling mode unavailable")
            }

            var lastFftMs = 0L
            val listenerStatus =
                candidate.setDataCaptureListener(
                    object : Visualizer.OnDataCaptureListener {
                        override fun onWaveFormDataCapture(
                            visualizer: Visualizer,
                            waveform: ByteArray,
                            samplingRate: Int,
                        ) {
                            if (!VisualizerRecoveryPolicy.hasUsableSamplingRate(samplingRate)) return
                            val now = SystemClock.uptimeMillis()
                            if (now - lastFftMs < FFT_PREFERENCE_WINDOW_MS) {
                                runtimeMetrics.recordSuppressedWaveform()
                                return
                            }
                            if (!hasUsableWaveform(waveform)) return
                            if (generation != currentGeneration || currentSessionId != sessionId) return
                            recoveryTracker.noteUsableFrame(now)
                            val frame =
                                if (runtimeMetrics.isActive) {
                                    val copyStart = SystemClock.elapsedRealtimeNanos()
                                    waveform.copyOf().also { copy ->
                                        runtimeMetrics.recordFrame(
                                            copy.size,
                                            SystemClock.elapsedRealtimeNanos() - copyStart,
                                            now,
                                        )
                                    }
                                } else {
                                    waveform.copyOf()
                                }
                            _state.value =
                                VisualizerState.Live(
                                    frame,
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
                            if (!VisualizerRecoveryPolicy.hasUsableSamplingRate(samplingRate)) return
                            if (!hasUsableFft(fft)) return
                            val now = SystemClock.uptimeMillis()
                            if (generation != currentGeneration || currentSessionId != sessionId) return
                            lastFftMs = now
                            recoveryTracker.noteUsableFrame(now)
                            val frame =
                                if (runtimeMetrics.isActive) {
                                    val copyStart = SystemClock.elapsedRealtimeNanos()
                                    fft.copyOf().also { copy ->
                                        runtimeMetrics.recordFrame(
                                            copy.size,
                                            SystemClock.elapsedRealtimeNanos() - copyStart,
                                            now,
                                        )
                                    }
                                } else {
                                    fft.copyOf()
                                }
                            _state.value =
                                VisualizerState.Live(
                                    frame,
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

            visualizer = candidate
            currentSessionId = sessionId
            try {
                candidate.enabled = true
            } catch (error: RuntimeException) {
                visualizer = null
                currentSessionId = null
                throw error
            }
            candidateToRelease = null
            L.i(
                "Visualizer started session=$sessionId captureSize=$targetSize " +
                    "rate=$targetRate attempt=${recoveryTracker.consecutiveRetries}"
            )
            scheduleWatchdog(sessionId, currentGeneration)
        } catch (error: RuntimeException) {
            val message =
                when (error) {
                    is SecurityException -> "Visualizer construction denied"
                    is IllegalArgumentException -> "Visualizer rejected session configuration"
                    is IllegalStateException -> "Visualizer entered invalid state"
                    is UnsupportedOperationException -> "Visualizer unsupported on this device"
                    else -> "Visualizer construction failed"
                }
            L.w(error, "$message for session $sessionId")
            visualizer = null
            currentSessionId = null
            _state.value = VisualizerState.Unavailable(message)
        } finally {
            candidateToRelease?.let(::releaseCandidate)
        }
    }

    private fun scheduleWatchdog(sessionId: Int, currentGeneration: Int) {
        watchdogJob?.cancel()
        val scope = activeScope
        if (scope == null) {
            releaseVisualizer(resetRecovery = true)
            _state.value = VisualizerState.Unavailable("Visualizer lifecycle is unavailable")
            return
        }
        watchdogJob =
            scope.launch {
                while (currentGeneration == generation && currentSessionId == sessionId) {
                    delay(VisualizerRecoveryPolicy.WATCHDOG_INTERVAL_MS)
                    if (currentGeneration != generation || currentSessionId != sessionId) {
                        return@launch
                    }
                    val now = SystemClock.uptimeMillis()
                    if (!recoveryTracker.isTimedOut(now)) continue

                    if (recoveryTracker.consumeRetry(MAX_VISUALIZER_RETRIES)) {
                        runtimeMetrics.recordWatchdogRetry()
                        val timeoutKind =
                            if (
                                recoveryTracker.lastUsableFrameAtUptimeMs ==
                                    VisualizerRecoveryPolicy.UNSET_UPTIME_MS
                            ) {
                                "first frame"
                            } else {
                                "live frame"
                            }
                        L.w(
                            "Visualizer $timeoutKind timeout; retrying " +
                                "session=$sessionId attempt=${recoveryTracker.consecutiveRetries}"
                        )
                        releaseVisualizer(resetRecovery = false)
                        updateState()
                    } else {
                        releaseVisualizer(resetRecovery = false)
                        _state.value =
                            VisualizerState.Unavailable("No usable FFT or waveform frames")
                    }
                    return@launch
                }
            }
    }

    private fun releaseVisualizer(resetRecovery: Boolean) {
        watchdogJob?.cancel()
        watchdogJob = null
        pauseReleaseJob?.cancel()
        pauseReleaseJob = null
        pausedAtUptimeMs = VisualizerRecoveryPolicy.UNSET_UPTIME_MS
        generation++
        val activeVisualizer = visualizer
        visualizer = null
        currentSessionId = null
        if (activeVisualizer != null) releaseCandidate(activeVisualizer)
        if (resetRecovery) recoveryTracker.reset()
    }

    private fun releaseCandidate(candidate: Visualizer) {
        try {
            if (candidate.enabled) candidate.enabled = false
        } catch (error: RuntimeException) {
            L.d(error, "Visualizer disable during release failed")
        }
        try {
            candidate.setDataCaptureListener(null, 0, false, false)
        } catch (error: RuntimeException) {
            L.d(error, "Visualizer listener cleanup failed")
        }
        try {
            candidate.release()
        } catch (error: RuntimeException) {
            L.d(error, "Visualizer native release failed")
        }
    }

    private fun hasPermission() =
        ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
            PackageManager.PERMISSION_GRANTED

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
        const val FFT_PREFERENCE_WINDOW_MS = 300L
        const val MAX_VISUALIZER_RETRIES = 1
        const val MIN_WAVEFORM_RANGE = 4
    }
}
