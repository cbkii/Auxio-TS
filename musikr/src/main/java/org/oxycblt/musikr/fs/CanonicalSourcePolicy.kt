/*
 * Copyright (c) 2026 Auxio Project
 * CanonicalSourcePolicy.kt is part of Auxio.
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

/**
 * The single canonical identity, scope and overlap policy for configured music sources.
 *
 * Every boundary that persists, displays, deduplicates or explores a configured source must derive
 * identity from this object so that one physical folder can never be configured, counted,
 * fingerprinted or traversed twice. The rules are pure string policy: no filesystem access happens
 * here, so the same identity is produced on a background scan thread, in the source picker and in
 * persistence read-repair.
 *
 * Normalisation intentionally stays inside the app-facing storage namespace. Raw vold backing paths
 * are collapsed onto their `/storage/...` equivalent because ordinary app-UID access, and therefore
 * playback, is only guaranteed there.
 */
object CanonicalSourcePolicy {
    /** How wide a configured source root is, which decides traversal noise policy and budgets. */
    enum class Scope {
        /** A user-selected folder narrower than a whole volume. Scanned without name filtering. */
        EXPLICIT,
        /** A whole-volume root, which keeps stronger noise exclusions and traversal budgets. */
        WHOLE_VOLUME,
    }

    /** Prefix used for file-backed canonical identities. */
    private const val PATH_IDENTITY_PREFIX = "path:"

    /** Prefix used for provider-backed (document tree) canonical identities. */
    private const val URI_IDENTITY_PREFIX = "uri:"

    private const val PRIMARY_SHARED_STORAGE = "/storage/emulated/0"

    private val PROTECTED_ROOTS =
        listOf("/", "/system", "/vendor", "/data", "/proc", "/sys", "/dev", "/acct", "/config")

    private val PROTECTED_PREFIXES =
        listOf("/data/", "/system/", "/vendor/", "/proc/", "/sys/", "/dev/", "/acct/", "/config/")

    private val UUID_VOLUME = Regex("^[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}$")

    private val USB_VOLUME = Regex("^usbdisk\\d+$", RegexOption.IGNORE_CASE)

    private val BACKING_VOLUME =
        Regex(
            "^/mnt/media_rw/(usbdisk\\d+|[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4})(/.*)?$",
            RegexOption.IGNORE_CASE,
        )

    private val VOLUME_ROOTS =
        listOf(
            Regex("^/storage/emulated/\\d+$"),
            Regex("^/storage/usbdisk\\d+$", RegexOption.IGNORE_CASE),
            Regex("^/storage/auxio-root/usbdisk\\d+$", RegexOption.IGNORE_CASE),
            Regex("^/storage/[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}$"),
        )

    /**
     * Normalises [rawPath] into the app-facing canonical namespace.
     *
     * Returns `null` when the path can never be an ordinary playback source: path traversal
     * segments, protected system roots, root-only backing storage that has no app-facing alias and
     * the filesystem root itself are all rejected.
     */
    fun normalizePath(rawPath: String): String? {
        val unified = rawPath.trim().replace('\\', '/')
        if (unified.isEmpty() || !unified.startsWith('/')) return null
        if (containsDotSegment(unified)) return null
        val collapsed = collapseSeparators(unified)
        val repaired = repairDuplicatedPrefix(collapsed)
        val aliased = normalizeVolumeAlias(repaired)
        if (aliased.isEmpty() || aliased == "/") return null
        if (PROTECTED_ROOTS.any { it == aliased }) return null
        if (PROTECTED_PREFIXES.any { aliased.startsWith(it) }) return null
        // Anything left outside /storage is either root-only backing storage or a namespace the
        // app UID cannot open for playback, so it is never an ordinary source.
        if (!aliased.startsWith("/storage/")) return null
        return aliased
    }

    /** Whether [path] denotes a whole volume rather than a folder inside one. */
    fun isVolumeRoot(path: String): Boolean {
        val normalized = normalizePath(path) ?: return false
        return VOLUME_ROOTS.any { it.matches(normalized) }
    }

    /** The traversal scope implied by [path]. */
    fun scopeOf(path: String): Scope =
        if (isVolumeRoot(path)) Scope.WHOLE_VOLUME else Scope.EXPLICIT

    /** Whether [descendant] is strictly contained by [ancestor] after normalisation. */
    fun isAncestorOf(ancestor: String, descendant: String): Boolean {
        val normalizedAncestor = normalizePath(ancestor) ?: return false
        val normalizedDescendant = normalizePath(descendant) ?: return false
        return normalizedDescendant.startsWith("$normalizedAncestor/")
    }

    /** The canonical identity of a file-backed source, or `null` when [path] is not usable. */
    fun identityForPath(path: String): String? =
        normalizePath(path)?.let { PATH_IDENTITY_PREFIX + it }

    /** The canonical identity of a provider-backed source URI. */
    fun identityForUriString(uri: String): String =
        URI_IDENTITY_PREFIX + uri.trim().trimEnd('/').ifEmpty { uri.trim() }

    /**
     * Collapses exact canonical duplicates, preserving the first configured occurrence.
     *
     * Items without a canonical identity are retained untouched so that unrelated repair or
     * reporting paths still observe them.
     */
    fun <T> collapseDuplicates(items: List<T>, identity: (T) -> String?): List<T> {
        if (items.size < 2) return items
        val seen = HashSet<String>(items.size)
        return items.filter { item ->
            val key = identity(item) ?: return@filter true
            seen.add(key)
        }
    }

    /**
     * Orders sources so that overlapping roots are traversed deterministically.
     *
     * Narrow explicit roots run before whole-volume roots, and deeper roots before shallower ones,
     * so an explicit folder always keeps its own (unfiltered) traversal policy even when a wider
     * root also covers it. A stable secondary ordering keeps runs reproducible.
     */
    fun <T> traversalOrder(items: List<T>, path: (T) -> String?): List<T> =
        items
            .withIndex()
            .sortedWith(
                compareBy(
                    { (_, item) ->
                        val normalized = path(item)?.let(::normalizePath)
                        when {
                            normalized == null -> 2
                            isVolumeRoot(normalized) -> 1
                            else -> 0
                        }
                    },
                    { (_, item) ->
                        -(path(item)?.let(::normalizePath)?.count { it == '/' } ?: 0)
                    },
                    { (index, _) -> index },
                )
            )
            .map { it.value }

    private fun containsDotSegment(path: String): Boolean =
        path.contains("/../") || path.endsWith("/..") || path.contains("/./") || path.endsWith("/.")

    private fun collapseSeparators(path: String): String {
        val collapsed = path.replace(Regex("/{2,}"), "/")
        return if (collapsed.length > 1) collapsed.trimEnd('/').ifEmpty { "/" } else collapsed
    }

    /**
     * Repairs historically mis-joined paths such as
     * `/storage/emulated/0/storage/emulated/0/Music`, which older builds could persist when a
     * volume root was concatenated with an already absolute path.
     */
    private fun repairDuplicatedPrefix(path: String): String {
        val dynamicRoots =
            listOf(
                    Regex("^/storage/usbdisk\\d+", RegexOption.IGNORE_CASE),
                    Regex("^/mnt/media_rw/usbdisk\\d+", RegexOption.IGNORE_CASE),
                    Regex("^/storage/auxio-root/usbdisk\\d+", RegexOption.IGNORE_CASE),
                    Regex("^/storage/[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}"),
                    Regex("^/mnt/media_rw/[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}"),
                )
                .mapNotNull { it.find(path)?.value }
        val prefixes = (listOf(PRIMARY_SHARED_STORAGE, "/sdcard") + dynamicRoots).distinct()
        for (prefix in prefixes) {
            val duplicated = prefix + prefix
            if (path == duplicated || path.startsWith("$duplicated/")) {
                return prefix + path.removePrefix(duplicated)
            }
        }
        return path
    }

    /**
     * Collapses storage aliases onto the app-facing namespace.
     *
     * `/sdcard` is the legacy symlink for primary shared storage, and `/mnt/media_rw/<volume>` is
     * the privileged backing mount of a removable volume that the app UID reaches at
     * `/storage/<volume>`.
     */
    private fun normalizeVolumeAlias(path: String): String {
        if (path == "/sdcard") return PRIMARY_SHARED_STORAGE
        if (path.startsWith("/sdcard/")) {
            return PRIMARY_SHARED_STORAGE + path.removePrefix("/sdcard")
        }
        val backing = BACKING_VOLUME.matchEntire(path)
        if (backing != null) {
            return "/storage/${normalizeVolumeToken(backing.groupValues[1])}${backing.groupValues[2]}"
        }
        return normalizeStorageVolumeToken(path)
    }

    /** Normalises only the volume token of a `/storage/<volume>/...` path. */
    private fun normalizeStorageVolumeToken(path: String): String {
        if (!path.startsWith("/storage/")) return path
        val remainder = path.removePrefix("/storage/")
        val separator = remainder.indexOf('/')
        val token = if (separator == -1) remainder else remainder.substring(0, separator)
        val rest = if (separator == -1) "" else remainder.substring(separator)
        return "/storage/${normalizeVolumeToken(token)}$rest"
    }

    /**
     * Volume identifiers are case-insensitive on Android, while the contents of a volume are not
     * (primary shared storage is case-sensitive), so only the volume token is case-folded.
     */
    private fun normalizeVolumeToken(token: String): String =
        when {
            UUID_VOLUME.matches(token) -> token.uppercase()
            USB_VOLUME.matches(token) -> token.lowercase()
            else -> token
        }
}
