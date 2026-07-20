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
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.Metric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Repeatable 0–60 second journey benchmarks for the corrected two-lane startup architecture. */
@OptIn(ExperimentalMetricApi::class)
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
        var seeded = false
        benchmarkRule.measureRepeated(
            packageName = BuildConfig.TARGET_PACKAGE,
            metrics =
                startupMetrics(
                    traceMetric(
                        CriticalJourneys.TRACE_SAVED_SESSION_TO_FIRST_AUDIO,
                        "savedSessionToFirstAudio",
                    )
                ),
            compilationMode =
                CompilationMode.Partial(baselineProfileMode = BaselineProfileMode.Require),
            startupMode = StartupMode.COLD,
            iterations = iterations,
            setupBlock = {
                if (!seeded) {
                    BenchmarkFixtureController.run { seedCommittedFixture(fixtureSongCount) }
                    seeded = true
                }
                pressHome()
            },
            measureBlock = { CriticalJourneys.run { exerciseSavedSessionResume() } },
        )
        captureReport(REQUIRED_IMMEDIATE_LABELS)
    }

    @Test
    fun primitiveQueueControlsJourney() =
        journeyBenchmark(
            metrics =
                listOf(
                    traceMetric(
                        CriticalJourneys.TRACE_NEXT_COMMAND_TO_NEXT_AUDIO,
                        "nextCommandToNextAudio",
                    )
                )
        ) {
            CriticalJourneys.run {
                launchFastStart()
                exercisePlaybackControls()
            }
        }

    @Test
    fun findAndPlayJourney() =
        journeyBenchmark(
            metrics =
                listOf(
                    traceMetric(
                        CriticalJourneys.TRACE_QUICK_FIND_FIRST_RESULT,
                        "quickFindFirstResult",
                    ),
                    traceMetric(
                        CriticalJourneys.TRACE_SEARCH_RESULT_TO_FIRST_AUDIO,
                        "searchResultToFirstAudio",
                    ),
                )
        ) {
            CriticalJourneys.run {
                launchFastStart()
                exerciseQuickFind()
            }
        }

    @Test
    fun usbFolderPlaybackJourney() =
        journeyBenchmark(
            metrics =
                listOf(
                    traceMetric(
                        CriticalJourneys.TRACE_USB0_FOLDER_TO_FIRST_AUDIO,
                        "usb0FolderToFirstAudio",
                    )
                )
        ) {
            CriticalJourneys.run {
                launchFastStart()
                exerciseUsbFolder(sourceIndex = 0)
            }
        }

    @Test
    fun secondUsbFolderPlaybackJourney() =
        journeyBenchmark(
            metrics =
                listOf(
                    traceMetric(
                        CriticalJourneys.TRACE_USB1_FOLDER_TO_FIRST_AUDIO,
                        "usb1FolderToFirstAudio",
                    )
                )
        ) {
            CriticalJourneys.run {
                launchFastStart()
                exerciseUsbFolder(sourceIndex = 1)
            }
        }

    @Test
    fun pagedLibraryJourney() =
        journeyBenchmark(
            metrics =
                listOf(
                    traceMetric(CriticalJourneys.TRACE_FIRST_SONGS_PAGE, "firstSongsPage"),
                    traceMetric(CriticalJourneys.TRACE_FIRST_ALBUMS_PAGE, "firstAlbumsPage"),
                )
        ) {
            CriticalJourneys.run {
                launchFastStart()
                exercisePagedLibrary()
            }
        }

    @Test
    fun earlyMediaBrowserJourney() =
        journeyBenchmark(
            metrics =
                listOf(
                    traceMetric(
                        CriticalJourneys.TRACE_MEDIA_BROWSER_FIRST_PAGE,
                        "mediaBrowserFirstPage",
                    )
                )
        ) {
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

    @Test
    fun completeLibraryMilestonesRemainNonBlocking() =
        journeyBenchmark(reportLabels = REQUIRED_COMPLETE_LIBRARY_LABELS) {
            CriticalJourneys.run { launchFastStart() }
            val report =
                BenchmarkFixtureController.awaitStartupReport(
                    device,
                    REQUIRED_COMPLETE_LIBRARY_LABELS,
                    timeoutMs = COMPLETE_LIBRARY_TIMEOUT_MS,
                )
            printReport(report)
        }

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
            measureBlock = { CriticalJourneys.run { launchFastStart() } },
        )
        captureReport(REQUIRED_IMMEDIATE_LABELS)
    }

    private fun journeyBenchmark(
        sourceMode: String = BenchmarkFixtureController.SOURCE_MODE_NORMAL,
        metrics: List<Metric> = emptyList(),
        reportLabels: Set<String> = REQUIRED_IMMEDIATE_LABELS,
        journey: MacrobenchmarkScope.() -> Unit,
    ) {
        var seeded = false
        benchmarkRule.measureRepeated(
            packageName = BuildConfig.TARGET_PACKAGE,
            metrics = startupMetrics(*metrics.toTypedArray()),
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
        captureReport(reportLabels)
    }

    private fun captureReport(requiredLabels: Set<String>) {
        printReport(BenchmarkFixtureController.captureStartupReport(device, requiredLabels))
    }

    private fun printReport(report: String) {
        println("AUXIO_STARTUP_REPORT_BEGIN")
        println(report)
        println("AUXIO_STARTUP_REPORT_END")
    }

    private fun startupMetrics(vararg additional: Metric): List<Metric> =
        listOf(StartupTimingMetric(), FrameTimingMetric()) + additional

    private fun traceMetric(sectionName: String, label: String): Metric =
        TraceSectionMetric(
            sectionName = sectionName,
            mode = TraceSectionMetric.Mode.First,
            label = label,
            targetPackageOnly = false,
        )

    private companion object {
        const val COMPLETE_LIBRARY_TIMEOUT_MS = 60_000L
        val REQUIRED_IMMEDIATE_LABELS =
            setOf(
                "Application.onCreate:start",
                "Application.onCreate:end",
                "startup.capability.PLAYBACK_SERVICE_READY",
                "startup.capability.QUEUE_READY",
                "startup.fast_home_first_rows",
            )
        val REQUIRED_COMPLETE_LIBRARY_LABELS =
            REQUIRED_IMMEDIATE_LABELS + "startup.capability.FULL_LIBRARY_READY"
    }
}
