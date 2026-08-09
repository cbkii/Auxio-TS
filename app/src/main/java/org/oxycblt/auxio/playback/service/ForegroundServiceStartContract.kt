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

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import org.oxycblt.auxio.AuxioService
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.IntegerTable

/** Explicit contract for playback starts issued through ContextCompat.startForegroundService(). */
internal object ForegroundServiceStartContract {
    const val EXTRA_REQUIRE_IMMEDIATE_PROMOTION =
        BuildConfig.APPLICATION_ID + ".service.REQUIRE_IMMEDIATE_FOREGROUND"

    fun start(context: Context, intent: Intent) {
        ContextCompat.startForegroundService(context, markRequired(intent))
    }

    fun markRequired(intent: Intent): Intent =
        intent.putExtra(EXTRA_REQUIRE_IMMEDIATE_PROMOTION, true)

    fun requiresImmediatePromotion(intent: Intent?): Boolean {
        if (intent?.getBooleanExtra(EXTRA_REQUIRE_IMMEDIATE_PROMOTION, false) == true) return true

        // Defence in depth for existing service/PendingIntent adapters that already carry one of
        // Auxio's externally initiated playback identities. This keeps a missed marker from
        // re-introducing the Android foreground-start deadline race while leaving activity/bind
        // starts outside the temporary-foreground path.
        return when (intent?.getIntExtra(AuxioService.INTENT_KEY_START_ID, -1)) {
            IntegerTable.START_ID_BOOT,
            IntegerTable.START_ID_BLUETOOTH,
            IntegerTable.START_ID_MEDIA_BUTTON,
            IntegerTable.START_ID_TOPWAY,
            IntegerTable.START_ID_TASKER -> true
            else -> false
        }
    }
}
