/*
 * Copyright (c) 2026 Auxio Project
 * SourceAuthorityScopePolicy.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.music

import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.musikr.cache.IncrementalScanPlan

/**
 * Keeps explicit-root request scope separate from provider-discovered source authority.
 *
 * SAF and DirectFS derive their current source universe from configured roots. MediaStore derives
 * its source universe from provider volumes discovered during preflight, so an empty explicit-root
 * set must never mean "scan zero MediaStore sources".
 */
internal object SourceAuthorityScopePolicy {
    fun normalizeRequestedSourceKeys(requestedSourceKeys: Set<String>?): Set<String>? =
        requestedSourceKeys?.takeIf { it.isNotEmpty() }

    fun allowExplicitEmptySourceSet(
        locationMode: LocationMode,
        hasCheckpointAuthority: Boolean,
        originalRequestedSourceKeys: Set<String>?,
        configuredSourceKeys: Set<String>,
    ): Boolean =
        locationMode != LocationMode.MEDIA_STORE &&
            hasCheckpointAuthority &&
            originalRequestedSourceKeys?.isEmpty() == true &&
            configuredSourceKeys.isEmpty()

    fun effectiveAttemptedSourceKeys(
        locationMode: LocationMode,
        requestedSourceKeys: Set<String>?,
        configuredSourceKeys: Set<String>,
        plan: IncrementalScanPlan?,
    ): Set<String> {
        requestedSourceKeys?.let { return it }
        if (locationMode != LocationMode.MEDIA_STORE) return configuredSourceKeys
        val currentPlan = plan ?: return emptySet()
        return linkedSetOf<String>().apply {
            addAll(currentPlan.scanSourceKeys)
            addAll(currentPlan.reuseSourceKeys)
            addAll(currentPlan.unavailableSourceKeys)
        }
    }
}
