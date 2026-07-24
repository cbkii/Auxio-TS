/*
 * Copyright (c) 2026 Auxio Project
 * RootStorageAccelerationPolicy.kt is part of Auxio.
 */
package org.oxycblt.auxio.headunit.root.storage

/** Ordering decision for one explicit TS18 source-resolution attempt. */
enum class RootStorageResolutionOrder {
    /** A validated app-private prepared-volume record is the cheapest available authority hint. */
    CACHED_ROOT_METADATA_FIRST,

    /** Raw/prepared storage needs the already-granted helper before a direct app-UID attempt. */
    REFRESHED_ROOT_METADATA_FIRST,

    /** An ordinary app-readable path is expected to be cheaper than starting a root process. */
    DIRECT_FIRST,
}

/**
 * Chooses the lowest expected-cost safe route instead of imposing a blanket direct-first rule.
 *
 * Cached prepared metadata is usable without `su` and can provide an O(1) representative-file
 * validation. A fresh root helper run leads only when root is already granted and the requested
 * path is itself a raw backing/prepared alias. Normal `/storage/...` paths without a cached record
 * remain direct-first because starting `su` would add overhead without evidence of benefit.
 */
object RootStorageAccelerationPolicy {
    fun choose(
        requestedPath: String,
        rootEnabled: Boolean,
        rootAvailable: Boolean,
        hasCachedRecord: Boolean,
    ): RootStorageResolutionOrder {
        if (!rootEnabled) return RootStorageResolutionOrder.DIRECT_FIRST
        if (hasCachedRecord) return RootStorageResolutionOrder.CACHED_ROOT_METADATA_FIRST
        if (rootAvailable && isRootDependentPath(requestedPath)) {
            return RootStorageResolutionOrder.REFRESHED_ROOT_METADATA_FIRST
        }
        return RootStorageResolutionOrder.DIRECT_FIRST
    }

    fun isRemovablePath(path: String): Boolean {
        val clean = path.replace('\\', '/').trimEnd('/')
        return clean.startsWith("/storage/usbdisk", ignoreCase = true) ||
            clean.startsWith("/mnt/media_rw/usbdisk", ignoreCase = true) ||
            clean.startsWith("/storage/auxio-root/usbdisk", ignoreCase = true) ||
            STORAGE_UUID.matches(clean)
    }

    private fun isRootDependentPath(path: String): Boolean {
        val clean = path.replace('\\', '/').trimEnd('/')
        return clean.startsWith("/mnt/media_rw/usbdisk", ignoreCase = true) ||
            clean.startsWith("/storage/auxio-root/usbdisk", ignoreCase = true)
    }

    private val STORAGE_UUID = Regex("^/storage/[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}(/.*)?$")
}
