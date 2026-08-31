/*
 * Copyright (c) 2026 Auxio Project
 * SeamlessQueueHandoffPolicy.kt is part of Auxio.
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
 * Computes the non-current playlist edits used to make a canonical queue surround the already
 * decoding Fast Resume item.
 *
 * The current player item is deliberately retained rather than replaced, cleared, sought or
 * re-prepared. The caller removes only items before/after it, then inserts canonical neighbours.
 */
internal object SeamlessQueueHandoffPolicy {
    data class Plan(
        val originalCurrentIndex: Int,
        val originalItemCount: Int,
        val canonicalItemCount: Int,
        val targetCurrentIndex: Int,
        val prependCount: Int,
        val appendCount: Int,
    )

    fun plan(
        originalItemCount: Int,
        originalCurrentIndex: Int,
        canonicalItemCount: Int,
        targetCurrentIndex: Int,
    ): Plan? {
        if (originalItemCount <= 0 || canonicalItemCount <= 0) return null
        if (originalCurrentIndex !in 0 until originalItemCount) return null
        if (targetCurrentIndex !in 0 until canonicalItemCount) return null
        return Plan(
            originalCurrentIndex = originalCurrentIndex,
            originalItemCount = originalItemCount,
            canonicalItemCount = canonicalItemCount,
            targetCurrentIndex = targetCurrentIndex,
            prependCount = targetCurrentIndex,
            appendCount = canonicalItemCount - targetCurrentIndex - 1,
        )
    }
}
