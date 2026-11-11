/*
 * Copyright (c) 2026 Auxio Project
 * HomePlayDeadlineGate.kt is part of Auxio.
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

/** One-use admission at decoder readiness. The main-thread playback owner serialises this gate. */
internal class HomePlayDeadlineGate(private val elapsedMs: () -> Long) {
    private var deadline: Long? = null
    val expiresAt: Long?
        get() = deadline

    fun permitsCommit(until: Long): Boolean = elapsedMs() < until

    fun arm(until: Long) {
        deadline = until.takeIf { it > elapsedMs() }
    }

    fun cancel() {
        deadline = null
    }

    fun expire(until: Long) {
        if (deadline == until && elapsedMs() >= until) deadline = null
    }

    fun consumeIfReady(ready: Boolean): Boolean {
        val until = deadline ?: return false
        if (elapsedMs() >= until) {
            cancel()
            return false
        }
        if (!ready) return false
        cancel()
        return true
    }
}
