/*
 * Copyright (c) 2024 Auxio Project
 * IndexingHolder.kt is part of Auxio.
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

package org.oxycblt.auxio.music.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.PowerManager
import android.os.SystemClock
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.ForegroundListener
import org.oxycblt.auxio.ForegroundServiceNotification
import org.oxycblt.auxio.music.IndexReason
import org.oxycblt.auxio.music.IndexRequest
import org.oxycblt.auxio.music.IndexingState
import org.oxycblt.auxio.music.IndexingTerminalOutcome
import org.oxycblt.auxio.music.IndexingWatchdogPolicy
import org.oxycblt.auxio.music.IndexingWatchdogState
import org.oxycblt.auxio.music.LibraryState
import org.oxycblt.auxio.music.MusicRepository
import org.oxycblt.auxio.music.MusicSettings
import org.oxycblt.auxio.music.ObservationMode
import org.oxycblt.auxio.music.StartupLibraryStatus
import org.oxycblt.auxio.music.StartupOptionalWorkGate
import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.auxio.util.PerfTimer
import org.oxycblt.auxio.util.getSystemServiceCompat
import org.oxycblt.musikr.fs.FSUpdate
import org.oxycblt.musikr.fs.SourceIdentity
import org.oxycblt.musikr.fs.direct.DirectFS
import org.oxycblt.musikr.fs.mediastore.MediaStore
import org.oxycblt.musikr.fs.saf.SAF
import org.oxycblt.musikr.library.MetadataProfile
import timber.log.Timber as L

class IndexingHolder
private constructor(
    private val workerContext: Context,
    private val foregroundListener: ForegroundListener,
    private val playbackManager: PlaybackStateManager,
    private val musicRepository: MusicRepository,
    private val musicSettings: MusicSettings,
    private val optionalWorkGate: StartupOptionalWorkGate,
) :
    MusicRepository.IndexingWorker,
    MusicRepository.IndexingListener,
    MusicRepository.UpdateListener,
    MusicSettings.Listener,
    PlaybackStateManager.Listener {
    class Factory
    @Inject
    constructor(
        private val playbackManager: PlaybackStateManager,
        private val musicRepository: MusicRepository,
        private val musicSettings: MusicSettings,
        private val optionalWorkGate: StartupOptionalWorkGate,
    ) {
        fun create(context: Context, listener: ForegroundListener) =
            IndexingHolder(
                context,
                listener,
                playbackManager,
                musicRepository,
                musicSettings,
                optionalWorkGate,
            )
    }

    private val indexJob = Job()
    private val indexScope = CoroutineScope(indexJob + Dispatchers.IO)

    private var currentIndexJob: Job? = null
    private var activeIndexRequest: IndexRequest? = null
    private var pendingIndexRequest: IndexRequest? = null
    private var startupJob: Job? = null
    private var startupRecoveryJob: Job? = null
    private var watchdogJob: Job? = null
    private var sourceConfigurationJob: Job? = null
    private var attached = false
    private var activeStartupOrigin: StartupScanOrigin? = null
    private var pendingStartupOrigin: StartupScanOrigin? = null
    private var lastHandledSourceConfigurationGeneration = Long.MIN_VALUE
    private val indexingNotification = IndexingNotification(workerContext)
    private val observingNotification = ObservingNotification(workerContext)
    private val wakeLock =
        workerContext
            .getSystemServiceCompat(PowerManager::class)
            .newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                BuildConfig.APPLICATION_ID + ":IndexingComponent",
            )
    private var trackingJob: Job? = null
    private var observationRequestJob: Job? = null
    private val observationBurstGate = ObservationBurstGate()
    private val pendingObservedSourceKeys = linkedSetOf<String>()
    private var removableStorageReceiver: BroadcastReceiver? = null
    private val removableStorageJobs = mutableMapOf<String, Job>()

    fun attach() {
        synchronized(this) {
            if (attached) return
            attached = true
        }
        musicSettings.registerListener(this)
        musicRepository.addUpdateListener(this)
        musicRepository.addIndexingListener(this)
        musicRepository.registerWorker(this)
        playbackManager.addListener(this)
        // Observer attachment is cheap: it registers notifications only. Provider enumeration and
        // extraction remain planner-controlled and notification bursts are conflated below.
        if (musicSettings.shouldBeObserving) startTracking()
        updateRemovableStorageReceiver()
    }

    fun release() {
        synchronized(this) {
            if (!attached) return
            attached = false
            activeStartupOrigin = null
            pendingStartupOrigin = null
        }
        startupJob?.cancel()
        startupJob = null
        startupRecoveryJob?.cancel()
        startupRecoveryJob = null
        watchdogJob?.cancel()
        watchdogJob = null
        sourceConfigurationJob?.cancel()
        sourceConfigurationJob = null
        stopTracking()
        unregisterRemovableStorageReceiver()
        observationRequestJob?.cancel()
        observationRequestJob = null
        if (currentIndexJob?.isActive == true) {
            musicRepository.prepareIndexingInterruption(IndexingTerminalOutcome.SERVICE_STOPPED)
            currentIndexJob?.cancel()
        }
        currentIndexJob = null
        pendingIndexRequest = null
        indexJob.cancel()
        wakeLock.releaseSafe()
        musicRepository.unregisterWorker(this)
        playbackManager.removeListener(this)
        musicRepository.removeIndexingListener(this)
        musicRepository.removeUpdateListener(this)
        musicSettings.unregisterListener(this)
    }

    @Synchronized
    fun start(origin: StartupScanOrigin = StartupScanOrigin.BACKGROUND) {
        PerfTimer.trace("IndexingHolder.start(origin=$origin)") {
            if (!attached) {
                L.d("Ignoring startup request after IndexingHolder release [origin=$origin]")
                return
            }
            if (startupJob?.isActive == true) {
                if (
                    origin == StartupScanOrigin.USER_VISIBLE &&
                        activeStartupOrigin != StartupScanOrigin.USER_VISIBLE
                ) {
                    pendingStartupOrigin = StartupScanOrigin.USER_VISIBLE
                    L.d("Queued trusted visible startup behind active background startup")
                } else {
                    L.d("Startup library load already running; ignoring duplicate [origin=$origin]")
                }
                return
            }
            activeStartupOrigin = origin
            startupJob =
                indexScope.launch {
                    try {
                        val sourceAuthority =
                            StartupScanAuthorityPolicy.hasCurrentSourceAuthority(
                                workerContext,
                                musicSettings,
                            )
                        val automaticScanAllowed =
                            StartupScanAuthorityPolicy.allowAutomaticScan(
                                topwayCompatFlavor = BuildConfig.TOPWAY_COMPAT_FLAVOR,
                                origin = origin,
                                sourceAuthority = sourceAuthority,
                            )
                        // Root probing remains on-demand. Cache and playback restoration do not
                        // wait
                        // for su, source traversal, or a library scan.
                        musicRepository.startup(this@IndexingHolder)
                        val pendingConfiguration =
                            synchronized(this@IndexingHolder) {
                                musicSettings.claimPendingConfiguration()?.also { checkpoint ->
                                    lastHandledSourceConfigurationGeneration = checkpoint.generation
                                }
                            }
                        if (pendingConfiguration != null) {
                            L.i(
                                "Claiming durable source configuration for a simple initial scan " +
                                    "[generation=${pendingConfiguration.generation}]"
                            )
                            requestIndex(
                                IndexRequest(
                                    reason = IndexReason.INITIAL_CONFIGURATION,
                                    withCache = false,
                                    configurationGeneration = pendingConfiguration.generation,
                                    sourceKeys =
                                        musicSettings.configuredSourceSpecs.mapTo(linkedSetOf()) {
                                            it.sourceKey
                                        },
                                )
                            )
                        } else if (BuildConfig.TOPWAY_COMPAT_FLAVOR && automaticScanAllowed) {
                            requestVisibleRecoveryScan(sourceAuthority)
                        }
                    } finally {
                        val nextOrigin =
                            synchronized(this@IndexingHolder) {
                                startupJob = null
                                activeStartupOrigin = null
                                if (attached) {
                                    pendingStartupOrigin.also { pendingStartupOrigin = null }
                                } else {
                                    pendingStartupOrigin = null
                                    null
                                }
                            }
                        if (nextOrigin != null) start(nextOrigin)
                    }
                }
        }
    }

    private fun requestVisibleRecoveryScan(sourceAuthority: Boolean) {
        if (!sourceAuthority) return
        val needsImmediateScan =
            musicSettings.revision == null || musicSettings.libraryState != LibraryState.USABLE
        if (needsImmediateScan) {
            L.i(
                "Trusted visible startup is repairing the library " +
                    "[state=${musicSettings.libraryState} revision=${musicSettings.revision}]"
            )
            requestIndex(
                IndexRequest(
                    reason = IndexReason.COMPATIBILITY_RECOVERY,
                    withCache = false,
                    configurationGeneration = musicSettings.sourceConfigurationGeneration,
                )
            )
            return
        }
        // A previous failure must not permanently strand an empty or unusable library,
        // but it should suppress delayed retry loops once a usable generation exists.
        if (musicSettings.lastScanFailed) return

        startupRecoveryJob?.cancel()
        startupRecoveryJob =
            indexScope.launch {
                delay(STARTUP_RECOVERY_GRACE_MS)
                val shouldRecover =
                    synchronized(this@IndexingHolder) {
                        attached && currentIndexJob?.isActive != true
                    } &&
                        !musicSettings.lastScanFailed &&
                        (musicRepository.library == null ||
                            musicRepository.startupLibraryStatus != StartupLibraryStatus.Usable)
                if (shouldRecover) {
                    L.w("Cached library did not become usable; requesting one recovery scan")
                    requestIndex(
                        IndexRequest(
                            reason = IndexReason.COMPATIBILITY_RECOVERY,
                            withCache = true,
                            configurationGeneration = musicSettings.sourceConfigurationGeneration,
                        )
                    )
                }
            }
    }

    fun createNotification(post: (ForegroundServiceNotification?) -> Unit) {
        val state = musicRepository.indexingState
        if (state is IndexingState.Indexing) {
            // There are a few reasons why we stay in the foreground with automatic rescanning:
            // 1. Newer versions of Android have become more and more restrictive regarding
            // how a foreground service starts. Thus, it's best to go foreground now so that
            // we can go foreground later.
            // 2. If a non-foreground service is killed, the app will probably still be alive,
            // and thus the music library will not be updated at all.
            indexingNotification.updateIndexingState(state)
            // Re-entering startForeground is intentional and idempotent. Foreground ownership
            // must never depend on whether the newly built progress text was deduplicated.
            post(indexingNotification)
        } else if (musicSettings.shouldBeObserving) {
            // Not observing and done loading, exit foreground.
            L.d("Exiting foreground")
            post(observingNotification)
        } else {
            post(null)
        }
    }

    @Synchronized
    override fun playbackActiveSnapshot(): Boolean = playbackManager.progression.isPlaying

    @Synchronized
    override fun requestIndex(withCache: Boolean) {
        requestIndex(IndexRequest(reason = IndexReason.USER_REFRESH, withCache = withCache))
    }

    @Synchronized
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

    @Synchronized
    override fun requestIndex(request: IndexRequest) {
        requestIndexLocked(request)
    }

    private fun requestIndexLocked(request: IndexRequest) {
        if (currentIndexJob?.isActive == true) {
            coalescePendingIndex(request)
            musicRepository.setPendingIndexReplacement(true)
            val activeGeneration = activeIndexRequest?.configurationGeneration
            val incomingGeneration = request.configurationGeneration
            val newerGeneration =
                activeGeneration != null &&
                    incomingGeneration != null &&
                    incomingGeneration > activeGeneration
            if (newerGeneration || request.reason == IndexReason.USER_RETRY) {
                L.i(
                    "Replacement request supersedes active indexing " +
                        "[reason=${request.reason} active=$activeGeneration " +
                        "incoming=$incomingGeneration]"
                )
                currentIndexJob?.cancel()
            }
            L.i("Coalesced indexing request while scan is running [request=$request]")
            return
        }
        val playbackActive = playbackActiveSnapshot()
        val mustWaitForIdle =
            playbackActive &&
                (request.metadataProfile == MetadataProfile.FULL ||
                    musicSettings.observationMode == ObservationMode.WHEN_IDLE)
        if (mustWaitForIdle) {
            coalescePendingIndex(request)
            L.i("Deferred indexing/enrichment until playback is idle [request=$request]")
            return
        }
        startIndexLocked(request)
    }

    @Synchronized
    private fun coalescePendingIndex(request: IndexRequest) {
        pendingIndexRequest = IndexRequestCoalescer.merge(pendingIndexRequest, request)
    }

    @Synchronized
    private fun startIndexLocked(request: IndexRequest) {
        L.i("Starting new indexing job [request=$request]")
        musicRepository.setPendingIndexReplacement(false)
        activeIndexRequest = request
        currentIndexJob =
            indexScope.launch {
                try {
                    musicRepository.index(this@IndexingHolder, request)
                } finally {
                    synchronized(this@IndexingHolder) {
                        currentIndexJob = null
                        activeIndexRequest = null
                        val pending = pendingIndexRequest
                        if (pending != null) {
                            val playbackActive = playbackActiveSnapshot()
                            val mustWaitForIdle =
                                playbackActive &&
                                    (pending.metadataProfile == MetadataProfile.FULL ||
                                        musicSettings.observationMode == ObservationMode.WHEN_IDLE)
                            if (!mustWaitForIdle) {
                                pendingIndexRequest = null
                                startIndexLocked(pending)
                            }
                        } else {
                            musicRepository.setPendingIndexReplacement(false)
                        }
                    }
                }
            }
    }

    @Synchronized
    override fun cancelIndexing() {
        pendingIndexRequest = null
        musicRepository.setPendingIndexReplacement(false)
        if (currentIndexJob?.isActive == true) {
            L.i("Cancelling active indexing job at user request")
            musicRepository.prepareIndexingInterruption(IndexingTerminalOutcome.CANCELLED)
            currentIndexJob?.cancel()
        }
    }

    override fun onProgressionChanged(progression: org.oxycblt.auxio.playback.state.Progression) {
        if (!progression.isPlaying) {
            synchronized(this) {
                val pending = pendingIndexRequest
                if (pending != null && currentIndexJob?.isActive != true) {
                    pendingIndexRequest = null
                    startIndexLocked(pending)
                }
            }
        }
    }

    override fun onIndexingStateChanged() {
        foregroundListener.updateForeground(ForegroundListener.Change.INDEXER)
        val state = musicRepository.indexingState
        if (state is IndexingState.Indexing) {
            wakeLock.acquireSafe()
            ensureWatchdog()
        } else {
            wakeLock.releaseSafe()
            watchdogJob?.cancel()
            watchdogJob = null
        }
    }

    private fun ensureWatchdog() {
        if (watchdogJob?.isActive == true) return
        watchdogJob =
            indexScope.launch {
                while (true) {
                    delay(WATCHDOG_POLL_MS)
                    val state =
                        musicRepository.indexingState as? IndexingState.Indexing ?: return@launch
                    wakeLock.acquireSafe()
                    val watchdogState =
                        IndexingWatchdogPolicy.classify(
                            nowElapsedMs = SystemClock.elapsedRealtime(),
                            startedAtElapsedMs = state.startedAtElapsedMs,
                            lastProgressAtElapsedMs = state.lastProgressAtElapsedMs,
                        )
                    musicRepository.updateIndexingWatchdog(watchdogState)
                    if (watchdogState == IndexingWatchdogState.OVERDUE) {
                        synchronized(this@IndexingHolder) {
                            pendingIndexRequest = null
                            musicRepository.setPendingIndexReplacement(false)
                            musicRepository.prepareIndexingInterruption(
                                IndexingTerminalOutcome.TIMED_OUT
                            )
                            currentIndexJob?.cancel()
                        }
                        return@launch
                    }
                }
            }
    }

    override fun onMusicChanges(changes: MusicRepository.Changes) {
        L.d("Music changed [device=${changes.deviceLibrary}, user=${changes.userLibrary}]")
        if (musicSettings.shouldBeObserving && trackingJob == null) startTracking()
        // Playback owns its persistent primitive queue. Rich metadata reconciliation is bounded
        // by the playback holder and must not clear all artwork or reapply a complete Song queue.
    }

    private fun startTracking() {
        stopTracking()
        if (!musicSettings.shouldBeObserving) return
        val fs =
            when (musicSettings.locationMode) {
                LocationMode.MEDIA_STORE ->
                    MediaStore.from(workerContext, musicSettings.mediaStoreQuery)
                LocationMode.SAF -> SAF.from(workerContext, musicSettings.safQuery)
                LocationMode.DIRECT_FS -> DirectFS(musicSettings.safQuery.source)
            }
        trackingJob =
            indexScope.launch {
                fs.track().collect { update ->
                    val location = (update as? FSUpdate.LocationChanged)?.location
                    val sourceKey = location?.let(SourceIdentity::forLocation)
                    musicRepository.invalidateSource(sourceKey)
                    synchronized(this@IndexingHolder) {
                        if (sourceKey != null) pendingObservedSourceKeys += sourceKey
                    }
                    if (update is FSUpdate.LocationChanged) {
                        // Check if the location that changed is still accessible
                        if (location != null && !location.path.volume.isAccessible()) {
                            L.i("Source became inaccessible (unmounted?): ${location.uri}")
                            cancelCurrentIndex()
                            // Keep the tracker alive and continue to the debounced planner so the
                            // source ledger records unavailability without publishing an empty
                            // generation.
                        }
                    }

                    val token = observationBurstGate.nextToken()
                    observationRequestJob?.cancel()
                    observationRequestJob =
                        indexScope.launch {
                            delay(OBSERVATION_DEBOUNCE_MS)
                            if (observationBurstGate.isLatest(token)) {
                                L.i("Storage notification burst settled; planning cached refresh")
                                val sourceKeys =
                                    synchronized(this@IndexingHolder) {
                                        pendingObservedSourceKeys.toSet().also {
                                            pendingObservedSourceKeys.clear()
                                        }
                                    }
                                requestIndex(
                                    IndexRequest(
                                        reason = IndexReason.SOURCE_OBSERVER,
                                        withCache = true,
                                        configurationGeneration =
                                            musicSettings.sourceConfigurationGeneration,
                                        sourceKeys = sourceKeys.takeIf { it.isNotEmpty() },
                                    )
                                )
                            }
                        }
                }
            }
    }

    private fun stopTracking() {
        trackingJob?.cancel()
        trackingJob = null
        observationRequestJob?.cancel()
        observationRequestJob = null
        pendingObservedSourceKeys.clear()
    }

    @Synchronized
    private fun cancelCurrentIndex() {
        currentIndexJob?.let {
            if (it.isActive) {
                L.i("Cancelling active indexing job due to source change")
                it.cancel()
            }
        }
    }

    @Synchronized
    private fun cancelSourceWork(sourceKeys: Set<String>) {
        val activeSources = activeIndexRequest?.sourceKeys
        if (
            currentIndexJob?.isActive == true &&
                (activeSources == null || activeSources.any { it in sourceKeys })
        ) {
            L.i("Cancelling indexing work targeting removed sources $sourceKeys")
            currentIndexJob?.cancel()
        }
        pendingIndexRequest =
            pendingIndexRequest?.let { pending ->
                val pendingSources = pending.sourceKeys ?: return@let pending
                val remaining = pendingSources - sourceKeys
                if (remaining.isEmpty()) null else pending.copy(sourceKeys = remaining)
            }
    }

    private fun updateRemovableStorageReceiver() {
        if (musicSettings.locationMode != LocationMode.DIRECT_FS) {
            unregisterRemovableStorageReceiver()
            return
        }
        if (removableStorageReceiver != null) return
        val receiver =
            object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val action = intent?.action ?: return
                    val keys =
                        RemovableStorageEventPolicy.matchingSourceKeys(
                            intent.data?.path,
                            musicSettings.configuredSourceSpecs,
                        )
                    if (keys.isEmpty()) return
                    when (action) {
                        Intent.ACTION_MEDIA_MOUNTED -> scheduleMountedSourceRetry(keys)
                        Intent.ACTION_MEDIA_UNMOUNTED,
                        Intent.ACTION_MEDIA_EJECT,
                        Intent.ACTION_MEDIA_REMOVED -> handleSourcesRemoved(keys)
                    }
                }
            }
        val filter =
            IntentFilter().apply {
                addAction(Intent.ACTION_MEDIA_MOUNTED)
                addAction(Intent.ACTION_MEDIA_UNMOUNTED)
                addAction(Intent.ACTION_MEDIA_EJECT)
                addAction(Intent.ACTION_MEDIA_REMOVED)
                addDataScheme("file")
            }
        workerContext.registerReceiver(receiver, filter)
        removableStorageReceiver = receiver
    }

    private fun unregisterRemovableStorageReceiver() {
        removableStorageJobs.values.forEach { it.cancel() }
        removableStorageJobs.clear()
        val receiver = removableStorageReceiver ?: return
        removableStorageReceiver = null
        try {
            workerContext.unregisterReceiver(receiver)
        } catch (_: IllegalArgumentException) {
            // Already unregistered during service teardown.
        }
    }

    private fun scheduleMountedSourceRetry(sourceKeys: Set<String>) {
        val eventKey = sourceKeys.sorted().joinToString("|")
        removableStorageJobs.remove(eventKey)?.cancel()
        removableStorageJobs[eventKey] =
            indexScope.launch {
                try {
                    optionalWorkGate.awaitOpen()
                    for (settleDelay in RemovableStorageEventPolicy.settleDelaysMs) {
                        delay(settleDelay)
                        val readable =
                            musicSettings.configuredSourceSpecs
                                .asSequence()
                                .filter { it.sourceKey in sourceKeys }
                                .filter { spec ->
                                    runCatching {
                                            spec.normalizedUri.path?.let(::File)?.let {
                                                it.canRead() &&
                                                    it.isDirectory &&
                                                    it.listFiles() != null
                                            } ?: false
                                        }
                                        .getOrDefault(false)
                                }
                                .mapTo(linkedSetOf()) { it.sourceKey }
                        if (readable.isEmpty()) continue
                        for (sourceKey in readable) musicRepository.invalidateSource(sourceKey)
                        musicRepository.requestIndex(
                            IndexRequest(
                                reason = IndexReason.STORAGE_MOUNTED,
                                withCache = true,
                                configurationGeneration =
                                    musicSettings.sourceConfigurationGeneration,
                                sourceKeys = readable,
                            )
                        )
                        return@launch
                    }
                    L.w("Mounted source did not become app-readable after bounded settle attempts")
                } finally {
                    synchronized(this@IndexingHolder) { removableStorageJobs.remove(eventKey) }
                }
            }
    }

    private fun handleSourcesRemoved(sourceKeys: Set<String>) {
        removableStorageJobs.entries
            .filter { (eventKey, job) -> job.isActive && sourceKeys.any { eventKey.contains(it) } }
            .forEach { (_, job) -> job.cancel() }
        cancelSourceWork(sourceKeys)
        musicRepository.markSourcesTemporarilyUnavailable(sourceKeys)
    }

    override fun onMusicLocationsChanged() {
        super.onMusicLocationsChanged()
        if (musicSettings.shouldBeObserving) startTracking() else stopTracking()
        updateRemovableStorageReceiver()
        sourceConfigurationJob?.cancel()
        sourceConfigurationJob =
            indexScope.launch {
                delay(SOURCE_CONFIGURATION_DEBOUNCE_MS)
                val (checkpoint, shouldHandle) =
                    synchronized(this@IndexingHolder) {
                        val pending = musicSettings.claimPendingConfiguration()
                        val currentGeneration =
                            pending?.generation ?: musicSettings.sourceConfigurationGeneration
                        if (currentGeneration == lastHandledSourceConfigurationGeneration) {
                            pending to false
                        } else {
                            lastHandledSourceConfigurationGeneration = currentGeneration
                            pending to true
                        }
                    }
                if (!shouldHandle) {
                    L.d(
                        "Ignoring duplicate source callback " +
                            "[generation=${musicSettings.sourceConfigurationGeneration}]"
                    )
                    return@launch
                }
                musicRepository.invalidateSource()
                musicRepository.requestIndex(
                    IndexRequest(
                        reason =
                            if (checkpoint != null) {
                                IndexReason.INITIAL_CONFIGURATION
                            } else {
                                IndexReason.USER_REFRESH
                            },
                        withCache = checkpoint == null,
                        configurationGeneration = checkpoint?.generation,
                        sourceKeys =
                            musicSettings.configuredSourceSpecs.mapTo(linkedSetOf()) {
                                it.sourceKey
                            },
                    )
                )
                sourceConfigurationJob = null
            }
    }

    override fun onGeneratedPlaylistsChanged() {
        indexScope.launch { musicRepository.refreshGeneratedPlaylists(force = false) }
    }

    override fun onIndexingSettingChanged() {
        super.onIndexingSettingChanged()
        musicRepository.requestIndex(true)
    }

    override fun onObservingChanged() {
        super.onObservingChanged()
        if (musicSettings.shouldBeObserving) startTracking() else stopTracking()
        // Make sure we don't override the service state with the observing
        // notification if we were actively loading when the automatic rescanning
        // setting changed. In such a case, the state will still be updated when
        // the music loading process ends.
        if (musicRepository.indexingState == null) {
            L.d("Not loading, updating idle session")
            foregroundListener.updateForeground(ForegroundListener.Change.INDEXER)
        }
    }

    /** Utility to safely acquire a [PowerManager.WakeLock] without crashes/inefficiency. */
    private fun PowerManager.WakeLock.acquireSafe() {
        // Avoid unnecessary acquire calls.
        if (!wakeLock.isHeld) {
            L.d("Acquiring wake lock")
            // Time out after a minute, which is the average music loading time for a medium-sized
            // library. If this runs out, we will re-request the lock, and if music loading is
            // shorter than the timeout, it will be released early.
            acquire(WAKELOCK_TIMEOUT_MS)
        }
    }

    /** Utility to safely release a [PowerManager.WakeLock] without crashes/inefficiency. */
    private fun PowerManager.WakeLock.releaseSafe() {
        // Avoid unnecessary release calls.
        if (wakeLock.isHeld) {
            L.d("Releasing wake lock")
            release()
        }
    }

    companion object {
        const val WAKELOCK_TIMEOUT_MS = 60 * 1000L
        internal const val OBSERVATION_DEBOUNCE_MS = 750L
        internal const val SOURCE_CONFIGURATION_DEBOUNCE_MS = 600L
        internal const val STARTUP_RECOVERY_GRACE_MS = 3_000L
        internal const val WATCHDOG_POLL_MS = 5_000L
    }
}
