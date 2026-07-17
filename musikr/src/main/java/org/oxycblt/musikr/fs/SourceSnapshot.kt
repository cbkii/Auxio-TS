/*
 * Copyright (c) 2026 Auxio Project
 * SourceSnapshot.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.musikr.fs

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
)

/** File systems able to plan and restrict work per source before recursive exploration. */
interface SourceAwareFS : FS {
    suspend fun sourceSnapshots(): List<SourceSnapshot>

    /** Return an equivalent file system restricted to [sourceKeys]. */
    fun selectSources(sourceKeys: Set<String>): FS
}

/** Stable source-key policy shared by file-system adapters and the incremental cache. */
object SourceIdentity {
    fun forFile(file: File): String = forVolume(file.path.volume)

    fun forLocation(location: Location): String = forVolume(location.path.volume)

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
