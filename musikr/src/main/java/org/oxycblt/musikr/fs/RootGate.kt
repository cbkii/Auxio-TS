/*
 * Copyright (c) 2026 Auxio Project
 * RootGate.kt is part of Auxio.
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

package org.oxycblt.musikr.fs

/** One bounded read-only root snapshot entry, relative to [RootTreeSnapshot.rootPath]. */
data class RootTreeEntry(
    val relativePath: String,
    val isDirectory: Boolean,
    val isSymlink: Boolean,
    val modifiedMs: Long,
    val size: Long,
)

/** A single-process, bounded snapshot of one configured storage root. */
data class RootTreeSnapshot(val rootPath: String, val entries: List<RootTreeEntry>)

/**
 * Narrow storage-only root authority exposed to Musikr.
 *
 * Implementations must construct the command internally. Callers cannot submit free-form shell
 * commands, package mutations or vendor-service operations through this boundary.
 */
interface RootGate {
    fun snapshotTreeSync(
        rootPath: String,
        maxDepth: Int = 32,
        timeoutMs: Long = 15_000L,
    ): RootTreeSnapshot?
}

