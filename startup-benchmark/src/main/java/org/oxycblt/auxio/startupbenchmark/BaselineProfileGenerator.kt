/*
 * Copyright (c) 2026 Auxio Project
 * BaselineProfileGenerator.kt is part of Auxio.
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

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Generates production profiles from verified immediate-interaction journeys. */
@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {
    @get:Rule val baselineProfileRule = BaselineProfileRule()

    @Test
    fun startupPaths() =
        baselineProfileRule.collect(
            packageName = BuildConfig.TARGET_PACKAGE,
            outputFilePrefix = "auxio-startup",
            includeInStartupProfile = true,
            filterPredicate = ::isProductionRule,
        ) {
            BenchmarkFixtureController.run { seedCommittedFixture() }
            CriticalJourneys.run {
                launchFastStart()
                exerciseProcessDeathRelaunch()
            }
        }

    @Test
    fun immediateInteractionPaths() =
        baselineProfileRule.collect(
            packageName = BuildConfig.TARGET_PACKAGE,
            outputFilePrefix = "auxio-interactions",
            includeInStartupProfile = false,
            filterPredicate = ::isProductionRule,
        ) {
            BenchmarkFixtureController.run { seedCommittedFixture() }
            CriticalJourneys.run {
                // Exercise the bounded primitive queue before any direct-open journey replaces it.
                launchFastStart()
                exercisePlaybackControls()

                // Each remaining path starts from a known Fast Start surface. The previous
                // interaction may leave search, playback or a folder dialog on top, so relying on
                // activity state restoration would make profile generation order-dependent.
                exerciseProcessDeathRelaunch()
                exerciseQuickFind()

                exerciseProcessDeathRelaunch()
                exerciseUsbFolder(sourceIndex = 0)

                exerciseProcessDeathRelaunch()
                exerciseUsbFolder(sourceIndex = 1)

                exerciseProcessDeathRelaunch()
                exercisePagedLibrary()

                exerciseProcessDeathRelaunch()
                exerciseEarlyMediaBrowser()
            }
        }

    private fun isProductionRule(rule: String): Boolean {
        val productionPackage =
            rule.contains("Lorg/oxycblt/auxio/") || rule.contains("Lorg/oxycblt/musikr/")
        val benchmarkPackage = rule.contains("/benchmark/") || rule.contains("/startupbenchmark/")
        return productionPackage && !benchmarkPackage
    }
}
