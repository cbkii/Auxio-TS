/*
 * Copyright (c) 2026 Auxio Project
 * SourceScanOutcome.kt is part of Auxio.
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

sealed interface SourceScanOutcome {
    val unresolvedSourceKeys: Set<String>

    data class Success(val committedSourceKeys: Set<String>) : SourceScanOutcome {
        override val unresolvedSourceKeys = emptySet<String>()
    }

    data class Partial(
        val committedSourceKeys: Set<String>,
        override val unresolvedSourceKeys: Set<String>,
    ) : SourceScanOutcome

    data class TemporarilyUnavailable(override val unresolvedSourceKeys: Set<String>) :
        SourceScanOutcome

    data class PermissionRequired(override val unresolvedSourceKeys: Set<String>) :
        SourceScanOutcome

    data class AuthoritativeEmpty(val enumeratedSourceKeys: Set<String>) : SourceScanOutcome {
        override val unresolvedSourceKeys = emptySet<String>()
    }

    data class Truncated(
        val readableSourceKeys: Set<String>,
        override val unresolvedSourceKeys: Set<String>,
    ) : SourceScanOutcome

    data class Cancelled(override val unresolvedSourceKeys: Set<String> = emptySet()) :
        SourceScanOutcome

    data class Interrupted(
        val terminalOutcome: IndexingTerminalOutcome,
        override val unresolvedSourceKeys: Set<String> = emptySet(),
    ) : SourceScanOutcome

    data class TimedOut(
        val phase: String,
        val noProgressMs: Long,
        val detail: String,
        override val unresolvedSourceKeys: Set<String> = emptySet(),
    ) : SourceScanOutcome

    data class Failed(
        val retryable: Boolean,
        val failureClass: String,
        val failureMessage: String?,
        override val unresolvedSourceKeys: Set<String> = emptySet(),
    ) : SourceScanOutcome

    companion object {
        fun classify(
            configuredSourceKeys: Set<String>,
            failedSources: Map<String, String>,
            songCount: Int,
        ): SourceScanOutcome {
            val failures = failedSources.keys
            val readable = configuredSourceKeys - failures
            if (failedSources.isEmpty()) {
                return if (songCount == 0) {
                    AuthoritativeEmpty(configuredSourceKeys)
                } else {
                    Success(configuredSourceKeys)
                }
            }
            val truncated = failedSources.filterValues { it.startsWith("TRUNCATED|") }.keys
            if (truncated.isNotEmpty()) {
                return Truncated(readable, failures)
            }
            if (songCount > 0 || readable.isNotEmpty()) {
                return Partial(readable, failures)
            }
            return if (failedSources.values.any { it.startsWith("PERMISSION_REQUIRED|") }) {
                PermissionRequired(failures)
            } else {
                TemporarilyUnavailable(failures)
            }
        }
    }
}

/** Session truthfulness is broader than durable source authority. */
internal fun SourceScanOutcome.isPartialSessionResult(
    unresolvedSourceKeys: Set<String>,
    enrichmentComplete: Boolean,
): Boolean =
    this is SourceScanOutcome.Partial ||
        this is SourceScanOutcome.Truncated ||
        unresolvedSourceKeys.isNotEmpty() ||
        !enrichmentComplete
