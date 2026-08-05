#!/usr/bin/env python3
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]


def read(path: str) -> str:
    return (ROOT / path).read_text(encoding="utf-8")


def write(path: str, content: str) -> None:
    (ROOT / path).write_text(content, encoding="utf-8")


def replace_once(path: str, old: str, new: str) -> None:
    text = read(path)
    count = text.count(old)
    if count != 1:
        raise RuntimeError(f"{path}: expected one match, found {count}: {old[:120]!r}")
    write(path, text.replace(old, new, 1))


def replace_range(path: str, start: str, end: str, replacement: str) -> None:
    text = read(path)
    start_index = text.find(start)
    if start_index < 0:
        raise RuntimeError(f"{path}: start marker not found: {start!r}")
    end_index = text.find(end, start_index)
    if end_index < 0:
        raise RuntimeError(f"{path}: end marker not found: {end!r}")
    if text.find(start, start_index + len(start)) >= 0:
        raise RuntimeError(f"{path}: start marker is not unique: {start!r}")
    write(path, text[:start_index] + replacement + text[end_index:])


# Extend the immutable plan/commit protocol without changing the Room schema.
replace_once(
    "musikr/src/main/java/org/oxycblt/musikr/cache/IncrementalScan.kt",
    '''data class IncrementalScanPlan(
    val scanId: String,
    val scanSources: List<SourceSnapshot>,
    val reuseSourceKeys: Set<String>,
    val unavailableSourceKeys: Set<String>,
    val metadataProfile: MetadataProfile,
    val configurationRevision: Long,
    val force: Boolean,
) {
    val scanSourceKeys: Set<String> = scanSources.mapTo(linkedSetOf()) { it.sourceKey }

    val hasWork: Boolean
        get() = scanSources.isNotEmpty()
}''',
    '''data class IncrementalScanPlan(
    val scanId: String,
    val scanSources: List<SourceSnapshot>,
    val reuseSourceKeys: Set<String>,
    val unavailableSourceKeys: Set<String>,
    val metadataProfile: MetadataProfile,
    val configurationRevision: Long,
    val force: Boolean,
    val scanReasons: Map<String, SourceScanReason> = emptyMap(),
    val removedSourceKeys: Set<String> = emptySet(),
    val enrichmentOnly: Boolean = false,
) {
    val scanSourceKeys: Set<String> = scanSources.mapTo(linkedSetOf()) { it.sourceKey }

    val hasWork: Boolean
        get() = scanSources.isNotEmpty() || removedSourceKeys.isNotEmpty()
}''',
)
replace_once(
    "musikr/src/main/java/org/oxycblt/musikr/cache/IncrementalScan.kt",
    '''data class IncrementalScanCommit(
    val scanId: String,
    val committedSources: Set<String>,
    val reusedSources: Set<String>,
    val unavailableSources: Set<String>,
    val failedSources: Map<String, String>,
    val changedRows: Int,
    val removedRows: Int,
    val metadataProfile: MetadataProfile,
)''',
    '''data class IncrementalScanCommit(
    val scanId: String,
    val committedSources: Set<String>,
    val reusedSources: Set<String>,
    val unavailableSources: Set<String>,
    val failedSources: Map<String, String>,
    val changedRows: Int,
    val removedRows: Int,
    val metadataProfile: MetadataProfile,
    val removedSources: Set<String> = emptySet(),
    val enrichmentOnly: Boolean = false,
    val enrichmentComplete: Boolean = true,
)''',
)

# Add bounded enrichment coverage queries; no Room entity/schema change is required.
replace_once(
    "musikr/src/main/java/org/oxycblt/musikr/cache/db/IncrementalDatabase.kt",
    '''    suspend fun pendingCount(scanId: String, sourceKey: String): Int

    @Query(
        "INSERT OR REPLACE INTO IndexedSongData''',
    '''    suspend fun pendingCount(scanId: String, sourceKey: String): Int

    @Query(
        "SELECT COUNT(*) FROM ScanSeenData WHERE scanId = :scanId AND sourceKey = :sourceKey"
    )
    suspend fun seenCount(scanId: String, sourceKey: String): Int

    @Query(
        "SELECT COUNT(*) FROM IndexedSongData WHERE sourceKey = :sourceKey AND generation = :generation"
    )
    suspend fun committedSongCount(sourceKey: String, generation: Long): Int

    @Query(
        "INSERT OR REPLACE INTO IndexedSongData''',
)

# Make planning side-effect-safe for removal/unavailability and classify pure enrichment.
replace_range(
    "musikr/src/main/java/org/oxycblt/musikr/cache/db/IncrementalScanStore.kt",
    '''    override suspend fun planScan(
''',
    '''    override suspend fun beginScan(plan: IncrementalScanPlan) {
''',
    '''    override suspend fun planScan(
        snapshots: List<SourceSnapshot>,
        force: Boolean,
        metadataProfile: MetadataProfile,
        configurationRevision: Long,
    ): IncrementalScanPlan {
        check(currentPlan == null) { "Cannot plan a second scan while one is active" }
        val scanSources = mutableListOf<SourceSnapshot>()
        val scanReasons = linkedMapOf<String, org.oxycblt.musikr.cache.SourceScanReason>()
        val reuse = linkedSetOf<String>()
        val unavailable = linkedSetOf<String>()
        val removed = linkedSetOf<String>()
        val distinctSnapshots = snapshots.distinctBy { it.sourceKey }
        val currentSourceKeys = distinctSnapshots.mapTo(linkedSetOf()) { it.sourceKey }

        db.withTransaction {
            for (ledger in dao.sourceLedgers()) {
                if (ledger.sourceKey !in currentSourceKeys && ledger.lastCommittedGeneration != null) {
                    // Omission is a candidate removal only. Keep the old generation visible until
                    // the replacement source configuration commits successfully.
                    removed += ledger.sourceKey
                }
            }
            for (snapshot in distinctSnapshots) {
                val previous = dao.sourceLedger(snapshot.sourceKey)
                val observed =
                    if (previous == null) {
                        SourceLedgerData(
                            sourceKey = snapshot.sourceKey,
                            sourceType = snapshot.sourceType,
                            rootUri = snapshot.rootUri,
                            rootPath = snapshot.rootPath,
                            fingerprint = null,
                            fingerprintStrength = SourceFingerprintStrength.NONE.name,
                            available = snapshot.available,
                            lastSeenMs = snapshot.observedAtMs,
                            lastCommittedGeneration = null,
                            pendingGeneration = null,
                            lastSuccessfulScanMs = null,
                            configurationRevision = configurationRevision,
                            invalidationVersion = 0L,
                            committedInvalidationVersion = 0L,
                            committedProfile = null,
                            enrichmentRevision = 0L,
                            incomplete = false,
                        )
                    } else {
                        previous.observed(snapshot).copy(
                            // A transient unmount must not hide the last committed generation.
                            available = snapshot.available || previous.available
                        )
                    }
                dao.upsertSourceLedger(observed)

                if (!snapshot.available) {
                    unavailable += snapshot.sourceKey
                    if (previous?.lastCommittedGeneration != null) reuse += snapshot.sourceKey
                    continue
                }
                val previousProfile =
                    previous?.committedProfile?.let {
                        runCatching { MetadataProfile.valueOf(it) }.getOrNull()
                    }
                val profileUpgrade =
                    previousProfile == null ||
                        metadataProfile.incrementalRank > previousProfile.incrementalRank
                val reason =
                    SourceFingerprintReusePolicy.scanReason(
                        strength = snapshot.fingerprintStrength,
                        fingerprint = snapshot.fingerprint,
                        previous = previous?.reuseState(),
                        force = force,
                        profileUpgrade = profileUpgrade,
                        configurationRevision = configurationRevision,
                        nowMs = System.currentTimeMillis(),
                    )
                if (reason == null) {
                    reuse += snapshot.sourceKey
                } else {
                    scanSources += snapshot
                    scanReasons[snapshot.sourceKey] = reason
                }
            }
        }

        val enrichmentOnly =
            scanSources.isNotEmpty() &&
                removed.isEmpty() &&
                scanSources.all {
                    scanReasons[it.sourceKey] ==
                        org.oxycblt.musikr.cache.SourceScanReason.METADATA_PROFILE_UPGRADE
                }
        return IncrementalScanPlan(
            scanId = UUID.randomUUID().toString(),
            scanSources = scanSources,
            reuseSourceKeys = reuse,
            unavailableSourceKeys = unavailable,
            metadataProfile = metadataProfile,
            configurationRevision = configurationRevision,
            force = force,
            scanReasons = scanReasons,
            removedSourceKeys = removed,
            enrichmentOnly = enrichmentOnly,
        )
    }

''',
)
replace_range(
    "musikr/src/main/java/org/oxycblt/musikr/cache/db/IncrementalScanStore.kt",
    '''    override suspend fun beginScan(plan: IncrementalScanPlan) {
''',
    '''    override suspend fun markSeen(file: File, cachedFile: CachedFile?) {
''',
    '''    override suspend fun beginScan(plan: IncrementalScanPlan) {
        check(currentPlan == null) { "An incremental scan is already active" }
        sourceFailures.clear()
        val now = System.currentTimeMillis()
        db.withTransaction {
            for (snapshot in plan.scanSources) {
                val ledger = requireNotNull(dao.sourceLedger(snapshot.sourceKey))
                dao.deletePendingForSource(snapshot.sourceKey)
                dao.deleteSeenForSource(snapshot.sourceKey)
                if (plan.enrichmentOnly) {
                    check(ledger.lastCommittedGeneration != null) {
                        "Enrichment requires a committed source generation"
                    }
                    continue
                }
                val generation = (ledger.lastCommittedGeneration ?: 0L) + 1L
                dao.upsertSourceLedger(
                    ledger.copy(
                        pendingGeneration = generation,
                        configurationRevision = plan.configurationRevision,
                        incomplete = true,
                    )
                )
                dao.upsertGeneration(
                    SourceScanGenerationData(
                        scanId = plan.scanId,
                        sourceKey = snapshot.sourceKey,
                        generation = generation,
                        state = STATE_PENDING,
                        startedAtMs = now,
                        completedAtMs = null,
                        error = null,
                    )
                )
            }
        }
        currentPlan = plan
    }

''',
)
replace_range(
    "musikr/src/main/java/org/oxycblt/musikr/cache/db/IncrementalScanStore.kt",
    '''    override suspend fun markSeen(file: File, cachedFile: CachedFile?) {
''',
    '''    private suspend fun upsertSeen(
''',
    '''    override suspend fun markSeen(file: File, cachedFile: CachedFile?) {
        val plan = currentPlan ?: return
        val sourceKey = SourceIdentity.forFile(file)
        if (sourceKey !in plan.scanSourceKeys) return
        val state = dao.uriState(sourceKey, file.uri.toString())
        if (plan.enrichmentOnly && state?.available != true) return
        val profile = state?.metadataProfile ?: plan.metadataProfile.name
        upsertSeen(plan, sourceKey, file, cachedFile, profile)
    }

''',
)
replace_range(
    "musikr/src/main/java/org/oxycblt/musikr/cache/db/IncrementalScanStore.kt",
    '''    override suspend fun stage(cachedFile: CachedFile): Boolean {
''',
    '''    override fun reusedCachedFiles(sourceKeys: Set<String>): Flow<CachedFile> = flow {
''',
    '''    override suspend fun stage(cachedFile: CachedFile): Boolean {
        val plan = currentPlan ?: return false
        val sourceKey = SourceIdentity.forFile(cachedFile.file)
        if (sourceKey !in plan.scanSourceKeys) return false
        if (
            plan.enrichmentOnly &&
                dao.uriState(sourceKey, cachedFile.file.uri.toString())?.available != true
        ) {
            // Consume a newly observed file without leaking it through the legacy write path.
            return true
        }
        val audio = cachedFile.audio
        val tags = audio?.tags
        val durableCoverId = audio?.coverId ?: readDao.selectSongByUri(cachedFile.file.uri)?.coverId
        dao.upsertPending(
            PendingCachedFileData(
                scanId = plan.scanId,
                sourceKey = sourceKey,
                uri = cachedFile.file.uri.toString(),
                modifiedMs = cachedFile.file.modifiedMs,
                addedMs = cachedFile.addedMs,
                mimeType = audio?.properties?.mimeType,
                durationMs = audio?.properties?.durationMs,
                bitrateKbps = audio?.properties?.bitrateKbps,
                sampleRateHz = audio?.properties?.sampleRateHz,
                musicBrainzId = tags?.musicBrainzId,
                name = tags?.name,
                sortName = tags?.sortName,
                track = tags?.track,
                disc = tags?.disc,
                subtitle = tags?.subtitle,
                date = tags?.date,
                albumMusicBrainzId = tags?.albumMusicBrainzId,
                albumName = tags?.albumName,
                albumSortName = tags?.albumSortName,
                releaseTypes = tags?.releaseTypes,
                artistMusicBrainzIds = tags?.artistMusicBrainzIds,
                artistNames = tags?.artistNames,
                artistSortNames = tags?.artistSortNames,
                albumArtistMusicBrainzIds = tags?.albumArtistMusicBrainzIds,
                albumArtistNames = tags?.albumArtistNames,
                albumArtistSortNames = tags?.albumArtistSortNames,
                genreNames = tags?.genreNames,
                replayGainTrackAdjustment = tags?.replayGainTrackAdjustment,
                replayGainAlbumAdjustment = tags?.replayGainAlbumAdjustment,
                coverId = durableCoverId,
            )
        )
        upsertSeen(
            plan = plan,
            sourceKey = sourceKey,
            file = cachedFile.file,
            cachedFile = cachedFile,
            profile = plan.metadataProfile.name,
            artworkRef = durableCoverId,
        )
        return true
    }

''',
)
replace_range(
    "musikr/src/main/java/org/oxycblt/musikr/cache/db/IncrementalScanStore.kt",
    '''    override suspend fun commitScan(): IncrementalScanCommit {
''',
    '''    override suspend fun abortScan(cause: Throwable?) {
''',
    '''    override suspend fun commitScan(): IncrementalScanCommit {
        val plan = requireNotNull(currentPlan) { "No incremental scan is active" }
        var changedRows = 0
        var removedRows = 0
        var enrichmentComplete = true
        val committed = linkedSetOf<String>()
        val removed = linkedSetOf<String>()
        var committedSuccessfully = false
        try {
            db.withTransaction {
                if (plan.enrichmentOnly) {
                    for (snapshot in plan.scanSources) {
                        val ledger = requireNotNull(dao.sourceLedger(snapshot.sourceKey))
                        val generation = requireNotNull(ledger.lastCommittedGeneration)
                        if (sourceFailures[snapshot.sourceKey] != null) {
                            enrichmentComplete = false
                            dao.deletePendingForSource(snapshot.sourceKey)
                            dao.deleteSeenForSource(snapshot.sourceKey)
                            continue
                        }
                        var offset = 0
                        while (true) {
                            val page = dao.pendingPage(plan.scanId, snapshot.sourceKey, PAGE_SIZE, offset)
                            if (page.isEmpty()) break
                            writeDao.updateSongs(page.map { it.toCachedFileData() })
                            changedRows += page.size
                            if (page.size < PAGE_SIZE) break
                            offset += page.size
                        }
                        val committedCount = dao.committedSongCount(snapshot.sourceKey, generation)
                        val seenCount = dao.seenCount(plan.scanId, snapshot.sourceKey)
                        dao.publishSeenSongs(
                            plan.scanId,
                            snapshot.sourceKey,
                            generation,
                            FULL_ENRICHMENT_REVISION,
                        )
                        dao.publishSeenUriStates(plan.scanId, snapshot.sourceKey, generation)
                        if (seenCount == committedCount) {
                            dao.upsertSourceLedger(
                                ledger.copy(
                                    committedProfile = MetadataProfile.FULL.name,
                                    enrichmentRevision = FULL_ENRICHMENT_REVISION,
                                )
                            )
                        } else {
                            enrichmentComplete = false
                        }
                        committed += snapshot.sourceKey
                    }
                } else {
                    for (snapshot in plan.scanSources) {
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
                        val sourceChangedRows = dao.pendingCount(plan.scanId, snapshot.sourceKey)
                        var offset = 0
                        while (true) {
                            val page = dao.pendingPage(plan.scanId, snapshot.sourceKey, PAGE_SIZE, offset)
                            if (page.isEmpty()) break
                            writeDao.updateSongs(page.map { it.toCachedFileData() })
                            changedRows += page.size
                            if (page.size < PAGE_SIZE) break
                            offset += page.size
                        }
                        dao.publishSeenSongs(
                            plan.scanId,
                            snapshot.sourceKey,
                            generation,
                            if (plan.metadataProfile == MetadataProfile.FULL) {
                                FULL_ENRICHMENT_REVISION
                            } else {
                                ledger.enrichmentRevision
                            },
                        )
                        dao.publishSeenUriStates(plan.scanId, snapshot.sourceKey, generation)
                        ledger.lastCommittedGeneration?.let { oldGeneration ->
                            dao.tombstoneMissingUris(
                                plan.scanId,
                                snapshot.sourceKey,
                                oldGeneration,
                                generation,
                            )
                            removedRows +=
                                dao.deleteMissingCachedRows(
                                    plan.scanId,
                                    snapshot.sourceKey,
                                    oldGeneration,
                                )
                        }
                        val completedAt = System.currentTimeMillis()
                        dao.upsertSourceLedger(
                            ledger.copy(
                                sourceType = snapshot.sourceType,
                                rootUri = snapshot.rootUri,
                                rootPath = snapshot.rootPath,
                                fingerprint = snapshot.fingerprint,
                                fingerprintStrength = snapshot.fingerprintStrength.name,
                                available = true,
                                lastSeenMs = snapshot.observedAtMs,
                                lastCommittedGeneration = generation,
                                pendingGeneration = null,
                                lastSuccessfulScanMs = completedAt,
                                configurationRevision = plan.configurationRevision,
                                committedInvalidationVersion = ledger.invalidationVersion,
                                committedProfile =
                                    if (
                                        sourceChangedRows == 0 &&
                                            ledger.committedProfile == MetadataProfile.FULL.name
                                    ) {
                                        MetadataProfile.FULL.name
                                    } else {
                                        plan.metadataProfile.name
                                    },
                                enrichmentRevision =
                                    if (plan.metadataProfile == MetadataProfile.FULL) {
                                        FULL_ENRICHMENT_REVISION
                                    } else {
                                        ledger.enrichmentRevision
                                    },
                                incomplete = false,
                            )
                        )
                        dao.completeGeneration(
                            plan.scanId,
                            snapshot.sourceKey,
                            STATE_COMMITTED,
                            completedAt,
                            null,
                        )
                        dao.deleteOlderIndexedRows(snapshot.sourceKey, generation)
                        committed += snapshot.sourceKey
                    }
                    val replacementComplete =
                        sourceFailures.isEmpty() &&
                            plan.unavailableSourceKeys.isEmpty() &&
                            committed.containsAll(plan.scanSourceKeys)
                    if (replacementComplete) {
                        val removedAt = System.currentTimeMillis()
                        for (sourceKey in plan.removedSourceKeys) {
                            val ledger = dao.sourceLedger(sourceKey) ?: continue
                            dao.upsertSourceLedger(ledger.copy(available = false, lastSeenMs = removedAt))
                            removed += sourceKey
                        }
                    }
                }
                dao.deletePending(plan.scanId)
                dao.deleteSeen(plan.scanId)
            }
            committedSuccessfully = true
        } finally {
            if (committedSuccessfully) currentPlan = null
        }
        val failed = sourceFailures.toMap()
        sourceFailures.clear()
        return IncrementalScanCommit(
            scanId = plan.scanId,
            committedSources = committed,
            reusedSources = plan.reuseSourceKeys,
            unavailableSources = plan.unavailableSourceKeys,
            failedSources = failed,
            changedRows = changedRows,
            removedRows = removedRows,
            metadataProfile = plan.metadataProfile,
            removedSources = removed,
            enrichmentOnly = plan.enrichmentOnly,
            enrichmentComplete = enrichmentComplete,
        )
    }

''',
)
replace_range(
    "musikr/src/main/java/org/oxycblt/musikr/cache/db/IncrementalScanStore.kt",
    '''    override suspend fun abortScan(cause: Throwable?) {
''',
    '''    override suspend fun invalidateSource(sourceKey: String?) {
''',
    '''    override suspend fun abortScan(cause: Throwable?) {
        val plan = currentPlan ?: return
        try {
            db.withTransaction {
                if (!plan.enrichmentOnly) {
                    for (snapshot in plan.scanSources) {
                        val ledger = dao.sourceLedger(snapshot.sourceKey) ?: continue
                        dao.upsertSourceLedger(
                            ledger.copy(pendingGeneration = null, incomplete = true)
                        )
                        dao.completeGeneration(
                            plan.scanId,
                            snapshot.sourceKey,
                            if (cause is CancellationException) STATE_CANCELLED else STATE_FAILED,
                            System.currentTimeMillis(),
                            cause?.message?.take(MAX_ERROR_LENGTH),
                        )
                    }
                }
                dao.deletePending(plan.scanId)
                dao.deleteSeen(plan.scanId)
            }
        } finally {
            currentPlan = null
            sourceFailures.clear()
        }
    }

''',
)

# Planner: forced scans keep source generations; explicit empty sets permit removal-only commit.
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/IncrementalIndexPlanner.kt",
    '''        targetSourceKeys: Set<String>? = null,
        legacyWriteOnly: (MutableCache) -> MutableCache,
''',
    '''        targetSourceKeys: Set<String>? = null,
        allowEmptySourceSet: Boolean = false,
        applyRemovedSources: Boolean = true,
        legacyWriteOnly: (MutableCache) -> MutableCache,
''',
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/IncrementalIndexPlanner.kt",
    '''        if (observedSnapshots.isEmpty()) {
            throw SourcePreflightException("Music-source preflight returned no configured sources")
        }
''',
    '''        if (observedSnapshots.isEmpty() && !allowEmptySourceSet) {
            throw SourcePreflightException("Music-source preflight returned no configured sources")
        }
''',
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/IncrementalIndexPlanner.kt",
    '''        val plan =
            if (targetSourceKeys == null) {
                completePlan
            } else {
                val selectedSources =
                    completePlan.scanSources.filter { it.sourceKey in targetSourceKeys }
                val deferredSourceKeys =
                    completePlan.scanSourceKeys - targetSourceKeys +
                        (completePlan.unavailableSourceKeys - targetSourceKeys)
                completePlan.copy(
                    scanSources = selectedSources,
                    reuseSourceKeys = completePlan.reuseSourceKeys + deferredSourceKeys,
                    unavailableSourceKeys = completePlan.unavailableSourceKeys,
                )
            }
''',
    '''        val removalScopedPlan =
            if (applyRemovedSources) completePlan
            else completePlan.copy(removedSourceKeys = emptySet())
        val plan =
            if (targetSourceKeys == null) {
                removalScopedPlan.copy(
                    enrichmentOnly =
                        removalScopedPlan.scanSources.isNotEmpty() &&
                            removalScopedPlan.removedSourceKeys.isEmpty() &&
                            removalScopedPlan.scanSources.all {
                                removalScopedPlan.scanReasons[it.sourceKey] ==
                                    org.oxycblt.musikr.cache.SourceScanReason
                                        .METADATA_PROFILE_UPGRADE
                            }
                )
            } else {
                val selectedSources =
                    removalScopedPlan.scanSources.filter { it.sourceKey in targetSourceKeys }
                val selectedKeys = selectedSources.mapTo(linkedSetOf()) { it.sourceKey }
                val selectedReasons = removalScopedPlan.scanReasons.filterKeys { it in selectedKeys }
                val deferredSourceKeys =
                    removalScopedPlan.scanSourceKeys - targetSourceKeys +
                        (removalScopedPlan.unavailableSourceKeys - targetSourceKeys)
                removalScopedPlan.copy(
                    scanSources = selectedSources,
                    scanReasons = selectedReasons,
                    reuseSourceKeys = removalScopedPlan.reuseSourceKeys + deferredSourceKeys,
                    unavailableSourceKeys = removalScopedPlan.unavailableSourceKeys,
                    enrichmentOnly =
                        selectedSources.isNotEmpty() &&
                            removalScopedPlan.removedSourceKeys.isEmpty() &&
                            selectedSources.all {
                                selectedReasons[it.sourceKey] ==
                                    org.oxycblt.musikr.cache.SourceScanReason
                                        .METADATA_PROFILE_UPGRADE
                            },
                )
            }
''',
)

# Repository wiring: retain explicit empty sets and use forced incremental planning for no-cache.
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt",
    '''                val requestedSourceKeys = request.sourceKeys?.takeIf { it.isNotEmpty() }
''',
    '''                val requestedSourceKeys = request.sourceKeys
''',
)
replace_range(
    "app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt",
    '''                val prepared =
                    if (!request.withCache) {
''',
    '''                val plan = prepared.plan
''',
    '''                val prepared =
                    try {
                        IncrementalIndexPlanner.prepare(
                            fs = rawFs,
                            cache = cache,
                            withCache = request.withCache,
                            profile = resolvedProfile,
                            configurationRevision = sourceConfigurationRevision(),
                            targetSourceKeys = requestedSourceKeys,
                            allowEmptySourceSet =
                                checkpointAuthority != null &&
                                    requestedSourceKeys?.isEmpty() == true &&
                                    allConfiguredSourceKeys.isEmpty(),
                            applyRemovedSources = checkpointAuthority != null,
                            legacyWriteOnly = ::WriteOnlyMutableCache,
                        )
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
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
''',
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt",
    '''                        "scan=${plan?.scanSourceKeys} reuse=${plan?.reuseSourceKeys} " +
                        "unavailable=${plan?.unavailableSourceKeys}]"
''',
    '''                        "scan=${plan?.scanSourceKeys} reuse=${plan?.reuseSourceKeys} " +
                        "unavailable=${plan?.unavailableSourceKeys} " +
                        "removed=${plan?.removedSourceKeys} enrichmentOnly=${plan?.enrichmentOnly}]"
''',
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt",
    '''                val retainedUnresolved = priorUnresolved - attemptedSourceKeys
''',
    '''                val retainedUnresolved =
                    priorUnresolved - attemptedSourceKeys - plan?.removedSourceKeys.orEmpty()
''',
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt",
    '''                            completeMetadata = resolvedProfile == MetadataProfile.FULL,
                        )
''',
    '''                            completeMetadata = resolvedProfile == MetadataProfile.FULL,
                            enrichmentOnly = plan?.enrichmentOnly == true,
                        )
''',
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt",
    '''                if (resolvedProfile == MetadataProfile.FULL) {
                    emitStartupReadinessState(StartupReadinessState.EnrichmentComplete)
                }
''',
    '''                if (resolvedProfile == MetadataProfile.FULL && result.enrichmentComplete) {
                    emitStartupReadinessState(StartupReadinessState.EnrichmentComplete)
                }
''',
)
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt",
    '''            cleanupCovers = metadataProfile == MetadataProfile.FULL,
''',
    '''            cleanupCovers =
                metadataProfile == MetadataProfile.FULL && scanPlan?.enrichmentOnly != true,
''',
)

# Musikr reloads committed authority whenever the in-flight graph is intentionally incomplete.
replace_once(
    "musikr/src/main/java/org/oxycblt/musikr/Musikr.kt",
    '''    val failedSources: Map<String, String>
        get() = emptyMap()

    /** Delete only resources proven expired by the successfully published generation. */
''',
    '''    val failedSources: Map<String, String>
        get() = emptyMap()

    /** Whether optional rich work observed every member of the committed base generation. */
    val enrichmentComplete: Boolean
        get() = true

    /** Delete only resources proven expired by the successfully published generation. */
''',
)
replace_range(
    "musikr/src/main/java/org/oxycblt/musikr/Musikr.kt",
    '''            val commit = if (plan != null) incremental?.commitScan() else null
''',
    '''        } catch (e: CancellationException) {
''',
    '''            val commit = if (plan != null) incremental?.commitScan() else null
            if (commit != null) {
                Log.d(
                    "Musikr",
                    "Committed ${commit.committedSources.size} source generation(s), " +
                        "${commit.changedRows} changed and ${commit.removedRows} removed rows",
                )
                if (SourceScanCommitPolicy.allAttemptedSourcesFailed(commit)) {
                    val hasPreservedRows = config.storage.cache.snapshot().any { it.audio != null }
                    if (
                        SourceScanCommitPolicy.rejectsAsAuthoritativeEmpty(
                            commit,
                            hasPreservedRows,
                        )
                    ) {
                        throw SourceScanFailureException(commit.failedSources)
                    }
                }
                if (
                    commit.enrichmentOnly ||
                        commit.removedSources.isNotEmpty() ||
                        commit.failedSources.isNotEmpty() ||
                        commit.unavailableSources.isNotEmpty()
                ) {
                    // Publish the durable graph, not the intentionally incomplete in-flight graph.
                    resultLibrary = Musikr.loadCached(context, config)
                }
            }
            Log.d("Musikr", "Indexing took ${System.currentTimeMillis() - start}ms")
            trace.mark(PipelineStage.PIPELINE_COMPLETED)
            LibraryResultImpl(
                config = config,
                library = resultLibrary,
                failedSources = commit?.failedSources.orEmpty(),
                enrichmentOnly = commit?.enrichmentOnly == true,
                enrichmentComplete = commit?.enrichmentComplete ?: true,
            )
''',
)
replace_once(
    "musikr/src/main/java/org/oxycblt/musikr/Musikr.kt",
    '''private class LibraryResultImpl(
    private val config: Config,
    override val library: MutableLibrary,
    override val failedSources: Map<String, String>,
) : LibraryResult {
    override suspend fun cleanup() {
        if (config.cleanupCovers) {
            config.storage.covers.cleanup(library.songs.mapNotNull { it.cover })
        }
    }
}
''',
    '''private class LibraryResultImpl(
    private val config: Config,
    override val library: MutableLibrary,
    override val failedSources: Map<String, String>,
    private val enrichmentOnly: Boolean,
    override val enrichmentComplete: Boolean,
) : LibraryResult {
    override suspend fun cleanup() {
        if (config.cleanupCovers && !enrichmentOnly) {
            config.storage.covers.cleanup(library.songs.mapNotNull { it.cover })
        }
    }
}
''',
)

# Destructive cleanup explicitly rejects optional enrichment.
replace_once(
    "app/src/main/java/org/oxycblt/auxio/music/CoverCleanupPolicy.kt",
    '''        unavailableSourceKeys: Set<String>,
        completeMetadata: Boolean,
    ): Decision =
        when {
            !published -> Decision(false, "no-new-generation-published")
''',
    '''        unavailableSourceKeys: Set<String>,
        completeMetadata: Boolean,
        enrichmentOnly: Boolean = false,
    ): Decision =
        when {
            !published -> Decision(false, "no-new-generation-published")
            enrichmentOnly -> Decision(false, "enrichment-does-not-own-cleanup")
''',
)
replace_once(
    "app/src/test/java/org/oxycblt/auxio/music/CoverCleanupPolicyTest.kt",
    '''    @Test
    fun `retained unresolved sources block cleanup`() {
''',
    '''    @Test
    fun `optional enrichment never owns destructive cleanup`() {
        assertFalse(evaluate(enrichmentOnly = true).allowed)
    }

    @Test
    fun `retained unresolved sources block cleanup`() {
''',
)
replace_once(
    "app/src/test/java/org/oxycblt/auxio/music/CoverCleanupPolicyTest.kt",
    '''        completeMetadata: Boolean = true,
    ) =
''',
    '''        completeMetadata: Boolean = true,
        enrichmentOnly: Boolean = false,
    ) =
''',
)
replace_once(
    "app/src/test/java/org/oxycblt/auxio/music/CoverCleanupPolicyTest.kt",
    '''            completeMetadata = completeMetadata,
        )
''',
    '''            completeMetadata = completeMetadata,
            enrichmentOnly = enrichmentOnly,
        )
''',
)

# Focused planner regressions.
replace_once(
    "app/src/test/java/org/oxycblt/auxio/music/IncrementalIndexPlannerTest.kt",
    '''class IncrementalIndexPlannerTest {
''',
    '''class IncrementalIndexPlannerTest {
    @Test
    fun noCacheRequestUsesAForcedSourceLedgerPlan() = runBlocking {
        val source =
            SourceSnapshot(
                sourceKey = "direct:a",
                sourceType = "DIRECT_FS",
                rootUri = "file:///storage/usbdisk0",
                rootPath = "/storage/usbdisk0",
                available = true,
                fingerprint = "a",
                fingerprintStrength = SourceFingerprintStrength.AUTHORITATIVE,
            )
        val original = FakeSourceAwareFs(snapshots = listOf(source))
        val cache = FakeIncrementalCache()
        var legacyFallbackUsed = false

        val prepared =
            IncrementalIndexPlanner.prepare(
                fs = original,
                cache = cache,
                withCache = false,
                profile = MetadataProfile.LEAN,
                configurationRevision = 1L,
                legacyWriteOnly = {
                    legacyFallbackUsed = true
                    it
                },
            )

        assertTrue(prepared.plan?.force == true)
        assertTrue(cache.plannedForce == true)
        assertTrue(!legacyFallbackUsed)
    }

    @Test
    fun explicitEmptyConfigurationMayProduceARemovalOnlyPlan() = runBlocking {
        val original = FakeSourceAwareFs()
        val cache = FakeIncrementalCache()

        val prepared =
            IncrementalIndexPlanner.prepare(
                fs = original,
                cache = cache,
                withCache = false,
                profile = MetadataProfile.LEAN,
                configurationRevision = 2L,
                targetSourceKeys = emptySet(),
                allowEmptySourceSet = true,
                legacyWriteOnly = { it },
            )

        assertEquals(emptySet<String>(), cache.plannedSnapshotKeys)
        assertTrue(prepared.plan != null)
        assertEquals(emptySet<String>(), original.selectedSourceKeys)
    }

''',
)
replace_once(
    "app/src/test/java/org/oxycblt/auxio/music/IncrementalIndexPlannerTest.kt",
    '''        var plannedSnapshots: List<SourceSnapshot>? = null
        val plannedSnapshotKeys: Set<String>?
''',
    '''        var plannedSnapshots: List<SourceSnapshot>? = null
        var plannedForce: Boolean? = null
        val plannedSnapshotKeys: Set<String>?
''',
)
replace_once(
    "app/src/test/java/org/oxycblt/auxio/music/IncrementalIndexPlannerTest.kt",
    '''            plannedSnapshots = snapshots
            return IncrementalScanPlan(
''',
    '''            plannedSnapshots = snapshots
            plannedForce = force
            return IncrementalScanPlan(
''',
)

# Replace obsolete removal/unmount expectations and add authority regressions.
replace_range(
    "musikr/src/test/java/org/oxycblt/musikr/cache/db/IncrementalScanStoreTest.kt",
    '''    @Test
    fun `removed configured source becomes unavailable without deleting its cache`() = runBlocking {
''',
    '''    @Test
    fun `temporary unmount never becomes deletion`() = runBlocking {
''',
    '''    @Test
    fun `removed source stays readable until the replacement commits`() = runBlocking {
        val usb0 = snapshot("usb0", "/storage/usbdisk0")
        val usb1 = snapshot("usb1-v1", "/storage/usbdisk1")
        val first = store.planScan(listOf(usb0, usb1), false, MetadataProfile.LEAN, 1L)
        store.beginScan(first)
        store.stage(cachedFile("alpha.mp3", 1L, "/storage/usbdisk0"))
        store.stage(cachedFile("beta.mp3", 1L, "/storage/usbdisk1"))
        store.commitScan()

        val replacementSource = usb1.copy(fingerprint = "usb1-v2")
        val failedPlan = store.planScan(listOf(replacementSource), false, MetadataProfile.LEAN, 2L)
        assertEquals(setOf(usb0.sourceKey), failedPlan.removedSourceKeys)
        assertTrue(db.incrementalDao().sourceLedger(usb0.sourceKey)?.available == true)
        assertEquals(2, store.compatibilityCachedFiles().toList().size)

        store.beginScan(failedPlan)
        store.markSourceFailed(usb1.sourceKey, "replacement failed")
        val failed = store.commitScan()
        assertTrue(failed.removedSources.isEmpty())
        assertTrue(db.incrementalDao().sourceLedger(usb0.sourceKey)?.available == true)
        assertEquals(2, store.compatibilityCachedFiles().toList().size)

        val successfulPlan = store.planScan(listOf(replacementSource), false, MetadataProfile.LEAN, 2L)
        store.beginScan(successfulPlan)
        store.stage(cachedFile("beta.mp3", 2L, "/storage/usbdisk1"))
        val successful = store.commitScan()

        assertEquals(setOf(usb0.sourceKey), successful.removedSources)
        assertFalse(db.incrementalDao().sourceLedger(usb0.sourceKey)?.available ?: true)
        assertEquals(
            listOf("file:///storage/usbdisk1/beta.mp3"),
            store.compatibilityCachedFiles().toList().map { it.file.uri.toString() },
        )
        assertEquals(2, db.readDao().selectAllSongs().size)
    }

    @Test
    fun `removal-only configuration commits deterministically`() = runBlocking {
        val source = snapshot("v1")
        val first = store.planScan(listOf(source), false, MetadataProfile.LEAN, 1L)
        store.beginScan(first)
        store.stage(cachedFile("alpha.mp3", 1L))
        store.commitScan()

        val removal = store.planScan(emptyList(), true, MetadataProfile.LEAN, 2L)
        assertTrue(removal.hasWork)
        assertEquals(setOf(source.sourceKey), removal.removedSourceKeys)
        store.beginScan(removal)
        val commit = store.commitScan()

        assertEquals(setOf(source.sourceKey), commit.removedSources)
        assertEquals(0, db.incrementalLibraryDao().songCount())
        assertEquals(1, db.readDao().selectAllSongs().size)
    }

''',
)
replace_range(
    "musikr/src/test/java/org/oxycblt/musikr/cache/db/IncrementalScanStoreTest.kt",
    '''    @Test
    fun `temporary unmount never becomes deletion`() = runBlocking {
''',
    '''    @Test
    fun `one source failure preserves its prior generation while sibling commits`() = runBlocking {
''',
    '''    @Test
    fun `temporary unmount preserves the committed generation as unresolved`() = runBlocking {
        val mounted = snapshot("v1")
        val first = store.planScan(listOf(mounted), false, MetadataProfile.LEAN, 1L)
        store.beginScan(first)
        store.stage(cachedFile("alpha.mp3", modifiedMs = 1L))
        store.commitScan()

        val absent = mounted.copy(available = false, fingerprint = null)
        val plan = store.planScan(listOf(absent), false, MetadataProfile.LEAN, 1L)

        assertFalse(plan.hasWork)
        assertEquals(setOf(mounted.sourceKey), plan.unavailableSourceKeys)
        assertEquals(setOf(mounted.sourceKey), plan.reuseSourceKeys)
        assertTrue(db.incrementalDao().sourceLedger(mounted.sourceKey)?.available == true)
        assertEquals(1, store.compatibilityCachedFiles().toList().size)
    }

    @Test
    fun `forced scan uses pending generation and abort preserves committed rows`() = runBlocking {
        val source = snapshot("v1")
        val first = store.planScan(listOf(source), false, MetadataProfile.LEAN, 1L)
        store.beginScan(first)
        store.stage(cachedFile("alpha.mp3", 1L))
        store.commitScan()

        val forced = store.planScan(listOf(source.copy(fingerprint = "v2")), true, MetadataProfile.LEAN, 2L)
        store.beginScan(forced)
        val inFlight = requireNotNull(db.incrementalDao().sourceLedger(source.sourceKey))
        assertEquals(1L, inFlight.lastCommittedGeneration)
        assertEquals(2L, inFlight.pendingGeneration)
        assertTrue(inFlight.incomplete)
        store.abortScan(IllegalStateException("failed first configuration"))

        val retained = requireNotNull(db.incrementalDao().sourceLedger(source.sourceKey))
        assertEquals(1L, retained.lastCommittedGeneration)
        assertNull(retained.pendingGeneration)
        assertEquals(
            1L,
            db.readDao().selectSongByUri(Uri.parse("file:///storage/usbdisk0/alpha.mp3"))?.modifiedMs,
        )
    }

    @Test
    fun `metadata enrichment updates profile without owning source generation`() = runBlocking {
        val source = snapshot("v1")
        val first = store.planScan(listOf(source), false, MetadataProfile.LEAN, 1L)
        store.beginScan(first)
        store.stage(cachedFile("alpha.mp3", 1L))
        store.commitScan()
        val before = requireNotNull(db.incrementalDao().sourceLedger(source.sourceKey))

        val enrichment = store.planScan(listOf(source), false, MetadataProfile.FULL, 1L)
        assertTrue(enrichment.enrichmentOnly)
        store.beginScan(enrichment)
        val during = requireNotNull(db.incrementalDao().sourceLedger(source.sourceKey))
        assertEquals(before.lastCommittedGeneration, during.lastCommittedGeneration)
        assertEquals(before.pendingGeneration, during.pendingGeneration)
        assertEquals(before.incomplete, during.incomplete)
        store.stage(cachedFile("alpha.mp3", 1L))
        val commit = store.commitScan()
        val after = requireNotNull(db.incrementalDao().sourceLedger(source.sourceKey))

        assertTrue(commit.enrichmentOnly)
        assertTrue(commit.enrichmentComplete)
        assertEquals(before.lastCommittedGeneration, after.lastCommittedGeneration)
        assertEquals(before.fingerprint, after.fingerprint)
        assertEquals(before.configurationRevision, after.configurationRevision)
        assertEquals(MetadataProfile.FULL.name, after.committedProfile)
        assertEquals(1, db.incrementalLibraryDao().songCount())
    }

    @Test
    fun `enrichment abort and failure preserve base authority`() = runBlocking {
        val source = snapshot("v1")
        val first = store.planScan(listOf(source), false, MetadataProfile.LEAN, 1L)
        store.beginScan(first)
        store.stage(cachedFile("alpha.mp3", 1L))
        store.commitScan()
        val before = requireNotNull(db.incrementalDao().sourceLedger(source.sourceKey))

        val cancelled = store.planScan(listOf(source), false, MetadataProfile.FULL, 1L)
        store.beginScan(cancelled)
        store.stage(cachedFile("alpha.mp3", 2L))
        store.abortScan(CancellationException("optional work stopped"))
        val afterCancel = requireNotNull(db.incrementalDao().sourceLedger(source.sourceKey))
        assertEquals(before.lastCommittedGeneration, afterCancel.lastCommittedGeneration)
        assertEquals(before.pendingGeneration, afterCancel.pendingGeneration)
        assertEquals(before.incomplete, afterCancel.incomplete)
        assertEquals(before.fingerprint, afterCancel.fingerprint)

        val failed = store.planScan(listOf(source), false, MetadataProfile.FULL, 1L)
        store.beginScan(failed)
        store.markSourceFailed(source.sourceKey, "rich metadata unavailable")
        val failureCommit = store.commitScan()
        val afterFailure = requireNotNull(db.incrementalDao().sourceLedger(source.sourceKey))
        assertFalse(failureCommit.enrichmentComplete)
        assertEquals(before.lastCommittedGeneration, afterFailure.lastCommittedGeneration)
        assertEquals(before.pendingGeneration, afterFailure.pendingGeneration)
        assertEquals(before.incomplete, afterFailure.incomplete)
        assertEquals(before.fingerprint, afterFailure.fingerprint)
        assertEquals(1, db.incrementalLibraryDao().songCount())
    }

    @Test
    fun `enrichment cannot add or remove committed membership`() = runBlocking {
        val source = snapshot("v1")
        val first = store.planScan(listOf(source), false, MetadataProfile.LEAN, 1L)
        store.beginScan(first)
        store.stage(cachedFile("alpha.mp3", 1L))
        store.commitScan()

        val enrichment = store.planScan(listOf(source), false, MetadataProfile.FULL, 1L)
        store.beginScan(enrichment)
        assertTrue(store.stage(cachedFile("beta.mp3", 1L)))
        val commit = store.commitScan()

        assertFalse(commit.enrichmentComplete)
        assertEquals(
            listOf("file:///storage/usbdisk0/alpha.mp3"),
            store.compatibilityCachedFiles().toList().map { it.file.uri.toString() },
        )
        assertNull(db.readDao().selectSongByUri(Uri.parse("file:///storage/usbdisk0/beta.mp3")))
        assertEquals(
            MetadataProfile.LEAN.name,
            db.incrementalDao().sourceLedger(source.sourceKey)?.committedProfile,
        )
    }

''',
)

print("PR #232 authority patch applied")
