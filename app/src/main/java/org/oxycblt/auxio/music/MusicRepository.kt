/*
 * Copyright (c) 2023 Auxio Project
 * MusicRepository.kt is part of Auxio.
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
import android.os.SystemClock
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.diagnostics.DiagnosticJournal
import org.oxycblt.auxio.headunit.root.RootStateHolder
import org.oxycblt.auxio.headunit.topway.TopwaySourcePolicy
import org.oxycblt.auxio.image.covers.SettingCovers
import org.oxycblt.auxio.music.MusicRepository.IndexingWorker
import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.auxio.music.shim.WriteOnlyMutableCache
import org.oxycblt.auxio.util.PerfTimer
import org.oxycblt.musikr.Config
import org.oxycblt.musikr.IndexingPhase
import org.oxycblt.musikr.IndexingProgress
import org.oxycblt.musikr.Interpretation
import org.oxycblt.musikr.Library
import org.oxycblt.musikr.Music
import org.oxycblt.musikr.Musikr
import org.oxycblt.musikr.MutableLibrary
import org.oxycblt.musikr.Playlist
import org.oxycblt.musikr.Song
import org.oxycblt.musikr.SourceScanFailureException
import org.oxycblt.musikr.Storage
import org.oxycblt.musikr.cache.IncrementalCache
import org.oxycblt.musikr.cache.IncrementalScanPlan
import org.oxycblt.musikr.cache.MutableCache
import org.oxycblt.musikr.fs.CanonicalSourcePolicy
import org.oxycblt.musikr.fs.FS
import org.oxycblt.musikr.fs.FSUpdate
import org.oxycblt.musikr.fs.direct.DirectFS
import org.oxycblt.musikr.fs.direct.DirectFsWorkProgress
import org.oxycblt.musikr.fs.mediastore.MediaStore
import org.oxycblt.musikr.fs.saf.SAF
import org.oxycblt.musikr.library.MetadataProfile
import org.oxycblt.musikr.playlist.db.StoredPlaylists
import org.oxycblt.musikr.tag.interpret.Naming
import org.oxycblt.musikr.tag.interpret.Separators
import timber.log.Timber as L

/**
 * Primary manager of music information and loading.
 *
 * Music information is loaded in-memory by this repository using an [IndexingWorker]. Changes in
 * music (loading) can be reacted to with [UpdateListener] and [IndexingListener].
 *
 * @author Alexander Capehart (OxygenCobalt)
 *
 * TODO: Switch listeners to set when you can confirm there are no order-dependent listener
 *   configurations
 */
interface MusicRepository {
    /** The current library */
    val library: Library?

    /** The current state of music loading. Null if no load has occurred yet. */
    val indexingState: IndexingState?

    /** UI-visible startup/readiness capability used to avoid alarming launch empty states. */
    val startupReadinessState: StartupReadinessState

    /** Recoverable startup library/source condition independent of readiness capability. */
    val startupLibraryStatus: StartupLibraryStatus

    /** Current state of optional generated-playlist publication. */
    val generatedPlaylistStatus: StateFlow<GeneratedPlaylistStatus>
        get() = MutableStateFlow(GeneratedPlaylistStatus.OFF)

    /** Structured result of the latest source attempt, if any. */
    val lastSourceScanOutcome: SourceScanOutcome?
        get() = null

    /**
     * Add an [UpdateListener] to receive updates from this instance.
     *
     * @param listener The [UpdateListener] to add.
     */
    fun addUpdateListener(listener: UpdateListener)

    /**
     * Remove an [UpdateListener] such that it does not receive any further updates from this
     * instance.
     *
     * @param listener The [UpdateListener] to remove.
     */
    fun removeUpdateListener(listener: UpdateListener)

    /**
     * Add an [IndexingListener] to receive updates from this instance.
     *
     * @param listener The [UpdateListener] to add.
     */
    fun addIndexingListener(listener: IndexingListener)

    /**
     * Remove an [IndexingListener] such that it does not receive any further updates from this
     * instance.
     *
     * @param listener The [IndexingListener] to remove.
     */
    fun removeIndexingListener(listener: IndexingListener)

    fun addStartupReadinessListener(listener: StartupReadinessListener)

    fun removeStartupReadinessListener(listener: StartupReadinessListener)

    /**
     * Synchronously returns the [Music] item with the given [Music.UID].
     *
     * @param uid The [Music.UID] to find.
     * @return The [Music] item with the given [Music.UID], or null if it does not exist.
     */
    fun find(uid: Music.UID): Music?

    /**
     * Create a new playlist in the music library.
     *
     * @param name The name of the playlist to create.
     * @param songs The songs to add to the playlist.
     */
    suspend fun createPlaylist(name: String, songs: List<Song>)

    /**
     * Rename the given [Playlist] to the new name.
     *
     * @param playlist The [Playlist] to rename.
     * @param name The new name of the playlist.
     */
    suspend fun renamePlaylist(playlist: Playlist, name: String)

    /**
     * Add songs to the given [Playlist].
     *
     * @param songs The [Song]s to add.
     * @param playlist The [Playlist] to mutate.
     */
    suspend fun addToPlaylist(songs: List<Song>, playlist: Playlist)

    /**
     * Replace the songs in the given [Playlist].
     *
     * @param playlist The [Playlist] to mutate.
     * @param songs The replacement [Song] list.
     */
    suspend fun rewritePlaylist(playlist: Playlist, songs: List<Song>)

    /**
     * Delete the given [Playlist] from the music library.
     *
     * @param playlist The [Playlist] to delete.
     */
    suspend fun deletePlaylist(playlist: Playlist)

    /**
     * Register an [IndexingWorker] to this instance. Only one worker can be registered at a time.
     *
     * @param worker The [IndexingWorker] to register.
     */
    fun registerWorker(worker: IndexingWorker)

    /**
     * Unregister an [IndexingWorker] from this instance.
     *
     * @param worker The [IndexingWorker] to unregister.
     */
    fun unregisterWorker(worker: IndexingWorker)

    /**
     * Request that the library be indexed. This will trigger a call to
     * [IndexingWorker.requestIndex] on the registered worker.
     *
     * @param withCache Whether to use the file-system cache for improved loading times.
     */
    fun requestIndex(withCache: Boolean)

    /** Request a scan with an explicit metadata-work profile. */
    fun requestIndex(withCache: Boolean, metadataProfile: MetadataProfile) {
        requestIndex(withCache)
    }

    /** Request indexing with explicit authority, generation, and source scope. */
    fun requestIndex(request: IndexRequest) {
        if (request.metadataProfile != null) {
            requestIndex(request.withCache, request.metadataProfile)
        } else {
            requestIndex(request.withCache)
        }
    }

    /** Persist a source invalidation without performing a provider query. */
    suspend fun invalidateSource(sourceKey: String? = null) = Unit

    /** Preserve the library while exposing removable roots that disappeared. */
    fun markSourcesTemporarilyUnavailable(sourceKeys: Set<String>) = Unit

    /** Retry the current durable source generation after startup-critical work. */
    fun retrySourceConfiguration() = Unit

    /** Cancel active source/indexing work without clearing a committed library. */
    fun cancelIndexing() = Unit

    /** Publish whether a newer request is waiting behind the active source generation. */
    fun setPendingIndexReplacement(pending: Boolean) = Unit

    /** Mark a cancellation as an atomic handoff to a replacement that will start immediately. */
    fun prepareIndexingReplacementHandoff() = Unit

    /** Update the elapsed/no-progress watchdog state without restarting the pipeline. */
    fun updateIndexingWatchdog(decision: IndexingWatchdogDecision) = Unit

    /** Record service teardown before cancelling the owning coroutine. */
    fun prepareIndexingInterruption(
        outcome: IndexingTerminalOutcome,
        request: IndexRequest? = null,
    ) = Unit

    /** Rebuild only optional generated playlists from the current base library. */
    suspend fun refreshGeneratedPlaylists(force: Boolean = false) = Unit

    /**
     * Start the music system. This should be called by the application at startup.
     *
     * @param worker The [IndexingWorker] to use for initial loading.
     */
    suspend fun startup(worker: IndexingWorker)

    /**
     * Re-index the music library. This will trigger a call to [IndexingWorker.requestIndex] on the
     * registered worker.
     *
     * @param worker The [IndexingWorker] requesting the index.
     * @param withCache Whether to use the file-system cache for improved loading times.
     */
    suspend fun index(worker: IndexingWorker, withCache: Boolean)

    suspend fun index(
        worker: IndexingWorker,
        withCache: Boolean,
        metadataProfile: MetadataProfile,
    ) {
        index(worker, withCache)
    }

    suspend fun index(worker: IndexingWorker, request: IndexRequest) {
        if (request.metadataProfile != null) {
            index(worker, request.withCache, request.metadataProfile)
        } else {
            index(worker, request.withCache)
        }
    }

    /** Data regarding the current changes in the music library. */
    data class Changes(
        val deviceLibrary: Boolean,
        val userLibrary: Boolean,
        val deviceGeneration: Long,
        val userGeneration: Long,
    )

    /** Listener for changes in the music library. */
    interface UpdateListener {
        /**
         * Called when the music library has changed.
         *
         * @param changes Information regarding what parts of the library have changed.
         */
        fun onMusicChanges(changes: Changes)
    }

    /** Listener for changes in the indexing state. */
    interface IndexingListener {
        /** Called when the [indexingState] has changed. */
        fun onIndexingStateChanged()
    }

    interface StartupReadinessListener {
        fun onStartupReadinessStateChanged()
    }

    /** A worker that performs library indexing and tag extraction. */
    interface IndexingWorker {
        /** Snapshot whether playback is currently active for scan resource policy decisions. */
        fun playbackActiveSnapshot(): Boolean = false

        /**
         * Request that the library be indexed.
         *
         * @param withCache Whether to use the file-system cache for improved loading times.
         */
        fun requestIndex(withCache: Boolean)

        fun requestIndex(withCache: Boolean, metadataProfile: MetadataProfile) {
            requestIndex(withCache)
        }

        fun requestIndex(request: IndexRequest) {
            if (request.metadataProfile != null) {
                requestIndex(request.withCache, request.metadataProfile)
            } else {
                requestIndex(request.withCache)
            }
        }

        /** Cancel active work at the user's request. */
        fun cancelIndexing() = Unit
    }
}

/** The current state of music loading. */
sealed interface IndexingState {
    /**
     * Currently indexing and extracting tags from device music.
     *
     * @param progress The current progress of the music pipeline.
     */
    data class Indexing(
        val progress: IndexingProgress,
        val sessionId: Long = 0L,
        val request: IndexRequest? = null,
        val locationMode: LocationMode? = null,
        val sourceLabels: List<String> = emptyList(),
        val attemptedSourceKeys: Set<String> = emptySet(),
        val startedAtElapsedMs: Long = 0L,
        val lastProgressAtElapsedMs: Long = startedAtElapsedMs,
        val pendingReplacement: Boolean = false,
        val watchdogState: IndexingWatchdogState = IndexingWatchdogState.HEALTHY,
        val watchdogDetail: String = "",
        val noProgressDurationMs: Long = 0L,
        val noProgressDeadlineMs: Long = 0L,
        val sourceScope: IndexingSourceScope = IndexingSourceScope.UNKNOWN,
        val firstFileEmitted: Boolean = false,
        val directFsDirectoriesVisited: Int? = null,
        val directFsEntriesInspected: Int? = null,
        val directFsFilesEmitted: Int? = null,
        val queuedDirectFsWork: Int? = null,
        val activeDirectFsEnumerators: Int? = null,
        val nonAuthoritativeWorkDeferred: Boolean = false,
    ) : IndexingState

    /**
     * Music loading has completed.
     *
     * @param error If an error occurred during loading, it will be contained here.
     */
    data class Completed(
        val error: Exception?,
        val outcome: IndexingTerminalOutcome =
            if (error == null) IndexingTerminalOutcome.SUCCESS else IndexingTerminalOutcome.FAILED,
    ) : IndexingState
}

class MusicRepositoryImpl
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val cache: MutableCache,
    private val storedPlaylists: StoredPlaylists,
    private val settingCovers: SettingCovers,
    private val musicSettings: MusicSettings,
    private val rootGate: RootStateHolder,
    private val startupReadinessController: StartupReadinessController,
    private val optionalWorkGate: StartupOptionalWorkGate,
    private val generatedPlaylistCoordinator: GeneratedPlaylistCoordinator,
    private val diagnosticJournal: DiagnosticJournal,
) : MusicRepository {
    private val updateListeners = CopyOnWriteArrayList<MusicRepository.UpdateListener>()
    private val indexingListeners = CopyOnWriteArrayList<MusicRepository.IndexingListener>()
    private val readinessAdapter =
        StartupReadinessController.Listener {
            for (listener in startupReadinessListeners) {
                listener.onStartupReadinessStateChanged()
            }
        }
    private val startupReadinessListeners =
        CopyOnWriteArrayList<MusicRepository.StartupReadinessListener>()
    @Volatile private var indexingWorker: IndexingWorker? = null
    private val pendingIndexRequests = RepositoryIndexRequestQueue()
    private val indexingSessionIds = AtomicLong(0L)
    private val indexingSessionGate = IndexingSessionGate()
    private val deviceLibraryGeneration = AtomicLong(0L)
    private val userLibraryGeneration = AtomicLong(0L)
    private val compatibilityHydrationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    @Volatile private var compatibilityHydrationJob: Job? = null

    @Volatile override var library: MutableLibrary? = null
    @Volatile private var previousCompletedState: IndexingState.Completed? = null
    @Volatile private var currentIndexingState: IndexingState? = null
    @Volatile private var preparedInterruptionOutcome: IndexingTerminalOutcome? = null
    @Volatile private var preparedReplacementHandoff = false
    @Volatile private var lastProgressLogAtElapsedMs = 0L
    @Volatile private var lastHeartbeatPersistedAtElapsedMs = 0L
    @Volatile private var lastDirectFsProgressLogAtElapsedMs = 0L
    @Volatile private var lastLoggedProgressPhase: IndexingPhase? = null
    @Volatile
    override var lastSourceScanOutcome: SourceScanOutcome? = null
        private set

    override val indexingState: IndexingState?
        get() = currentIndexingState ?: previousCompletedState

    override val startupReadinessState: StartupReadinessState
        get() = startupReadinessController.capability

    override val startupLibraryStatus: StartupLibraryStatus
        get() = startupReadinessController.state.libraryStatus

    override val generatedPlaylistStatus: StateFlow<GeneratedPlaylistStatus>
        get() = generatedPlaylistCoordinator.status

    init {
        startupReadinessController.addListener(readinessAdapter)
    }

    override fun addUpdateListener(listener: MusicRepository.UpdateListener) {
        L.d("Adding $listener to update listeners")
        updateListeners.add(listener)
        listener.onMusicChanges(
            MusicRepository.Changes(
                deviceLibrary = true,
                userLibrary = true,
                deviceGeneration = deviceLibraryGeneration.get(),
                userGeneration = userLibraryGeneration.get(),
            )
        )
    }

    override fun removeUpdateListener(listener: MusicRepository.UpdateListener) {
        L.d("Removing $listener to update listeners")
        if (!updateListeners.remove(listener)) {
            L.w("Update listener $listener was not added prior, cannot remove")
        }
    }

    override fun addIndexingListener(listener: MusicRepository.IndexingListener) {
        L.d("Adding $listener to indexing listeners")
        indexingListeners.add(listener)
        listener.onIndexingStateChanged()
    }

    override fun removeIndexingListener(listener: MusicRepository.IndexingListener) {
        L.d("Removing $listener from indexing listeners")
        if (!indexingListeners.remove(listener)) {
            L.w("Indexing listener $listener was not added prior, cannot remove")
        }
    }

    override fun addStartupReadinessListener(listener: MusicRepository.StartupReadinessListener) {
        startupReadinessListeners.add(listener)
        listener.onStartupReadinessStateChanged()
    }

    override fun removeStartupReadinessListener(
        listener: MusicRepository.StartupReadinessListener
    ) {
        val removed = startupReadinessListeners.remove(listener)
        if (!removed) {
            L.w("Startup readiness listener $listener was not added prior, cannot remove")
        }
    }

    override fun registerWorker(worker: IndexingWorker) {
        val pending =
            synchronized(this) {
                if (indexingWorker != null) {
                    L.w("Worker is already registered")
                    return
                }
                L.d("Registering worker $worker")
                indexingWorker = worker
                pendingIndexRequests.drain()
            }
        pending?.also {
            L.i("Dispatching scan request queued before worker attachment [request=$it]")
            worker.requestIndex(it)
        }
    }

    override fun unregisterWorker(worker: IndexingWorker) {
        val dispatchCompletion =
            synchronized(this) {
                if (indexingWorker !== worker) {
                    L.w("Given worker did not match current worker")
                    return
                }
                L.d("Unregistering worker $worker")
                indexingWorker = null
                val activeState = currentIndexingState as? IndexingState.Indexing
                if (activeState != null) {
                    val outcome =
                        preparedInterruptionOutcome ?: IndexingTerminalOutcome.SERVICE_STOPPED
                    indexingSessionGate.complete(activeState.sessionId)
                    previousCompletedState =
                        IndexingState.Completed(IndexingInterruptedException(outcome), outcome)
                    currentIndexingState = null
                    preparedInterruptionOutcome = null
                    preparedReplacementHandoff = false
                    true
                } else {
                    false
                }
            }
        if (dispatchCompletion) {
            dispatchIndexingState()
        }
    }

    @Synchronized
    override fun find(uid: Music.UID) =
        (library?.run {
            findSong(uid)
                ?: findAlbum(uid)
                ?: findArtist(uid)
                ?: findGenre(uid)
                ?: findPlaylist(uid)
        })

    override suspend fun createPlaylist(name: String, songs: List<Song>) {
        val library = synchronized(this) { library ?: return }
        L.d("Creating playlist $name with ${songs.size} songs")
        val newLibrary = library.createPlaylist(name, songs)
        synchronized(this) { this.library = newLibrary }
        withContext(Dispatchers.Main) { dispatchLibraryChange(device = false, user = true) }
    }

    override suspend fun renamePlaylist(playlist: Playlist, name: String) {
        val library = synchronized(this) { library ?: return }
        L.d("Renaming $playlist to $name")
        val newLibrary = library.renamePlaylist(playlist, name)
        synchronized(this) { this.library = newLibrary }
        withContext(Dispatchers.Main) { dispatchLibraryChange(device = false, user = true) }
    }

    override suspend fun addToPlaylist(songs: List<Song>, playlist: Playlist) {
        val library = synchronized(this) { library ?: return }
        L.d("Adding ${songs.size} song(s) to $playlist")
        val newLibrary = library.addToPlaylist(playlist, songs)
        synchronized(this) { this.library = newLibrary }
        withContext(Dispatchers.Main) { dispatchLibraryChange(device = false, user = true) }
    }

    override suspend fun rewritePlaylist(playlist: Playlist, songs: List<Song>) {
        val library = synchronized(this) { library ?: return }
        L.d("Rewriting $playlist with ${songs.size} song(s)")
        val newLibrary = library.rewritePlaylist(playlist, songs)
        synchronized(this) { this.library = newLibrary }
        withContext(Dispatchers.Main) { dispatchLibraryChange(device = false, user = true) }
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        val library = synchronized(this) { library ?: return }
        L.d("Deleting $playlist")
        val newLibrary = library.deletePlaylist(playlist)
        synchronized(this) { this.library = newLibrary }
        withContext(Dispatchers.Main) { dispatchLibraryChange(device = false, user = true) }
    }

    override fun requestIndex(withCache: Boolean) {
        requestIndex(IndexRequest(reason = IndexReason.USER_REFRESH, withCache = withCache))
    }

    override fun requestIndex(withCache: Boolean, metadataProfile: MetadataProfile) {
        requestIndex(
            IndexRequest(
                reason =
                    if (metadataProfile == MetadataProfile.FULL) {
                        IndexReason.METADATA_ENRICHMENT
                    } else {
                        IndexReason.USER_REFRESH
                    },
                withCache = withCache,
                metadataProfile = metadataProfile,
            )
        )
    }

    override fun requestIndex(request: IndexRequest) {
        dispatchOrQueue(request)
    }

    private fun dispatchOrQueue(request: IndexRequest) {
        val worker =
            synchronized(this) {
                indexingWorker
                    ?: run {
                        pendingIndexRequests.offer(request)
                        null
                    }
            }
        if (worker != null) {
            worker.requestIndex(request)
        } else {
            L.i("Queued scan request until worker attachment [request=$request]")
        }
    }

    override suspend fun invalidateSource(sourceKey: String?) {
        (cache as? IncrementalCache)?.invalidateSource(sourceKey)
    }

    override fun markSourcesTemporarilyUnavailable(sourceKeys: Set<String>) {
        if (sourceKeys.isEmpty()) return
        lastSourceScanOutcome = SourceScanOutcome.TemporarilyUnavailable(sourceKeys)
        musicSettings.markSourcesUnresolved(sourceKeys, "TemporarilyUnavailable")
        emitStartupLibraryStatus(StartupLibraryStatus.SourceUnavailable)
    }

    override fun retrySourceConfiguration() {
        compatibilityHydrationScope.launch {
            optionalWorkGate.awaitOpen()
            val configuredSourceKeys =
                musicSettings.configuredSourceSpecs.mapTo(linkedSetOf()) { it.sourceKey }
            val request =
                IndexRequestPolicy.sourceRetryRequest(
                    checkpoint = musicSettings.sourceConfigurationCheckpoint,
                    currentGeneration = musicSettings.sourceConfigurationGeneration,
                    configuredSourceKeys = configuredSourceKeys,
                    hasRevision = musicSettings.revision != null,
                    allowUnscopedSources = musicSettings.locationMode == LocationMode.MEDIA_STORE,
                )
            if (request == null) {
                val checkpoint = musicSettings.sourceConfigurationCheckpoint
                L.i(
                    "Ignoring source retry without a retryable source " +
                        "[generation=${checkpoint?.generation} state=${checkpoint?.state}]"
                )
                return@launch
            }
            if (request.reason == IndexReason.USER_REFRESH) {
                L.i("Retrying failed non-checkpoint refresh through normal source authority")
                requestIndex(request)
                return@launch
            }
            for (sourceKey in request.sourceKeys.orEmpty()) invalidateSource(sourceKey)
            requestIndex(request)
        }
    }

    override fun cancelIndexing() {
        synchronized(this) { indexingWorker }?.cancelIndexing()
    }

    override fun setPendingIndexReplacement(pending: Boolean) {
        val changed =
            synchronized(this) {
                val current =
                    currentIndexingState as? IndexingState.Indexing ?: return@synchronized null
                if (current.pendingReplacement == pending) return@synchronized null
                currentIndexingState = current.copy(pendingReplacement = pending)
                current
            }
        if (changed != null) {
            diagnosticJournal.log(
                DiagnosticJournal.CAT_INDEXING,
                "Replacement",
                "session=${changed.sessionId} generation=" +
                    "${changed.request?.configurationGeneration} attempt=" +
                    "${changed.request?.attemptId} pending=$pending",
                result = if (pending) "PENDING" else "CLEARED",
            )
            dispatchIndexingState()
        }
    }

    override fun prepareIndexingReplacementHandoff() {
        synchronized(this) {
            if (currentIndexingState !is IndexingState.Indexing) return
            preparedReplacementHandoff = true
        }
        L.i("Prepared direct indexing-generation handoff")
    }

    override fun updateIndexingWatchdog(decision: IndexingWatchdogDecision) {
        val update =
            synchronized(this) {
                val active =
                    currentIndexingState as? IndexingState.Indexing ?: return@synchronized null
                if (
                    active.watchdogState == decision.state &&
                        active.noProgressDurationMs == decision.noProgressMs &&
                        active.noProgressDeadlineMs == decision.noProgressDeadlineMs &&
                        active.watchdogDetail == decision.detail
                ) {
                    return@synchronized null
                }
                currentIndexingState =
                    active.copy(
                        watchdogState = decision.state,
                        watchdogDetail = decision.detail,
                        noProgressDurationMs = decision.noProgressMs,
                        noProgressDeadlineMs = decision.noProgressDeadlineMs,
                    )
                active to (active.watchdogState != decision.state)
            } ?: return
        val (current, stateChanged) = update
        if (stateChanged) {
            L.w(
                "Indexing watchdog changed [state=${decision.state} " +
                    "phase=${current.progress.phase} item=${current.progress.currentItem} " +
                    "detail=${decision.detail}]"
            )
            diagnosticJournal.log(
                DiagnosticJournal.CAT_INDEXING,
                "Watchdog",
                "session=${current.sessionId} generation=" +
                    "${current.request?.configurationGeneration} attempt=" +
                    "${current.request?.attemptId} ${decision.detail}",
                result = decision.state.name,
            )
        }
        dispatchIndexingState()
    }

    override fun prepareIndexingInterruption(
        outcome: IndexingTerminalOutcome,
        request: IndexRequest?,
    ) {
        val current =
            synchronized(this) {
                preparedInterruptionOutcome = outcome
                if (outcome != IndexingTerminalOutcome.SUPERSEDED) {
                    preparedReplacementHandoff = false
                }
                currentIndexingState as? IndexingState.Indexing
            }
        if (current != null) {
            completeAttemptForInterruption(current, outcome)
        } else if (request != null) {
            val durableAccepted = completeSourceAttemptForInterruption(request, outcome, null)
            // Record when: (a) non-authoritative (no checkpoint lease, so durable completion is
            // never applicable), or (b) the durable completion was actually accepted.
            if (IndexRequestPolicy.shouldRecordInterruptionOutcome(request, durableAccepted)) {
                recordSourceScanOutcome(
                    request,
                    sourceOutcomeForInterruption(outcome, request = request),
                )
            }
        } else {
            return
        }
        L.w(
            "Prepared indexing interruption [outcome=$outcome phase=${current?.progress?.phase} " +
                "item=${current?.progress?.currentItem}]"
        )
    }

    override suspend fun startup(worker: IndexingWorker) {
        PerfTimer.traceSuspend("MusicRepository.startup") {
            val start = System.currentTimeMillis()
            L.i("Music system starting...")
            val decision =
                StartupLibraryStartup.run(
                    hasInMemoryLibrary = synchronized(this) { library != null },
                    revisionKnown = musicSettings.revision != null,
                    priorState = musicSettings.libraryState,
                    deferCachedLoad = true,
                    lastScanFailed = { musicSettings.lastScanFailed },
                    isTopwayCompat = BuildConfig.TOPWAY_COMPAT_ENABLED,
                    loadCachedLibrary = { 0 },
                    cachedSongCount = { 0 },
                    emitCachedLibrary = {},
                    emitCachedLoadFailure = {
                        L.w(it, "Cached library load failed during startup")
                    },
                    setLibraryState = { musicSettings.libraryState = it },
                    requestIndex = { withCache ->
                        val pendingCheckpoint = musicSettings.sourceConfigurationCheckpoint
                        if (
                            pendingCheckpoint?.state !=
                                SourceConfigurationCheckpoint.State.RUNNING &&
                                pendingCheckpoint?.canClaim(
                                    SourceScanClaimReason.STARTUP_RECOVERY
                                ) != true
                        ) {
                            worker.requestIndex(
                                IndexRequest(
                                    reason = IndexReason.COMPATIBILITY_RECOVERY,
                                    withCache = withCache,
                                    configurationGeneration = pendingCheckpoint?.generation,
                                )
                            )
                        }
                    },
                    setStartupReadinessState = ::emitStartupReadinessState,
                    setStartupLibraryStatus = ::emitStartupLibraryStatus,
                    sourceConfigured =
                        StartupLibraryPolicy.isMusicSourceConfigured(
                            musicSettings.locationMode,
                            musicSettings.configuredSourceCount,
                        ),
                )
            L.d(
                "Startup policy completed in ${System.currentTimeMillis() - start}ms " +
                    "[state=${decision.libraryState}, scan=${decision.requestScan}, reason=${decision.reason}]"
            )
        }
        try {
            cache.prepareStartupProjections()
            emitStartupReadinessState(StartupReadinessState.FastBrowseReady)
            emitStartupReadinessState(StartupReadinessState.SearchReady)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            L.w(e, "Bounded startup projection seed failed; continuing legacy hydration")
            emitStartupLibraryStatus(StartupLibraryStatus.CacheUnavailable)
        }
        startCompatibilityHydration(worker)
        startCompatibilityBackfill()
    }

    override suspend fun index(worker: IndexingWorker, withCache: Boolean) =
        indexWithProfile(worker, withCache, metadataProfile = null)

    override suspend fun index(
        worker: IndexingWorker,
        withCache: Boolean,
        metadataProfile: MetadataProfile,
    ) = indexWithProfile(worker, withCache, metadataProfile)

    override suspend fun index(worker: IndexingWorker, request: IndexRequest) =
        indexWithProfile(worker, request)

    private suspend fun indexWithProfile(
        worker: IndexingWorker,
        withCache: Boolean,
        metadataProfile: MetadataProfile?,
    ) =
        indexWithProfile(
            worker,
            IndexRequest(
                reason =
                    if (metadataProfile == MetadataProfile.FULL) {
                        IndexReason.METADATA_ENRICHMENT
                    } else {
                        IndexReason.USER_REFRESH
                    },
                withCache = withCache,
                metadataProfile = metadataProfile,
            ),
        )

    private suspend fun indexWithProfile(worker: IndexingWorker, request: IndexRequest) =
        PerfTimer.traceSuspend(
            "MusicRepository.index(reason=${request.reason} cache=${request.withCache} " +
                "profile=${request.metadataProfile} generation=${request.configurationGeneration})"
        ) {
            yield()
            val checkpointAuthority = IndexRequestPolicy.checkpointAuthority(request)
            if (indexingWorker !== worker) {
                L.w("Index requested from unregistered worker; ignoring")
                completeSourceAttemptForInterruption(
                    request,
                    IndexingTerminalOutcome.SERVICE_STOPPED,
                    null,
                )
                return@traceSuspend
            }
            if (IndexRequestPolicy.requiresAttemptClaim(request) && checkpointAuthority == null) {
                L.w(
                    "Authoritative source request has no attempt lease; ignoring [request=$request]"
                )
                return@traceSuspend
            }
            if (
                checkpointAuthority != null &&
                    !musicSettings.ownsSourceConfigurationAttempt(
                        checkpointAuthority.generation,
                        checkpointAuthority.attemptId,
                        checkpointAuthority.owner,
                    )
            ) {
                L.w(
                    "Stale source attempt cannot begin repository indexing " +
                        "[generation=${checkpointAuthority.generation} " +
                        "attempt=${checkpointAuthority.attemptId}]"
                )
                return@traceSuspend
            }

            val sessionId =
                try {
                    beginIndexing(request)
                } catch (e: Exception) {
                    val attemptedKeys = attemptedSourceKeys(request)
                    recordSourceScanOutcome(
                        request,
                        SourceScanOutcome.Failed(
                            retryable = true,
                            failureClass = e.javaClass.name,
                            failureMessage = e.message,
                            unresolvedSourceKeys = attemptedKeys,
                        ),
                    )
                    val completed =
                        completeSourceAttempt(
                            request = request,
                            outcome = SourceScanAttemptOutcome.FAILED_RETRYABLE,
                            unresolvedSourceKeys = attemptedKeys,
                            reason = "Failed to initialise indexing session",
                            failure = e,
                            lastScanFailed = true,
                        )
                    if (
                        !completed &&
                            checkpointAuthority == null &&
                            IndexRequestPolicy.recordsSourceOutcome(request)
                    ) {
                        musicSettings.lastScanFailed = true
                    }
                    L.w(e, "Unable to initialise indexing session")
                    return@traceSuspend
                }
            try {
                val playbackActive = worker.playbackActiveSnapshot()
                val resolvedProfile =
                    DrivingStartupPolicy.metadataProfile(
                        explicit = request.metadataProfile,
                        scanPriority = musicSettings.scanPriority,
                        playbackActive = playbackActive,
                        isTopwayVariant = BuildConfig.TOPWAY_COMPAT_ENABLED,
                    )
                updateNonAuthoritativeWorkDeferred(
                    sessionId,
                    resolvedProfile == MetadataProfile.LEAN && playbackActive,
                )
                val currentRevision = musicSettings.revision
                val newRevision = currentRevision?.takeIf { request.withCache } ?: UUID.randomUUID()
                val workerCount =
                    DefaultIndexingResourcePolicy.resolveWorkerCount(
                        scanPriority = musicSettings.scanPriority,
                        playbackActive = playbackActive,
                        isTopwayVariant = BuildConfig.TOPWAY_COMPAT_ENABLED,
                        availableProcessors = Runtime.getRuntime().availableProcessors(),
                    )
                val originalRequestedSourceKeys = request.sourceKeys
                val requestedSourceKeys =
                    SourceAuthorityScopePolicy.normalizeRequestedSourceKeys(
                        originalRequestedSourceKeys
                    )
                val allConfiguredSourceKeys =
                    musicSettings.configuredSourceSpecs.mapTo(linkedSetOf()) { it.sourceKey }
                var attemptedSourceKeys = requestedSourceKeys ?: allConfiguredSourceKeys
                val rawFs =
                    createFileSystem(
                        sessionId = sessionId,
                        sourceKeys = requestedSourceKeys.takeIf { !request.withCache },
                    )
                val prepared =
                    try {
                        IncrementalIndexPlanner.prepare(
                            fs = rawFs,
                            cache = cache,
                            withCache = request.withCache,
                            profile = resolvedProfile,
                            configurationRevision = sourceConfigurationRevision(),
                            targetSourceKeys = requestedSourceKeys,
                            allowEmptySourceSet =
                                SourceAuthorityScopePolicy.allowExplicitEmptySourceSet(
                                    locationMode = musicSettings.locationMode,
                                    hasCheckpointAuthority = checkpointAuthority != null,
                                    originalRequestedSourceKeys = originalRequestedSourceKeys,
                                    configuredSourceKeys = allConfiguredSourceKeys,
                                ),
                            applyRemovedSources = checkpointAuthority != null,
                            legacyWriteOnly = ::WriteOnlyMutableCache,
                        )
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: SourcePreflightException) {
                        recordSourceScanOutcome(
                            request,
                            SourceScanOutcome.TemporarilyUnavailable(attemptedSourceKeys),
                        )
                        completeSourceAttempt(
                            request = request,
                            outcome = SourceScanAttemptOutcome.TEMPORARILY_UNAVAILABLE,
                            unresolvedSourceKeys = attemptedSourceKeys,
                            reason = "Music-source preflight unavailable",
                            failure = e,
                            lastScanFailed = true,
                        )
                        if (
                            checkpointAuthority == null &&
                                IndexRequestPolicy.recordsSourceOutcome(request)
                        ) {
                            musicSettings.lastScanFailed = true
                        }
                        emitStartupLibraryStatus(StartupLibraryStatus.SourceUnavailable)
                        L.w(
                            e,
                            "Music-source preflight failed; preserving the last readable library",
                        )
                        emitIndexingCompletion(
                            sessionId,
                            e,
                            IndexingTerminalOutcome.SOURCE_UNAVAILABLE,
                        )
                        return@traceSuspend
                    }
                val plan = prepared.plan
                attemptedSourceKeys =
                    SourceAuthorityScopePolicy.effectiveAttemptedSourceKeys(
                        locationMode = musicSettings.locationMode,
                        requestedSourceKeys = requestedSourceKeys,
                        configuredSourceKeys = allConfiguredSourceKeys,
                        plan = plan,
                    )
                synchronized(this) {
                    val active = currentIndexingState as? IndexingState.Indexing
                    if (active?.sessionId == sessionId) {
                        currentIndexingState =
                            active.copy(attemptedSourceKeys = attemptedSourceKeys)
                    }
                }
                L.i(
                    "Resolved scan policy [workers=$workerCount profile=$resolvedProfile " +
                        "reason=${request.reason} generation=${request.configurationGeneration} " +
                        "scan=${plan?.scanSourceKeys} reuse=${plan?.reuseSourceKeys} " +
                        "unavailable=${plan?.unavailableSourceKeys} " +
                        "removed=${plan?.removedSourceKeys} enrichmentOnly=${plan?.enrichmentOnly}]"
                )

                if (
                    plan != null &&
                        !plan.hasWork &&
                        plan.unavailableSourceKeys.isEmpty() &&
                        synchronized(this) { library != null }
                ) {
                    L.i(
                        "All configured sources are unchanged; skipping provider scan and extraction"
                    )
                    if (resolvedProfile == MetadataProfile.FULL) {
                        emitStartupReadinessState(StartupReadinessState.EnrichmentComplete)
                    } else {
                        worker.requestIndex(
                            IndexRequest(
                                reason = IndexReason.METADATA_ENRICHMENT,
                                withCache = true,
                                metadataProfile = MetadataProfile.FULL,
                                configurationGeneration = request.configurationGeneration,
                            )
                        )
                    }
                    val unresolved =
                        musicSettings.sourceConfigurationCheckpoint
                            ?.unresolvedSourceKeys
                            .orEmpty() - attemptedSourceKeys
                    val empty = synchronized(this) { library?.songs?.isEmpty() == true }
                    recordSourceScanOutcome(
                        request,
                        when {
                            unresolved.isNotEmpty() ->
                                SourceScanOutcome.Partial(
                                    committedSourceKeys = attemptedSourceKeys - unresolved,
                                    unresolvedSourceKeys = unresolved,
                                )
                            empty -> SourceScanOutcome.AuthoritativeEmpty(attemptedSourceKeys)
                            else -> SourceScanOutcome.Success(attemptedSourceKeys)
                        },
                    )
                    completeSourceAttempt(
                        request = request,
                        outcome =
                            if (empty) {
                                SourceScanAttemptOutcome.AUTHORITATIVE_EMPTY
                            } else {
                                SourceScanAttemptOutcome.SUCCESS
                            },
                        unresolvedSourceKeys = unresolved,
                        reason = "Configured sources unchanged",
                        lastScanFailed = unresolved.isNotEmpty(),
                    )
                    emitIndexingCompletion(
                        sessionId,
                        error = null,
                        outcome =
                            if (unresolved.isEmpty()) {
                                IndexingTerminalOutcome.SUCCESS
                            } else {
                                IndexingTerminalOutcome.PARTIAL_SUCCESS
                            },
                    )
                    return@traceSuspend
                }

                val config =
                    createConfig(
                        revision = newRevision,
                        cache = prepared.cache,
                        workerCount = workerCount,
                        fs = prepared.fs,
                        metadataProfile = resolvedProfile,
                        scanPlan = plan,
                        sessionId = sessionId,
                        request = request,
                        checkpointAuthority = checkpointAuthority,
                    )

                val locations =
                    when (musicSettings.locationMode) {
                        LocationMode.SAF,
                        LocationMode.DIRECT_FS -> musicSettings.safQuery.source
                        LocationMode.MEDIA_STORE -> emptyList()
                    }
                if (
                    plan == null &&
                        musicSettings.locationMode == LocationMode.SAF &&
                        locations.any { !it.path.volume.isAccessible() }
                ) {
                    L.w("One or more legacy music sources are inaccessible. Preserving cache.")
                    val unavailable = Exception("Music source inaccessible")
                    recordSourceScanOutcome(
                        request,
                        SourceScanOutcome.TemporarilyUnavailable(attemptedSourceKeys),
                    )
                    completeSourceAttempt(
                        request = request,
                        outcome = SourceScanAttemptOutcome.TEMPORARILY_UNAVAILABLE,
                        unresolvedSourceKeys = attemptedSourceKeys,
                        reason = "Legacy SAF source inaccessible",
                        failure = unavailable,
                        lastScanFailed = true,
                    )
                    if (
                        checkpointAuthority == null &&
                            IndexRequestPolicy.recordsSourceOutcome(request)
                    ) {
                        musicSettings.lastScanFailed = true
                    }
                    emitStartupLibraryStatus(StartupLibraryStatus.SourceUnavailable)
                    emitIndexingCompletion(
                        sessionId,
                        unavailable,
                        IndexingTerminalOutcome.SOURCE_UNAVAILABLE,
                    )
                    return@traceSuspend
                }

                val pathKeywords =
                    if (
                        musicSettings.ts18SystemSourceFilter &&
                            musicSettings.locationMode == LocationMode.SAF
                    ) {
                        TopwaySourcePolicy.SYSTEM_SOURCE_PATH_KEYWORDS
                    } else {
                        emptyList()
                    }
                val start = System.currentTimeMillis()
                val result =
                    Musikr.new(
                            context = context,
                            config = config,
                            noisyDirs = TopwaySourcePolicy.NOISY_DIRS,
                            pathKeywords = pathKeywords,
                            rootGate = rootGate,
                        )
                        .run { progress -> emitIndexingProgress(sessionId, request, progress) }
                emitIndexingProgress(
                    sessionId,
                    request,
                    IndexingProgress.Stage(IndexingPhase.FINALISING),
                )
                L.d("Index finished in ${System.currentTimeMillis() - start}ms")
                val currentConfigurationGeneration = musicSettings.sourceConfigurationGeneration
                if (
                    checkpointAuthority == null &&
                        IndexRequestPolicy.isSupersededByNewerConfiguration(
                            request,
                            currentConfigurationGeneration,
                        )
                ) {
                    // Optional lanes hold no checkpoint lease, so this is the only thing stopping a
                    // result computed for an older source configuration from replacing the library
                    // a newer authoritative scan already committed. The newer generation owns the
                    // reported source outcome, so this one is discarded without recording anything.
                    L.w(
                        "Discarding non-authoritative result superseded by a newer source " +
                            "configuration [reason=${request.reason} " +
                            "request=${request.configurationGeneration} " +
                            "current=$currentConfigurationGeneration]"
                    )
                    emitIndexingCompletion(
                        sessionId,
                        error = null,
                        outcome = IndexingTerminalOutcome.SUPERSEDED,
                    )
                    return@traceSuspend
                }
                val scopedFailures =
                    if (requestedSourceKeys == null) {
                        result.failedSources
                    } else {
                        result.failedSources.filterKeys { it in attemptedSourceKeys }
                    }
                val sourceOutcome =
                    SourceScanOutcome.classify(
                        configuredSourceKeys = attemptedSourceKeys,
                        failedSources = scopedFailures,
                        songCount = result.library.songs.size,
                    )
                recordSourceScanOutcome(request, sourceOutcome)

                if (
                    plan == null &&
                        musicSettings.locationMode == LocationMode.SAF &&
                        result.library.songs.isEmpty()
                ) {
                    if (locations.any { !it.path.volume.isAccessible() }) {
                        L.w("Legacy scan became inaccessible. Preserving cache.")
                        val unavailable = Exception("Source became inaccessible during scan")
                        recordSourceScanOutcome(
                            request,
                            SourceScanOutcome.TemporarilyUnavailable(attemptedSourceKeys),
                        )
                        completeSourceAttempt(
                            request = request,
                            outcome = SourceScanAttemptOutcome.TEMPORARILY_UNAVAILABLE,
                            unresolvedSourceKeys = attemptedSourceKeys,
                            reason = "Legacy SAF source became inaccessible",
                            failure = unavailable,
                            lastScanFailed = true,
                        )
                        if (
                            checkpointAuthority == null &&
                                IndexRequestPolicy.recordsSourceOutcome(request)
                        ) {
                            musicSettings.lastScanFailed = true
                        }
                        emitStartupLibraryStatus(StartupLibraryStatus.SourceUnavailable)
                        emitIndexingCompletion(
                            sessionId,
                            unavailable,
                            IndexingTerminalOutcome.SOURCE_UNAVAILABLE,
                        )
                        return@traceSuspend
                    }
                }

                val publishedLibrary =
                    if (scopedFailures.isEmpty() || result.library.songs.isNotEmpty()) {
                        if (scopedFailures.isNotEmpty()) {
                            L.w(
                                "Publishing readable partial library; source warnings: " +
                                    scopedFailures.keys
                            )
                        }
                        result.library
                    } else {
                        when (sourceOutcome) {
                            is SourceScanOutcome.PermissionRequired,
                            is SourceScanOutcome.TemporarilyUnavailable -> {
                                val outcome =
                                    if (sourceOutcome is SourceScanOutcome.PermissionRequired) {
                                        SourceScanAttemptOutcome.PERMISSION_REQUIRED
                                    } else {
                                        SourceScanAttemptOutcome.TEMPORARILY_UNAVAILABLE
                                    }
                                val failure = SourceScanFailureException(scopedFailures)
                                completeSourceAttempt(
                                    request = request,
                                    outcome = outcome,
                                    unresolvedSourceKeys = sourceOutcome.unresolvedSourceKeys,
                                    reason = sourceOutcome.javaClass.simpleName,
                                    failure = failure,
                                    lastScanFailed = true,
                                )
                                emitStartupLibraryStatus(StartupLibraryStatus.SourceUnavailable)
                                if (
                                    checkpointAuthority == null &&
                                        IndexRequestPolicy.recordsSourceOutcome(request)
                                ) {
                                    musicSettings.lastScanFailed = true
                                }
                                emitIndexingCompletion(
                                    sessionId,
                                    failure,
                                    IndexingTerminalOutcome.SOURCE_UNAVAILABLE,
                                )
                                return@traceSuspend
                            }
                            is SourceScanOutcome.Partial,
                            is SourceScanOutcome.Truncated -> {
                                val retained =
                                    musicSettings.sourceConfigurationCheckpoint
                                        ?.unresolvedSourceKeys
                                        .orEmpty() - attemptedSourceKeys
                                val failure = SourceScanFailureException(scopedFailures)
                                completeSourceAttempt(
                                    request = request,
                                    outcome = SourceScanAttemptOutcome.FAILED_RETRYABLE,
                                    unresolvedSourceKeys =
                                        retained + sourceOutcome.unresolvedSourceKeys,
                                    reason =
                                        "${sourceOutcome.javaClass.simpleName} result not publishable",
                                    failure = failure,
                                    lastScanFailed = true,
                                )
                                if (
                                    checkpointAuthority == null &&
                                        IndexRequestPolicy.recordsSourceOutcome(request)
                                ) {
                                    musicSettings.lastScanFailed = true
                                }
                                emitStartupLibraryStatus(StartupLibraryStatus.SourceUnavailable)
                                emitIndexingCompletion(
                                    sessionId,
                                    failure,
                                    IndexingTerminalOutcome.SOURCE_UNAVAILABLE,
                                )
                                return@traceSuspend
                            }
                            else -> throw SourceScanFailureException(scopedFailures)
                        }
                    }
                val isEmpty = publishedLibrary.songs.isEmpty()
                val publishedState = if (isEmpty) LibraryState.EMPTY else LibraryState.USABLE
                val priorUnresolved =
                    musicSettings.sourceConfigurationCheckpoint?.unresolvedSourceKeys.orEmpty()
                val retainedUnresolved =
                    priorUnresolved - attemptedSourceKeys - plan?.removedSourceKeys.orEmpty()
                val unresolved = retainedUnresolved + sourceOutcome.unresolvedSourceKeys
                val effectiveSourceOutcome =
                    when {
                        unresolved.isEmpty() -> sourceOutcome
                        sourceOutcome is SourceScanOutcome.Success ->
                            SourceScanOutcome.Partial(sourceOutcome.committedSourceKeys, unresolved)
                        sourceOutcome is SourceScanOutcome.AuthoritativeEmpty ->
                            SourceScanOutcome.Partial(emptySet(), unresolved)
                        else -> sourceOutcome
                    }
                recordSourceScanOutcome(request, effectiveSourceOutcome)
                val partial =
                    effectiveSourceOutcome.isPartialSessionResult(
                        unresolvedSourceKeys = unresolved,
                        enrichmentComplete = result.enrichmentComplete,
                    )
                val attemptOutcome =
                    when (effectiveSourceOutcome) {
                        is SourceScanOutcome.Success -> SourceScanAttemptOutcome.SUCCESS
                        is SourceScanOutcome.AuthoritativeEmpty ->
                            SourceScanAttemptOutcome.AUTHORITATIVE_EMPTY
                        is SourceScanOutcome.Partial -> SourceScanAttemptOutcome.PARTIAL_SUCCESS
                        is SourceScanOutcome.Truncated -> SourceScanAttemptOutcome.TRUNCATED
                        is SourceScanOutcome.PermissionRequired ->
                            SourceScanAttemptOutcome.PERMISSION_REQUIRED
                        is SourceScanOutcome.TemporarilyUnavailable ->
                            SourceScanAttemptOutcome.TEMPORARILY_UNAVAILABLE
                        is SourceScanOutcome.Cancelled -> SourceScanAttemptOutcome.CANCELLED
                        is SourceScanOutcome.Interrupted,
                        is SourceScanOutcome.TimedOut,
                        is SourceScanOutcome.Failed ->
                            error("Terminal source outcome cannot be published")
                    }
                var libraryChanged = false
                if (checkpointAuthority != null) {
                    val accepted =
                        completeSourceAttempt(
                            request = request,
                            outcome = attemptOutcome,
                            unresolvedSourceKeys = unresolved,
                            reason = effectiveSourceOutcome.javaClass.simpleName,
                            publishedRevision = newRevision,
                            publishedLibraryState = publishedState,
                            lastScanFailed = partial,
                            publishAfterCommit = {
                                libraryChanged = replaceLibrary(publishedLibrary)
                            },
                        )
                    if (!accepted) {
                        throw IllegalStateException(
                            "Source attempt lost ownership before library publication"
                        )
                    }
                    if (libraryChanged) {
                        withContext(Dispatchers.Main) {
                            dispatchLibraryChange(device = true, user = true)
                        }
                    }
                } else {
                    musicSettings.revision = newRevision
                    emitLibrary(publishedLibrary)
                    musicSettings.libraryState = publishedState
                    if (request.reason != IndexReason.METADATA_ENRICHMENT) {
                        musicSettings.lastScanFailed = partial
                    }
                }
                try {
                    val cleanup =
                        CoverCleanupPolicy.evaluate(
                            published = true,
                            outcome = effectiveSourceOutcome,
                            unresolvedSourceKeys = unresolved,
                            unavailableSourceKeys = plan?.unavailableSourceKeys.orEmpty(),
                            completeMetadata = resolvedProfile == MetadataProfile.FULL,
                            enrichmentOnly = plan?.enrichmentOnly == true,
                        )
                    if (cleanup.allowed) {
                        result.cleanup()
                    } else {
                        L.d("Skipping cover cleanup [reason=${cleanup.reason}]")
                    }
                } catch (cleanupFailure: Exception) {
                    L.w(cleanupFailure, "Post-publication cover cleanup failed")
                }
                emitStartupLibraryStatus(
                    if (isEmpty) StartupLibraryStatus.Empty else StartupLibraryStatus.Usable
                )
                if (!isEmpty) {
                    emitStartupReadinessState(StartupReadinessState.FullLibraryReady)
                    requestGeneratedPlaylistRefresh()
                }
                if (resolvedProfile == MetadataProfile.FULL && result.enrichmentComplete) {
                    emitStartupReadinessState(StartupReadinessState.EnrichmentComplete)
                }
                emitIndexingCompletion(
                    sessionId,
                    error = null,
                    outcome =
                        if (partial) {
                            IndexingTerminalOutcome.PARTIAL_SUCCESS
                        } else {
                            IndexingTerminalOutcome.SUCCESS
                        },
                )

                if (resolvedProfile == MetadataProfile.LEAN && !isEmpty) {
                    // The worker defers this while playback is active, so rich extraction cannot
                    // compete with first audio or driving interaction.
                    worker.requestIndex(
                        IndexRequest(
                            reason = IndexReason.METADATA_ENRICHMENT,
                            withCache = true,
                            metadataProfile = MetadataProfile.FULL,
                            configurationGeneration = request.configurationGeneration,
                        )
                    )
                }
            } catch (e: CancellationException) {
                val terminalOutcome =
                    preparedInterruptionOutcome ?: IndexingTerminalOutcome.CANCELLED
                // Complete the durable attempt first so a stale authoritative cancellation cannot
                // replace an already-terminal outcome via the subsequent record call.
                val durableAccepted =
                    completeSourceAttemptForInterruption(request, terminalOutcome, e)
                // Record when non-authoritative (no checkpoint lease) or the durable completion
                // was accepted; mirrors the same rule in prepareIndexingInterruption.
                if (IndexRequestPolicy.shouldRecordInterruptionOutcome(request, durableAccepted)) {
                    recordSourceScanOutcome(
                        request,
                        sourceOutcomeForInterruption(terminalOutcome, request = request),
                    )
                }
                val directReplacementHandoff =
                    synchronized(this) {
                        preparedReplacementHandoff.also { preparedReplacementHandoff = false }
                    }
                if (directReplacementHandoff) {
                    L.i("Cancelled source generation handed directly to pending replacement")
                } else {
                    withContext(NonCancellable) {
                        emitIndexingCompletion(
                            sessionId,
                            IndexingInterruptedException(terminalOutcome),
                            terminalOutcome,
                        )
                    }
                }
                throw e
            } catch (e: Exception) {
                val attemptedKeys = currentAttemptedSourceKeys(sessionId, request)
                recordSourceScanOutcome(
                    request,
                    SourceScanOutcome.Failed(
                        retryable = true,
                        failureClass = e.javaClass.name,
                        failureMessage = e.message,
                        unresolvedSourceKeys =
                            musicSettings.sourceConfigurationCheckpoint
                                ?.unresolvedSourceKeys
                                .orEmpty() + attemptedKeys,
                    ),
                )
                val completedAttempt =
                    completeSourceAttempt(
                        request = request,
                        outcome = SourceScanAttemptOutcome.FAILED_RETRYABLE,
                        unresolvedSourceKeys =
                            musicSettings.sourceConfigurationCheckpoint
                                ?.unresolvedSourceKeys
                                .orEmpty() + attemptedKeys,
                        reason = "Fatal indexing failure",
                        failure = e,
                        lastScanFailed = true,
                    )
                if (
                    !completedAttempt &&
                        checkpointAuthority == null &&
                        request.reason != IndexReason.METADATA_ENRICHMENT
                ) {
                    musicSettings.lastScanFailed = true
                }
                L.w(e, "Indexing failed; committed source generations remain readable")
                emitIndexingCompletion(sessionId, e, IndexingTerminalOutcome.FAILED)
            }
        }

    override suspend fun refreshGeneratedPlaylists(force: Boolean) {
        val requestLibrary = synchronized(this) { library } ?: return
        val fingerprint =
            "${musicSettings.revision}:${deviceLibraryGeneration.get()}:" +
                requestLibrary.songs.size
        generatedPlaylistCoordinator.request(
            enabled = musicSettings.generatedPlaylistsEnabled,
            fingerprint = fingerprint,
            force = force,
        ) { enabled ->
            PerfTimer.point("startup.generated_playlist_start")
            try {
                val current =
                    synchronized(this@MusicRepositoryImpl) { library } ?: return@request false
                val startingRevision = musicSettings.revision
                val startingDeviceGeneration = deviceLibraryGeneration.get()
                val projected = current.withGeneratedPlaylists(enabled)
                val stillCurrent =
                    synchronized(this@MusicRepositoryImpl) {
                        library === current &&
                            musicSettings.revision == startingRevision &&
                            deviceLibraryGeneration.get() == startingDeviceGeneration
                    }
                if (!stillCurrent) {
                    L.d("Skipping generated-playlist projection superseded by a newer library")
                    return@request false
                }
                if (projected !== current) {
                    emitLibrary(projected, device = false, user = true)
                }
                L.i("Generated playlists ${if (enabled) "enabled" else "disabled"}")
                true
            } finally {
                PerfTimer.point("startup.generated_playlist_end")
            }
        }
    }

    private fun requestGeneratedPlaylistRefresh() {
        if (!musicSettings.generatedPlaylistsEnabled) return
        compatibilityHydrationScope.launch { refreshGeneratedPlaylists() }
    }

    private fun startCompatibilityBackfill() {
        compatibilityHydrationScope.launch {
            optionalWorkGate.awaitOpen()
            PerfTimer.point("startup.compatibility_backfill_start")
            try {
                val migrated = cache.populateNormalizedLibrary()
                if (migrated > 0) {
                    L.d("Backfilled $migrated legacy cache rows into the normalized library")
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                L.w(e, "Normalized library backfill failed; last valid library remains available")
            } finally {
                PerfTimer.point("startup.compatibility_backfill_end")
            }
        }
    }

    private fun startCompatibilityHydration(worker: IndexingWorker) {
        compatibilityHydrationJob?.cancel()
        val startingDeviceGeneration = deviceLibraryGeneration.get()
        val startingRevision = musicSettings.revision
        val priorState = musicSettings.libraryState
        val sourceConfigured =
            StartupLibraryPolicy.isMusicSourceConfigured(
                musicSettings.locationMode,
                musicSettings.configuredSourceCount,
            )
        compatibilityHydrationJob =
            compatibilityHydrationScope.launch {
                optionalWorkGate.awaitOpen()
                PerfTimer.point("startup.compatibility_hydration_start")
                try {
                    val cached = loadCachedLibrary()
                    val songCount = cached.songs.size
                    val decision =
                        StartupLibraryPolicy.onCachedLoadSucceeded(
                            priorState,
                            songCount,
                            musicSettings.lastScanFailed,
                        )
                    withContext(Dispatchers.Main) {
                        val publishLibrary =
                            songCount > 0 || decision.libraryState == LibraryState.EMPTY
                        val accepted =
                            synchronized(this@MusicRepositoryImpl) {
                                val superseded =
                                    deviceLibraryGeneration.get() != startingDeviceGeneration ||
                                        musicSettings.revision != startingRevision
                                if (superseded) {
                                    false
                                } else {
                                    if (publishLibrary) library = cached
                                    true
                                }
                            }
                        if (!accepted) {
                            L.d("Skipping compatibility hydration superseded by a newer scan")
                            return@withContext
                        }
                        musicSettings.libraryState = decision.libraryState
                        if (publishLibrary) {
                            dispatchLibraryChange(device = true, user = true)
                        }
                        emitStartupLibraryStatus(
                            StartupLibraryPolicy.startupReadinessAfterDecision(
                                decision,
                                sourceConfigured,
                                songCount,
                            )
                        )
                        if (songCount > 0) {
                            emitStartupReadinessState(StartupReadinessState.FullLibraryReady)
                            requestGeneratedPlaylistRefresh()
                        }
                        requestCompatibilityRecoveryIfNeeded(
                            worker,
                            priorState,
                            decision,
                            sourceConfigured,
                        )
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    L.w(e, "Compatibility cached-library hydration failed")
                    val decision =
                        StartupLibraryPolicy.onCachedLoadFailed(
                            priorState,
                            musicSettings.lastScanFailed,
                        )
                    withContext(Dispatchers.Main) {
                        val superseded =
                            synchronized(this@MusicRepositoryImpl) {
                                deviceLibraryGeneration.get() != startingDeviceGeneration ||
                                    musicSettings.revision != startingRevision
                            }
                        if (superseded) {
                            L.d("Skipping compatibility recovery superseded by a newer scan")
                            return@withContext
                        }
                        musicSettings.libraryState = decision.libraryState
                        emitStartupLibraryStatus(
                            StartupLibraryPolicy.startupReadinessAfterDecision(
                                decision,
                                sourceConfigured,
                                cachedSongCount = null,
                            )
                        )
                        requestCompatibilityRecoveryIfNeeded(
                            worker,
                            priorState,
                            decision,
                            sourceConfigured,
                        )
                    }
                } finally {
                    PerfTimer.point("startup.compatibility_hydration_end")
                }
            }
    }

    private fun requestCompatibilityRecoveryIfNeeded(
        worker: IndexingWorker,
        priorState: LibraryState,
        decision: StartupLibraryPolicy.Decision,
        sourceConfigured: Boolean,
    ) {
        if (
            priorState == LibraryState.USABLE &&
                decision.requestScan &&
                sourceConfigured &&
                !BuildConfig.TOPWAY_COMPAT_ENABLED
        ) {
            worker.requestIndex(
                IndexRequest(
                    reason = IndexReason.COMPATIBILITY_RECOVERY,
                    withCache = MusicScanRequestMode.REFRESH_WITH_CACHE,
                    configurationGeneration = musicSettings.sourceConfigurationGeneration,
                )
            )
        }
    }

    private suspend fun loadCachedLibrary(): MutableLibrary {
        val revision = musicSettings.revision ?: UUID.randomUUID()
        // Use a lightweight config for cached startup: no filesystem construction needed
        // since loadCached only reads from the DB cache and stored playlists.
        val config = createCachedConfig(revision)
        val start = System.currentTimeMillis()
        return Musikr.loadCached(context, config).also {
            L.d("Cached library loaded in ${System.currentTimeMillis() - start}ms")
        }
    }

    /**
     * Builds a minimal [Config] for cached startup that avoids touching the filesystem, storage
     * providers, or cover storage initialization. This prevents SAF/MediaStore provider queries
     * from competing with the cached library load on slow TS18 firmware.
     */
    private suspend fun createCachedConfig(revision: UUID): Config {
        val separators = Separators.from(musicSettings.separators)
        val nameFactory =
            if (musicSettings.intelligentSorting) {
                Naming.intelligent()
            } else {
                Naming.simple()
            }
        val covers = settingCovers.mutate(context, revision)
        // Use a no-op FS since loadCached doesn't explore the filesystem
        val fs = NoOpFS
        return Config(
            fs,
            Storage(cache, covers, storedPlaylists),
            Interpretation(nameFactory, separators),
            indexingWorkerCount = 1,
        )
    }

    private suspend fun createConfig(
        revision: UUID,
        cache: MutableCache,
        workerCount: Int,
        fs: FS,
        metadataProfile: MetadataProfile,
        scanPlan: IncrementalScanPlan?,
        sessionId: Long,
        request: IndexRequest,
        checkpointAuthority: SourceScanAttemptAuthority?,
    ): Config {
        val configStart = System.currentTimeMillis()
        val separators = Separators.from(musicSettings.separators)
        val nameFactory =
            if (musicSettings.intelligentSorting) Naming.intelligent() else Naming.simple()
        val covers = settingCovers.mutate(context, revision)
        L.d("Config: covers init ${System.currentTimeMillis() - configStart}ms")
        return Config(
            fs = fs,
            storage = Storage(cache, covers, storedPlaylists),
            interpretation = Interpretation(nameFactory, separators),
            indexingWorkerCount = workerCount,
            metadataProfile = metadataProfile,
            dimensionPolicy = DrivingStartupPolicy.dimensions(metadataProfile),
            artworkPolicy = DrivingStartupPolicy.artworkPolicy(metadataProfile),
            scanPlan = scanPlan,
            // The retained set is only complete once rich extraction has run, and the invocation
            // itself is additionally gated on a complete authoritative outcome by
            // CoverCleanupPolicy.
            cleanupCovers =
                metadataProfile == MetadataProfile.FULL && scanPlan?.enrichmentOnly != true,
            sourceCommitAuthorised = {
                val current =
                    sourceCommitStillCurrent(sessionId) &&
                        !IndexRequestPolicy.isSupersededByNewerConfiguration(
                            request,
                            musicSettings.sourceConfigurationGeneration,
                        ) &&
                        (checkpointAuthority == null ||
                            musicSettings.ownsSourceConfigurationAttempt(
                                checkpointAuthority.generation,
                                checkpointAuthority.attemptId,
                                checkpointAuthority.owner,
                            ))
                if (!current) markSourceCommitSuperseded(sessionId, request)
                current
            },
            sourceCommitStillCurrent = { sourceCommitStillCurrent(sessionId) },
        )
    }

    private fun sourceCommitStillCurrent(sessionId: Long): Boolean =
        synchronized(this) {
            indexingSessionGate.isCurrent(sessionId) &&
                preparedInterruptionOutcome == null &&
                !preparedReplacementHandoff
        }

    private fun markSourceCommitSuperseded(sessionId: Long, request: IndexRequest) {
        synchronized(this) {
            if (preparedInterruptionOutcome == null) {
                preparedInterruptionOutcome = IndexingTerminalOutcome.SUPERSEDED
            }
        }
        L.w(
            "Rejecting stale source commit [session=$sessionId generation=" +
                "${request.configurationGeneration} attempt=${request.attemptId}]"
        )
    }

    private fun createFileSystem(sessionId: Long, sourceKeys: Set<String>? = null): FS {
        val delegate =
            when (musicSettings.locationMode) {
                LocationMode.SAF -> SAF.from(context, musicSettings.safQuery)
                LocationMode.MEDIA_STORE -> {
                    val query =
                        musicSettings.mediaStoreQuery.copy(
                            // Keep the shared MediaStore adapter variant-neutral. Topway
                            // compatibility
                            // selects the relaxed provider heuristic at this app integration
                            // boundary;
                            // physical TS18 outcomes still require device validation.
                            relaxIsMusicHeuristic = musicSettings.ts18SystemSourceFilter
                        )
                    MediaStore.from(context, query)
                }
                LocationMode.DIRECT_FS ->
                    DirectFS(musicSettings.safQuery) { progress ->
                        updateDirectFsWorkProgress(sessionId, progress)
                    }
            }
        val sourceAware =
            if (musicSettings.locationMode == LocationMode.MEDIA_STORE) {
                delegate
            } else {
                ConfiguredSourceAwareFS(delegate, musicSettings.configuredSourceSpecs)
            }
        return if (sourceKeys != null && sourceAware is org.oxycblt.musikr.fs.SourceAwareFS) {
            sourceAware.selectSources(sourceKeys)
        } else {
            sourceAware
        }
    }

    private fun sourceConfigurationRevision(): Long =
        // One shared definition with ConfiguredSourcePolicy. Interpretation and resource settings
        // are deliberately excluded so a separator or sort-order change refreshes the library
        // without invalidating every committed source generation.
        SourceConfigurationIdentity.revision(musicSettings)

    private fun emitStartupReadinessState(state: StartupReadinessState) {
        startupReadinessController.publishCapability(state)
    }

    private fun emitStartupLibraryStatus(status: StartupLibraryStatus) {
        startupReadinessController.publishLibraryStatus(status)
    }

    private fun beginIndexing(request: IndexRequest): Long {
        val now = SystemClock.elapsedRealtime()
        lastProgressLogAtElapsedMs = 0L
        lastHeartbeatPersistedAtElapsedMs = 0L
        lastDirectFsProgressLogAtElapsedMs = 0L
        lastLoggedProgressPhase = null
        val sessionId = indexingSessionIds.incrementAndGet()
        val attemptedKeys =
            request.sourceKeys?.takeIf { it.isNotEmpty() }
                ?: musicSettings.configuredSourceSpecs.mapTo(linkedSetOf()) { it.sourceKey }
        val labels =
            musicSettings.configuredSourceSpecs
                .asSequence()
                .filter { it.sourceKey in attemptedKeys }
                .map { it.displayPath }
                .distinct()
                .toList()
        val sourceScope = indexingSourceScope(attemptedKeys)
        synchronized(this) {
            preparedInterruptionOutcome = null
            preparedReplacementHandoff = false
            indexingSessionGate.begin(sessionId)
            currentIndexingState =
                IndexingState.Indexing(
                    progress = IndexingProgress.Stage(IndexingPhase.PREPARING),
                    sessionId = sessionId,
                    request = request,
                    locationMode = musicSettings.locationMode,
                    sourceLabels = labels,
                    attemptedSourceKeys = attemptedKeys,
                    startedAtElapsedMs = now,
                    lastProgressAtElapsedMs = now,
                    sourceScope = sourceScope,
                )
        }
        L.i(
            "Indexing session started [reason=${request.reason} mode=${musicSettings.locationMode} " +
                "generation=${request.configurationGeneration} attempt=${request.attemptId} " +
                "owner=${request.attemptOwner?.lifecycleId} scope=$sourceScope sources=$labels]"
        )
        diagnosticJournal.log(
            DiagnosticJournal.CAT_INDEXING,
            "Started",
            "session=$sessionId reason=${request.reason} " +
                "mode=${musicSettings.locationMode} generation=${request.configurationGeneration} " +
                "attempt=${request.attemptId} owner=${request.attemptOwner?.lifecycleId} " +
                "scope=$sourceScope sources=$labels",
        )
        dispatchIndexingState()
        return sessionId
    }

    private fun updateNonAuthoritativeWorkDeferred(sessionId: Long, deferred: Boolean) {
        if (!deferred) return
        synchronized(this) {
            val current = currentIndexingState as? IndexingState.Indexing ?: return
            if (current.sessionId != sessionId || !indexingSessionGate.isCurrent(sessionId)) return
            currentIndexingState = current.copy(nonAuthoritativeWorkDeferred = true)
        }
    }

    private suspend fun emitIndexingProgress(
        sessionId: Long,
        request: IndexRequest,
        progress: IndexingProgress,
    ) {
        yield()
        val now = SystemClock.elapsedRealtime()
        val update =
            synchronized(this) {
                val current = currentIndexingState as? IndexingState.Indexing
                if (
                    current == null ||
                        current.sessionId != sessionId ||
                        !indexingSessionGate.isCurrent(sessionId)
                ) {
                    return@synchronized null
                }
                val meaningful = progress.isMeaningfulAfter(current.progress)
                if (!meaningful) return@synchronized null
                val phaseChanged = progress.phase != current.progress.phase
                val firstFileEmitted =
                    current.firstFileEmitted ||
                        ((progress as? IndexingProgress.Songs)?.explored ?: 0) > 0
                val updated =
                    current.copy(
                        progress = progress,
                        lastProgressAtElapsedMs = now,
                        watchdogState = IndexingWatchdogState.HEALTHY,
                        watchdogDetail = "",
                        noProgressDurationMs = 0L,
                        firstFileEmitted = firstFileEmitted,
                    )
                currentIndexingState = updated
                updated to phaseChanged
            } ?: return
        val (updatedState, phaseChanged) = update
        val shouldPersistHeartbeat =
            synchronized(this) {
                val shouldPersist =
                    phaseChanged ||
                        lastHeartbeatPersistedAtElapsedMs == 0L ||
                        now - lastHeartbeatPersistedAtElapsedMs >= HEARTBEAT_PERSIST_INTERVAL_MS
                if (shouldPersist) lastHeartbeatPersistedAtElapsedMs = now
                shouldPersist
            }
        if (shouldPersistHeartbeat) {
            IndexRequestPolicy.checkpointAuthority(request)?.let { authority ->
                val counts = progress.counts()
                musicSettings.heartbeatSourceConfigurationAttempt(
                    generation = authority.generation,
                    attemptId = authority.attemptId,
                    owner = authority.owner,
                    nowMs = System.currentTimeMillis(),
                    progress =
                        SourceScanAttemptProgress(
                            phase = progress.phase.name,
                            explored = counts.explored,
                            loaded = counts.loaded,
                            evaluated = counts.evaluated,
                            currentItem = progress.currentItem,
                            directFsDirectoriesVisited = updatedState.directFsDirectoriesVisited,
                            directFsEntriesInspected = updatedState.directFsEntriesInspected,
                            directFsFilesEmitted = updatedState.directFsFilesEmitted,
                            queuedDirectFsWork = updatedState.queuedDirectFsWork,
                            activeDirectFsEnumerators = updatedState.activeDirectFsEnumerators,
                        ),
                )
            }
        }
        if (
            progress.phase != lastLoggedProgressPhase ||
                now - lastProgressLogAtElapsedMs >= PROGRESS_LOG_INTERVAL_MS
        ) {
            lastLoggedProgressPhase = progress.phase
            lastProgressLogAtElapsedMs = now
            val counts =
                (progress as? IndexingProgress.Songs)?.let {
                    " explored=${it.explored} loaded=${it.loaded} evaluated=${it.evaluated}"
                } ?: ""
            L.i("Indexing progress [phase=${progress.phase}$counts item=${progress.currentItem}]")
            diagnosticJournal.log(
                DiagnosticJournal.CAT_INDEXING,
                "Progress",
                "session=$sessionId generation=${request.configurationGeneration} " +
                    "attempt=${request.attemptId} phase=${progress.phase}$counts " +
                    "item=${progress.currentItem} firstFile=${updatedState.firstFileEmitted}",
            )
        }
        dispatchIndexingState()
    }

    private fun updateDirectFsWorkProgress(sessionId: Long, progress: DirectFsWorkProgress) {
        val now = SystemClock.elapsedRealtime()
        val update =
            synchronized(this) {
                val current = currentIndexingState as? IndexingState.Indexing
                if (
                    current == null ||
                        current.sessionId != sessionId ||
                        !indexingSessionGate.isCurrent(sessionId)
                ) {
                    return@synchronized null
                }
                val meaningful =
                    progress.directoriesVisited > (current.directFsDirectoriesVisited ?: -1) ||
                        progress.entriesInspected > (current.directFsEntriesInspected ?: -1) ||
                        progress.filesEmitted > (current.directFsFilesEmitted ?: -1)
                val updated =
                    current.copy(
                        lastProgressAtElapsedMs =
                            if (meaningful) now else current.lastProgressAtElapsedMs,
                        watchdogState =
                            if (meaningful) {
                                IndexingWatchdogState.HEALTHY
                            } else {
                                current.watchdogState
                            },
                        watchdogDetail = if (meaningful) "" else current.watchdogDetail,
                        noProgressDurationMs = if (meaningful) 0L else current.noProgressDurationMs,
                        firstFileEmitted = current.firstFileEmitted || progress.filesEmitted > 0,
                        directFsDirectoriesVisited = progress.directoriesVisited,
                        directFsEntriesInspected = progress.entriesInspected,
                        directFsFilesEmitted = progress.filesEmitted,
                        queuedDirectFsWork = progress.queuedDirectories,
                        activeDirectFsEnumerators = progress.activeEnumerators,
                    )
                currentIndexingState = updated
                val persistHeartbeat =
                    meaningful &&
                        (lastHeartbeatPersistedAtElapsedMs == 0L ||
                            now - lastHeartbeatPersistedAtElapsedMs >=
                                HEARTBEAT_PERSIST_INTERVAL_MS)
                if (persistHeartbeat) lastHeartbeatPersistedAtElapsedMs = now
                val logProgress =
                    meaningful &&
                        (lastDirectFsProgressLogAtElapsedMs == 0L ||
                            now - lastDirectFsProgressLogAtElapsedMs >= PROGRESS_LOG_INTERVAL_MS)
                if (logProgress) lastDirectFsProgressLogAtElapsedMs = now
                Triple(updated, persistHeartbeat, logProgress)
            } ?: return
        val (updated, persistHeartbeat, logProgress) = update
        if (persistHeartbeat) {
            updated.request
                ?.let { IndexRequestPolicy.checkpointAuthority(it) }
                ?.let { authority ->
                    val counts = updated.progress.counts()
                    musicSettings.heartbeatSourceConfigurationAttempt(
                        generation = authority.generation,
                        attemptId = authority.attemptId,
                        owner = authority.owner,
                        nowMs = System.currentTimeMillis(),
                        progress =
                            SourceScanAttemptProgress(
                                phase = updated.progress.phase.name,
                                explored = counts.explored,
                                loaded = counts.loaded,
                                evaluated = counts.evaluated,
                                currentItem = updated.progress.currentItem,
                                directFsDirectoriesVisited = progress.directoriesVisited,
                                directFsEntriesInspected = progress.entriesInspected,
                                directFsFilesEmitted = progress.filesEmitted,
                                queuedDirectFsWork = progress.queuedDirectories,
                                activeDirectFsEnumerators = progress.activeEnumerators,
                            ),
                    )
                }
        }
        if (logProgress) {
            diagnosticJournal.log(
                DiagnosticJournal.CAT_INDEXING,
                "DirectFS progress",
                "session=$sessionId generation=" +
                    "${updated.request?.configurationGeneration} attempt=" +
                    "${updated.request?.attemptId} directories=${progress.directoriesVisited} " +
                    "entries=${progress.entriesInspected} files=${progress.filesEmitted} " +
                    "queued=${progress.queuedDirectories} active=${progress.activeEnumerators}",
            )
        }
    }

    private suspend fun emitLibrary(
        newLibrary: MutableLibrary,
        device: Boolean = true,
        user: Boolean = true,
    ) {
        val emitStart = System.currentTimeMillis()
        val changed = replaceLibrary(newLibrary)
        if (!changed) {
            L.d("Library instance has not changed, skipping update")
            return
        }

        // A completed Musikr publication is a deliberate generation boundary. Avoid comparing all
        // songs/albums/artists/genres/playlists under the repository monitor; consumers invalidate
        // from monotonic generations instead.
        withContext(Dispatchers.Main) { dispatchLibraryChange(device = device, user = user) }
        L.d("emitLibrary completed in ${System.currentTimeMillis() - emitStart}ms")
    }

    private fun replaceLibrary(newLibrary: MutableLibrary): Boolean =
        synchronized(this) {
            if (library === newLibrary) {
                false
            } else {
                library = newLibrary
                true
            }
        }

    private fun completeSourceAttempt(
        request: IndexRequest,
        outcome: SourceScanAttemptOutcome,
        unresolvedSourceKeys: Set<String>,
        reason: String,
        failure: Throwable? = null,
        publishedRevision: UUID? = null,
        publishedLibraryState: LibraryState? = null,
        lastScanFailed: Boolean,
        publishAfterCommit: () -> Unit = {},
    ): Boolean {
        val authority = IndexRequestPolicy.checkpointAuthority(request) ?: return false
        return musicSettings.completeSourceConfigurationAttempt(
            generation = authority.generation,
            attemptId = authority.attemptId,
            owner = authority.owner,
            nowMs = System.currentTimeMillis(),
            completion =
                SourceScanAttemptCompletion(
                    outcome = outcome,
                    unresolvedSourceKeys = unresolvedSourceKeys,
                    reason = reason,
                    failureClass = failure?.javaClass?.name,
                    failureMessage = failure?.message,
                    publishedRevision = publishedRevision,
                    publishedLibraryState = publishedLibraryState,
                    lastScanFailed = lastScanFailed,
                ),
            publishAfterCommit = publishAfterCommit,
        )
    }

    private fun completeAttemptForInterruption(
        state: IndexingState.Indexing,
        outcome: IndexingTerminalOutcome,
    ) {
        state.request?.let { request ->
            val accepted = completeSourceAttemptForInterruption(request, outcome, null)
            if (accepted) {
                recordSourceScanOutcome(
                    request,
                    sourceOutcomeForInterruption(outcome, state, request),
                )
            }
        }
    }

    private fun recordSourceScanOutcome(request: IndexRequest, outcome: SourceScanOutcome) {
        if (IndexRequestPolicy.recordsSourceOutcome(request)) lastSourceScanOutcome = outcome
    }

    private fun completeSourceAttemptForInterruption(
        request: IndexRequest,
        outcome: IndexingTerminalOutcome,
        failure: Throwable?,
    ): Boolean {
        val state = synchronized(this) { currentIndexingState as? IndexingState.Indexing }
        val attemptedSources =
            state?.attemptedSourceKeys?.takeIf { it.isNotEmpty() } ?: attemptedSourceKeys(request)
        val retained = musicSettings.sourceConfigurationCheckpoint?.unresolvedSourceKeys.orEmpty()
        val unresolved =
            when (outcome) {
                IndexingTerminalOutcome.TIMED_OUT,
                IndexingTerminalOutcome.SOURCE_UNAVAILABLE,
                IndexingTerminalOutcome.FAILED -> retained + attemptedSources
                else -> retained
            }
        val attemptOutcome =
            when (outcome) {
                IndexingTerminalOutcome.SUCCESS -> SourceScanAttemptOutcome.SUCCESS
                IndexingTerminalOutcome.PARTIAL_SUCCESS -> SourceScanAttemptOutcome.PARTIAL_SUCCESS
                IndexingTerminalOutcome.SOURCE_UNAVAILABLE ->
                    SourceScanAttemptOutcome.TEMPORARILY_UNAVAILABLE
                IndexingTerminalOutcome.FAILED -> SourceScanAttemptOutcome.FAILED_RETRYABLE
                IndexingTerminalOutcome.CANCELLED -> SourceScanAttemptOutcome.CANCELLED
                IndexingTerminalOutcome.SERVICE_STOPPED -> SourceScanAttemptOutcome.SERVICE_STOPPED
                IndexingTerminalOutcome.SUPERSEDED -> SourceScanAttemptOutcome.SUPERSEDED
                IndexingTerminalOutcome.TIMED_OUT -> SourceScanAttemptOutcome.TIMED_OUT
            }
        val reason =
            if (outcome == IndexingTerminalOutcome.TIMED_OUT) {
                state?.watchdogDetail?.ifBlank { null } ?: "Stage-aware watchdog timeout"
            } else {
                outcome.name
            }
        return completeSourceAttempt(
            request = request,
            outcome = attemptOutcome,
            unresolvedSourceKeys = unresolved,
            reason = reason,
            failure = failure,
            lastScanFailed =
                outcome == IndexingTerminalOutcome.TIMED_OUT ||
                    outcome == IndexingTerminalOutcome.SOURCE_UNAVAILABLE ||
                    outcome == IndexingTerminalOutcome.FAILED,
        )
    }

    private fun sourceOutcomeForInterruption(
        outcome: IndexingTerminalOutcome,
        state: IndexingState.Indexing? =
            synchronized(this) { currentIndexingState as? IndexingState.Indexing },
        request: IndexRequest? = state?.request,
    ): SourceScanOutcome {
        val retained = musicSettings.sourceConfigurationCheckpoint?.unresolvedSourceKeys.orEmpty()
        val attempted =
            state?.attemptedSourceKeys?.takeIf { it.isNotEmpty() }
                ?: request?.let(::attemptedSourceKeys).orEmpty()
        val unresolved = retained + attempted
        return when (outcome) {
            IndexingTerminalOutcome.CANCELLED -> SourceScanOutcome.Cancelled(retained)
            IndexingTerminalOutcome.TIMED_OUT ->
                SourceScanOutcome.TimedOut(
                    phase = state?.progress?.phase?.name.orEmpty(),
                    noProgressMs = state?.noProgressDurationMs ?: 0L,
                    detail = state?.watchdogDetail.orEmpty(),
                    unresolvedSourceKeys = unresolved,
                )
            IndexingTerminalOutcome.SOURCE_UNAVAILABLE ->
                SourceScanOutcome.TemporarilyUnavailable(unresolved)
            IndexingTerminalOutcome.FAILED ->
                SourceScanOutcome.Failed(true, "IndexingFailure", null, unresolved)
            IndexingTerminalOutcome.SUCCESS,
            IndexingTerminalOutcome.PARTIAL_SUCCESS,
            IndexingTerminalOutcome.SERVICE_STOPPED,
            IndexingTerminalOutcome.SUPERSEDED -> SourceScanOutcome.Interrupted(outcome, retained)
        }
    }

    private fun indexingSourceScope(attemptedSourceKeys: Set<String>): IndexingSourceScope {
        if (musicSettings.locationMode != LocationMode.DIRECT_FS) {
            return IndexingSourceScope.UNKNOWN
        }
        val scopes =
            musicSettings.configuredSourceSpecs
                .asSequence()
                .filter { it.sourceKey in attemptedSourceKeys }
                .mapNotNull { it.traversalScope }
                .toSet()
        return when {
            scopes.isEmpty() -> IndexingSourceScope.UNKNOWN
            scopes == setOf(CanonicalSourcePolicy.Scope.EXPLICIT) -> IndexingSourceScope.NARROW
            scopes == setOf(CanonicalSourcePolicy.Scope.WHOLE_VOLUME) ->
                IndexingSourceScope.WHOLE_VOLUME
            else -> IndexingSourceScope.MIXED
        }
    }

    private fun currentAttemptedSourceKeys(sessionId: Long, request: IndexRequest): Set<String> =
        synchronized(this) {
            (currentIndexingState as? IndexingState.Indexing)
                ?.takeIf { it.sessionId == sessionId }
                ?.attemptedSourceKeys
                ?.takeIf { it.isNotEmpty() }
        } ?: attemptedSourceKeys(request)

    private fun attemptedSourceKeys(request: IndexRequest): Set<String> =
        request.sourceKeys?.takeIf { it.isNotEmpty() }
            ?: musicSettings.configuredSourceSpecs.mapTo(linkedSetOf()) { it.sourceKey }

    private fun IndexingProgress.isMeaningfulAfter(previous: IndexingProgress): Boolean {
        if (phase != previous.phase || currentItem != previous.currentItem) return true
        val currentCounts = counts()
        val previousCounts = previous.counts()
        return currentCounts != previousCounts
    }

    private fun IndexingProgress.counts(): ProgressCounts =
        if (this is IndexingProgress.Songs) {
            ProgressCounts(explored, loaded, evaluated)
        } else {
            ProgressCounts(0, 0, 0)
        }

    private data class ProgressCounts(val explored: Int, val loaded: Int, val evaluated: Int)

    private suspend fun emitIndexingCompletion(
        sessionId: Long,
        error: Exception?,
        outcome: IndexingTerminalOutcome =
            if (error == null) IndexingTerminalOutcome.SUCCESS else IndexingTerminalOutcome.FAILED,
    ) {
        yield()
        val completedState =
            synchronized(this) {
                val active = currentIndexingState as? IndexingState.Indexing
                if (
                    active == null ||
                        active.sessionId != sessionId ||
                        !indexingSessionGate.complete(sessionId)
                ) {
                    return@synchronized null
                }
                previousCompletedState = IndexingState.Completed(error, outcome)
                currentIndexingState = null
                preparedInterruptionOutcome = null
                preparedReplacementHandoff = false
                active
            }
        if (completedState == null) {
            L.d(
                "Ignoring stale or duplicate indexing completion [session=$sessionId outcome=$outcome]"
            )
            return
        }
        L.i("Dispatching indexing completion [outcome=$outcome error=$error]")
        diagnosticJournal.log(
            DiagnosticJournal.CAT_INDEXING,
            "Completed",
            "session=$sessionId generation=${completedState.request?.configurationGeneration} " +
                "attempt=${completedState.request?.attemptId} outcome=$outcome " +
                "error=${error?.javaClass?.name.orEmpty()} " +
                "message=${error?.message.orEmpty()} sourceOutcome=$lastSourceScanOutcome " +
                "watchdog=${completedState.watchdogDetail}",
            result = outcome.name,
        )
        dispatchIndexingState()
    }

    private fun dispatchIndexingState() {
        for (listener in indexingListeners) {
            listener.onIndexingStateChanged()
        }
    }

    private companion object {
        const val PROGRESS_LOG_INTERVAL_MS = 5_000L
        const val HEARTBEAT_PERSIST_INTERVAL_MS = 5_000L
    }

    private fun dispatchLibraryChange(device: Boolean, user: Boolean) {
        val changes =
            MusicRepository.Changes(
                deviceLibrary = device,
                userLibrary = user,
                deviceGeneration =
                    if (device) deviceLibraryGeneration.incrementAndGet()
                    else deviceLibraryGeneration.get(),
                userGeneration =
                    if (user) userLibraryGeneration.incrementAndGet()
                    else userLibraryGeneration.get(),
            )
        L.d("Dispatching library change [changes=$changes]")
        for (listener in updateListeners) {
            listener.onMusicChanges(changes)
        }
    }
}

/**
 * A no-op [FS] implementation used during cached startup. Cached startup loads from the DB cache
 * without exploring the filesystem, so no real FS is needed. This avoids triggering
 * SAF/MediaStore/StorageManager queries on startup.
 */
private object NoOpFS : FS {
    override suspend fun explore(
        files: Channel<org.oxycblt.musikr.fs.File>
    ): kotlinx.coroutines.Deferred<Result<Unit>> {
        files.close()
        return CompletableDeferred(Result.success(Unit))
    }

    override fun track(): Flow<FSUpdate> = emptyFlow()
}
