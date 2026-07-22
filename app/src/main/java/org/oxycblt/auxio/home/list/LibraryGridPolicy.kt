/*
 * Copyright (c) 2026 Auxio Project
 * LibraryGridPolicy.kt is part of Auxio.
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

package org.oxycblt.auxio.home.list

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/** One presentation-only authority for persisted library grid column values. */
internal object LibraryGridPolicy {
    const val INHERIT = 0
    const val TWO_COLUMNS = 2
    const val THREE_COLUMNS = 3

    fun normalizeDefault(value: Int): Int =
        if (value == THREE_COLUMNS) THREE_COLUMNS else TWO_COLUMNS

    fun normalizeOverride(value: Int): Int =
        when (value) {
            TWO_COLUMNS,
            THREE_COLUMNS -> value
            else -> INHERIT
        }

    fun effective(defaultValue: Int, overrideValue: Int): Int =
        normalizeOverride(overrideValue).takeUnless { it == INHERIT }
            ?: normalizeDefault(defaultValue)
}

/** Relayout without replacing adapters, selection, sorting, playback, or library state. */
internal fun RecyclerView.applyLibraryGridSpanCount(requestedSpanCount: Int) {
    val manager = layoutManager as? GridLayoutManager ?: return
    val spanCount = LibraryGridPolicy.normalizeDefault(requestedSpanCount)
    if (manager.spanCount == spanCount) return

    val firstPosition = manager.findFirstVisibleItemPosition()
    val offset =
        if (firstPosition == RecyclerView.NO_POSITION) {
            0
        } else {
            (manager.findViewByPosition(firstPosition)?.top ?: paddingTop) - paddingTop
        }
    manager.spanCount = spanCount
    if (firstPosition != RecyclerView.NO_POSITION) {
        manager.scrollToPositionWithOffset(firstPosition, offset)
    }
}
