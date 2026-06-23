/*
 * Copyright (c) 2026 Auxio Project
 * PerfTimer.kt is part of Auxio.
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

package org.oxycblt.auxio.util

import org.oxycblt.auxio.BuildConfig
import timber.log.Timber

/**
 * A lightweight timing helper for development/debug diagnostics. Instruments key spans without
 * release overhead.
 */
object PerfTimer {
    @PublishedApi internal const val TAG = "AuxioPerf"

    /** Records a timing for a block of code. Only active in debug builds. */
    inline fun <T> trace(label: String, block: () -> T): T {
        if (!BuildConfig.DEBUG) return block()

        val start = System.currentTimeMillis()
        return try {
            block()
        } finally {
            val end = System.currentTimeMillis()
            Timber.tag(TAG).d("$label took ${end - start}ms")
        }
    }

    /** Records a timing for a suspend block of code. Only active in debug builds. */
    suspend inline fun <T> traceSuspend(label: String, crossinline block: suspend () -> T): T {
        if (!BuildConfig.DEBUG) return block()

        val start = System.currentTimeMillis()
        return try {
            block()
        } finally {
            val end = System.currentTimeMillis()
            Timber.tag(TAG).d("$label took ${end - start}ms")
        }
    }

    /** Records a point in time with a label. */
    fun point(label: String) {
        if (!BuildConfig.DEBUG) return
        Timber.tag(TAG).d("POINT: $label at ${System.currentTimeMillis()}ms")
    }
}
