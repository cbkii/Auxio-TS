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

    @Synchronized
    fun probeSync(): State {
        // Timeouts are intentionally retryable: TS18 su prompts can be transient, and a
        // process-wide permanent timeout would disable root-assisted DirectFS until restart.
        if (state != State.Unknown && state != State.TimedOut) return state
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
        try {
            val finished = process.waitForCompat(2000)
            if (!finished) {
                process.destroyCompat()
                state = State.TimedOut
            } else {
                val stdout = process.inputStream.bufferedReader().use { it.readText() }
                state =
                    if (process.exitValue() == 0 && stdout.contains("uid=0")) State.Available
                    else State.Denied
            }
        } finally {
            process.inputStream.closeQuietly()
            process.errorStream.closeQuietly()
            process.outputStream.closeQuietly()
        }
        return state
    }

    // Prevent free-form shell execution. Only accept known-safe deterministic commands.
    @Synchronized
    override fun runRootCommandSync(command: String, timeoutMs: Long): List<String>? {
        if (state == State.Unknown || state == State.TimedOut) probeSync()
        if (state != State.Available) return null

        // Validation: command must be one of the known safe operations.
        // DirectFS listing starts with "for p in" and handles listing.
        val isDirectFSList = command.startsWith("for p in ") && command.endsWith("done")

        if (!isDirectFSList) {
            return null
        }

        return try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", command))
            try {
                if (!process.waitForCompat(timeoutMs)) {
                    process.destroyCompat()
                    null
                } else {
                    if (process.exitValue() != 0) null
                    else
                        process.inputStream.bufferedReader().use { reader ->
                            reader.readLines().filter { it.isNotBlank() }
                        }
                }
            } finally {
                process.inputStream.closeQuietly()
                process.errorStream.closeQuietly()
                process.outputStream.closeQuietly()
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun Process.waitForCompat(timeoutMs: Long): Boolean {
        val deadlineNanos = System.nanoTime() + timeoutMs * 1_000_000L
        while (true) {
            try {
                exitValue()
                return true
            } catch (_: IllegalThreadStateException) {}

            if (System.nanoTime() >= deadlineNanos) return false

            try {
                val remainingMs =
                    ((deadlineNanos - System.nanoTime()) / 1_000_000L).coerceAtLeast(1L)
                Thread.sleep(minOf(remainingMs, 25L))
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                return false
            }
        }
    }

    private fun Process.destroyCompat() {
        destroy()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            destroyForcibly()
        }
    }

    private fun java.io.Closeable.closeQuietly() {
        try {
            close()
        } catch (_: Exception) {}
    }
}

@dagger.hilt.EntryPoint
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
interface RootEntryPoint {
    fun rootGate(): RootStateHolder
}
