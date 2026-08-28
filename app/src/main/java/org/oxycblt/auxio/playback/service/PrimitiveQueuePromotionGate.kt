/*
 * Copyright (c) 2026 Auxio Project
 * PrimitiveQueuePromotionGate.kt is part of Auxio.
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
 * Small state machine for promoting the pre-library primitive queue to its hydrated canonical
 * representation.
 *
 * Library readiness alone never requests promotion. A natural track boundary or playback-semantic
 * interaction does. Queue mutations may be deferred while preparation is still in flight; simple
 * play/seek/repeat state changes can execute immediately and let the caller carry their resulting
 * state into the eventual promotion.
 */
internal class PrimitiveQueuePromotionGate {
    data class Key(val sessionId: Long, val revision: Long)

    enum class Decision {
        /** No canonical promotion is currently possible; keep the primitive path fail-open. */
        BYPASS,
        /** Canonical data is not prepared yet; remember this boundary and prepare it. */
        PREPARE,
        /** Canonical data is prepared for this exact queue authority; promote now. */
        PROMOTE,
    }

    private var preparedKey: Key? = null
    private var failedKey: Key? = null
    private var boundaryKey: Key? = null

    /** A newly committed library generation may make a previously unresolved queue hydratable. */
    fun onLibraryChanged(key: Key) {
        if (preparedKey == key) preparedKey = null
        if (failedKey == key) failedKey = null
    }

    fun onPrepared(key: Key): Boolean {
        preparedKey = key
        if (failedKey == key) failedKey = null
        return boundaryKey == key
    }

    fun onFailed(key: Key) {
        if (preparedKey == key) preparedKey = null
        failedKey = key
    }

    fun requestBoundary(key: Key, libraryReady: Boolean): Decision {
        if (!libraryReady || failedKey == key) return Decision.BYPASS
        boundaryKey = key
        return if (preparedKey == key) Decision.PROMOTE else Decision.PREPARE
    }

    fun isPrepared(key: Key): Boolean = preparedKey == key

    fun clearBoundary(key: Key) {
        if (boundaryKey == key) boundaryKey = null
    }

    fun clear() {
        preparedKey = null
        failedKey = null
        boundaryKey = null
    }
}
