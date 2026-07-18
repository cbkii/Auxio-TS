/*
 * Copyright (c) 2026 Auxio Project
 * ObservationBurstGate.kt is part of Auxio.
 */

package org.oxycblt.auxio.music.service

/** Latest-wins token used to conflate rapid source observer notifications. */
internal class ObservationBurstGate {
    private var generation = 0L

    @Synchronized fun nextToken(): Long = ++generation

    @Synchronized fun isLatest(token: Long): Boolean = token == generation
}
