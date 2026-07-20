/*
 * Copyright (c) 2026 Auxio Project
 * TopwayCommandServiceContract.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.topway

import android.os.Bundle
import android.view.KeyEvent

/**
 * Verified Binder contract exposed by the exact TS18 `com.tw.service.xt.CommandService` APK.
 *
 * Only the narrow callback-registration and source-query subset required for playback controls is
 * represented here. Auxio never writes TWUtil/MCU commands, changes the active Topway source, or
 * assumes platform signing/UID 1000.
 */
object TopwayCommandServiceContract {
    const val PACKAGE_NAME = "com.tw.service.xt"
    const val SERVICE_CLASS_NAME = "com.tw.service.xt.CommandService"
    const val ACTION_BIND = "com.tw.service.xt.CommandService.Bind"

    const val COMMAND_DESCRIPTOR = "com.tw.service.xt.aidl.ITWCommandAidl"
    const val MUSIC_CALLBACK_DESCRIPTOR = "com.tw.service.xt.aidl.IMusicCallBack"
    const val COMMAND_CALLBACK_DESCRIPTOR = "com.tw.service.xt.aidl.ITWCommandCallbackAidl"

    object CommandTransaction {
        const val REGISTER_COMMAND_CALLBACK = 1
        const val UNREGISTER_COMMAND_CALLBACK = 2
        const val REGISTER_MUSIC_CALLBACK = 5
        const val UNREGISTER_MUSIC_CALLBACK = 6
        const val EXTENDED_INTERFACE = 67
    }

    object MusicCallbackTransaction {
        const val NEXT = 1
        const val PREVIOUS = 2
        const val PLAY = 3
        const val PAUSE = 4
        const val MODE = 5
        const val EXTENDED_INTERFACE = 6
    }

    object CommandCallbackTransaction {
        const val SYSTEM_VOLUME = 1
        const val VOLUME_STATUS = 2
        const val BT_PHONE_STATUS = 3
        const val BT_CALL_STATUS = 4
        const val BT_CONNECTED_STATUS = 5
        const val REVERSE_STATUS = 6
        const val SLEEP_STATUS = 7
        const val EXTENDED_INTERFACE = 8
    }

    const val EXTRA_PROJECT = "project"
    const val EXTRA_ACTION = "action"
    const val PROJECT_SYSTEM = "system"
    const val ACTION_SOURCE_REQUEST = "source_request"
    const val ACTION_SOURCE_RECEIVE = "Source_recieve"
    const val EXTRA_SOURCE_VALUE = "SourceValue"

    fun sourceRequest(): Bundle =
        Bundle().apply {
            putString(EXTRA_PROJECT, PROJECT_SYSTEM)
            putString(EXTRA_ACTION, ACTION_SOURCE_REQUEST)
        }

    fun parseSource(bundle: Bundle?): TopwaySourceState? {
        if (bundle?.getString(EXTRA_ACTION) != ACTION_SOURCE_RECEIVE) return null
        if (!bundle.containsKey(EXTRA_SOURCE_VALUE)) return null
        return TopwaySourceState(bundle.getInt(EXTRA_SOURCE_VALUE))
    }
}

/** Exact source values observed in `CommandService.sendSystemFunction`. */
data class TopwaySourceState(val value: Int) {
    val kind: Kind
        get() =
            when (value) {
                SOURCE_RADIO -> Kind.RADIO
                SOURCE_LOCAL_MUSIC -> Kind.LOCAL_MUSIC
                SOURCE_BLUETOOTH -> Kind.BLUETOOTH
                SOURCE_VIDEO -> Kind.VIDEO
                else -> Kind.OTHER
            }

    enum class Kind {
        RADIO,
        LOCAL_MUSIC,
        BLUETOOTH,
        VIDEO,
        OTHER,
    }

    companion object {
        const val SOURCE_RADIO = 1
        const val SOURCE_LOCAL_MUSIC = 3
        const val SOURCE_BLUETOOTH = 8
        const val SOURCE_VIDEO = 9
    }
}

/** Maps the exact music-callback transactions to Android-standard MediaSession key events. */
internal enum class TopwayMusicControl(val transactionCode: Int, val mediaKeyCode: Int) {
    NEXT(TopwayCommandServiceContract.MusicCallbackTransaction.NEXT, KeyEvent.KEYCODE_MEDIA_NEXT),
    PREVIOUS(
        TopwayCommandServiceContract.MusicCallbackTransaction.PREVIOUS,
        KeyEvent.KEYCODE_MEDIA_PREVIOUS,
    ),
    PLAY(TopwayCommandServiceContract.MusicCallbackTransaction.PLAY, KeyEvent.KEYCODE_MEDIA_PLAY),
    PAUSE(
        TopwayCommandServiceContract.MusicCallbackTransaction.PAUSE,
        KeyEvent.KEYCODE_MEDIA_PAUSE,
    );

    companion object {
        fun fromTransaction(code: Int): TopwayMusicControl? = entries.firstOrNull {
            it.transactionCode == code
        }
    }
}
