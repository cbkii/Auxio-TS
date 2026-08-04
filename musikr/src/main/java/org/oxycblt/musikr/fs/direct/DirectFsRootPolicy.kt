/*
 * Copyright (c) 2026 Auxio Project
 * DirectFsRootPolicy.kt is part of Auxio.
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

package org.oxycblt.musikr.fs.direct

import java.io.File as JavaFile
import org.oxycblt.musikr.fs.CanonicalSourcePolicy

/**
 * Which filesystem roots DirectFS may ever traverse.
 *
 * Ordinary app-UID access is the authority for both scanning and playback, so only the app-facing
 * storage namespace is accepted. Privileged backing mounts are accepted solely as an alias that
 * [CanonicalSourcePolicy] rewrites onto the app-facing path; they never become a traversal root in
 * their raw form.
 */
internal object DirectFsRootPolicy {
    private val PROTECTED_ROOTS =
        listOf("/", "/system", "/vendor", "/data", "/proc", "/sys", "/dev", "/acct", "/config")

    /** Whether [path] is an acceptable DirectFS traversal root. */
    fun isAllowedPath(path: String): Boolean {
        val clean = path.trimEnd('/')
        if (clean.isBlank()) return false
        if (PROTECTED_ROOTS.any { clean == it.trimEnd('/') }) return false
        if (
            clean.startsWith("/data/") ||
                clean.startsWith("/system/") ||
                clean.startsWith("/vendor/")
        ) {
            return false
        }
        return clean.startsWith("/storage/") || clean.startsWith("/mnt/media_rw/")
    }

    /** Whether [file] resolves to an acceptable DirectFS traversal root. */
    fun isAllowedRoot(file: JavaFile): Boolean =
        canonicalPathOrNull(file)?.let(::isAllowedPath) == true

    /**
     * Whether [directory] is one of the platform-restricted children that primary shared storage
     * always refuses, and which therefore never indicates a broken source.
     */
    fun isExpectedRestrictedSharedStorageChild(
        directory: JavaFile,
        canonicalRoot: JavaFile,
    ): Boolean {
        if (canonicalRoot.path.trimEnd('/') != "/storage/emulated/0") return false
        val canonicalDirectory = canonicalPathOrNull(directory) ?: return false
        val rootPath = canonicalRoot.path.trimEnd('/')
        val relative = canonicalDirectory.removePrefix(rootPath).trimStart('/')
        return relative == "Android/data" ||
            relative.startsWith("Android/data/") ||
            relative == "Android/obb" ||
            relative.startsWith("Android/obb/")
    }

    fun canonicalPathOrNull(file: JavaFile): String? =
        try {
            file.canonicalPath
        } catch (_: Exception) {
            null
        }
}
