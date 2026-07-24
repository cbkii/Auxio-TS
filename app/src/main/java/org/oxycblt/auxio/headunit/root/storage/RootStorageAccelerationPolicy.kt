/*
 * Copyright (c) 2026 Auxio Project
 * RootStorageAccelerationPolicy.kt is part of Auxio.
 */
package org.oxycblt.auxio.headunit.root.storage

/** Resolution order selected from already available, bounded storage authority. */
enum class RootStorageResolutionOrder {
    CACHED_ROOT_METADATA_FIRST,
    REFRESHED_ROOT_METADATA_FIRST,
    DIRECT_FIRST,
}

/**
 * Select the lowest expected-cost safe source-resolution order.
 *
 * Cached records do not start `su` and may provide an O(1) representative-file hint. A live root
 * refresh leads for raw/prepared paths after root was explicitly enabled; the caller may perform
 * the bounded consent probe in that explicit source flow. Ordinary `/storage` paths remain
 * direct-first when no acceleration evidence exists.
 */
object RootStorageAccelerationPolicy {
    private val rawUsb = Regex("^/mnt/media_rw/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE)
    private val prepared =
        Regex("^/storage/auxio-root/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE)
    private val appUsb = Regex("^/storage/usbdisk\\d+(/.*)?$", RegexOption.IGNORE_CASE)
    private val uuid = Regex("^/storage/[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}(/.*)?$")

    fun choose(
        requestedPath: String,
        rootEnabled: Boolean,
        rootAvailable: Boolean,
        hasCachedRecord: Boolean,
    ): RootStorageResolutionOrder {
        if (!rootEnabled) return RootStorageResolutionOrder.DIRECT_FIRST
        if (hasCachedRecord) return RootStorageResolutionOrder.CACHED_ROOT_METADATA_FIRST
        if (rootAvailable && requiresRootPreparation(requestedPath)) {
            return RootStorageResolutionOrder.REFRESHED_ROOT_METADATA_FIRST
        }
        return RootStorageResolutionOrder.DIRECT_FIRST
    }

    /** Raw backing and prepared-alias paths cannot be usefully resolved without preparation. */
    fun requiresRootPreparation(path: String): Boolean =
        rawUsb.matches(path) || prepared.matches(path)

    fun isRemovablePath(path: String): Boolean =
        appUsb.matches(path) || rawUsb.matches(path) || prepared.matches(path) || uuid.matches(path)
}
