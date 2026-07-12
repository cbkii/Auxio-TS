/*
 * Copyright (c) 2026 Auxio Project
 * RestoreProgress.kt is part of Auxio.
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

/** Tracked outcome for a single startup restore request. */
data class RestoreProgress(val requestId: Long, val outcome: RestoreOutcome)

/** Terminal and transitional outcomes for startup restore tracking. */
enum class RestoreOutcome {
    NOT_REQUESTED,
    WAITING_FOR_PLAYER,
    WAITING_FOR_LIBRARY,
    RAW_FAST_RESUME_ACTIVE,
    RESTORED_EXISTING_SESSION,
    FALLBACK_QUEUE_CREATED,
    NO_SAVED_SESSION,
    FAILED,
    CANCELLED,
}
