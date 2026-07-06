/*
 * Copyright (c) 2026 Auxio Project
 * TopwayLauncherIntegrationCoordinatorTest.kt is part of Auxio.
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
import android.content.ContextWrapper
import android.content.Intent
import androidx.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.Before
import org.junit.runner.RunWith
import org.oxycblt.auxio.diagnostics.DiagnosticJournal
import org.oxycblt.auxio.headunit.compat.HeadUnitMetadataSnapshot
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TopwayLauncherIntegrationCoordinatorTest {
    private lateinit var baseContext: Context

    @Before
    fun clearPrefs() {
        baseContext = ApplicationProvider.getApplicationContext()
        PreferenceManager.getDefaultSharedPreferences(baseContext).edit().clear().commit()
    }

    @Test
    fun `progress rate limit suppresses periodic duplicate but force bypasses`() {
        val context = RecordingContext(baseContext)
        val coordinator = coordinator(context, Ts18LauncherIntegrationMode.AutoAllSafePaths)

        coordinator.publishProgress(1_000L, 10_000L, reason = "first", nowMs = 1_000L)
        coordinator.publishProgress(1_100L, 10_000L, reason = "rate-limited", nowMs = 1_100L)
        coordinator.publishProgress(
            1_200L,
            10_000L,
            reason = "forced",
            force = true,
            nowMs = 1_200L,
        )

        assertEquals(
            2,
            context.broadcasts.count { it.action == TopwayMusicContract.ACTION_PROGRESS_DURATION },
        )
    }

    @Test
    fun `metadata change publishes immediately`() {
        val context = RecordingContext(baseContext)
        val coordinator = coordinator(context, Ts18LauncherIntegrationMode.AutoAllSafePaths)

        coordinator.publishMetadata(snapshot("One"), reason = "first")
        coordinator.publishMetadata(snapshot("One"), reason = "duplicate")
        coordinator.publishMetadata(snapshot("Two"), reason = "changed")

        assertEquals(
            2,
            context.broadcasts.count { it.action == TopwayMusicContract.ACTION_MUSIC_INFO },
        )
    }

    @Test
    fun `cmd update republishes without toggling playback`() {
        val context = RecordingContext(baseContext)
        val coordinator = coordinator(context, Ts18LauncherIntegrationMode.AutoAllSafePaths)
        val callbacks = RecordingCallbacks(hasCurrentSong = true)

        coordinator.handle(
            Intent(TopwayMusicContract.ACTION_CMD)
                .putExtra(TopwayMusicContract.EXTRA_CMD, TopwayMusicContract.CMD_UPDATE),
            callbacks,
        )

        assertEquals(listOf("update"), callbacks.events)
    }

    @Test
    fun `mode gates incoming commands`() {
        val disabledCallbacks = RecordingCallbacks(hasCurrentSong = true)
        coordinator(RecordingContext(baseContext), Ts18LauncherIntegrationMode.Disabled)
            .handle(Intent(TopwayMusicContract.ACTION_NEXT), disabledCallbacks)
        assertEquals(listOf("ignore"), disabledCallbacks.events)

        val broadcastOnlyCallbacks = RecordingCallbacks(hasCurrentSong = true)
        coordinator(RecordingContext(baseContext), Ts18LauncherIntegrationMode.TopwayBroadcastOnly)
            .handle(Intent(TopwayMusicContract.ACTION_NEXT), broadcastOnlyCallbacks)
        assertEquals(listOf("ignore"), broadcastOnlyCallbacks.events)

        val diagnosticsOnlyCallbacks = RecordingCallbacks(hasCurrentSong = true)
        coordinator(RecordingContext(baseContext), Ts18LauncherIntegrationMode.DiagnosticsOnly)
            .handle(Intent(TopwayMusicContract.ACTION_NEXT), diagnosticsOnlyCallbacks)
        assertEquals(listOf("ignore"), diagnosticsOnlyCallbacks.events)

        val commandOnlyCallbacks = RecordingCallbacks(hasCurrentSong = true)
        coordinator(RecordingContext(baseContext), Ts18LauncherIntegrationMode.TopwayCommandOnly)
            .handle(Intent(TopwayMusicContract.ACTION_NEXT), commandOnlyCallbacks)
        assertEquals(listOf("next"), commandOnlyCallbacks.events)
    }

    @Test
    fun `mode gates outgoing broadcasts`() {
        val disabledContext = RecordingContext(baseContext)
        coordinator(disabledContext, Ts18LauncherIntegrationMode.Disabled)
            .publishProgress(1_000L, 10_000L, reason = "disabled", force = true)
        assertEquals(0, disabledContext.broadcasts.size)

        val commandOnlyContext = RecordingContext(baseContext)
        coordinator(commandOnlyContext, Ts18LauncherIntegrationMode.TopwayCommandOnly)
            .publishProgress(1_000L, 10_000L, reason = "command-only", force = true)
        assertEquals(0, commandOnlyContext.broadcasts.size)

        val diagnosticsOnlyContext = RecordingContext(baseContext)
        coordinator(diagnosticsOnlyContext, Ts18LauncherIntegrationMode.DiagnosticsOnly)
            .publishProgress(1_000L, 10_000L, reason = "diagnostics-only", force = true)
        assertEquals(0, diagnosticsOnlyContext.broadcasts.size)

        val broadcastContext = RecordingContext(baseContext)
        coordinator(broadcastContext, Ts18LauncherIntegrationMode.TopwayBroadcastOnly)
            .publishProgress(1_000L, 10_000L, reason = "broadcast-only", force = true)
        assertEquals(1, broadcastContext.broadcasts.size)
    }

    private fun coordinator(
        context: Context,
        mode: Ts18LauncherIntegrationMode,
    ): TopwayLauncherIntegrationCoordinator =
        TopwayLauncherIntegrationCoordinator(context, DiagnosticJournal()).apply {
            this.mode = mode
        }

    private fun snapshot(title: String) =
        HeadUnitMetadataSnapshot(
            displayTitle = title,
            displaySubtitle = "Artist",
            artist = "Artist",
            albumArtist = "Artist",
            albumTitle = "Album",
            displayDescription = "Description",
            durationMs = 10_000L,
            mediaId = title,
            mediaUri = "content://song/$title",
            artworkUri = null,
            hasArtwork = false,
        )

    private class RecordingContext(base: Context) : ContextWrapper(base) {
        val broadcasts = mutableListOf<Intent>()

        override fun sendBroadcast(intent: Intent) {
            broadcasts += Intent(intent)
        }
    }

    private class RecordingCallbacks(
        override val hasCurrentSong: Boolean,
        override val currentDurationMs: Long? = 10_000L,
    ) : TopwayStartCallbacks {
        val events = mutableListOf<String>()

        override fun previous() {
            events += "previous"
        }

        override fun next() {
            events += "next"
        }

        override fun playPause() {
            events += "playPause"
        }

        override fun widgetUpdate() {
            events += "update"
        }

        override fun seekTo(positionMs: Long) {
            events += "seek:$positionMs"
        }

        override fun ignore() {
            events += "ignore"
        }
    }
}
