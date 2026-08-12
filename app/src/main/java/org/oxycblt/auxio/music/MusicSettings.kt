/*
 * Copyright (c) 2023 Auxio Project
 * MusicSettings.kt is part of Auxio.
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

package org.oxycblt.auxio.music

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.UUID
import javax.inject.Inject
import org.oxycblt.auxio.IntegerTable
import org.oxycblt.auxio.R
import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.auxio.music.locations.MusicSourceCanonicalizer
import org.oxycblt.auxio.music.locations.MusicSourcePathNormalizer
import org.oxycblt.auxio.settings.Settings
import org.oxycblt.auxio.util.PerfTimer
import org.oxycblt.auxio.util.unlikelyToBeNull
import org.oxycblt.musikr.fs.CanonicalSourcePolicy
import org.oxycblt.musikr.fs.Location
import org.oxycblt.musikr.fs.SourceIdentity
import org.oxycblt.musikr.fs.mediastore.MediaStore
import org.oxycblt.musikr.fs.saf.SAF
import timber.log.Timber as L

/**
 * User configuration specific to music system.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
interface MusicSettings : Settings<MusicSettings.Listener> {
    /** The current library revision. */
    var revision: UUID?

    /** Persisted knowledge about whether a usable library has existed before. */
    var libraryState: LibraryState

    /** Whether the last scan attempt failed. Used to avoid startup scan storms. */
    var lastScanFailed: Boolean

    /** The mode for loading music locations (SAF or System database). */
    var locationMode: LocationMode

    /** The currently configured SAF query (if any) * */
    var safQuery: SAF.Query

    /** Raw persisted SAF/DirectFS source count, without requiring paths to open successfully. */
    val configuredSourceCount: Int

    /** Raw configured roots retained even when they cannot currently be opened. */
    val configuredSourceSpecs: List<ConfiguredSourceSpec>
        get() = emptyList()

    /** The currently configured MediaStore query (if any) * */
    var mediaStoreQuery: MediaStore.Query

    /** Monotonic generation for an atomically persisted source configuration. */
    val sourceConfigurationGeneration: Long
        get() = 0L

    /** Optional post-load generated playlists. Disabled by default. */
    val generatedPlaylistsEnabled: Boolean
        get() = false

    /** Persist all source fields together and queue one cache-bypassing initial scan. */
    fun applySourceConfiguration(
        mode: LocationMode,
        safQuery: SAF.Query,
        mediaStoreQuery: MediaStore.Query,
    ): Boolean {
        val changed =
            locationMode != mode ||
                this.safQuery != safQuery ||
                this.mediaStoreQuery != mediaStoreQuery
        locationMode = mode
        this.safQuery = safQuery
        this.mediaStoreQuery = mediaStoreQuery
        return changed
    }

    val sourceConfigurationCheckpoint: SourceConfigurationCheckpoint?
        get() = null

    /** Terminalise a persisted owner that cannot survive this process/service lifecycle. */
    fun recoverInterruptedSourceConfiguration(
        owner: SourceScanAttemptOwner,
        nowMs: Long,
    ): SourceConfigurationCheckpoint? = sourceConfigurationCheckpoint

    /** Atomically allocate one attempt for an eligible generation. */
    fun claimPendingConfiguration(
        expectedGeneration: Long,
        owner: SourceScanAttemptOwner,
        attemptId: String,
        nowMs: Long,
        reason: SourceScanClaimReason,
    ): SourceConfigurationCheckpoint? = null

    /** Explicitly transfer one live lease inside the same process. */
    fun handoffSourceConfigurationAttempt(
        generation: Long,
        attemptId: String,
        fromOwner: SourceScanAttemptOwner,
        toOwner: SourceScanAttemptOwner,
        nowMs: Long,
    ): Boolean = false

    /** Verify generation, attempt and lifecycle ownership before repository work or publication. */
    fun ownsSourceConfigurationAttempt(
        generation: Long,
        attemptId: String,
        owner: SourceScanAttemptOwner,
    ): Boolean = false

    /** Persist bounded meaningful progress only for the current owner. */
    fun heartbeatSourceConfigurationAttempt(
        generation: Long,
        attemptId: String,
        owner: SourceScanAttemptOwner,
        nowMs: Long,
        progress: SourceScanAttemptProgress,
    ): Boolean = false

    /** Assign exactly one terminal result, optionally publishing compatible library state. */
    fun completeSourceConfigurationAttempt(
        generation: Long,
        attemptId: String,
        owner: SourceScanAttemptOwner,
        nowMs: Long,
        completion: SourceScanAttemptCompletion,
        publishAfterCommit: () -> Unit = {},
    ): Boolean = false

    /** Retain a committed generation while recording currently unavailable configured roots. */
    fun markSourcesUnresolved(sourceKeys: Set<String>, outcome: String) = Unit

    /** Resource priority used for the next immutable Musikr scan pipeline. */
    val scanPriority: ScanPriority

    /** Policy controlling automatic source observation and rescans. */
    val observationMode: ObservationMode

    /** Whether the current observation policy requires a source watcher. */
    val shouldBeObserving: Boolean
        get() = observationMode != ObservationMode.MANUAL

    /** Root-assisted DirectFS access policy. Ordinary app access is always tried first. */
    val rootAccessPolicy: RootAccessPolicy

    /** Whether non-essential launcher dynamic shortcuts may be published. */
    val dynamicShortcutsEnabled: Boolean

    /** Whether bounded detailed performance capture is enabled. */
    val performanceCaptureEnabled: Boolean

    /** A [String] of characters representing the desired characters to denote multi-value tags. */
    var separators: String

    /** Whether to enable more advanced sorting by articles and numbers. */
    val intelligentSorting: Boolean

    /** Whether to use the file-system cache for improved loading times. */
    val useFileTreeCache: Boolean

    /**
     * Whether to apply the TS18 system source path filter (only include paths containing
     * music/download/media keywords) when using MediaStore mode. Default true for TS18 builds.
     */
    var ts18SystemSourceFilter: Boolean

    fun forceLocationUpdate()

    interface Listener {
        /** Called when the current music locations changed. */
        fun onMusicLocationsChanged() {}

        /** Called when a setting controlling how music is loaded has changed. */
        fun onIndexingSettingChanged() {}

        /** Called when the [shouldBeObserving] configuration has changed. */
        fun onObservingChanged() {}

        /** Rebuild optional generated playlists without rescanning sources. */
        fun onGeneratedPlaylistsChanged() {}
    }
}

class MusicSettingsImpl @Inject constructor(@ApplicationContext private val context: Context) :
    Settings.Impl<MusicSettings.Listener>(context), MusicSettings {

    init {
        // Restore the explicit release-build capture policy after process recreation. Debug and
        // benchmark builds remain enabled by their build type and still use the same bounded ring.
        PerfTimer.configure(performanceCaptureEnabled)
    }

    override var revision: UUID?
        get() =
            sharedPreferences
                .getString(getString(R.string.set_key_library_revision), null)
                ?.takeUnless { it == "null" }
                ?.let { runCatching { UUID.fromString(it) }.getOrNull() }
        set(value) {
            sharedPreferences.edit {
                if (value == null) {
                    remove(getString(R.string.set_key_library_revision))
                } else {
                    putString(getString(R.string.set_key_library_revision), value.toString())
                }
                apply()
            }
        }

    override var libraryState: LibraryState
        get() =
            LibraryState.fromName(
                sharedPreferences.getString(getString(R.string.set_key_library_state), null)
            ) ?: if (revision != null) LibraryState.USABLE else LibraryState.NEVER
        set(value) {
            sharedPreferences.edit {
                putString(getString(R.string.set_key_library_state), value.name)
            }
        }

    override var lastScanFailed: Boolean
        get() =
            sharedPreferences.getBoolean(
                getString(R.string.set_key_library_last_scan_failed),
                false,
            )
        set(value) {
            sharedPreferences.edit {
                putBoolean(getString(R.string.set_key_library_last_scan_failed), value)
            }
        }

    override val scanPriority: ScanPriority
        get() =
            ScanPriority.fromName(
                sharedPreferences.getString(getString(R.string.set_key_scan_priority), null)
            )
                ?: if (org.oxycblt.auxio.BuildConfig.TOPWAY_COMPAT_ENABLED) {
                    ScanPriority.PLAYBACK_FIRST
                } else {
                    ScanPriority.BALANCED
                }

    override val observationMode: ObservationMode
        get() {
            val stored =
                ObservationMode.fromName(
                    sharedPreferences.getString(getString(R.string.set_key_observation_mode), null)
                )
            if (stored != null) return stored
            return if (sharedPreferences.getBoolean(getString(R.string.set_key_observing), false)) {
                ObservationMode.CONTINUOUS
            } else {
                ObservationMode.MANUAL
            }
        }

    override val rootAccessPolicy: RootAccessPolicy
        get() =
            RootAccessPolicy.fromName(
                sharedPreferences.getString(getString(R.string.set_key_root_access_policy), null)
            ) ?: RootAccessPolicy.ON_DEMAND

    override val dynamicShortcutsEnabled: Boolean
        get() =
            sharedPreferences.getBoolean(
                getString(R.string.set_key_dynamic_shortcuts),
                !org.oxycblt.auxio.BuildConfig.TOPWAY_COMPAT_ENABLED,
            )

    override val performanceCaptureEnabled: Boolean
        get() = sharedPreferences.getBoolean(getString(R.string.set_key_performance_capture), false)

    override val sourceConfigurationGeneration: Long
        get() = sharedPreferences.getLong(KEY_SOURCE_CONFIGURATION_GENERATION, 0L)

    override val configuredSourceSpecs: List<ConfiguredSourceSpec>
        get() {
            if (locationMode == LocationMode.MEDIA_STORE) return emptyList()
            val fileOnly = locationMode == LocationMode.DIRECT_FS
            repairPersistedSourceDuplicates(fileOnly)
            // Descriptors come from the canonical persisted list rather than safQuery.source.
            // Opening a revoked SAF grant legitimately fails, but the configured identity must
            // survive so planning can report PERMISSION_REQUIRED instead of "no sources".
            val locations =
                unlikelyToBeNull(
                        sharedPreferences.getString(getString(R.string.set_key_music_locations), "")
                    )
                    .toUnopenedLocations(fileOnly)
            val origins = resolvedOrigins(locations)
            val grants = context.contentResolver.persistedUriPermissions
            return locations.map { location ->
                val uri = location.uri
                val canonicalKey = MusicSourceCanonicalizer.canonicalKeyOf(location)
                val appFacingPath = MusicSourceCanonicalizer.appFacingPathOf(location)
                val access =
                    if (uri.scheme == "file") {
                        val file = uri.path?.let(::File)
                        if (file?.canRead() == true && file.isDirectory) {
                            ConfiguredSourceSpec.AccessState.AVAILABLE
                        } else {
                            ConfiguredSourceSpec.AccessState.TEMPORARILY_UNAVAILABLE
                        }
                    } else if (
                        grants.any {
                            it.isReadPermission &&
                                MusicSourceCanonicalizer.canonicalKeyOfUri(it.uri) == canonicalKey
                        }
                    ) {
                        ConfiguredSourceSpec.AccessState.AVAILABLE
                    } else {
                        ConfiguredSourceSpec.AccessState.PERMISSION_REQUIRED
                    }
                ConfiguredSourceSpec(
                    normalizedUri = uri,
                    sourceKey = SourceIdentity.forConfiguredRoot(locationMode.name, location),
                    canonicalKey = canonicalKey,
                    mode = locationMode,
                    displayPath =
                        appFacingPath
                            ?: uri.path?.takeIf { it.isNotBlank() }
                            ?: location.path.components.unixString,
                    accessState = access,
                    origin =
                        origins[canonicalKey]
                            ?: CanonicalSourcePolicy.legacyOriginForPath(appFacingPath),
                    traversalScope = appFacingPath?.let(CanonicalSourcePolicy::scopeOf),
                )
            }
        }

    override val sourceConfigurationCheckpoint: SourceConfigurationCheckpoint?
        get() = synchronized(SOURCE_CHECKPOINT_LOCK) { readSourceConfigurationCheckpointLocked() }

    override val generatedPlaylistsEnabled: Boolean
        get() = sharedPreferences.getBoolean(getString(R.string.set_key_generated_playlists), false)

    override var separators: String
        // Differ from convention and store a string of separator characters instead of an int
        // code. This makes it easier to use and more extendable.
        get() = sharedPreferences.getString(getString(R.string.set_key_separators), "") ?: ""
        set(value) {
            sharedPreferences.edit {
                putString(getString(R.string.set_key_separators), value)
                apply()
            }
        }

    override val intelligentSorting: Boolean
        get() = sharedPreferences.getBoolean(getString(R.string.set_key_auto_sort_names), true)

    override val useFileTreeCache: Boolean
        get() = sharedPreferences.getBoolean(getString(R.string.set_key_fs_cache), false)

    override var ts18SystemSourceFilter: Boolean
        get() {
            // Default to true only on detected TS18/Topway head units to avoid
            // silently filtering custom directories on standard phones/tablets.
            val isTs18 =
                android.os.Build.DEVICE.orEmpty().lowercase().contains("s9863a1h10") ||
                    android.os.Build.BOARD.orEmpty().lowercase().contains("s9863a1h10")
            return sharedPreferences.getBoolean(KEY_TS18_SYSTEM_SOURCE_FILTER, isTs18)
        }
        set(value) {
            sharedPreferences.edit { putBoolean(KEY_TS18_SYSTEM_SOURCE_FILTER, value) }
        }

    override var locationMode: LocationMode
        get() {
            val fallback =
                LocationMode.defaultForFlavor(org.oxycblt.auxio.BuildConfig.TOPWAY_COMPAT_ENABLED)
            val mode =
                sharedPreferences.getInt(
                    getString(R.string.set_key_locations_mode),
                    fallback.intCode,
                )
            return LocationMode.fromInt(mode) ?: fallback
        }
        set(value) {
            sharedPreferences.edit {
                putInt(getString(R.string.set_key_locations_mode), value.intCode)
                apply()
            }
        }

    override var safQuery: SAF.Query
        get() {
            val fileOnly = locationMode == LocationMode.DIRECT_FS
            repairPersistedSourceDuplicates(fileOnly = fileOnly)
            val locations =
                unlikelyToBeNull(
                        sharedPreferences.getString(getString(R.string.set_key_music_locations), "")
                    )
                    .toOpenedLocations(fileOnly = fileOnly)
            val excludedLocations =
                unlikelyToBeNull(
                        sharedPreferences.getString(
                            getString(R.string.set_key_excluded_locations),
                            "",
                        )
                    )
                    .toUnopenedLocations(fileOnly = fileOnly)
            val withHidden =
                sharedPreferences.getBoolean(getString(R.string.set_key_with_hidden), false)
            val multithread =
                sharedPreferences.getBoolean(getString(R.string.set_key_saf_multithread), true)
            return SAF.Query(
                source = locations,
                exclude = excludedLocations,
                withHidden = withHidden,
                multithread = multithread,
                sourceOrigins = resolvedOrigins(locations),
            )
        }
        set(value) {
            val fileOnly = locationMode == LocationMode.DIRECT_FS
            val canonical = canonicalizeSafQuery(value, fileOnly)
            sharedPreferences.edit {
                putString(
                    getString(R.string.set_key_music_locations),
                    canonical.source.serializeLocations(),
                )
                putString(
                    getString(R.string.set_key_excluded_locations),
                    canonical.exclude.serializeLocations(),
                )
                putString(KEY_SOURCE_ORIGINS, serializeOrigins(canonical.sourceOrigins))
                putBoolean(getString(R.string.set_key_with_hidden), canonical.withHidden)
                putBoolean(
                    context.getString(R.string.set_key_saf_multithread),
                    canonical.multithread,
                )
                apply()
            }
        }

    override val configuredSourceCount: Int
        get() = rawConfiguredSourceCount(fileOnly = locationMode == LocationMode.DIRECT_FS)

    override var mediaStoreQuery: MediaStore.Query
        get() {
            val filterMode =
                sharedPreferences.getInt(
                    getString(R.string.set_key_filter_mode),
                    IntegerTable.FILTER_MODE_EXCLUDE,
                )
            val filteredLocations =
                unlikelyToBeNull(
                        sharedPreferences.getString(
                            getString(R.string.set_key_filtered_locations),
                            "",
                        )
                    )
                    .toUnopenedLocations(fileOnly = false)
            val excludeNonMusic =
                sharedPreferences.getBoolean(getString(R.string.set_key_exclude_non_music), true)
            return MediaStore.Query(
                mode =
                    when (filterMode) {
                        IntegerTable.FILTER_MODE_INCLUDE -> MediaStore.FilterMode.INCLUDE
                        IntegerTable.FILTER_MODE_EXCLUDE -> MediaStore.FilterMode.EXCLUDE
                        else -> MediaStore.FilterMode.EXCLUDE
                    },
                filtered = filteredLocations,
                excludeNonMusic = excludeNonMusic,
            )
        }
        set(value) {
            sharedPreferences.edit {
                val filterMode =
                    when (value.mode) {
                        MediaStore.FilterMode.INCLUDE -> IntegerTable.FILTER_MODE_INCLUDE
                        MediaStore.FilterMode.EXCLUDE -> IntegerTable.FILTER_MODE_EXCLUDE
                    }
                putInt(getString(R.string.set_key_filter_mode), filterMode)
                putString(
                    getString(R.string.set_key_filtered_locations),
                    value.filtered.stringify(),
                )
                putBoolean(getString(R.string.set_key_exclude_non_music), value.excludeNonMusic)
                apply()
            }
        }

    override fun applySourceConfiguration(
        mode: LocationMode,
        safQuery: SAF.Query,
        mediaStoreQuery: MediaStore.Query,
    ): Boolean =
        synchronized(SOURCE_CHECKPOINT_LOCK) {
            // Collapse before comparing and before persisting so a duplicate can never be stored,
            // and
            // so re-selecting the same folders is not mistaken for a configuration change.
            val canonicalQuery =
                canonicalizeSafQuery(safQuery, fileOnly = mode == LocationMode.DIRECT_FS)
            val changed =
                locationMode != mode ||
                    this.safQuery != canonicalQuery ||
                    this.mediaStoreQuery != mediaStoreQuery
            if (!changed) return@synchronized false

            val nextGeneration = sourceConfigurationGeneration + 1L
            val nowMs = System.currentTimeMillis()
            val priorCheckpoint = readSourceConfigurationCheckpointLocked()
            val supersededAttempt =
                priorCheckpoint?.attempt?.let { attempt ->
                    if (attempt.isTerminal) {
                        attempt
                    } else {
                        attempt.copy(
                            heartbeatAtMs = nowMs,
                            terminalAtMs = nowMs,
                            terminalOutcome = SourceScanAttemptOutcome.SUPERSEDED,
                            reason = "Superseded by source configuration generation $nextGeneration",
                        )
                    }
                } ?: priorCheckpoint?.previousAttempt
            val configuredKeys =
                if (mode == LocationMode.MEDIA_STORE) {
                    emptySet()
                } else {
                    canonicalQuery.source.mapTo(linkedSetOf()) { SourceIdentity.forLocation(it) }
                }
            val pendingCheckpoint =
                SourceConfigurationCheckpoint(
                    generation = nextGeneration,
                    state = SourceConfigurationCheckpoint.State.PENDING,
                    unresolvedSourceKeys =
                        priorCheckpoint?.unresolvedSourceKeys?.intersect(configuredKeys).orEmpty(),
                    previousAttempt = supersededAttempt,
                    reason = "Source configuration changed",
                )
            val persisted = commitPreferences {
                putInt(getString(R.string.set_key_locations_mode), mode.intCode)
                putString(
                    getString(R.string.set_key_music_locations),
                    canonicalQuery.source.serializeLocations(),
                )
                putString(
                    getString(R.string.set_key_excluded_locations),
                    canonicalQuery.exclude.serializeLocations(),
                )
                putString(KEY_SOURCE_ORIGINS, serializeOrigins(canonicalQuery.sourceOrigins))
                putBoolean(getString(R.string.set_key_with_hidden), canonicalQuery.withHidden)
                putBoolean(getString(R.string.set_key_saf_multithread), canonicalQuery.multithread)

                val filterMode =
                    when (mediaStoreQuery.mode) {
                        MediaStore.FilterMode.INCLUDE -> IntegerTable.FILTER_MODE_INCLUDE
                        MediaStore.FilterMode.EXCLUDE -> IntegerTable.FILTER_MODE_EXCLUDE
                    }
                putInt(getString(R.string.set_key_filter_mode), filterMode)
                putString(
                    getString(R.string.set_key_filtered_locations),
                    mediaStoreQuery.filtered.stringify(),
                )
                putBoolean(
                    getString(R.string.set_key_exclude_non_music),
                    mediaStoreQuery.excludeNonMusic,
                )
                putBoolean(getString(R.string.set_key_library_last_scan_failed), false)
                putLong(KEY_SOURCE_CONFIGURATION_GENERATION, nextGeneration)
                writeCheckpointLocked(this, pendingCheckpoint)
            }
            if (!persisted) {
                L.e("Failed to persist source configuration generation $nextGeneration")
                return@synchronized false
            }
            L.i("Persisted source configuration generation $nextGeneration [mode=$mode]")
            true
        }

    override fun recoverInterruptedSourceConfiguration(
        owner: SourceScanAttemptOwner,
        nowMs: Long,
    ): SourceConfigurationCheckpoint? =
        synchronized(SOURCE_CHECKPOINT_LOCK) {
            val checkpoint = readSourceConfigurationCheckpointLocked() ?: return@synchronized null
            val attempt = checkpoint.attempt ?: return@synchronized checkpoint
            if (
                checkpoint.state != SourceConfigurationCheckpoint.State.RUNNING ||
                    attempt.isTerminal
            ) {
                return@synchronized checkpoint
            }
            if (attempt.owner == owner) return@synchronized checkpoint
            val processRestarted = attempt.owner.processId != owner.processId
            val outcome =
                if (processRestarted) {
                    SourceScanAttemptOutcome.PROCESS_INTERRUPTED
                } else {
                    SourceScanAttemptOutcome.SERVICE_STOPPED
                }
            val recovered =
                checkpoint.copy(
                    state = SourceConfigurationCheckpoint.State.INTERRUPTED,
                    attempt =
                        attempt.copy(
                            heartbeatAtMs = nowMs,
                            terminalAtMs = nowMs,
                            terminalOutcome = outcome,
                            reason =
                                if (processRestarted) {
                                    "Recovered after process recreation"
                                } else {
                                    "Recovered after service lifecycle recreation"
                                },
                        ),
                    reason = outcome.name,
                )
            val persisted = commitPreferences {
                writeCheckpointLocked(this, recovered)
                putBoolean(getString(R.string.set_key_library_last_scan_failed), false)
            }
            if (!persisted) {
                L.e("Unable to terminalise stale source attempt ${attempt.attemptId}")
                return@synchronized checkpoint
            }
            recovered
        }

    override fun claimPendingConfiguration(
        expectedGeneration: Long,
        owner: SourceScanAttemptOwner,
        attemptId: String,
        nowMs: Long,
        reason: SourceScanClaimReason,
    ): SourceConfigurationCheckpoint? =
        synchronized(SOURCE_CHECKPOINT_LOCK) {
            require(attemptId.isNotBlank())
            val checkpoint = readSourceConfigurationCheckpointLocked() ?: return@synchronized null
            if (checkpoint.generation != expectedGeneration || !checkpoint.canClaim(reason)) {
                return@synchronized null
            }
            if (
                checkpoint.attempt?.attemptId == attemptId ||
                    checkpoint.previousAttempt?.attemptId == attemptId
            ) {
                return@synchronized null
            }
            val currentAttempt = checkpoint.attempt
            if (currentAttempt != null && !currentAttempt.isTerminal) return@synchronized null
            val attempt =
                SourceScanAttemptRecord(
                    generation = expectedGeneration,
                    attemptId = attemptId,
                    owner = owner,
                    claimedAtMs = nowMs,
                    heartbeatAtMs = nowMs,
                    progress = SourceScanAttemptProgress("PREPARING"),
                    reason = "Claimed for ${reason.name}",
                )
            val claimed =
                checkpoint.copy(
                    state = SourceConfigurationCheckpoint.State.RUNNING,
                    attempt = attempt,
                    previousAttempt = currentAttempt ?: checkpoint.previousAttempt,
                    reason = "Attempt running",
                )
            if (!commitPreferences { writeCheckpointLocked(this, claimed) }) {
                return@synchronized null
            }
            claimed
        }

    override fun handoffSourceConfigurationAttempt(
        generation: Long,
        attemptId: String,
        fromOwner: SourceScanAttemptOwner,
        toOwner: SourceScanAttemptOwner,
        nowMs: Long,
    ): Boolean =
        synchronized(SOURCE_CHECKPOINT_LOCK) {
            if (fromOwner.processId != toOwner.processId) return@synchronized false
            val checkpoint = readSourceConfigurationCheckpointLocked() ?: return@synchronized false
            val attempt = checkpoint.attempt ?: return@synchronized false
            if (
                checkpoint.generation != generation ||
                    checkpoint.state != SourceConfigurationCheckpoint.State.RUNNING ||
                    attempt.attemptId != attemptId ||
                    attempt.owner != fromOwner ||
                    attempt.isTerminal
            ) {
                return@synchronized false
            }
            val handedOff =
                checkpoint.copy(
                    attempt =
                        attempt.copy(
                            owner = toOwner,
                            heartbeatAtMs = nowMs,
                            reason = "Explicit same-process lifecycle handoff",
                        ),
                    reason = "Attempt owner handed off",
                )
            commitPreferences { writeCheckpointLocked(this, handedOff) }
        }

    override fun ownsSourceConfigurationAttempt(
        generation: Long,
        attemptId: String,
        owner: SourceScanAttemptOwner,
    ): Boolean =
        synchronized(SOURCE_CHECKPOINT_LOCK) {
            val checkpoint = readSourceConfigurationCheckpointLocked() ?: return@synchronized false
            val attempt = checkpoint.attempt ?: return@synchronized false
            checkpoint.generation == generation &&
                checkpoint.state == SourceConfigurationCheckpoint.State.RUNNING &&
                attempt.attemptId == attemptId &&
                attempt.owner == owner &&
                !attempt.isTerminal
        }

    override fun heartbeatSourceConfigurationAttempt(
        generation: Long,
        attemptId: String,
        owner: SourceScanAttemptOwner,
        nowMs: Long,
        progress: SourceScanAttemptProgress,
    ): Boolean =
        synchronized(SOURCE_CHECKPOINT_LOCK) {
            val checkpoint = readSourceConfigurationCheckpointLocked() ?: return@synchronized false
            val attempt = checkpoint.attempt ?: return@synchronized false
            if (
                checkpoint.generation != generation ||
                    checkpoint.state != SourceConfigurationCheckpoint.State.RUNNING ||
                    attempt.attemptId != attemptId ||
                    attempt.owner != owner ||
                    attempt.isTerminal
            ) {
                return@synchronized false
            }
            val boundedProgress =
                progress.copy(currentItem = progress.currentItem?.take(MAX_DIAGNOSTIC_TEXT_LENGTH))
            val updated =
                checkpoint.copy(
                    attempt =
                        attempt.copy(
                            heartbeatAtMs = nowMs,
                            progress = boundedProgress,
                            reason = "Meaningful ${boundedProgress.phase.lowercase()} progress",
                        )
                )
            sharedPreferences.edit { writeCheckpointLocked(this, updated) }
            true
        }

    override fun completeSourceConfigurationAttempt(
        generation: Long,
        attemptId: String,
        owner: SourceScanAttemptOwner,
        nowMs: Long,
        completion: SourceScanAttemptCompletion,
        publishAfterCommit: () -> Unit,
    ): Boolean =
        synchronized(SOURCE_CHECKPOINT_LOCK) {
            val checkpoint = readSourceConfigurationCheckpointLocked() ?: return@synchronized false
            val attempt = checkpoint.attempt ?: return@synchronized false
            if (
                checkpoint.generation != generation ||
                    checkpoint.state != SourceConfigurationCheckpoint.State.RUNNING ||
                    attempt.attemptId != attemptId ||
                    attempt.owner != owner ||
                    attempt.isTerminal
            ) {
                return@synchronized false
            }
            val terminalAttempt =
                attempt.copy(
                    heartbeatAtMs = nowMs,
                    terminalAtMs = nowMs,
                    terminalOutcome = completion.outcome,
                    reason = completion.reason.take(MAX_DIAGNOSTIC_TEXT_LENGTH),
                    failureClass = completion.failureClass?.take(MAX_DIAGNOSTIC_TEXT_LENGTH),
                    failureMessage = completion.failureMessage?.take(MAX_DIAGNOSTIC_TEXT_LENGTH),
                )
            val completed =
                checkpoint.copy(
                    state = completion.checkpointState(),
                    unresolvedSourceKeys = completion.unresolvedSourceKeys,
                    attempt = terminalAttempt,
                    reason = completion.reason.take(MAX_DIAGNOSTIC_TEXT_LENGTH),
                )
            val editor = sharedPreferences.edit()
            writeCheckpointLocked(editor, completed)
            editor.putBoolean(
                getString(R.string.set_key_library_last_scan_failed),
                completion.lastScanFailed,
            )
            completion.publishedRevision?.let {
                editor.putString(getString(R.string.set_key_library_revision), it.toString())
            }
            completion.publishedLibraryState?.let {
                editor.putString(getString(R.string.set_key_library_state), it.name)
            }
            if (!editor.commit()) return@synchronized false
            // This callback deliberately runs while SOURCE_CHECKPOINT_LOCK is held. Publishing
            // after
            // releasing it would let a newer generation interleave between durable acknowledgement
            // and in-memory publication. Callers must follow checkpoint-lock -> repository-monitor
            // order; repository code must never acquire a checkpoint operation under its monitor.
            publishAfterCommit()
            true
        }

    override fun markSourcesUnresolved(sourceKeys: Set<String>, outcome: String) {
        if (sourceKeys.isEmpty()) return
        synchronized(SOURCE_CHECKPOINT_LOCK) {
            val checkpoint = readSourceConfigurationCheckpointLocked() ?: return@synchronized
            val state =
                when (checkpoint.state) {
                    SourceConfigurationCheckpoint.State.COMMITTED,
                    SourceConfigurationCheckpoint.State.PARTIALLY_COMMITTED ->
                        SourceConfigurationCheckpoint.State.PARTIALLY_COMMITTED
                    else -> checkpoint.state
                }
            val updated =
                checkpoint.copy(
                    state = state,
                    unresolvedSourceKeys = checkpoint.unresolvedSourceKeys + sourceKeys,
                    reason = outcome.take(MAX_DIAGNOSTIC_TEXT_LENGTH),
                )
            if (!commitPreferences { writeCheckpointLocked(this, updated) }) {
                L.e("Failed to persist unresolved source checkpoint")
            }
        }
    }

    override fun forceLocationUpdate() {
        markInitialScanPending()
        listeners.forEach { it.onMusicLocationsChanged() }
    }

    private fun markInitialScanPending() {
        synchronized(SOURCE_CHECKPOINT_LOCK) {
            val nextGeneration = sourceConfigurationGeneration + 1L
            val nowMs = System.currentTimeMillis()
            val prior = readSourceConfigurationCheckpointLocked()
            val superseded =
                prior?.attempt?.let { attempt ->
                    if (attempt.isTerminal) {
                        attempt
                    } else {
                        attempt.copy(
                            heartbeatAtMs = nowMs,
                            terminalAtMs = nowMs,
                            terminalOutcome = SourceScanAttemptOutcome.SUPERSEDED,
                            reason = "Superseded by forced generation $nextGeneration",
                        )
                    }
                } ?: prior?.previousAttempt
            val pending =
                SourceConfigurationCheckpoint(
                    generation = nextGeneration,
                    state = SourceConfigurationCheckpoint.State.PENDING,
                    unresolvedSourceKeys = prior?.unresolvedSourceKeys.orEmpty(),
                    previousAttempt = superseded,
                    reason = "Forced source refresh",
                )
            val persisted = commitPreferences {
                putBoolean(getString(R.string.set_key_library_last_scan_failed), false)
                putLong(KEY_SOURCE_CONFIGURATION_GENERATION, nextGeneration)
                writeCheckpointLocked(this, pending)
            }
            if (!persisted) {
                L.e("Unable to persist forced source generation $nextGeneration")
            }
        }
    }

    override fun onSettingChanged(key: String, listener: MusicSettings.Listener) {
        when (key) {
            getString(R.string.set_key_locations_mode),
            getString(R.string.set_key_music_locations) -> {
                L.d("Dispatching music locations change")
                listener.onMusicLocationsChanged()
            }
            getString(R.string.set_key_excluded_locations),
            getString(R.string.set_key_with_hidden),
            context.getString(R.string.set_key_saf_multithread),
            getString(R.string.set_key_filter_mode),
            getString(R.string.set_key_filtered_locations),
            getString(R.string.set_key_exclude_non_music),
            getString(R.string.set_key_separators),
            getString(R.string.set_key_auto_sort_names),
            KEY_TS18_SYSTEM_SOURCE_FILTER -> {
                L.d("Dispatching indexing setting change for $key")
                listener.onIndexingSettingChanged()
            }
            getString(R.string.set_key_observing),
            getString(R.string.set_key_observation_mode) -> {
                L.d("Dispatching observing setting change")
                listener.onObservingChanged()
            }
            getString(R.string.set_key_scan_priority),
            getString(R.string.set_key_root_access_policy) -> {
                L.d("Dispatching indexing setting change for $key")
                listener.onIndexingSettingChanged()
            }
            getString(R.string.set_key_generated_playlists) -> {
                L.d("Applying generated-playlist preference without source reindex")
                listener.onGeneratedPlaylistsChanged()
            }
            getString(R.string.set_key_performance_capture) -> {
                // Diagnostics-only toggle: refresh the bounded capture state without
                // requesting or starting a library reindex.
                L.d("Applying performance capture change without reindex")
                PerfTimer.configure(performanceCaptureEnabled)
            }
        }
    }

    private fun List<Location>.stringify(): String =
        MusicSourceCanonicalizer.collapseLocations(this).joinToString(separator = ";") {
            it.uri.toString().replace(";", "\\;")
        }

    /** Serialises an already canonical location list without reintroducing raw aliases. */
    private fun List<Location>.serializeLocations(): String =
        joinToString(separator = ";") { it.uri.toString().replace(";", "\\;") }

    private fun canonicalizeSafQuery(value: SAF.Query, fileOnly: Boolean): SAF.Query {
        val sourceEntries =
            value.source.mapNotNull {
                MusicSourcePathNormalizer.normalizePersistedLocation(it.uri.toString(), fileOnly)
            }
        val canonicalSources =
            MusicSourceCanonicalizer.collapseEntries(sourceEntries, fileOnly).mapNotNull {
                Location.Unopened.from(context, it.toUri()).open(context)
            }
        val excludeEntries =
            value.exclude.mapNotNull {
                MusicSourcePathNormalizer.normalizePersistedLocation(it.uri.toString(), fileOnly)
            }
        val canonicalExcludes =
            MusicSourceCanonicalizer.collapseEntries(excludeEntries, fileOnly).mapNotNull {
                Location.Unopened.from(context, it.toUri())
            }
        val origins = linkedMapOf<String, CanonicalSourcePolicy.Origin>()
        for (source in canonicalSources) {
            val key = MusicSourceCanonicalizer.canonicalKeyOf(source)
            origins[key] =
                value.sourceOrigins[key]
                    ?: CanonicalSourcePolicy.legacyOriginForPath(
                        MusicSourceCanonicalizer.appFacingPathOf(source)
                    )
        }
        return value.copy(
            source = canonicalSources,
            exclude = canonicalExcludes,
            sourceOrigins = origins,
        )
    }

    private fun resolvedOrigins(
        locations: List<Location>
    ): Map<String, CanonicalSourcePolicy.Origin> {
        val stored = parseOrigins(sharedPreferences.getString(KEY_SOURCE_ORIGINS, null))
        return buildMap {
            for (location in locations) {
                val key = MusicSourceCanonicalizer.canonicalKeyOf(location)
                put(
                    key,
                    stored[key]
                        ?: CanonicalSourcePolicy.legacyOriginForPath(
                            MusicSourceCanonicalizer.appFacingPathOf(location)
                        ),
                )
            }
        }
    }

    private fun parseOrigins(raw: String?): Map<String, CanonicalSourcePolicy.Origin> =
        raw.orEmpty()
            .split(';')
            .mapNotNull { entry ->
                val separator = entry.lastIndexOf('=')
                if (separator <= 0) return@mapNotNull null
                val key = android.net.Uri.decode(entry.substring(0, separator))
                val origin =
                    runCatching {
                            CanonicalSourcePolicy.Origin.valueOf(entry.substring(separator + 1))
                        }
                        .getOrNull() ?: return@mapNotNull null
                key to origin
            }
            .toMap(linkedMapOf())

    private fun serializeOrigins(origins: Map<String, CanonicalSourcePolicy.Origin>): String =
        origins.entries.joinToString(separator = ";") { (key, origin) ->
            "${android.net.Uri.encode(key)}=${origin.name}"
        }

    /**
     * Effective configured source count, which is what the user configured and what will actually
     * be scanned. Exact canonical duplicates are not separate sources and must never be counted as
     * such: the TS18 diagnostic that motivated this work reported two identical
     * `/storage/emulated/0/Music` entries as `configuredSourceCount=2`.
     */
    private fun rawConfiguredSourceCount(fileOnly: Boolean): Int {
        repairPersistedSourceDuplicates(fileOnly)
        return unlikelyToBeNull(
                sharedPreferences.getString(getString(R.string.set_key_music_locations), "")
            )
            .toCanonicalEntries(fileOnly)
            .size
    }

    private fun String.toOpenedLocations(fileOnly: Boolean): List<Location.Opened> =
        toCanonicalEntries(fileOnly).mapNotNull {
            Location.Unopened.from(context, it.toUri()).open(context)
        }

    private fun String.toUnopenedLocations(fileOnly: Boolean): List<Location.Unopened> =
        toCanonicalEntries(fileOnly).mapNotNull { Location.Unopened.from(context, it.toUri()) }

    /** Normalises a persisted list and collapses exact canonical duplicates. */
    private fun String.toCanonicalEntries(fileOnly: Boolean): List<String> =
        MusicSourceCanonicalizer.collapseEntries(
            splitEscaped { it == ';' }
                .filter { it.isNotBlank() }
                .mapNotNull { MusicSourcePathNormalizer.normalizePersistedLocation(it, fileOnly) },
            fileOnly,
        )

    /**
     * One-shot read-repair for source lists persisted by older builds.
     *
     * The repair is idempotent and deliberately does not touch
     * [KEY_SOURCE_CONFIGURATION_GENERATION]: dropping an exact canonical duplicate cannot change
     * the effective scan scope, so it must not queue another full rescan or invalidate the cached
     * library.
     */
    @Synchronized
    private fun repairPersistedSourceDuplicates(fileOnly: Boolean) {
        val sourceKey = getString(R.string.set_key_music_locations)
        val excludeKey = getString(R.string.set_key_excluded_locations)
        val rawSources = unlikelyToBeNull(sharedPreferences.getString(sourceKey, ""))
        val rawExcludes = unlikelyToBeNull(sharedPreferences.getString(excludeKey, ""))
        val canonicalSources = rawSources.toCanonicalEntries(fileOnly)
        val canonicalExcludes = rawExcludes.toCanonicalEntries(fileOnly)
        val serialisedSources =
            canonicalSources.joinToString(separator = ";") { it.replace(";", "\\;") }
        val serialisedExcludes =
            canonicalExcludes.joinToString(separator = ";") { it.replace(";", "\\;") }
        val storedOrigins = parseOrigins(sharedPreferences.getString(KEY_SOURCE_ORIGINS, null))
        val canonicalOrigins = linkedMapOf<String, CanonicalSourcePolicy.Origin>()
        for (entry in canonicalSources) {
            val uri = entry.toUri()
            val key = MusicSourceCanonicalizer.canonicalKeyOfUri(uri)
            canonicalOrigins[key] =
                storedOrigins[key]
                    ?: CanonicalSourcePolicy.legacyOriginForPath(
                        if (uri.scheme == "file") uri.path
                        else CanonicalSourcePolicy.externalStorageTreePath(uri)
                    )
        }
        val serialisedOrigins = serializeOrigins(canonicalOrigins)
        val rawOrigins = sharedPreferences.getString(KEY_SOURCE_ORIGINS, "").orEmpty()
        if (
            rawSources == serialisedSources &&
                rawExcludes == serialisedExcludes &&
                rawOrigins == serialisedOrigins
        )
            return
        // apply() updates the in-memory preference map before returning and persists it
        // asynchronously. Serialising this read-repair prevents concurrent getters from queuing
        // the same migration while keeping disk I/O off the caller (often the main thread).
        sharedPreferences.edit {
            putString(sourceKey, serialisedSources)
            putString(excludeKey, serialisedExcludes)
            putString(KEY_SOURCE_ORIGINS, serialisedOrigins)
        }
        L.i(
            "Canonicalised persisted music sources without changing the source configuration " +
                "generation"
        )
    }

    private fun readSourceConfigurationCheckpointLocked(): SourceConfigurationCheckpoint? {
        val generation = sourceConfigurationGeneration
        if (generation <= 0L) return null
        val storedState =
            SourceConfigurationCheckpoint.State.entries.firstOrNull {
                it.name == sharedPreferences.getString(KEY_CHECKPOINT_STATE, null)
            }
        val state =
            storedState
                ?: if (sharedPreferences.getBoolean(KEY_PENDING_INITIAL_SCAN, false)) {
                    SourceConfigurationCheckpoint.State.PENDING
                } else {
                    SourceConfigurationCheckpoint.State.COMMITTED
                }
        var attempt = readAttemptLocked(ATTEMPT_PREFIX, generation)
        if (attempt == null && state == SourceConfigurationCheckpoint.State.RUNNING) {
            // Migration for checkpoints written before attempt leases existed. It is deliberately
            // treated as ownerless so startup recovery records it interrupted before any retry.
            val claimedAt =
                sharedPreferences.getLong(KEY_CHECKPOINT_LAST_ATTEMPT, Long.MIN_VALUE).takeUnless {
                    it == Long.MIN_VALUE
                } ?: 0L
            attempt =
                SourceScanAttemptRecord(
                    generation = generation,
                    attemptId = "legacy-generation-$generation",
                    owner = SourceScanAttemptOwner("legacy-process", "legacy-lifecycle"),
                    claimedAtMs = claimedAt,
                    heartbeatAtMs = claimedAt,
                    progress = SourceScanAttemptProgress("UNKNOWN"),
                    reason = "Migrated checkpoint without attempt identity",
                )
        }
        return SourceConfigurationCheckpoint(
            generation = generation,
            state = state,
            unresolvedSourceKeys =
                sharedPreferences.getStringSet(KEY_CHECKPOINT_UNRESOLVED, emptySet()).orEmpty(),
            attempt = attempt,
            previousAttempt = readAttemptLocked(PREVIOUS_ATTEMPT_PREFIX, generation),
            reason =
                sharedPreferences.getString(KEY_CHECKPOINT_REASON, null)
                    ?: sharedPreferences.getString(KEY_CHECKPOINT_LAST_OUTCOME, null),
        )
    }

    private fun readAttemptLocked(
        prefix: String,
        fallbackGeneration: Long,
    ): SourceScanAttemptRecord? {
        val attemptId = sharedPreferences.getString(prefix + ATTEMPT_ID_SUFFIX, null) ?: return null
        val processId =
            sharedPreferences.getString(prefix + OWNER_PROCESS_SUFFIX, null) ?: "legacy-process"
        val lifecycleId =
            sharedPreferences.getString(prefix + OWNER_LIFECYCLE_SUFFIX, null) ?: "legacy-lifecycle"
        val claimedAt = sharedPreferences.getLong(prefix + CLAIMED_AT_SUFFIX, 0L)
        val heartbeatAt = sharedPreferences.getLong(prefix + HEARTBEAT_AT_SUFFIX, claimedAt)
        val phase = sharedPreferences.getString(prefix + PROGRESS_PHASE_SUFFIX, null)
        val progress =
            phase?.let {
                SourceScanAttemptProgress(
                    phase = it,
                    explored = sharedPreferences.getInt(prefix + PROGRESS_EXPLORED_SUFFIX, 0),
                    loaded = sharedPreferences.getInt(prefix + PROGRESS_LOADED_SUFFIX, 0),
                    evaluated = sharedPreferences.getInt(prefix + PROGRESS_EVALUATED_SUFFIX, 0),
                    currentItem = sharedPreferences.getString(prefix + PROGRESS_ITEM_SUFFIX, null),
                    directFsDirectoriesVisited =
                        sharedPreferences.optionalInt(prefix + PROGRESS_DIRECTORIES_SUFFIX),
                    directFsEntriesInspected =
                        sharedPreferences.optionalInt(prefix + PROGRESS_ENTRIES_SUFFIX),
                    directFsFilesEmitted =
                        sharedPreferences.optionalInt(prefix + PROGRESS_FILES_SUFFIX),
                    queuedDirectFsWork =
                        sharedPreferences.optionalInt(prefix + PROGRESS_QUEUED_SUFFIX),
                    activeDirectFsEnumerators =
                        sharedPreferences.optionalInt(prefix + PROGRESS_ACTIVE_SUFFIX),
                )
            }
        return SourceScanAttemptRecord(
            generation =
                sharedPreferences.getLong(prefix + ATTEMPT_GENERATION_SUFFIX, fallbackGeneration),
            attemptId = attemptId,
            owner = SourceScanAttemptOwner(processId, lifecycleId),
            claimedAtMs = claimedAt,
            heartbeatAtMs = heartbeatAt,
            progress = progress,
            terminalAtMs =
                sharedPreferences.getLong(prefix + TERMINAL_AT_SUFFIX, Long.MIN_VALUE).takeUnless {
                    it == Long.MIN_VALUE
                },
            terminalOutcome =
                SourceScanAttemptOutcome.entries.firstOrNull {
                    it.name == sharedPreferences.getString(prefix + TERMINAL_OUTCOME_SUFFIX, null)
                },
            reason = sharedPreferences.getString(prefix + ATTEMPT_REASON_SUFFIX, null),
            failureClass = sharedPreferences.getString(prefix + FAILURE_CLASS_SUFFIX, null),
            failureMessage = sharedPreferences.getString(prefix + FAILURE_MESSAGE_SUFFIX, null),
        )
    }

    private fun writeCheckpointLocked(
        editor: SharedPreferences.Editor,
        checkpoint: SourceConfigurationCheckpoint,
    ) {
        val attempt = checkpoint.attempt
        val previousAttempt = checkpoint.previousAttempt
        fun SourceScanAttemptRecord.hasConsistentTerminalPair(): Boolean {
            val hasTerminalAt = terminalAtMs != null
            val hasTerminalOutcome = terminalOutcome != null
            return hasTerminalAt == hasTerminalOutcome
        }

        require(attempt == null || attempt.generation == checkpoint.generation)
        require(attempt == null || attempt.hasConsistentTerminalPair())
        require(previousAttempt == null || previousAttempt.isTerminal)
        require(previousAttempt == null || previousAttempt.hasConsistentTerminalPair())
        if (checkpoint.state == SourceConfigurationCheckpoint.State.RUNNING) {
            require(attempt != null && !attempt.isTerminal)
        } else if (attempt != null) {
            require(attempt.isTerminal)
        }
        editor.putBoolean(
            KEY_PENDING_INITIAL_SCAN,
            checkpoint.state == SourceConfigurationCheckpoint.State.PENDING ||
                checkpoint.state == SourceConfigurationCheckpoint.State.INTERRUPTED,
        )
        editor.putString(KEY_CHECKPOINT_STATE, checkpoint.state.name)
        editor.putStringSet(KEY_CHECKPOINT_UNRESOLVED, checkpoint.unresolvedSourceKeys)
        editor.putString(KEY_CHECKPOINT_REASON, checkpoint.reason)
        editor.writeAttempt(ATTEMPT_PREFIX, checkpoint.attempt)
        editor.writeAttempt(PREVIOUS_ATTEMPT_PREFIX, checkpoint.previousAttempt)
        editor.remove(KEY_CHECKPOINT_LAST_ATTEMPT)
        editor.remove(KEY_CHECKPOINT_LAST_OUTCOME)
    }

    private inline fun commitPreferences(mutate: SharedPreferences.Editor.() -> Unit): Boolean {
        val editor = sharedPreferences.edit()
        editor.mutate()
        return editor.commit()
    }

    private fun SharedPreferences.Editor.writeAttempt(
        prefix: String,
        attempt: SourceScanAttemptRecord?,
    ) {
        val keys =
            listOf(
                ATTEMPT_ID_SUFFIX,
                ATTEMPT_GENERATION_SUFFIX,
                OWNER_PROCESS_SUFFIX,
                OWNER_LIFECYCLE_SUFFIX,
                CLAIMED_AT_SUFFIX,
                HEARTBEAT_AT_SUFFIX,
                TERMINAL_AT_SUFFIX,
                TERMINAL_OUTCOME_SUFFIX,
                ATTEMPT_REASON_SUFFIX,
                FAILURE_CLASS_SUFFIX,
                FAILURE_MESSAGE_SUFFIX,
                PROGRESS_PHASE_SUFFIX,
                PROGRESS_EXPLORED_SUFFIX,
                PROGRESS_LOADED_SUFFIX,
                PROGRESS_EVALUATED_SUFFIX,
                PROGRESS_ITEM_SUFFIX,
                PROGRESS_DIRECTORIES_SUFFIX,
                PROGRESS_ENTRIES_SUFFIX,
                PROGRESS_FILES_SUFFIX,
                PROGRESS_QUEUED_SUFFIX,
                PROGRESS_ACTIVE_SUFFIX,
            )
        if (attempt == null) {
            keys.forEach { remove(prefix + it) }
            return
        }
        putString(prefix + ATTEMPT_ID_SUFFIX, attempt.attemptId)
        putLong(prefix + ATTEMPT_GENERATION_SUFFIX, attempt.generation)
        putString(prefix + OWNER_PROCESS_SUFFIX, attempt.owner.processId)
        putString(prefix + OWNER_LIFECYCLE_SUFFIX, attempt.owner.lifecycleId)
        putLong(prefix + CLAIMED_AT_SUFFIX, attempt.claimedAtMs)
        putLong(prefix + HEARTBEAT_AT_SUFFIX, attempt.heartbeatAtMs)
        attempt.terminalAtMs?.let { putLong(prefix + TERMINAL_AT_SUFFIX, it) }
            ?: remove(prefix + TERMINAL_AT_SUFFIX)
        putString(prefix + TERMINAL_OUTCOME_SUFFIX, attempt.terminalOutcome?.name)
        putString(prefix + ATTEMPT_REASON_SUFFIX, attempt.reason)
        putString(prefix + FAILURE_CLASS_SUFFIX, attempt.failureClass)
        putString(prefix + FAILURE_MESSAGE_SUFFIX, attempt.failureMessage)
        putString(prefix + PROGRESS_PHASE_SUFFIX, attempt.progress?.phase)
        if (attempt.progress != null) {
            putInt(prefix + PROGRESS_EXPLORED_SUFFIX, attempt.progress.explored)
            putInt(prefix + PROGRESS_LOADED_SUFFIX, attempt.progress.loaded)
            putInt(prefix + PROGRESS_EVALUATED_SUFFIX, attempt.progress.evaluated)
            putString(prefix + PROGRESS_ITEM_SUFFIX, attempt.progress.currentItem)
            putOptionalInt(
                prefix + PROGRESS_DIRECTORIES_SUFFIX,
                attempt.progress.directFsDirectoriesVisited,
            )
            putOptionalInt(
                prefix + PROGRESS_ENTRIES_SUFFIX,
                attempt.progress.directFsEntriesInspected,
            )
            putOptionalInt(prefix + PROGRESS_FILES_SUFFIX, attempt.progress.directFsFilesEmitted)
            putOptionalInt(prefix + PROGRESS_QUEUED_SUFFIX, attempt.progress.queuedDirectFsWork)
            putOptionalInt(
                prefix + PROGRESS_ACTIVE_SUFFIX,
                attempt.progress.activeDirectFsEnumerators,
            )
        } else {
            remove(prefix + PROGRESS_EXPLORED_SUFFIX)
            remove(prefix + PROGRESS_LOADED_SUFFIX)
            remove(prefix + PROGRESS_EVALUATED_SUFFIX)
            remove(prefix + PROGRESS_ITEM_SUFFIX)
            remove(prefix + PROGRESS_DIRECTORIES_SUFFIX)
            remove(prefix + PROGRESS_ENTRIES_SUFFIX)
            remove(prefix + PROGRESS_FILES_SUFFIX)
            remove(prefix + PROGRESS_QUEUED_SUFFIX)
            remove(prefix + PROGRESS_ACTIVE_SUFFIX)
        }
    }

    private fun SharedPreferences.optionalInt(key: String): Int? =
        if (contains(key)) getInt(key, 0) else null

    private fun SharedPreferences.Editor.putOptionalInt(key: String, value: Int?) {
        if (value == null) remove(key) else putInt(key, value)
    }

    private fun SourceScanAttemptCompletion.checkpointState(): SourceConfigurationCheckpoint.State =
        when (outcome) {
            SourceScanAttemptOutcome.SUCCESS,
            SourceScanAttemptOutcome.AUTHORITATIVE_EMPTY ->
                if (unresolvedSourceKeys.isEmpty()) {
                    SourceConfigurationCheckpoint.State.COMMITTED
                } else {
                    SourceConfigurationCheckpoint.State.PARTIALLY_COMMITTED
                }
            SourceScanAttemptOutcome.PARTIAL_SUCCESS,
            SourceScanAttemptOutcome.TRUNCATED ->
                SourceConfigurationCheckpoint.State.PARTIALLY_COMMITTED
            SourceScanAttemptOutcome.TEMPORARILY_UNAVAILABLE,
            SourceScanAttemptOutcome.PERMISSION_REQUIRED,
            SourceScanAttemptOutcome.FAILED_RETRYABLE ->
                SourceConfigurationCheckpoint.State.FAILED_RETRYABLE
            SourceScanAttemptOutcome.FAILED_FINAL ->
                SourceConfigurationCheckpoint.State.FAILED_FINAL
            SourceScanAttemptOutcome.CANCELLED -> SourceConfigurationCheckpoint.State.CANCELLED
            SourceScanAttemptOutcome.TIMED_OUT -> SourceConfigurationCheckpoint.State.TIMED_OUT
            SourceScanAttemptOutcome.SERVICE_STOPPED,
            SourceScanAttemptOutcome.PROCESS_INTERRUPTED,
            SourceScanAttemptOutcome.SUPERSEDED -> SourceConfigurationCheckpoint.State.INTERRUPTED
        }

    private inline fun String.splitEscaped(selector: (Char) -> Boolean): List<String> {
        val split = mutableListOf<String>()
        var currentString = ""
        var i = 0

        while (i < length) {
            val a = get(i)
            val b = getOrNull(i + 1)

            if (selector(a)) {
                // Non-escaped separator, split the string here, making sure any stray whitespace
                // is removed.
                split.add(currentString)
                currentString = ""
                i++
                continue
            }

            if (b != null && a == '\\' && selector(b)) {
                // Is an escaped character, add the non-escaped variant and skip two
                // characters to move on to the next one.
                currentString += b
                i += 2
            } else {
                // Non-escaped, increment normally.
                currentString += a
                i++
            }
        }

        if (currentString.isNotEmpty()) {
            // Had an in-progress split string that is now terminated, add it.
            split.add(currentString)
        }

        return split
    }

    private companion object {
        val SOURCE_CHECKPOINT_LOCK = Any()
        const val MAX_DIAGNOSTIC_TEXT_LENGTH = 240
        const val KEY_TS18_SYSTEM_SOURCE_FILTER = "auxio_ts18_system_source_filter"
        const val KEY_PENDING_INITIAL_SCAN = "auxio_pending_initial_music_scan"
        const val KEY_SOURCE_CONFIGURATION_GENERATION = "auxio_source_configuration_generation"
        const val KEY_SOURCE_ORIGINS = "auxio_source_origins"
        const val KEY_CHECKPOINT_STATE = "auxio_source_checkpoint_state"
        const val KEY_CHECKPOINT_UNRESOLVED = "auxio_source_checkpoint_unresolved"
        const val KEY_CHECKPOINT_LAST_ATTEMPT = "auxio_source_checkpoint_last_attempt"
        const val KEY_CHECKPOINT_LAST_OUTCOME = "auxio_source_checkpoint_last_outcome"
        const val KEY_CHECKPOINT_REASON = "auxio_source_checkpoint_reason"
        const val ATTEMPT_PREFIX = "auxio_source_checkpoint_attempt_"
        const val PREVIOUS_ATTEMPT_PREFIX = "auxio_source_checkpoint_previous_attempt_"
        const val ATTEMPT_ID_SUFFIX = "id"
        const val ATTEMPT_GENERATION_SUFFIX = "generation"
        const val OWNER_PROCESS_SUFFIX = "owner_process"
        const val OWNER_LIFECYCLE_SUFFIX = "owner_lifecycle"
        const val CLAIMED_AT_SUFFIX = "claimed_at"
        const val HEARTBEAT_AT_SUFFIX = "heartbeat_at"
        const val TERMINAL_AT_SUFFIX = "terminal_at"
        const val TERMINAL_OUTCOME_SUFFIX = "terminal_outcome"
        const val ATTEMPT_REASON_SUFFIX = "reason"
        const val FAILURE_CLASS_SUFFIX = "failure_class"
        const val FAILURE_MESSAGE_SUFFIX = "failure_message"
        const val PROGRESS_PHASE_SUFFIX = "progress_phase"
        const val PROGRESS_EXPLORED_SUFFIX = "progress_explored"
        const val PROGRESS_LOADED_SUFFIX = "progress_loaded"
        const val PROGRESS_EVALUATED_SUFFIX = "progress_evaluated"
        const val PROGRESS_ITEM_SUFFIX = "progress_item"
        const val PROGRESS_DIRECTORIES_SUFFIX = "progress_directories"
        const val PROGRESS_ENTRIES_SUFFIX = "progress_entries"
        const val PROGRESS_FILES_SUFFIX = "progress_files"
        const val PROGRESS_QUEUED_SUFFIX = "progress_queued"
        const val PROGRESS_ACTIVE_SUFFIX = "progress_active"
    }
}

/** Policy controlling when source changes trigger an automatic library refresh. */
enum class ObservationMode {
    MANUAL,
    WHEN_IDLE,
    CONTINUOUS;

    companion object {
        fun fromName(name: String?) = entries.firstOrNull { it.name == name }
    }
}

/** Policy controlling whether DirectFS may fall back to bounded root-assisted listing. */
enum class RootAccessPolicy {
    OFF,
    ON_DEMAND;

    companion object {
        fun fromName(name: String?) = entries.firstOrNull { it.name == name }
    }
}

/** Persisted startup state for the indexed music library. */
enum class LibraryState {
    /** No successful library scan has ever been recorded. */
    NEVER,
    /** A non-empty cached/indexed library exists and can be shown before scanning. */
    USABLE,
    /** A scan completed successfully, but no music was found. */
    EMPTY,
    /** Cached data could not be used or the previous startup scan failed. */
    RECOVERY;

    companion object {
        fun fromName(name: String?) = entries.firstOrNull { it.name == name }
    }
}
