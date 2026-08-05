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
     * A bounded, cheap observation such as a shallow directory sample. Useful to avoid needless
     * warm work, never proof that a large tree is unchanged, so it expires on a bounded interval.
     */
    ADVISORY,
    /** No trustworthy change token. The source must be enumerated. */
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
 * Pure classification of "is this source provably unchanged?".
 *
 * Reuse is only ever a performance optimisation layered on top of the authoritative scan lane. Any
 * doubt must resolve to a real scan, because a wrongly reused generation silently hides files the
 * user can see on the device.
 */
object SourceFingerprintReusePolicy {
    /**
     * How long an advisory fingerprint may suppress enumeration.
     *
     * Bounded rather than indefinite because advisory tokens observe a sample, not the complete
     * tree: a change beneath the sampled level is invisible to them.
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
     * Returns the reason this source must be scanned, or `null` when its committed generation may
     * be reused as-is.
     */
    fun scanReason(
        strength: SourceFingerprintStrength,
        fingerprint: String?,
        previous: LedgerState?,
        force: Boolean,
        profileUpgrade: Boolean,
        configurationRevision: Long,
        nowMs: Long,
    ): SourceScanReason? {
        if (force) return SourceScanReason.FORCED
        if (previous == null || !previous.hasCommittedGeneration) {
            return SourceScanReason.NEVER_COMMITTED
        }
        if (previous.incomplete) return SourceScanReason.PREVIOUS_SCAN_INCOMPLETE
        if (previous.configurationRevision != configurationRevision) {
            return SourceScanReason.CONFIGURATION_CHANGED
        }
        if (profileUpgrade) return SourceScanReason.METADATA_PROFILE_UPGRADE
        if (previous.invalidated) return SourceScanReason.INVALIDATED
        if (previous.fingerprint != fingerprint || previous.fingerprintStrength != strength.name) {
            return SourceScanReason.FINGERPRINT_CHANGED
        }
        return when (confidence(strength, fingerprint)) {
            SourceFingerprintConfidence.UNAVAILABLE -> SourceScanReason.FINGERPRINT_UNAVAILABLE
            SourceFingerprintConfidence.STRONG -> null
            SourceFingerprintConfidence.ADVISORY -> {
                val lastSuccess = previous.lastSuccessfulScanMs
                if (lastSuccess == null || nowMs - lastSuccess >= ADVISORY_REFRESH_MS) {
                    SourceScanReason.ADVISORY_FINGERPRINT_EXPIRED
                } else {
                    null
                }
            }
        }
    }
}
