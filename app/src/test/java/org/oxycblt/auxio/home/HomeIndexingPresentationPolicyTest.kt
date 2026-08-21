/*
 * Copyright (c) 2026 Auxio Project
 * HomeIndexingPresentationPolicyTest.kt is part of Auxio.
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

package org.oxycblt.auxio.home

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.oxycblt.auxio.music.IndexingState
import org.oxycblt.auxio.music.IndexingTerminalOutcome

class HomeIndexingPresentationPolicyTest {
    @Test
    fun noIndexerStateDoesNotShowDiagnosticCard() {
        assertFalse(HomeIndexingPresentationPolicy.shouldShowStatusCard(null))
    }

    @Test
    fun successfulCompletionDoesNotShowDiagnosticCard() {
        assertFalse(
            HomeIndexingPresentationPolicy.shouldShowStatusCard(
                completed(IndexingTerminalOutcome.SUCCESS)
            )
        )
    }

    @Test
    fun actionableTerminalOutcomeShowsDiagnosticCard() {
        assertTrue(
            HomeIndexingPresentationPolicy.shouldShowStatusCard(
                completed(IndexingTerminalOutcome.SOURCE_UNAVAILABLE)
            )
        )
    }

    private fun completed(outcome: IndexingTerminalOutcome): IndexingState.Completed =
        IndexingState.Completed(outcome = outcome, error = null)
}
