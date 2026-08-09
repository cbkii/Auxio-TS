/*
 * Copyright (c) 2026 Auxio Project
 * ForegroundServiceStartContract.kt is part of Auxio.
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

import android.content.Intent
import org.oxycblt.auxio.BuildConfig

/** Explicit contract for starts issued through ContextCompat.startForegroundService(). */
internal object ForegroundServiceStartContract {
    const val EXTRA_REQUIRE_IMMEDIATE_PROMOTION =
        BuildConfig.APPLICATION_ID + ".service.REQUIRE_IMMEDIATE_FOREGROUND"

    fun markRequired(intent: Intent): Intent =
        intent.putExtra(EXTRA_REQUIRE_IMMEDIATE_PROMOTION, true)

    fun requiresImmediatePromotion(intent: Intent?): Boolean =
        intent?.getBooleanExtra(EXTRA_REQUIRE_IMMEDIATE_PROMOTION, false) == true
}
