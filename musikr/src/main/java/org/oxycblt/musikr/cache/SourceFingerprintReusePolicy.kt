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
     * A bounded, cheap observation such as a shallow directory sample. It is a change signal, not
     * proof that a large tree is unchanged. Auxio-TS therefore combines it with explicit
     * invalidation/refresh authority instead of periodically expiring it into an unsolicited scan.
     */
    ADVISORY,
    /** No trustworthy change token. The source must be enumerated when an index is explicitly run. */
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
}

/**
 * Pure classification of "may this committed source generation be reused?".
 *
 * Auxio-TS treats a committed library as the normal operating state. Time passing by itself must
 * never become source-enumeration authority: an unchanged advisory fingerprint remains reusable
 * until a source observer/mount path explicitly invalidates it, its observed token changes, the
 * configuration changes, or the user requests a forced rescan. This is deliberately different from
 * periodically validating a weak fingerprint in the background, which caused ordinary TS18
 * lifecycle work to become a full library traversal after enough wall-clock time elapsed.
 *
 * A missing fingerprint still carries no evidence and cannot suppress an index that has otherwise
 * been authorised.
 */
object SourceFingerprintReusePolicy {
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
     *
     * Source correctness gates deliberately precede a metadata-profile upgrade. A changed or
     * invalidated source remains an authoritative source scan even when the same request also asks
     * for richer metadata; the profile request must never conceal why reuse was unsafe.
     *
     * [nowMs] remains part of this internal contract because the durable ledger records successful
     * scan time for diagnostics and compatibility, but elapsed wall-clock time is intentionally no
     * longer a scan trigger.
     */
    @Suppress("UNUSED_PARAMETER")
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
        if (previous.invalidated) return SourceScanReason.INVALIDATED
        if (previous.fingerprint != fingerprint || previous.fingerprintStrength != strength.name) {
            return SourceScanReason.FINGERPRINT_CHANGED
        }

        if (confidence(strength, fingerprint) == SourceFingerprintConfidence.UNAVAILABLE) {
            return SourceScanReason.FINGERPRINT_UNAVAILABLE
        }

        return if (profileUpgrade) SourceScanReason.METADATA_PROFILE_UPGRADE else null
    }
}
