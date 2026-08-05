/*
 * Copyright (c) 2026 Auxio Project
 * SourceFingerprintReusePolicyTest.kt is part of Auxio.
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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.oxycblt.musikr.fs.SourceFingerprintStrength

/**
 * Reuse is a performance optimisation on top of the authoritative scan lane, so every ambiguous
 * case must resolve to a real enumeration rather than to a silently stale library.
 */
class SourceFingerprintReusePolicyTest {
    private val now = 1_000_000_000L

    @Test
    fun `strong fingerprint reuses a committed generation indefinitely`() {
        assertNull(
            reason(
                strength = SourceFingerprintStrength.AUTHORITATIVE,
                fingerprint = "token-1",
                previous =
                    committed(
                        fingerprint = "token-1",
                        strength = SourceFingerprintStrength.AUTHORITATIVE,
                        // Far older than the advisory refresh window, which must not apply here.
                        lastSuccessfulScanMs = now - 30L * ADVISORY_WINDOW,
                    ),
            )
        )
    }

    @Test
    fun `advisory fingerprint reuses only inside its bounded window`() {
        assertNull(
            reason(
                strength = SourceFingerprintStrength.ADVISORY,
                fingerprint = "shallow-1",
                previous =
                    committed(
                        fingerprint = "shallow-1",
                        lastSuccessfulScanMs = now - ADVISORY_WINDOW / 2,
                    ),
            )
        )
    }

    @Test
    fun `expired advisory fingerprint forces a validating scan`() {
        assertEquals(
            SourceScanReason.ADVISORY_FINGERPRINT_EXPIRED,
            reason(
                strength = SourceFingerprintStrength.ADVISORY,
                fingerprint = "shallow-1",
                previous =
                    committed(
                        fingerprint = "shallow-1",
                        lastSuccessfulScanMs = now - ADVISORY_WINDOW,
                    ),
            ),
        )
    }

    @Test
    fun `advisory fingerprint without a token is never proof of an unchanged source`() {
        // A shallow directory observation that produced nothing must not suppress enumeration just
        // because the previous scan also produced nothing.
        assertEquals(
            SourceFingerprintConfidence.UNAVAILABLE,
            SourceFingerprintReusePolicy.confidence(SourceFingerprintStrength.ADVISORY, null),
        )
        assertEquals(
            SourceFingerprintConfidence.UNAVAILABLE,
            SourceFingerprintReusePolicy.confidence(SourceFingerprintStrength.ADVISORY, "  "),
        )
        assertEquals(
            SourceScanReason.FINGERPRINT_UNAVAILABLE,
            reason(
                strength = SourceFingerprintStrength.ADVISORY,
                fingerprint = null,
                previous =
                    committed(fingerprint = null, lastSuccessfulScanMs = now - ADVISORY_WINDOW / 2),
            ),
        )
    }

    @Test
    fun `absent fingerprint support always enumerates`() {
        assertEquals(
            SourceScanReason.FINGERPRINT_UNAVAILABLE,
            reason(
                strength = SourceFingerprintStrength.NONE,
                fingerprint = "ignored",
                previous =
                    committed(
                        fingerprint = "ignored",
                        strength = SourceFingerprintStrength.NONE,
                        lastSuccessfulScanMs = now,
                    ),
            ),
        )
    }

    @Test
    fun `a changed fingerprint enumerates that source`() {
        assertEquals(
            SourceScanReason.FINGERPRINT_CHANGED,
            reason(
                strength = SourceFingerprintStrength.ADVISORY,
                fingerprint = "shallow-2",
                previous = committed(fingerprint = "shallow-1"),
            ),
        )
    }

    @Test
    fun `a strengthened fingerprint is validated rather than trusted`() {
        assertEquals(
            SourceScanReason.FINGERPRINT_CHANGED,
            reason(
                strength = SourceFingerprintStrength.AUTHORITATIVE,
                fingerprint = "token-1",
                previous =
                    committed(
                        fingerprint = "token-1",
                        strength = SourceFingerprintStrength.ADVISORY,
                    ),
            ),
        )
    }

    @Test
    fun `correctness gates outrank any fingerprint evidence`() {
        val strong =
            committed(
                fingerprint = "token-1",
                strength = SourceFingerprintStrength.AUTHORITATIVE,
                lastSuccessfulScanMs = now,
            )

        assertEquals(SourceScanReason.FORCED, reason(previous = strong, force = true))
        assertEquals(SourceScanReason.NEVER_COMMITTED, reason(previous = null))
        assertEquals(
            SourceScanReason.NEVER_COMMITTED,
            reason(previous = strong.copy(hasCommittedGeneration = false)),
        )
        assertEquals(
            SourceScanReason.PREVIOUS_SCAN_INCOMPLETE,
            reason(previous = strong.copy(incomplete = true)),
        )
        assertEquals(
            SourceScanReason.CONFIGURATION_CHANGED,
            reason(previous = strong.copy(configurationRevision = 99L)),
        )
        assertEquals(
            SourceScanReason.METADATA_PROFILE_UPGRADE,
            reason(previous = strong, profileUpgrade = true),
        )
        assertEquals(
            SourceScanReason.INVALIDATED,
            reason(previous = strong.copy(invalidated = true)),
        )
    }

    private fun committed(
        fingerprint: String?,
        strength: SourceFingerprintStrength = SourceFingerprintStrength.ADVISORY,
        lastSuccessfulScanMs: Long? = now,
    ) =
        SourceFingerprintReusePolicy.LedgerState(
            hasCommittedGeneration = true,
            incomplete = false,
            configurationRevision = REVISION,
            invalidated = false,
            fingerprint = fingerprint,
            fingerprintStrength = strength.name,
            lastSuccessfulScanMs = lastSuccessfulScanMs,
        )

    private fun reason(
        previous: SourceFingerprintReusePolicy.LedgerState?,
        strength: SourceFingerprintStrength =
            previous?.fingerprintStrength?.let { SourceFingerprintStrength.valueOf(it) }
                ?: SourceFingerprintStrength.AUTHORITATIVE,
        fingerprint: String? = previous?.fingerprint ?: "token-1",
        force: Boolean = false,
        profileUpgrade: Boolean = false,
    ) =
        SourceFingerprintReusePolicy.scanReason(
            strength = strength,
            fingerprint = fingerprint,
            previous = previous,
            force = force,
            profileUpgrade = profileUpgrade,
            configurationRevision = REVISION,
            nowMs = now,
        )

    private companion object {
        const val REVISION = 7L
        const val ADVISORY_WINDOW = SourceFingerprintReusePolicy.ADVISORY_REFRESH_MS
    }
}
