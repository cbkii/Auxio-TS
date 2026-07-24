/*
 * Copyright (c) 2026 Auxio Project
 * RootStateHolder.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
import org.oxycblt.musikr.fs.RootTreeSnapshot
import org.oxycblt.musikr.fs.RootTreeSnapshotCodec

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
    private val storageOperationLock = Any()
    private var consentGeneration = 0L

    @Volatile var state: State = State.Unknown
        private set

    init {
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) state = State.UnsupportedForVariant
    }

    private val prefs by lazy {
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    }

    private fun userEnabled(): Boolean =
        BuildConfig.TOPWAY_COMPAT_FLAVOR && prefs.getBoolean(KEY_USE_ROOT_FS, false)

    /** Snapshot persisted storage-root consent without invoking `su`. */
    fun isUserEnabled(): Boolean = userEnabled()

    /** Persist an explicit storage-only root decision and reset the bounded probe generation. */
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
        if (!BuildConfig.TOPWAY_COMPAT_FLAVOR) return State.UnsupportedForVariant
        if (!userEnabled()) return State.DisabledByUser
        if (state == State.DisabledByUser) state = State.Unknown
        return state
    }

    /** Fixed read-only TS18 compatibility probes remain separate from storage root authority. */
    fun runTs18ProbeSync(probe: org.oxycblt.auxio.headunit.root.dofun.Ts18RootProbe): String? {
        if (stateSnapshot() == State.Unknown || stateSnapshot() == State.TimedOut) probeSync()
        if (stateSnapshot() != State.Available) return null
        return successfulStdout(
            processRunner.runRootCommand(
                probe.command,
                timeoutMs = TS18_OPERATION_TIMEOUT_MS,
                maxOutputBytes = TS18_OPERATION_OUTPUT_BYTES,
            )
        )
    }

    fun probeSync(): State =
        synchronized(probeLock) {
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

    /**
     * Take one recursive, bounded, read-only snapshot for a configured volume.
     *
     * The command is constructed internally from an allow-listed path. No free-form shell command
     * crosses the Musikr root boundary and no package mutation is available here.
     */
    override fun snapshotTreeSync(
        rootPath: String,
        maxDepth: Int,
        timeoutMs: Long,
    ): RootTreeSnapshot? =
        synchronized(storageOperationLock) {
            if (!RootStorageCommandPolicy.isAllowedStorageRoot(rootPath)) return null
            if (maxDepth !in 1..32 || timeoutMs !in 1L..MAX_STORAGE_TIMEOUT_MS) return null
            if (stateSnapshot() == State.Unknown || stateSnapshot() == State.TimedOut) probeSync()
            if (stateSnapshot() != State.Available) return null
            val command = RootStorageCommandPolicy.buildSnapshotCommand(rootPath, maxDepth)
            when (
                val result =
                    processRunner.runRootCommand(
                        command,
                        timeoutMs = timeoutMs,
                        maxOutputBytes = ROOT_SNAPSHOT_OUTPUT_BYTES,
                    )
            ) {
                is RootProcessResult.Success ->
                    RootTreeSnapshotCodec.parse(
                        rootPath = rootPath.trimEnd('/'),
                        text = result.stdout,
                        maxEntries = MAX_ROOT_SNAPSHOT_ENTRIES,
                    )
                RootProcessResult.TimedOut -> {
                    state = State.TimedOut
                    journal.log(
                        DiagnosticJournal.CAT_STORAGE,
                        "Root volume snapshot timed out",
                        "root=$rootPath timeoutMs=$timeoutMs",
                    )
                    null
                }
                RootProcessResult.OutputLimitExceeded -> {
                    journal.log(
                        DiagnosticJournal.CAT_STORAGE,
                        "Root volume snapshot output limit exceeded",
                        "root=$rootPath maxBytes=$ROOT_SNAPSHOT_OUTPUT_BYTES",
                    )
                    null
                }
                is RootProcessResult.NonZeroExit,
                is RootProcessResult.ExecutionFailure -> null
            }
        }

    /** Read only the fixed Magisk-prepared volume manifest. */
    fun readPreparedVolumeManifestSync(): String? =
        synchronized(storageOperationLock) {
            if (stateSnapshot() == State.Unknown || stateSnapshot() == State.TimedOut) probeSync()
            if (stateSnapshot() != State.Available) return null
            successfulStdout(
                processRunner.runRootCommand(
                    "cat '$PREPARED_VOLUME_MANIFEST'",
                    timeoutMs = MANIFEST_READ_TIMEOUT_MS,
                    maxOutputBytes = MANIFEST_OUTPUT_BYTES,
                )
            )
        }

    private fun successfulStdout(result: RootProcessResult): String? =
        when (result) {
            is RootProcessResult.Success -> result.stdout
            RootProcessResult.TimedOut -> {
                state = State.TimedOut
                null
            }
            is RootProcessResult.NonZeroExit,
            RootProcessResult.OutputLimitExceeded,
            is RootProcessResult.ExecutionFailure -> null
        }

    private companion object {
        const val KEY_USE_ROOT_FS = "auxio_use_root_fs"
        const val ROOT_PROBE_TIMEOUT_MS = 2_000L
        const val ROOT_PROBE_OUTPUT_BYTES = 4 * 1024
        const val TS18_OPERATION_TIMEOUT_MS = 5_000L
        const val TS18_OPERATION_OUTPUT_BYTES = 64 * 1024
        const val ROOT_SNAPSHOT_OUTPUT_BYTES = 16 * 1024 * 1024
        const val MAX_ROOT_SNAPSHOT_ENTRIES = 100_000
        const val MAX_STORAGE_TIMEOUT_MS = 20_000L
        const val MANIFEST_READ_TIMEOUT_MS = 3_000L
        const val MANIFEST_OUTPUT_BYTES = 256 * 1024
        const val PREPARED_VOLUME_MANIFEST = "/data/adb/auxio-ts-root/volumes.tsv"
    }
}

@dagger.hilt.EntryPoint
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
interface RootEntryPoint {
    fun rootGate(): RootStateHolder
}
