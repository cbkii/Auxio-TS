/*
 * Copyright (c) 2026 Auxio Project
 * MusicSourceCanonicalizer.kt is part of Auxio.
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

package org.oxycblt.auxio.music.locations

import android.net.Uri
import androidx.core.net.toUri
import org.oxycblt.musikr.fs.CanonicalSourcePolicy
import org.oxycblt.musikr.fs.Location
import org.oxycblt.musikr.fs.SourceIdentity

/**
 * The app-facing entry point to the shared canonical source policy.
 *
 * One physical folder must have exactly one identity wherever the app persists, displays, counts,
 * compares or explores it. Persistence, the source picker and backend construction therefore all
 * derive identity from here rather than from object equality, which only ever compared raw URIs and
 * so treated `/sdcard/Music`, `/storage/emulated/0/Music/` and a duplicate entry of the same path
 * as three distinct sources.
 */
internal object MusicSourceCanonicalizer {
    /**
     * The canonical identity of a persisted source entry, or `null` when the entry is not usable in
     * the current mode.
     */
    fun canonicalKeyOfEntry(value: String, fileOnly: Boolean): String? =
        MusicSourcePathNormalizer.normalizePersistedLocation(value, fileOnly)?.let {
            canonicalKeyOfUri(it.toUri())
        }

    /** The canonical identity of a source URI. */
    fun canonicalKeyOfUri(uri: Uri): String = SourceIdentity.canonicalKeyForUri(uri)

    /** The canonical identity of an opened or unopened location. */
    fun canonicalKeyOf(location: Location): String =
        SourceIdentity.canonicalKeyForLocation(location)

    /** Collapses exact canonical duplicates in persisted entries, keeping the first occurrence. */
    fun collapseEntries(entries: List<String>, fileOnly: Boolean): List<String> =
        CanonicalSourcePolicy.collapseDuplicates(entries) { canonicalKeyOfEntry(it, fileOnly) }

    /**
     * Collapses exact canonical duplicates in a list of locations, keeping the first occurrence.
     */
    fun <T : Location> collapseLocations(locations: List<T>): List<T> =
        CanonicalSourcePolicy.collapseDuplicates(locations, ::canonicalKeyOf)

    /** Whether [candidate] is already represented by one of [existing]. */
    fun isDuplicate(existing: List<Location>, candidate: Location): Boolean {
        val key = canonicalKeyOf(candidate)
        return existing.any { canonicalKeyOf(it) == key }
    }

    /**
     * The already configured source that strictly contains [candidate], if any.
     *
     * Overlap is reported rather than silently resolved: removing a deliberate custom source could
     * shrink the effective scan scope, so the user is told instead.
     */
    fun ancestorOf(existing: List<Location>, candidate: Location): Location? {
        val candidatePath = appFacingPathOf(candidate) ?: return null
        return existing.firstOrNull {
            val existingPath = appFacingPathOf(it) ?: return@firstOrNull false
            CanonicalSourcePolicy.isAncestorOf(existingPath, candidatePath)
        }
    }

    /** The already configured sources strictly contained by [candidate]. */
    fun descendantsOf(existing: List<Location>, candidate: Location): List<Location> {
        val candidatePath = appFacingPathOf(candidate) ?: return emptyList()
        return existing.filter {
            val existingPath = appFacingPathOf(it) ?: return@filter false
            CanonicalSourcePolicy.isAncestorOf(candidatePath, existingPath)
        }
    }

    /** Whether [location] covers a whole volume rather than a folder inside one. */
    fun isWholeVolume(location: Location): Boolean =
        appFacingPathOf(location)?.let(CanonicalSourcePolicy::isVolumeRoot) == true

    /** Whether [path] denotes a whole volume rather than a folder inside one. */
    fun isWholeVolumePath(path: String): Boolean = CanonicalSourcePolicy.isVolumeRoot(path)

    /** Whether [path] is inside any of [existingPaths]. */
    fun hasNarrowerSourceOn(existingPaths: List<String>, path: String): Boolean =
        existingPaths.any { CanonicalSourcePolicy.isAncestorOf(path, it) }

    /**
     * The app-facing canonical path of [location], when the provider exposes structural identity.
     */
    fun appFacingPathOf(location: Location): String? = appFacingPathOfUri(location.uri)

    /** The app-facing canonical path of [uri], or `null` for an opaque provider-backed source. */
    fun appFacingPathOfUri(uri: Uri): String? =
        if (uri.scheme == "file") {
            uri.path?.let(CanonicalSourcePolicy::normalizePath)
        } else {
            CanonicalSourcePolicy.externalStorageTreePath(uri)
        }
}
