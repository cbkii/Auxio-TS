/*
 * Copyright (c) 2026 Auxio Project
 * SourceConfigurationIdentity.kt is part of Auxio.
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

import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.auxio.music.locations.MusicSourceCanonicalizer

/**
 * The single canonical identity of one filesystem source configuration.
 *
 * The source ledger reuses a committed generation only while this identity is unchanged, so every
 * field below directly decides whether a source must be enumerated and extracted again. Two rules
 * follow from that:
 * 1. Semantically equal configurations must produce the same identity. Canonical keys, sorting and
 *    deduplication remove alias, ordering and duplicate noise before hashing.
 * 2. Settings that only reinterpret already-extracted rows must be excluded. They may still request
 *    a library refresh, but they must never invalidate filesystem authority.
 *
 * Included material, and why:
 * - **location mode** — selects the backend (MediaStore, SAF, DirectFS) and therefore the entire
 *   meaning of every configured root.
 * - **canonical source roots** (SAF/DirectFS) — the exact set of traversed roots, each with its
 *   origin and traversal scope, because those decide what a traversal is allowed to reach.
 * - **canonical exclusions** (SAF/DirectFS) — pruned subtrees change which files a successful
 *   generation may legitimately contain.
 * - **hidden-file policy** (SAF/DirectFS) — changes the eligible file set of every root.
 * - **MediaStore filter mode, filtered roots and non-music filtering** — the provider-side
 *   equivalent of roots and exclusions.
 * - **system source filter** (SAF/MediaStore) — narrows SAF path keywords and relaxes the provider
 *   `IS_MUSIC` heuristic, so it changes which rows a generation may contain.
 * - **root access policy** (DirectFS) — decides whether root-prepared candidate roots may become
 *   readable DirectFS roots at all.
 *
 * Deliberately excluded material, and why:
 * - **separators, intelligent sorting** — tag interpretation only. Cached raw tags stay valid, so
 *   invalidating source generations would force a needless full re-enumeration and re-extraction.
 * - **SAF multithread, scan priority, observation mode** — resource/scheduling policy that cannot
 *   change the authoritative content of a source.
 * - **generated playlists, performance capture, dynamic shortcuts** — non-authoritative
 *   presentation or diagnostics toggles.
 * - **source ordering and alias spelling** — presentation of the same canonical set.
 * - **availability and permission state** — transient runtime conditions. They are carried by the
 *   per-source ledger and fingerprints, never by configuration identity, so a temporarily
 *   unavailable USB volume cannot invalidate every other source.
 */
internal object SourceConfigurationIdentity {
    /** The exact, canonical, order-independent material hashed into a revision. */
    data class Material(
        val locationMode: LocationMode,
        val sourceTokens: List<String>,
        val excludeTokens: List<String>,
        val withHidden: Boolean,
        val mediaStoreTokens: List<String>,
        val systemSourceFilter: Boolean,
        val rootAccessPolicy: String?,
    ) {
        /** Stable textual encoding; the revision is only a hash of this. */
        fun encode(): String = buildString {
            append("mode=").append(locationMode.name)
            sourceTokens.forEach { append("\u0000source=").append(it) }
            excludeTokens.forEach { append("\u0000exclude=").append(it) }
            append("\u0000withHidden=").append(withHidden)
            mediaStoreTokens.forEach { append("\u0000mediaStore=").append(it) }
            append("\u0000systemSourceFilter=").append(systemSourceFilter)
            rootAccessPolicy?.let { append("\u0000rootAccess=").append(it) }
        }
    }

    fun material(settings: MusicSettings): Material {
        val mode = settings.locationMode
        val treeMode = mode == LocationMode.SAF || mode == LocationMode.DIRECT_FS
        val safQuery = if (treeMode) settings.safQuery else null
        val sourceTokens =
            if (treeMode) {
                settings.configuredSourceSpecs
                    .distinctBy { it.canonicalKey }
                    .map { spec ->
                        "${spec.canonicalKey}|${spec.origin.name}|" +
                            spec.traversalScope?.name.orEmpty()
                    }
                    .sorted()
            } else {
                emptyList()
            }
        val excludeTokens =
            safQuery
                ?.exclude
                ?.map(MusicSourceCanonicalizer::canonicalKeyOf)
                ?.distinct()
                ?.sorted()
                .orEmpty()
        val mediaStoreTokens =
            if (mode == LocationMode.MEDIA_STORE) {
                val query = settings.mediaStoreQuery
                buildList {
                    add("mode=${query.mode.name}")
                    add("excludeNonMusic=${query.excludeNonMusic}")
                    query.filtered
                        .map(MusicSourceCanonicalizer::canonicalKeyOf)
                        .distinct()
                        .sorted()
                        .forEach { add("filtered=$it") }
                }
            } else {
                emptyList()
            }
        return Material(
            locationMode = mode,
            sourceTokens = sourceTokens,
            excludeTokens = excludeTokens,
            withHidden = safQuery?.withHidden ?: false,
            mediaStoreTokens = mediaStoreTokens,
            // The TS18 filter narrows SAF path keywords and the MediaStore music heuristic, but
            // DirectFS traversal never consults it.
            systemSourceFilter = mode != LocationMode.DIRECT_FS && settings.ts18SystemSourceFilter,
            // Root preparation only ever produces DirectFS roots.
            rootAccessPolicy =
                if (mode == LocationMode.DIRECT_FS) settings.rootAccessPolicy.name else null,
        )
    }

    fun revision(settings: MusicSettings): Long = revision(material(settings))

    fun revision(material: Material): Long = hash(material.encode())

    /**
     * Deterministic 64-bit FNV-style hash.
     *
     * [String.hashCode] is only 32-bit and is not guaranteed identical across platforms for the
     * durable ledger, so identity is computed explicitly instead.
     */
    private fun hash(text: String): Long {
        var hash = -3750763034362895579L
        for (character in text) {
            hash = hash xor character.code.toLong()
            hash *= 1099511628211L
        }
        return hash
    }
}
