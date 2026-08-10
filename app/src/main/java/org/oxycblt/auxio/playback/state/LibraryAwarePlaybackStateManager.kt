/*
 * Copyright (c) 2026 Auxio Project
 * LibraryAwarePlaybackStateManager.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.state

import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject
import org.oxycblt.auxio.list.adapter.UpdateInstructions
import org.oxycblt.auxio.music.MusicRepository
import org.oxycblt.auxio.playback.persist.QueueWindow
import org.oxycblt.musikr.Library
import org.oxycblt.musikr.MusicParent
import org.oxycblt.musikr.Song

/**
 * Presentation bridge that keeps playback metadata aligned with the latest library generation.
 *
 * ExoPlayer intentionally retains the Song objects used to build its MediaItems. With staged LEAN
 * -> FULL enrichment those objects can pre-date artwork, ReplayGain, or richer relationships even
 * though the canonical library now contains an enriched Song with the same UID. Re-applying the
 * complete player queue merely to update presentation metadata would be disruptive, so this wrapper
 * projects the delegate queue through the current library and emits a metadata-only mapping update
 * whenever the device library generation changes.
 */
class LibraryAwarePlaybackStateManager
@Inject
constructor(
    private val delegate: PlaybackStateManagerImpl,
    private val musicRepository: MusicRepository,
) : PlaybackStateManager by delegate, MusicRepository.UpdateListener {
    private val listeners = CopyOnWriteArrayList<PlaybackStateManager.Listener>()

    @Volatile private var initialized = false
    @Volatile private var cachedLibrary: Library? = null
    @Volatile private var cachedSourceQueue: List<Song>? = null
    @Volatile private var cachedProjectedQueue: List<Song> = emptyList()

    private val bridge =
        object : PlaybackStateManager.Listener {
            override fun onIndexMoved(index: Int) {
                listeners.forEach { it.onIndexMoved(index) }
            }

            override fun onQueueChanged(queue: List<Song>, index: Int, change: QueueChange) {
                invalidateProjection()
                listeners.forEach { it.onQueueChanged(projectQueue(queue), index, change) }
            }

            override fun onQueueReordered(
                queue: List<Song>,
                index: Int,
                isShuffled: Boolean,
            ) {
                invalidateProjection()
                listeners.forEach {
                    it.onQueueReordered(projectQueue(queue), index, isShuffled)
                }
            }

            override fun onNewPlayback(
                parent: MusicParent?,
                queue: List<Song>,
                index: Int,
                isShuffled: Boolean,
            ) {
                initialized = true
                invalidateProjection()
                listeners.forEach {
                    it.onNewPlayback(projectParent(parent), projectQueue(queue), index, isShuffled)
                }
            }

            override fun onProgressionChanged(progression: Progression) {
                listeners.forEach { it.onProgressionChanged(progression) }
            }

            override fun onRawPlaybackMetadataChanged(metadata: RawPlaybackMetadata?) {
                listeners.forEach { it.onRawPlaybackMetadataChanged(metadata) }
            }

            override fun onQueueWindowChanged(window: QueueWindow?) {
                initialized = true
                listeners.forEach { it.onQueueWindowChanged(window) }
            }

            override fun onRestoreOutcomeChanged(outcome: RestoreOutcome) {
                listeners.forEach { it.onRestoreOutcomeChanged(outcome) }
            }

            override fun onRepeatModeChanged(repeatMode: RepeatMode) {
                listeners.forEach { it.onRepeatModeChanged(repeatMode) }
            }

            override fun onAudioSessionIdChanged(audioSessionId: Int) {
                listeners.forEach { it.onAudioSessionIdChanged(audioSessionId) }
            }

            override fun onSessionEnded() {
                listeners.forEach { it.onSessionEnded() }
            }
        }

    init {
        delegate.addListener(bridge)
        musicRepository.addUpdateListener(this)
    }

    override val queue: List<Song>
        get() = projectQueue(delegate.queue)

    override val currentSong: Song?
        get() = queue.getOrNull(delegate.index)

    override val parent: MusicParent?
        get() = projectParent(delegate.parent)

    override fun addListener(listener: PlaybackStateManager.Listener) {
        listeners += listener
        if (initialized) {
            listener.onNewPlayback(parent, queue, index, isShuffled)
            listener.onProgressionChanged(progression)
            listener.onRepeatModeChanged(repeatMode)
            listener.onRawPlaybackMetadataChanged(rawPlaybackMetadata)
            listener.onQueueWindowChanged(queueWindow)
        }
    }

    override fun removeListener(listener: PlaybackStateManager.Listener) {
        listeners.remove(listener)
    }

    override fun onMusicChanges(changes: MusicRepository.Changes) {
        if (!changes.deviceLibrary) return
        invalidateProjection()
        if (!initialized || delegate.queue.isEmpty()) return

        val refreshedQueue = queue
        val change = QueueChange(QueueChange.Type.MAPPING, UpdateInstructions.Diff)
        listeners.forEach { listener ->
            // Mapping refresh updates queue/list artwork without changing logical playback state.
            listener.onQueueChanged(refreshedQueue, index, change)
            // Current-track consumers such as MediaSession update their metadata on IndexMoved.
            listener.onIndexMoved(index)
        }
    }

    @Synchronized
    private fun projectQueue(source: List<Song>): List<Song> {
        val library = musicRepository.library ?: return source
        if (cachedLibrary === library && cachedSourceQueue === source) return cachedProjectedQueue
        val projected = source.map { song -> library.findSong(song.uid) ?: song }
        cachedLibrary = library
        cachedSourceQueue = source
        cachedProjectedQueue = projected
        return projected
    }

    private fun projectParent(parent: MusicParent?): MusicParent? {
        parent ?: return null
        return musicRepository.find(parent.uid) as? MusicParent ?: parent
    }

    @Synchronized
    private fun invalidateProjection() {
        cachedLibrary = null
        cachedSourceQueue = null
        cachedProjectedQueue = emptyList()
    }
}
