/*
 * Copyright (c) 2026 Auxio Project
 * RootStorageCommandPolicy.kt is part of Auxio.
 */
package org.oxycblt.auxio.headunit.root

/** Builds the only recursive root command accepted by the storage root gate. */
object RootStorageCommandPolicy {
    private val usb = Regex("^/storage/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE)
    private val rawUsb = Regex("^/mnt/media_rw/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE)
    private val prepared =
        Regex("^/storage/auxio-root/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE)
    private val uuid = Regex("^/storage/[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}(/.*)?$")

    fun isAllowedStorageRoot(value: String): Boolean {
        val path = value.replace('\\', '/').trimEnd('/').ifEmpty { "/" }
        if (
            path.contains('\u0000') ||
                path.contains('\n') ||
                path.contains('\r') ||
                path.contains('\t') ||
                path.contains("/../") ||
                path.endsWith("/..") ||
                path.contains("/./") ||
                path.endsWith("/.")
        ) {
            return false
        }
        return path == "/sdcard" ||
            path.startsWith("/sdcard/") ||
            path == "/storage/emulated/0" ||
            path.startsWith("/storage/emulated/0/") ||
            usb.matches(path) ||
            rawUsb.matches(path) ||
            prepared.matches(path) ||
            uuid.matches(path)
    }

    fun buildSnapshotCommand(rootPath: String, maxDepth: Int): String {
        require(isAllowedStorageRoot(rootPath)) { "unsafe root storage path" }
        require(maxDepth in 1..32) { "invalid snapshot depth" }
        val quoted = shellQuote(rootPath.trimEnd('/'))
        return "root=$quoted; [ -d \"\$root\" ] || exit 4; " +
            "find \"\$root\" -xdev -mindepth 1 -maxdepth $maxDepth -print 2>/dev/null | " +
            "while IFS= read -r p; do " +
            "rel=\${p#\"\$root\"/}; [ -n \"\$rel\" ] || continue; " +
            "t=f; [ -d \"\$p\" ] && t=d; [ -L \"\$p\" ] && t=l; " +
            "m=\$(stat -c %Y \"\$p\" 2>/dev/null || echo 0); " +
            "s=\$(stat -c %s \"\$p\" 2>/dev/null || echo 0); " +
            "printf '%s\\t%s\\t%s\\t%s\\n' \"\$t\" \"\$m\" \"\$s\" \"\$rel\"; " +
            "done"
    }

    internal fun shellQuote(value: String): String = "'${value.replace("'", "'\"'\"'")}'"
}
