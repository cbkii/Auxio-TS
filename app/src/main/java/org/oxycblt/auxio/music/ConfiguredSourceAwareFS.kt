/*
 * Copyright (c) 2026 Auxio Project
 * ConfiguredSourceAwareFS.kt is part of Auxio.
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

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import org.oxycblt.musikr.fs.FS
import org.oxycblt.musikr.fs.FSUpdate
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.fs.SourceAwareFS
import org.oxycblt.musikr.fs.SourceFingerprintStrength
import org.oxycblt.musikr.fs.SourceSnapshot

/**
 * Preserves configured identity when an underlying provider drops roots it cannot open.
 *
 * The delegate remains the sole source of scan files. Synthetic rows are planning and recovery
 * metadata only, preserving the app-UID authority boundary.
 */
internal class ConfiguredSourceAwareFS(
    private val delegate: FS,
    private val specs: List<ConfiguredSourceSpec>,
) : SourceAwareFS {
    override suspend fun sourceSnapshots(): List<SourceSnapshot> {
        val actual =
            (delegate as? SourceAwareFS)?.sourceSnapshots().orEmpty().associateByTo(linkedMapOf()) {
                it.sourceKey
            }
        for ((sourceKey, grouped) in specs.groupBy { it.sourceKey }) {
            val unavailable =
                grouped.firstOrNull { it.accessState != ConfiguredSourceSpec.AccessState.AVAILABLE }
            if (unavailable != null || sourceKey !in actual) {
                val first = unavailable ?: grouped.first()
                actual[sourceKey] =
                    SourceSnapshot(
                        sourceKey = sourceKey,
                        sourceType = first.mode.name,
                        rootUri = first.normalizedUri.toString(),
                        rootPath = first.displayPath,
                        available = false,
                        fingerprint = null,
                        fingerprintStrength = SourceFingerprintStrength.NONE,
                    )
            }
        }
        return actual.values.toList()
    }

    override fun selectSources(sourceKeys: Set<String>): FS =
        ConfiguredSourceAwareFS(
            delegate = (delegate as? SourceAwareFS)?.selectSources(sourceKeys) ?: delegate,
            specs = specs.filter { it.sourceKey in sourceKeys },
        )

    override fun drainSourceFailures(): Map<String, String> {
        val failures = (delegate as? SourceAwareFS)?.drainSourceFailures().orEmpty().toMutableMap()
        for (spec in specs) {
            val detail =
                when (spec.accessState) {
                    ConfiguredSourceSpec.AccessState.AVAILABLE -> null
                    ConfiguredSourceSpec.AccessState.PERMISSION_REQUIRED ->
                        "PERMISSION_REQUIRED|${spec.displayPath}"
                    ConfiguredSourceSpec.AccessState.TEMPORARILY_UNAVAILABLE ->
                        "TEMPORARILY_UNAVAILABLE|${spec.displayPath}"
                }
            if (detail != null) failures.putIfAbsent(spec.sourceKey, detail)
        }
        return failures
    }

    override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> =
        delegate.explore(files)

    override fun track(): Flow<FSUpdate> = delegate.track()
}
