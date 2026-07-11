/*
 * Copyright (c) 2026 Auxio Project
 * OverlayLifecycleJournal.kt is part of Auxio.
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

package org.oxycblt.auxio.car.overlay

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber as L

object OverlayLifecycleJournal {

    private const val PREFS_NAME = "OverlayLifecycleJournal"
    private const val KEY_EVENTS = "events"
    private const val MAX_EVENTS = 64

    private lateinit var prefs: SharedPreferences
    private var eventsCache: JSONArray? = null

    fun init(context: Context) {
        if (!this::prefs.isInitialized) {
            prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    @Synchronized
    fun log(
        trigger: String,
        enabled: Boolean,
        permissionGranted: Boolean,
        suppressed: Boolean,
        serviceCreated: Boolean,
        result: String,
    ) {
        if (!this::prefs.isInitialized) return

        try {
            val events = getEventsArray()

            val event = JSONObject()
            event.put("timestamp", System.currentTimeMillis())
            event.put("trigger", trigger)
            event.put("enabled", enabled)
            event.put("permissionGranted", permissionGranted)
            event.put("suppressed", suppressed)
            event.put("serviceCreated", serviceCreated)
            event.put("result", result)

            events.put(event)

            while (events.length() > MAX_EVENTS) {
                events.remove(0)
            }

            saveEventsArray(events)
        } catch (e: Exception) {
            L.w(e, "Unable to persist overlay lifecycle history; resetting it")
            prefs.edit().remove(KEY_EVENTS).apply()
            eventsCache = null
        }
    }

    private fun getEventsArray(): JSONArray {
        if (eventsCache != null) {
            return eventsCache!!
        }
        val jsonString = prefs.getString(KEY_EVENTS, "[]")
        eventsCache =
            try {
                JSONArray(jsonString)
            } catch (e: Exception) {
                L.w(e, "Unable to parse overlay lifecycle history")
                JSONArray()
            }
        return eventsCache!!
    }

    private fun saveEventsArray(events: JSONArray) {
        prefs.edit().putString(KEY_EVENTS, events.toString()).apply()
        eventsCache = events
    }

    @Synchronized
    fun getHistory(): String {
        if (!this::prefs.isInitialized) return "[]"
        return getEventsArray().toString(2)
    }

    @Synchronized
    fun clear() {
        if (!this::prefs.isInitialized) return
        prefs.edit().remove(KEY_EVENTS).apply()
        eventsCache = null
    }
}
