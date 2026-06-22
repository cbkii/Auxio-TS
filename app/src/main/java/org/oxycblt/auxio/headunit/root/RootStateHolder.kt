/*
 * Copyright (c) 2026 Auxio Project
 * RootStateHolder.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.root

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.musikr.fs.RootGate

@Singleton
class RootStateHolder @Inject constructor() : RootGate {
    enum class State {
        Unknown,
        Available,
        Unavailable,
        Denied,
        TimedOut,
        UnsupportedForVariant,
    }

    @Volatile
    var state: State = State.Unknown
        private set

    init {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) state = State.UnsupportedForVariant
    }

    fun probeSync(): State {
        if (state != State.Unknown) return state
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            state = State.UnsupportedForVariant
            return state
        }
        val process =
            try {
                Runtime.getRuntime().exec(arrayOf("su", "-c", "id"))
            } catch (e: Exception) {
                state = State.Unavailable
                return state
            }
        val finished = process.waitFor(2000, TimeUnit.MILLISECONDS)
        if (!finished) {
            process.destroy()
            state = State.TimedOut
        } else {
            val stdout = BufferedReader(InputStreamReader(process.inputStream)).readText()
            state =
                if (process.exitValue() == 0 && stdout.contains("uid=0")) State.Available
                else State.Denied
        }
        return state
    }

    override fun runRootCommandSync(command: String, timeoutMs: Long): List<String>? {
        if (state == State.Unknown || state == State.TimedOut) probeSync()
        if (state != State.Available) return null
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", command))
            if (!process.waitFor(timeoutMs, TimeUnit.MILLISECONDS)) {
                process.destroy()
                null
            } else {
                if (process.exitValue() != 0) null
                else
                    BufferedReader(InputStreamReader(process.inputStream)).readLines().filter {
                        it.isNotBlank()
                    }
            }
        } catch (e: Exception) {
            null
        }
    }
}

@dagger.hilt.EntryPoint
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
interface RootEntryPoint {
    fun rootGate(): RootStateHolder
}
