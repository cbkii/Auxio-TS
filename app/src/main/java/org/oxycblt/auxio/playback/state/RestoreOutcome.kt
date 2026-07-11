/*
 * Copyright (c) 2026 Auxio Project
 * RestoreOutcome.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.state

/**
 * Describes the outcome of a [DeferredPlayback.RestoreState] request.
 *
 * @author Auxio-TS contributors
 */
enum class RestoreOutcome {
    /** Restoration has not been requested or has not begun. */
    NOT_REQUESTED,
    /** Player is waiting for library readiness or an internal state before continuing. */
    WAITING_FOR_PLAYER,
    /** Player is waiting for the library cache to be scanned/ready. */
    WAITING_FOR_LIBRARY,
    /** Raw TS18 fast-resume has been successfully activated. */
    RAW_FAST_RESUME_ACTIVE,
    /** An existing normal playback session was restored. */
    RESTORED_EXISTING_SESSION,
    /** No previous session existed, so a fallback queue (e.g. Shuffle All) was created. */
    FALLBACK_QUEUE_CREATED,
    /** No previous session existed and no fallback queue was created. */
    NO_SAVED_SESSION,
    /** Restoration failed entirely. */
    FAILED,
    /** Restoration request was cancelled. */
    CANCELLED,
}
