/*
 * Copyright (c) 2026 Auxio Project
 * DiagnosticMarkerController.kt is part of Auxio.
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

package org.oxycblt.auxio.diagnostics

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import org.oxycblt.auxio.headunit.compat.HeadUnitMetadataPolicy
import org.oxycblt.auxio.headunit.topway.TopwayMusicBroadcastBridge
import org.oxycblt.auxio.music.resolve
import org.oxycblt.auxio.music.resolveNames
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.auxio.ui.UISettings

/** Internal-only publisher for guided diagnostic metadata markers. */
@Singleton
class DiagnosticMarkerController
@Inject
constructor(
    @ApplicationContext context: Context,
    uiSettings: UISettings,
    private val journal: DiagnosticJournal,
    private val playbackManager: PlaybackStateManager,
) {
    private val bridge = TopwayMusicBroadcastBridge(context, uiSettings, journal)
    private val appContext = context

    fun publishMarker(label: String) {
        bridge.publishMarker(label)
    }

    fun restoreCurrentMetadata() {
        val song = playbackManager.currentSong
        if (song == null) {
            bridge.restore(null)
            journal.log(
                DiagnosticJournal.CAT_TOPWAY_BROADCAST,
                "Marker restored",
                "No current song",
            )
            return
        }

        val snapshot =
            HeadUnitMetadataPolicy.fromRaw(
                title = song.name.resolve(appContext),
                artist = song.artists.resolveNames(appContext),
                albumArtist = song.album.artists.resolveNames(appContext),
                albumTitle = song.album.name.resolve(appContext),
                durationMs = song.durationMs,
                mediaId = song.uid.toString(),
                mediaUri = song.uri.toString(),
                artworkUri = null,
                hasArtwork = false,
            )
        bridge.restore(snapshot)
        journal.log(
            DiagnosticJournal.CAT_TOPWAY_BROADCAST,
            "Marker restored",
            song.uid.toString(),
        )
    }
}
