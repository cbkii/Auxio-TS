/*
 * Copyright (c) 2026 Auxio Project
 * PlaybackNotificationChannel.kt is part of Auxio.
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

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import org.oxycblt.auxio.BuildConfig

/** User-controlled playback notification channel state. */
enum class PlaybackChannelState {
    Usable,
    NotCreated,
    Blocked,
}

data class PlaybackChannelSnapshot(
    val state: PlaybackChannelState,
    val packageNotificationsEnabled: Boolean,
    val channelExists: Boolean,
    val importance: Int?,
    val publicationRequestedThisProcess: Boolean,
    val firstPublicationRequestedElapsedMs: Long?,
)

/**
 * Read-only status and settings routing for the playback channel.
 *
 * Android preserves channel importance across app updates. Auxio reports a blocked channel and
 * opens system settings; it never attempts to silently override the user's channel choice.
 */
object PlaybackNotificationChannel {
    val id: String = BuildConfig.APPLICATION_ID + ".channel.PLAYBACK"

    private val publicationRequested = AtomicBoolean(false)
    private val firstPublicationElapsedMs = AtomicLong(UNSET_ELAPSED_MS)

    fun markPublicationRequested() {
        publicationRequested.set(true)
        firstPublicationElapsedMs.compareAndSet(UNSET_ELAPSED_MS, SystemClock.elapsedRealtime())
    }

    fun inspect(context: Context): PlaybackChannelSnapshot {
        val packageEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return PlaybackChannelSnapshot(
                state = classify(packageEnabled, channelExists = true, importance = 1),
                packageNotificationsEnabled = packageEnabled,
                channelExists = true,
                importance = null,
                publicationRequestedThisProcess = publicationRequested.get(),
                firstPublicationRequestedElapsedMs = publicationElapsedMsOrNull(),
            )
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        val channel = manager?.getNotificationChannel(id)
        return PlaybackChannelSnapshot(
            state =
                classify(
                    notificationsEnabled = packageEnabled,
                    channelExists = channel != null,
                    importance = channel?.importance,
                ),
            packageNotificationsEnabled = packageEnabled,
            channelExists = channel != null,
            importance = channel?.importance,
            publicationRequestedThisProcess = publicationRequested.get(),
            firstPublicationRequestedElapsedMs = publicationElapsedMsOrNull(),
        )
    }

    fun settingsIntent(context: Context): Intent {
        val hasChannel =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val manager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                manager?.getNotificationChannel(id) != null
            } else {
                false
            }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && hasChannel) {
            Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                .putExtra(Settings.EXTRA_CHANNEL_ID, id)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        } else {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.fromParts("package", context.packageName, null))
        }
    }

    internal fun classify(
        notificationsEnabled: Boolean,
        channelExists: Boolean,
        importance: Int?,
    ): PlaybackChannelState =
        when {
            !notificationsEnabled -> PlaybackChannelState.Blocked
            !channelExists -> PlaybackChannelState.NotCreated
            importance == NotificationManager.IMPORTANCE_NONE -> PlaybackChannelState.Blocked
            else -> PlaybackChannelState.Usable
        }

    private fun publicationElapsedMsOrNull(): Long? =
        firstPublicationElapsedMs.get().takeUnless { it == UNSET_ELAPSED_MS }

    private const val UNSET_ELAPSED_MS = Long.MIN_VALUE
}
