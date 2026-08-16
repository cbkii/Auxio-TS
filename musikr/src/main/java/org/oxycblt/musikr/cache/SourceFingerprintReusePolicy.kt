/*
 * Copyright (c) 2026 Auxio Project
 * SourceFingerprintReusePolicy.kt is part of Auxio.
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

package org.oxycblt.musikr.cache

import org.oxycblt.musikr.fs.SourceFingerprintStrength

/** How much a source fingerprint is allowed to suppress authoritative enumeration. */
enum class SourceFingerprintConfidence {
    /**
     * A provider-issued token covering the complete configured source. Reuse stays valid until the
     * token itself changes.
     */
    STRONG,
    /**
     * A bounded, cheap observation such as a shallow directory sample. It is a useful change signal
     * but not proof that a large tree is unchanged. Automatic observation modes may therefore
     * periodically validate it; manual mode deliberately does not turn elapsed time into scan
     * authority.
     */
    ADVISORY,
    /** No trustworthy change token. The source must be enumerated when an index is authorised. */
    UNAVAILABLE,
}

/** Why the planner decided to enumerate a source instead of reusing its committed generation. */
enum class SourceScanReason {
    FORCED,
    NEVER_COMMITTED,
    PREVIOUS_SCAN_INCOMPLETE,
    CONFIGURATION_CHANGED,
    METADATA_PROFILE_UPGRADE,
    INVALIDATED,
    FINGERPRINT_CHANGED,
    FINGERPRINT_UNAVAILABLE,
    ADVISORY_FINGERPRINT_EXPIRED,
}

/**
 * Pure classification of "may this committed source generation be reused?".
 *
 * Reuse is a performance optimisation layered on top of explicit scan authority. In the maintained
 * TS18 product, manual library mode sets [allowAdvisoryExpiry] to false so wall-clock age alone can
 * never turn an ordinary cached refresh into a filesystem traversal. Automatic observation modes
 * retain bounded advisory validation because they explicitly opt into background source checking.
 */
object SourceFingerprintReusePolicy {
    /**
     * How long an advisory fingerprint may suppress enumeration when periodic validation is
     * explicitly enabled by the caller.
     */
    const val ADVISORY_REFRESH_MS = 6L * 60L * 60L * 1000L

    /** Durable per-source ledger fields consulted before enumeration. */
    data class LedgerState(
        val hasCommittedGeneration: Boolean,
        val incomplete: Boolean,
        val configurationRevision: Long,
        val invalidated: Boolean,
        val fingerprint: String?,
        val fingerprintStrength: String,
        val lastSuccessfulScanMs: Long?,
    )

    /**
     * Downgrades a claimed strength to the confidence it actually earns.
     *
     * A missing or blank token carries no evidence regardless of the strength an adapter reports,
     * so it can never suppress a scan.
     */
    fun confidence(
        strength: SourceFingerprintStrength,
        fingerprint: String?,
    ): SourceFingerprintConfidence =
        when {
            fingerprint.isNullOrBlank() -> SourceFingerprintConfidence.UNAVAILABLE
            strength == SourceFingerprintStrength.AUTHORITATIVE ->
                SourceFingerprintConfidence.STRONG
            strength == SourceFingerprintStrength.ADVISORY -> SourceFingerprintConfidence.ADVISORY
            else -> SourceFingerprintConfidence.UNAVAILABLE
        }

    /**
     * Determines whether a committed source generation requires scanning.
     *
     * Correctness requirements take precedence over metadata profile upgrades.
     *
     * @param allowAdvisoryExpiry Whether advisory fingerprints are required to pass freshness validation.
     * @return The applicable scan reason, or `null` when the committed generation may be reused.
     */
    fun scanReason(
        strength: SourceFingerprintStrength,
        fingerprint: String?,
        previous: LedgerState?,
        force: Boolean,
        profileUpgrade: Boolean,
        configurationRevision: Long,
        nowMs: Long,
        allowAdvisoryExpiry: Boolean = true,
    ): SourceScanReason? {
        if (force) return SourceScanReason.FORCED
        if (previous == null || !previous.hasCommittedGeneration) {
            return SourceScanReason.NEVER_COMMITTED
        }
        if (previous.incomplete) return SourceScanReason.PREVIOUS_SCAN_INCOMPLETE
        if (previous.configurationRevision != configurationRevision) {
            return SourceScanReason.CONFIGURATION_CHANGED
        }
        if (previous.invalidated) return SourceScanReason.INVALIDATED
        if (previous.fingerprint != fingerprint || previous.fingerprintStrength != strength.name) {
            return SourceScanReason.FINGERPRINT_CHANGED
        }

        when (confidence(strength, fingerprint)) {
            SourceFingerprintConfidence.UNAVAILABLE ->
                return SourceScanReason.FINGERPRINT_UNAVAILABLE
            SourceFingerprintConfidence.ADVISORY -> {
                if (allowAdvisoryExpiry) {
                    val lastSuccess = previous.lastSuccessfulScanMs
                    val ageMs = lastSuccess?.let { nowMs - it }
                    // Persisted timestamps use wall clock. A clock rollback/future timestamp is not
                    // evidence that the advisory observation remains fresh, so automatic modes fail
                    // safe to a validating scan. Manual mode never enters this expiry branch.
                    if (ageMs == null || ageMs < 0L || ageMs >= ADVISORY_REFRESH_MS) {
                        return SourceScanReason.ADVISORY_FINGERPRINT_EXPIRED
                    }
                }
            }
            SourceFingerprintConfidence.STRONG -> Unit
        }

        return if (profileUpgrade) SourceScanReason.METADATA_PROFILE_UPGRADE else null
    }
}
