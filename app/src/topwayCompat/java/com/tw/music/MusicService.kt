/*
 * Copyright (c) 2026 Auxio Project
 * MusicService.kt is part of Auxio.
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

package com.tw.music

import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import org.oxycblt.auxio.AuxioService
import org.oxycblt.auxio.ts18bridge.IAuxioBridgeCommand
import timber.log.Timber

/**
 * Thin wrapper for the DoFun/Topway launcher entry that expects `com.tw.music.MusicService`.
 *
 * No [dagger.hilt.android.AndroidEntryPoint] annotation is needed here because the parent
 * [org.oxycblt.auxio.AuxioService] is already an `@AndroidEntryPoint` and performs all Hilt
 * injection in its own `onCreate`. Adding the annotation to this subclass would cause KSP to
 * generate a Java injector that cannot resolve this Kotlin class during the Java compilation phase,
 * breaking the `topwayTwMusicDebug` / `topwayTwMediaDebug` builds.
 */
class MusicService : AuxioService() {

    private val handler = Handler(Looper.getMainLooper())

    // Extremely simplistic deduplication to avoid double commands
    private var lastCommandId: Long = -1

    // For seeking, track the requested state, though realistically this service binding
    // might need to inject the proper dependencies to forward to `PlaybackStateManager` or
    // `PlaybackStateHolder`.
    // A full canonical queue integration requires injecting the proper state managers here.

    private val bridgeBinder =
        object : IAuxioBridgeCommand.Stub() {
            override fun dispatchCommand(
                protocolVersion: Int,
                commandId: Long,
                commandType: String,
                seekPos: Long,
                sourceAdapter: String,
                clientGeneration: Long,
                clientTimestamp: Long,
            ): Int {
                // Validate protocol version
                if (protocolVersion != 1) return 8 // RESULT_VERSION_MISMATCH

                // Validate caller UID is System UID (1000) for stock shim
                val callingUid = Binder.getCallingUid()
                if (callingUid != android.os.Process.SYSTEM_UID) {
                    Timber.w("Rejecting untrusted bridge command from UID $callingUid")
                    return 6 // RESULT_UNTRUSTED
                }

                if (commandId == lastCommandId && commandId != 0L) {
                    return 2 // RESULT_DUPLICATE
                }
                lastCommandId = commandId

                // Enqueue operation onto the main thread (canonical playback path expectation)
                handler.post {
                    Timber.d("Bridge command enqueued: type=$commandType, seekPos=$seekPos")
                    val session = sessionToken
                    if (session != null) {
                        val controller =
                            android.support.v4.media.session.MediaControllerCompat(
                                this@MusicService,
                                session,
                            )
                        val transportControls = controller.transportControls
                        when (commandType) {
                            "PREVIOUS" -> transportControls.skipToPrevious()
                            "NEXT" -> transportControls.skipToNext()
                            "PLAY" -> transportControls.play()
                            "PAUSE" -> transportControls.pause()
                            "SEEK" -> if (seekPos >= 0L) transportControls.seekTo(seekPos)
                            "PLAY_PAUSE" ->
                                if (
                                    controller.playbackState?.state ==
                                        android.support.v4.media.session.PlaybackStateCompat
                                            .STATE_PLAYING
                                )
                                    transportControls.pause()
                                else transportControls.play()
                            else -> Timber.w("Unknown bridge command type: $commandType")
                        }
                    }
                }

                return 1 // RESULT_ACCEPTED
            }
        }

    override fun onBind(intent: Intent): IBinder? {
        if (intent.action == "org.oxycblt.auxio.ts18bridge.ACTION_BIND_COMMAND") {
            return bridgeBinder
        }
        return super.onBind(intent)
    }
}
