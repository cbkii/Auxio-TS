/*
 * Copyright (c) 2024 Auxio Project
 * TopwayBridgeExtrasPolicy.kt is part of Auxio.
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

import android.content.Intent
import android.os.BadParcelableException
import timber.log.Timber as L

data class TopwayBridgeExtras(val cmd: String?, val widgetProgress: Int?)

/** Allowlist-only bridge payload sanitizer for exported Topway receiver intents. */
object TopwayBridgeExtrasPolicy {
    private val ALLOWED_COMMANDS: Set<String> =
        setOf(
            TopwayMusicContract.CMD_PREV,
            TopwayMusicContract.CMD_NEXT,
            TopwayMusicContract.CMD_PLAY_PAUSE,
            TopwayMusicContract.CMD_UPDATE,
        )

    fun sanitizeIncomingExtras(incoming: Map<String, Any?>): TopwayBridgeExtras {
        val cmd =
            (incoming[TopwayMusicContract.EXTRA_CMD] as? String)?.takeIf {
                it.length <= 16 && it in ALLOWED_COMMANDS
            }
        val widgetProgress =
            parseWidgetProgress(incoming[TopwayMusicContract.EXTRA_WIDGET_PROGRESS])
        return TopwayBridgeExtras(cmd = cmd, widgetProgress = widgetProgress)
    }

    /**
     * Safely extracts the tiny Topway extras allowlist from an untrusted [Intent].
     *
     * Exported receivers and services can be addressed by launcher/widget processes or other apps.
     * Reading a malformed Bundle can throw before any routing decision is made, so isolate Bundle
     * deserialization here and let callers continue with an empty extras map.
     */
    fun safelyExtractIncomingExtras(
        intent: Intent?,
        classLoader: ClassLoader?,
        source: String = "Topway bridge",
    ): Map<String, Any?> {
        val action = intent?.action
        return try {
            val extras = intent?.extras ?: return emptyMap()
            extras.classLoader = classLoader
            extractAllowlistedIncomingExtras(
                containsKey = extras::containsKey,
                getValue = { key ->
                    @Suppress("DEPRECATION") // Bundle.get is the only untyped accessor; callers
                    // perform their own type checks in sanitizeIncomingExtras.
                    extras.get(key)
                },
            )
        } catch (e: BadParcelableException) {
            L.w(
                e,
                "Ignoring malformed extras from untrusted Topway intent: source=$source action=$action",
            )
            emptyMap()
        } catch (e: RuntimeException) {
            L.w(
                e,
                "Ignoring unreadable extras from untrusted Topway intent: source=$source action=$action",
            )
            emptyMap()
        }
    }

    internal fun extractAllowlistedIncomingExtras(
        containsKey: (String) -> Boolean,
        getValue: (String) -> Any?,
    ): Map<String, Any?> =
        buildMap(2) {
            if (containsKey(TopwayMusicContract.EXTRA_CMD)) {
                put(TopwayMusicContract.EXTRA_CMD, getValue(TopwayMusicContract.EXTRA_CMD))
            }
            if (containsKey(TopwayMusicContract.EXTRA_WIDGET_PROGRESS)) {
                put(
                    TopwayMusicContract.EXTRA_WIDGET_PROGRESS,
                    getValue(TopwayMusicContract.EXTRA_WIDGET_PROGRESS),
                )
            }
        }

    private fun parseWidgetProgress(raw: Any?): Int? =
        when (raw) {
            is Int -> raw
            is Long -> raw.takeIf { it in Int.MIN_VALUE.toLong()..Int.MAX_VALUE.toLong() }?.toInt()
            is String -> raw.takeIf { it.length <= 10 }?.toIntOrNull()
            else -> null
        }
}
