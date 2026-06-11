/*
 * Copyright (c) 2026 Auxio Project
 * TopwayMusicBroadcastBridgeTest.kt is part of Auxio.
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
import androidx.test.core.app.ApplicationProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.runner.RunWith
import org.oxycblt.auxio.ui.UISettings
import org.oxycblt.auxio.ui.accent.Accent
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TopwayMusicBroadcastBridgeTest {
    @Test
    fun `publishProgress clears stale Topway progress when duration becomes unknown`() {
        val context = RecordingContext(ApplicationProvider.getApplicationContext<Context>())
        val bridge =
            TopwayMusicBroadcastBridge(context, FakeUiSettings(headUnitLandscapeMode = true))

        bridge.publishProgress(progressMs = 1_000L, durationMs = 5_000L, nowMs = 1_000L)
        bridge.publishProgress(progressMs = 1_500L, durationMs = 0L, nowMs = 2_000L)
        bridge.publishProgress(progressMs = 1_700L, durationMs = -1L, nowMs = 3_000L)

        val progressBroadcasts =
            context.broadcasts.filter { it.action == TopwayMusicContract.ACTION_PROGRESS_DURATION }
        assertEquals(2, progressBroadcasts.size)
        assertEquals(
            1_000,
            progressBroadcasts[0].getIntExtra(TopwayMusicContract.EXTRA_PROGRESS, -1),
        )
        assertEquals(
            5_000,
            progressBroadcasts[0].getIntExtra(TopwayMusicContract.EXTRA_DURATION, -1),
        )
        assertEquals(0, progressBroadcasts[1].getIntExtra(TopwayMusicContract.EXTRA_PROGRESS, -1))
        assertEquals(0, progressBroadcasts[1].getIntExtra(TopwayMusicContract.EXTRA_DURATION, -1))
    }

    @Test
    fun `publishProgress uses consistent time base for clearing to avoid suppressing future updates`() {
        val context = RecordingContext(ApplicationProvider.getApplicationContext<Context>())
        val bridge =
            TopwayMusicBroadcastBridge(context, FakeUiSettings(headUnitLandscapeMode = true))

        // 1. Publish active progress at deterministic time
        bridge.publishProgress(progressMs = 1_000L, durationMs = 5_000L, nowMs = 10_000L)

        // 2. Publish invalid duration, causing clear, also at deterministic time
        // If this used real SystemClock.elapsedRealtime() internally, it could be > 10_000 +
        // MIN_INTERVAL
        bridge.publishProgress(progressMs = 1_500L, durationMs = 0L, nowMs = 10_100L)

        // 3. Publish active progress again at a time that is > 1000ms after the PREVIOUS
        // deterministic time (10_100)
        // and exceeds the minIntervalMs (1000ms) to ensure it triggers a broadcast under the
        // refined policy.
        // We use 12_200 to be well past 11_100 (10_100 + 1000).
        bridge.publishProgress(progressMs = 2_500L, durationMs = 5_000L, nowMs = 12_200L)

        val progressBroadcasts =
            context.broadcasts.filter { it.action == TopwayMusicContract.ACTION_PROGRESS_DURATION }

        // Should have 3 broadcasts: Active(1000/5000), Clear(0/0), Active(2500/5000)
        assertEquals(3, progressBroadcasts.size, "Should have 3 broadcasts including the recovery")

        assertEquals(
            1_000,
            progressBroadcasts[0].getIntExtra(TopwayMusicContract.EXTRA_PROGRESS, -1),
        )
        assertEquals(0, progressBroadcasts[1].getIntExtra(TopwayMusicContract.EXTRA_PROGRESS, -1))
        assertEquals(
            2_500,
            progressBroadcasts[2].getIntExtra(TopwayMusicContract.EXTRA_PROGRESS, -1),
        )
    }

    private class RecordingContext(base: Context) : ContextWrapper(base) {
        val broadcasts = mutableListOf<Intent>()

        override fun sendBroadcast(intent: Intent) {
            broadcasts += Intent(intent)
        }
    }

    private class FakeUiSettings(override val headUnitLandscapeMode: Boolean) : UISettings {
        override val theme: Int = 0
        override val useBlackTheme: Boolean = false
        override var accent: Accent = Accent.from(Accent.DEFAULT)
        override val roundMode: Boolean = true
        override val driverSide: UISettings.DriverSide = UISettings.DriverSide.RIGHT
        override val largeHeadUnitControls: Boolean = true
        override val showHeadUnitAlbumArt: Boolean = true
        override val showHeadUnitDashboardQuickAccess: Boolean = true
        override val headUnitCompatStatusSummary: String = "test"

        override fun registerListener(listener: UISettings.Listener) = Unit

        override fun unregisterListener(listener: UISettings.Listener) = Unit
    }
}
