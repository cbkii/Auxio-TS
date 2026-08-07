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
import androidx.core.content.edit
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.UUID
import javax.inject.Inject
import org.oxycblt.auxio.IntegerTable
import org.oxycblt.auxio.R
import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.auxio.music.locations.MusicSourcePathNormalizer
import org.oxycblt.auxio.settings.Settings
import org.oxycblt.auxio.util.PerfTimer
import org.oxycblt.auxio.util.unlikelyToBeNull
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

    /** Mark a pending generation running without clearing its durable record. */
    fun claimPendingConfiguration(): SourceConfigurationCheckpoint? = null

    /** Resolve only a matching generation and attempt after a structured scan result. */
    fun acknowledgeSourceConfiguration(
        generation: Long,
        attemptId: String,
        unresolvedSourceKeys: Set<String>,
        outcome: String,
    ) = Unit

    /** Complete a failed attempt, returning the generation to a retryable state if applicable. */
    fun failSourceConfigurationAttempt(
        generation: Long,
        attemptId: String,
        retryable: Boolean,
        outcome: String,
    ) = Unit

    /** Mark the current attempt as interrupted by process or service lifecycle. */
    fun markAttemptInterrupted(generation: Long, attemptId: String, outcome: String) = Unit

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
                ?: if (org.oxycblt.auxio.BuildConfig.TOPWAY_COMPAT_FLAVOR) {
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
                !org.oxycblt.auxio.BuildConfig.TOPWAY_COMPAT_FLAVOR,
            )

    override val performanceCaptureEnabled: Boolean
        get() = sharedPreferences.getBoolean(getString(R.string.set_key_performance_capture), false)

    override val sourceConfigurationGeneration: Long
        get() = sharedPreferences.getLong(KEY_SOURCE_CONFIGURATION_GENERATION, 0L)

    override val configuredSourceSpecs: List<ConfiguredSourceSpec>
        get() {
            if (locationMode == LocationMode.MEDIA_STORE) return emptyList()
            val fileOnly = locationMode == LocationMode.DIRECT_FS
            val locations =
                unlikelyToBeNull(
                        sharedPreferences.getString(getString(R.string.set_key_music_locations), "")
                    )
                    .splitEscaped { it == ';' }
                    .toUnopenedLocations(fileOnly)
            val grants = context.contentResolver.persistedUriPermissions
            return locations.map { location ->
                val uri = location.uri
                val access =
                    if (uri.scheme == "file") {
                        val file = uri.path?.let(::File)
                        if (file?.canRead() == true && file.isDirectory) {
                            ConfiguredSourceSpec.AccessState.AVAILABLE
                        } else {
                            ConfiguredSourceSpec.AccessState.TEMPORARILY_UNAVAILABLE
                        }
                    } else if (grants.any { it.uri == uri && it.isReadPermission }) {
                        ConfiguredSourceSpec.AccessState.AVAILABLE
                    } else {
                        ConfiguredSourceSpec.AccessState.PERMISSION_REQUIRED
                    }
                ConfiguredSourceSpec(
                    normalizedUri = uri,
                    sourceKey = SourceIdentity.forLocation(location),
                    mode = locationMode,
                    displayPath =
                        uri.path?.takeIf { it.isNotBlank() } ?: location.path.components.unixString,
                    accessState = access,
                )
            }
        }

    override val sourceConfigurationCheckpoint: SourceConfigurationCheckpoint?
        get() {
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
            return SourceConfigurationCheckpoint(
                generation = generation,
                state = state,
                unresolvedSourceKeys =
                    sharedPreferences.getStringSet(KEY_CHECKPOINT_UNRESOLVED, emptySet()).orEmpty(),
                lastAttemptAtMs =
                    sharedPreferences
                        .getLong(KEY_CHECKPOINT_LAST_ATTEMPT, Long.MIN_VALUE)
                        .takeUnless { it == Long.MIN_VALUE },
                lastOutcome = sharedPreferences.getString(KEY_CHECKPOINT_LAST_OUTCOME, null),
                attemptId = sharedPreferences.getString(KEY_CHECKPOINT_ATTEMPT_ID, null),
            )
        }

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
                LocationMode.defaultForFlavor(org.oxycblt.auxio.BuildConfig.TOPWAY_COMPAT_FLAVOR)
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
            val rawLocations =
                unlikelyToBeNull(
                        sharedPreferences.getString(getString(R.string.set_key_music_locations), "")
                    )
                    .splitEscaped { it == ';' }
            val deduplicatedLocations =
                org.oxycblt.auxio.music.locations.MusicSourcePathNormalizer.deduplicateSources(
                    rawLocations,
                    fileOnly = locationMode == LocationMode.DIRECT_FS,
                )
            val locations =
                deduplicatedLocations.toOpenedLocations(
                    fileOnly = locationMode == LocationMode.DIRECT_FS
                )
            val excludedLocations =
                unlikelyToBeNull(
                        sharedPreferences.getString(
                            getString(R.string.set_key_excluded_locations),
                            "",
                        )
                    )
                    .splitEscaped { it == ';' }
                    .toUnopenedLocations(fileOnly = locationMode == LocationMode.DIRECT_FS)
            val withHidden =
                sharedPreferences.getBoolean(getString(R.string.set_key_with_hidden), false)
            val multithread =
                sharedPreferences.getBoolean(getString(R.string.set_key_saf_multithread), true)
            return SAF.Query(
                source = locations,
                exclude = excludedLocations,
                withHidden = withHidden,
                multithread = multithread,
            )
        }
        set(value) {
            sharedPreferences.edit {
                putString(getString(R.string.set_key_music_locations), value.source.stringify())
                putString(getString(R.string.set_key_excluded_locations), value.exclude.stringify())
                putBoolean(getString(R.string.set_key_with_hidden), value.withHidden)
                putBoolean(context.getString(R.string.set_key_saf_multithread), value.multithread)
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
                    .splitEscaped { it == ';' }
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

    @Synchronized
    override fun applySourceConfiguration(
        mode: LocationMode,
        safQuery: SAF.Query,
        mediaStoreQuery: MediaStore.Query,
    ): Boolean {
        val changed =
            locationMode != mode ||
                this.safQuery != safQuery ||
                this.mediaStoreQuery != mediaStoreQuery
        if (!changed) return false

        val nextGeneration = sourceConfigurationGeneration + 1L
        val configuredKeys =
            if (mode == LocationMode.MEDIA_STORE) {
                emptySet()
            } else {
                safQuery.source.mapTo(linkedSetOf()) { SourceIdentity.forLocation(it) }
            }
        sharedPreferences.edit(commit = true) {
            putInt(getString(R.string.set_key_locations_mode), mode.intCode)
            putString(getString(R.string.set_key_music_locations), safQuery.source.stringify())
            putString(getString(R.string.set_key_excluded_locations), safQuery.exclude.stringify())
            putBoolean(getString(R.string.set_key_with_hidden), safQuery.withHidden)
            putBoolean(getString(R.string.set_key_saf_multithread), safQuery.multithread)

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
            putBoolean(KEY_PENDING_INITIAL_SCAN, true)
            putLong(KEY_SOURCE_CONFIGURATION_GENERATION, nextGeneration)
            putString(KEY_CHECKPOINT_STATE, SourceConfigurationCheckpoint.State.PENDING.name)
            putStringSet(
                KEY_CHECKPOINT_UNRESOLVED,
                sourceConfigurationCheckpoint
                    ?.unresolvedSourceKeys
                    ?.intersect(configuredKeys)
                    .orEmpty(),
            )
            remove(KEY_CHECKPOINT_LAST_ATTEMPT)
            remove(KEY_CHECKPOINT_LAST_OUTCOME)
        }
        L.i("Persisted source configuration generation $nextGeneration [mode=$mode]")
        return true
    }

    @Synchronized
    override fun claimPendingConfiguration(): SourceConfigurationCheckpoint? {
        var checkpoint = sourceConfigurationCheckpoint ?: return null

        if (checkpoint.state == SourceConfigurationCheckpoint.State.RUNNING) {
            // A stale RUNNING attempt must first be terminally recorded as interrupted before a new
            // attempt is created.
            markAttemptInterrupted(
                checkpoint.generation,
                checkpoint.attemptId ?: "",
                "ProcessInterrupted",
            )
            checkpoint = sourceConfigurationCheckpoint ?: return null
        }

        if (
            checkpoint.state != SourceConfigurationCheckpoint.State.PENDING &&
                checkpoint.state != SourceConfigurationCheckpoint.State.FAILED_RETRYABLE &&
                checkpoint.state != SourceConfigurationCheckpoint.State.INTERRUPTED
        ) {
            return null
        }
        val attemptId = java.util.UUID.randomUUID().toString()
        val claimed =
            checkpoint.copy(
                state = SourceConfigurationCheckpoint.State.RUNNING,
                lastAttemptAtMs = System.currentTimeMillis(),
                attemptId = attemptId,
            )
        sharedPreferences.edit(commit = true) {
            putBoolean(KEY_PENDING_INITIAL_SCAN, true)
            putString(KEY_CHECKPOINT_STATE, claimed.state.name)
            putLong(KEY_CHECKPOINT_LAST_ATTEMPT, requireNotNull(claimed.lastAttemptAtMs))
            putString(KEY_CHECKPOINT_ATTEMPT_ID, attemptId)
        }
        return claimed
    }

    @Synchronized
    override fun acknowledgeSourceConfiguration(
        generation: Long,
        attemptId: String,
        unresolvedSourceKeys: Set<String>,
        outcome: String,
    ) {
        if (generation != sourceConfigurationGeneration) return
        val currentCheckpoint = sourceConfigurationCheckpoint
        if (currentCheckpoint?.attemptId != attemptId) return
        if (currentCheckpoint.state != SourceConfigurationCheckpoint.State.RUNNING) return
        val state =
            if (unresolvedSourceKeys.isEmpty()) {
                SourceConfigurationCheckpoint.State.COMMITTED
            } else {
                SourceConfigurationCheckpoint.State.PARTIALLY_COMMITTED
            }
        sharedPreferences.edit(commit = true) {
            putBoolean(KEY_PENDING_INITIAL_SCAN, false)
            putString(KEY_CHECKPOINT_STATE, state.name)
            putStringSet(KEY_CHECKPOINT_UNRESOLVED, unresolvedSourceKeys)
            putString(KEY_CHECKPOINT_LAST_OUTCOME, outcome)
        }
    }

    @Synchronized
    override fun failSourceConfigurationAttempt(
        generation: Long,
        attemptId: String,
        retryable: Boolean,
        outcome: String,
    ) {
        if (generation != sourceConfigurationGeneration) return
        val currentCheckpoint = sourceConfigurationCheckpoint
        if (currentCheckpoint?.attemptId != attemptId) return
        if (currentCheckpoint.state != SourceConfigurationCheckpoint.State.RUNNING) return

        val state =
            if (retryable) {
                SourceConfigurationCheckpoint.State.FAILED_RETRYABLE
            } else {
                SourceConfigurationCheckpoint.State.FAILED_FINAL
            }
        sharedPreferences.edit(commit = true) {
            putString(KEY_CHECKPOINT_STATE, state.name)
            putString(KEY_CHECKPOINT_LAST_OUTCOME, outcome)
        }
    }

    @Synchronized
    override fun markAttemptInterrupted(generation: Long, attemptId: String, outcome: String) {
        if (generation != sourceConfigurationGeneration) return
        val currentCheckpoint = sourceConfigurationCheckpoint
        if (currentCheckpoint?.attemptId != attemptId) return
        if (currentCheckpoint.state != SourceConfigurationCheckpoint.State.RUNNING) return

        sharedPreferences.edit(commit = true) {
            putString(KEY_CHECKPOINT_STATE, SourceConfigurationCheckpoint.State.INTERRUPTED.name)
            putString(KEY_CHECKPOINT_LAST_OUTCOME, outcome)
        }
    }

    @Synchronized
    override fun markSourcesUnresolved(sourceKeys: Set<String>, outcome: String) {
        if (sourceKeys.isEmpty()) return
        val checkpoint = sourceConfigurationCheckpoint ?: return
        sharedPreferences.edit(commit = true) {
            putBoolean(KEY_PENDING_INITIAL_SCAN, false)
            putString(
                KEY_CHECKPOINT_STATE,
                SourceConfigurationCheckpoint.State.PARTIALLY_COMMITTED.name,
            )
            putStringSet(KEY_CHECKPOINT_UNRESOLVED, checkpoint.unresolvedSourceKeys + sourceKeys)
            putString(KEY_CHECKPOINT_LAST_OUTCOME, outcome)
        }
    }

    override fun forceLocationUpdate() {
        markInitialScanPending()
        listeners.forEach { it.onMusicLocationsChanged() }
    }

    @Synchronized
    private fun markInitialScanPending() {
        val nextGeneration = sourceConfigurationGeneration + 1L
        sharedPreferences.edit(commit = true) {
            putBoolean(getString(R.string.set_key_library_last_scan_failed), false)
            putBoolean(KEY_PENDING_INITIAL_SCAN, true)
            putLong(KEY_SOURCE_CONFIGURATION_GENERATION, nextGeneration)
            putString(KEY_CHECKPOINT_STATE, SourceConfigurationCheckpoint.State.PENDING.name)
            remove(KEY_CHECKPOINT_LAST_ATTEMPT)
            remove(KEY_CHECKPOINT_LAST_OUTCOME)
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
        joinToString(separator = ";") { it.uri.toString().replace(";", "\\;") }

    private fun rawConfiguredSourceCount(fileOnly: Boolean): Int {
        val rawLocations =
            unlikelyToBeNull(
                    sharedPreferences.getString(getString(R.string.set_key_music_locations), "")
                )
                .splitEscaped { it == ';' }
        return org.oxycblt.auxio.music.locations.MusicSourcePathNormalizer.deduplicateSources(
                rawLocations,
                fileOnly,
            )
            .size
    }

    private fun List<String>.toOpenedLocations(fileOnly: Boolean): List<Location.Opened> =
        mapNotNull {
            Location.Unopened.from(context, it.toUri()).open(context)
        }

    private fun List<String>.toUnopenedLocations(fileOnly: Boolean): List<Location.Unopened> =
        mapNotNull {
                org.oxycblt.auxio.music.locations.MusicSourcePathNormalizer
                    .normalizePersistedLocation(it, fileOnly)
            }
            .mapNotNull { Location.Unopened.from(context, it.toUri()) }

    private fun normalizePersistedLocation(value: String, fileOnly: Boolean): String? =
        MusicSourcePathNormalizer.normalizePersistedLocation(value, fileOnly)

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
        const val KEY_TS18_SYSTEM_SOURCE_FILTER = "auxio_ts18_system_source_filter"
        const val KEY_PENDING_INITIAL_SCAN = "auxio_pending_initial_music_scan"
        const val KEY_SOURCE_CONFIGURATION_GENERATION = "auxio_source_configuration_generation"
        const val KEY_CHECKPOINT_STATE = "auxio_source_checkpoint_state"
        const val KEY_CHECKPOINT_UNRESOLVED = "auxio_source_checkpoint_unresolved"
        const val KEY_CHECKPOINT_LAST_ATTEMPT = "auxio_source_checkpoint_last_attempt"
        const val KEY_CHECKPOINT_LAST_OUTCOME = "auxio_source_checkpoint_last_outcome"
        const val KEY_CHECKPOINT_ATTEMPT_ID = "auxio_source_checkpoint_attempt_id"
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
