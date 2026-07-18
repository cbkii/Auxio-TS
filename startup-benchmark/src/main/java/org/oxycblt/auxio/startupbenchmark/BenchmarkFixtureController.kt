/*
 * Copyright (c) 2026 Auxio Project
 * BenchmarkFixtureController.kt is part of Auxio.
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

package org.oxycblt.auxio.startupbenchmark

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.platform.app.InstrumentationRegistry

/** Seeds a deterministic committed database once per benchmark test, outside measured iterations. */
internal object BenchmarkFixtureController {
    private const val RECEIVER = "org.oxycblt.auxio.benchmark.BenchmarkFixtureReceiver"
    private const val ACTION = "org.oxycblt.auxio.action.SEED_BENCHMARK_FIXTURE"
    private val supportedPackages = setOf("org.oxycblt.auxio", "com.tw.music", "com.tw.media")

    val requestedSongCount: Int
        get() =
            InstrumentationRegistry.getArguments()
                .getString("auxio.fixtureSongCount")
                ?.toIntOrNull()
                ?.takeIf { it in BenchmarkFixtures.supportedSongCounts }
                ?: 5_000

    fun MacrobenchmarkScope.seedCommittedFixture(songCount: Int = requestedSongCount) {
        require(songCount in BenchmarkFixtures.supportedSongCounts)
        val packageName = BuildConfig.TARGET_PACKAGE
        require(packageName in supportedPackages)
        startActivityAndWait()
        device.executeShellCommand("am force-stop $packageName")
        val component = "$packageName/$RECEIVER"
        val output =
            device.executeShellCommand(
                "am broadcast -W --include-stopped-packages -a $ACTION " +
                    "-n $component --ei song_count $songCount"
            )
        check(output.contains("result=-1") || output.contains("result=0")) {
            "Fixture seed broadcast failed: $output"
        }
        device.executeShellCommand("am force-stop $packageName")
    }
}
