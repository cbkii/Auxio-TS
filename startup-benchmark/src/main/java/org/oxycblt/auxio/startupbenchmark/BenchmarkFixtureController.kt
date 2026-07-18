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

import android.util.Base64
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice

/** Seeds deterministic committed state and retrieves bounded app-process timing evidence. */
internal object BenchmarkFixtureController {
    private const val RECEIVER = "org.oxycblt.auxio.benchmark.BenchmarkFixtureReceiver"
    private const val ACTION_SEED = "org.oxycblt.auxio.action.SEED_BENCHMARK_FIXTURE"
    private const val ACTION_REPORT = "org.oxycblt.auxio.action.EXPORT_BENCHMARK_REPORT"
    const val SOURCE_MODE_NORMAL = "normal"
    const val SOURCE_MODE_USB1_ABSENT = "usb1_absent"
    const val SOURCE_MODE_PENDING = "pending_generation"
    private val supportedPackages = setOf("org.oxycblt.auxio", "com.tw.music", "com.tw.media")

    val requestedSongCount: Int
        get() =
            InstrumentationRegistry.getArguments()
                .getString("auxio.fixtureSongCount")
                ?.toIntOrNull()
                ?.takeIf { it in BenchmarkFixtures.supportedSongCounts } ?: 5_000

    val requestedIterations: Int
        get() =
            InstrumentationRegistry.getArguments()
                .getString("auxio.iterations")
                ?.toIntOrNull()
                ?.coerceIn(3, 30) ?: 7

    fun MacrobenchmarkScope.seedCommittedFixture(
        songCount: Int = requestedSongCount,
        sourceMode: String = SOURCE_MODE_NORMAL,
    ) {
        require(songCount in BenchmarkFixtures.supportedSongCounts)
        require(
            sourceMode in setOf(SOURCE_MODE_NORMAL, SOURCE_MODE_USB1_ABSENT, SOURCE_MODE_PENDING)
        )
        val packageName = BuildConfig.TARGET_PACKAGE
        require(packageName in supportedPackages)
        startActivityAndWait()
        device.executeShellCommand("am force-stop $packageName")
        val component = "$packageName/$RECEIVER"
        val output =
            device.executeShellCommand(
                "am broadcast -W --include-stopped-packages -a $ACTION_SEED " +
                    "-n $component --ei song_count $songCount --es source_mode $sourceMode"
            )
        check(Regex("result=-1(?:,|\\s)").containsMatchIn(output)) {
            "Fixture seed broadcast failed: $output"
        }
        device.executeShellCommand("am force-stop $packageName")
    }

    fun captureStartupReport(
        device: UiDevice,
        requiredLabels: Set<String> =
            setOf("Application.onCreate:start", "Application.onCreate:end"),
    ): String {
        val packageName = BuildConfig.TARGET_PACKAGE
        val component = "$packageName/$RECEIVER"
        val output =
            device.executeShellCommand(
                "am broadcast -W --include-stopped-packages -a $ACTION_REPORT -n $component"
            )
        check(Regex("result=-1(?:,|\\s)").containsMatchIn(output)) {
            "Startup report broadcast failed: $output"
        }
        val encoded =
            Regex("data=\"?([A-Za-z0-9+/=]+)\"?").find(output)?.groupValues?.getOrNull(1)
                ?: error("Startup report data missing: $output")
        val report = String(Base64.decode(encoded, Base64.DEFAULT), Charsets.UTF_8)
        check(report.contains("authority=benchmark-ordered-broadcast")) { report }
        check(Regex("eventCount=([1-9][0-9]*)").containsMatchIn(report)) { report }
        requiredLabels.forEach { label -> check(report.contains("label=$label")) { report } }
        return report
    }
}
