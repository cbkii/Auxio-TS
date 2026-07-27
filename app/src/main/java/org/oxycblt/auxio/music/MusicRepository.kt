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
import org.oxycblt.auxio.headunit.root.RootStateHolder
import org.oxycblt.auxio.headunit.topway.TopwaySourcePolicy
import org.oxycblt.auxio.image.covers.SettingCovers
import org.oxycblt.auxio.music.MusicRepository.IndexingWorker
import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.auxio.music.shim.WriteOnlyMutableCache
import org.oxycblt.auxio.util.PerfTimer
import org.oxycblt.musikr.Config
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
import org.oxycblt.musikr.fs.FS
import org.oxycblt.musikr.fs.FSUpdate
import org.oxycblt.musikr.fs.direct.DirectFS
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
    }
}

/** The current state of music loading. */
sealed interface IndexingState {
    /**
     * Currently indexing and extracting tags from device music.
     *
     * @param progress The current progress of the music pipeline.
     */
    data class Indexing(val progress: IndexingProgress) : IndexingState

    /**
     * Music loading has completed.
     *
     * @param error If an error occurred during loading, it will be contained here.
     */
    data class Completed(val error: Exception?) : IndexingState
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
    private val deviceLibraryGeneration = AtomicLong(0L)
    private val userLibraryGeneration = AtomicLong(0L)
    private val compatibilityHydrationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    @Volatile private var compatibilityHydrationJob: Job? = null

    @Volatile override var library: MutableLibrary? = null
    @Volatile private var previousCompletedState: IndexingState.Completed? = null
    @Volatile private var currentIndexingState: IndexingState? = null
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

    @Synchronized
    override fun unregisterWorker(worker: IndexingWorker) {
        if (indexingWorker !== worker) {
            L.w("Given worker did not match current worker")
            return
        }
        L.d("Unregistering worker $worker")
        indexingWorker = null
        currentIndexingState = null
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
            val checkpoint = musicSettings.sourceConfigurationCheckpoint ?: return@launch
            val sourceKeys =
                checkpoint.unresolvedSourceKeys.ifEmpty {
                    musicSettings.configuredSourceSpecs.mapTo(linkedSetOf()) { it.sourceKey }
                }
            for (sourceKey in sourceKeys) invalidateSource(sourceKey)
            val requiresAuthoritativeInitial =
                checkpoint.state == SourceConfigurationCheckpoint.State.PENDING ||
                    checkpoint.state == SourceConfigurationCheckpoint.State.RUNNING
            requestIndex(
                IndexRequest(
                    reason = IndexReason.USER_RETRY,
                    withCache = !requiresAuthoritativeInitial,
                    configurationGeneration = checkpoint.generation,
                    sourceKeys = sourceKeys,
                )
            )
        }
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
                    isTopwayCompat = BuildConfig.TOPWAY_COMPAT_FLAVOR,
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
                                SourceConfigurationCheckpoint.State.PENDING &&
                                pendingCheckpoint?.state !=
                                    SourceConfigurationCheckpoint.State.RUNNING
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
            if (indexingWorker !== worker) {
                L.w("Index requested from unregistered worker; ignoring")
                return@traceSuspend
            }

            val playbackActive = worker.playbackActiveSnapshot()
            val resolvedProfile =
                DrivingStartupPolicy.metadataProfile(
                    explicit = request.metadataProfile,
                    scanPriority = musicSettings.scanPriority,
                    playbackActive = playbackActive,
                    isTopwayVariant = BuildConfig.TOPWAY_COMPAT_FLAVOR,
                )
            val currentRevision = musicSettings.revision
            val newRevision = currentRevision?.takeIf { request.withCache } ?: UUID.randomUUID()
            val workerCount =
                DefaultIndexingResourcePolicy.resolveWorkerCount(
                    scanPriority = musicSettings.scanPriority,
                    playbackActive = playbackActive,
                    isTopwayVariant = BuildConfig.TOPWAY_COMPAT_FLAVOR,
                    availableProcessors = Runtime.getRuntime().availableProcessors(),
                )
            val requestedSourceKeys = request.sourceKeys?.takeIf { it.isNotEmpty() }
            val allConfiguredSourceKeys =
                musicSettings.configuredSourceSpecs.mapTo(linkedSetOf()) { it.sourceKey }
            val attemptedSourceKeys = requestedSourceKeys ?: allConfiguredSourceKeys
            val rawFs =
                createFileSystem(sourceKeys = requestedSourceKeys.takeIf { !request.withCache })
            val prepared =
                if (!request.withCache) {
                    L.i("Using simple source-authoritative scan; incremental preflight bypassed")
                    IncrementalIndexPlanner.Prepared(
                        fs = rawFs,
                        cache = WriteOnlyMutableCache(cache),
                        plan = null,
                    )
                } else {
                    try {
                        IncrementalIndexPlanner.prepare(
                            fs = rawFs,
                            cache = cache,
                            withCache = true,
                            profile = resolvedProfile,
                            configurationRevision = sourceConfigurationRevision(),
                            targetSourceKeys = requestedSourceKeys,
                            legacyWriteOnly = ::WriteOnlyMutableCache,
                        )
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        request.configurationGeneration?.let {
                            musicSettings.returnSourceConfigurationToPending(
                                it,
                                "TemporarilyUnavailable",
                            )
                        }
                        lastSourceScanOutcome =
                            SourceScanOutcome.TemporarilyUnavailable(attemptedSourceKeys)
                        musicSettings.lastScanFailed = true
                        emitStartupLibraryStatus(StartupLibraryStatus.SourceUnavailable)
                        L.w(
                            e,
                            "Music-source preflight failed; preserving the last readable library",
                        )
                        emitIndexingCompletion(e)
                        return@traceSuspend
                    }
                }
            val plan = prepared.plan
            L.i(
                "Resolved scan policy [workers=$workerCount profile=$resolvedProfile " +
                    "reason=${request.reason} generation=${request.configurationGeneration} " +
                    "scan=${plan?.scanSourceKeys} reuse=${plan?.reuseSourceKeys} " +
                    "unavailable=${plan?.unavailableSourceKeys}]"
            )

            if (
                plan != null &&
                    !plan.hasWork &&
                    plan.unavailableSourceKeys.isEmpty() &&
                    synchronized(this) { library != null }
            ) {
                L.i("All configured sources are unchanged; skipping provider scan and extraction")
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
                request.configurationGeneration?.let {
                    val unresolved =
                        musicSettings.sourceConfigurationCheckpoint
                            ?.unresolvedSourceKeys
                            .orEmpty() - (request.sourceKeys ?: emptySet())
                    musicSettings.acknowledgeSourceConfiguration(
                        it,
                        unresolvedSourceKeys = unresolved,
                        outcome = "Success",
                    )
                }
                emitIndexingCompletion(null)
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
                request.configurationGeneration?.let {
                    musicSettings.returnSourceConfigurationToPending(it, "TemporarilyUnavailable")
                }
                musicSettings.lastScanFailed = true
                emitIndexingCompletion(Exception("Music source inaccessible"))
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
            try {
                val start = System.currentTimeMillis()
                val result =
                    Musikr.new(
                            context = context,
                            config = config,
                            noisyDirs = TopwaySourcePolicy.NOISY_DIRS,
                            pathKeywords = pathKeywords,
                            rootGate = rootGate,
                        )
                        .run(::emitIndexingProgress)
                L.d("Index finished in ${System.currentTimeMillis() - start}ms")
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
                lastSourceScanOutcome = sourceOutcome

                if (
                    plan == null &&
                        musicSettings.locationMode == LocationMode.SAF &&
                        result.library.songs.isEmpty()
                ) {
                    if (locations.any { !it.path.volume.isAccessible() }) {
                        L.w("Legacy scan became inaccessible. Preserving cache.")
                        musicSettings.lastScanFailed = true
                        emitIndexingCompletion(Exception("Source became inaccessible during scan"))
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
                                request.configurationGeneration?.let {
                                    musicSettings.returnSourceConfigurationToPending(
                                        it,
                                        sourceOutcome.javaClass.simpleName,
                                    )
                                }
                                emitStartupLibraryStatus(StartupLibraryStatus.SourceUnavailable)
                                musicSettings.lastScanFailed = true
                                emitIndexingCompletion(SourceScanFailureException(scopedFailures))
                                return@traceSuspend
                            }
                            is SourceScanOutcome.Partial,
                            is SourceScanOutcome.Truncated -> {
                                request.configurationGeneration?.let { generation ->
                                    val retained =
                                        musicSettings.sourceConfigurationCheckpoint
                                            ?.unresolvedSourceKeys
                                            .orEmpty() - attemptedSourceKeys
                                    musicSettings.acknowledgeSourceConfiguration(
                                        generation,
                                        retained + sourceOutcome.unresolvedSourceKeys,
                                        sourceOutcome.javaClass.simpleName,
                                    )
                                }
                                musicSettings.lastScanFailed = true
                                emitStartupLibraryStatus(StartupLibraryStatus.SourceUnavailable)
                                emitIndexingCompletion(SourceScanFailureException(scopedFailures))
                                return@traceSuspend
                            }
                            else -> throw SourceScanFailureException(scopedFailures)
                        }
                    }
                musicSettings.revision = newRevision
                emitLibrary(publishedLibrary)
                try {
                    result.cleanup()
                } catch (cleanupFailure: Exception) {
                    L.w(cleanupFailure, "Post-publication cover cleanup failed")
                }
                val isEmpty = publishedLibrary.songs.isEmpty()
                musicSettings.libraryState =
                    if (isEmpty) LibraryState.EMPTY else LibraryState.USABLE
                musicSettings.lastScanFailed = false
                request.configurationGeneration?.let { generation ->
                    val priorUnresolved =
                        musicSettings.sourceConfigurationCheckpoint?.unresolvedSourceKeys.orEmpty()
                    val retainedUnresolved = priorUnresolved - attemptedSourceKeys
                    when (sourceOutcome) {
                        is SourceScanOutcome.Success,
                        is SourceScanOutcome.AuthoritativeEmpty ->
                            musicSettings.acknowledgeSourceConfiguration(
                                generation,
                                unresolvedSourceKeys = retainedUnresolved,
                                outcome = sourceOutcome.javaClass.simpleName,
                            )
                        is SourceScanOutcome.Partial ->
                            musicSettings.acknowledgeSourceConfiguration(
                                generation,
                                unresolvedSourceKeys =
                                    retainedUnresolved + sourceOutcome.unresolvedSourceKeys,
                                outcome = "Partial",
                            )
                        is SourceScanOutcome.Truncated ->
                            musicSettings.acknowledgeSourceConfiguration(
                                generation,
                                unresolvedSourceKeys =
                                    retainedUnresolved + sourceOutcome.unresolvedSourceKeys,
                                outcome = "Truncated",
                            )
                        is SourceScanOutcome.PermissionRequired,
                        is SourceScanOutcome.TemporarilyUnavailable,
                        SourceScanOutcome.Cancelled ->
                            musicSettings.returnSourceConfigurationToPending(
                                generation,
                                sourceOutcome.javaClass.simpleName,
                            )
                    }
                }
                emitStartupLibraryStatus(
                    if (isEmpty) StartupLibraryStatus.Empty else StartupLibraryStatus.Usable
                )
                if (!isEmpty) {
                    emitStartupReadinessState(StartupReadinessState.FullLibraryReady)
                    requestGeneratedPlaylistRefresh()
                }
                if (resolvedProfile == MetadataProfile.FULL) {
                    emitStartupReadinessState(StartupReadinessState.EnrichmentComplete)
                }
                emitIndexingCompletion(null)

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
                lastSourceScanOutcome = SourceScanOutcome.Cancelled
                request.configurationGeneration?.let {
                    musicSettings.returnSourceConfigurationToPending(it, "Cancelled")
                }
                withContext(NonCancellable) {
                    emitIndexingCompletion(
                        Exception("Music indexing cancelled before completion", e)
                    )
                }
                throw e
            } catch (e: Exception) {
                request.configurationGeneration?.let {
                    musicSettings.returnSourceConfigurationToPending(it, "Failed")
                }
                musicSettings.lastScanFailed = true
                L.w(e, "Indexing failed; committed source generations remain readable")
                emitIndexingCompletion(e)
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
                !BuildConfig.TOPWAY_COMPAT_FLAVOR
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
            cleanupCovers = scanPlan == null && metadataProfile == MetadataProfile.FULL,
        )
    }

    private fun createFileSystem(sourceKeys: Set<String>? = null): FS {
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
                LocationMode.DIRECT_FS -> DirectFS(musicSettings.safQuery.source)
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

    private fun sourceConfigurationRevision(): Long {
        val material = buildString {
            append(musicSettings.locationMode)
            append('|').append(musicSettings.safQuery)
            append('|').append(musicSettings.mediaStoreQuery)
            append('|').append(musicSettings.rootAccessPolicy)
            append('|').append(musicSettings.ts18SystemSourceFilter)
            append('|').append(musicSettings.separators)
            append('|').append(musicSettings.intelligentSorting)
        }
        return material.hashCode().toLong() and 0xffffffffL
    }

    private fun emitStartupReadinessState(state: StartupReadinessState) {
        startupReadinessController.publishCapability(state)
    }

    private fun emitStartupLibraryStatus(status: StartupLibraryStatus) {
        startupReadinessController.publishLibraryStatus(status)
    }

    private suspend fun emitIndexingProgress(progress: IndexingProgress) {
        yield()
        currentIndexingState = IndexingState.Indexing(progress)
        for (listener in indexingListeners) {
            listener.onIndexingStateChanged()
        }
    }

    private suspend fun emitLibrary(
        newLibrary: MutableLibrary,
        device: Boolean = true,
        user: Boolean = true,
    ) {
        val emitStart = System.currentTimeMillis()
        val changed =
            synchronized(this) {
                if (library === newLibrary) {
                    false
                } else {
                    library = newLibrary
                    true
                }
            }
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

    private suspend fun emitIndexingCompletion(error: Exception?) {
        yield()
        previousCompletedState = IndexingState.Completed(error)
        currentIndexingState = null
        L.d("Dispatching completion state [error=$error]")
        for (listener in indexingListeners) {
            listener.onIndexingStateChanged()
        }
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
