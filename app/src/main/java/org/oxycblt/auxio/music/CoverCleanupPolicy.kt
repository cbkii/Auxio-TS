/*
 * Copyright (c) 2026 Auxio Project
 * CoverCleanupPolicy.kt is part of Auxio.
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

package org.oxycblt.auxio.music

/**
 * Decides whether expired covers may be deleted after a library publication.
 *
 * Cover cleanup retains only the artwork referenced by the library that was just published, so it
 * is destructive whenever that library is not a complete authoritative view. A source that failed,
 * was unavailable, or was never enumerated still owns committed rows in the ledger; deleting the
 * artwork they reference would silently degrade the last-known-good library the moment that source
 * returns.
 */
internal object CoverCleanupPolicy {
    data class Decision(val allowed: Boolean, val reason: String)

    /**
     * @param published Whether a new library was actually published by this scan.
     * @param outcome The single terminal outcome recorded for the scan.
     * @param unresolvedSourceKeys Configured sources still carrying an unresolved generation.
     * @param unavailableSourceKeys Sources the plan could not observe during this scan.
     * @param completeMetadata Whether the published library used the complete metadata profile. A
     *   lean publication is deliberately incomplete and is followed by enrichment, so it must not
     *   define the retained cover set.
     */
    fun evaluate(
        published: Boolean,
        outcome: SourceScanOutcome,
        unresolvedSourceKeys: Set<String>,
        unavailableSourceKeys: Set<String>,
        completeMetadata: Boolean,
    ): Decision =
        when {
            !published -> Decision(false, "no-new-generation-published")
            !completeMetadata -> Decision(false, "lean-publication-is-not-complete")
            unresolvedSourceKeys.isNotEmpty() -> Decision(false, "unresolved-sources-retained")
            unavailableSourceKeys.isNotEmpty() -> Decision(false, "sources-unobserved")
            outcome is SourceScanOutcome.Success -> Decision(true, "complete-authoritative-success")
            outcome is SourceScanOutcome.AuthoritativeEmpty ->
                Decision(true, "complete-authoritative-empty")
            else -> Decision(false, "non-authoritative-outcome")
        }
}
