/*
 * Copyright (c) 2026 Auxio Project
 * EarlyPrestartSettings.kt is part of Auxio.
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
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.R

/** Persisted, Topway-only policy and bounded diagnostics for boot-time early preparation. */
@Singleton
class EarlyPrestartSettings @Inject constructor(@ApplicationContext context: Context) {
    enum class Outcome {
        NEVER,
        REQUESTED,
        READY,
        TIMED_OUT,
        SKIPPED_ROOT_DISABLED,
        START_FAILED,
    }

    private val appContext = context.applicationContext
    private val prefs = PreferenceManager.getDefaultSharedPreferences(appContext)

    var enabled: Boolean
        get() =
            BuildConfig.TOPWAY_COMPAT_FLAVOR && prefs.getBoolean(KEY_EARLY_PRESTART_ENABLED, false)
        set(value) {
            prefs.edit {
                putBoolean(KEY_EARLY_PRESTART_ENABLED, value && BuildConfig.TOPWAY_COMPAT_FLAVOR)
            }
        }

    val outcome: Outcome
        get() =
            runCatching {
                    Outcome.valueOf(
                        prefs.getString(KEY_EARLY_PRESTART_OUTCOME, Outcome.NEVER.name)
                            ?: Outcome.NEVER.name
                    )
                }
                .getOrDefault(Outcome.NEVER)

    val lastRunEpochMs: Long
        get() = prefs.getLong(KEY_EARLY_PRESTART_LAST_RUN, 0L)

    fun mark(outcome: Outcome) {
        prefs.edit {
            putString(KEY_EARLY_PRESTART_OUTCOME, outcome.name)
            putLong(KEY_EARLY_PRESTART_LAST_RUN, System.currentTimeMillis())
        }
    }

    fun summary(): String {
        val state =
            when (outcome) {
                Outcome.NEVER -> appContext.getString(R.string.set_early_prestart_status_never)
                Outcome.REQUESTED ->
                    appContext.getString(R.string.set_early_prestart_status_requested)
                Outcome.READY -> appContext.getString(R.string.set_early_prestart_status_ready)
                Outcome.TIMED_OUT ->
                    appContext.getString(R.string.set_early_prestart_status_timed_out)
                Outcome.SKIPPED_ROOT_DISABLED ->
                    appContext.getString(R.string.set_early_prestart_status_root_disabled)
                Outcome.START_FAILED ->
                    appContext.getString(R.string.set_early_prestart_status_start_failed)
            }
        return if (lastRunEpochMs > 0L) {
            appContext.getString(
                R.string.set_early_prestart_status_with_time,
                state,
                lastRunEpochMs,
            )
        } else {
            state
        }
    }

    companion object {
        const val KEY_EARLY_PRESTART_ENABLED = "auxio_early_prestart"
        private const val KEY_EARLY_PRESTART_OUTCOME = "auxio_early_prestart_outcome"
        private const val KEY_EARLY_PRESTART_LAST_RUN = "auxio_early_prestart_last_run"
    }
}
