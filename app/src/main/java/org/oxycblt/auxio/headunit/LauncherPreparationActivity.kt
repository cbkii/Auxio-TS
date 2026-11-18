/*
 * Copyright (c) 2026 Auxio Project
 * LauncherPreparationActivity.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import org.oxycblt.auxio.AuxioService
import org.oxycblt.auxio.IntegerTable
import org.oxycblt.auxio.headunit.topway.TopwayServiceBridge

/** Non-exported entry: only this app or a genuinely privileged root caller can prepare it. */
class LauncherPreparationActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val now = SystemClock.elapsedRealtime()
        val until = intent.getLongExtra(EXTRA_UNTIL, now + 10_000L)
        if (until <= now) {
            finish()
            return
        }
        // The already-authorised preparation entry starts only the existing service from
        // our own Activity UID. MainActivity/Compose, browsing and autoplay are unnecessary.
        startService(
            Intent(this, TopwayServiceBridge.resolveCompatServiceClass(AuxioService::class.java))
                .setAction(AuxioService.ACTION_START)
                .putExtra(AuxioService.INTENT_KEY_START_ID, IntegerTable.START_ID_BACKGROUND_READY)
        )
        finish()
    }

    companion object {
        const val EXTRA_UNTIL = "org.oxycblt.auxio.extra.PREPARE_UNTIL"
        // Old MainActivity handoffs are rejected, including tasks restored after an upgrade.
        internal const val LEGACY_ACTION = "org.oxycblt.auxio.action.PREPARE_HOME"
    }
}
