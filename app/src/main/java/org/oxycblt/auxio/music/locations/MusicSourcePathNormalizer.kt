/*
 * Copyright (c) 2026 Auxio Project
 * MusicSourcePathNormalizer.kt is part of Auxio.
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
import java.io.File
import org.oxycblt.musikr.fs.CanonicalSourcePolicy
import timber.log.Timber as L

/** Mode-aware persisted Music Source normalisation used before runtime backend creation. */
internal object MusicSourcePathNormalizer {
    fun normalizePersistedLocation(value: String, fileOnly: Boolean): String? {
        val trimmed = value.trim()
        if (trimmed.isEmpty()) return null
        val uri = trimmed.toUri()
        val repaired = repairDuplicatedStoragePath(uri)
        val candidate = repaired ?: uri
        if (!fileOnly) {
            val canonical =
                when {
                    candidate.scheme.isNullOrEmpty() &&
                        candidate.path?.startsWith("/") == true ->
                        CanonicalSourcePolicy.canonicalUriString(
                            Uri.fromFile(File(requireNotNull(candidate.path))).toString()
                        )
                    else -> CanonicalSourcePolicy.canonicalUriString(candidate.toString())
                }
            if (canonical == null) {
                L.w("Skipping malformed music source URI: $candidate")
            }
            return canonical
        }

        val fileUri =
            when (candidate.scheme) {
                "file" -> candidate
                null,
                "" -> candidate.path?.takeIf { it.startsWith("/") }?.let { Uri.fromFile(File(it)) }
                "content" -> externalStorageTreeToFileUri(candidate)
                else -> null
            }?.let(::repairAndCanonicalizeFileUri)
        if (fileUri?.path == null) return null
        if (fileUri != candidate) {
            L.i("Normalised DirectFS source $candidate -> $fileUri")
        }
        return fileUri.toString()
    }

    /**
     * Canonicalises a file URI through the shared policy.
     *
     * Canonicalising an app-facing vold path may resolve it back onto `/mnt/media_rw`, so the
     * result is normalised again: the ordinary app UID can only enumerate and play the app-facing
     * namespace.
     */
    private fun repairAndCanonicalizeFileUri(uri: Uri): Uri? {
        val rawPath = uri.path ?: return null
        val normalized =
            CanonicalSourcePolicy.normalizePath(rawPath)
                ?: run {
                    L.w("Skipping unusable DirectFS source path: $uri")
                    return null
                }
        val canonical =
            try {
                File(normalized).canonicalFile
            } catch (_: Exception) {
                File(normalized).absoluteFile
            }
        val appFacing =
            CanonicalSourcePolicy.normalizePath(canonical.absolutePath) ?: return null
        return Uri.fromFile(File(appFacing))
    }

    /** Legacy alias-collapse entry point, retained for callers that only hold a raw path. */
    internal fun normaliseSharedStorageAlias(path: String): String =
        CanonicalSourcePolicy.normalizePath(path) ?: path.trimEnd('/').ifEmpty { "/" }

    fun repairDuplicatedStoragePath(uri: Uri): Uri? {
        val path = uri.path ?: return null
        val repaired = CanonicalSourcePolicy.normalizePath(path) ?: return null
        if (repaired == path) return null
        val repairedUri =
            if (uri.scheme == "file") Uri.fromFile(File(repaired)) else repaired.toUri()
        L.i("Repaired music source path $uri -> $repairedUri")
        return repairedUri
    }

    private fun externalStorageTreeToFileUri(uri: Uri): Uri? {
        val path = CanonicalSourcePolicy.externalStorageTreePath(uri) ?: return null
        return Uri.fromFile(File(path))
    }
}
