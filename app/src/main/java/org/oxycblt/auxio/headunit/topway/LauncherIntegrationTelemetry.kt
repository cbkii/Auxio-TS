/*
 * Copyright (c) 2026 Auxio Project
 * LauncherIntegrationTelemetry.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.topway

import android.content.Context
import android.os.SystemClock
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.diagnostics.DiagnosticJournal
import timber.log.Timber as L

/**
 * Small, bounded event seam for comparing DoFun/Topway command ingress routes on an exact TS18.
 *
 * The DiagnosticJournal bounds retained memory and files, but its debug persistence executor uses a
 * normal task queue. This helper therefore rate-limits all newly added launcher telemetry before
 * journal submission. It adds no playback, dedupe or control authority.
 */
@Singleton
class LauncherIntegrationTelemetry
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val journal: DiagnosticJournal,
) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val submissionLimiter = EventRateLimiter(SystemClock::elapsedRealtime)

    fun log(
        category: String,
        event: String,
        origin: String,
        command: String,
        result: String,
        detail: String? = null,
    ) {
        if (
            !submissionLimiter.allow(
                key = TELEMETRY_LIMITER_KEY,
                maxEvents = MAX_TELEMETRY_EVENTS_PER_WINDOW,
                windowMs = TELEMETRY_WINDOW_MS,
            )
        ) {
            return
        }

        val mode =
            if (BuildConfig.TOPWAY_COMPAT_ENABLED) {
                Ts18LauncherIntegrationMode.fromPreference(
                        prefs.getString(Ts18LauncherIntegrationMode.PREF_KEY, null)
                    )
                    .name
            } else {
                Ts18LauncherIntegrationMode.AndroidMediaSessionOnly.name
            }
        val payload = buildString {
            append("elapsedMs=")
            append(SystemClock.elapsedRealtime())
            append(" origin=")
            append(origin)
            append(" command=")
            append(command)
            append(" mode=")
            append(mode)
            if (!detail.isNullOrBlank()) {
                append(" detail=")
                append(detail.take(MAX_DETAIL_CHARS))
            }
        }
        journal.log(category, event, payload, result)
        L.d("Launcher integration telemetry: $event $payload result=$result")
    }

    private companion object {
        const val MAX_DETAIL_CHARS = 320
        const val TELEMETRY_LIMITER_KEY = "launcher-integration-telemetry"
        const val MAX_TELEMETRY_EVENTS_PER_WINDOW = 16
        const val TELEMETRY_WINDOW_MS = 1_000L
    }
}

