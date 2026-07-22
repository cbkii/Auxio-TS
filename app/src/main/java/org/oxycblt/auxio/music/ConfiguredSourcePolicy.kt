/*
 * Copyright (c) 2026 Auxio Project
 * ConfiguredSourcePolicy.kt is part of Auxio.
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

import android.net.Uri
import java.io.File
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton
import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.musikr.fs.mediastore.MediaStore

/**
 * Produces immutable, operation-scoped snapshots of the configured music-source policy.
 *
 * Discovery can suggest other volumes, but it cannot make them runtime sources until the user
 * commits a new configuration and a later snapshot contains them.
 */
@Singleton
class ConfiguredSourcePolicy @Inject constructor(private val settings: MusicSettings) {

    enum class SourceKind {
        INTERNAL,
        REMOVABLE,
        DOCUMENT_PROVIDER,
        MEDIA_STORE,
        UNKNOWN,
    }

    enum class Availability {
        READY,
        UNAVAILABLE,
        PROVIDER_MANAGED,
    }

    data class Source(
        val id: String,
        val uri: String,
        val appPath: String?,
        val kind: SourceKind,
        val availability: Availability,
    )

    data class Snapshot(
        val locationMode: LocationMode,
        val sources: List<Source>,
        val configurationRevision: Long,
    ) {
        val configuredRoots: List<String>
            get() = sources.mapNotNull { it.appPath }

        val hasConfiguredUsb: Boolean
            get() = sources.any { it.kind == SourceKind.REMOVABLE }

        fun rootFiles(): Map<String, File> =
            sources
                .asSequence()
                .filter { it.appPath != null && it.availability != Availability.UNAVAILABLE }
                .associate { requireNotNull(it.appPath) to File(requireNotNull(it.appPath)) }

        fun containsPath(path: String): Boolean {
            val cleanPath = normalisePath(path) ?: return false
            return configuredRoots.any { root ->
                cleanPath == root || cleanPath.startsWith("$root/")
            }
        }
    }

    val locationMode: LocationMode
        get() = settings.locationMode

    val isUsbConfigured: Boolean
        get() = snapshot().hasConfiguredUsb

    /** Unconfigured removable discovery remains an explicit source-picker action. */
    val allowUnconfiguredUsbDiscovery: Boolean
        get() = false

    /** Capture one internally consistent source configuration for a complete operation. */
    fun snapshot(): Snapshot {
        val mode = settings.locationMode
        val safQuery = settings.safQuery
        val mediaStoreQuery = settings.mediaStoreQuery
        val systemFilter = settings.ts18SystemSourceFilter
        val sourceUris =
            when (mode) {
                LocationMode.SAF,
                LocationMode.DIRECT_FS -> safQuery.source.map { it.uri.toString() }
                LocationMode.MEDIA_STORE ->
                    if (mediaStoreQuery.mode == MediaStore.FilterMode.INCLUDE) {
                        mediaStoreQuery.filtered.map { it.uri.toString() }
                    } else {
                        // Exclude-mode MediaStore filtering has no positive direct-browse roots.
                        emptyList()
                    }
            }

        val sources = sourceUris.distinct().map { sourceFromUri(it, mode) }
        val revisionMaterial = buildString {
            append(mode.name)
            append('|').append(safQuery)
            append('|').append(mediaStoreQuery)
            append('|').append(systemFilter)
        }
        return Snapshot(mode, sources, stableLong(revisionMaterial))
    }

    fun getConfiguredRootsAsFiles(): Map<String, File> = snapshot().rootFiles()

    fun isConfiguredPath(path: String): Boolean = snapshot().containsPath(path)

    private fun sourceFromUri(rawUri: String, mode: LocationMode): Source {
        val appPath = appFacingPath(rawUri)
        val kind = classify(rawUri, appPath, mode)
        val availability =
            if (appPath == null) {
                Availability.PROVIDER_MANAGED
            } else {
                try {
                    val file = File(appPath)
                    if (file.exists() && file.isDirectory && file.canRead()) {
                        Availability.READY
                    } else {
                        Availability.UNAVAILABLE
                    }
                } catch (_: RuntimeException) {
                    Availability.UNAVAILABLE
                }
            }
        return Source(
            id = stableId(rawUri),
            uri = rawUri,
            appPath = appPath,
            kind = kind,
            availability = availability,
        )
    }

    private fun classify(rawUri: String, appPath: String?, mode: LocationMode): SourceKind {
        val lower = (appPath ?: rawUri).lowercase()
        return when {
            lower.contains("usbdisk") || UUID_STORAGE.matches(appPath.orEmpty()) ->
                SourceKind.REMOVABLE
            appPath == "/sdcard" ||
                appPath?.startsWith("/sdcard/") == true ||
                appPath == "/storage/emulated/0" ||
                appPath?.startsWith("/storage/emulated/0/") == true -> SourceKind.INTERNAL
            rawUri.startsWith("content://") -> SourceKind.DOCUMENT_PROVIDER
            mode == LocationMode.MEDIA_STORE -> SourceKind.MEDIA_STORE
            else -> SourceKind.UNKNOWN
        }
    }

    private fun appFacingPath(rawUri: String): String? {
        val uri = runCatching { Uri.parse(rawUri) }.getOrNull()
        val rawPath =
            when {
                rawUri.startsWith("/") -> rawUri
                uri?.scheme == "file" -> uri.path
                uri?.scheme.isNullOrEmpty() -> uri?.path
                uri?.scheme == "content" &&
                    uri.authority == "com.android.externalstorage.documents" ->
                    externalStorageTreePath(uri)
                else -> null
            } ?: return null
        return normalisePath(rawPath)
    }

    private fun externalStorageTreePath(uri: Uri): String? {
        val treeIndex = uri.pathSegments.indexOf("tree")
        if (treeIndex < 0 || treeIndex + 1 >= uri.pathSegments.size) return null
        val treeId = Uri.decode(uri.pathSegments[treeIndex + 1])
        val parts = treeId.split(':', limit = 2)
        val volume = parts.firstOrNull()?.takeIf { it.isNotBlank() } ?: return null
        val relative = parts.getOrNull(1).orEmpty().trim('/')
        val root = if (volume == "primary") "/storage/emulated/0" else "/storage/$volume"
        return if (relative.isEmpty()) root else "$root/$relative"
    }

    private fun stableId(value: String): String =
        "source:" + digest(value).take(12).joinToString("") { "%02x".format(it) }

    private fun stableLong(value: String): Long {
        val bytes = digest(value)
        var result = 0L
        for (index in 0 until Long.SIZE_BYTES) {
            result = (result shl 8) or (bytes[index].toLong() and 0xffL)
        }
        return result
    }

    private fun digest(value: String): ByteArray =
        MessageDigest.getInstance("SHA-256").digest(value.toByteArray(Charsets.UTF_8))

    private companion object {
        val UUID_STORAGE = Regex("^/storage/[0-9a-fA-F]{4}-[0-9a-fA-F]{4}(/.*)?$")

        fun normalisePath(value: String): String? {
            var clean = value.trim().replace('\\', '/').replace(Regex("/+$"), "")
            if (clean.isEmpty() || !clean.startsWith('/')) return null
            if (
                clean.contains("/../") ||
                    clean.endsWith("/..") ||
                    clean.contains("/./") ||
                    clean.endsWith("/.")
            ) {
                return null
            }
            val mediaRw = Regex("^/mnt/media_rw/(usbdisk\\d+)(/.*)?$", RegexOption.IGNORE_CASE)
            val match = mediaRw.matchEntire(clean)
            if (match != null) {
                clean = "/storage/${match.groupValues[1]}${match.groupValues[2]}"
            }
            return clean
        }
    }
}
