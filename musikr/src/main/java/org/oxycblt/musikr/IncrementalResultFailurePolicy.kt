/*
 * Copyright (c) 2026 Auxio Project
 * IncrementalResultFailurePolicy.kt is part of Auxio.
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

package org.oxycblt.musikr

import org.oxycblt.musikr.cache.IncrementalScanCommit

/** Converts non-publication scan evidence into truthful repository-visible failures. */
internal object IncrementalResultFailurePolicy {
    fun effectiveFailures(commit: IncrementalScanCommit?): Map<String, String> {
        if (commit == null) return emptyMap()
        val failures = LinkedHashMap(commit.failedSources)
        for (sourceKey in commit.unavailableSources) {
            failures.putIfAbsent(
                sourceKey,
                "TEMPORARILY_UNAVAILABLE|Source preflight did not observe this source",
            )
        }
        return failures
    }
}
