/*
 * Copyright (c) 2024 Auxio Project
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
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.oxycblt.auxio.headunit.topway.TopwaySourcePolicy
import org.oxycblt.auxio.image.covers.SettingCovers
import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.auxio.music.playlist.StoredPlaylists
import org.oxycblt.musikr.Config
import org.oxycblt.musikr.IndexingProgress
import org.oxycblt.musikr.Interpretation
import org.oxycblt.musikr.Library
import org.oxycblt.musikr.Music
import org.oxycblt.musikr.Musikr
import org.oxycblt.musikr.MutableLibrary
import org.oxycblt.musikr.Song
import org.oxycblt.musikr.Storage
import org.oxycblt.musikr.cache.MutableCache
import org.oxycblt.musikr.cache.WriteOnlyMutableCache
import org.oxycblt.musikr.fs.FS
import org.oxycblt.musikr.fs.FSUpdate
import org.oxycblt.musikr.fs.mediastore.MediaStore
import org.oxycblt.musikr.fs.saf.SAF
import org.oxycblt.musikr.interpretation.Naming
import org.oxycblt.musikr.interpretation.Separators
import org.oxycblt.musikr.playlist.Playlist
import timber.log.Timber as L

/**
 * A central repository for accessing and managing the music library.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
interface MusicRepository {
    /** The currently loaded [Library], or null if it hasn't been loaded yet. */
    val library: Library?

    /** The current [IndexingState] of the library, or null if it's not being indexed. */
    val indexingState: IndexingState?

    /**
     * Add a listener to be notified when the library is updated.
     *
     * @param listener The listener to add.
     */
    fun addUpdateListener(listener: UpdateListener)

    /**
     * Remove a listener from being notified when the library is updated.
     *
     * @param listener The listener to remove.
     */
    fun removeUpdateListener(listener: UpdateListener)

    /**
     * Add a listener to be notified when the indexing state changes.
     *
     * @param listener The listener to add.
     */
    fun addIndexingListener(listener: IndexingListener)

    /**
     * Remove a listener from being notified when the indexing state changes.
     *
     * @param listener The listener to remove.
     */
    fun removeIndexingListener(listener: IndexingListener)

    /**
     * Start the music repository. This will load the library from cache and start an index if
     * necessary.
     */
    suspend fun startup(worker: IndexingWorker)

    /**
     * Request an index of the library.
     *
     * @param worker The worker to use for indexing.
     * @param withCache Whether to use the cache for indexing.
     */
    suspend fun index(worker: IndexingWorker, withCache: Boolean)

    /**
     * Register an [IndexingWorker] to be used for indexing the library.
     *
     * @param worker The worker to register.
     */
    fun registerWorker(worker: IndexingWorker)

    /**
     * Unregister an [IndexingWorker].
     *
     * @param worker The worker to unregister.
     */
    fun unregisterWorker(worker: IndexingWorker)

    /**
     * Generically search for the [Music] associated with the given [Music.UID]. Note that this
     * method is much slower that type-specific find implementations, so this should only be used if
     * the type of music being searched for is entirely unknown.
     *
     * @param uid The [Music.UID] to search for.
     * @return The expected [Music] information, or null if it could not be found.
     */
    fun find(uid: Music.UID): Music?

    /**
     * Create a new [Playlist] of the given [Song]s.
     *
     * @param name The name of the new [Playlist].
     * @param songs The songs to populate the new [Playlist] with.
     */
    suspend fun createPlaylist(name: String, songs: List<Song>)

    /**
     * Rename a [Playlist].
     *
     * @param playlist The [Playlist] to rename.
     * @param name The name of the new [Playlist].
     */
    suspend fun renamePlaylist(playlist: Playlist, name: String)

    /**
     * Delete a [Playlist].
     *
     * @param playlist The [Playlist] to delete.
     */
    suspend fun deletePlaylist(playlist: Playlist)

    /**
     * Add [Song]s to a [Playlist].
     *
     * @param playlist The [Playlist] to add songs to.
     * @param songs The songs to add.
     */
    suspend fun addToPlaylist(playlist: Playlist, songs: List<Song>)

    /**
     * Remove a [Song] from a [Playlist].
     *
     * @param playlist The [Playlist] to remove the song from.
     * @param index The index of the song to remove.
     */
    suspend fun removeFromPlaylist(playlist: Playlist, index: Int)

    /**
     * Move a [Song] within a [Playlist].
     *
     * @param playlist The [Playlist] to move the song within.
     * @param from The current index of the song.
     * @param to The new index of the song.
     */
    suspend fun moveInPlaylist(playlist: Playlist, from: Int, to: Int)

    /** A listener for when the library is updated. */
    interface UpdateListener {
        /**
         * Called when the library is updated.
         *
         * @param changes Information about what changed in the library.
         */
        fun onMusicChanges(changes: Changes)
    }

    /** A listener for when the indexing state changes. */
    interface IndexingListener {
        /** Called when the indexing state changes. */
        fun onIndexingStateChanged()
    }

    /** A worker for indexing the library. */
    interface IndexingWorker {
        /**
         * Request an index of the library.
         *
         * @param withCache Whether to use the cache for indexing.
         */
        fun requestIndex(withCache: Boolean)
    }

    /**
     * Information about what changed in the library.
     *
     * @param deviceLibrary Whether the device-local music library changed.
     * @param userLibrary Whether the user-created music library (playlists) changed.
     */
    data class Changes(val deviceLibrary: Boolean, val userLibrary: Boolean)
}

/** Represents the current state of indexing the library. */
sealed interface IndexingState {
    /** Currently indexing the library. */
    data class Indexing(val progress: IndexingProgress) : IndexingState

    /** Indexing has completed. */
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
    private val rootGate: org.oxycblt.auxio.headunit.root.RootStateHolder,
) : MusicRepository {
    private val updateListeners = mutableListOf<MusicRepository.UpdateListener>()
    private val indexingListeners = mutableListOf<MusicRepository.IndexingListener>()
    @Volatile private var indexingWorker: IndexingWorker? = null

    @Volatile override var library: MutableLibrary? = null
    @Volatile private var previousCompletedState: IndexingState.Completed? = null
    @Volatile private var currentIndexingState: IndexingState? = null
    override val indexingState: IndexingState?
        get() = currentIndexingState ?: previousCompletedState

    override fun addUpdateListener(listener: MusicRepository.UpdateListener) {
        updateListeners.add(listener)
    }

    override fun removeUpdateListener(listener: MusicRepository.UpdateListener) {
        updateListeners.remove(listener)
    }

    override fun addIndexingListener(listener: MusicRepository.IndexingListener) {
        indexingListeners.add(listener)
    }

    override fun removeIndexingListener(listener: MusicRepository.IndexingListener) {
        indexingListeners.remove(listener)
    }

    override fun registerWorker(worker: MusicRepository.IndexingWorker) {
        indexingWorker = worker
    }

    override fun unregisterWorker(worker: MusicRepository.IndexingWorker) {
        if (indexingWorker == worker) {
            indexingWorker = null
        }
    }

    override suspend fun startup(worker: MusicRepository.IndexingWorker) {
        if (library != null) {
            return
        }

        L.i("Starting up MusicRepository")
        library = loadCachedLibrary()
        dispatchLibraryChange(device = true, user = true)

        if (musicSettings.shouldBeIndexing) {
            index(worker, withCache = true)
        }
    }

    override fun find(uid: Music.UID): Music? = library?.find(uid)

    override suspend fun createPlaylist(name: String, songs: List<Song>) {
        storedPlaylists.create(name, songs)
        library?.let { lib ->
            L.d("Playlist created, refreshing library")
            val playlists = storedPlaylists.read()
            lib.updatePlaylists(playlists)
            dispatchLibraryChange(device = false, user = true)
        }
    }

    override suspend fun renamePlaylist(playlist: Playlist, name: String) {
        storedPlaylists.rename(playlist.uid, name)
        library?.let { lib ->
            L.d("Playlist renamed, refreshing library")
            val playlists = storedPlaylists.read()
            lib.updatePlaylists(playlists)
            dispatchLibraryChange(device = false, user = true)
        }
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        storedPlaylists.delete(playlist.uid)
        library?.let { lib ->
            L.d("Playlist deleted, refreshing library")
            val playlists = storedPlaylists.read()
            lib.updatePlaylists(playlists)
            dispatchLibraryChange(device = false, user = true)
        }
    }

    override suspend fun addToPlaylist(playlist: Playlist, songs: List<Song>) {
        storedPlaylists.add(playlist.uid, songs)
        library?.let { lib ->
            L.d("Songs added to playlist, refreshing library")
            val playlists = storedPlaylists.read()
            lib.updatePlaylists(playlists)
            dispatchLibraryChange(device = false, user = true)
        }
    }

    override suspend fun removeFromPlaylist(playlist: Playlist, index: Int) {
        storedPlaylists.remove(playlist.uid, index)
        library?.let { lib ->
            L.d("Song removed from playlist, refreshing library")
            val playlists = storedPlaylists.read()
            lib.updatePlaylists(playlists)
            dispatchLibraryChange(device = false, user = true)
        }
    }

    override suspend fun removeFromPlaylist(playlist: Playlist, songs: List<Song>) {
        storedPlaylists.remove(playlist.uid, songs)
        library?.let { lib ->
            L.d("Songs removed from playlist, refreshing library")
            val playlists = storedPlaylists.read()
            lib.updatePlaylists(playlists)
            dispatchLibraryChange(device = false, user = true)
        }
    }

    override suspend fun moveInPlaylist(playlist: Playlist, from: Int, to: Int) {
        storedPlaylists.move(playlist.uid, from, to)
        library?.let { lib ->
            L.d("Song moved in playlist, refreshing library")
            val playlists = storedPlaylists.read()
            lib.updatePlaylists(playlists)
            dispatchLibraryChange(device = false, user = true)
        }
    }

    override suspend fun index(worker: MusicRepository.IndexingWorker, withCache: Boolean) {
        if (currentIndexingState != null) {
            L.d("Ignoring indexing request: already indexing")
            return
        }

        if (org.oxycblt.auxio.BuildConfig.DEBUG) {
            val twStorageSwitch =
                try {
                    val systemProperties = Class.forName("android.os.SystemProperties")
                    val get = systemProperties.getMethod("get", String::class.java)
                    get.invoke(null, "persist.tw.storage.switch") as? String
                } catch (e: Exception) {
                    null
                }
            if (!twStorageSwitch.isNullOrEmpty()) {
                L.d("TS18 diagnostic: persist.tw.storage.switch=$twStorageSwitch")
            }
        }

        val currentRevision = musicSettings.revision
        val newRevision = currentRevision?.takeIf { withCache } ?: UUID.randomUUID()
        val config =
            createConfig(newRevision, if (withCache) cache else WriteOnlyMutableCache(cache))

        // Check accessibility before starting
        val locations =
            when (musicSettings.locationMode) {
                LocationMode.SAF, LocationMode.DIRECT_FS -> musicSettings.safQuery.source
                LocationMode.MEDIA_STORE ->
                    emptyList() // MediaStore is always "accessible" as a provider
            }

        if (locations.any { !it.path.volume.isAccessible() }) {
            L.w("One or more music sources are inaccessible. Aborting scan to preserve cache.")
            // Mark last scan failed but keep library state USABLE if it was,
            // or RECOVERY if it needs a scan.
            musicSettings.lastScanFailed = true
            emitIndexingCompletion(Exception("Music source inaccessible"))
            return
        }

        L.d("Running index...")
        val start = System.currentTimeMillis()
        // When ts18SystemSourceFilter is enabled, the path restriction is now applied at the
        // SQL level in the MediaStore query (useDefaultSystemFilter). For SAF mode, the
        // FilteredFS pathKeywords still serve as the filtering mechanism since there is no
        // SQL query to augment.
        val pathKeywords =
            if (
                musicSettings.ts18SystemSourceFilter &&
                    (musicSettings.locationMode == LocationMode.SAF || musicSettings.locationMode == LocationMode.DIRECT_FS)
            ) {
                TopwaySourcePolicy.SYSTEM_SOURCE_PATH_KEYWORDS
            } else {
                emptyList()
            }
        val result =
            Musikr.new(context, config, TopwaySourcePolicy.NOISY_DIRS, pathKeywords, rootGate)
                .run(::emitIndexingProgress)
        L.d("Index finished in ${System.currentTimeMillis() - start}ms")

        // Final accessibility check before committing empty state
        if (result.library.songs.isEmpty()) {
            if (locations.any { !it.path.volume.isAccessible() }) {
                L.w("Scan returned empty but sources became inaccessible. Preserving cache.")
                musicSettings.lastScanFailed = true
                emitIndexingCompletion(Exception("Source became inaccessible during scan"))
                return
            }
        }

        // Music loading completed, update the revision right now so we re-use this work
        // later.
        L.d("Revisioning from $currentRevision -> $newRevision")
        musicSettings.revision = newRevision
        // Deliver the library to the rest of the app
        // This will more or less block until all required item translation and
        // cleanup finishes.
        L.d("Emitting new library")
        emitLibrary(result.library)
        // Clean up old data that is now impossible for the app to be using.
        L.d("Cleanup")
        result.cleanup()
        // Finish up loading.
        musicSettings.libraryState =
            if (result.library.songs.isEmpty()) LibraryState.EMPTY else LibraryState.USABLE
        musicSettings.lastScanFailed = false
        L.i("Indexing complete [state=${musicSettings.libraryState}]")
        emitIndexingCompletion(null)
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
        )
    }

    private suspend fun createConfig(revision: UUID, cache: MutableCache): Config {
        val configStart = System.currentTimeMillis()
        val separators = Separators.from(musicSettings.separators)
        val nameFactory =
            if (musicSettings.intelligentSorting) {
                Naming.intelligent()
            } else {
                Naming.simple()
            }
        val covers = settingCovers.mutate(context, revision)
        L.d("Config: covers init ${System.currentTimeMillis() - configStart}ms")
        val fsStart = System.currentTimeMillis()
        val fs =
            when (musicSettings.locationMode) {
                LocationMode.SAF -> SAF.from(context, musicSettings.safQuery)
                LocationMode.DIRECT_FS -> org.oxycblt.musikr.fs.direct.DirectFS(musicSettings.safQuery.source, rootGate)
                LocationMode.MEDIA_STORE -> {
                    // Merge TS18 system source filter into the MediaStore query so the SQL
                    // WHERE clause limits rows before cursor iteration.
                    val query =
                        musicSettings.mediaStoreQuery.copy(
                            useDefaultSystemFilter = musicSettings.ts18SystemSourceFilter
                        )
                    MediaStore.from(context, query)
                }
            }
        L.d(
            "Config: FS construction ${System.currentTimeMillis() - fsStart}ms [mode=${musicSettings.locationMode}]"
        )
        return Config(
            fs,
            Storage(cache, covers, storedPlaylists),
            Interpretation(nameFactory, separators),
        )
    }

    private suspend fun emitIndexingProgress(progress: IndexingProgress) {
        yield()
        synchronized(this) {
            currentIndexingState = IndexingState.Indexing(progress)
            for (listener in indexingListeners) {
                listener.onIndexingStateChanged()
            }
        }
    }

    private suspend fun emitLibrary(newLibrary: MutableLibrary) {
        val emitStart = System.currentTimeMillis()
        val deviceLibraryChanged: Boolean
        val userLibraryChanged: Boolean
        // We want to make sure that all reads and writes are synchronized due to the sheer
        // amount of consumers of MusicRepository.
        synchronized(this) {
            // It's possible that this reload might have changed nothing, so make sure that
            // hasn't happened before dispatching a change to all consumers.

            // This is an old compat shim back when device library and user library were different
            // thinks. For the sake of avoiding drastic changes, it sticks around.
            // TODO: Remove this once you start work on kindred.
            deviceLibraryChanged =
                this.library?.songs != newLibrary.songs ||
                    this.library?.albums != newLibrary.albums ||
                    this.library?.artists != newLibrary.artists ||
                    this.library?.genres != newLibrary.genres
            userLibraryChanged = this.library?.playlists != newLibrary.playlists
            if (!deviceLibraryChanged && !userLibraryChanged) {
                L.d("Library has not changed, skipping update")
                return
            }

            this.library = newLibrary
        }

        // Consumers expect their updates to be on the main thread (notably PlaybackService),
        // so switch to it.
        withContext(Dispatchers.Main) {
            dispatchLibraryChange(deviceLibraryChanged, userLibraryChanged)
        }
        L.d("emitLibrary completed in ${System.currentTimeMillis() - emitStart}ms")
    }

    private suspend fun emitIndexingCompletion(error: Exception?) {
        yield()
        synchronized(this) {
            previousCompletedState = IndexingState.Completed(error)
            currentIndexingState = null
            L.d("Dispatching completion state [error=$error]")
            for (listener in indexingListeners) {
                listener.onIndexingStateChanged()
            }
        }
    }

    @Synchronized
    private fun dispatchLibraryChange(device: Boolean, user: Boolean) {
        val changes = MusicRepository.Changes(device, user)
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
