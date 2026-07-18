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
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test

/** Repeatable 0–60 second journey benchmarks for the corrected two-lane startup architecture. */
class StartupMacrobenchmark {
    @get:Rule val benchmarkRule = MacrobenchmarkRule()

    private val iterations: Int
        get() =
            InstrumentationRegistry.getArguments()
                .getString("auxio.iterations")
                ?.toIntOrNull()
                ?.coerceIn(3, 30)
                ?: 7

    @Test
    fun coldStartupWithoutProfiles() =
        startupBenchmark(CompilationMode.None(), StartupMode.COLD)

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
    fun findAndPlayJourney() =
        journeyBenchmark {
            CriticalJourneys.run {
                launchFastStart()
                exerciseQuickFind()
                exercisePlaybackControls()
            }
        }

    @Test
    fun usbFolderPlaybackJourney() =
        journeyBenchmark {
            CriticalJourneys.run {
                launchFastStart()
                exerciseUsbFolder()
            }
        }

    @Test
    fun pagedLibraryJourney() =
        journeyBenchmark {
            CriticalJourneys.run {
                launchFastStart()
                exercisePagedLibrary()
            }
        }

    private fun startupBenchmark(compilationMode: CompilationMode, startupMode: StartupMode) {
        benchmarkRule.measureRepeated(
            packageName = BuildConfig.TARGET_PACKAGE,
            metrics = listOf(StartupTimingMetric(), FrameTimingMetric()),
            compilationMode = compilationMode,
            startupMode = startupMode,
            iterations = iterations,
            setupBlock = { pressHome() },
            measureBlock = { startActivityAndWait() },
        )
    }

    private fun journeyBenchmark(
        journey: androidx.benchmark.macro.MacrobenchmarkScope.() -> Unit
    ) {
        benchmarkRule.measureRepeated(
            packageName = BuildConfig.TARGET_PACKAGE,
            metrics = listOf(StartupTimingMetric(), FrameTimingMetric()),
            compilationMode =
                CompilationMode.Partial(baselineProfileMode = BaselineProfileMode.Require),
            startupMode = StartupMode.COLD,
            iterations = iterations,
            setupBlock = { pressHome() },
            measureBlock = journey,
        )
    }
}
