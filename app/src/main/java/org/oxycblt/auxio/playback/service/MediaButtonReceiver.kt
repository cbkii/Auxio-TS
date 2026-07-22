/*
 * Copyright (c) 2022 Auxio Project
 * MediaButtonReceiver.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.playback.service

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.BadParcelableException
import android.view.KeyEvent
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import org.oxycblt.auxio.AuxioService
import org.oxycblt.auxio.IntegerTable
import org.oxycblt.auxio.headunit.topway.ExportedCommandRateLimiter
import org.oxycblt.auxio.headunit.topway.TopwayServiceBridge
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import timber.log.Timber as L

/**
 * A [BroadcastReceiver] that forwards [Intent.ACTION_MEDIA_BUTTON] [Intent]s to
 * [PlaybackServiceFragment].
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
@AndroidEntryPoint
class MediaButtonReceiver : BroadcastReceiver() {
    @Inject lateinit var playbackManager: PlaybackStateManager

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_MEDIA_BUTTON) return
        val event =
            try {
                IntentCompat.getParcelableExtra(
                    intent,
                    Intent.EXTRA_KEY_EVENT,
                    KeyEvent::class.java,
                )
            } catch (e: BadParcelableException) {
                L.w(e, "Ignoring malformed media-button payload")
                return
            } catch (e: RuntimeException) {
                L.w(e, "Ignoring unreadable media-button payload")
                return
            }

        val hasCurrentSong = playbackManager.currentSong != null
        val isFocusHeld = playbackManager.isAudioFocusHeld
        if (
            !MediaButtonActionMapper.shouldForward(
                event,
                hasCurrentSong = hasCurrentSong,
                isFocusHeld =
                    AudioFocusPolicy.shouldHandleMediaButton(
                        isFocusHeld = isFocusHeld,
                        hasCurrentSong = hasCurrentSong,
                        sessionOngoing = hasCurrentSong,
                    ),
            )
        ) {
            L.d("Ignoring media button event after policy evaluation: $event")
            return
        }
        val keyCode = event?.keyCode ?: return
        if (
            !ExportedCommandRateLimiter.allow(
                key = "media-button:$keyCode",
                maxEvents = MAX_MEDIA_BUTTON_EVENTS_PER_WINDOW,
                windowMs = MEDIA_BUTTON_RATE_WINDOW_MS,
            )
        ) {
            L.w("Dropping excessive media-button events for keyCode=$keyCode")
            return
        }

        val serviceClass = TopwayServiceBridge.resolveCompatServiceClass(AuxioService::class.java)
        val serviceIntent =
            Intent(Intent.ACTION_MEDIA_BUTTON)
                .setComponent(ComponentName(context, serviceClass))
                .putExtra(Intent.EXTRA_KEY_EVENT, event)
                .putExtra(AuxioService.INTENT_KEY_START_ID, IntegerTable.START_ID_MEDIA_BUTTON)
        try {
            ContextCompat.startForegroundService(context, serviceIntent)
        } catch (e: IllegalStateException) {
            L.w(e, "Unable to start Auxio for media-button event")
        } catch (e: SecurityException) {
            L.w(e, "Media-button service start rejected")
        } catch (e: RuntimeException) {
            L.w(e, "Media-button service start failed")
        }
    }

    private companion object {
        const val MAX_MEDIA_BUTTON_EVENTS_PER_WINDOW = 30
        const val MEDIA_BUTTON_RATE_WINDOW_MS = 1_000L
    }
}
