#!/usr/bin/env python3
from pathlib import Path


def replace(path: str, old: str, new: str, count: int = 1) -> None:
    p = Path(path)
    text = p.read_text()
    if old not in text:
        raise SystemExit(f"missing patch anchor in {path}: {old[:220]!r}")
    p.write_text(text.replace(old, new, count))


# File-system adapters report source-local failures without collapsing successful sibling sources.
replace(
    'musikr/src/main/java/org/oxycblt/musikr/fs/SourceSnapshot.kt',
    '''    /** Return an equivalent file system restricted to [sourceKeys]. */
    fun selectSources(sourceKeys: Set<String>): FS
}''',
    '''    /** Return an equivalent file system restricted to [sourceKeys]. */
    fun selectSources(sourceKeys: Set<String>): FS

    /** Drain source-local failures collected by the most recent exploration. */
    fun drainSourceFailures(): Map<String, String> = emptyMap()
}''',
)

scan = 'musikr/src/main/java/org/oxycblt/musikr/cache/IncrementalScan.kt'
replace(
    scan,
    '''    val unavailableSources: Set<String>,
    val changedRows: Int,''',
    '''    val unavailableSources: Set<String>,
    val failedSources: Map<String, String>,
    val changedRows: Int,''',
)
replace(
    scan,
    '''    /** Stage changed metadata; returns false when no incremental scan is active. */
    suspend fun stage(cachedFile: CachedFile): Boolean

    suspend fun commitScan(): IncrementalScanCommit''',
    '''    /** Stage changed metadata; returns false when no incremental scan is active. */
    suspend fun stage(cachedFile: CachedFile): Boolean

    /** Mark one source failed while allowing sibling source generations to commit. */
    suspend fun markSourceFailed(sourceKey: String, detail: String)

    suspend fun commitScan(): IncrementalScanCommit''',
)

# Room supplies a bounded compatibility snapshot containing only available committed generations
# plus legacy rows not yet claimed by the incremental ledger.
db = 'musikr/src/main/java/org/oxycblt/musikr/cache/db/IncrementalDatabase.kt'
replace(
    db,
    '''    suspend fun committedCachedPage(
        sourceKeys: Set<String>,
        limit: Int,
        offset: Int,
    ): List<CommittedCachedRow>

    @Query("SELECT * FROM IndexedUriStateData''',
    '''    suspend fun committedCachedPage(
        sourceKeys: Set<String>,
        limit: Int,
        offset: Int,
    ): List<CommittedCachedRow>

    @Query(
        "SELECT * FROM (" +
            "SELECT cache.*, song.sourceKey AS sourceKey, song.displayPath AS committedDisplayPath, song.sizeBytes AS committedSizeBytes, source.rootUri AS committedSourceUri, source.rootPath AS committedRootPath FROM CachedFileData cache INNER JOIN IndexedSongData song ON song.uri = cache.uri INNER JOIN SourceLedgerData source ON source.sourceKey = song.sourceKey AND source.lastCommittedGeneration = song.generation WHERE source.available = 1 " +
            "UNION ALL SELECT cache.*, 'legacy:' || cache.uri AS sourceKey, cache.uri AS committedDisplayPath, 0 AS committedSizeBytes, NULL AS committedSourceUri, NULL AS committedRootPath FROM CachedFileData cache WHERE NOT EXISTS (SELECT 1 FROM IndexedUriStateData state WHERE state.uri = cache.uri)) rows ORDER BY uri LIMIT :limit OFFSET :offset"
    )
    suspend fun compatibilityCachedPage(limit: Int, offset: Int): List<CommittedCachedRow>

    @Query("SELECT * FROM IndexedUriStateData''',
)

store = 'musikr/src/main/java/org/oxycblt/musikr/cache/db/IncrementalScanStore.kt'
replace(store, 'import java.util.UUID\n', 'import java.util.UUID\nimport java.util.concurrent.ConcurrentHashMap\n')
replace(
    store,
    '''    @Volatile private var currentPlan: IncrementalScanPlan? = null

    override fun activePlan(): IncrementalScanPlan? = currentPlan''',
    '''    @Volatile private var currentPlan: IncrementalScanPlan? = null
    private val sourceFailures = ConcurrentHashMap<String, String>()

    override fun activePlan(): IncrementalScanPlan? = currentPlan''',
)
replace(
    store,
    '''        check(currentPlan == null) { "An incremental scan is already active" }
        val now = System.currentTimeMillis()''',
    '''        check(currentPlan == null) { "An incremental scan is already active" }
        sourceFailures.clear()
        val now = System.currentTimeMillis()''',
)
replace(
    store,
    '''    override fun reusedCachedFiles(sourceKeys: Set<String>): Flow<CachedFile> = flow {
        if (sourceKeys.isEmpty()) return@flow
        var offset = 0
        while (true) {
            val page = dao.committedCachedPage(sourceKeys, PAGE_SIZE, offset)
            if (page.isEmpty()) break
            for (row in page) emit(row.toCachedFile())
            if (page.size < PAGE_SIZE) break
            offset += page.size
        }
    }

    override suspend fun commitScan(): IncrementalScanCommit {''',
    '''    override fun reusedCachedFiles(sourceKeys: Set<String>): Flow<CachedFile> = flow {
        if (sourceKeys.isEmpty()) return@flow
        var offset = 0
        while (true) {
            val page = dao.committedCachedPage(sourceKeys, PAGE_SIZE, offset)
            if (page.isEmpty()) break
            for (row in page) emit(row.toCachedFile())
            if (page.size < PAGE_SIZE) break
            offset += page.size
        }
    }

    internal fun compatibilityCachedFiles(): Flow<CachedFile> = flow {
        var offset = 0
        while (true) {
            val page = dao.compatibilityCachedPage(PAGE_SIZE, offset)
            if (page.isEmpty()) break
            for (row in page) emit(row.toCachedFile())
            if (page.size < PAGE_SIZE) break
            offset += page.size
        }
    }

    override suspend fun markSourceFailed(sourceKey: String, detail: String) {
        val plan = currentPlan ?: return
        if (sourceKey in plan.scanSourceKeys) {
            sourceFailures[sourceKey] = detail.take(MAX_ERROR_LENGTH)
        }
    }

    override suspend fun commitScan(): IncrementalScanCommit {''',
)
replace(
    store,
    '''                for (snapshot in plan.scanSources) {
                    val ledger = requireNotNull(dao.sourceLedger(snapshot.sourceKey))
                    val generation = requireNotNull(ledger.pendingGeneration)
                    val sourceChangedRows = dao.pendingCount(plan.scanId, snapshot.sourceKey)''',
    '''                for (snapshot in plan.scanSources) {
                    val ledger = requireNotNull(dao.sourceLedger(snapshot.sourceKey))
                    val generation = requireNotNull(ledger.pendingGeneration)
                    val sourceFailure = sourceFailures[snapshot.sourceKey]
                    if (sourceFailure != null) {
                        dao.deletePendingForSource(snapshot.sourceKey)
                        dao.deleteSeenForSource(snapshot.sourceKey)
                        dao.upsertSourceLedger(
                            ledger.copy(pendingGeneration = null, incomplete = true)
                        )
                        dao.completeGeneration(
                            plan.scanId,
                            snapshot.sourceKey,
                            STATE_FAILED,
                            System.currentTimeMillis(),
                            sourceFailure,
                        )
                        continue
                    }
                    val sourceChangedRows = dao.pendingCount(plan.scanId, snapshot.sourceKey)''',
)
replace(
    store,
    '''            committedSuccessfully = true
        } finally {
            if (committedSuccessfully) currentPlan = null
        }
        return IncrementalScanCommit(
            scanId = plan.scanId,
            committedSources = committed,
            reusedSources = plan.reuseSourceKeys,
            unavailableSources = plan.unavailableSourceKeys,
            changedRows = changedRows,''',
    '''            committedSuccessfully = true
        } finally {
            if (committedSuccessfully) {
                currentPlan = null
            }
        }
        val failed = sourceFailures.toMap()
        sourceFailures.clear()
        return IncrementalScanCommit(
            scanId = plan.scanId,
            committedSources = committed,
            reusedSources = plan.reuseSourceKeys,
            unavailableSources = plan.unavailableSourceKeys,
            failedSources = failed,
            changedRows = changedRows,''',
)
replace(
    store,
    '''        } finally {
            currentPlan = null
        }
    }

    override suspend fun invalidateSource''',
    '''        } finally {
            currentPlan = null
            sourceFailures.clear()
        }
    }

    override suspend fun invalidateSource''',
)

# Compatibility hydration now excludes unavailable or uncommitted generations.
dbcache = 'musikr/src/main/java/org/oxycblt/musikr/cache/db/DBCache.kt'
replace(dbcache, 'import org.oxycblt.musikr.cache.StartupSummaryRow\n', 'import kotlinx.coroutines.flow.toList\nimport org.oxycblt.musikr.cache.StartupSummaryRow\n')
replace(
    dbcache,
    '''    override suspend fun snapshot(): List<CachedFile> {
        // Explicit compatibility bridge for rich screens not yet migrated to projections.
        val result = mutableListOf<CachedFile>()
        var offset = 0
        while (true) {
            val page = readDao.selectSongsPage(SNAPSHOT_PAGE_SIZE, offset)
            if (page.isEmpty()) break
            result += page.map { it.toCachedFile(it.toSyntheticFile()) }
            offset += page.size
        }
        return result
    }''',
    '''    override suspend fun snapshot(): List<CachedFile> {
        // Explicit compatibility bridge for rich screens not yet migrated to projections. The
        // source ledger filters unavailable and uncommitted generations before object hydration.
        return incrementalStore.compatibilityCachedFiles().toList()
    }''',
)
replace(dbcache, '        private const val SNAPSHOT_PAGE_SIZE = 256\n', '')

# Exploration records adapter-local failures once enumeration drains. Downstream work may finish for
# successful sources, then the generation store publishes only their source transactions.
explore = 'musikr/src/main/java/org/oxycblt/musikr/pipeline/ExploreStep.kt'
replace(explore, 'import org.oxycblt.musikr.fs.RootGate\n', 'import org.oxycblt.musikr.fs.RootGate\nimport org.oxycblt.musikr.fs.SourceAwareFS\n')
replace(
    explore,
    '''        val files = Channel<File>(PipelinePolicy.BUFFER_CAPACITY)
        val filesTask = filteredFs.explore(files)

        val classified''',
    '''        val files = Channel<File>(PipelinePolicy.BUFFER_CAPACITY)
        val filesTask = filteredFs.explore(files)
        val sourceFailureTask =
            scope.tryAsync(Dispatchers.IO) {
                filesTask.await().getOrThrow()
                val incremental = storage.cache as? IncrementalCache
                val failures = (fs as? SourceAwareFS)?.drainSourceFailures().orEmpty()
                for ((sourceKey, detail) in failures) {
                    incremental?.markSourceFailed(sourceKey, detail)
                }
            }

        val classified''',
)
replace(
    explore,
    '''        return scope.merge(filesTask, classifiedTask, exploredTask, playlistsTask, mergeTask)''',
    '''        return scope.merge(
            filesTask,
            sourceFailureTask,
            classifiedTask,
            exploredTask,
            playlistsTask,
            mergeTask,
        )''',
)

# DirectFS: distinguish an empty readable folder from an unavailable one and isolate root failures.
direct = 'musikr/src/main/java/org/oxycblt/musikr/fs/direct/DirectFS.kt'
replace(direct, 'import java.util.Locale\n', 'import java.util.Locale\nimport java.util.concurrent.ConcurrentHashMap\n')
replace(
    direct,
    ''') :
    SourceAwareFS {
    override suspend fun sourceSnapshots''',
    ''') :
    SourceAwareFS {
    private val sourceFailures = ConcurrentHashMap<String, String>()

    override suspend fun sourceSnapshots''',
)
replace(
    direct,
    '''    override fun selectSources(sourceKeys: Set<String>): FS =
        DirectFS(roots.filter { SourceIdentity.forLocation(it) in sourceKeys }, rootGate)

    override suspend fun explore''',
    '''    override fun selectSources(sourceKeys: Set<String>): FS =
        DirectFS(roots.filter { SourceIdentity.forLocation(it) in sourceKeys }, rootGate)

    override fun drainSourceFailures(): Map<String, String> =
        sourceFailures.toMap().also { sourceFailures.clear() }

    override suspend fun explore''',
)
replace(
    direct,
    '''                .map { location ->
                    if (location.uri.scheme != "file") {
                        Log.w(TAG, "Skipping non-file DirectFS source: ${location.uri}")
                        return@map CompletableDeferred(Result.success(Unit))
                    }
                    val root = location.uri.path?.let(::JavaFile)
                    if (root == null || !isAllowedRoot(root)) {
                        Log.w(TAG, "Skipping unsafe DirectFS source: ${location.uri}")
                        return@map CompletableDeferred(Result.success(Unit))
                    }
                    exploreDirectoryImpl(root, location.path, null, files, 0)
                }''',
    '''                .map { location ->
                    val sourceKey = SourceIdentity.forLocation(location)
                    if (location.uri.scheme != "file") {
                        val detail = "Unsupported DirectFS URI ${location.uri}"
                        Log.w(TAG, detail)
                        sourceFailures[sourceKey] = detail
                        return@map CompletableDeferred(Result.success(Unit))
                    }
                    val root = location.uri.path?.let(::JavaFile)
                    if (root == null || !isAllowedRoot(root)) {
                        val detail = "Unsafe or missing DirectFS source ${location.uri}"
                        Log.w(TAG, detail)
                        sourceFailures[sourceKey] = detail
                        return@map CompletableDeferred(Result.success(Unit))
                    }
                    tryAsync(Dispatchers.IO) {
                        val result =
                            exploreDirectoryImpl(
                                    root,
                                    location.path,
                                    null,
                                    files,
                                    0,
                                    sourceKey,
                                )
                                .await()
                        result.exceptionOrNull()?.let { error ->
                            sourceFailures[sourceKey] = error.message ?: error.javaClass.simpleName
                        }
                    }
                }''',
)
replace(
    direct,
    '''        files: Channel<File>,
        depth: Int,
    ): Deferred<Result<Unit>> =''',
    '''        files: Channel<File>,
        depth: Int,
        sourceKey: String,
    ): Deferred<Result<Unit>> =''',
)
replace(
    direct,
    '''            val recursive = mutableListOf<Deferred<Result<Unit>>>()
            for (entry in listFilesSafe(directory)) {''',
    '''            val recursive = mutableListOf<Deferred<Result<Unit>>>()
            val entries = listFilesSafe(directory)
            if (entries == null) {
                sourceFailures.putIfAbsent(
                    sourceKey,
                    "DirectFS source became unavailable at ${directory.path}",
                )
                return@tryAsync
            }
            for (entry in entries) {''',
)
replace(
    direct,
    '''                        exploreDirectoryImpl(item, newPath, directoryDeferred, files, depth + 1)''',
    '''                        exploreDirectoryImpl(
                            item,
                            newPath,
                            directoryDeferred,
                            files,
                            depth + 1,
                            sourceKey,
                        )''',
)
replace(direct, '    private fun listFilesSafe(directory: JavaFile): List<DirectEntry> {', '    private fun listFilesSafe(directory: JavaFile): List<DirectEntry>? {')
replace(direct, '        return emptyList()\n    }', '        return null\n    }', 1)
replace(
    direct,
    '''        listFilesSafe(root)
            .asSequence()''',
    '''        listFilesSafe(root)
            .orEmpty()
            .asSequence()''',
)

# SAF: wrap each tree so one provider failure is recorded without cancelling other roots.
saf = 'musikr/src/main/java/org/oxycblt/musikr/fs/saf/SAF.kt'
replace(saf, 'import kotlinx.coroutines.withContext\n', 'import kotlinx.coroutines.withContext\nimport java.util.concurrent.ConcurrentHashMap\n')
replace(
    saf,
    ''') : SourceAwareFS {
    override suspend fun sourceSnapshots''',
    ''') : SourceAwareFS {
    private val sourceFailures = ConcurrentHashMap<String, String>()

    override suspend fun sourceSnapshots''',
)
replace(
    saf,
    '''    override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> = coroutineScope {
        tryAsyncWith(files, Dispatchers.IO) {
            query.source
                .map { location ->
                    exploreDirectoryImpl(
                        location.uri,
                        DocumentsContract.getTreeDocumentId(location.uri),
                        location.path,
                        null,
                        query.exclude.mapTo(mutableSetOf()) { it.path },
                        files,
                    )
                }
                .tryAwaitAll()
        }
    }

    override fun track''',
    '''    override fun drainSourceFailures(): Map<String, String> =
        sourceFailures.toMap().also { sourceFailures.clear() }

    override suspend fun explore(files: Channel<File>): Deferred<Result<Unit>> = coroutineScope {
        tryAsyncWith(files, Dispatchers.IO) {
            query.source
                .map { location ->
                    val sourceKey = SourceIdentity.forLocation(location)
                    tryAsync(Dispatchers.IO) {
                        val result =
                            exploreDirectoryImpl(
                                    location.uri,
                                    DocumentsContract.getTreeDocumentId(location.uri),
                                    location.path,
                                    null,
                                    query.exclude.mapTo(mutableSetOf()) { it.path },
                                    files,
                                )
                                .await()
                        result.exceptionOrNull()?.let { error ->
                            sourceFailures[sourceKey] = error.message ?: error.javaClass.simpleName
                        }
                    }
                }
                .tryAwaitAll()
        }
    }

    override fun track''',
)

# MediaStore records volume-local query errors and continues with healthy volumes.
media = 'musikr/src/main/java/org/oxycblt/musikr/fs/mediastore/MediaStore.kt'
replace(media, 'import androidx.core.database.getStringOrNull\n', 'import androidx.core.database.getStringOrNull\nimport java.util.concurrent.ConcurrentHashMap\n')
replace(
    media,
    ''') : SourceAwareFS {
    private val pathInterpreterFactory''',
    ''') : SourceAwareFS {
    private val pathInterpreterFactory''',
)
replace(
    media,
    '''    private val pathInterpreterFactory = MediaStorePathInterpreter.Factory.from(volumeManager)

    override suspend fun sourceSnapshots''',
    '''    private val pathInterpreterFactory = MediaStorePathInterpreter.Factory.from(volumeManager)
    private val sourceFailures = ConcurrentHashMap<String, String>()

    override suspend fun sourceSnapshots''',
)
replace(
    media,
    '''    override fun selectSources(sourceKeys: Set<String>): FS =
        MediaStore(context, volumeManager, query, sourceKeys)

    @OptIn''',
    '''    override fun selectSources(sourceKeys: Set<String>): FS =
        MediaStore(context, volumeManager, query, sourceKeys)

    override fun drainSourceFailures(): Map<String, String> =
        sourceFailures.toMap().also { sourceFailures.clear() }

    @OptIn''',
)
replace(media, '            var anyVolumeSucceeded = false\n            var lastVolumeError: Exception? = null\n\n', '')
replace(media, '                    anyVolumeSucceeded = true\n', '')
replace(
    media,
    '''                } catch (e: Exception) {
                    lastVolumeError = e
                    android.util.Log.e(TAG, "Failed to query volume: $volumeName", e)
                }
            }
            if (!anyVolumeSucceeded) lastVolumeError?.let { throw it }''',
    '''                } catch (e: Exception) {
                    android.util.Log.e(TAG, "Failed to query volume: $volumeName", e)
                    sourceFailures[sourceKey] = e.message ?: e.javaClass.simpleName
                }
            }''',
)

# Expose failed-source state from Musikr and rebuild the rich compatibility graph from only committed,
# available rows. Fast Start remains projection-backed throughout.
musikr = 'musikr/src/main/java/org/oxycblt/musikr/Musikr.kt'
replace(
    musikr,
    '''interface LibraryResult {
    val library: MutableLibrary

    /** Delete''',
    '''interface LibraryResult {
    val library: MutableLibrary

    val failedSources: Map<String, String>
        get() = emptyMap()

    /** Delete''',
)
replace(
    musikr,
    '''            LibraryResultImpl(config, library)
        } catch''',
    '''            LibraryResultImpl(config, library, commit?.failedSources.orEmpty())
        } catch''',
)
replace(
    musikr,
    '''private class LibraryResultImpl(private val config: Config, override val library: MutableLibrary) :
    LibraryResult {''',
    '''private class LibraryResultImpl(
    private val config: Config,
    override val library: MutableLibrary,
    override val failedSources: Map<String, String>,
) : LibraryResult {''',
)

repo = 'app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt'
replace(
    repo,
    '''                musicSettings.revision = newRevision
                emitLibrary(result.library)
                result.cleanup()
                val isEmpty = result.library.songs.isEmpty()''',
    '''                musicSettings.revision = newRevision
                val publishedLibrary =
                    if (result.failedSources.isEmpty()) {
                        result.library
                    } else {
                        L.w(
                            "Source-local failures preserved prior generations: " +
                                result.failedSources.keys
                        )
                        Musikr.loadCached(
                            context,
                            config.copy(scanPlan = null, cleanupCovers = false),
                        )
                    }
                emitLibrary(publishedLibrary)
                result.cleanup()
                val isEmpty = publishedLibrary.songs.isEmpty()''',
)

# Tests: one source may fail while its sibling commits, and unavailable rows stay durable but hidden.
test = 'musikr/src/test/java/org/oxycblt/musikr/cache/db/IncrementalScanStoreTest.kt'
replace(
    test,
    '''    @Test
    fun `one failed source does not invalidate another source`() = runBlocking {''',
    '''    @Test
    fun `one source failure preserves its prior generation while sibling commits`() = runBlocking {
        val usb0 = snapshot("usb0-v1", "/storage/usbdisk0")
        val usb1 = snapshot("usb1-v1", "/storage/usbdisk1")
        val first = store.planScan(listOf(usb0, usb1), false, MetadataProfile.LEAN, 1L)
        store.beginScan(first)
        store.stage(cachedFile("alpha.mp3", 1L, "/storage/usbdisk0"))
        store.stage(cachedFile("beta.mp3", 1L, "/storage/usbdisk1"))
        store.commitScan()

        val next =
            store.planScan(
                listOf(usb0.copy(fingerprint = "usb0-v2"), usb1.copy(fingerprint = "usb1-v2")),
                false,
                MetadataProfile.LEAN,
                1L,
            )
        store.beginScan(next)
        store.stage(cachedFile("alpha.mp3", 2L, "/storage/usbdisk0"))
        store.stage(cachedFile("beta.mp3", 2L, "/storage/usbdisk1"))
        store.markSourceFailed(usb0.sourceKey, "removed during scan")
        val commit = store.commitScan()

        assertEquals(setOf(usb1.sourceKey), commit.committedSources)
        assertEquals(setOf(usb0.sourceKey), commit.failedSources.keys)
        assertEquals(
            1L,
            db.readDao()
                .selectSongByUri(Uri.parse("file:///storage/usbdisk0/alpha.mp3"))
                ?.modifiedMs,
        )
        assertEquals(
            2L,
            db.readDao()
                .selectSongByUri(Uri.parse("file:///storage/usbdisk1/beta.mp3"))
                ?.modifiedMs,
        )
    }

    @Test
    fun `one failed source does not invalidate another source`() = runBlocking {''',
)
replace(
    test,
    '''        assertFalse(db.incrementalDao().sourceLedger(mounted.sourceKey)?.available ?: true)
    }

    @Test
    fun `one source failure''',
    '''        assertFalse(db.incrementalDao().sourceLedger(mounted.sourceKey)?.available ?: true)
        assertTrue(store.compatibilityCachedFiles().toList().isEmpty())
        assertEquals(1, db.readDao().selectAllSongs().size)
    }

    @Test
    fun `one source failure''',
)
replace(test, 'import kotlinx.coroutines.runBlocking\n', 'import kotlinx.coroutines.flow.toList\nimport kotlinx.coroutines.runBlocking\n')

Path('scripts/pr2-source-isolation.py').unlink()
Path('.github/workflows/pr2-source-isolation.yml').unlink()
