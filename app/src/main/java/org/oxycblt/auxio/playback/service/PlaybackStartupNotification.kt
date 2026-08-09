/*
 * Copyright (c) 2026 Auxio Project
 * PlaybackStartupNotification.kt is part of Auxio.
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
import androidx.core.app.NotificationCompat
import org.oxycblt.auxio.ForegroundServiceNotification
import org.oxycblt.auxio.IntegerTable
import org.oxycblt.auxio.R

/**
 * Lightweight foreground placeholder used only while a foreground-service start is restoring the
 * real playback/indexing notification state.
 *
 * It deliberately reuses the canonical playback channel and notification ID so it cannot create a
 * second playback notification authority. No artwork, storage or Binder work is performed here.
 */
internal class PlaybackStartupNotification(context: Context) :
    ForegroundServiceNotification(
        context,
        ChannelInfo(id = PlaybackNotificationChannel.id, nameRes = R.string.lbl_playback),
    ) {
    init {
        setSmallIcon(R.drawable.ic_auxio_24)
        setCategory(NotificationCompat.CATEGORY_SERVICE)
        setContentTitle(context.getString(R.string.info_app_name))
        setContentText(context.getString(R.string.lbl_playback_restoring))
        setShowWhen(false)
        setSilent(true)
        setOnlyAlertOnce(true)
        setOngoing(true)
        setAutoCancel(false)
        setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
    }

    override val code: Int
        get() = IntegerTable.PLAYBACK_NOTIFICATION_CODE
}
