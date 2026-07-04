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

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.musikr.fs.RootGate

@Singleton
class RootStateHolder @Inject constructor(@ApplicationContext private val context: Context) :
    RootGate {
    enum class State {
        Unknown,
        Available,
        Unavailable,
        Denied,
        TimedOut,
        UnsupportedForVariant,
        DisabledByUser,
    }

    @Volatile
    var state: State = State.Unknown
        private set

    init {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) state = State.UnsupportedForVariant
    }

    private val prefs by lazy {
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    }

    private fun userEnabled(): Boolean =
        BuildConfig.TOPWAY_COMPAT_FLAVOR && prefs.getBoolean(KEY_USE_ROOT_FS, false)

    @Synchronized
    fun stateSnapshot(): State {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            return State.UnsupportedForVariant
        }
        if (!userEnabled()) {
            return State.DisabledByUser
        }
        if (state == State.DisabledByUser) {
            state = State.Unknown
        }
        return state
    }

    @Synchronized
    fun probeSync(): State {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            state = State.UnsupportedForVariant
            return state
        }

        if (!userEnabled()) {
            state = State.DisabledByUser
            return state
        }
        if (state == State.DisabledByUser) {
            state = State.Unknown
        }

        // Timeouts are intentionally retryable: TS18 su prompts can be transient, and a
        // process-wide permanent timeout would disable root-assisted DirectFS until restart.
        if (state != State.Unknown && state != State.TimedOut) return state
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
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            state = State.UnsupportedForVariant
            return null
        }
        if (!userEnabled()) {
            state = State.DisabledByUser
            return null
        }
        if (state == State.DisabledByUser) {
            state = State.Unknown
        }

        if (state == State.Unknown || state == State.TimedOut) probeSync()
        if (state != State.Available) return null

        // Validation: extract the path from the command and ensure the command matches exactly what
        // we'd build for that path.
        val prefix = "for p in '"
        if (!command.startsWith(prefix)) return null

        val pathEndIndex = command.indexOf("'", prefix.length)
        if (pathEndIndex == -1) return null

        val extractedPath = command.substring(prefix.length, pathEndIndex)

        // Disallow path injection characters
        if (
            extractedPath.contains("\n") ||
                extractedPath.contains(";") ||
                extractedPath.contains("`") ||
                extractedPath.contains("\$")
        ) {
            return null
        }

        val expectedCommand =
            "for p in '${extractedPath}'/* '${extractedPath}'/.*; do " +
                "[ -e \"\$p\" ] || continue; " +
                "b=\${p##*/}; [ \"\$b\" = . ] && continue; [ \"\$b\" = .. ] && continue; " +
                "t=f; [ -d \"\$p\" ] && t=d; [ -L \"\$p\" ] && t=l; " +
                "m=\$(stat -c %Y \"\$p\" 2>/dev/null || echo 0); " +
                "s=\$(stat -c %s \"\$p\" 2>/dev/null || echo 0); " +
                "printf '%s\t%s\t%s\t%s\t%s\n' \"\$t\" \"\$t\" \"\$m\" \"\$s\" \"\$b\"; " +
                "done"

        if (command != expectedCommand) {
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

    private companion object {
        const val KEY_USE_ROOT_FS = "auxio_use_root_fs"
    }
}

@dagger.hilt.EntryPoint
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
interface RootEntryPoint {
    fun rootGate(): RootStateHolder
}
