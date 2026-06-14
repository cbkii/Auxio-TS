/*
 * Copyright (c) 2024 Auxio Project
 * TopwayMusicBroadcastBridge.kt is part of Auxio.
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
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.diagnostics.DiagnosticJournal
import org.oxycblt.auxio.headunit.compat.HeadUnitMetadataSnapshot
import org.oxycblt.auxio.ui.UISettings

class TopwayMusicBroadcastBridge(
    private val context: Context,
    private val uiSettings: UISettings,
    private val journal: DiagnosticJournal? = null,
) {
    private var lastMetadata: HeadUnitMetadataSnapshot? = null
    private var lastProgress: TopwayProgressSnapshot? = null
    private var lastProgressAtMs = 0L

    private val bridgeEnabled: Boolean
        get() = BuildConfig.TOPWAY_COMPAT_FLAVOR || uiSettings.headUnitLandscapeMode

    fun publishMetadata(snapshot: HeadUnitMetadataSnapshot?) {
        if (!bridgeEnabled) return
        if (snapshot == null) {
            clearMetadata()
            return
        }
        if (snapshot == lastMetadata) return
        val intent = TopwayMusicIntentFactory.metadataIntent(snapshot)
        context.sendBroadcast(intent)
        journal?.log(
            DiagnosticJournal.CAT_TOPWAY_BROADCAST,
            "Metadata",
            "Title: ${snapshot.displayTitle}, Artist: ${snapshot.artist}",
        )
        lastMetadata = snapshot
    }

    private fun clearMetadata() {
        if (lastMetadata != null) {
            context.sendBroadcast(TopwayMusicIntentFactory.metadataIntent(null))
            lastMetadata = null
        }
    }

    fun publishProgress(
        progressMs: Long,
        durationMs: Long,
        nowMs: Long = SystemClock.elapsedRealtime(),
    ) {
        if (!bridgeEnabled) return
        val snapshot =
            TopwayProgressStatePolicy.active(progressMs, durationMs)
                ?: run {
                    clearProgress(nowMs)
                    return
                }
        if (
            !TopwayProgressStatePolicy.shouldPublish(
                snapshot,
                lastProgress,
                nowMs,
                lastProgressAtMs,
                MIN_PROGRESS_INTERVAL_MS,
            )
        ) {
            return
        }
        context.sendBroadcast(
            TopwayMusicIntentFactory.progressIntent(snapshot.progressMs, snapshot.durationMs)
        )
        journal?.log(
            DiagnosticJournal.CAT_TOPWAY_BROADCAST,
            "Progress",
            "${snapshot.progressMs} / ${snapshot.durationMs}",
        )
        lastProgress = snapshot
        lastProgressAtMs = nowMs
    }

    fun clear() {
        if (!bridgeEnabled) return
        val nowMs = SystemClock.elapsedRealtime()
        clearMetadata()
        clearProgress(nowMs)
    }

    private fun clearProgress(nowMs: Long) {
        if (lastProgress != TopwayProgressStatePolicy.CLEAR) {
            context.sendBroadcast(TopwayMusicIntentFactory.progressIntent(0L, 0L))
            lastProgress = TopwayProgressStatePolicy.CLEAR
            lastProgressAtMs = nowMs
        }
    }

    fun publishMarker(label: String) {
        if (!bridgeEnabled) return
        val marker =
            HeadUnitMetadataSnapshot(
                displayTitle = "DIAGNOSTIC MARKER",
                displaySubtitle = label,
                artist = label,
                albumArtist = "Auxio-TS",
                albumTitle = "Health Check",
                displayDescription = "TS18 Diagnostic Marker",
                durationMs = 0,
                mediaId = "marker",
                mediaUri = "",
                artworkUri = null,
                hasArtwork = false,
            )
        lastMetadata = marker
        context.sendBroadcast(TopwayMusicIntentFactory.metadataIntent(marker))
        journal?.log(DiagnosticJournal.CAT_TOPWAY_BROADCAST, "Marker Published", label)
    }

    fun restore(snapshot: HeadUnitMetadataSnapshot?) {
        lastMetadata = null // Force re-publish
        publishMetadata(snapshot)
    }

    private companion object {
        const val MIN_PROGRESS_INTERVAL_MS = 2500L
    }
}
