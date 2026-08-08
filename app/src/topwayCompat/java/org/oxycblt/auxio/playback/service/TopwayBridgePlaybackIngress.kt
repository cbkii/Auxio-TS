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
 * directly unit-testable. Terminal timeout/interruption transitions are serialised with the final
 * canonical playback-manager invocation, so the caller cannot be told a request expired and then
 * have that same request mutate playback afterwards.
 */
internal class TopwayBridgeAdmissionState {
    private val phase = AtomicReference(Phase.PENDING)

    fun start(): Boolean = phase.compareAndSet(Phase.PENDING, Phase.RUNNING)

    @Synchronized
    fun executeWhileRunning(
        command: () -> TopwayBridgeAdmissionResult
    ): TopwayBridgeAdmissionResult {
        val current = phase.get()
        if (current != Phase.RUNNING) return current.toResult()
        return try {
            command().also { phase.set(it.toTerminalPhase()) }
        } catch (error: RuntimeException) {
            phase.set(Phase.ERROR)
            throw error
        }
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
 * <p>The Binder thread never owns playback state. It asks the main looper to perform the same
 * singleton [PlaybackStateManager] operations used by the normal MediaSession and Topway adapter,
 * and waits only until the request deadline. A request that expires before its canonical mutation
 * becomes terminally expired and cannot execute later; once a mutation begins, its terminal result
 * is committed under the same admission monitor before timeout/interruption can return.
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
            return execute(commandType, seekPositionMs, deadlineElapsedMs, state)
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
                execute(commandType, seekPositionMs, deadlineElapsedMs, state)
            } finally {
                done.countDown()
            }
        }
    }

    private fun execute(
        commandType: Int,
        seekPositionMs: Long,
        deadlineElapsedMs: Long,
        state: TopwayBridgeAdmissionState,
    ): TopwayBridgeAdmissionResult =
        try {
            synchronized(playbackManager) {
                state.executeWhileRunning {
                    // This check and the mutating call are serialised against expire()/interrupt().
                    // If the waiter wins first the block is never entered; if this block wins, its
                    // canonical result becomes terminal before the waiter can return.
                    if (SystemClock.elapsedRealtime() >= deadlineElapsedMs) {
                        TopwayBridgeAdmissionResult.EXPIRED
                    } else {
                        when (commandType) {
                            BridgeWireContract.COMMAND_PREVIOUS ->
                                withLivePlayback { playbackManager.prev() }
                            BridgeWireContract.COMMAND_NEXT ->
                                withLivePlayback { playbackManager.next() }
                            BridgeWireContract.COMMAND_PLAY_PAUSE -> playPause()
                            BridgeWireContract.COMMAND_PLAY -> play()
                            BridgeWireContract.COMMAND_PAUSE ->
                                withLivePlayback { playbackManager.playing(false) }
                            BridgeWireContract.COMMAND_SEEK ->
                                withLivePlayback { playbackManager.seekTo(seekPositionMs) }
                            else -> TopwayBridgeAdmissionResult.INVALID
                        }
                    }
                }
            }
        } catch (error: RuntimeException) {
            Timber.w(error, "Track-C command admission failed")
            TopwayBridgeAdmissionResult.ERROR
        }

    private inline fun withLivePlayback(command: () -> Unit): TopwayBridgeAdmissionResult {
        if (!hasLivePlayback()) return TopwayBridgeAdmissionResult.NOT_READY
        command()
        return TopwayBridgeAdmissionResult.ACCEPTED
    }

    private fun playPause(): TopwayBridgeAdmissionResult {
        if (hasLivePlayback()) {
            playbackManager.playing(!playbackManager.progression.isPlaying)
        } else {
            // Deferred playback is itself a canonical accepted action: PlaybackStateManager stores
            // it until its one PlaybackStateHolder can consume it.
            restorePlaying()
        }
        return TopwayBridgeAdmissionResult.ACCEPTED
    }

    private fun play(): TopwayBridgeAdmissionResult {
        if (hasLivePlayback()) {
            playbackManager.playing(true)
        } else {
            restorePlaying()
        }
        return TopwayBridgeAdmissionResult.ACCEPTED
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
}
