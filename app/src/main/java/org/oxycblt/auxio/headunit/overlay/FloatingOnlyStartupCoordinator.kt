/*
 * Copyright (c) 2026 Auxio Project
 * FloatingOnlyStartupCoordinator.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.overlay

import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager
import org.oxycblt.auxio.AuxioService
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.IntegerTable
import org.oxycblt.auxio.R
import org.oxycblt.auxio.headunit.topway.TopwayServiceBridge
import org.oxycblt.auxio.playback.service.ForegroundServiceStartContract
import timber.log.Timber as L

/**
 * Starts Auxio's existing playback/library service for floating-only operation without creating a
 * second playback authority or opening [org.oxycblt.auxio.MainActivity].
 *
 * A true floating-only launch uses [restorePlayback] so playback restoration follows the same
 * autoplay policy as a normal launch. Later lifecycle reassertions can use `restorePlayback=false`
 * to ensure the canonical service/library exists without restarting an already healthy track.
 */
object FloatingOnlyStartupCoordinator {
    sealed interface PlaybackStartResult {
        data object StartRequested : PlaybackStartResult

        data object UnsupportedBuild : PlaybackStartResult

        data class StartRejected(val reason: String) : PlaybackStartResult
    }

    data class StartResult(
        val playback: PlaybackStartResult,
        val overlay: CarOverlayContract.OverlayRestoreResult,
    )

    fun start(context: Context, reason: String, restorePlayback: Boolean): StartResult {
        val playback = startPlayback(context, reason, restorePlayback)
        val overlay = TopwayOverlayRestoreBridge.requestOverlayRestore(context, reason)
        L.i(
            "Floating-only startup [$reason] playback=$playback overlay=$overlay restorePlayback=$restorePlayback"
        )
        return StartResult(playback, overlay)
    }

    fun startPlayback(
        context: Context,
        reason: String,
        restorePlayback: Boolean,
    ): PlaybackStartResult {
        if (!BuildConfig.TOPWAY_COMPAT_ENABLED) return PlaybackStartResult.UnsupportedBuild

        val startId =
            if (restorePlayback) IntegerTable.START_ID_BOOT else IntegerTable.START_ID_ACTIVITY
        return try {
            val serviceClass = TopwayServiceBridge.resolveCompatServiceClass(AuxioService::class.java)
            val serviceIntent =
                Intent(context, serviceClass)
                    .setAction(AuxioService.ACTION_START)
                    .putExtra(AuxioService.INTENT_KEY_START_ID, startId)
            ForegroundServiceStartContract.start(context, serviceIntent)
            L.i(
                "Requested headless Auxio service start [$reason] restorePlayback=$restorePlayback"
            )
            PlaybackStartResult.StartRequested
        } catch (e: SecurityException) {
            L.w(e, "Headless Auxio service start denied [$reason]")
            PlaybackStartResult.StartRejected("SecurityException")
        } catch (e: IllegalStateException) {
            L.w(e, "Headless Auxio service start rejected [$reason]")
            PlaybackStartResult.StartRejected("IllegalStateException")
        } catch (e: RuntimeException) {
            L.w(e, "Headless Auxio service start failed [$reason]")
            PlaybackStartResult.StartRejected("RuntimeException")
        }
    }

    fun isConfigured(context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        return isConfigured(
            autostartOnBoot =
                prefs.getBoolean(context.getString(R.string.set_key_autostart_on_boot), false),
            floatingOnly =
                prefs.getBoolean(
                    context.getString(R.string.set_key_autostart_floating_only),
                    false,
                ),
        )
    }

    internal fun isConfigured(autostartOnBoot: Boolean, floatingOnly: Boolean): Boolean =
        autostartOnBoot && floatingOnly
}
