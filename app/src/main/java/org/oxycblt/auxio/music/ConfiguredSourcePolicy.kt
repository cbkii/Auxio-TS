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
import javax.inject.Inject
import javax.inject.Singleton
import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.auxio.music.locations.MusicSourcePathNormalizer

/** Operation-scoped source authority derived only from the user's configured music locations. */
@Singleton
class ConfiguredSourcePolicy @Inject constructor(private val settings: MusicSettings) {

    enum class SourceKind {
        INTERNAL,
        REMOVABLE,
        PROVIDER,
    }

    enum class Availability {
        AVAILABLE,
        UNAVAILABLE,
        PROVIDER_MANAGED,
    }

    data class Source(
        val id: String,
        val uri: Uri,
        val fileRoot: File?,
        val kind: SourceKind,
        val availability: Availability,
    )

    /** Immutable policy snapshot used for one browse, restore, or indexing decision. */
    data class Snapshot(
        val locationMode: LocationMode,
        val sources: List<Source>,
        val configurationRevision: Long,
    ) {
        val configuredFileRoots: Map<String, File> =
            sources
                .mapNotNull { it.fileRoot }
                .distinctBy { it.absolutePath }
                .associateBy { it.absolutePath }

        val isUsbConfigured: Boolean
            get() = sources.any { it.kind == SourceKind.REMOVABLE }
    }

    val locationMode: LocationMode
        get() = settings.locationMode

    val isUsbConfigured: Boolean
        get() = snapshot().isUsbConfigured

    /** Unconfigured removable roots are never a background source authority. */
    val allowUnconfiguredUsbDiscovery: Boolean
        get() = false

    fun snapshot(): Snapshot {
        val mode = settings.locationMode
        val configuredUris = settings.safQuery.source.map { it.uri }
        val sources =
            configuredUris.mapIndexed { index, uri ->
                val fileRoot = configuredFileRoot(uri, mode)
                val kind =
                    when {
                        isUsbUri(uri) || fileRoot?.let(::isRemovablePath) == true ->
                            SourceKind.REMOVABLE
                        fileRoot != null -> SourceKind.INTERNAL
                        else -> SourceKind.PROVIDER
                    }
                val availability =
                    when {
                        fileRoot == null -> Availability.PROVIDER_MANAGED
                        runCatching {
                                fileRoot.exists() && fileRoot.isDirectory && fileRoot.canRead()
                            }
                            .getOrDefault(false) -> Availability.AVAILABLE
                        else -> Availability.UNAVAILABLE
                    }
                Source(
                    id = "${mode.name}:$index:${uri}",
                    uri = uri,
                    fileRoot = fileRoot,
                    kind = kind,
                    availability = availability,
                )
            }
        return Snapshot(mode, sources, configurationRevision(mode, configuredUris))
    }

    /** Compatibility view for DirectFS and raw Fast Resume callers. */
    fun getConfiguredRootsAsFiles(): Map<String, File> = snapshot().configuredFileRoots

    private fun configuredFileRoot(uri: Uri, mode: LocationMode): File? {
        if (mode == LocationMode.DIRECT_FS) {
            val normalised =
                MusicSourcePathNormalizer.normalizePersistedLocation(
                    uri.toString(),
                    fileOnly = true,
                ) ?: return null
            return normaliseConfiguredRoot(normalised)
        }
        return when (uri.scheme?.lowercase()) {
            "file" -> normaliseConfiguredRoot(uri.toString())
            null,
            "" -> uri.path?.let(::normaliseConfiguredRoot)
            else -> null
        }
    }

    internal companion object {
        private val UUID_STORAGE_PATH = Regex("^/storage/[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}(/.*)?$")
        private val UUID_VOLUME_TOKEN = Regex("(?:^|[/=:])[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}(?=[:/]|$)")
        private val USB_DISK_PATH = Regex("^/storage/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE)
        private val MEDIA_RW_USB_PATH =
            Regex("^/mnt/media_rw/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE)

        fun isUsbUri(uri: Uri): Boolean {
            val decoded = Uri.decode(uri.toString()).replace('\\', '/')
            val path = Uri.decode(uri.path.orEmpty()).replace('\\', '/')
            if (decoded.contains("usbdisk", ignoreCase = true)) return true
            if (MEDIA_RW_USB_PATH.matches(path.trimEnd('/'))) return true
            if (UUID_STORAGE_PATH.matches(path.trimEnd('/'))) return true
            if (
                uri.authority == "com.android.externalstorage.documents" &&
                    UUID_VOLUME_TOKEN.containsMatchIn(decoded)
            ) {
                return true
            }
            return false
        }

        fun normaliseConfiguredRoot(rawPath: String): File? {
            val trimmed = rawPath.trim()
            if (trimmed.isEmpty()) return null
            val decodedPath =
                when {
                    trimmed.startsWith("file:", ignoreCase = true) ->
                        runCatching { Uri.parse(trimmed).path }.getOrNull()
                    trimmed.startsWith('/') -> trimmed
                    else -> null
                } ?: return null
            val clean = decodedPath.replace('\\', '/').trimEnd('/').ifEmpty { "/" }
            if (containsDotSegment(clean)) return null

            val appFacing = toAppFacingPath(clean)
            val canonical =
                runCatching { File(appFacing).canonicalFile }
                    .getOrElse { File(appFacing).absoluteFile }
            val canonicalPath = toAppFacingPath(canonical.absolutePath.replace('\\', '/'))
            if (!isAllowedRoot(canonicalPath)) return null
            return File(canonicalPath)
        }

        private fun containsDotSegment(path: String): Boolean =
            path.contains("/../") ||
                path.endsWith("/..") ||
                path.contains("/./") ||
                path.endsWith("/.")

        private fun toAppFacingPath(path: String): String {
            val clean = path.trimEnd('/').ifEmpty { "/" }
            val match =
                Regex("^/mnt/media_rw/(usbdisk\\d+)(/.*)?$", RegexOption.IGNORE_CASE)
                    .matchEntire(clean)
            return if (match != null) {
                "/storage/${match.groupValues[1]}${match.groupValues[2]}"
            } else {
                clean
            }
        }

        private fun isAllowedRoot(path: String): Boolean =
            path == "/sdcard" ||
                path.startsWith("/sdcard/") ||
                path == "/storage/emulated/0" ||
                path.startsWith("/storage/emulated/0/") ||
                USB_DISK_PATH.matches(path) ||
                UUID_STORAGE_PATH.matches(path)

        private fun isRemovablePath(file: File): Boolean {
            val path = file.absolutePath.replace('\\', '/').trimEnd('/')
            return USB_DISK_PATH.matches(path) || UUID_STORAGE_PATH.matches(path)
        }

        private fun configurationRevision(mode: LocationMode, uris: List<Uri>): Long {
            var hash = 1125899906842597L
            val text = buildString {
                append(mode.name)
                uris.forEach {
                    append('\u0000')
                    append(it.toString())
                }
            }
            text.forEach { hash = hash * 31L + it.code }
            return hash
        }
    }
}
