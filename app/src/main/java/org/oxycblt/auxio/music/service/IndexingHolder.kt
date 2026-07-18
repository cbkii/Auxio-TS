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

import android.content.Context
import android.os.PowerManager
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.ForegroundListener
import org.oxycblt.auxio.ForegroundServiceNotification
import org.oxycblt.auxio.headunit.root.RootStateHolder
import org.oxycblt.auxio.music.IndexingState
import org.oxycblt.auxio.music.MusicRepository
import org.oxycblt.auxio.music.MusicSettings
import org.oxycblt.auxio.music.ObservationMode
import org.oxycblt.auxio.music.RootAccessPolicy
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
    private val rootGate: RootStateHolder,
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
        private val rootGate: RootStateHolder,
    ) {
        fun create(context: Context, listener: ForegroundListener) =
            IndexingHolder(
                context,
                listener,
                playbackManager,
                musicRepository,
                musicSettings,
                rootGate,
            )
    }

    private val indexJob = Job()
    private val indexScope = CoroutineScope(indexJob + Dispatchers.IO)

    private var currentIndexJob: Job? = null
    private var pendingIndexRequest: IndexRequest? = null
    private var startupJob: Job? = null
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

    fun attach() {
        musicSettings.registerListener(this)
        musicRepository.addUpdateListener(this)
        musicRepository.addIndexingListener(this)
        musicRepository.registerWorker(this)
        playbackManager.addListener(this)
        // Observer attachment is cheap: it registers notifications only. Provider enumeration and
        // extraction remain planner-controlled and notification bursts are conflated below.
        if (musicSettings.shouldBeObserving) startTracking()
    }

    fun release() {
        startupJob?.cancel()
        startupJob = null
        stopTracking()
        observationRequestJob?.cancel()
        observationRequestJob = null
        currentIndexJob?.cancel()
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
    fun start() {
        PerfTimer.trace("IndexingHolder.start") {
            if (startupJob?.isActive == true) {
                L.d("Startup library load already running; ignoring duplicate start")
                return
            }
            startupJob =
                indexScope.launch {
                    // Root probing is intentionally on-demand. Normal startup must restore
                    // playback/session surfaces without waiting for su.
                    musicRepository.startup(this@IndexingHolder)
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
            val changed = indexingNotification.updateIndexingState(state.progress)
            if (changed) {
                post(indexingNotification)
            }
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
        requestIndexLocked(IndexRequest(withCache, null))
    }

    @Synchronized
    override fun requestIndex(withCache: Boolean, metadataProfile: MetadataProfile) {
        requestIndexLocked(IndexRequest(withCache, metadataProfile))
    }

    private fun requestIndexLocked(request: IndexRequest) {
        if (currentIndexJob?.isActive == true) {
            coalescePendingIndex(request)
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
        currentIndexJob =
            indexScope.launch {
                try {
                    if (request.metadataProfile != null) {
                        musicRepository.index(
                            this@IndexingHolder,
                            request.withCache,
                            request.metadataProfile,
                        )
                    } else {
                        musicRepository.index(this@IndexingHolder, request.withCache)
                    }
                } finally {
                    synchronized(this@IndexingHolder) {
                        currentIndexJob = null
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
                        }
                    }
                }
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
        } else {
            wakeLock.releaseSafe()
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
                LocationMode.DIRECT_FS ->
                    DirectFS(
                        musicSettings.safQuery.source,
                        rootGate.takeIf {
                            musicSettings.rootAccessPolicy == RootAccessPolicy.ON_DEMAND
                        },
                    )
            }
        trackingJob =
            indexScope.launch {
                fs.track().collect { update ->
                    val location = (update as? FSUpdate.LocationChanged)?.location
                    musicRepository.invalidateSource(location?.let(SourceIdentity::forLocation))
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
                                requestIndex(true)
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

    override fun onMusicLocationsChanged() {
        super.onMusicLocationsChanged()
        if (musicSettings.shouldBeObserving) startTracking() else stopTracking()
        indexScope.launch {
            musicRepository.invalidateSource()
            musicRepository.requestIndex(true)
        }
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
    }
}
