/*
 * Copyright (c) 2026 Auxio Project
 * IncrementalIndexPlanner.kt is part of Auxio.
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

import kotlinx.coroutines.CancellationException
import org.oxycblt.musikr.cache.IncrementalCache
import org.oxycblt.musikr.cache.IncrementalScanPlan
import org.oxycblt.musikr.cache.MutableCache
import org.oxycblt.musikr.fs.FS
import org.oxycblt.musikr.fs.SourceAwareFS
import org.oxycblt.musikr.fs.SourceFingerprintStrength
import org.oxycblt.musikr.library.MetadataProfile
import timber.log.Timber as L

/** Source-aware planning could not safely classify any configured source. */
internal class SourcePreflightException(message: String, cause: Throwable? = null) :
    IllegalStateException(message, cause)

internal object IncrementalIndexPlanner {
    data class Prepared(val fs: FS, val cache: MutableCache, val plan: IncrementalScanPlan?)

    suspend fun prepare(
        fs: FS,
        cache: MutableCache,
        withCache: Boolean,
        profile: MetadataProfile,
        configurationRevision: Long,
        legacyWriteOnly: (MutableCache) -> MutableCache,
    ): Prepared {
        val incremental = cache as? IncrementalCache
        val sourceAware = fs as? SourceAwareFS
        if (incremental == null || sourceAware == null) {
            return legacyPrepared(fs, cache, withCache, legacyWriteOnly)
        }

        val observedSnapshots =
            try {
                sourceAware.sourceSnapshots()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Once source generations are active, bypassing the ledger can turn an OEM probe
                // failure into a successful empty library. Preserve the last committed generation
                // and surface an actionable scan failure instead.
                throw SourcePreflightException("Music-source preflight failed", e)
            }

        if (observedSnapshots.isEmpty()) {
            throw SourcePreflightException("Music-source preflight returned no configured sources")
        }

        val retryableSnapshots =
            observedSnapshots.map { snapshot ->
                if (snapshot.available) {
                    snapshot
                } else {
                    // Availability probes are advisory. Actual enumeration remains authoritative.
                    // Clear the fingerprint so the ledger plans a real scan and writes a readable
                    // generation when enumeration succeeds. A source-local enumeration failure is
                    // still recorded by markSourceFailed without deleting the previous generation.
                    snapshot.copy(
                        available = true,
                        fingerprint = null,
                        fingerprintStrength = SourceFingerprintStrength.NONE,
                    )
                }
            }
        val retriedKeys =
            observedSnapshots.filterNot { it.available }.mapTo(linkedSetOf()) { it.sourceKey }
        if (retriedKeys.isNotEmpty()) {
            L.w("Retrying sources rejected by advisory preflight: $retriedKeys")
        }

        val plan =
            incremental.planScan(
                snapshots = retryableSnapshots,
                force = !withCache,
                metadataProfile = profile,
                configurationRevision = configurationRevision,
            )
        return Prepared(
            fs = sourceAware.selectSources(plan.scanSourceKeys),
            cache = cache,
            plan = plan,
        )
    }

    private fun legacyPrepared(
        fs: FS,
        cache: MutableCache,
        withCache: Boolean,
        legacyWriteOnly: (MutableCache) -> MutableCache,
    ) = Prepared(fs = fs, cache = if (withCache) cache else legacyWriteOnly(cache), plan = null)
}
