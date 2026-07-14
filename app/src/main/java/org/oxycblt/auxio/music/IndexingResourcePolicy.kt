/*
 * Copyright (c) 2026 Auxio Project
 * IndexingResourcePolicy.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.music

/** User-facing scan priority used to resolve immutable Musikr worker counts per scan. */
enum class ScanPriority {
    PLAYBACK_FIRST,
    BALANCED,
    FAST_SCAN;

    companion object {
        fun fromName(name: String?) = entries.firstOrNull { it.name == name }
    }
}

/** App-layer indexing resource policy. Resolved once before constructing a Musikr pipeline. */
interface IndexingResourcePolicy {
    fun resolveWorkerCount(
        scanPriority: ScanPriority,
        playbackActive: Boolean,
        isTopwayVariant: Boolean,
        availableProcessors: Int,
    ): Int
}

object DefaultIndexingResourcePolicy : IndexingResourcePolicy {
    override fun resolveWorkerCount(
        scanPriority: ScanPriority,
        playbackActive: Boolean,
        isTopwayVariant: Boolean,
        availableProcessors: Int,
    ): Int {
        val processors = availableProcessors.coerceAtLeast(1)
        val requested =
            when (scanPriority) {
                ScanPriority.PLAYBACK_FIRST -> if (playbackActive) 1 else 2
                ScanPriority.BALANCED -> if (playbackActive) 2 else 3
                ScanPriority.FAST_SCAN -> processors.coerceAtMost(4)
            }
        val topwayCap = if (isTopwayVariant) 4 else processors.coerceAtMost(8)
        return requested.coerceIn(1, topwayCap.coerceAtLeast(1))
    }
}
