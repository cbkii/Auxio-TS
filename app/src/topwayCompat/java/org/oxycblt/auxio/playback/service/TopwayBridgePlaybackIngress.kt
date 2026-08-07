/*
 * Copyright (c) 2026 Auxio Project
 * TopwayBridgePlaybackIngress.kt is part of Auxio-TS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

/**
 * Variant-local ingress from the narrow Track-C Binder into Auxio's one playback authority.
 *
 * <p>The Binder thread never owns playback state. It asks the main looper to perform the same
 * singleton [PlaybackStateManager] operations used by the normal MediaSession and Topway adapter,
 * and waits only until the request deadline. A request that is still pending at its deadline is
 * cancelled before it can execute later.
 */
@Singleton
internal class TopwayBridgePlaybackIngress
@Inject
constructor(private val playbackManager: PlaybackStateManager) {
    private val handler = Handler(Looper.getMainLooper())

    fun admit(commandType: Int, seekPositionMs: Long, deadlineElapsedMs: Long): Boolean {
        if (!BridgeWireContract.isSupportedCommand(commandType)) return false
        if (commandType == BridgeWireContract.COMMAND_SEEK && seekPositionMs < 0L) return false
        if (SystemClock.elapsedRealtime() >= deadlineElapsedMs) return false

        if (Looper.myLooper() == Looper.getMainLooper()) {
            return execute(commandType, seekPositionMs)
        }

        val admission = Admission(commandType, seekPositionMs)
        if (!handler.post(admission)) return false

        while (true) {
            val remainingMs = deadlineElapsedMs - SystemClock.elapsedRealtime()
            if (remainingMs <= 0L) {
                return admission.cancelPendingOrAccepted()
            }
            try {
                if (admission.done.await(remainingMs, TimeUnit.MILLISECONDS)) {
                    return admission.accepted()
                }
            } catch (error: InterruptedException) {
                Thread.currentThread().interrupt()
                return admission.cancelPendingOrAccepted()
            }
        }
    }

    private inner class Admission(
        private val commandType: Int,
        private val seekPositionMs: Long,
    ) : Runnable {
        val done = CountDownLatch(1)
        private val state = AtomicReference(State.PENDING)

        override fun run() {
            if (!state.compareAndSet(State.PENDING, State.RUNNING)) {
                done.countDown()
                return
            }
            try {
                state.set(if (execute(commandType, seekPositionMs)) State.ACCEPTED else State.FAILED)
            } finally {
                done.countDown()
            }
        }

        fun accepted(): Boolean = state.get() == State.ACCEPTED

        fun cancelPendingOrAccepted(): Boolean {
            if (state.compareAndSet(State.PENDING, State.CANCELLED)) return false
            return when (state.get()) {
                State.RUNNING, State.ACCEPTED -> true
                State.PENDING, State.FAILED, State.CANCELLED -> false
            }
        }
    }

    private fun execute(commandType: Int, seekPositionMs: Long): Boolean =
        try {
            when (commandType) {
                BridgeWireContract.COMMAND_PREVIOUS -> playbackManager.prev()
                BridgeWireContract.COMMAND_NEXT -> playbackManager.next()
                BridgeWireContract.COMMAND_PLAY_PAUSE -> playPause()
                BridgeWireContract.COMMAND_PLAY -> play()
                BridgeWireContract.COMMAND_PAUSE -> playbackManager.playing(false)
                BridgeWireContract.COMMAND_SEEK -> playbackManager.seekTo(seekPositionMs)
                else -> return false
            }
            true
        } catch (error: RuntimeException) {
            Timber.w(error, "Track-C command admission failed")
            false
        }

    private fun playPause() {
        if (hasRestorablePlayback()) {
            playbackManager.playing(!playbackManager.progression.isPlaying)
        } else {
            restorePlaying()
        }
    }

    private fun play() {
        if (hasRestorablePlayback()) {
            playbackManager.playing(true)
        } else {
            restorePlaying()
        }
    }

    private fun hasRestorablePlayback(): Boolean =
        playbackManager.currentSong != null || playbackManager.rawPlaybackMetadata != null

    private fun restorePlaying() {
        playbackManager.playDeferred(
            DeferredPlayback.RestoreState(play = true, fallback = DeferredPlayback.ShuffleAll())
        )
    }

    private enum class State {
        PENDING,
        RUNNING,
        ACCEPTED,
        FAILED,
        CANCELLED,
    }
}
