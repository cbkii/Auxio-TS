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
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.R
import org.oxycblt.auxio.diagnostics.DiagnosticJournal
import org.oxycblt.auxio.music.RootAccessPolicy
import org.oxycblt.musikr.fs.RootGate

@Singleton
class RootStateHolder
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val processRunner: RootProcessRunner,
    private val journal: DiagnosticJournal,
) : RootGate {
    enum class State {
        Unknown,
        Available,
        Unavailable,
        Denied,
        TimedOut,
        UnsupportedForVariant,
        DisabledByUser,
    }

    private val stateLock = Any()
    private val probeLock = Any()
    private var consentGeneration = 0L

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

    /** Snapshot the persisted user consent without invoking `su`. */
    fun isUserEnabled(): Boolean = userEnabled()

    /**
     * Persist an explicit user decision for root-assisted DirectFS.
     *
     * Enabling resets any prior denied/unavailable result so the next bounded probe may present the
     * Magisk consent UI again. Disabling immediately closes the root gate and leaves ordinary
     * `/storage/...` access untouched.
     */
    fun setUserEnabled(enabled: Boolean) {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) {
            state = State.UnsupportedForVariant
            return
        }
        prefs.edit {
            putBoolean(KEY_USE_ROOT_FS, enabled)
            putString(
                context.getString(R.string.set_key_root_access_policy),
                if (enabled) RootAccessPolicy.ON_DEMAND.name else RootAccessPolicy.OFF.name,
            )
        }
        synchronized(stateLock) {
            consentGeneration += 1L
            state = if (enabled) State.Unknown else State.DisabledByUser
        }
    }

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

    fun runTs18ProbeSync(probe: org.oxycblt.auxio.headunit.root.dofun.Ts18RootProbe): String? {
        if (stateSnapshot() != State.Available) return null
        return successfulStdout(
            processRunner.runRootCommand(
                probe.command,
                timeoutMs = TS18_OPERATION_TIMEOUT_MS,
                maxOutputBytes = TS18_OPERATION_OUTPUT_BYTES,
            )
        )
    }

    fun runTs18MutationSync(
        mutation: org.oxycblt.auxio.headunit.root.dofun.Ts18RootMutation
    ): String? {
        if (stateSnapshot() != State.Available) return null
        return successfulStdout(
            processRunner.runRootCommand(
                mutation.command,
                timeoutMs = TS18_OPERATION_TIMEOUT_MS,
                maxOutputBytes = TS18_OPERATION_OUTPUT_BYTES,
            )
        )
    }

    fun probeSync(): State = synchronized(probeLock) {
        val generation =
            synchronized(stateLock) {
                if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) {
                    state = State.UnsupportedForVariant
                    return state
                }
                if (!userEnabled()) {
                    state = State.DisabledByUser
                    return state
                }
                if (state == State.DisabledByUser) state = State.Unknown
                // A timeout is retryable. Other resolved states remain cached for this consent
                // generation.
                if (state != State.Unknown && state != State.TimedOut) return state
                consentGeneration
            }

        val probed =
            when (
                val result =
                    processRunner.runRootCommand(
                        "id",
                        timeoutMs = ROOT_PROBE_TIMEOUT_MS,
                        maxOutputBytes = ROOT_PROBE_OUTPUT_BYTES,
                    )
            ) {
                is RootProcessResult.Success ->
                    if (result.stdout.contains("uid=0")) State.Available else State.Denied
                is RootProcessResult.NonZeroExit -> State.Denied
                RootProcessResult.TimedOut -> State.TimedOut
                RootProcessResult.OutputLimitExceeded -> State.Denied
                is RootProcessResult.ExecutionFailure -> State.Unavailable
            }

        synchronized(stateLock) {
  if (generation == consentGeneration) {
      state = if (userEnabled()) probed else State.DisabledByUser
  }
  state
        }
    }

    // Prevent free-form shell execution. Only accept known-safe deterministic commands.
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
        if (!isAllowedRootListCommand(command)) return null

        return when (
            val result =
                processRunner.runRootCommand(
                    command,
                    timeoutMs = timeoutMs,
                    maxOutputBytes = ROOT_LIST_OUTPUT_BYTES,
                )
        ) {
            is RootProcessResult.Success -> {
                val lines =
                    result.stdout
                        .lineSequence()
                        .filter(String::isNotBlank)
                        .take(MAX_ROOT_LIST_LINES + 1)
                        .toList()
                if (lines.size > MAX_ROOT_LIST_LINES) {
                    journal.log(
                        DiagnosticJournal.CAT_STORAGE,
                        "Root listing line limit exceeded",
                        "maxLines=$MAX_ROOT_LIST_LINES",
                    )
                    null
                } else {
                    lines
                }
            }
            RootProcessResult.TimedOut -> {
                state = State.TimedOut
                null
            }
            RootProcessResult.OutputLimitExceeded -> {
                journal.log(
                    DiagnosticJournal.CAT_STORAGE,
                    "Root listing output limit exceeded",
                    "maxBytes=$ROOT_LIST_OUTPUT_BYTES",
                )
                null
            }
            is RootProcessResult.NonZeroExit,
            is RootProcessResult.ExecutionFailure -> null
        }
    }

    private fun successfulStdout(result: RootProcessResult): String? =
        when (result) {
            is RootProcessResult.Success -> result.stdout.take(TS18_OPERATION_OUTPUT_BYTES)
            RootProcessResult.TimedOut -> {
                state = State.TimedOut
                null
            }
            is RootProcessResult.NonZeroExit,
            RootProcessResult.OutputLimitExceeded,
            is RootProcessResult.ExecutionFailure -> null
        }

    private fun isAllowedRootListCommand(command: String): Boolean {
        // Extract the path and reconstruct the only shell command RootGate accepts. Paths with
        // shell metacharacters are rejected before reconstruction.
        val prefix = "for p in '"
        if (!command.startsWith(prefix)) return false
        val pathEndIndex = command.indexOf("'", prefix.length)
        if (pathEndIndex == -1) return false
        val extractedPath = command.substring(prefix.length, pathEndIndex)
        if (
            extractedPath.contains('\n') ||
                extractedPath.contains(';') ||
                extractedPath.contains('`') ||
                extractedPath.contains('$')
        ) {
            return false
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
        return command == expectedCommand
    }

    private companion object {
        const val KEY_USE_ROOT_FS = "auxio_use_root_fs"
        const val ROOT_PROBE_TIMEOUT_MS = 2_000L
        const val ROOT_PROBE_OUTPUT_BYTES = 4 * 1024
        const val TS18_OPERATION_TIMEOUT_MS = 5_000L
        const val TS18_OPERATION_OUTPUT_BYTES = 5_000
        const val ROOT_LIST_OUTPUT_BYTES = 16 * 1024 * 1024
        const val MAX_ROOT_LIST_LINES = 50_000
    }
}

@dagger.hilt.EntryPoint
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
interface RootEntryPoint {
    fun rootGate(): RootStateHolder
}
