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
import coil3.ImageLoader
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.ForegroundListener
import org.oxycblt.auxio.ForegroundServiceNotification
import org.oxycblt.auxio.music.IndexingState
import org.oxycblt.auxio.music.MusicRepository
import org.oxycblt.auxio.music.MusicSettings
import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import org.oxycblt.auxio.util.getSystemServiceCompat
import org.oxycblt.musikr.MusicParent
import org.oxycblt.musikr.fs.FSUpdate
import org.oxycblt.musikr.fs.mediastore.MediaStore
import org.oxycblt.musikr.fs.saf.SAF
import org.oxycblt.musikr.fs.direct.DirectFS
import timber.log.Timber as L

class IndexingHolder
private constructor(
    private val workerContext: Context,
    private val foregroundListener: ForegroundListener,
    private val playbackManager: PlaybackStateManager,
    private val musicRepository: MusicRepository,
    private val musicSettings: MusicSettings,
    private val imageLoader: ImageLoader,
    private val rootGate: org.oxycblt.auxio.headunit.root.RootStateHolder,
) :
    MusicRepository.IndexingWorker,
    MusicRepository.IndexingListener,
    MusicRepository.UpdateListener,
    MusicSettings.Listener {
    class Factory
    @Inject
    constructor(
        private val playbackManager: PlaybackStateManager,
        private val musicRepository: MusicRepository,
        private val musicSettings: MusicSettings,
        private val imageLoader: ImageLoader,
        private val rootGate: org.oxycblt.auxio.headunit.root.RootStateHolder,
    ) {
        fun create(context: Context, listener: ForegroundListener) =
            IndexingHolder(
                context,
                listener,
                playbackManager,
                musicRepository,
                musicSettings,
                imageLoader,
                rootGate,
            )
    }

    private val indexJob = Job()
    private val indexScope = CoroutineScope(indexJob + Dispatchers.IO)
    private var currentIndexJob: Job? = null
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

    fun attach() {
        musicSettings.registerListener(this)
        musicRepository.addUpdateListener(this)
        musicRepository.addIndexingListener(this)
        musicRepository.registerWorker(this)
    }

    fun release() {
        startupJob?.cancel()
        stopTracking()
        musicRepository.unregisterWorker(this)
        musicRepository.removeIndexingListener(this)
        musicRepository.removeUpdateListener(this)
        musicSettings.unregisterListener(this)
    }

    @Synchronized
    fun start() {
        if (startupJob?.isActive == true) {
            L.d("Startup library load already running; ignoring duplicate start")
            return
        }
        startupJob =
            indexScope.launch {
                if (org.oxycblt.auxio.BuildConfig.TOPWAY_COMPAT_FLAVOR) {
                    rootGate.probeSync()
                }
                musicRepository.startup(this@IndexingHolder)
            }
    }

    fun createNotification(post: (ForegroundServiceNotification?) -> Unit) {
        val state = musicRepository.indexingState
        if (state is IndexingState.Indexing) {
            val changed = indexingNotification.updateIndexingState(state.progress)
            if (changed) {
                post(indexingNotification)
            }
        } else if (musicSettings.shouldBeObserving) {
            L.d("Exiting foreground")
            post(observingNotification)
        } else {
            post(null)
        }
    }

    @Synchronized
    override fun requestIndex(withCache: Boolean) {
        if (currentIndexJob?.isActive == true) {
            L.i("Ignoring duplicate indexing request while scan is running [cache=$withCache]")
            return
        }
        L.i("Starting new indexing job [cache=$withCache]")
        currentIndexJob =
            indexScope.launch {
                try {
                    musicRepository.index(this@IndexingHolder, withCache)
                } finally {
                    synchronized(this@IndexingHolder) { currentIndexJob = null }
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
        val library = musicRepository.library ?: return
        L.d("Music changed, updating shared objects")
        if (trackingJob == null) {
            startTracking()
        }
        imageLoader.memoryCache?.clear()
        playbackManager.toSavedState()?.let { savedState ->
            playbackManager.applySavedState(
                savedState.copy(
                    parent =
                        savedState.parent?.let { musicRepository.find(it.uid) as? MusicParent? },
                    heap = savedState.heap.map { song -> song?.let { library.findSong(it.uid) } },
                ),
                true,
            )
        }
    }

    private fun startTracking() {
        stopTracking()
        val fs =
            when (musicSettings.locationMode) {
                LocationMode.MEDIA_STORE ->
                    MediaStore.from(workerContext, musicSettings.mediaStoreQuery)
                LocationMode.SAF -> SAF.from(workerContext, musicSettings.safQuery)
                LocationMode.DIRECT_FS ->
                    org.oxycblt.musikr.fs.direct.DirectFS(musicSettings.safQuery.source, rootGate)
            }
        trackingJob =
            indexScope.launch {
                fs.track().collect { update ->
                    if (update is FSUpdate.LocationChanged) {
                        val location = update.location
                        if (location != null && !location.path.volume.isAccessible()) {
                            L.i("Source became inaccessible (unmounted?): ${location.uri}")
                            cancelCurrentIndex()
                            return@collect
                        }
                    }

                    if (musicRepository.library == null) {
                        L.i("Ignoring storage change before cached/startup library is available")
                    } else {
                        L.i("Storage change observed; refreshing library with cache")
                        requestIndex(true)
                    }
                }
            }
    }

    private fun stopTracking() {
        trackingJob?.cancel()
        trackingJob = null
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
        startTracking()
        musicRepository.requestIndex(true)
    }

    override fun onIndexingSettingChanged() {
        super.onIndexingSettingChanged()
        musicRepository.requestIndex(true)
    }

    override fun onObservingChanged() {
        super.onObservingChanged()
        if (musicRepository.indexingState == null) {
            L.d("Not loading, updating idle session")
            foregroundListener.updateForeground(ForegroundListener.Change.INDEXER)
        }
    }

    private fun PowerManager.WakeLock.acquireSafe() {
        if (!isHeld) {
            L.d("Acquiring wake lock")
            acquire(WAKELOCK_TIMEOUT_MS)
        }
    }

    private fun PowerManager.WakeLock.releaseSafe() {
        if (isHeld) {
            L.d("Releasing wake lock")
            release()
        }
    }

    companion object {
        const val WAKELOCK_TIMEOUT_MS = 60 * 1000L
    }
}
