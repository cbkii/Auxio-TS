/*
 * Copyright (c) 2026 Auxio Project
 * SourceSnapshot.kt is part of Auxio.
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

import android.net.Uri

/** Whether a source fingerprint can safely suppress a scan by itself. */
enum class SourceFingerprintStrength {
    /** The provider exposes a generation-like token covering the complete configured source. */
    AUTHORITATIVE,
    /** A bounded fingerprint is useful but must be refreshed periodically. */
    ADVISORY,
    /** No trustworthy change token is available, so the source must be explored. */
    NONE,
}

/**
 * Cheap, source-scoped state captured before expensive exploration.
 *
 * [sourceKey] must remain stable for the configured source. Availability and fingerprint changes
 * never imply deletion; only a successfully committed scan may reconcile missing files.
 */
data class SourceSnapshot(
    val sourceKey: String,
    val sourceType: String,
    val rootUri: String?,
    val rootPath: String?,
    val available: Boolean,
    val fingerprint: String?,
    val fingerprintStrength: SourceFingerprintStrength,
    val observedAtMs: Long = System.currentTimeMillis(),
    /** Exact configured-root identity when one snapshot represents one canonical descriptor. */
    val canonicalKey: String? = null,
    val sourceOrigin: CanonicalSourcePolicy.Origin? = null,
    val traversalScope: CanonicalSourcePolicy.Scope? = null,
)

/** File systems able to plan and restrict work per source before recursive exploration. */
interface SourceAwareFS : FS {
    suspend fun sourceSnapshots(): List<SourceSnapshot>

    /** Return an equivalent file system restricted to [sourceKeys]. */
    fun selectSources(sourceKeys: Set<String>): FS

    /** Drain source-local failures collected by the most recent exploration. */
    fun drainSourceFailures(): Map<String, String> = emptyMap()
}

/** Stable source-key policy shared by file-system adapters and the incremental cache. */
object SourceIdentity {
    fun forFile(file: File): String = file.sourceKey ?: forVolume(file.path.volume)

    /** Volume-scoped identity for genuinely volume-wide sources and legacy attribution only. */
    fun forLocation(location: Location): String = forVolume(location.path.volume)

    /**
     * Canonical alias/deduplication identity of one configured root.
     *
     * This value intentionally omits the backend namespace. SAF and DirectFS ledger authority must
     * use [forConfiguredRoot] so two explicit folders on one volume remain independent and the same
     * visible root reached through different backends cannot collide. [forLocation] remains only
     * for genuinely volume-wide sources and legacy emitted rows that lack an explicit source key.
     */
    fun canonicalKeyForLocation(location: Location): String = canonicalKeyForUri(location.uri)

    /** Stable ledger identity for one exact configured root and backend. */
    fun forConfiguredRoot(sourceType: String, uri: Uri): String =
        "${sourceType.lowercase()}:${canonicalKeyForUri(uri)}"

    fun forConfiguredRoot(sourceType: String, location: Location): String =
        forConfiguredRoot(sourceType, location.uri)

    /** The canonical identity of one configured source URI. */
    fun canonicalKeyForUri(uri: Uri): String =
        if (uri.scheme == "file") {
            uri.path?.let(CanonicalSourcePolicy::identityForPath)
                ?: CanonicalSourcePolicy.identityForUriString(uri.toString())
        } else {
            CanonicalSourcePolicy.identityForUriString(uri.toString())
        }

    fun forVolume(volume: Volume): String =
        when (volume) {
            is Volume.External ->
                "external:${volume.id ?: volume.mediaStoreName ?: volume.components?.unixString ?: "unknown"}"
            is Volume.Internal ->
                "internal:${volume.mediaStoreName ?: volume.components?.unixString ?: "primary"}"
            is Volume.ThirdParty -> {
                val uri = volume.uri
                val value =
                    if (uri.scheme == "file") {
                        uri.path?.trimEnd('/')?.takeIf { it.isNotEmpty() } ?: uri.toString()
                    } else {
                        uri.toString()
                    }
                "third-party:$value"
            }
        }
}
