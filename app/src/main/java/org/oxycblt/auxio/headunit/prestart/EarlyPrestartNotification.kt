/*
 * Copyright (c) 2026 Auxio Project
 * EarlyPrestartNotification.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.prestart

import android.content.Context
import androidx.core.app.NotificationCompat
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.ForegroundServiceNotification
import org.oxycblt.auxio.IntegerTable
import org.oxycblt.auxio.R

/** Temporary low-importance notification used by the existing Auxio service during early prep. */
class EarlyPrestartNotification(context: Context) :
    ForegroundServiceNotification(
        context,
        ChannelInfo(CHANNEL_ID, R.string.notification_early_prestart_channel),
    ) {
    override val code = IntegerTable.EARLY_PRESTART_NOTIFICATION_CODE

    init {
        setSmallIcon(R.drawable.ic_auxio_24)
        setContentTitle(context.getString(R.string.notification_early_prestart_title))
        setContentText(context.getString(R.string.notification_early_prestart_text))
        setCategory(NotificationCompat.CATEGORY_SERVICE)
        setPriority(NotificationCompat.PRIORITY_LOW)
        setSilent(true)
        setOnlyAlertOnce(true)
        setOngoing(true)
        setShowWhen(false)
    }

    private companion object {
        const val CHANNEL_ID = BuildConfig.APPLICATION_ID + ".channel.EARLY_PRESTART"
    }
}
