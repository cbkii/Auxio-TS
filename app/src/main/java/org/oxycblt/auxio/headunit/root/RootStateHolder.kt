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
import timber.log.Timber as L

/** Global gate for root/superuser state on TS18 variants. */
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
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            state = State.UnsupportedForVariant
        }
    }

    /**
     * Performs a one-time root probe if the state is currently Unknown. This should be called off
     * the main thread.
     */
    @Synchronized
    fun probeSync(): State {
        if (state != State.Unknown) return state

        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            state = State.UnsupportedForVariant
            return state
        }

        L.d("Probing root capability...")
        val result = runRootCommandInternalSync("id", timeoutMs = ROOT_PROBE_TIMEOUT_MS)

        state =
            when {
                result == null -> State.TimedOut
                result.exitCode == 0 && result.stdout.contains("uid=0") -> State.Available
                result.exitCode == 1 ||
                    result.stderr.contains("denied", ignoreCase = true) ||
                    result.stderr.contains("permission", ignoreCase = true) -> State.Denied
                else -> State.Unavailable
            }

        L.i("Root probe completed: $state")
        return state
    }

    /** Resets the root gate, allowing a new probe to be performed. */
    fun reset() {
        if (BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            state = State.Unknown
        }
    }

    /** Helper to run a command via su with a timeout. */
    fun runRootCommandRawSync(
        command: String,
        timeoutMs: Long = ROOT_COMMAND_TIMEOUT_MS,
    ): CommandResult? {
        if (
            state == State.UnsupportedForVariant ||
                state == State.Denied ||
                state == State.Unavailable ||
                state == State.TimedOut
        ) {
            return null
        }

        if (state == State.Unknown) {
            probeSync()
        }

        if (state != State.Available) {
            return null
        }
        return runRootCommandInternalSync(command, timeoutMs)
    }

    private fun runRootCommandInternalSync(command: String, timeoutMs: Long): CommandResult? =
        try {
            val process = Runtime.getRuntime().exec(arrayOf(SU_BINARY, "-c", command))
            val finished = process.waitFor(timeoutMs, TimeUnit.MILLISECONDS)

            if (!finished) {
                L.w("Root command timed out after ${timeoutMs}ms")
                process.destroyForcibly()
                return null
            }

            val exitCode = process.exitValue()
            val stdout = BufferedReader(InputStreamReader(process.inputStream)).readText()
            val stderr = BufferedReader(InputStreamReader(process.errorStream)).readText()

            CommandResult(exitCode, stdout, stderr)
        } catch (e: Exception) {
            L.e(e, "Failed to run root command")
            null
        }

    /** Implementation of [RootGate.runRootCommandSync] for use within the musikr library. */
    override fun runRootCommandSync(command: String, timeoutMs: Long): List<String>? {
        val result = runRootCommandRawSync(command, timeoutMs)
        if (result == null || result.exitCode != 0) return null
        return result.stdout.lines().filter { it.isNotBlank() }
    }

    data class CommandResult(val exitCode: Int, val stdout: String, val stderr: String)

    companion object {
        const val ROOT_PROBE_TIMEOUT_MS = 2000L
        const val ROOT_COMMAND_TIMEOUT_MS = 5000L
        private const val SU_BINARY = "su"

        fun shellQuote(value: String): String = buildString {
            append('\'')
            append(value.replace("'", "'\"'\"'"))
            append('\'')
        }
    }
}
