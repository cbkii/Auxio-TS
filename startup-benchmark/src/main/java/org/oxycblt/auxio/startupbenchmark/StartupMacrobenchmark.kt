/*
 * Copyright (c) 2026 Auxio Project
 * StartupMacrobenchmark.kt is part of Auxio.
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

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Repeatable 0–60 second journey benchmarks for the corrected two-lane startup architecture. */
@RunWith(AndroidJUnit4::class)
@LargeTest
class StartupMacrobenchmark {
    @get:Rule val benchmarkRule = MacrobenchmarkRule()

    private val iterations: Int
        get() = BenchmarkFixtureController.requestedIterations

    private val fixtureSongCount: Int
        get() = BenchmarkFixtureController.requestedSongCount

    private val device: UiDevice by lazy {
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @Test
    fun coldStartupWithoutProfiles() = startupBenchmark(CompilationMode.None(), StartupMode.COLD)

    @Test
    fun coldStartupWithBaselineProfile() =
        startupBenchmark(
            CompilationMode.Partial(baselineProfileMode = BaselineProfileMode.Require),
            StartupMode.COLD,
        )

    @Test
    fun warmStartupWithBaselineProfile() =
        startupBenchmark(
            CompilationMode.Partial(baselineProfileMode = BaselineProfileMode.Require),
            StartupMode.WARM,
        )

    @Test
    fun hotStartupWithBaselineProfile() =
        startupBenchmark(
            CompilationMode.Partial(baselineProfileMode = BaselineProfileMode.Require),
            StartupMode.HOT,
        )

    @Test
    fun savedSessionColdStartupWithBaselineProfile() {
        var prepared = false
        benchmarkRule.measureRepeated(
            packageName = BuildConfig.TARGET_PACKAGE,
            metrics = startupMetrics(),
            compilationMode =
                CompilationMode.Partial(baselineProfileMode = BaselineProfileMode.Require),
            startupMode = StartupMode.COLD,
            iterations = iterations,
            setupBlock = {
                if (!prepared) {
                    BenchmarkFixtureController.run { seedCommittedFixture(fixtureSongCount) }
                    CriticalJourneys.run {
                        launchFastStart()
                        exerciseQuickFind()
                    }
                    device.executeShellCommand("am force-stop ${BuildConfig.TARGET_PACKAGE}")
                    prepared = true
                }
                pressHome()
            },
            measureBlock = { startActivityAndWait() },
        )
        captureReport()
    }

    @Test
    fun findAndPlayJourney() = journeyBenchmark {
        CriticalJourneys.run {
            launchFastStart()
            exerciseQuickFind()
            exercisePlaybackControls()
        }
    }

    @Test
    fun usbFolderPlaybackJourney() = journeyBenchmark {
        CriticalJourneys.run {
            launchFastStart()
            exerciseUsbFolder()
        }
    }

    @Test
    fun pagedLibraryJourney() = journeyBenchmark {
        CriticalJourneys.run {
            launchFastStart()
            exercisePagedLibrary()
        }
    }

    @Test
    fun earlyMediaBrowserJourney() = journeyBenchmark {
        CriticalJourneys.run {
            launchFastStart()
            exerciseEarlyMediaBrowser()
        }
    }

    @Test
    fun coldStartupWithUnavailableSecondUsb() =
        startupBenchmark(
            CompilationMode.Partial(baselineProfileMode = BaselineProfileMode.Require),
            StartupMode.COLD,
            BenchmarkFixtureController.SOURCE_MODE_USB1_ABSENT,
        )

    @Test
    fun coldStartupWithInterruptedPendingGeneration() =
        startupBenchmark(
            CompilationMode.Partial(baselineProfileMode = BaselineProfileMode.Require),
            StartupMode.COLD,
            BenchmarkFixtureController.SOURCE_MODE_PENDING,
        )

    private fun startupBenchmark(
        compilationMode: CompilationMode,
        startupMode: StartupMode,
        sourceMode: String = BenchmarkFixtureController.SOURCE_MODE_NORMAL,
    ) {
        var seeded = false
        benchmarkRule.measureRepeated(
            packageName = BuildConfig.TARGET_PACKAGE,
            metrics = startupMetrics(),
            compilationMode = compilationMode,
            startupMode = startupMode,
            iterations = iterations,
            setupBlock = {
                if (!seeded) {
                    BenchmarkFixtureController.run {
                        seedCommittedFixture(fixtureSongCount, sourceMode)
                    }
                    seeded = true
                }
                pressHome()
            },
            measureBlock = { startActivityAndWait() },
        )
        captureReport()
    }

    private fun journeyBenchmark(
        sourceMode: String = BenchmarkFixtureController.SOURCE_MODE_NORMAL,
        journey: MacrobenchmarkScope.() -> Unit,
    ) {
        var seeded = false
        benchmarkRule.measureRepeated(
            packageName = BuildConfig.TARGET_PACKAGE,
            metrics = startupMetrics(),
            compilationMode =
                CompilationMode.Partial(baselineProfileMode = BaselineProfileMode.Require),
            startupMode = StartupMode.COLD,
            iterations = iterations,
            setupBlock = {
                if (!seeded) {
                    BenchmarkFixtureController.run {
                        seedCommittedFixture(fixtureSongCount, sourceMode)
                    }
                    seeded = true
                }
                pressHome()
            },
            measureBlock = journey,
        )
        captureReport()
    }

    private fun captureReport() {
        BenchmarkFixtureController.captureStartupReport(device)
    }

    private fun startupMetrics() = listOf(StartupTimingMetric(), FrameTimingMetric())
}
