/*
 * Copyright (c) 2026 Auxio Project
 * LauncherIntegrationTelemetry.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
 * The DiagnosticJournal already bounds memory/persistence. This helper adds no queue, timer, I/O or
 * dedupe authority; it only annotates events with their integration mode and monotonic timestamp.
 */
@Singleton
class LauncherIntegrationTelemetry
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val journal: DiagnosticJournal,
) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun log(
        category: String,
        event: String,
        origin: String,
        command: String,
        result: String,
        detail: String? = null,
    ) {
        val mode =
            if (BuildConfig.TOPWAY_COMPAT_FLAVOR) {
                Ts18LauncherIntegrationMode.fromPreference(
                    prefs.getString(Ts18LauncherIntegrationMode.PREF_KEY, null)
                ).name
            } else {
                Ts18LauncherIntegrationMode.AndroidMediaSessionOnly.name
            }
        val payload =
            buildString {
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
    }
}
