/*
 * Copyright (c) 2026 Auxio Project
 * TopwayBridgePlaybackIngress.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.service

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton
import org.oxycblt.auxio.playback.state.DeferredPlayback
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.auxio.ts18bridge.BridgeWireContract
import timber.log.Timber

internal enum class TopwayBridgeAdmissionResult {
    ACCEPTED,
    NOT_READY,
    INVALID,
    EXPIRED,
    INTERRUPTED,
    ERROR,
}

/**
 * Admission state kept separate from Android scheduling so timeout and interruption ordering is
 * directly unit-testable. ACCEPTED means the validated command has been atomically committed to
 * this Auxio-owned ingress while holding the playback authority's monitor; it deliberately does
 * not mean the synchronous playback mutation has already returned.
 */
internal class TopwayBridgeAdmissionState {
    private val phase = AtomicReference(Phase.PENDING)

    fun start(): Boolean = phase.compareAndSet(Phase.PENDING, Phase.RUNNING)

    @Synchronized
    fun complete(result: TopwayBridgeAdmissionResult): TopwayBridgeAdmissionResult {
        val current = phase.get()
        if (current != Phase.RUNNING) return current.toResult()
        phase.set(result.toTerminalPhase())
        return result
    }

    @Synchronized
    fun expire(): TopwayBridgeAdmissionResult =
        terminateUnfinished(TopwayBridgeAdmissionResult.EXPIRED)

    @Synchronized
    fun interrupt(): TopwayBridgeAdmissionResult =
        terminateUnfinished(TopwayBridgeAdmissionResult.INTERRUPTED)

    fun result(): TopwayBridgeAdmissionResult = phase.get().toResult()

    private fun terminateUnfinished(
        result: TopwayBridgeAdmissionResult
    ): TopwayBridgeAdmissionResult {
        val terminal = result.toTerminalPhase()
        while (true) {
            when (val current = phase.get()) {
                Phase.ACCEPTED -> return TopwayBridgeAdmissionResult.ACCEPTED
                Phase.PENDING,
                Phase.RUNNING -> {
                    if (phase.compareAndSet(current, terminal)) return result
                }
                else -> return current.toResult()
            }
        }
    }

    private fun TopwayBridgeAdmissionResult.toTerminalPhase(): Phase =
        when (this) {
            TopwayBridgeAdmissionResult.ACCEPTED -> Phase.ACCEPTED
            TopwayBridgeAdmissionResult.NOT_READY -> Phase.NOT_READY
            TopwayBridgeAdmissionResult.INVALID -> Phase.INVALID
            TopwayBridgeAdmissionResult.EXPIRED -> Phase.EXPIRED
            TopwayBridgeAdmissionResult.INTERRUPTED -> Phase.INTERRUPTED
            TopwayBridgeAdmissionResult.ERROR -> Phase.ERROR
        }

    private fun Phase.toResult(): TopwayBridgeAdmissionResult =
        when (this) {
            Phase.ACCEPTED -> TopwayBridgeAdmissionResult.ACCEPTED
            Phase.NOT_READY -> TopwayBridgeAdmissionResult.NOT_READY
            Phase.INVALID -> TopwayBridgeAdmissionResult.INVALID
            Phase.EXPIRED -> TopwayBridgeAdmissionResult.EXPIRED
            Phase.INTERRUPTED -> TopwayBridgeAdmissionResult.INTERRUPTED
            Phase.ERROR -> TopwayBridgeAdmissionResult.ERROR
            Phase.PENDING,
            Phase.RUNNING -> TopwayBridgeAdmissionResult.NOT_READY
        }

    private enum class Phase {
        PENDING,
        RUNNING,
        ACCEPTED,
        NOT_READY,
        INVALID,
        EXPIRED,
        INTERRUPTED,
        ERROR,
    }
}

/**
 * Variant-local ingress from the narrow Track-C Binder into Auxio's one playback authority.
 *
 * <p>The Binder thread never owns playback state. It asks the main looper to validate and commit a
 * command while holding the singleton [PlaybackStateManager] monitor, and waits only until that
 * bounded commit. Timeout/interruption can win before commit and prevent execution. Once ACCEPTED
 * is committed, the waiter is released before the synchronous playback mutation runs, so a slow
 * mutation cannot outlive the stock shim's acknowledgement timeout and also trigger stock fallback.
 */
@Singleton
internal class TopwayBridgePlaybackIngress
@Inject
constructor(private val playbackManager: PlaybackStateManager) {
    private val handler = Handler(Looper.getMainLooper())

    fun admitResult(
        commandType: Int,
        seekPositionMs: Long,
        deadlineElapsedMs: Long,
    ): TopwayBridgeAdmissionResult {
        if (!BridgeWireContract.isSupportedCommand(commandType)) {
            return TopwayBridgeAdmissionResult.INVALID
        }
        if (commandType == BridgeWireContract.COMMAND_SEEK && seekPositionMs < 0L) {
            return TopwayBridgeAdmissionResult.INVALID
        }
        if (SystemClock.elapsedRealtime() >= deadlineElapsedMs) {
            return TopwayBridgeAdmissionResult.EXPIRED
        }

        if (Looper.myLooper() == Looper.getMainLooper()) {
            val state = TopwayBridgeAdmissionState()
            if (!state.start()) return TopwayBridgeAdmissionResult.NOT_READY
            return execute(commandType, seekPositionMs, deadlineElapsedMs, state) {}
        }

        val admission = Admission(commandType, seekPositionMs, deadlineElapsedMs)
        if (!handler.post(admission)) return TopwayBridgeAdmissionResult.NOT_READY

        while (true) {
            val remainingMs = deadlineElapsedMs - SystemClock.elapsedRealtime()
            if (remainingMs <= 0L) return admission.state.expire()
            try {
                if (admission.done.await(remainingMs, TimeUnit.MILLISECONDS)) {
                    return admission.state.result()
                }
            } catch (error: InterruptedException) {
                Thread.currentThread().interrupt()
                return admission.state.interrupt()
            }
        }
    }

    private inner class Admission(
        private val commandType: Int,
        private val seekPositionMs: Long,
        private val deadlineElapsedMs: Long,
    ) : Runnable {
        val done = CountDownLatch(1)
        val state = TopwayBridgeAdmissionState()

        override fun run() {
            if (!state.start()) {
                done.countDown()
                return
            }
            try {
                execute(commandType, seekPositionMs, deadlineElapsedMs, state, done::countDown)
            } finally {
                // Idempotent fallback for an unexpected failure before execute() reaches a
                // terminal admission result. Normal accepted/non-accepted paths signal earlier.
                done.countDown()
            }
        }
    }

    private fun execute(
        commandType: Int,
        seekPositionMs: Long,
        deadlineElapsedMs: Long,
        state: TopwayBridgeAdmissionState,
        onCommitted: () -> Unit,
    ): TopwayBridgeAdmissionResult =
        try {
            synchronized(playbackManager) {
                if (SystemClock.elapsedRealtime() >= deadlineElapsedMs) {
                    return@synchronized commit(
                        state,
                        PreparedCommand(TopwayBridgeAdmissionResult.EXPIRED),
                        onCommitted,
                    )
                }

                val prepared = prepare(commandType, seekPositionMs)
                commit(state, prepared, onCommitted)
            }
        } catch (error: RuntimeException) {
            val result = state.complete(TopwayBridgeAdmissionResult.ERROR)
            onCommitted()
            Timber.w(error, "Track-C command admission failed")
            result
        }

    /**
     * Atomically terminalise admission before invoking an accepted mutation.
     *
     * The acknowledgement callback runs immediately after the terminal state is committed and
     * before the synchronous playback call. This is the key boundedness guarantee: a command that
     * reaches ACCEPTED cannot later fall back to stock merely because the playback call is slow.
     */
    private fun commit(
        state: TopwayBridgeAdmissionState,
        prepared: PreparedCommand,
        onCommitted: () -> Unit,
    ): TopwayBridgeAdmissionResult {
        val result = state.complete(prepared.result)
        onCommitted()
        if (result != TopwayBridgeAdmissionResult.ACCEPTED) return result
        if (prepared.result != TopwayBridgeAdmissionResult.ACCEPTED) return result

        try {
            checkNotNull(prepared.action).invoke()
        } catch (error: RuntimeException) {
            // ACCEPTED is a canonical admission acknowledgement, not a completion result. Once
            // committed we must not rewrite it to failure and invite the stock path to duplicate a
            // command that Auxio already owns. Preserve the failure as bounded diagnostics.
            Timber.w(error, "Accepted Track-C playback mutation failed")
        }
        return result
    }

    private fun prepare(commandType: Int, seekPositionMs: Long): PreparedCommand =
        when (commandType) {
            BridgeWireContract.COMMAND_PREVIOUS -> prepareLive { playbackManager.prev() }
            BridgeWireContract.COMMAND_NEXT -> prepareLive { playbackManager.next() }
            BridgeWireContract.COMMAND_PLAY_PAUSE -> preparePlayPause()
            BridgeWireContract.COMMAND_PLAY -> preparePlay()
            BridgeWireContract.COMMAND_PAUSE -> prepareLive { playbackManager.playing(false) }
            BridgeWireContract.COMMAND_SEEK -> prepareLive { playbackManager.seekTo(seekPositionMs) }
            else -> PreparedCommand(TopwayBridgeAdmissionResult.INVALID)
        }

    private fun prepareLive(action: () -> Unit): PreparedCommand {
        if (!hasLivePlayback()) return PreparedCommand(TopwayBridgeAdmissionResult.NOT_READY)
        return PreparedCommand(TopwayBridgeAdmissionResult.ACCEPTED, action)
    }

    private fun preparePlayPause(): PreparedCommand {
        if (hasLivePlayback()) {
            val shouldPlay = !playbackManager.progression.isPlaying
            return PreparedCommand(TopwayBridgeAdmissionResult.ACCEPTED) {
                playbackManager.playing(shouldPlay)
            }
        }
        return PreparedCommand(TopwayBridgeAdmissionResult.ACCEPTED, ::restorePlaying)
    }

    private fun preparePlay(): PreparedCommand =
        if (hasLivePlayback()) {
            PreparedCommand(TopwayBridgeAdmissionResult.ACCEPTED) {
                playbackManager.playing(true)
            }
        } else {
            // Deferred playback is a canonical Auxio-owned action. The ingress commits ownership
            // before invoking PlaybackStateManager so the stock caller is never held open by the
            // synchronous manager call.
            PreparedCommand(TopwayBridgeAdmissionResult.ACCEPTED, ::restorePlaying)
        }

    private fun hasLivePlayback(): Boolean =
        playbackManager.currentAudioSessionId != null &&
            MediaSessionInterface.shouldResumeExistingPlayback(
                hasCurrentSong = playbackManager.currentSong != null,
                hasRawPlaybackMetadata = playbackManager.rawPlaybackMetadata != null,
            )

    private fun restorePlaying() {
        playbackManager.playDeferred(
            DeferredPlayback.RestoreState(play = true, fallback = DeferredPlayback.ShuffleAll())
        )
    }

    private data class PreparedCommand(
        val result: TopwayBridgeAdmissionResult,
        val action: (() -> Unit)? = null,
    )
}
