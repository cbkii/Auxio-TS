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
import org.oxycblt.auxio.MainActivity

/** Non-exported entry: only this app or a genuinely privileged root caller can prepare it. */
class LauncherPreparationActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(
            LauncherPreparation.issue()
                .setClass(this, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        finish()
    }
}

/** One-use, process-local authority; an extra on the exported launcher alias is insufficient. */
internal object LauncherPreparation {
    private const val ACTION = "org.oxycblt.auxio.action.PREPARE_HOME"
    private const val EXTRA = "org.oxycblt.auxio.extra.PREPARE_NONCE"
    private var nonce: String? = null
    private var deadline = 0L

    fun requested(intent: Intent?): Boolean = intent?.action == ACTION

    @Synchronized
    fun issue(): Intent {
        val issued = java.util.UUID.randomUUID().toString()
        nonce = issued
        deadline = SystemClock.elapsedRealtime() + 10_000L
        return Intent(ACTION).putExtra(EXTRA, issued)
    }

    @Synchronized
    fun consume(intent: Intent?): Boolean {
        if (intent?.action != ACTION) return false
        val offered = intent.getStringExtra(EXTRA)
        intent.removeExtra(EXTRA)
        if (offered == null || offered != nonce || SystemClock.elapsedRealtime() >= deadline) {
            return false
        }
        nonce = null
        deadline = 0L
        return true
    }
}
