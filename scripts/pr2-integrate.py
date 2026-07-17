#!/usr/bin/env python3
from pathlib import Path
import re


def replace(path: str, old: str, new: str, count: int = 1) -> None:
    p = Path(path)
    text = p.read_text()
    if old not in text:
        raise SystemExit(f"missing anchor in {path}: {old[:160]!r}")
    p.write_text(text.replace(old, new, count))


def sub(path: str, pattern: str, replacement: str) -> None:
    p = Path(path)
    text = p.read_text()
    new, n = re.subn(pattern, replacement, text, flags=re.S)
    if n != 1:
        raise SystemExit(f"expected one regex match in {path}, found {n}: {pattern[:120]!r}")
    p.write_text(new)


repo = "app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt"
replace(
    repo,
    "import org.oxycblt.musikr.cache.MutableCache\n",
    "import org.oxycblt.musikr.cache.IncrementalCache\nimport org.oxycblt.musikr.cache.IncrementalScanPlan\nimport org.oxycblt.musikr.cache.MutableCache\n",
)
replace(
    repo,
    "import org.oxycblt.musikr.fs.saf.SAF\n",
    "import org.oxycblt.musikr.fs.saf.SAF\nimport org.oxycblt.musikr.library.MetadataProfile\n",
)
replace(
    repo,
    """    fun requestIndex(withCache: Boolean)\n\n    /**\n     * Start the music system.""",
    """    fun requestIndex(withCache: Boolean)\n\n    /** Request a scan with an explicit metadata-work profile. */\n    fun requestIndex(withCache: Boolean, metadataProfile: MetadataProfile) {\n        requestIndex(withCache)\n    }\n\n    /** Persist a source invalidation without performing a provider query. */\n    suspend fun invalidateSource(sourceKey: String? = null) = Unit\n\n    /**\n     * Start the music system.""",
)
replace(
    repo,
    """    suspend fun index(worker: IndexingWorker, withCache: Boolean)\n\n    /** Data regarding""",
    """    suspend fun index(worker: IndexingWorker, withCache: Boolean)\n\n    suspend fun index(\n        worker: IndexingWorker,\n        withCache: Boolean,\n        metadataProfile: MetadataProfile,\n    ) {\n        index(worker, withCache)\n    }\n\n    /** Data regarding""",
)
replace(
    repo,
    """        fun requestIndex(withCache: Boolean)\n    }\n}""",
    """        fun requestIndex(withCache: Boolean)\n\n        fun requestIndex(withCache: Boolean, metadataProfile: MetadataProfile) {\n            requestIndex(withCache)\n        }\n    }\n}""",
)
replace(
    repo,
    """    override fun requestIndex(withCache: Boolean) {\n        indexingWorker?.requestIndex(withCache)\n    }\n\n    override suspend fun startup""",
    """    override fun requestIndex(withCache: Boolean) {\n        indexingWorker?.requestIndex(withCache)\n    }\n\n    override fun requestIndex(withCache: Boolean, metadataProfile: MetadataProfile) {\n        indexingWorker?.requestIndex(withCache, metadataProfile)\n    }\n\n    override suspend fun invalidateSource(sourceKey: String?) {\n        (cache as? IncrementalCache)?.invalidateSource(sourceKey)\n    }\n\n    override suspend fun startup""",
)

index_impl = r'''    override suspend fun index(worker: IndexingWorker, withCache: Boolean) =
        index(worker, withCache, metadataProfile = null)

    override suspend fun index(
        worker: IndexingWorker,
        withCache: Boolean,
        metadataProfile: MetadataProfile,
    ) = index(worker, withCache, metadataProfile as MetadataProfile?)

    private suspend fun index(
        worker: IndexingWorker,
        withCache: Boolean,
        metadataProfile: MetadataProfile?,
    ) =
        PerfTimer.traceSuspend("MusicRepository.index(cache=$withCache profile=$metadataProfile)") {
            yield()
            if (indexingWorker !== worker) {
                L.w("Index requested from unregistered worker; ignoring")
                return@traceSuspend
            }

            if (BuildConfig.TOPWAY_COMPAT_FLAVOR) {
                val twStorageSwitch = readTwStorageSwitch()
                if (!twStorageSwitch.isNullOrEmpty()) {
                    L.d("TS18 diagnostic: persist.tw.storage.switch=$twStorageSwitch")
                }
            }

            val playbackActive = worker.playbackActiveSnapshot()
            val resolvedProfile =
                DrivingStartupPolicy.metadataProfile(
                    explicit = metadataProfile,
                    scanPriority = musicSettings.scanPriority,
                    playbackActive = playbackActive,
                    isTopwayVariant = BuildConfig.TOPWAY_COMPAT_FLAVOR,
                )
            val currentRevision = musicSettings.revision
            val newRevision = currentRevision?.takeIf { withCache } ?: UUID.randomUUID()
            val workerCount =
                DefaultIndexingResourcePolicy.resolveWorkerCount(
                    scanPriority = musicSettings.scanPriority,
                    playbackActive = playbackActive,
                    isTopwayVariant = BuildConfig.TOPWAY_COMPAT_FLAVOR,
                    availableProcessors = Runtime.getRuntime().availableProcessors(),
                )
            val rawFs = createFileSystem()
            val prepared =
                IncrementalIndexPlanner.prepare(
                    fs = rawFs,
                    cache = cache,
                    withCache = withCache,
                    profile = resolvedProfile,
                    configurationRevision = sourceConfigurationRevision(),
                    legacyWriteOnly = ::WriteOnlyMutableCache,
                )
            val plan = prepared.plan
            L.i(
                "Resolved scan policy [workers=$workerCount profile=$resolvedProfile " +
                    "scan=${plan?.scanSourceKeys} reuse=${plan?.reuseSourceKeys} " +
                    "unavailable=${plan?.unavailableSourceKeys}]"
            )

            if (plan != null && !plan.hasWork && synchronized(this) { library != null }) {
                L.i("All configured sources are unchanged; skipping provider scan and extraction")
                if (resolvedProfile == MetadataProfile.FULL) {
                    emitStartupReadinessState(StartupReadinessState.EnrichmentComplete)
                } else {
                    worker.requestIndex(true, MetadataProfile.FULL)
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
            if (plan == null && locations.any { !it.path.volume.isAccessible() }) {
                L.w("One or more legacy music sources are inaccessible. Preserving cache.")
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

                if (plan == null && result.library.songs.isEmpty()) {
                    if (locations.any { !it.path.volume.isAccessible() }) {
                        L.w("Legacy scan became inaccessible. Preserving cache.")
                        musicSettings.lastScanFailed = true
                        emitIndexingCompletion(Exception("Source became inaccessible during scan"))
                        return@traceSuspend
                    }
                }

                musicSettings.revision = newRevision
                emitLibrary(result.library)
                result.cleanup()
                val isEmpty = result.library.songs.isEmpty()
                musicSettings.libraryState = if (isEmpty) LibraryState.EMPTY else LibraryState.USABLE
                musicSettings.lastScanFailed = false
                emitStartupLibraryStatus(
                    if (isEmpty) StartupLibraryStatus.Empty else StartupLibraryStatus.Usable
                )
                if (!isEmpty) emitStartupReadinessState(StartupReadinessState.FullLibraryReady)
                if (resolvedProfile == MetadataProfile.FULL) {
                    emitStartupReadinessState(StartupReadinessState.EnrichmentComplete)
                }
                emitIndexingCompletion(null)

                if (resolvedProfile == MetadataProfile.LEAN && !isEmpty) {
                    // The worker defers this while playback is active, so rich extraction cannot
                    // compete with first audio or driving interaction.
                    worker.requestIndex(true, MetadataProfile.FULL)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                musicSettings.lastScanFailed = true
                L.w(e, "Indexing failed; committed source generations remain readable")
                emitIndexingCompletion(e)
            }
        }

    private fun startCompatibilityBackfill()'''
sub(
    repo,
    r"    override suspend fun index\(worker: IndexingWorker, withCache: Boolean\) =.*?\n    private fun startCompatibilityBackfill\(\)",
    index_impl,
)

config_impl = r'''    private suspend fun createConfig(
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

    private fun createFileSystem(): FS =
        when (musicSettings.locationMode) {
            LocationMode.SAF -> SAF.from(context, musicSettings.safQuery)
            LocationMode.MEDIA_STORE -> {
                val query =
                    musicSettings.mediaStoreQuery.copy(
                        useDefaultSystemFilter = musicSettings.ts18SystemSourceFilter
                    )
                MediaStore.from(context, query)
            }
            LocationMode.DIRECT_FS ->
                DirectFS(
                    musicSettings.safQuery.source,
                    rootGate.takeIf {
                        musicSettings.rootAccessPolicy == RootAccessPolicy.ON_DEMAND
                    },
                )
        }

    private fun sourceConfigurationRevision(): Long {
        val material =
            buildString {
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

    private fun emitStartupReadinessState'''
sub(
    repo,
    r"    private suspend fun createConfig\(.*?\n    private fun emitStartupReadinessState",
    config_impl,
)

holder = "app/src/main/java/org/oxycblt/auxio/music/service/IndexingHolder.kt"
replace(
    holder,
    "import org.oxycblt.musikr.fs.FSUpdate\n",
    "import org.oxycblt.musikr.fs.FSUpdate\nimport org.oxycblt.musikr.fs.SourceIdentity\nimport org.oxycblt.musikr.library.MetadataProfile\n",
)
replace(
    holder,
    """    private var currentIndexJob: Job? = null\n    private var pendingIndexWithCache: Boolean? = null\n    private var startupJob: Job? = null""",
    """    private data class IndexRequest(\n        val withCache: Boolean,\n        val metadataProfile: MetadataProfile?,\n    )\n\n    private var currentIndexJob: Job? = null\n    private var pendingIndexRequest: IndexRequest? = null\n    private var startupJob: Job? = null""",
)
replace(holder, "pendingIndexWithCache = null", "pendingIndexRequest = null")
request_impl = r'''    @Synchronized
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
        val current = pendingIndexRequest
        pendingIndexRequest =
            if (current == null) {
                request
            } else {
                IndexRequest(
                    // A cache-bypassing request is stronger, so false wins.
                    withCache = current.withCache && request.withCache,
                    // Full enrichment is stronger than Lean; explicit beats automatic policy.
                    metadataProfile =
                        when {
                            current.metadataProfile == MetadataProfile.FULL ||
                                request.metadataProfile == MetadataProfile.FULL ->
                                MetadataProfile.FULL
                            current.metadataProfile == MetadataProfile.LEAN ||
                                request.metadataProfile == MetadataProfile.LEAN ->
                                MetadataProfile.LEAN
                            else -> null
                        },
                )
            }
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

    override fun onProgressionChanged'''
sub(
    holder,
    r"    @Synchronized\n    override fun requestIndex\(withCache: Boolean\).*?\n    override fun onProgressionChanged",
    request_impl,
)
replace(
    holder,
    """                val pending = pendingIndexWithCache\n                if (pending != null && currentIndexJob?.isActive != true) {\n                    pendingIndexWithCache = null\n                    startIndexLocked(pending)\n                }""",
    """                val pending = pendingIndexRequest\n                if (pending != null && currentIndexJob?.isActive != true) {\n                    pendingIndexRequest = null\n                    startIndexLocked(pending)\n                }""",
)
replace(
    holder,
    """                fs.track().collect { update ->\n                    if (update is FSUpdate.LocationChanged) {\n                        val location = update.location""",
    """                fs.track().collect { update ->\n                    val location = (update as? FSUpdate.LocationChanged)?.location\n                    musicRepository.invalidateSource(location?.let(SourceIdentity::forLocation))\n                    if (update is FSUpdate.LocationChanged) {""",
)
replace(
    holder,
    """        if (musicSettings.shouldBeObserving) startTracking() else stopTracking()\n        musicRepository.requestIndex(true)""",
    """        if (musicSettings.shouldBeObserving) startTracking() else stopTracking()\n        indexScope.launch {\n            musicRepository.invalidateSource()\n            musicRepository.requestIndex(true)\n        }""",
)

home = "app/src/main/java/org/oxycblt/auxio/home/HomeViewModel.kt"
replace(
    home,
    """    private val _currentTabType = MutableStateFlow(currentTabTypes[0])\n    /** The [MusicType]""",
    """    private val _currentTabType = MutableStateFlow(currentTabTypes[0])\n    private val categorySubscriptions = CategorySubscriptionGate(currentTabTypes[0])\n    /** The [MusicType]""",
)
replace(
    home,
    """    override fun invalidateMusic(type: MusicType, instructions: UpdateInstructions) {\n        // Cancel any previous""",
    """    override fun invalidateMusic(type: MusicType, instructions: UpdateInstructions) {\n        // Expensive rich-category work is subscriber-driven. Inactive invalidations conflate into\n        // one refresh when their tab becomes visible.\n        if (!categorySubscriptions.invalidate(type)) return\n        // Cancel any previous""",
)
replace(
    home,
    """        L.d("Updating current tab to ${currentTabTypes[pagerPos]}")\n        _currentTabType.value = currentTabTypes[pagerPos]""",
    """        val next = currentTabTypes[pagerPos]\n        L.d("Updating current tab to $next")\n        _currentTabType.value = next\n        if (categorySubscriptions.activate(next)) {\n            invalidateMusic(next, UpdateInstructions.Replace(0))\n        }""",
)

# Preserve unavailable committed source rows in the compatibility graph while projections expose
# availability separately.
explore = "musikr/src/main/java/org/oxycblt/musikr/pipeline/ExploreStep.kt"
replace(
    explore,
    "config.scanPlan?.reuseSourceKeys.orEmpty(),",
    "config.scanPlan?.let { it.reuseSourceKeys + it.unavailableSourceKeys }.orEmpty(),",
)

Path("scripts/pr2-integrate.py").unlink()
Path(".github/workflows/pr2-integrate.yml").unlink()
