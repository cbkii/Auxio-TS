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

import org.oxycblt.musikr.cache.IncrementalCache
import org.oxycblt.musikr.cache.IncrementalScanPlan
import org.oxycblt.musikr.cache.MutableCache
import org.oxycblt.musikr.fs.FS
import org.oxycblt.musikr.fs.SourceAwareFS
import org.oxycblt.musikr.library.MetadataProfile

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
            return Prepared(
                fs = fs,
                cache = if (withCache) cache else legacyWriteOnly(cache),
                plan = null,
            )
        }

        val plan =
            incremental.planScan(
                snapshots = sourceAware.sourceSnapshots(),
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
}
