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
                // Snapshot metadata is only an optimisation. A broken OEM provider version/root
                // query must never suppress the real MediaStore, SAF, or DirectFS enumeration.
                L.w(e, "Source preflight failed; falling back to a complete source scan")
                return legacyPrepared(fs, cache, withCache, legacyWriteOnly)
            }

        if (observedSnapshots.isEmpty()) {
            // MediaStore implementations on vendor Android 10 builds can fail volume discovery while
            // the ordinary external audio query still works. Let the real adapter decide.
            L.w("Source preflight returned no snapshots; falling back to a complete source scan")
            return legacyPrepared(fs, cache, withCache, legacyWriteOnly)
        }

        val retryableSnapshots =
            observedSnapshots.map { snapshot ->
                if (snapshot.available) {
                    snapshot
                } else {
                    // Availability probes are advisory. Actual enumeration is the authority and can
                    // record a source-local failure without deleting the previous committed rows.
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
    ) =
        Prepared(
            fs = fs,
            cache = if (withCache) cache else legacyWriteOnly(cache),
            plan = null,
        )
}
