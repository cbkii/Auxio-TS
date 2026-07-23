/*
 * Copyright (c) 2026 Auxio Project
 * MediaButtonIntentFactory.kt is part of Auxio.
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

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import org.oxycblt.auxio.AuxioService
import org.oxycblt.auxio.IntegerTable

/** Builds explicit media-button intents for Auxio's one canonical playback service/session. */
object MediaButtonIntentFactory {
    fun receiverComponent(context: Context): ComponentName =
        ComponentName(context, MediaButtonReceiver::class.java)

    fun receiverIntent(context: Context, keyCode: Int): Intent =
        Intent(Intent.ACTION_MEDIA_BUTTON)
            .setComponent(receiverComponent(context))
            .putExtra(Intent.EXTRA_KEY_EVENT, KeyEvent(KeyEvent.ACTION_DOWN, keyCode))

    fun serviceIntent(context: Context, serviceClass: Class<*>, keyCode: Int): Intent =
        Intent(context, serviceClass)
            .setAction(Intent.ACTION_MEDIA_BUTTON)
            .putExtra(Intent.EXTRA_KEY_EVENT, KeyEvent(KeyEvent.ACTION_DOWN, keyCode))
            .putExtra(AuxioService.INTENT_KEY_START_ID, IntegerTable.START_ID_MEDIA_BUTTON)
}
