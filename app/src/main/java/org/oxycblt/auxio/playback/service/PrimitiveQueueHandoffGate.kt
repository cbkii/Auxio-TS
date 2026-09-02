/*
 * Copyright (c) 2026 Auxio Project
 * PrimitiveQueueHandoffGate.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.service

/**
 * Arbitration state for the short interval between Fast Resume playback and automatic canonical
 * queue takeover.
 *
 * Library readiness itself owns the normal handoff trigger. This gate only tells a racing
 * queue-affecting user interaction whether it can run immediately, should wait for the in-flight
 * canonical preparation, or must fail open to primitive behaviour because this exact queue revision
 * could not be hydrated safely.
 */
internal class PrimitiveQueueHandoffGate {
    data class Key(val sessionId: Long, val revision: Long)

    enum class Decision {
        /** Canonical takeover is not currently safe; keep the primitive path fail-open. */
        BYPASS,
        /** Canonical data is not prepared yet; wait for preparation and replay exactly once. */
        PREPARE,
        /** Canonical data is prepared for this exact queue authority; commit before the action. */
        PROMOTE,
    }

    private var preparedKey: Key? = null
    private var failedKey: Key? = null

    /** A newly committed library generation may make a previously unresolved queue hydratable. */
    fun onLibraryChanged(key: Key) {
        if (preparedKey == key) preparedKey = null
        if (failedKey == key) failedKey = null
    }

    fun onPrepared(key: Key) {
        preparedKey = key
        if (failedKey == key) failedKey = null
    }

    fun onFailed(key: Key) {
        if (preparedKey == key) preparedKey = null
        failedKey = key
    }

    fun requestHandoff(key: Key, libraryReady: Boolean): Decision {
        if (!libraryReady || failedKey == key) return Decision.BYPASS
        return if (preparedKey == key) Decision.PROMOTE else Decision.PREPARE
    }

    fun isPrepared(key: Key): Boolean = preparedKey == key

    fun clear() {
        preparedKey = null
        failedKey = null
    }
}
